package shop.catchmind.member.dto;

import lombok.Builder;
import shop.catchmind.picture.dto.response.SimpleResultDto;

import java.util.List;

@Builder
public record GetPictureListResponse(
        SimpleMemberDto simpleMemberDto,
        List<SimpleResultDto> simpleResultDtoList
) {
    public static GetPictureListResponse of(final SimpleMemberDto simpleMemberDto, final List<SimpleResultDto> simpleResultDtoList) {
        return GetPictureListResponse.builder()
                .simpleMemberDto(simpleMemberDto)
                .simpleResultDtoList(simpleResultDtoList)
                .build();
    }
}
