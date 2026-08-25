package com.apitest.mytest.domainstate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DocumentRevision 과 같은 Aggregate 에 속하는 자식 엔티티.
 * revision 은 다른 Aggregate 가 아니라 "같은 Aggregate 내부"이므로 ID(Long)가 아니라
 * 객체 참조(@ManyToOne)로 갖는다. 미션 2의 documentId 와 반드시 비교해서 이해할 것.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "tb_document_sections")
public class DocumentSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "revision_id", nullable = false)
    private DocumentRevision revision;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    // TODO 1: 새 섹션 인스턴스를 만들어 반환하세요.
    //   힌트: new DocumentSection(null, revision, title, content)
    public static DocumentSection newInstance(DocumentRevision revision, String title, String content) {
        throw new UnsupportedOperationException("TODO 1");
    }

    // TODO 2: revision.isDraft() 가 false 면 IllegalStateException, 통과하면 content 를 변경하세요.
    //   setter 는 만들지 마세요 — 이 메서드가 유일한 변경 경로입니다.
    public void updateContent(String newContent) {
        throw new UnsupportedOperationException("TODO 2");
    }
}
