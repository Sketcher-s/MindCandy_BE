package shop.catchmind.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import shop.catchmind.auth.filter.CustomJsonAuthenticationFilter;

import java.util.List;
import java.util.Optional;

import static shop.catchmind.auth.constant.AuthProcessingConstant.*;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ApplicationContext applicationContext;
    private final String securitySchemeName = "bearerAuth";

    @Bean
    public OpenAPI openAPI(@Value("OpenAPI") String appVersion, SecurityConfig securityConfig) {
        Server prodServer = new Server().url(securityConfig.getBackEndUrl()).description("운영 서버");
        Server localServer = new Server().url("http://localhost:8080").description("로컬 서버");
        Info info = new Info().title("CatchMind API").version(appVersion)
                .description("CatchMind API 입니다.")
                .termsOfService(securityConfig.getBackEndUrl())
                .contact(new Contact().name("CatchMind").email("sketchersMJU@gmail.com"))
                .license(new License().name("Apache License Version 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0"));

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(info)
                .servers(List.of(prodServer, localServer));
    }

    @Bean
    public OpenApiCustomizer springSecurityLoginEndpointCustomizer() {
        FilterChainProxy filterChainProxy = applicationContext.getBean(
                AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME,
                FilterChainProxy.class);
        return openAPI -> {
            for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {
                Optional<CustomJsonAuthenticationFilter> optionalFilter =
                        filterChain.getFilters().stream()
                                .filter(CustomJsonAuthenticationFilter.class::isInstance)
                                .map(CustomJsonAuthenticationFilter.class::cast)
                                .findAny();
                if (optionalFilter.isPresent()) {
                    CustomJsonAuthenticationFilter filter = optionalFilter.get();
                    Operation operation = new Operation();
                    Schema<?> schema = new ObjectSchema()
                            .addProperty(USERNAME_KEY, new StringSchema())
                            .addProperty(PASSWORD_KEY, new StringSchema());
                    RequestBody requestBody = new RequestBody().content(
                            new Content().addMediaType(
                                    org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                    new MediaType().schema(schema)));
                    operation.requestBody(requestBody);
                    ApiResponses apiResponses = new ApiResponses();
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()),
                            new ApiResponse().description(HttpStatus.OK.getReasonPhrase()));
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                            new ApiResponse().description(HttpStatus.BAD_REQUEST.getReasonPhrase()));
                    operation.responses(apiResponses);
                    operation.addTagsItem("Auth API");
                    operation.summary("로그인").description("아이디와 비밀번호를 받아 로그인합니다.");
                    PathItem pathItem = new PathItem().post(operation);
                    openAPI.getPaths().addPathItem(DEFAULT_LOGIN_REQUEST_URL, pathItem);
                }
            }
        };
    }

}
