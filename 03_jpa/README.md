# 03. JPA (Java Persistence API)

> **💡 메인 학습 목표:**
> 1. JPA가 SQL을 직접 짜는 것과 무엇이 다른지, 왜 필요한지 이해한다.
> 2. 영속성 컨텍스트(1차 캐시, 더티 체킹, 쓰기 지연)의 동작 원리를 체화한다.
> 3. 연관관계 매핑과 로딩 전략을 이해하고 N+1 문제를 해결할 수 있다.
> 4. Spring Data JPA로 실무 CRUD와 쿼리를 작성할 수 있다.

---

## 🗺️ 단계별 학습 목차 (총 9단계)

| 단계 | 주제 | 핵심 키워드 | 진행 상태 |
| :---: | :--- | :--- | :---: |
| **1** | [ORM 개념 이해](./README_PRACTICE01.md) | JPA가 필요한 이유, ORM이란, JPA(표준) vs Hibernate(구현체) | ✅ 완료 |
| **2** | [엔티티 매핑](./README_PRACTICE02.md) | `@Entity`, `@Id`, `@GeneratedValue`, `@Column`, `@Table`, 기본키 생성 전략(`IDENTITY`/`SEQUENCE`), 리플렉션 | 🔄 진행중 |
| **3** | 영속성 컨텍스트 (⭐ 제일 중요) | `EntityManager`, 1차 캐시, 엔티티 생명주기(비영속→영속→준영속→삭제), 더티 체킹, 쓰기 지연 | ⏳ 예정 |
| **4** | 연관관계 매핑 | `@ManyToOne`/`@OneToMany`, 외래키 vs 객체 참조, 연관관계의 주인, `mappedBy`, 필드명 관례 | ⏳ 예정 |
| **5** | 즉시로딩 / 지연로딩 | `FetchType.EAGER` vs `LAZY`, N+1 문제, fetch join, `@EntityGraph` | ⏳ 예정 |
| **6** | 값 타입 / 상속 관계 매핑 | `@Embeddable`, 상속 전략(`SINGLE_TABLE`/`JOINED`/`TABLE_PER_CLASS`) | ⏳ 예정 |
| **7** | JPQL / QueryDSL | 메서드 이름으로 안 되는 쿼리, Spring Data JPA 메서드 네이밍이 편한 이유 체감 | ⏳ 예정 |
| **8** | Spring Data JPA | `JpaRepository`, 메서드 네이밍 규칙, `@Query` | ⏳ 예정 |
| **9** | 실전 심화 (선택) | cascade/orphanRemoval, 낙관적·비관적 락(`@Version`), 벌크 연산, `@Transactional` 전파, OSIV, 배치 처리 | ⏳ 예정 |

---

## 📚 참고 노션 문서
- [1단계 - ORM 개념 이해](https://app.notion.com/p/1-ORM-3c60d80a457681a5b86df9117ed0ee84?pvs=21)
- [2단계 - 엔티티 매핑](https://app.notion.com/p/2-3c60d80a45768180a635ef376fc3ae8c?pvs=21)
- [3단계 - 영속성 컨텍스트](https://app.notion.com/p/3-3c50d80a457681d990fcd577beec5e8c?pvs=21)
- [4단계 - 연관관계 매핑](https://app.notion.com/p/4-3c60d80a457681e4bc09cb5f2e28cb6c?pvs=21)

> 5~9단계는 노션 문서가 아직 없어서, 진행하면서 `README_PRACTICE0N.md`로 정리 예정입니다.

---

## ⚙️ 프로젝트 세팅 (Spring Boot + JPA + H2)

01/02와 달리 3단계(영속성 컨텍스트)부터는 `EntityManager`가 실제로 동작해야 의미가 있어서, 이번엔 프로젝트 루트를 **Gradle + Spring Boot** 프로젝트로 전환했습니다.

- `build.gradle` — `spring-boot-starter-data-jpa`, `spring-boot-starter-web`, `h2`, `lombok`
- `gradlew` / `gradlew.bat` — Gradle Wrapper (Gradle 설치 불필요)
- `src/main/java/com/apitest/mytest/MyTestApplication.java` — 부트 실행 클래스
- `src/main/resources/application.yml` — H2 인메모리 DB, `ddl-auto: create-drop`, SQL 로그 출력, H2 콘솔(`/h2-console`) 활성화

### 실행 방법
```bash
# JAVA_HOME이 JDK 17+ 를 가리켜야 합니다 (프로젝트는 JDK 21 toolchain 사용)
./gradlew bootRun
```
서버 기동 후 `http://localhost:8080/h2-console`에서 JDBC URL `jdbc:h2:mem:mytest`로 접속하면 실습 중 테이블/데이터를 직접 확인할 수 있습니다.

### 실습 코드 위치
01/02는 숫자 폴더 안에 클래스를 바로 뒀지만, JPA부터는 Gradle 표준 레이아웃을 따릅니다.
- 실습 코드: `src/main/java/com/apitest/mytest/jpa/practiceN/` (단계별 패키지)
- 정리 문서: `03_jpa/README_PRACTICEN.md` (개념 설명 + Q&A, 기존 폴더와 동일한 방식)

---

## 진행 방식
1~8단계는 순서대로 진행하고, 9단계(실전 심화)는 8단계까지 끝낸 뒤 필요할 때마다 추가합니다. 각 단계마다 실습 코드(`src/main/java/.../practiceN`)와 정리 문서(`README_PRACTICEN.md`)를 만들 예정입니다.
