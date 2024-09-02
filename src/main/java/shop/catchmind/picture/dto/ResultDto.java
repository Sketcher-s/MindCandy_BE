package shop.catchmind.picture.dto;

import lombok.Builder;
import shop.catchmind.picture.domain.Result;

import java.time.LocalDateTime;

@Builder
public record ResultDto(
        Long id,
        Long memberId,
        String title,
        String replaceImageUrl,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ResultDto from(final Result result) {
        return ResultDto.builder()
                .id(result.getId())
                .memberId(result.getMemberId())
                .title(result.getTitle())
                .replaceImageUrl(result.getReplaceImageUrl())
                .content(result.getContent())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }
}
