package shop.catchmind.member.dto;

import lombok.Builder;

@Builder
public record SimpleMemberDto(

        Long id,
        String name,
        String email
) {
    public static SimpleMemberDto of(MemberDto memberDto) {
        return SimpleMemberDto.builder()
                .id(memberDto.id())
                .name(memberDto.name())
                .email(memberDto.email())
                .build();
    }
}
