package shop.catchmind.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.catchmind.auth.application.LoginService;
import shop.catchmind.auth.filter.CustomJsonAuthenticationFilter;
import shop.catchmind.auth.filter.JwtAuthProcessingFilter;
import shop.catchmind.auth.handler.LoginFailureHandler;
import shop.catchmind.auth.handler.LoginSuccessHandler;
import shop.catchmind.auth.provider.JwtProvider;
import shop.catchmind.auth.provider.RedisProvider;
import shop.catchmind.member.repository.MemberRepository;

import java.util.Arrays;
import java.util.List;

@Getter
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final RedisProvider redisProvider;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Value("${url.be}")
    private String backEndUrl;

    @Value("${url.fe}")
    private String frontEndUrl;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(apiConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**",
                                        "/health", "/health/**", "/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterAfter(customJsonAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthProcessingFilter(), CustomJsonAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtProvider, memberRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    @Bean
    public CustomJsonAuthenticationFilter customJsonAuthenticationFilter() {
        CustomJsonAuthenticationFilter filter = new CustomJsonAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        return filter;
    }

    @Bean
    public JwtAuthProcessingFilter jwtAuthProcessingFilter() {
        return new JwtAuthProcessingFilter(jwtProvider, redisProvider, memberRepository);
    }

    @Bean
    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080", frontEndUrl, backEndUrl, "https://d1xhtcoobbelk9.cloudfront.net/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of(accessHeader));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
