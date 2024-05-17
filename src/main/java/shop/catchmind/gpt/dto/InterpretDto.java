package shop.catchmind.gpt.dto;

import jakarta.persistence.Lob;
import lombok.Builder;

@Builder
public record InterpretDto(
        @Lob
        String data
){ }
