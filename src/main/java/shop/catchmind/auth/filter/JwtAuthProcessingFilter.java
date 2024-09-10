package shop.catchmind.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.catchmind.auth.Role;
import shop.catchmind.auth.dto.AuthenticationDto;
import shop.catchmind.auth.provider.JwtProvider;
import shop.catchmind.auth.provider.RedisProvider;
import shop.catchmind.member.domain.Member;
import shop.catchmind.member.repository.MemberRepository;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthProcessingFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisProvider redisProvider;
    private final MemberRepository memberRepository;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        jwtProvider.extractAccessToken(request)
                .filter(jwtProvider::isTokenValid)
                .filter(redisProvider::isNotTokenBlacklisted)
                .flatMap(accessToken -> jwtProvider.extractId(accessToken)
                        .flatMap(memberRepository::findById)).ifPresent(this::saveAuthentication);

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(final Member myMember) {
        AuthenticationDto authInfoDto = AuthenticationDto.builder()
                .id(myMember.getId())
                .email(myMember.getEmail())
                .authorities(
                        Set.of(Role.USER).stream()
                                .map(Role::getAuthority)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toUnmodifiableSet()))
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(authInfoDto, null,
                        authoritiesMapper.mapAuthorities(authInfoDto.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
