# 📄 [Practice 02] ErrorCode Enum & CustomException 비즈니스 예외 설계

> **대상 실습 파일:** [`Practice02.java`](./Practice02.java)  
> **상위 목차:** [`01_global_exception/README.md`](./README.md)

---

## 🎯 학습 목표
1. 왜 `IllegalArgumentException` 같은 자바 표준 예외만으로는 실무 서비스를 개발하기 어려운지 이해한다.
2. HTTP 상태 코드(`status`), 고유 비즈니스 에러 코드(`code`), 기본 메시지(`message`)를 하나로 묶는 **`ErrorCode` Enum**을 설계한다.
3. **`RuntimeException`을 상속**하는 프로젝트 전용 `CustomException`을 구현하고, `Optional.orElseThrow()`와 유기적으로 결합한다.
4. `CustomException` 인스턴스에서 에러 코드(`code`), HTTP 상태값(`status`), 메시지(`message`)가 어떻게 추출되어 처리되는지 내부 동작 원리를 파악한다.

---

## 1. ❓ 왜 표준 예외 대신 커스텀 예외와 ErrorCode를 쓸까?

### ❌ 표준 예외의 한계
* `IllegalArgumentException`이나 `NoSuchElementException`은 HTTP 상태 코드가 몇 번이어야 하는지(400인지, 404인지, 409인지), 클라이언트와 약속한 고유 에러 코드(예: `USER_001`)가 무엇인지 알 수 없습니다.
* 발생한 위치마다 에러 메시지 문자열을 하드코딩해야 하므로 메시지가 일관되지 않고 변경에 취약합니다.

### ⭕ ErrorCode + CustomException 체계의 장점
1. **중앙 집중 관리**: 서비스 내 모든 에러 규격을 `ErrorCode` Enum 하나에서 관리합니다.
2. **명확한 클라이언트 소통**: 프론트엔드는 HTTP Status와 고유 에러 코드(`USER_NOT_FOUND`)를 보고 정확한 UI 처리가 가능해집니다.
3. **간결한 예외 발생**: 비즈니스 로직에서는 `throw new CustomException(ErrorCode.USER_NOT_FOUND);` 한 줄로 의도를 표현합니다.

---

## 2. 🏛️ 핵심 아키텍처 및 데이터 흐름 분석

`new CustomException(ErrorCode.USER_NOT_FOUND)`를 실행했을 때 내부에서 데이터가 흐르고 저장되는 구조입니다.

### 🔄 내부 동작 흐름 다이어그램

```text
[ 1. ErrorCode.USER_NOT_FOUND 생성/선언 ]
   ├── status  : 404
   ├── code    : "USER_001"
   └── message : "존재하지 않는 회원입니다."
       │
       ▼
[ 2. new CustomException(errorCode) 호출 ]
   ├── super(errorCode.getMessage())  ──▶ 부모 RuntimeException에 메시지 등록 (e.getMessage() 사용 가능)
   └── this.errorCode = errorCode     ──▶ CustomException 내부 필드에 ErrorCode 통째로 보관
       │
       ▼
[ 3. catch (CustomException e) 에서 꺼내 쓰기 ]
   ├── e.getErrorCode().getStatus()   ──▶ 404 (HTTP 상태 코드)
   ├── e.getErrorCode().getCode()     ──▶ "USER_001" (비즈니스 에러 코드)
   ├── e.getErrorCode().getMessage()  ──▶ "존재하지 않는 회원입니다."
   ├── e.getMessage()                 ──▶ "존재하지 않는 회원입니다." (부모 클래스 메시지)
   └── e.getErrorCode()               ──▶ USER_NOT_FOUND (Enum 상수)
```

---

### 🔍 클래스 설계 구조

