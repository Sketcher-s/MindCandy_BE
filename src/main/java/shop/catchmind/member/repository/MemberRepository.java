package shop.catchmind.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.catchmind.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(final String email);

    Boolean existsByEmail(final String email);
}
