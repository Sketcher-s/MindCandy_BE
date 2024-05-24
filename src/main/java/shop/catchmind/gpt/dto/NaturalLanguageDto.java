package shop.catchmind.gpt.dto;

import jakarta.persistence.Lob;
import lombok.Builder;

@Builder
public record NaturalLanguageDto(
        @Lob
        String value
) {
    public static NaturalLanguageDto of(String value) {
        return NaturalLanguageDto.builder()
                .value(value)
                .build();
    }
}
