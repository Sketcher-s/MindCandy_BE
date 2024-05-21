package shop.catchmind.common.presentation;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public String health() {
        return "OK!";
    }
}
