package shop.catchmind.picture.dto.response;

import jakarta.persistence.Lob;
import lombok.Builder;
import shop.catchmind.picture.domain.PictureType;

@Builder
public record RecognitionResultResponse(
        PictureType pictureType,
        @Lob
        String value
) {
    public static RecognitionResultResponse of(final PictureType pictureType, final String value) {
        return RecognitionResultResponse.builder()
                .pictureType(pictureType)
                .value(value)
                .build();
    }
}
