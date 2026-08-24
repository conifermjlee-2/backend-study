# 02. DTO, VO & Java Record 완벽 정복 (계층별 데이터 전송 아키텍처)

> **메인 학습 목표:**
> 1. Java 16+ 정식 스펙인 **`record`의 문법과 불변성(Immutability)**을 체득한다.
> 2. 단순 원시값을 포장하는 **값 객체(Value Object, VO)**의 역할과 자가 검증(Self-Validation)을 이해한다.
> 3. 실제 `atworks-lithium` 프로젝트의 핵심 아키텍처인 **계층별 DTO 분리 (`Request ➔ Command ➔ Result ➔ Response`)** 파이프라인을 직접 구현한다.

---

## 🏛️ 1. Entity vs DTO vs VO vs Record 한눈에 정리

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Java Object 분류                                 │
├───────────────────┬───────────────────┬────────────────────────────────┤
│    Entity (엔티티) │    VO (값 객체)   │          DTO (전송 객체)        │
├───────────────────┼───────────────────┼────────────────────────────────┤
│ • 고유 식별자(PK) 있음│ • 식별자 없음     │ • 계층 간 데이터 전달용         │
│ • 가변 상태 (JPA) │ • 완전한 불변(Immutable)│ • 순수 데이터 운반체           │
│ • 비즈니스 상태 변경  │ • 값 자체의 검증/연산 │ • Controller/Service DTO 분리  │
│ • DB 테이블 1:1 매핑│ • 원시값 포장용    │ • Java record 적극 활용 권장   │
└───────────────────┴───────────────────┴────────────────────────────────┘
```

---

## 🌊 2. atworks-lithium 실전 데이터 파이프라인 흐름도

```
[클라이언트 HTTP 요청]
       │
       ▼
 1. Request DTO (Controller) ──▶ @Valid, 웹 요청 파라미터 매핑 & 기본 검증
       │
       │ .toCommand()
       ▼
 2. Command DTO (Service)    ──▶ 순수 비즈니스 파라미터 (웹 기술 의존성 0%)
       │
       ▼
 3. Entity (Domain)          ──▶ 비즈니스 로직 수행 & JPA DB 영속화
       │
       │ Result.from(entity)
       ▼
 4. Result DTO (Service)     ──▶ 서비스 실행 결과 (엔티티 은닉, 민감정보 필터링)
       │
       │ Response.from(result)
       ▼
 5. Response DTO (Controller)──▶ 프론트엔드/클라이언트 전용 JSON 포맷 반환
       │
       ▼
[클라이언트 HTTP 응답]
```

---

## 🗺️ 3. 단계별 실습 로드맵 (Practice Series)

각 단계별 실습 코드를 열고, TODO 미션을 채워 넣은 뒤 `main()`을 실행하여 콘솔 결과를 확인하세요.

| 번호 | 실습 주제 | 실습 코드 | 상세 가이드 (README) | 핵심 포인트 |
| :---: | :--- | :---: | :---: | :--- |
| **01** | **Java Record 기본 문법 & 불변성** | [`Practice01.java`](./Practice01.java) | [`README_PRACTICE01.md`](./README_PRACTICE01.md) | • `name()`, `id()` 게터<br>• 컴팩트 생성자 검증 & 정규화<br>• 값 기반 동등성(`equals`) |
| **02** | **값 객체 (VO) 패턴 & 원시값 포장** | [`Practice02.java`](./Practice02.java) | [`README_PRACTICE02.md`](./README_PRACTICE02.md) | • `Email`, `Money` VO<br>• `ServerBaseUrl` 정규화<br>• 자가 검증(Self-Validation) |
| **03** | **실무 계층형 DTO 아키텍처** | [`Practice03.java`](./Practice03.java) | [`README_PRACTICE03.md`](./README_PRACTICE03.md) | • `Request.toCommand()`<br>• `Result.from(entity)`<br>• `Response.from(result)` |

---

## 🔗 관련 프로젝트 참조 코드 (`atworks-lithium`)
- **Controller DTOs:** [`user/controller/request`](../../atworks-lithium/src/main/java/com/apitest/atworks/user/controller/request), [`user/controller/response`](../../atworks-lithium/src/main/java/com/apitest/atworks/user/controller/response)
- **Service DTOs:** [`user/service/command`](../../atworks-lithium/src/main/java/com/apitest/atworks/user/service/command), [`user/service/result`](../../atworks-lithium/src/main/java/com/apitest/atworks/user/service/result)
- **Domain VOs:** [`server/entity/ServerBaseUrl.java`](../../atworks-lithium/src/main/java/com/apitest/atworks/server/entity/ServerBaseUrl.java), [`sender/entity/BodyPayload.java`](../../atworks-lithium/src/main/java/com/apitest/atworks/sender/entity/BodyPayload.java)
