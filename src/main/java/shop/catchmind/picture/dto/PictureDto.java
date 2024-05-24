package shop.catchmind.picture.dto;

import lombok.Builder;
import shop.catchmind.picture.domain.Picture;

@Builder
public record PictureDto(
        Long id,
        Long memberId,
        String title,
        String imageUrl,
        String result
) {
    public static PictureDto of(Picture picture) {
        return PictureDto.builder()
                .id(picture.getId())
                .memberId(picture.getMemberId())
                .title(picture.getTitle())
                .imageUrl(picture.getImageUrl())
                .result(picture.getResult())
                .build();
    }
}
