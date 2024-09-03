package shop.catchmind.picture.dto.request;

import shop.catchmind.picture.domain.PictureType;

public record RecognizeRequest(
        PictureType pictureType
) {
}
