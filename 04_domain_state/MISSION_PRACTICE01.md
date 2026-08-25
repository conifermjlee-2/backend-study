# 📄 [04] 도메인 상태 전이 & Aggregate 경계 — 문제지

> **문제지입니다. 정답 코드는 없습니다.** 실제 목표는 `atworks-lithium`의
> [`apispec/entity/ApiSpecRevision.java`](../../team-atworks/backend/atworks-lithium/src/main/java/com/apitest/atworks/apispec/entity/ApiSpecRevision.java)
> 패턴을 미니 도메인으로 직접 재현해보는 것입니다. 다 풀거나 막히면 알려주세요 —
> `README_PRACTICE01.md`로 정답지 + atworks-lithium 코드와의 비교 해설을 정리해 드립니다.

---

## 🎯 이 미션의 목표

atworks-lithium 코드는 거의 모든 엔티티가 두 가지 규칙을 따릅니다.

1. **상태는 setter가 아니라 도메인 메서드로 전이한다.** (`publish()`, `archive()`처럼 이름이 있는 행위)
2. **같은 Aggregate 내부는 객체 참조, 다른 Aggregate는 ID 참조.** (`@ManyToOne` vs `Long xxxId`)

이 두 가지를 이해하지 못하면 `inventory`, `execution`, `apispec` 등 어떤 도메인 패키지를 열어도
"왜 이 필드는 객체가 아니라 그냥 숫자 ID지?", "왜 서비스가 아니라 엔티티 안에 이 로직이 있지?" 에서 막힙니다.

이번 미션에서는 **"API 문서 리비전 관리"**라는 미니 도메인을 만들어서 `ApiSpecRevision`과 거의 1:1로 대응시켜볼 거예요.

| 내가 만들 것 | atworks-lithium의 대응 코드 |
| :--- | :--- |
| `DocumentRevision` (상태 전이 aggregate root) | `apispec/entity/ApiSpecRevision.java` |
| `DocumentSection` (같은 aggregate 내부 자식) | `apispec/entity/ApiSpec.java` |
| `documentId` (다른 aggregate를 ID로만 참조) | `ApiSpecRevision.serverId` (Server는 별도 aggregate) |

---

## 🧠 미리 알아야 할 규칙 (atworks-lithium `AGENTS.md` 그대로)

| 규칙 | 내용 |
| :--- | :--- |
| PK | `Long id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` |
| 생성자 | 기본 생성자 `protected`, 전체 필드 생성자 `private` |
| 생성 방법 | public 생성자 노출 금지, `newInstance(...)` 정적 팩터리만 사용 |
| 상태 변경 | Setter 금지. 의미 있는 이름의 도메인 메서드로만 변경 |
| 같은 Aggregate 내부 | JPA 객체 연관관계 사용, `fetch = LAZY` |
| 다른 Aggregate / Bounded Context | 엔티티 객체 대신 **ID**로 참조 |
| cascade | 생명주기가 같을 때만 사용 (부모 지우면 자식도 지워질 때) |
| Enum | 반드시 `@Enumerated(EnumType.STRING)` |
| 필수 참조 ID | 메서드 인자·값이 항상 있는 ID는 `long`(primitive), null 가능한 선택 값만 `Long` |

---

## 🗂️ 실습 위치

- 새 폴더: `04_domain_state/`
- 실습 코드 패키지: `com.apitest.mytest.domainstate`
  - `src/main/java/com/apitest/mytest/domainstate/DocumentStatus.java`
  - `src/main/java/com/apitest/mytest/domainstate/DocumentRevision.java`
  - `src/main/java/com/apitest/mytest/domainstate/DocumentSection.java`
- 테스트: `src/test/java/com/apitest/mytest/domainstate/DocumentRevisionTest.java`
- JPA 어노테이션(`@Entity` 등)은 이번에도 실무처럼 붙이되, **이번 테스트는 DB 없이 순수 객체(JUnit)로만 검증**합니다.
  (영속성 컨텍스트는 03_jpa에서 이미 다뤘으니, 이번엔 "도메인 로직 자체"에 집중)

---

## 🧩 미션 1. `DocumentStatus` — 상태 정의

- [ ] `enum DocumentStatus { DRAFT, PUBLISHED, ARCHIVED }` 를 만든다.
- [ ] 왜 `ORDINAL`이 아니라 `STRING`으로 저장해야 하는지 주석 한 줄로 다시 정리해본다. (01단계 복습)

---

## 🧩 미션 2. `DocumentRevision` — Aggregate Root & 상태 전이

`ApiSpecRevision`을 참고하되 **복붙하지 말고** 아래 필드/규칙만 보고 직접 구성하세요.

- [ ] 필드: `id`(Long, PK), `documentId`(long — **다른 aggregate에 대한 ID 참조**), `revisionNumber`(long), `status`(DocumentStatus)
- [ ] `sections` 필드: `DocumentSection`의 리스트. **같은 aggregate 내부이므로 객체 연관관계**로 가진다.
      (`@OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, orphanRemoval = true)`)
