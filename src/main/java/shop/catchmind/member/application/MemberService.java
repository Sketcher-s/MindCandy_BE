package shop.catchmind.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.catchmind.member.domain.Member;
import shop.catchmind.member.dto.MemberDto;
import shop.catchmind.member.dto.SimpleMemberDto;
import shop.catchmind.member.exception.InvalidUserException;
import shop.catchmind.member.repository.MemberRepository;
import shop.catchmind.picture.domain.Picture;
import shop.catchmind.member.dto.GetPictureListResponse;
import shop.catchmind.picture.dto.PictureDto;
import shop.catchmind.picture.dto.response.SimplePictureDto;
import shop.catchmind.picture.repository.PictureRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PictureRepository pictureRepository;

    // TODO: 회원 탈퇴
    @Transactional
    public void leaveMember(final Long memberId) {
        Member member = getMember(memberId);
        pictureRepository.deleteAllByMemberId(memberId);
        memberRepository.delete(member);
    }

    // TODO: 마이페이지 조회
    public GetPictureListResponse getMyPage(final Long memberId, final int page, final int size) {
        Member member = getMember(memberId);
        MemberDto memberDto = MemberDto.of(member);
        SimpleMemberDto simpleMemberDto = SimpleMemberDto.of(memberDto);

        // 그림 목록 조회 (페이지네이션 적용)
        Pageable pageable = PageRequest.of(page, size);
        Page<Picture> picturePage = pictureRepository.findAllByMemberId(memberId, pageable);
        List<SimplePictureDto> simplePictureDtoList = picturePage.stream()
                .map(PictureDto::of)
                .map(SimplePictureDto::of)
                .toList();

        // 회원 페이지 DTO 생성 및 반환
        return GetPictureListResponse.of(simpleMemberDto, simplePictureDtoList);
    }

    public Member getMember(Long memberId) {
       return memberRepository.findById(memberId)
                .orElseThrow(InvalidUserException::new);
    }
}
