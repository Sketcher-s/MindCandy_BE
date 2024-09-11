package shop.catchmind.picture.dto;

import jakarta.persistence.Lob;
import shop.catchmind.picture.domain.PictureType;

public record PictureRequestDto(
        PictureType pictureType,
        @Lob
        String value
) {
}
