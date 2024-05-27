package shop.catchmind.picture.dto.response;

import lombok.Builder;
import shop.catchmind.picture.dto.PictureDto;

import java.time.LocalDateTime;

@Builder
public record SimplePictureDto(
        Long id,
        String title,
        LocalDateTime createdAt
) {
    public static SimplePictureDto of(final PictureDto pictureDto) {
        return SimplePictureDto.builder()
                .id(pictureDto.id())
                .title(pictureDto.title())
                .createdAt(pictureDto.createdAt())
                .build();
    }
}
