package shop.catchmind.picture.dto.request;

import shop.catchmind.picture.dto.PictureRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public record InspectRequest(
        List<PictureRequestDto> pictureRequestDtoList,
        LocalDateTime requiredTime
) {
}
