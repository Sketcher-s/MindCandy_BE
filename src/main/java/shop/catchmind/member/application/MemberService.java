package shop.catchmind.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.catchmind.member.domain.Member;
import shop.catchmind.member.exception.InvalidUserException;
import shop.catchmind.member.repository.MemberRepository;
import shop.catchmind.picture.repository.PictureRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PictureRepository pictureRepository;

    // TODO: 회원 탈퇴
    @Transactional
    public void leaveMember(final Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(InvalidUserException::new);
        pictureRepository.deleteAllByMemberId(memberId);
        memberRepository.delete(member);
    }
}
