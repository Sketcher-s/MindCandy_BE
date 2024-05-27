package shop.catchmind.picture.dto;

import lombok.Builder;
import shop.catchmind.picture.domain.Picture;

import java.time.LocalDateTime;

@Builder
public record PictureDto(
        Long id,
        Long memberId,
        String title,
        String imageUrl,
        String result,
        LocalDateTime createdAt
) {
    public static PictureDto of(Picture picture) {
        return PictureDto.builder()
                .id(picture.getId())
                .memberId(picture.getMemberId())
                .title(picture.getTitle())
                .imageUrl(picture.getImageUrl())
                .result(picture.getResult())
                .createdAt(picture.getCreatedAt())
                .build();
    }
}
