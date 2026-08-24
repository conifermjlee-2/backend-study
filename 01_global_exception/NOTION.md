# 01. Java Optional & 스프링 글로벌 예외 처리 (TIL)

> 📅 **학습 일자:** 2026-08-22  
> 🏷️ **태그:** `Java`, `Optional`, `Spring Boot`, `Exception Handling`, `Clean Code`  
> 🎯 **핵심 목표:** `NullPointerException(NPE)` 방어 전략과 실무형 예외 처리 흐름 완벽 이해  

---

## 1. ❓ 왜 `try-catch` 대신 `Optional` + 글로벌 예외 처리인가?

### ❌ 과거/잘못된 패턴의 문제점
* Service나 Controller 곳곳에 `try-catch`를 남발하면 **코드가 비대해지고 비즈니스 로직의 가독성이 급격히 저하**됨.
* `Optional.get()`을 직접 호출하다가 데이터가 없으면 `NoSuchElementException`이 터져 서버 장애로 이어짐.

### ⭕ 현대 스프링의 표준 접근법
1. **Service 레이어:** 비즈니스 핵심 로직에만 집중하고, 데이터가 없으면 **`.orElseThrow()`로 의도된 예외를 던진다.**
2. **`@RestControllerAdvice`:** 상위(`DispatcherServlet`)로 전파된 예외를 한곳에서 가로채 **클라이언트에게 표준화된 JSON 에러 응답(400, 404, 500 등)을 반환**한다.

```
[클라이언트 요청] ──▶ Controller ──▶ Service (.orElseThrow()로 예외 던짐)
                                           │
[JSON 표준 에러 응답] ◀── @RestControllerAdvice ◀── (DispatcherServlet이 예외 가로챔)
```

---

## 2. 📊 Optional 핵심 6대 메서드 선택 기준

| 메서드 | 사용 목적 | 동작 방식 | 비유 / 실무 활용 |
| :--- | :--- | :--- | :--- |
| **`orElse(기본값)`** | 데이터가 없어도 정상 흐름일 때 | 비어있으면 지정된 기본값 반환 | "닉네임이 없으면 '손님'으로 표기" |
| **`orElseGet(Supplier)`** | 기본값 생성에 비용(객체 생성/DB 조회)이 들 때 | **비어있을 때만** 람다를 실행하여 기본값 생성 (지연 로딩) | `.orElseGet(() -> createDefaultUser())` |
| **`orElseThrow(Supplier)`** | **데이터가 없으면 비즈니스 진행 불가(에러)** | 즉시 지정한 예외(`Exception`)를 던지고 작업 중단 | `findUserById(id).orElseThrow(...)` |
| **`map(Function)`** | 객체 내부의 속성을 안전하게 가공/추출할 때 | **값이 있을 때만 가공 실행** (NPE 100% 방지) | `findEmail().map(e -> e.split("@")[0])` |
| **`filter(Predicate)`** | 비즈니스 조건 검증 | 조건 불만족 시 빈 `Optional`로 변환하여 예외로 연결 | `findAge().filter(age -> age >= 19)` |
| **`ifPresent(Consumer)`** | 데이터가 있을 때만 어떤 동작을 수행할 때 | 값이 있을 때만 실행, **없으면 조용히 무시** | 로그인 ID가 있을 때만 화면에 표기 |
| **`ifPresentOrElse(Consumer, Runnable)`** | 있을 때와 없을 때 각각 다른 로직을 처리할 때 | 값 유무에 따른 2-way 분기 실행 | 로그인 성공 시 세션 갱신 / 실패 시 로그아웃 처리 |

---

## 3. 🔥 오늘의 핵심 인사이트 & 실무 Q&A (Troubleshooting)

### Q1. `chkAge > 18` 보다 `chkAge >= 19`가 왜 더 좋은 코드인가요?
* **답변:** 비즈니스 요구사항("19세 이상")의 기준 숫자(`19`)와 연산자(`>=`)가 코드에 직관적으로 드러나기 때문입니다.
* 코드를 읽는 동료의 인지 비용을 줄이고, 나중에 `ADULT_AGE = 19`처럼 상수로 추출할 때도 훨씬 명확합니다.

### Q2. `main()` 메서드에서 부르는 메서드들에 왜 `static`을 붙여야 했나요?
* **답변:** `main()` 자체가 프로그램 시작 시 JVM에 의해 객체 생성(`new`) 없이 메모리에 올라가는 `static` 메서드이기 때문입니다.
* `static` 컨텍스트에서는 아직 `new`로 생성되지 않은 일반 인스턴스 메서드를 부를 수 없습니다.
* *(참고: 실제 스프링 부트에서는 스프링 컨테이너가 Service/Repository 객체를 Bean으로 관리하고 주입해주므로 `static`을 붙이지 않습니다.)*

### Q3. 면접 단골 질문! `orElse()` vs `orElseGet()`의 치명적인 차이는?
* **`orElse(createDefaultUser())`** : Optional에 값이 **이미 존재하더라도** 인자 안의 `createDefaultUser()` 메서드가 **무조건 실행**됩니다. (DB 생성이나 무거운 로직이 불필요하게 실행되는 심각한 리소스 낭비 발생)
* **`orElseGet(() -> createDefaultUser())`** : Optional이 **비어있을 때만** 람다가 지연 실행(Lazy Evaluation)됩니다.  
👉 **실무에서는 동적 생성/메서드 호출 시 무조건 `orElseGet()`을 쓰는 것이 안전합니다.**

