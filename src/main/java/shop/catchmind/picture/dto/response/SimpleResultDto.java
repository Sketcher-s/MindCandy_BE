package shop.catchmind.picture.dto.response;

import lombok.Builder;
import shop.catchmind.picture.dto.ResultDto;

import java.time.LocalDateTime;

@Builder
public record SimpleResultDto(
        Long id,
        String title,
        String replaceImageUrl,
        LocalDateTime createdAt
) {
    public static SimpleResultDto of(final ResultDto resultDto) {
        return SimpleResultDto.builder()
                .id(resultDto.id())
                .title(resultDto.title())
                .replaceImageUrl(resultDto.replaceImageUrl())
                .createdAt(resultDto.createdAt())
                .build();
    }
}
