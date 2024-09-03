package shop.catchmind.picture.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static shop.catchmind.picture.constant.ResultErrorConstant.*;


public record UpdateTitleRequest(
        @NotNull(message = ID_NOT_BLANK)
        Long resultId,

        @Size(max = 15, message = TITLE_SIZE)
        @NotBlank(message = TITLE_NOT_BLANK)
        String title
) {
}