```java
// 1. 에러 코드 Enum
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "COMMON_001", "잘못된 입력값입니다."),
    USER_NOT_FOUND(404, "USER_001", "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(409, "USER_002", "이미 사용 중인 이메일입니다."),
    INSUFFICIENT_BALANCE(400, "ACCOUNT_001", "계좌 잔액이 부족합니다.");

    private final int status;       // HTTP 상태 코드 (400, 404, 409 등)
    private final String code;      // 클라이언트와 협의한 고유 에러 코드
    private final String message;   // 에러 설명 기본 메시지

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}

// 2. 비즈니스 런타임 예외 클래스
public static class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 RuntimeException에 기본 메시지 전달
        this.errorCode = errorCode;
    }

    // 필요 시 메시지를 직접 커스텀할 수 있는 오버로딩 생성자
    public CustomException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
```

---

### 🖥️ 실제 Catch 블록 추출 & 콘솔 출력 비교

```java
try {
    String user = findUser("user99")
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
} catch (CustomException e) {
    // 1. ErrorCode 객체를 꺼내서 개별 필드(getter) 호출
    int httpStatus = e.getErrorCode().getStatus(); // 404
    String errorCode = e.getErrorCode().getCode();  // "USER_001"
    
    // 2. 부모 RuntimeException의 getMessage() 호출
    String message = e.getMessage();                // "존재하지 않는 회원입니다."

    System.out.println("-> [가로챔] 에러코드: " + e.getErrorCode().getCode()
            + " | HTTP 상태: " + e.getErrorCode().getStatus()
            + " | 메시지: " + e.getMessage());

    System.out.println("에러코드 : " + e.getErrorCode());
}
```

#### 📌 콘솔 출력 결과:
```text
-> [가로챔] 에러코드: USER_001 | HTTP 상태: 404 | 메시지: 존재하지 않는 회원입니다.
에러코드 : USER_NOT_FOUND
```

---

### 💡 실무 연계: Spring Boot에서 이 패턴을 사용하는 이유

스프링의 `@RestControllerAdvice` / `@ExceptionHandler(CustomException.class)`에서 예외를 가로채 클라이언트에게 일관된 JSON 에러 응답을 만들 때 `status`와 `code`를 직관적으로 활용할 수 있습니다.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        
        return ResponseEntity
                .status(errorCode.getStatus()) // HTTP 상태 코드 (404, 409 등)
                .body(new ErrorResponse(
                        errorCode.getCode(),   // 비즈니스 고유 코드 ("USER_001")
                        e.getMessage()         // 에러 메시지 ("존재하지 않는 회원입니다.")
                ));
    }
}
```

---

## 3. 🧩 실습 미션 해설 & 가이드 (`Practice02.java`)

### 미션 1. 회원 조회 실패 시 `USER_NOT_FOUND` 예외 던지기
* **상황:** ID로 유저를 조회했을 때 데이터가 없으면 404 Not Found에 해당하는 비즈니스 예외 발생
```java
String user = findUser("user99")
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
```

---

### 미션 2. 이메일 중복 시 `DUPLICATE_EMAIL` 예외 던지기
* **상황:** 이미 가입된 이메일로 회원가입 시도 시 409 Conflict에 해당하는 예외 발생
```java
if (isEmailExist("test@kakao.com")) {
    throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
}
```

---

### 미션 3. 출금 잔액 검증 (`filter` + `orElseThrow`)
* **상황:** 현재 잔액(5,000원)이 출금 요청액(10,000원)보다 작으면 `INSUFFICIENT_BALANCE` 예외 발생
```java
int withdrawAmount = 10000;

int currentBalance = findAccountBalance()
        .filter(balance -> balance >= withdrawAmount)
        .orElseThrow(() -> new CustomException(ErrorCode.INSUFFICIENT_BALANCE));
```

---

## 4. 💡 다음 단계 안내
비즈니스 예외(`CustomException`)와 `ErrorCode`를 정의했으므로, 다음 단계에서는 이 예외들을 클라이언트에게 예쁜 JSON 규격으로 내려주기 위한 **`ErrorResponse` DTO**를 설계합니다.

👉 **다음 실습 예정:** `Practice 03: ErrorResponse DTO 표준 규격 설계`

