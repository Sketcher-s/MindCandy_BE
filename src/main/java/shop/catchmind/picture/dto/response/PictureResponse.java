package shop.catchmind.picture.dto.response;

import lombok.Builder;
import shop.catchmind.picture.dto.PictureDto;

@Builder
public record PictureResponse(
        PictureDto pictureDto
) {

    public static PictureResponse of(PictureDto pictureDto) {
        return PictureResponse.builder()
                .pictureDto(pictureDto)
                .build();
    }
}
