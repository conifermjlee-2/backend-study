# 01. 스프링 글로벌 예외 처리 (Global Exception Handling) & Optional

> **학습 목표:**
> 1. 왜 `try-catch` 대신 `orElseThrow()` + `@RestControllerAdvice`를 사용하는지 이해한다.
> 2. 스프링이 예외를 가로채서 처리하는 내부 흐름(DispatcherServlet ➔ HandlerExceptionResolver)을 이해한다.
> 3. `orElseThrow()` vs `orElse()` / `ifPresent()`의 적절한 사용 기준을 익힌다.

---

## 1. 한눈에 보는 핵심 개념

### ① 예외 전파와 글로벌 예외 처리
- **일반 Java (main):** 예외를 catch하지 않으면 프로그램이 비정상 종료(Crash)됨.
- **Spring Boot:** Service나 Controller에서 `try-catch`를 하지 않고 던진 예외(`throw`)는 최상단의 **`DispatcherServlet`**까지 전파됨.
- **`@RestControllerAdvice`:** 스프링이 시작될 때 `@ExceptionHandler` 메서드들을 스캔하여 예외 처리 지도를 만들어 두고, 발생한 예외에 맞는 메서드를 실행하여 **클라이언트에게 표준화된 JSON 에러 응답(400, 404, 500 등)**을 반환함.

```
[클라이언트 요청]
       │
       ▼
1. DispatcherServlet (스프링 총괄 컨트롤러)
       │
       ▼
2. Controller
       │
       ▼
3. Service (비즈니스 로직) ───▶ orElseThrow()로 예외 던짐! (try-catch 필요 없음)
                                      │
                                      ▼ (예외가 상위로 전파)
1. DispatcherServlet (스프링이 가로챔)
       │
       ▼
4. @RestControllerAdvice (GlobalExceptionHandler)가 실행됨
       │
       ▼
[클라이언트에게 정돈된 JSON 응답 반환: 400/404 ErrorResponse]
```

---

## 2. Optional 핵심 메서드 선택 기준 & 비교표

| 메서드 | 언제 사용하는가? | 동작 방식 | 실무 예시 |
| :--- | :--- | :--- | :--- |
| **`orElseThrow()`** | **데이터가 없으면 진행 불가(에러 발생)** | 비어있으면 즉시 예외(`Exception`)를 던지고 중단 | `userRepository.findById(id).orElseThrow(...)` |
| **`orElse(기본값)`** | **데이터가 없어도 정상 흐름이며 고정 기본값 사용** | 비어있으면 대체 기본값 반환 | `.orElse("손님")` (단순 상수/문자열) |
| **`orElseGet(Supplier)`** | **기본값 생성에 비용(DB조회/객체생성)이 들 때** | 비어있을 때만 지연(Lazy) 실행하여 기본값 생성 | `.orElseGet(() -> createDefaultUser())` |
| **`map(Function)`** | **객체 내부의 특정 필드/값을 안전하게 가공할 때** | 값이 있을 때만 가공 실행 (NPE 원천 차단) | `.map(email -> email.split("@")[0])` |
| **`filter(Predicate)`** | **조건에 맞지 않는 데이터를 걸러낼 때** | 조건을 만족하지 못하면 빈 Optional로 변환 | `.filter(age -> age >= 19)` |
| **`ifPresent(동작)`** | **값이 있을 때만 어떤 액션을 취하고, 없으면 무시** | 값이 있을 때만 Consumer 실행 | `.ifPresent(id -> log.info("접속: " + id))` |
| **`ifPresentOrElse(동작A, 동작B)`** | **있을 때와 없을 때의 행동을 분기 처리할 때** | 있을 때는 A(Consumer), 없을 때는 B(Runnable) 실행 | 회원 존재 시 업데이트, 없으면 신규 가입 안내 |

> ⚠️ **[중요] `orElse()` vs `orElseGet()` 차이점**
> * `orElse(메서드호출())` : Optional에 값이 **이미 존재해도** 인자 안의 `메서드호출()`이 무조건 실행됩니다. (불필요한 리소스 낭비 위험)
> * `orElseGet(() -> 메서드호출())` : Optional이 **비어있을 때만** 람다가 실행되어 안전합니다. (실무 권장)

---

## 3. 코드 패턴 비교

