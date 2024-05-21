package shop.catchmind.auth.dto.response;

import lombok.Builder;

@Builder
public record IsExistedEmailResponse(
        Boolean isExisted
) {
}
