package shop.catchmind.picture.domain;

import jakarta.persistence.*;
import lombok.*;
import shop.catchmind.common.AuditingField;

@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Picture extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "content", length = 3000)
    private String content;

    @Column(name = "picture_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PictureType pictureType;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private Result result;

    @Builder
    private Picture(final String imageUrl, final String content, final PictureType pictureType) {
        this.imageUrl = imageUrl;
        this.content = content;
        this.pictureType = pictureType;
    }
}
