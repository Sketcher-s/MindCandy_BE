package shop.catchmind.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GptResponse(
        String id,
        String object,
        long created,
        String model,
        Usage usage,
        List<Choice> choices
) {

    public record Usage(
            @JsonProperty("prompt_tokens")
            int promptTokens,
            @JsonProperty("completion_tokens")
            int completionTokens,
            @JsonProperty("total_tokens")
            int totalTokens
    ) {
    }

    public record Choice(
            GptMessage message,
            @JsonProperty("finish_reason")
            String finishReason,
            int index
    ) {
    }
}
