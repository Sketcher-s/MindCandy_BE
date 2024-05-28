package shop.catchmind.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import shop.catchmind.auth.dto.AuthenticationDto;
import shop.catchmind.auth.provider.JwtProvider;
import shop.catchmind.member.domain.Member;
import shop.catchmind.member.exception.InvalidUserException;
import shop.catchmind.member.repository.MemberRepository;

import java.io.IOException;

import static shop.catchmind.auth.constant.AuthProcessingConstant.CHARACTER_ENCODING;
import static shop.catchmind.auth.constant.AuthProcessingConstant.CONTENT_TYPE;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
        Long userId = extractId(authentication);
        String accessToken = jwtProvider.createAccessToken(userId);

        jwtProvider.sendAccessToken(response, accessToken);

        Member member = memberRepository.findById(userId).orElseThrow(InvalidUserException::new);

        memberRepository.saveAndFlush(member);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);
    }

    private Long extractId(final Authentication authentication) {
        AuthenticationDto auth = (AuthenticationDto) authentication.getPrincipal();
        return auth.id();
    }
}