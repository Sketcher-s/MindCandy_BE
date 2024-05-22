package shop.catchmind.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.catchmind.auth.dto.request.SignUpRequest;
import shop.catchmind.auth.dto.response.IsExistedEmailResponse;
import shop.catchmind.member.domain.Member;
import shop.catchmind.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(final SignUpRequest request) {
        Member member = request.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    public IsExistedEmailResponse isExistedEmail(String email) {
        return IsExistedEmailResponse.builder()
                .isExisted(memberRepository.existsByEmail(email))
                .build();

    }
}
