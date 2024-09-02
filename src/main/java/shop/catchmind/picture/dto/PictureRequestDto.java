package shop.catchmind.picture.dto;

import jakarta.persistence.Lob;
import org.springframework.web.multipart.MultipartFile;
import shop.catchmind.picture.domain.PictureType;

public record PictureRequestDto(
        PictureType pictureType,
        MultipartFile image,
        @Lob
        String value
) {
}
