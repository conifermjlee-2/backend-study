# 📄 [Practice 01] Java Record 기본 문법 & 불변 객체 (Immutability)

> **대상 실습 파일:** [`Practice01.java`](./Practice01.java)  
> **상위 목차:** [`02_dto_vo_record/README.md`](./README.md)

---

## 🎯 학습 목표
1. Java 16+ 정식 스펙으로 도입된 **`record`의 본질과 등장 배경**을 이해한다.
2. 기존 Lombok(`@Getter`, `@ToString`, `@EqualsAndHashCode`, `@AllArgsConstructor`) 기반 DTO와의 차이점을 파악한다.
3. **컴팩트 생성자(Compact Constructor)**를 통해 인자 검증 및 전처리(정규화)를 깔끔하게 처리하는 법을 익힌다.

---

## 1. ❓ Java Record란 무엇인가?

`record`는 **"순수하게 불변 데이터(Immutable Data)를 전달하기 위한 데이터 운반체(Data Carrier)"**입니다.

### ⚖️ 기존 Class vs Record 비교

| 구분 | 기존 Class (+ Lombok) | Java Record |
| :--- | :--- | :--- |
| **선언 방식** | `public class UserDto { ... }` + 어노테이션 4~5개 | `public record UserDto(Long id, String name) {}` |
| **불변성 (Immutability)** | 필드마다 `final` 선언 필요 | **모든 필드가 기본적으로 `private final`** |
| **게터 메서드** | `getName()`, `getId()` | **`name()`, `id()`** (`get` 접두사 없음) |
| **상속** | 다른 클래스 상속 가능 | `java.lang.Record`를 암묵적 상속 (다른 클래스 상속 불가, 인터페이스 구현은 가능) |
| **동등성 비교** | `@EqualsAndHashCode` 필요 | 모든 필드 값을 기준으로 `equals()`, `hashCode()` 자동 생성 |

---

## 2. 🔍 핵심 문법: 컴팩트 생성자 (Compact Constructor)

일반 클래스에서는 생성자에서 검증 로직을 작성할 때 `this.name = name;` 같은 필드 대입 코드가 필요하지만, `record`는 **매개변수 목록을 생략한 컴팩트 생성자**를 제공합니다.

```java
public record UserRecord(String name, String email, int age) {

    // 컴팩트 생성자: (String name, ...) 매개변수 선언을 생략
    public UserRecord {
        // [1] 유효성 검증 (Validation)
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        if (age < 0) {
            throw new IllegalArgumentException("나이는 0 이상이어야 합니다.");
        }

        // [2] 데이터 정규화 (Normalization)
        // 변수에 재할당하면 생성자 종료 시점에 자동으로 final 필드에 대입됨!
        if (email != null) {
            email = email.trim().toLowerCase();
        }
    }
}
```

---

## 3. 🧩 실습 미션 요약 (`Practice01.java`)

1. **미션 1: `SimpleUserRecord` 기본 기능 테스트**
   - `name()`, `email()` 게터 호출 방식 확인
   - `user1.equals(user2)` 값 기반 동등성 확인
2. **미션 2: 컴팩트 생성자 유효성 검증 테스트**
   - 이름 공백 / 나이 음수 입력 시 `IllegalArgumentException` 발생 여부 확인
3. **미션 3: 이메일 정규화 테스트**
   - `"  UserAbc@KaKao.COM  "` ➔ `"userabc@kakao.com"`으로 공백 제거 및 소문자 변환 확인

---

## 4. 💡 다음 단계 안내
Record의 기본 문법과 불변성을 이해했다면, 이제 실무 도메인 주도 설계(DDD)에서 가장 중요한 개념 중 하나인 **값 객체(Value Object, VO)**를 Record로 구현하는 Practice 02로 넘어갑니다.

👉 **다음 실습:** [`README_PRACTICE02.md`](./README_PRACTICE02.md)
