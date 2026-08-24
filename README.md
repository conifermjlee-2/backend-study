# 🚀 백엔드 실전 개발 학습 가이드 (`my-test`)

> **🎯 메인 학습 타겟:** [`backend/atworks-lithium`](../atworks-lithium)  
> 이 저장소는 실제 서비스 수준으로 작성된 `atworks-lithium` 프로젝트의 백엔드 아키텍처와 코딩 패턴을 **초보자 눈높이에서 단계별로 분석하고, 직접 손으로 코드를 치며 체득(체화)**하기 위한 실습 공간입니다.

---

## 📌 왜 `atworks-lithium`을 타겟으로 공부해야 할까?

`atworks-lithium`은 단순한 CRUD를 넘어 **엔터프라이즈급 실무 백엔드 개발의 핵심 요소들**이 집약되어 있습니다:
1. **표준 패키지 구조 & 계층 분리**: Controller ➔ Service(Command/Result) ➔ Repository ➔ Entity (DDD 지향)
2. **견고한 예외 처리**: `@RestControllerAdvice` 기반 글로벌 예외 처리와 `ErrorCode` 체계
3. **인증 & 인가 시스템**: Spring Security + JWT + 프로젝트 역할(Role) 기반 커스텀 권한 AOP (`@RequireProjectPermission`)
4. **고급 JPA & 데이터 모델링**: 연관관계 매핑, 리비전(Draft ➔ Published) 관리, 비관적 락(`forUpdate`)을 통한 동시성 제어
5. **확장성 높은 비즈니스 로직**: 전략 패턴(Strategy)을 활용한 OpenAPI / Log 추출 및 Diff 계산 파이프라인
6. **철저한 테스트 코드**: 단위 테스트(Unit)부터 슬라이스 테스트(`@DataJpaTest`, `@WebMvcTest`), 통합 테스트(`@SpringBootTest`)까지 완비

---

## 🗺️ 초보자를 위한 추천 학습 로드맵 (단계별 커리큘럼)

복잡한 프로젝트를 처음부터 통째로 보면 길을 잃기 쉽습니다. 아래의 **6단계 순서**대로 하나씩 격파해 나가는 것을 추천합니다.

```
[1단계: 기초 체력] ──▶ [2단계: 도메인 & CRUD] ──▶ [3단계: 보안 & 권한]
   예외/Optional         Entity & DTO 분리        Spring Security & JWT
         │                                               │
         ▼                                               ▼
[4단계: 고급 JPA] ──▶ [5단계: 비즈니스 파이프라인] ──▶ [6단계: 테스트 코드]
   연관관계 & 락           인벤토리 & Diff 전략          단위/슬라이스/통합 테스트
```

---

### 📋 단계별 세부 학습 내용 및 매핑

| 단계 | 실습 폴더 (my-test) | 핵심 학습 개념 | `atworks-lithium` 참조 코드 |
| :---: | :--- | :--- | :--- |
| **01** | [`01_global_exception`](./01_global_exception/README.md) | • `Optional` 핵심 메서드 (`orElseThrow`, `map`, `filter`)<br>• `@RestControllerAdvice` 글로벌 예외 핸들러<br>• `ErrorCode` & `CustomException` 설계 | • [`global/exception`](../atworks-lithium/src/main/java/com/apitest/atworks/global/exception)<br>• [`auth/exception`](../atworks-lithium/src/main/java/com/apitest/atworks/auth/exception) |
| **02** | [`02_dto_vo_record`](./02_dto_vo_record/README.md) | • Java `record` 기본 문법 & 불변 객체<br>• 값 객체(VO: Value Object) 패턴 & 원시값 포장<br>• `atworks` 스타일 계층별 DTO (`Request` ➔ `Command` ➔ `Result` ➔ `Response`) | • [`user/controller`](../atworks-lithium/src/main/java/com/apitest/atworks/user/controller)<br>• [`user/service`](../atworks-lithium/src/main/java/com/apitest/atworks/user/service)<br>• [`server/entity/ServerBaseUrl.java`](../atworks-lithium/src/main/java/com/apitest/atworks/server/entity/ServerBaseUrl.java) |
| **03** | `03_security_and_jwt` *(예정)* | • Spring Security 기본 흐름 & `SecurityFilterChain`<br>• JWT 토큰 생성, 검증, `JwtAuthenticationFilter`<br>• AOP 기반 커스텀 권한 어노테이션 (`@RequireProjectPermission`) | • [`global/security`](../atworks-lithium/src/main/java/com/apitest/atworks/global/security)<br>• [`auth/service/AuthService.java`](../atworks-lithium/src/main/java/com/apitest/atworks/auth/service/AuthService.java)<br>• [`authorization/annotation`](../atworks-lithium/src/main/java/com/apitest/atworks/authorization/annotation) |
| **04** | `04_jpa_and_concurrency` *(예정)* | • JPA 연관관계 (`@ManyToOne`, `@OneToMany`, 지연로딩 `LAZY`)<br>• 엔티티 상태 관리 및 생명주기<br>• 동시성 제어를 위한 비관적 락 (`PESSIMISTIC_WRITE`, `forUpdate`) | • [`inventory/entity`](../atworks-lithium/src/main/java/com/apitest/atworks/inventory/entity)<br>• [`apispec/entity`](../atworks-lithium/src/main/java/com/apitest/atworks/apispec/entity)<br>• [`inventory/repository/ApiInventoryStateRepository.java`](../atworks-lithium/src/main/java/com/apitest/atworks/inventory/repository/ApiInventoryStateRepository.java) |
| **05** | `05_business_pipeline` *(예정)* | • 전략 패턴(Strategy) & 레지스트리 패턴<br>• OpenAPI 문서 파싱 및 Diff 계산 알고리즘<br>• 승인 ➔ 스펙 리비전(Draft ➔ Published) 반영 워크플로우 | • [`inventory/service/extractor`](../atworks-lithium/src/main/java/com/apitest/atworks/inventory/service/extractor)<br>• [`inventory/service/diff`](../atworks-lithium/src/main/java/com/apitest/atworks/inventory/service/diff)<br>• [`inventory/service/ApiInventoryPromotionService.java`](../atworks-lithium/src/main/java/com/apitest/atworks/inventory/service/ApiInventoryPromotionService.java) |
| **06** | `06_test_driven_backend` *(예정)* | • JUnit 5 & AssertJ를 이용한 단위 테스트<br>• `@DataJpaTest`를 활용한 리포지토리 슬라이스 테스트<br>• `@WebMvcTest` 컨트롤러 모킹 테스트 & 통합 테스트 | • [`src/test/java/com/apitest/atworks`](../atworks-lithium/src/test/java/com/apitest/atworks) |

---

## 💡 효과적인 학습 팁 (어떻게 공부해야 늘까?)

1. **눈으로만 보지 말고 손으로 직접 타이핑하기**
   * `atworks-lithium`의 코드를 복사-붙여넣기하지 않고, `my-test` 폴더에 직접 빈 클래스를 만들고 필요한 로직을 고민하며 채워보세요.
2. **README에 '왜(Why)'를 기록하기**
   * 단순 문법이 아니라 *"왜 여기서는 DTO를 Command/Result로 쪼갰을까?"*, *"왜 `try-catch` 대신 `orElseThrow`를 썼을까?"* 같은 설계 이유를 README에 정리하세요.
3. **테스트 코드로 동작 검증하기**
   * `main` 메서드로 `System.out.println`을 찍어보는 것도 좋지만, 점차 JUnit 테스트 코드를 작성해 스스로 검증하는 습관을 들이면 실력이 급상승합니다.
