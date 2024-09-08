package shop.catchmind.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.catchmind.auth.application.AuthService;
import shop.catchmind.auth.dto.request.SignUpRequest;
import shop.catchmind.auth.dto.response.IsExistedEmailResponse;

import java.util.Objects;

@Tag(name = "Auth API", description = "인증/인가 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "이름, 이메일, 비밀번호를 받아 회원가입을 진행합니다."
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "회원가입 성공입니다."),
            @ApiResponse(responseCode = "400", description = "회원가입 실패입니다.")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(
            @RequestBody @Valid final SignUpRequest request
    ) {
        authService.signUp(request);
        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 진행합니다."
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "로그아웃 성공입니다."),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패입니다.")
    })
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            @RequestHeader @Valid final HttpHeaders header
            ) {
        authService.logout(Objects.requireNonNull(header.getFirst("Authorization")));
        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "이메일 중복 검사",
            description = "이메일을 받아 중복 여부를 검사합니다."
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "isExisted = true의 경우, 중복 이메일이 존재합니다." +
                    " isExisted = false의 경우, 동일한 이메일이 없습니다."),
            @ApiResponse(responseCode = "400", description = "이메일 중복 검사에 실패했습니다.")
    })
    @GetMapping("/{email}")
    public ResponseEntity<IsExistedEmailResponse> isValidEmail(
            @PathVariable final String email
    ) {
        return ResponseEntity.ok(authService.isExistedEmail(email));
    }
}
