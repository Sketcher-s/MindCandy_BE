package shop.catchmind.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.catchmind.member.domain.Member;

import static shop.catchmind.auth.constant.AuthErrorConstant.*;

public record SignUpRequest(

        @Size(min = 2, max = 6, message = NAME_SIZE)
        @NotBlank(message = NAME_NOT_BLANK)
        String name,

        @NotBlank(message = EMAIL_NOT_BLANK)
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n", message = EMAIL_EMAIL_FORM)
        String email,

        @Size(min = 8, max = 12, message = PASSWORD_SIZE)
        @NotBlank(message = PASSWORD_NOT_BLANK)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$", message = PASSWORD_PATTERN)
        String password
) {

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
