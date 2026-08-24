# 📄 [Practice 01] Java Optional 핵심 메서드 완벽 정복

> **대상 실습 파일:** [`Practice01.java`](./Practice01.java)  
> **상위 목차:** [`01_global_exception/README.md`](./README.md)

---

## 🎯 학습 목표
1. `NullPointerException (NPE)`을 원천 차단하는 `Optional`의 기본 개념을 이해한다.
2. 실무에서 가장 자주 쓰이는 **Optional 6대 핵심 메서드**의 차이점과 적절한 선택 기준을 체득한다.
3. `orElse()`와 `orElseGet()`의 치명적인 성능/동작 차이를 파악한다.

---

## 1. 📊 Optional 핵심 메서드 선택 기준 & 비교표

| 메서드 | 언제 사용하는가? | 동작 방식 | 실무 예시 |
| :--- | :--- | :--- | :--- |
| **`orElse(기본값)`** | **데이터가 없어도 정상 흐름**이며 고정 기본값을 쓸 때 | 비어있으면 지정된 기본값 반환 | `getNickname().orElse("손님")` (단순 상수/문자열) |
| **`orElseGet(Supplier)`** | **기본값 생성에 비용(DB조회/객체생성)**이 들 때 | 비어있을 때만 지연(Lazy) 실행하여 기본값 생성 | `.orElseGet(() -> createDefaultUser())` |
| **`orElseThrow(Supplier)`** | **데이터가 없으면 비즈니스 진행 불가 (에러)** | 비어있으면 즉시 예외(`Exception`)를 던지고 중단 | `userRepository.findById(id).orElseThrow(...)` |
| **`map(Function)`** | **객체 내부의 특정 필드/값을 안전하게 가공할 때** | 값이 있을 때만 가공 실행 (NPE 원천 차단) | `.map(email -> email.split("@")[0])` |
| **`filter(Predicate)`** | **비즈니스 조건을 검증하고 걸러낼 때** | 조건을 만족하지 못하면 빈 Optional로 변환 | `.filter(age -> age >= 19)` |
| **`ifPresent(Consumer)`** | **값이 있을 때만 어떤 액션을 취하고, 없으면 무시** | 값이 있을 때만 Consumer 실행 | `.ifPresent(id -> log.info("접속: " + id))` |
| **`ifPresentOrElse(Consumer, Runnable)`** | **있을 때와 없을 때의 행동을 2-way 분기할 때** | 있을 때는 Consumer, 없을 때는 Runnable 실행 | 로그인 성공 시 세션 갱신 / 실패 시 에러 로그 출력 |

---

## 2. ⚠️ [면접 & 실무 단골] `orElse()` vs `orElseGet()`의 치명적 차이

* **`orElse(메서드호출())`**: Optional에 값이 **이미 존재하더라도** 인자 안의 `메서드호출()`이 **무조건 실행**됩니다.
  ```java
  // User가 이미 존재해도 createNewUser()가 실행되어 불필요한 DB Insert/객체 생성이 발생할 수 있음!
  User user = userRepository.findById(id).orElse(createNewUser());
  ```
* **`orElseGet(() -> 메서드호출())`**: Optional이 **비어있을 때만** 람다가 지연 실행(Lazy Evaluation)되어 안전합니다.
  ```java
  // User가 없을 때만 createNewUser()가 실행됨 (권장)
  User user = userRepository.findById(id).orElseGet(() -> createNewUser());
  ```
> 💡 **가이드:** 단순 리터럴 상수(`"손님"`, `0L`)는 `orElse()`를 써도 무방하지만, **메서드 호출이나 객체 생성이 수반된다면 무조건 `orElseGet()`을 사용**하세요.

---

## 3. 🧩 실습 미션 해설 (`Practice01.java`)

### 미션 1. `orElse` 기본값 세팅
```java
// getNickname()이 비어있으면 기본값 "손님" 반환
String nickname = getNickname().orElse("손님");
```

### 미션 2. `orElseThrow` 필수 데이터 부재 시 예외 던지기
```java
// 계좌 정보가 없으면 비즈니스 예외 발생 (try-catch 대신 상위로 전파)
String account = getBankAccount()
        .orElseThrow(() -> new IllegalArgumentException("계좌 정보가 존재하지 않습니다."));
```

### 미션 3. `map` + `orElse` 안전한 데이터 가공 및 Null 방어
```java
// 이메일이 존재하면 "@" 앞의 아이디만 추출, 없으면 "unknown"
String emailId = findEmail()
        .map(email -> email.split("@")[0])
        .orElse("unknown");
```

### 미션 4. `filter` + `orElseThrow` 조건 검증 파이프라인
```java
// 19세 이상만 통과, 미달하거나 값이 없으면 예외 발생
Integer age = findUserAge()
        .filter(chkAge -> chkAge >= 19)
        .orElseThrow(() -> new IllegalStateException("19세 미만은 이용할 수 없습니다."));
```
> 💡 **코드 가독성 팁:** `chkAge > 18`보다 `chkAge >= 19`가 비즈니스 요구사항("19세 이상")을 훨씬 직관적으로 드러냅니다.

### 미션 5. `ifPresentOrElse` (Java 9+) 2-way 분기 처리
```java
// 존재 시 로그인 성공 처리, 부재 시 실패 처리
findLoginId().ifPresentOrElse(
        id -> System.out.println("로그인 성공: " + id),
        () -> System.out.println("로그인 실패: 세션이 만료되었습니다.")
);
```

---

## 4. 💡 다음 단계 안내
Optional의 기본기를 마쳤다면, 이제 단순 `IllegalArgumentException`을 넘어 서비스 전용 에러 코드와 비즈니스 예외를 설계하는 **Practice 02**로 이동합니다.

👉 **다음 실습:** [`README_PRACTICE02.md`](./README_PRACTICE02.md)
