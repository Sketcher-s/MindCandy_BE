package shop.catchmind.picture.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.catchmind.common.AuditingField;

@Entity
@Table(name = "result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Result extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "title", length = 15)
    private String title;

    @Column(name = "image_url")
    private String replaceImageUrl;

    @Column(name = "content", length = 3000)
    private String content;

    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateContent(final String content) {
        this.content = content;
    }

    public void updateReplaceImageUrl(String replaceImageUrl) {
        this.replaceImageUrl = replaceImageUrl;
    }

    @Builder
    private Result(final Long memberId, final String title, final String replaceImageUrl, final String content) {
        this.memberId = memberId;
        this.title = title;
        this.replaceImageUrl = replaceImageUrl;
        this.content = content;
    }
}
