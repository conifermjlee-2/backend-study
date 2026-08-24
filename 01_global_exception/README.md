# 01. 스프링 글로벌 예외 처리 (Global Exception Handling) & Optional

> **💡 메인 학습 목표:**
> 1. 왜 서비스 로직마다 `try-catch`를 쓰지 않고 `orElseThrow()` + `@RestControllerAdvice`를 사용하는지 전체 아키텍처 흐름을 이해한다.
> 2. `Optional` ➔ `ErrorCode & CustomException(RuntimeException)` ➔ `ErrorResponse` ➔ `@RestControllerAdvice`로 이어지는 실무 예외 처리 파이프라인을 체화한다.
> 3. **Checked Exception vs RuntimeException**의 실무적 차이와, **DTO / VO / Entity / Record**의 역할 구분을 명확히 정립한다.

---

## 🏛️ 1. 스프링 글로벌 예외 처리 내부 동작 흐름

스프링 부트 환경에서 Service나 Controller가 예외를 `try-catch`로 잡지 않고 던지면(`throw`), 예외는 자기를 호출한 상위 계층으로 자동 패스(전파/버블링)되어 최상단의 **`DispatcherServlet`**을 거쳐 `@RestControllerAdvice`로 등록된 핸들러에게 전달됩니다.

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
3. Service (비즈니스 로직) ───▶ orElseThrow()로 CustomException 던짐! (try-catch 불필요)
                                      │
                                      ▼ (예외가 상위로 자동 전파/토스)
1. DispatcherServlet (스프링이 예외를 가로챔)
       │
       ▼
4. @RestControllerAdvice (GlobalExceptionHandler) ──▶ 🎣 "잡았다!"
       │
       ▼
[클라이언트에게 정돈된 JSON 응답 반환: 400/404/409 ErrorResponse]
```

---

## ⚖️ 2. 코드 패턴 비교: Before & After

### ❌ 안 좋은 패턴: 모든 곳에 try-catch 남용 & get() 직접 호출
```java
// 서비스/컨트롤러마다 try-catch를 덕지덕지 붙이면 코드가 매우 복잡해지고 유지보수가 어려움
try {
    User user = userRepository.findById(id).get(); // get() 직접 호출 시 NoSuchElementException 위험!
    user.changeName(name);
} catch (NoSuchElementException e) {
    // 적절히 처리하지 못하고 넘어가면 2차 장애 발생
}
```

### ⭕ 권장 패턴: 깔끔한 Optional 체이닝 + 글로벌 핸들러
```java
// [1] 서비스 레이어 : 비즈니스 로직에만 집중 (예외는 던지기만 함)
@Service
public class UserService {
    public void updateUser(Long id, String name) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.changeName(name); // 이 줄부터는 user가 무조건 존재함을 보장받음!
    }
}

// [2] 글로벌 핸들러 : 예외 처리를 한곳에서 모아서 관리
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest req) {
        ErrorCode code = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(code.getStatus(), code.getCode(), e.getMessage(), req.getRequestURI());
        return ResponseEntity.status(code.getStatus()).body(response);
    }
}
```

---

## 🧠 3. 핵심 아키텍처 & 실무 문답 (Deep Dive)

### Q1. 왜 `CustomException`은 그냥 `Exception`이 아니라 `RuntimeException`을 상속받을까?
1. **스프링 트랜잭션(`@Transactional`)의 자동 롤백**: 스프링은 기본적으로 **`RuntimeException`(언체크 예외)이 발생했을 때만 DB를 자동 롤백**합니다.
2. **`throws` 지옥(코드 오염) 방지**: 체크 예외(`Exception`)를 쓰면 모든 메서드에 `throws CustomException`을 줄줄이 붙여야 합니다.
3. **`Optional`의 한 줄 체이닝 유지**: Optional을 만든 목적이 코드 다이어트인데, `orElseThrow`에서 체크 예외를 던지면 또 `try-catch`로 감싸야 하므로 Optional의 존재 이유가 사라집니다.

### Q2. 컴파일러가 에러를 잡는 것과 런타임 에러의 분업 구조는?
* **컴파일러의 핵심 무기 (타입 검사 / 문법 검사)**: 오타, `int`에 `String` 넣는 실수, 메서드명 불일치 등 멍청한 실수를 100% 잡아냅니다. (자바의 생명줄!)
* **런타임 예외의 영역 (비즈니스 상황 처리)**: "잔액 부족", "유저 없음" 등은 코드를 실제로 돌려봐야 알 수 있으므로, `RuntimeException`으로 던져 `@RestControllerAdvice`에서 우아하게 처리합니다.

### Q3. DTO, VO, Entity, Record는 어떻게 구분해서 쓰나요?

| 구분 | 역할 | 특징 | `record` 사용 여부 |
| :--- | :--- | :--- | :---: |
| **VO** *(Value Object)* | 값 그 자체를 표현 (주소, 금액 등) | 식별자(ID) 없음, **수정 불가(불변)** | **무조건 추천 (100% 찰떡)** |
| **DTO** *(Data Transfer)* | 계층 간 데이터 전달 바구니 (Request/Response) | 비즈니스 로직 없음, 데이터 전달용 | **적극 추천 (요즘 대세)** |
| **Entity** *(엔티티)* | DB 테이블과 1:1 매핑되는 주인공 | `@Id` 식별자 필수, DB와 직결 | **절대 금지 ❌** (JPA 프록시/더티체킹 불가) |

---

## 🗺️ 4. 단계별 실습 목차 (Practice Series)

| 번호 | 실습 주제 | 실습 코드 | 상세 가이드 (README) | 진행 상태 |
| :---: | :--- | :---: | :---: | :---: |
| **01** | **Optional 핵심 메서드 정복**<br>(`orElse`, `orElseThrow`, `map`, `filter`, `ifPresent`) | [`Practice01.java`](./Practice01.java) | [`README_PRACTICE01.md`](./README_PRACTICE01.md) | ✅ 완료 |
| **02** | **ErrorCode(Enum) & CustomException 설계**<br>(도메인 에러코드, RuntimeException 상속, filter 연계) | [`Practice02.java`](./Practice02.java) | [`README_PRACTICE02.md`](./README_PRACTICE02.md) | ✅ 완료 |
| **03** | **표준 JSON 에러 응답 객체 (ErrorResponse DTO)**<br>(timestamp, status, code, message, path 규격화) | `Practice03.java` *(예정)* | `README_PRACTICE03.md` *(예정)* | ⏳ 예정 |
| **04** | **@RestControllerAdvice 글로벌 핸들러 완성**<br>(CustomException, @Valid 에러, 500 방어) | `Practice04.java` *(예정)* | `README_PRACTICE04.md` *(예정)* | ⏳ 예정 |

---

## 📚 추가 참고 자료
- [`OptionalAndExceptionExample.java`](./OptionalAndExceptionExample.java) : Optional 대체값/예외 처리 단독 실행 예제
- [`NOTION.md`](./NOTION.md) : 실습 요약 및 실무 Q&A 노트
