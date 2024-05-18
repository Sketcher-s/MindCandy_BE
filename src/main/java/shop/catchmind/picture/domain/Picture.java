package shop.catchmind.picture.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.catchmind.common.AuditingField;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Picture extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    public Picture(final String title, final String imageUrl, final String result) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.result = result;
    }
}
