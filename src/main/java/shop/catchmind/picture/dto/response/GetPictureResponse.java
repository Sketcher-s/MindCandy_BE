package shop.catchmind.picture.dto.response;

import lombok.Builder;
import shop.catchmind.picture.domain.Picture;
import shop.catchmind.picture.domain.Result;
import shop.catchmind.picture.dto.PictureDto;
import shop.catchmind.picture.dto.ResultDto;

import java.util.List;

@Builder
public record GetPictureResponse(
        List<PictureDto> pictureList,
        ResultDto result
) {

    public static GetPictureResponse of(final List<Picture> pictureList, final Result result) {
        return GetPictureResponse.builder()
                .pictureList(pictureList.stream()
                        .map(PictureDto::of)
                        .toList())
                .result(ResultDto.from(result))
                .build();
    }
}
