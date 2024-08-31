package shop.catchmind.picture.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import shop.catchmind.global.s3.MultipartInputStreamFileResource;
import shop.catchmind.global.s3.S3Provider;
import shop.catchmind.gpt.application.GptService;
import shop.catchmind.gpt.dto.InterpretDto;
import shop.catchmind.gpt.dto.NaturalLanguageDto;
import shop.catchmind.picture.domain.Picture;
import shop.catchmind.picture.dto.PictureDto;
import shop.catchmind.picture.dto.request.UpdateTitleRequest;
import shop.catchmind.picture.dto.response.InterpretResponse;
import shop.catchmind.picture.dto.response.GetPictureResponse;
import shop.catchmind.picture.exception.PictureNotFoundException;
import shop.catchmind.picture.exception.UnmatchedMemberPictureException;
import shop.catchmind.picture.repository.PictureRepository;

import java.io.IOException;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;
    private final GptService gptService;
    private final S3Provider s3Provider;
    private final RestTemplate restTemplate;

    @Value("${url.ai}")
    private String flaskServerUrl;

    @Transactional
    public InterpretResponse inspect(final Long authId, final MultipartFile file) {
        String imageUrl = s3Provider.uploadFile(s3Provider.generateFilesKeyName(), file);

        InterpretDto result = serveToFlask(file);

        Picture picture = pictureRepository.save(
                Picture.builder()
                        .imageUrl(imageUrl)
                        .result(removeNumbersInParentheses(Objects.requireNonNull(result).data()))
                        .memberId(authId)
                        .build()
        );

        return InterpretResponse.of(PictureDto.of(picture));
    }

    public GetPictureResponse getPicture(final Long authId, final Long pictureId) {
        Picture picture = pictureRepository.findById(pictureId)
                .orElseThrow(PictureNotFoundException::new);

        isOwnerOfPicture(authId, picture);

        return GetPictureResponse.of(PictureDto.of(picture));
    }

    @Transactional
    public void updateTitle(final Long authId, final UpdateTitleRequest request) {
        Picture picture = pictureRepository.findById(request.id())
                .orElseThrow(PictureNotFoundException::new);

        isOwnerOfPicture(authId, picture);

        picture.updateTitle(request.title());
    }

    // Flask 서버 통신
    private InterpretDto serveToFlask(MultipartFile file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl, requestEntity, String.class);
        if (Objects.equals(response.getBody(), " ")) {
            return null;
        }

        return gptService.interpretPicture(NaturalLanguageDto.of(response.getBody()));
    }

    // 정규 표현식을 사용하여 "(숫자, 문자)" 패턴을 제거
    private String removeNumbersInParentheses(final String input) {
        return input.replaceAll("\\(.*?\\)", "");
    }

    // 요청한 ID를 가진 그림 검사 결과가 요청한 유저의 검사인지 확인하는 메서드
    private void isOwnerOfPicture(final Long authId, final Picture picture) {
        if (!picture.getMemberId().equals(authId)) {
            throw new UnmatchedMemberPictureException();
        }
    }

}
