package shop.catchmind.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.catchmind.auth.application.AuthService;
import shop.catchmind.auth.dto.request.IsExistedEmailRequest;
import shop.catchmind.auth.dto.request.SignUpRequest;
import shop.catchmind.auth.dto.response.IsExistedEmailResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(
            @RequestBody final SignUpRequest request
    ) {
        authService.signUp(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/email")
    public ResponseEntity<IsExistedEmailResponse> isValidEmail(
            @RequestBody final IsExistedEmailRequest request
    ) {
        return ResponseEntity.ok(authService.isExistedEmail(request));
    }
}
