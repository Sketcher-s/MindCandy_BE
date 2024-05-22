package shop.catchmind.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.catchmind.auth.application.AuthService;
import shop.catchmind.auth.dto.request.SignUpRequest;
import shop.catchmind.auth.dto.response.IsExistedEmailResponse;

@Tag(name = "Auth API", description = "인증/인가 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    @Operation(
            summary = "회원가입",
            description = "이름, 이메일, 비밀번호를 받아 회원가입을 진행합니다."
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "회원가입 성공입니다.")
    })
    public ResponseEntity<Object> signUp(
            @RequestBody @Valid final SignUpRequest request
    ) {
        authService.signUp(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{email}")
    @Operation(
            summary = "이메일 중복 검사",
            description = "이메일을 받아 중복 여부를 검사합니다."
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "isExisted = true의 경우, 중복 이메일이 존재합니다." +
                    " isExisted = false의 경우, 동일한 이메일이 없습니다.")
    })
    public ResponseEntity<IsExistedEmailResponse> isValidEmail(
            @PathVariable final String email
    ) {
        return ResponseEntity.ok(authService.isExistedEmail(email));
    }
}
