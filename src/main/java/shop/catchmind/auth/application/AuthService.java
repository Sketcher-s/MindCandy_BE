package shop.catchmind.auth.application;

import shop.catchmind.auth.dto.request.IsExistedEmailRequest;
import shop.catchmind.auth.dto.request.SignUpRequest;
import shop.catchmind.auth.dto.response.IsExistedEmailResponse;

public interface AuthService {
    void signUp(SignUpRequest request);

    IsExistedEmailResponse isExistedEmail(IsExistedEmailRequest request);
}