- [ ] 기본 생성자 `protected`, 전체 필드 생성자 `private`
- [ ] `public static DocumentRevision newInstance(long documentId, long revisionNumber)` — `DRAFT` 상태로 시작, `sections`는 빈 리스트로 초기화
- [ ] `public boolean isDraft()`
- [ ] `public void assertMutable()` — `DRAFT`가 아니면 `IllegalStateException`
- [ ] `public void addSection(String title, String content)`
      - `assertMutable()` 먼저 호출 (DRAFT일 때만 섹션 추가 가능)
      - 내부에서 `DocumentSection.newInstance(this, title, content)`를 만들어 `sections`에 추가
- [ ] `public void publish()`
      - `assertMutable()` 먼저 호출
      - **추가 불변식**: `sections`가 비어 있으면 `IllegalStateException("섹션이 없으면 발행할 수 없다")`
      - 통과하면 `status = PUBLISHED`
- [ ] `public void archive()`
      - 이미 `ARCHIVED`면 아무것도 안 하고 조용히 리턴 (idempotent)
      - `PUBLISHED`가 아니면 `IllegalStateException`
      - 통과하면 `status = ARCHIVED`

> 💡 `documentId`를 `Document document` 객체로 만들지 않고 `long documentId`로 둔 이유를 README나 주석에
> 한 문장으로 남겨보세요. (힌트: `Document`는 이 리비전과 생명주기가 다른, 별도 Aggregate입니다.)

---

## 🧩 미션 3. `DocumentSection` — 같은 Aggregate 내부의 자식 엔티티

- [ ] 필드: `id`(Long, PK), `revision`(**`DocumentRevision` 객체 참조**, `@ManyToOne(fetch = FetchType.LAZY)`), `title`(String), `content`(String)
- [ ] 기본 생성자 `protected`, 전체 필드 생성자 `private`
- [ ] `public static DocumentSection newInstance(DocumentRevision revision, String title, String content)`
- [ ] `public void updateContent(String newContent)`
      - `revision.isDraft()`가 `false`면 `IllegalStateException`
      - 통과하면 `content` 변경
- [ ] setter는 하나도 만들지 않는다.

> 💡 `revision`은 왜 `Long revisionId`가 아니라 객체 참조(`DocumentRevision revision`)로 두었을까요?
> 미션 2의 `documentId`와 비교해서 한 문장으로 정리해보세요. (이게 이 미션의 핵심 질문입니다.)

---

## 🧩 미션 4. 테스트로 증명하기 (`DocumentRevisionTest.java`)

아래 8개를 전부 통과시키세요. (메서드 이름은 자유, 시나리오만 지키면 됩니다)

- [ ] DRAFT 상태에서 `addSection()`을 호출하면 성공하고 `sections.size()`가 늘어난다.
- [ ] 섹션이 하나도 없는 상태에서 `publish()`를 호출하면 `IllegalStateException`이 터진다.
- [ ] 섹션을 하나 추가한 뒤 `publish()`를 호출하면 `status`가 `PUBLISHED`로 바뀐다.
- [ ] `PUBLISHED` 상태에서 `addSection()`을 호출하면 `IllegalStateException`이 터진다. (assertMutable 검증)
- [ ] `PUBLISHED` 상태에서 `archive()`를 호출하면 `ARCHIVED`로 바뀐다.
- [ ] `ARCHIVED` 상태에서 `archive()`를 **다시** 호출해도 예외 없이 그대로 `ARCHIVED`다. (idempotent 검증)
- [ ] `DRAFT` 상태에서 바로 `archive()`를 호출하면 `IllegalStateException`이 터진다.
- [ ] `PUBLISHED` 상태의 리비전에 딸린 `DocumentSection`의 `updateContent()`를 호출하면 `IllegalStateException`이 터진다.

---

## ✅ 최종 체크리스트

- [ ] `DocumentRevision`, `DocumentSection` 어디에도 public setter가 없다
- [ ] `documentId`는 `long`(ID 참조), `revision`은 `DocumentRevision`(객체 참조) — 이 둘이 왜 다른지 한 문장으로 설명할 수 있다
- [ ] `publish()` / `archive()` 전이 규칙이 위 8개 테스트로 전부 증명된다
- [ ] `./gradlew.bat test --tests "com.apitest.mytest.domainstate.DocumentRevisionTest"` 전부 초록불
- [ ] 다 되면 `apispec/entity/ApiSpecRevision.java`, `apispec/entity/ApiSpec.java`를 열어서
      내가 짠 코드와 나란히 비교해보고, 다르게 짠 부분이 있으면 왜 그런지 적어본다

다 되면 알려주세요. 코드 보고 `README_PRACTICE01.md` 정답지 + atworks-lithium 코드와의 비교 해설을 정리해 드릴게요.
