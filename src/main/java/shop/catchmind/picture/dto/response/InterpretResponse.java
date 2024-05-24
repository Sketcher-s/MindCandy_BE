package shop.catchmind.picture.dto.response;

import lombok.Builder;
import shop.catchmind.picture.dto.PictureDto;

@Builder
public record InterpretResponse(
        PictureDto pictureDto
) {
    public static InterpretResponse of(PictureDto pictureDto) {
        return InterpretResponse.builder()
                .pictureDto(pictureDto)
                .build();
    }
}
