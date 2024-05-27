package shop.catchmind.member.dto;

import lombok.Builder;
import lombok.Getter;
import shop.catchmind.member.domain.Member;

@Builder
public record MemberDto (
        Long id,
        String name,
        String email,
        String password

) {
    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
