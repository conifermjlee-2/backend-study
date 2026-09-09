package com.apitest.mytest.domainstate_2;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부 접근 금지, 내부에서만 생성
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 내부에서 사용하기 위한 생성자
@Table(name = "tb_document_revisions")  
@Entity
public class DocumentRevision {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "document_id", nullable = false)
    private long documentId;

    @Column(name = "revision_number", nullable = false)
    private long revisionNumber;


    //기본값은 ORDINAL 이다 -> 문제 가능성 1,2,3 이렇게 저장
    //중간에 값이 추가되면 이미 저장된 애들 꺠짐
    //순서가 바뀌어도 안전하게 -> string
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentStatus status;

   /* - [ ] `sections` 필드: `DocumentSection`의 리스트. **같은 aggregate 내부이므로 객체 연관관계**로 가진다.
   (`@OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, orphanRemoval = true)`)*/

}
