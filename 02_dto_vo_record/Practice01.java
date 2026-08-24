package com.apitest.mytest.dtovorecord;

import java.util.Objects;

/**
 * [Practice 01] Java record 기본 문법 & 불변 객체 (Immutability)
 * 
 * 💡 학습 목표:
 * 1. 기존 Class(Lombok @Getter/@ToString/equals/hashCode) 대비 Java record의 장점과 특징을 이해한다.
 * 2. record 컴팩트 생성자(Compact Constructor)를 활용한 유효성 검증과 데이터 정규화(Trim 등)를 익힌다.
 * 3. record의 불변성(Immutability)과 값 기반 동등성(Value Equality)을 직접 테스트한다.
 */
public class Practice01 {

    // =========================================================================
    // 1. 기본 Record 정의 (간결한 선언)
    // =========================================================================
    // TODO 1: Long id, String name, String email을 필드로 갖는 SimpleUserRecord를 작성해보세요.
    public record SimpleUserRecord(Long id, String name, String email) {
    }

    // =========================================================================
    // 2. 컴팩트 생성자 (Compact Constructor)를 활용한 검증 & 정규화
    // =========================================================================
    public record ValidatedUserRecord(
            Long id,
            String name,
            String email,
            int age
    ) {
        // 컴팩트 생성자: 매개변수 목록 `(Long id, ...)`을 생략하고 검증 및 필드 가공 로직에만 집중
        public ValidatedUserRecord {
            // TODO 2-1: name이 null이거나 공백이면 IllegalArgumentException("이름은 필수입니다.") 던지기
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("이름은 필수입니다.");
            }

            // TODO 2-2: age가 0 미만이면 IllegalArgumentException("나이는 0 이상이어야 합니다.") 던지기
            if (age < 0) {
                throw new IllegalArgumentException("나이는 0 이상이어야 합니다.");
            }

            // TODO 2-3: email의 앞뒤 공백을 제거하고 소문자로 정규화하기
            // (record의 컴팩트 생성자에서는 매개변수 변수명에 바로 재할당하면 final 필드에 자동 대입됨)
            if (email != null) {
                email = email.trim().toLowerCase();
            }
        }

        // 정적 팩토리 메서드 (Static Factory Method)
        public static ValidatedUserRecord of(Long id, String name, String email, int age) {
            return new ValidatedUserRecord(id, name, email, age);
        }
    }

    // =========================================================================
    // 3. 메인 실행 및 미션 테스트
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== 미션 1. SimpleUserRecord 생성 및 기본 기능 확인 ===");
        SimpleUserRecord user1 = new SimpleUserRecord(1L, "conifer", "conifer@kakao.com");
        SimpleUserRecord user2 = new SimpleUserRecord(1L, "conifer", "conifer@kakao.com");

        // 1) 게터 호출: getName()이 아니라 name()으로 호출됨!
        System.out.println("이름 조회: " + user1.name());
        System.out.println("이메일 조회: " + user1.email());

        // 2) toString() 자동 생성 확인
        System.out.println("toString 출력: " + user1);

        // 3) equals() & hashCode() 값 기반 비교 (주소 비교가 아닌 필드 값 비교)
        System.out.println("user1.equals(user2) 결과: " + user1.equals(user2)); // true 기대


        System.out.println("\n=== 미션 2. 컴팩트 생성자 유효성 검증 테스트 ===");
        try {
            // 이름 공백 시 예외 발생 검증
            ValidatedUserRecord invalidUser = new ValidatedUserRecord(2L, "  ", "test@test.com", 20);
        } catch (IllegalArgumentException e) {
            System.out.println("-> [검증 성공] 예외 발생: " + e.getMessage());
        }

        try {
            // 나이 음수 시 예외 발생 검증
            ValidatedUserRecord invalidAgeUser = new ValidatedUserRecord(3L, "홍길동", "hong@test.com", -5);
        } catch (IllegalArgumentException e) {
            System.out.println("-> [검증 성공] 예외 발생: " + e.getMessage());
        }


        System.out.println("\n=== 미션 3. 이메일 정규화 (공백 제거 + 소문자 변환) 테스트 ===");
        ValidatedUserRecord normalizedUser = ValidatedUserRecord.of(4L, "김개발", "  UserAbc@KaKao.COM  ", 25);
        System.out.println("정규화된 이메일: [" + normalizedUser.email() + "]");
        // 기대값: "userabc@kakao.com"
    }
}
