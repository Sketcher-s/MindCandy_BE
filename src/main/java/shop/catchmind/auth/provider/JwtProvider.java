package shop.catchmind.auth.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.catchmind.member.repository.MemberRepository;

import java.util.Date;
import java.util.Optional;

import static shop.catchmind.auth.constant.JwtConstant.*;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret-key}")
    public String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    private final MemberRepository memberRepository;

    public String createAccessToken(final Long userId) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(ID_CLAIM, userId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessToken(final HttpServletResponse response, final String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
    }

    public Optional<String> extractAccessToken(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER_CLAIM + " "))
                .map(accessToken -> accessToken.replace(BEARER_CLAIM + " ", ""));
    }

    private void setAccessTokenHeader(final HttpServletResponse response, final String accessToken) {
        response.setHeader(accessHeader,accessToken);
    }

    public Optional<Long> extractId(final String token) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token)
                    .getClaim(ID_CLAIM)
                    .asLong()
            );
        } catch (Exception e) {
            log.error("AccessToken이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public boolean isTokenValid(final String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

}
