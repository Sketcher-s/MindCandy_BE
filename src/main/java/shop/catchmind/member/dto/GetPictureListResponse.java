package shop.catchmind.member.dto;

import lombok.Builder;
import shop.catchmind.picture.dto.response.SimplePictureDto;

import java.util.List;

@Builder
public record GetPictureListResponse(

        SimpleMemberDto simpleMemberDto,
        List<SimplePictureDto> simplePictureDtoList
) {
    public static GetPictureListResponse of(final SimpleMemberDto simpleMemberDto, final List<SimplePictureDto> simplePictureDtoList) {
        return GetPictureListResponse.builder()
                .simpleMemberDto(simpleMemberDto)
                .simplePictureDtoList(simplePictureDtoList)
                .build();
    }
}
