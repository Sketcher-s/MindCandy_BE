package shop.catchmind.picture.exception;

import shop.catchmind.picture.constant.PictureErrorConstant;

public class UnmatchedMemberPictureException extends RuntimeException{
    public UnmatchedMemberPictureException() {
        super(PictureErrorConstant.UNMATCHED_MEMBER_PICTURE);
    }
}
