package shop.catchmind.picture.dto.response;

import lombok.Builder;
import shop.catchmind.picture.domain.Picture;
import shop.catchmind.picture.domain.Result;
import shop.catchmind.picture.dto.PictureDto;
import shop.catchmind.picture.dto.ResultDto;

import java.util.List;

@Builder
public record InterpretResponse(
        List<PictureDto> pictureList,
        ResultDto result
) {
    public static InterpretResponse of(final List<Picture> pictureList, final Result result) {
        return InterpretResponse.builder()
                .pictureList(pictureList.stream()
                        .map(PictureDto::of)
                        .toList())
                .result(ResultDto.from(result))
                .build();
    }
}