### Q4. 인텔리제이에서 `// TODO:`에 초록색/이탤릭 하이라이팅이 들어가는 이유는?
* 인텔리제이의 **Task Tag(작업 태그)** 기능입니다.
* `Alt + 6` 단축키를 누르면 프로젝트 전체의 `TODO`, `FIXME` 주석을 한눈에 모아서 체크리스트로 관리할 수 있습니다.

### Q5. 왜 비즈니스 예외(CustomException)는 무조건 `RuntimeException`을 상속받을까?
1. **스프링 트랜잭션(`@Transactional`)의 기본 규칙:** 스프링은 `RuntimeException`(언체크 예외)이 터져야만 DB를 자동으로 롤백합니다.
2. **`throws` 지옥 탈출:** 체크 예외를 쓰면 서비스/컨트롤러마다 `throws`를 도배해야 하므로 코드가 지저분해집니다.
3. **`Optional.orElseThrow()`와의 찰떡궁합:** Optional을 쓰는 이유가 코드를 깔끔하게 한 줄로 줄이기 위함인데, 체크 예외를 던지면 또 `try-catch`로 감싸야 해서 본래 목적이 퇴색됩니다.

### Q6. DTO, VO, Entity, Record의 명확한 사용 기준은?
* **VO (Value Object):** 수정 불가능한 순수 값 객체 ➡️ **무조건 `record` 추천 (100%)**
* **DTO (Data Transfer Object):** 계층 간 데이터 전달용 택배 상자 ➡️ **`record` 적극 추천 (현대 대세)**
* **Entity:** DB 테이블과 매핑되는 고유 ID(`@Id`) 달린 객체 ➡️ **절대 `record` 불가 ❌** (JPA 프록시/더티체킹 불가)

### Q7. 서비스에서 `try-catch` 안 해도 어떻게 예외 처리가 될까? (예외 전파와 `@RestControllerAdvice`)
* 예외를 안 잡으면 **자기를 호출한 상위 계층으로 자동 토스(전파/버블링)**됩니다.
* `Repository` ➡️ `Service` ➡️ `Controller` ➡️ **`DispatcherServlet`** ➡️ **`@RestControllerAdvice` (최종 낚시꾼)**
* 결국 최상단에 있는 글로벌 핸들러가 딱 낚아채서 400/404 JSON 에러 응답을 클라이언트에게 돌려줍니다.

### Q8. `new CustomException(ErrorCode.USER_NOT_FOUND)` 호출 시 데이터가 어떻게 전달되고 꺼내지나요?
* `super(errorCode.getMessage())`를 통해 부모인 `RuntimeException`에 기본 메시지가 전달되므로 `e.getMessage()`로 메시지를 꺼낼 수 있습니다.
* 동시에 `this.errorCode = errorCode`로 `ErrorCode` Enum 객체 자체를 필드로 저장하므로:
  - `e.getErrorCode().getStatus()` ➔ `404` (HTTP 상태 코드)
  - `e.getErrorCode().getCode()` ➔ `"USER_001"` (클라이언트 협의 에러 코드)
  - `e.getErrorCode().getMessage()` ➔ `"존재하지 않는 회원입니다."`
  - `e.getErrorCode()` ➔ `USER_NOT_FOUND` (Enum 상수 이름)
* 이 세팅 덕분에 스프링의 `@RestControllerAdvice`에서 HTTP 응답 상태값과 JSON 에러 코드를 완벽하게 동적으로 제어할 수 있습니다.

---

## 4. 💻 실전 연습 코드 (`Practice01.java` & `Practice02.java`)

```java
// === [Practice 01] Optional 기본 메서드 ===
// 1. orElse: 기본값 대체
String nickname = getNickname().orElse("손님");

// 2. orElseThrow: 비즈니스 예외 발생
String account = getBankAccount().orElseThrow(() -> new IllegalArgumentException("계좌 정보가 없습니다."));

// 3. map: Null-Safe 데이터 가공
String emailId = findEmail().map(email -> email.split("@")[0]).orElse("unknown");

// 4. filter: 조건 검증 후 예외 연결
Integer age = findUserAge()
        .filter(chkAge -> chkAge >= 19)
        .orElseThrow(() -> new IllegalStateException("19세 미만은 접근 불가합니다."));


// === [Practice 02] ErrorCode Enum & CustomException ===
// 1. 유저 조회 실패 시 비즈니스 예외 발생
String user = findUser("user99")
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

// 2. 이메일 중복 시 비즈니스 예외 발생
if (isEmailExist("test@kakao.com")) {
    throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
}

// 3. 출금 잔액 검증 (filter + orElseThrow)
int withdrawAmount = 10000;
int currentBalance = findAccountBalance()
        .filter(balance -> balance >= withdrawAmount)
        .orElseThrow(() -> new CustomException(ErrorCode.INSUFFICIENT_BALANCE));
```

---

## 5. 🗺️ 다음 실습 로드맵 (Roadmap)

- [x] **Practice 01:** `Optional` 핵심 메서드 정복 (`orElse`, `orElseThrow`, `map`, `filter`, `ifPresent`, `ifPresentOrElse`)
- [x] **Practice 02:** `ErrorCode` Enum & `CustomException` 비즈니스 예외 설계
- [ ] **Practice 03:** 표준 JSON 에러 응답 규격 (`ErrorResponse` DTO) 설계
- [ ] **Practice 04:** `@RestControllerAdvice` & `@ExceptionHandler` 실전 글로벌 핸들러 완성
