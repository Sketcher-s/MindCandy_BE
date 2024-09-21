package shop.catchmind.picture.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import shop.catchmind.picture.dto.PictureRequestDto;

import java.time.LocalTime;
import java.util.List;

public record InspectRequest(
        List<PictureRequestDto> pictureRequestDtoList,
        @JsonFormat(pattern = "HH:mm")
        LocalTime consumedTime
) {
}
