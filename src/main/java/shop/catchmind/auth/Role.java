package shop.catchmind.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.catchmind.auth.constant.AuthRoleConstant;

@Getter
@AllArgsConstructor
public enum Role {

    USER(AuthRoleConstant.USER)
    ;

    private final String authority;
}
