package shop.catchmind.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.catchmind.global.config.constant.AuthorityConstant;

@Getter
@AllArgsConstructor
public enum Role {

    USER(AuthorityConstant.USER)
    ;

    private final String authority;
}
