package com.apitest.mytest.dtovorecord;

import java.net.URI;
import java.util.regex.Pattern;

/**
 * [Practice 02] 도메인 값 객체(Value Object, VO) 패턴 & 원시값 포장
 * 
 * 💡 학습 목표:
 * 1. 단순 String, int 등의 원시값 집착(Primitive Obsession)을 탈피하고 VO로 감싸는 이유를 체득한다.
 * 2. VO의 4대 핵심 특징(불변성, 동등성, 자가 검증, 식별자 없음)을 이해한다.
 * 3. 실제 atworks-lithium 프로젝트의 `ServerBaseUrl`과 같은 실무형 VO를 record로 구현한다.
 */
public class Practice02 {

    // =========================================================================
    // 1. Email VO (자가 검증 + 도메인 유틸 메서드)
    // =========================================================================
    public record Email(String value) {
        private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        public Email {
            // TODO 1-1: value가 null이거나 공백이면 IllegalArgumentException("이메일은 필수입니다.")
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("이메일은 필수입니다.");
            }

            value = value.trim().toLowerCase();

            // TODO 1-2: 정규식 패턴에 맞지 않으면 IllegalArgumentException("올바른 이메일 형식이 아닙니다.")
            if (!EMAIL_PATTERN.matcher(value).matches()) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
            }
        }

        // 도메인 편의 메서드: 도메인 부분(@ 뒤)만 추출
        public String getDomain() {
            return value.substring(value.indexOf("@") + 1);
        }
    }

    // =========================================================================
    // 2. Money VO (금액 연산 및 안전성 보장)
    // =========================================================================
    public record Money(long amount) {
        public Money {
            // TODO 2-1: amount가 음수이면 IllegalArgumentException("금액은 0원 이상이어야 합니다.")
            if (amount < 0) {
                throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
            }
        }

        public static Money zero() {
            return new Money(0);
        }

        public static Money of(long amount) {
            return new Money(amount);
        }

        // TODO 2-2: 더하기 연산 (새로운 Money 객체 반환 - 불변성 유지)
        public Money plus(Money other) {
            return new Money(this.amount + other.amount);
        }

        // TODO 2-3: 빼기 연산 (잔액 부족 시 IllegalArgumentException("잔액이 부족합니다."))
        public Money minus(Money other) {
            if (this.amount < other.amount) {
                throw new IllegalArgumentException("잔액이 부족합니다.");
            }
            return new Money(this.amount - other.amount);
        }
    }

    // =========================================================================
    // 3. atworks-lithium 실전형 VO: ServerBaseUrl
    // =========================================================================
    public record ServerBaseUrl(String value) {
        public ServerBaseUrl {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("baseUrl은 필수입니다.");
            }

            value = value.trim();

            URI uri;
            try {
                uri = URI.create(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("올바른 URL 형식이 아닙니다.", e);
            }

            String scheme = uri.getScheme();
            if (!uri.isAbsolute() || uri.getHost() == null ||
                !("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) {
                throw new IllegalArgumentException("baseUrl은 http 또는 https 프로토콜을 포함해야 합니다.");
            }

            // 끝에 붙은 '/' (trailing slash) 자동 제거 정규화
            value = value.replaceFirst("/+$", "");
        }
    }

    // =========================================================================
    // 4. 메인 실행 및 미션 테스트
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== 미션 1. Email VO 검증 및 도메인 추출 테스트 ===");
        try {
            new Email("invalid-email");
        } catch (IllegalArgumentException e) {
            System.out.println("-> [검증 성공] 잘못된 이메일 예외: " + e.getMessage());
        }

        Email validEmail = new Email("  Developer@Kakao.COM  ");
        System.out.println("정규화된 이메일: " + validEmail.value());
        System.out.println("추출된 도메인: " + validEmail.getDomain());


        System.out.println("\n=== 미션 2. Money VO 연산 및 불변성 테스트 ===");
        Money wallet = Money.of(10000);
        Money salary = Money.of(50000);
        Money total = wallet.plus(salary);

        System.out.println("기존 지갑 금액(불변 유지): " + wallet.amount() + "원");
        System.out.println("합산 금액: " + total.amount() + "원");

        try {
            Money overWithdraw = wallet.minus(Money.of(20000));
        } catch (IllegalArgumentException e) {
            System.out.println("-> [검증 성공] 잔액 부족 예외: " + e.getMessage());
        }


        System.out.println("\n=== 미션 3. atworks ServerBaseUrl 정규화 테스트 ===");
        ServerBaseUrl url = new ServerBaseUrl("https://api.lithium.atworks.com/v1///");
        System.out.println("정규화된 BaseUrl: " + url.value());
        // 기대값: "https://api.lithium.atworks.com/v1"
    }
}
