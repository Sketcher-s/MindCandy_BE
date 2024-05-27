package shop.catchmind.picture.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.catchmind.picture.domain.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    void deleteAllByMemberId(Long memberId);

    Page<Picture> findByMemberId(Long memberId, Pageable pageable);
}
