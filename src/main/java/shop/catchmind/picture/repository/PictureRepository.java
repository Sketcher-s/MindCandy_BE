package shop.catchmind.picture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.catchmind.picture.domain.Picture;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findAllByResultId(Long resultId);
    void deleteAllByResultId(Long resultId);
}
