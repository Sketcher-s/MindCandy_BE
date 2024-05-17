package shop.catchmind.gpt.dto;

import lombok.Builder;

@Builder
public record GptMessage(
        String role,
        String content
){}
