package shop.catchmind.picture.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.catchmind.common.AuditingField;

@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Picture extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "title", length = 15)
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "result")
    private String result;

    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateResult(final String result) {
        this.result = result;
    }

    @Builder
    public Picture(Long memberId, String title, String imageUrl, String result) {
        this.memberId = memberId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.result = result;
    }
}
