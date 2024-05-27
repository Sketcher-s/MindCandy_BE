package shop.catchmind.member.exception;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException() {
        super("유효하지 않는 유저 ID 입니다.");
    }

}
