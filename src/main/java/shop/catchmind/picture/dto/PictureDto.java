package shop.catchmind.picture.dto;

import lombok.Builder;
import shop.catchmind.picture.domain.Picture;
import shop.catchmind.picture.domain.PictureType;

@Builder
public record PictureDto(
        String imageUrl,
        String content,
        PictureType pictureType
) {
    public static PictureDto of(final Picture picture) {
        return PictureDto.builder()
                .imageUrl(picture.getImageUrl())
                .content(picture.getContent())
                .pictureType(picture.getPictureType())
                .build();
    }
}