### ❌ 안 좋은 패턴: 모든 곳에 try-catch 남용 & get() 직접 호출
```java
// 서비스/컨트롤러마다 try-catch를 덕지덕지 붙이면 코드가 매우 복잡해지고 유지보수가 어려움
try {
    User user = userRepository.findById(id).get(); // get() 직접 호출 시 NoSuchElementException 위험!
    user.changeName(name);
} catch (NoSuchElementException e) {
    // 여기서 안 멈추고 넘어가면 2차 버그 발생
}
```

### ⭕ 권장 패턴: 깔끔한 Optional 체이닝 + 글로벌 핸들러
```java
// [1] 서비스 레이어 : 비즈니스 로직에만 집중
@Service
public class UserService {
    public void updateUser(Long id, String name) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        user.changeName(name);
    }
}

// [2] 글로벌 핸들러 : 예외 처리를 한 곳에서 모아서 관리
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e, HttpServletRequest req) {
        ErrorResponse response = ErrorResponse.of(400, "BAD_REQUEST", e.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
```

---

## 4. 단계별 실습 로드맵 (Practice Series)

아래 순서대로 실습 클래스를 작성하고 완성해 나가면 스프링 글로벌 예외 처리 구조를 100% 마스터할 수 있습니다.

```
📁 01_global_exception
 ├── 📄 Practice01.java  ──▶ [완료] Optional 핵심 메서드 정복 (orElse, orElseThrow, map, filter, ifPresent, ifPresentOrElse)
 ├── 📄 Practice02.java  ──▶ [예정] ErrorCode(Enum) & CustomException(비즈니스 예외) 설계
 ├── 📄 Practice03.java  ──▶ [예정] 일관된 JSON 에러 응답 객체 (ErrorResponse DTO) 설계
 └── 📄 Practice04.java  ──▶ [예정] @RestControllerAdvice 글로벌 예외 핸들러 실전 완성
```

### 📌 Practice 01. `Optional` 핵심 메서드 정복 (완료)
* **목표:** NPE(NullPointerException)를 방지하고, 상황에 맞는 함수형 메서드를 선택하는 역량 습득.
* **완성된 핵심 실습 패턴:**
  1. `orElse("손님")` : 데이터가 없을 때 기본 문자열 대체.
  2. `orElseThrow(() -> new IllegalArgumentException(...))` : 필수 데이터 부재 시 비즈니스 예외 발생.
  3. `map(email -> email.split("@")[0])` : Null-Safe하게 이메일 아이디 추출 및 가공.
  4. `filter(age -> age >= 19)` : 19세 이상 조건 검증 후 미달 시 예외로 연결.
  5. `ifPresent(id -> ...)` : 존재할 때만 실행하고 없을 때는 무시.
  6. `ifPresentOrElse(존재할때동작, 없을때동작)` : 존재 여부에 따른 깔끔한 2-way 분기 처리.

---

### 📌 Practice 02. `ErrorCode` Enum & `CustomException` 설계 (다음 단계)
* **목표:** 표준 예외(`IllegalArgumentException` 등)의 한계를 극복하고, 서비스 전용 비즈니스 예외 체계 구축.
* **학습 내용:**
  * 왜 표준 예외만 쓰면 부족할까? (도메인별 구체적인 에러 코드와 상태 분기 필요)
  * `ErrorCode` Enum 설계: `HttpStatus`, `code` (예: `USER_NOT_FOUND`), `message` 포함
  * `CustomException` (또는 `BusinessException`) 구현: `RuntimeException` 상속

---

### 📌 Practice 03. 표준 에러 응답 객체 (`ErrorResponse` DTO)
* **목표:** 프론트엔드/클라이언트와 소통할 일관된 규격의 JSON 응답 포맷 정의.
* **포함 필드:**
  * `timestamp` : 에러 발생 시각
  * `status` : HTTP 상태 코드 (400, 404, 500 등)
  * `code` : 비즈니스 에러 코드 (예: `U001`, `INVALID_INPUT_VALUE`)
  * `message` : 사용자 친화적 에러 메시지
  * `path` : 요청 경로 (`/api/users/123`)

---

### 📌 Practice 04. `@RestControllerAdvice` 글로벌 예외 핸들러 완성
* **목표:** 서비스 전역에서 던져진 예외를 가로채서 `ErrorResponse`로 변환해주는 총괄 컨트롤러 구현.
* **처리 대상 예외:**
  1. `CustomException` : 비즈니스 로직 상의 의도된 예외 (400, 404 등)
  2. `MethodArgumentNotValidException` : DTO 유효성 검사(`@Valid`) 실패 예외
  3. `Exception` : 미처 예상하지 못한 서버 내부 오류 (500 Internal Server Error) 로깅 및 방어


