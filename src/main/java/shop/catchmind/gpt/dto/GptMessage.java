package shop.catchmind.gpt.dto;

import lombok.Builder;

@Builder
public record GptMessage(
        String role,
        Object content
) {
    public static GptMessage of(final String role, final Object content) {
        return GptMessage.builder()
                .role(role)
                .content(content)
                .build();
    }
}
