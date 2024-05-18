package shop.catchmind.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private final String securitySchemeName = "bearerAuth";

    @Bean
    public OpenAPI openAPI(@Value("OpenAPI") String appVersion) {
        Server prodServer = new Server().url("https://dev.catchmind.shop").description("운영 서버");
        Server localServer = new Server().url("http://localhost:8080").description("로컬 서버");
        Info info = new Info().title("CatchMind API").version(appVersion)
                .description("CatchMind API 입니다.")
                .termsOfService("https://dev.catchmind.shop")
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
                .servers(List.of(prodServer));
    }

}
