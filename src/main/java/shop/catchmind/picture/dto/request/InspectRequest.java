package shop.catchmind.picture.dto.request;

import org.springframework.format.annotation.DateTimeFormat;
import shop.catchmind.picture.dto.PictureRequestDto;

import java.time.LocalTime;
import java.util.List;

public record InspectRequest(
        List<PictureRequestDto> pictureRequestDtoList,
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime consumedTime
) {
}
