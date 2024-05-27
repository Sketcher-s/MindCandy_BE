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
import shop.catchmind.picture.dto.response.InterpretResponse;
import shop.catchmind.picture.dto.response.PictureResponse;
import shop.catchmind.picture.repository.PictureRepository;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;
    private final GptService gptService;
    private final S3Provider s3Provider;
    private final RestTemplate restTemplate;

    @Value("${url.ai}")
    private String flaskServerUrl;

    public InterpretResponse inspect(final Long authId, final MultipartFile file) {
        String imageUrl = s3Provider.uploadFile(s3Provider.generateFilesKeyName(), file);
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

        InterpretDto result = gptService.interpretPicture(NaturalLanguageDto.of(response.getBody()));

        Picture picture = pictureRepository.save(
                Picture.builder()
                        .title("이름을 지어주세요.")
                        .imageUrl(imageUrl)
                        .result(removeNumbersInParentheses(result.data()))
                        .memberId(authId)
                        .build()
        );

        return InterpretResponse.of(PictureDto.of(picture));
    }

    @Transactional(readOnly = true)
    public PictureResponse getPicture(final Long authId, final Long pictureId) {
        Picture picture = pictureRepository.findById(pictureId)
                .orElseThrow(RuntimeException::new);    // TODO: Exception 처리 필요

        if (!picture.getMemberId().equals(authId)) {
            throw new RuntimeException(); // TODO: Exception 처리 필요
        }

        return PictureResponse.of(PictureDto.of(picture));
    }

    // 정규 표현식을 사용하여 "(숫자)" 패턴을 제거
    private String removeNumbersInParentheses(final String input) {
        return input.replaceAll("\\(\\d+\\)", "");
    }
}
