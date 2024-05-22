package shop.catchmind.gpt.presentation;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.catchmind.gpt.application.GptService;
import shop.catchmind.gpt.dto.NaturalLanguageDto;

@Hidden
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;

    @PostMapping
    public ResponseEntity<?> interpretPicture(@RequestBody final NaturalLanguageDto dto) {
        return ResponseEntity.ok(gptService.interpretPicture(dto));
    }
}
