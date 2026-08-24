# 📄 [Practice 03] 실무 계층형 DTO 아키텍처 (Request ➔ Command ➔ Result ➔ Response)

> **대상 실습 파일:** [`Practice03.java`](./Practice03.java)  
> **상위 목차:** [`02_dto_vo_record/README.md`](./README.md)  
> **실제 참조 소스:** [`backend/atworks-lithium`](../../atworks-lithium/src/main/java/com/apitest/atworks/user)

---

## 🎯 학습 목표
1. 웹(HTTP) 계층과 비즈니스(Service) 계층의 **책임 분리(Separation of Concerns)** 원칙을 이해한다.
2. 왜 Controller의 `Request`를 Service에 그대로 넘기지 않고 **`Command`**로 변환하는지 체득한다.
3. 왜 JPA `Entity`를 클라이언트에게 직접 반환하지 않고 **`Result` ➔ `Response`**로 감싸야 하는지(보안, 의존성 격리)를 이해한다.

---

## 1. 🏛️ atworks-lithium의 4단계 DTO 파이프라인

```
[클라이언트 JSON]
       │
       ▼
 1. CreateUserRequest (Controller 계층 DTO) ──▶ 웹 유효성 검증 (@Valid, Request 바디 매핑)
       │
       │ .toCommand()
       ▼
 2. CreateUserCommand (Service 계층 DTO)    ──▶ 순수 비즈니스 파라미터 (웹 기술 종속성 0%)
       │
       ▼
    Service (비즈니스 로직 실행 & UserEntity 영속화)
       │
       │ UserResult.from(entity)
       ▼
 3. UserResult (Service 계층 DTO)           ──▶ 비즈니스 실행 결과 (민감정보 제외, 엔티티 은닉)
       │
       │ UserResponse.from(result)
       ▼
 4. UserResponse (Controller 계층 DTO)      ──▶ 최종 클라이언트 JSON 스펙 매핑
       │
       ▼
[클라이언트에게 JSON 반환]
```

---

## 2. ❓ 핵심 의문 & 실무 Q&A

### Q1. DTO를 왜 Request/Command/Result/Response로 4개나 쪼개나요? 번거롭지 않나요?
* **엔티티 노출 방지(보안)**: `Entity`를 그대로 반환하면 `passwordHash`, 내부 DB 컬럼, 불필요한 메타데이터가 외부에 유출되거나 Jackson 직렬화 시 무한 루프(Lazy Loading/양방향 연관관계)가 터집니다.
* **오버포스팅(Over-posting) 공격 방어**: 클라이언트가 수정 요청 시 `role: ADMIN` 같은 필드를 몰래 넣더라도, `Request`와 `Command`에 해당 필드가 없으면 원천 차단됩니다.
* **웹과 비즈니스의 디커플링**: 나중에 HTTP API 외에 배치(Batch)나 메시지 큐(Kafka), 테스트 코드에서 동일한 Service 메서드를 부를 때 웹 어노테이션이 섞이지 않은 `Command`만 넘겨서 호출할 수 있습니다.

### Q2. DTO 변환 시 메서드 명명 규칙은?
* **`toCommand()`**: 현재 객체를 다른 계층 객체로 변환할 때 (인스턴스 메서드)
* **`from(source)`**: 다른 소스로부터 현재 객체를 생성할 때 (정적 팩토리 메서드)

---

## 3. 🧩 실습 미션 요약 (`Practice03.java`)

1. **`CreateUserRequest.toCommand()`**:
   - Controller에서 수신한 `Request`를 `CreateUserCommand`로 매핑
2. **`UserResult.from(MockUserEntity)`**:
   - Service에서 저장된 `MockUserEntity`의 필드를 골라 `UserResult`로 매핑 (비밀번호 제외!)
3. **`UserResponse.from(UserResult)`**:
   - Controller에서 `UserResult`를 받아 클라이언트용 `UserResponse`로 최종 변환
4. 전체 흐름을 `main()` 메서드로 실행하여 콘솔에서 계층별 데이터 흐름 확인
