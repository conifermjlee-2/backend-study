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

## 2. orElseThrow vs orElse vs ifPresent 선택 기준

| 메서드 | 언제 사용하는가? | 동작 방식 | 비유 |
| :--- | :--- | :--- | :--- |
| **`orElseThrow()`** | **데이터가 없으면 비즈니스 로직을 더 이상 진행할 수 없는 경우 (에러)** | 작업을 즉시 중단하고 예외를 던짐 ➔ 글로벌 핸들러가 400/404 응답 | "회원이 없으면 정보 수정 불가!" |
| **`orElse(기본값)`** | **데이터가 없어도 정상 흐름이며, 기본값으로 채워야 하는 경우** | 비어있으면 대체값(기본값)을 사용하고 로직 정상 진행 | "첫 등록이라 이전 버전 번호가 없으면 1번으로 시작!" |
| **`ifPresent(동작)`** | **데이터가 있을 때만 어떤 동작을 수행하고, 없으면 그냥 지나쳐도 되는 경우** | 값이 있을 때만 람다 실행 | "스케줄 설정이 존재할 때만 비활성화 처리" |

---

## 3. 코드 패턴 비교

### ❌ 안 좋은 패턴: 모든 곳에 try-catch 남용
```java
// 서비스/컨트롤러마다 try-catch를 덕지덕지 붙이면 코드가 매우 복잡해지고 유지보수가 어려움
try {
    User user = userRepository.findById(id).get();
    user.changeName(name);
} catch (NoSuchElementException e) {
    // 여기서 안 멈추고 넘어가면 2차 버그 발생
}
```

### ⭕ 권장 패턴: 깔끔한 Optional + 글로벌 핸들러
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
