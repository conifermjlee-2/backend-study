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

    // ORDINAL(기본값)은 enum 순서를 숫자로 저장해서, 나중에 enum 순서가 바뀌거나
    // 중간에 값이 추가되면 이미 저장된 숫자의 의미가 틀어져 데이터가 깨진다.
    // STRING은 이름 그대로 저장하므로 순서가 바뀌어도 안전하다.
    @Enumerated(EnumType.STRING)
    // varchar 컬럼으로 저장, null 금지, 최대 길이 20자로 제한
    @Column(nullable = false, length = 20)
    private DocumentStatus status;

    // mappedBy = "revision" -> 연관관계의 주인은 내가 아니라 DocumentSection의 revision 필드다.
    //   (FK인 revision_id 컬럼은 DocumentSection 쪽 @JoinColumn이 관리한다. 나는 조회 전용.)
    // cascade = ALL         -> 이 revision을 저장/삭제하면 sections도 통째로 같이 저장/삭제된다.
    // orphanRemoval = true  -> revision은 그대로 두고 sections.remove(x)로 리스트에서만 빼도,
    //   그 자식은 "고아"로 간주되어 DB에서도 자동으로 DELETE된다.
    //   (false였다면 자바 리스트에서만 사라지고 DB에는 유령 row로 남는다 - 3단계 더티체킹과 동일한 원리)
    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentSection> sections = new ArrayList<>();

    // TODO 1: DRAFT 상태로 시작하는 인스턴스를 만들어 반환하세요.
    //
    // 정답:
    // public static DocumentRevision newInstance(long documentId, long revisionNumber) {
    //     return new DocumentRevision(null, documentId, revisionNumber, DocumentStatus.DRAFT, new ArrayList<>());
    // }
    public static DocumentRevision newInstance(long documentId, long revisionNumber) {
        throw new UnsupportedOperationException("TODO 1");
    }

    // TODO 2: status 가 DRAFT 인지 여부만 반환하세요.
    //
    // 정답:
    // public boolean isDraft() {
    //     return status == DocumentStatus.DRAFT;
    // }
    public boolean isDraft() {
        throw new UnsupportedOperationException("TODO 2");
    }

    // TODO 3: DRAFT 가 아니면 IllegalStateException 을 던지세요.
    //
    // 정답:
    // public void assertMutable() {
    //     if (!isDraft()) {
    //         throw new IllegalStateException("DRAFT 상태가 아니면 수정할 수 없다");
    //     }
    // }
    public void assertMutable() {
        throw new UnsupportedOperationException("TODO 3");
    }

    // TODO 4: assertMutable() 을 먼저 호출한 뒤,
    //   DocumentSection.newInstance(this, title, content) 를 만들어 sections 에 추가하세요.
    //
    // 정답:
    // public void addSection(String title, String content) {
    //     assertMutable();
    //     sections.add(DocumentSection.newInstance(this, title, content));
    // }
    public void addSection(String title, String content) {
        throw new UnsupportedOperationException("TODO 4");
    }

    // TODO 5: assertMutable() 호출 -> sections 가 비어있으면 IllegalStateException
    //   ("섹션이 없으면 발행할 수 없다") -> 통과하면 status 를 PUBLISHED 로 변경하세요.
    //
    // 정답:
    // public void publish() {
    //     assertMutable();
    //     if (sections.isEmpty()) {
    //         throw new IllegalStateException("섹션이 없으면 발행할 수 없다");
    //     }
    //     status = DocumentStatus.PUBLISHED;
    // }
    public void publish() {
        throw new UnsupportedOperationException("TODO 5");
    }

    // TODO 6: 이미 ARCHIVED 면 아무것도 하지 않고 리턴 (idempotent).
    //   PUBLISHED 가 아니면 IllegalStateException, 통과하면 ARCHIVED 로 변경하세요.
    //
    // 정답:
    // public void archive() {
    //     if (status == DocumentStatus.ARCHIVED) {
    //         return;
    //     }
    //     if (status != DocumentStatus.PUBLISHED) {
    //         throw new IllegalStateException("PUBLISHED 상태에서만 보관할 수 있다");
    //     }
    //     status = DocumentStatus.ARCHIVED;
    // }
    public void archive() {
        throw new UnsupportedOperationException("TODO 6");
    }
}
