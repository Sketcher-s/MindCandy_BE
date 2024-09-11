package shop.catchmind.picture.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.catchmind.auth.dto.AuthenticationDto;
import shop.catchmind.picture.application.PictureService;
import shop.catchmind.picture.dto.request.InspectRequest;
import shop.catchmind.picture.dto.request.RecognizeRequest;
import shop.catchmind.picture.dto.request.UpdateTitleRequest;
import shop.catchmind.picture.dto.response.GetPictureResponse;
import shop.catchmind.picture.dto.response.InterpretResponse;
import shop.catchmind.picture.dto.response.RecognitionResultResponse;

import java.util.List;

@Tag(name = "Picture API", description = "그림 검사 관련 API")
@RestController
@RequestMapping("/api/picture")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @Operation(
            summary = "그림 객체 인식",
            description = "요청받은 이미지에 대한 객체 인식을 진행하여, 자연어 처리된 결과를 도출합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "객체 인식이 완료되었습니다.")
    })
    @PostMapping(value = "/recognition", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecognitionResultResponse> recognizeObject(
            @RequestPart(value = "file") final MultipartFile image,
            @RequestPart(value = "pictureType") final RecognizeRequest recognizeRequest
    ) {
        return ResponseEntity.ok(pictureService.recognizeObject(image, recognizeRequest));
    }

    @Operation(
            summary = "그림 분석",
            description = "요청받은 이미지에 대한 심리 분석 결과를 제공합니다. " +
                    "/n(반드시 이미지, 이미지에 대한 정보의 순서를 매칭해서 보내주세요!!)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "분석이 완료되었습니다.")
    })
    @PostMapping(value = "/analysis",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InterpretResponse> inspectPicture(
            @AuthenticationPrincipal final AuthenticationDto auth,
            @RequestPart(value = "fileList") final List<MultipartFile> imageList,
            @RequestPart(value = "inspectRequest") final InspectRequest inspectRequest
    ) {
        return ResponseEntity.ok(pictureService.inspect(auth.id(), imageList, inspectRequest));
    }

    @Operation(
            summary = "그림 검사 결과 상세 조회",
            description = "요청한 ID의 그림 검사 결과의 상세 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그림 검사 결과 상세 정보 조회에 성공했습니다.")
    })
    @GetMapping("/{resultId}")
    public ResponseEntity<GetPictureResponse> getPicture(
            @AuthenticationPrincipal final AuthenticationDto auth,
            @PathVariable final Long resultId
    ) {
        return ResponseEntity.ok(pictureService.getResult(auth.id(), resultId));
    }

    @Operation(
            summary = "그림 검사 제목 수정",
            description = "요청한 ID의 그림 검사 결과의 제목을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그림 검사 결과의 제목 수정에 성공했습니다.")
    })
    @PatchMapping("/title")
    public ResponseEntity<?> updateTitle(
            @AuthenticationPrincipal final AuthenticationDto auth,
            @RequestBody @Valid final UpdateTitleRequest request
    ) {
        pictureService.updateTitle(auth.id(), request);
        return ResponseEntity.ok().build();
    }

}
