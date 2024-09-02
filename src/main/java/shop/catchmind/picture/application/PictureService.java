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
import shop.catchmind.picture.domain.PictureType;
import shop.catchmind.picture.domain.Result;
import shop.catchmind.picture.dto.PictureDto;
import shop.catchmind.picture.dto.PictureRequestDto;
import shop.catchmind.picture.dto.request.RecognizeRequest;
import shop.catchmind.picture.dto.request.UpdateTitleRequest;
import shop.catchmind.picture.dto.response.GetPictureResponse;
import shop.catchmind.picture.dto.response.InterpretResponse;
import shop.catchmind.picture.dto.response.RecognitionResultResponse;
import shop.catchmind.picture.exception.ResultNotFoundException;
import shop.catchmind.picture.exception.UnmatchedMemberResultException;
import shop.catchmind.picture.repository.PictureRepository;
import shop.catchmind.picture.repository.ResultRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static shop.catchmind.picture.constant.FlaskConstant.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;
    private final GptService gptService;
    private final S3Provider s3Provider;
    private final RestTemplate restTemplate;
    private final ResultRepository resultRepository;

    @Value("${url.ai}")
    private String flaskServerUrl;

    @Transactional
    public RecognitionResultResponse recognizeObject(final MultipartFile file, final RecognizeRequest request) {
        NaturalLanguageDto result = serveToFlask(file, request);
        return RecognitionResultResponse.of(request.pictureType(), result.value());
    }

    @Transactional
    public InterpretResponse inspect(final Long authId, final List<PictureRequestDto> request) {
        Result result = resultRepository.save(Result.builder()
                .memberId(authId)
                .build());

        List<Picture> pictureList = request.stream()
                .map(pictureRequestDto -> {
                    String imageUrl = s3Provider.uploadFile(s3Provider.generateFilesKeyName(), pictureRequestDto.image());
                    if (pictureRequestDto.pictureType() == PictureType.TREE) {
                        result.updateReplaceImageUrl(imageUrl);
                    }
                    InterpretDto interpretDto = gptService.interpretPicture(NaturalLanguageDto.of(pictureRequestDto.value()));  // TODO: 그림 별 모델 파라미터 추가 적용 필요
                    Picture picture = pictureRepository.save(Picture.builder()
                            .imageUrl(imageUrl)
                            .content(removeNumbersInParentheses(interpretDto.data()))
                            .pictureType(pictureRequestDto.pictureType())
                            .build());
                    picture.setResult(result);
                    return picture;
                }).toList();

        List<PictureDto> pictureDtoList = pictureList.stream().map(PictureDto::of).toList();

//        result.updateContent(gptService.interpretGeneral(pictureDtoList).getData()); // TODO: GptService 종합 도출 프롬프팅 설계 필요

        return InterpretResponse.of(pictureList, result);
    }

    public GetPictureResponse getResult(final Long authId, final Long resultId) {
        Result result = resultRepository.findById(resultId)
                .orElseThrow(ResultNotFoundException::new);
        isOwnerOfResult(authId, result);
        List<Picture> pictureList = pictureRepository.findAllByResultId(resultId);
        return GetPictureResponse.of(pictureList, result);
    }

    @Transactional
    public void updateTitle(final Long authId, final UpdateTitleRequest request) {
        Result result = resultRepository.findById(request.resultId())
                .orElseThrow(ResultNotFoundException::new);
        isOwnerOfResult(authId, result);
        result.updateTitle(request.title());
    }

    // Flask 서버 통신
    private NaturalLanguageDto serveToFlask(final MultipartFile file, final RecognizeRequest request) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            body.add(FILE_REQUEST, new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            body.add(MODEL_TYPE_REQUEST, request.pictureType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl, requestEntity, String.class);
        if (Objects.equals(response.getBody(), " ")) {
            throw new RuntimeException("객체 인식에 오류가 있습니다.");
        }

        return NaturalLanguageDto.of(response.getBody());
    }

    // 정규 표현식을 사용하여 "(숫자, 문자)" 패턴을 제거
    private String removeNumbersInParentheses(final String input) {
        return input.replaceAll(NUMBER_CHARACTER_PATTERN, "");
    }

    // 요청한 ID를 가진 그림 검사 결과가 요청한 유저의 결과인지 확인하는 메서드
    private void isOwnerOfResult(final Long authId, final Result result) {
        if (!result.getMemberId().equals(authId)) {
            throw new UnmatchedMemberResultException();
        }
    }

}
