package shop.catchmind.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record IsExistedEmailRequest(
        @NotBlank
        @Email
        String email
) {
}

