package shop.catchmind.picture.exception;

import shop.catchmind.picture.constant.PictureErrorConstant;

public class PictureNotFoundException extends RuntimeException{
    public PictureNotFoundException() {
        super(PictureErrorConstant.NOT_FOUND);
    }
}
