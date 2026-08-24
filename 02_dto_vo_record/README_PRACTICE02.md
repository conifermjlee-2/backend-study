# 📄 [Practice 02] 값 객체 (Value Object, VO) 패턴 & 원시값 포장

> **대상 실습 파일:** [`Practice02.java`](./Practice02.java)  
> **상위 목차:** [`02_dto_vo_record/README.md`](./README.md)

---

## 🎯 학습 목표
1. **원시값 집착(Primitive Obsession)** 안티패턴의 위험성을 이해한다.
2. **값 객체(VO: Value Object)**의 4대 핵심 특징(식별자 부재, 불변성, 자가 검증, 동등성)을 익힌다.
3. 실제 `atworks-lithium` 프로젝트에서 사용되는 `ServerBaseUrl`, `Email`, `Money` VO를 직접 작성해본다.

---

## 1. ❓ 왜 String이나 int 대신 VO를 만들어야 할까?

### ❌ 원시값 집착(Primitive Obsession)의 문제점
```java
// String 타입 파라미터가 연속으로 나열되면 순서를 바꿔 넣어도 컴파일 에러가 나지 않음!
public void registerUser(String email, String address, String nickname) { ... }

// 실수로 이메일 자리에 주소를 넣어도 정상 동작해버리는 치명적 버그 발생
registerUser("서울시 강남구 테헤란로", "user@kakao.com", "개발자");
```
* 유효성 검증 로직이 서비스/컨트롤러 곳곳에 중복으로 흩어짐.
* 문자열 조작 로직(`trim`, `@` 뒤 도메인 추출 등)이 여러 군데 파편화됨.

### ⭕ VO (Value Object) 도입 효과
```java
public void registerUser(Email email, Address address, Nickname nickname) { ... }
```
1. **타입 안전성 (Type Safety)**: 파라미터 순서 실수가 컴파일 타임에 즉시 잡힘.
2. **자가 검증 (Self-Validation)**: `new Email("invalid")` 시점에 즉시 예외가 터지므로, 시스템 내부에는 항상 유효한 값만 존재함.
3. **도메인 로직 응집**: `email.getDomain()`, `money.plus(other)`처럼 값에 종속된 로직을 객체 스스로 수행.

---

## 2. 🏛️ Entity vs DTO vs VO 3대 개념 비교

| 구분 | Entity (엔티티) | VO (값 객체) | DTO (데이터 전송 객체) |
| :--- | :--- | :--- | :--- |
| **식별자 (ID)** | 고유 식별자 있음 (`@Id Long id`) | **식별자 없음 (값 자체가 정체성)** | 식별자 여부 무관 |
| **가변성** | 상태 변경 가능 (Mutable) | **완전한 불변 (Immutable)** | 대개 불변 (Record 권장) |
| **동등성 판단** | ID(PK)가 같으면 같은 객체 | **모든 필드 값이 같으면 같은 객체** | 값 전달 목적 |
| **비즈니스 로직** | 핵심 도메인 상태 변경 로직 포함 | **값 자체에 대한 연산/검증 포함** | **로직 없음 (순수 데이터 전달)** |
| **실무 예시** | `User`, `Server`, `Project` | `Email`, `Money`, `ServerBaseUrl` | `CreateUserRequest`, `UserResponse` |

---

## 3. 🧩 실습 미션 요약 (`Practice02.java`)

1. **미션 1: `Email` VO**
   - 정규식 기반 유효성 검증 (`^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$`)
   - 앞뒤 공백 제거 및 소문자 정규화
   - `getDomain()` 편의 메서드 동작 확인
2. **미션 2: `Money` VO**
   - 음수 금액 방어 검증
   - `plus()`, `minus()` 연산 시 기존 객체를 변경하지 않고 새로운 `Money` 반환 (불변성)
   - 잔액 부족 시 `IllegalArgumentException` 발생
3. **미션 3: `atworks-lithium` 실전형 `ServerBaseUrl` VO**
   - `http://` 또는 `https://` 프로토콜 검증
   - URL 끝부분의 슬래시(`/+`) 자동 정규화

---

## 4. 💡 다음 단계 안내
VO로 도메인 값을 안전하게 포장했다면, 이제 컨트롤러와 서비스 계층 간에 데이터를 어떻게 깔끔하게 주고받는지 실무 계층 분리 DTO(`Request ➔ Command ➔ Result ➔ Response`)를 배울 차례입니다.

👉 **다음 실습:** [`README_PRACTICE03.md`](./README_PRACTICE03.md)
