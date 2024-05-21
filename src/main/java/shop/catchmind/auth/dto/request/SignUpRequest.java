package shop.catchmind.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.catchmind.member.domain.Member;

public record SignUpRequest(

        @Size(min = 2, max = 6)
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @Size(min = 8, max = 12)
        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$")
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
