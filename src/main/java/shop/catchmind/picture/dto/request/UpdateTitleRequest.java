package shop.catchmind.picture.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTitleRequest(
        @NotBlank
        Long id,

        @Size(max = 15)
        @NotBlank
        String title
) {
}
