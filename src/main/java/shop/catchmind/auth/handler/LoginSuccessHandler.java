package shop.catchmind.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import shop.catchmind.auth.provider.JwtProvider;
import shop.catchmind.member.domain.Member;
import shop.catchmind.member.repository.MemberRepository;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        String email = extractEmail(authentication);
        String accessToken = jwtProvider.createAccessToken(email);

        jwtProvider.sendAccessToken(response, accessToken);

        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);     // TODO: Exception 구현 필요

        memberRepository.saveAndFlush(member);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }

    private String extractEmail(final Authentication authentication) {
        UserDetails auth = (UserDetails) authentication.getPrincipal();
        return auth.getUsername();
    }
}