package shop.catchmind.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.catchmind.auth.dto.AuthenticationDto;
import shop.catchmind.member.application.MemberService;
import shop.catchmind.member.dto.GetPictureListResponse;

@Tag(name = "Member API", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원 탈퇴",
            description = "회원을 탈퇴 처리 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "유저 탈퇴 실패")
    })
    @DeleteMapping("/leave")
    public ResponseEntity<Void> inactiveCurrentUser(
            @AuthenticationPrincipal final AuthenticationDto auth
    ) {
        memberService.leaveMember(auth.id());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "마이 페이지 조회",
            description = "마이페이지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이 페이지 조회 성공"),
            @ApiResponse(responseCode = "400", description = "마이 페이지 조회 실패")
    })
    @GetMapping
    public ResponseEntity<GetPictureListResponse> inactiveCurrentUser(
            @AuthenticationPrincipal final AuthenticationDto auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(memberService.getMyPage(auth.id(), page, size));
    }
}
