package shop.catchmind.picture.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.catchmind.picture.domain.Result;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findAllByMemberId(Long memberId);

    Page<Result> findAllByMemberId(Long memberId, Pageable pageable);
}
