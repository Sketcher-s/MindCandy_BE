package shop.catchmind.auth.constant;

public final class AuthErrorConstant {

    private AuthErrorConstant() {}

    public static final String NAME_SIZE = "이름은 2~6자 이내이어야 합니다.";
    public static final String NAME_NOT_BLANK = "이름은 null이거나 빈칸일 수 없습니다.";

    public static final String EMAIL_NOT_BLANK = "이메일은 null이거나 빈칸일 수 없습니다.";
    public static final String EMAIL_EMAIL_FORM = "이메일 형식이 아닙니다.";

    public static final String PASSWORD_NOT_BLANK = "비밀번호는 null이거나 빈칸일 수 없습니다.";
    public static final String PASSWORD_PATTERN = "비밀번호는 적어도 한 글자의 영문자와, 숫자 하나, 특수 문자 하나를 포함해야 합니다.";
    public static final String PASSWORD_SIZE = "비밀번호는 8~12자 이내이어야 합니다.";

}
