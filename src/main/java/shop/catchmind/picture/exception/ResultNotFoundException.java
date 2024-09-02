package shop.catchmind.picture.exception;

import shop.catchmind.picture.constant.ResultErrorConstant;

public class ResultNotFoundException extends RuntimeException {
    public ResultNotFoundException() {
        super(ResultErrorConstant.NOT_FOUND);
    }
}
