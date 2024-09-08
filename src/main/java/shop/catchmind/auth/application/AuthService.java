package shop.catchmind.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.catchmind.auth.dto.request.SignUpRequest;
import shop.catchmind.auth.dto.response.IsExistedEmailResponse;
import shop.catchmind.auth.provider.JwtProvider;
import shop.catchmind.member.domain.Member;
import shop.catchmind.member.repository.MemberRepository;

import java.util.concurrent.TimeUnit;
import static shop.catchmind.auth.constant.JwtConstant.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signUp(final SignUpRequest request) {
        Member member = request.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    public IsExistedEmailResponse isExistedEmail(final String email) {
        return IsExistedEmailResponse.builder()
                .isExisted(memberRepository.existsByEmail(email))
                .build();
    }

    public void logout(final String bearerToken) {
        String accessToken = bearerToken.substring(7);
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();

        long remainingExpirationTime = jwtProvider.getRemainingExpirationTime(accessToken);

        stringValueOperations.set(REDIS_LOGOUT_KEY, accessToken, remainingExpirationTime, TimeUnit.SECONDS);
    }
}
