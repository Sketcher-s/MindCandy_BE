package shop.catchmind.picture.exception;


import shop.catchmind.picture.constant.ResultErrorConstant;

public class UnmatchedMemberResultException extends RuntimeException{
    public UnmatchedMemberResultException() {
        super(ResultErrorConstant.UNMATCHED_MEMBER_RESULT);
    }
}
