package com.apitest.mytest.domainstate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Aggregate Root.
 * documentId 는 다른 Aggregate(Document)를 가리키므로 객체가 아니라 ID로만 참조한다.
 * sections 는 같은 Aggregate 내부 자식이므로 객체 컬렉션으로 갖는다 (cascade = ALL).
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "tb_document_revisions")
public class DocumentRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id", nullable = false)
    private long documentId;

    @Column(name = "revision_number", nullable = false)
    private long revisionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentStatus status;

    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentSection> sections = new ArrayList<>();

    // TODO 1: DRAFT 상태로 시작하는 인스턴스를 만들어 반환하세요.
    //   힌트: new DocumentRevision(null, documentId, revisionNumber, DocumentStatus.DRAFT, new ArrayList<>())
    public static DocumentRevision newInstance(long documentId, long revisionNumber) {
        throw new UnsupportedOperationException("TODO 1");
    }

    // TODO 2: status 가 DRAFT 인지 여부만 반환하세요.
    public boolean isDraft() {
        throw new UnsupportedOperationException("TODO 2");
    }

    // TODO 3: DRAFT 가 아니면 IllegalStateException 을 던지세요.
    public void assertMutable() {
        throw new UnsupportedOperationException("TODO 3");
    }

    // TODO 4: assertMutable() 을 먼저 호출한 뒤,
    //   DocumentSection.newInstance(this, title, content) 를 만들어 sections 에 추가하세요.
    public void addSection(String title, String content) {
        throw new UnsupportedOperationException("TODO 4");
    }

    // TODO 5: assertMutable() 호출 -> sections 가 비어있으면 IllegalStateException
    //   ("섹션이 없으면 발행할 수 없다") -> 통과하면 status 를 PUBLISHED 로 변경하세요.
    public void publish() {
        throw new UnsupportedOperationException("TODO 5");
    }

    // TODO 6: 이미 ARCHIVED 면 아무것도 하지 않고 리턴 (idempotent).
    //   PUBLISHED 가 아니면 IllegalStateException, 통과하면 ARCHIVED 로 변경하세요.
    public void archive() {
        throw new UnsupportedOperationException("TODO 6");
    }
}
