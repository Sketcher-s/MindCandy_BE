package shop.catchmind.picture.dto.response;

import lombok.Builder;
import shop.catchmind.picture.dto.PictureDto;

@Builder
public record GetPictureResponse(
        PictureDto pictureDto
) {

    public static GetPictureResponse of(PictureDto pictureDto) {
        return GetPictureResponse.builder()
                .pictureDto(pictureDto)
                .build();
    }
}
