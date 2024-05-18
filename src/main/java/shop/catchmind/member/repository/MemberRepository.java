package shop.catchmind.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.catchmind.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
