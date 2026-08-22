package com.apitest.mytest.globalexception;

import java.util.Optional;

/**
 * [Practice 02] ErrorCode(Enum)와 비즈니스 커스텀 예외(CustomException) 설계
 * 
 * 💡 학습 목표:
 * 1. 단순 IllegalArgumentException 대신, 왜 서비스 전용 ErrorCode와 CustomException을 만드는지 이해한다.
 * 2. HTTP 상태 코드(400, 404, 409 등)와 비즈니스 에러 코드(USER_NOT_FOUND 등)를 Enum으로 묶어 관리한다.
 * 3. RuntimeException을 상속받는 비즈니스 예외를 설계하고, Optional.orElseThrow()와 연결한다.
 */
public class Practice02 {

    // =========================================================================
    // 1. 에러 코드 정의 (ErrorCode Enum)
    // =========================================================================
    public enum ErrorCode {
        // HTTP 상태코드, 비즈니스 에러코드, 기본 메시지
        INVALID_INPUT_VALUE(400, "COMMON_001", "잘못된 입력값입니다."),
        USER_NOT_FOUND(404, "USER_001", "존재하지 않는 회원입니다."),
        DUPLICATE_EMAIL(409, "USER_002", "이미 사용 중인 이메일입니다."),
        INSUFFICIENT_BALANCE(400, "ACCOUNT_001", "계좌 잔액이 부족합니다.");

        private final int status;       // HTTP 상태 코드 (400, 404, 409 등)
        private final String code;      // 클라이언트와 협의한 고유 에러 코드
        private final String message;   // 에러 설명 메시지

        ErrorCode(int status, String code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }

        public int getStatus() { return status; }
        public String getCode() { return code; }
        public String getMessage() { return message; }
    }

    // =========================================================================
    // 2. 비즈니스 커스텀 예외 클래스 (CustomException)
    // =========================================================================
    public static class CustomException extends RuntimeException {
        private final ErrorCode errorCode;

        public CustomException(ErrorCode errorCode) {
            super(errorCode.getMessage()); // 부모 RuntimeException에 기본 메시지 전달
            this.errorCode = errorCode;
        }

        // 필요 시 메시지를 직접 커스텀할 수 있는 생성자
        public CustomException(ErrorCode errorCode, String customMessage) {
            super(customMessage);
            this.errorCode = errorCode;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }
    }

    // =========================================================================
    // 3. 메인 실행 및 미션
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== 미션 1. 회원 조회 실패 시 USER_NOT_FOUND 던지기 ===");
        try {
            // TODO 1: findUser("user99")를 호출하고, 유저가 없으면
            //         CustomException(ErrorCode.USER_NOT_FOUND)을 던지도록 완성해보세요.
            // String user = findUser("user99").orElseThrow(...);
            
        } catch (CustomException e) {
            System.out.println("-> [가로챔] 에러코드: " + e.getErrorCode().getCode()
                    + " | HTTP 상태: " + e.getErrorCode().getStatus()
                    + " | 메시지: " + e.getMessage());
        }


        System.out.println("\n=== 미션 2. 이메일 중복 체크 시 DUPLICATE_EMAIL 던지기 ===");
        try {
            // TODO 2: checkDuplicateEmail("test@kakao.com")을 호출했을 때
            //         이미 이메일이 존재하면(true) CustomException(ErrorCode.DUPLICATE_EMAIL)을 던져보세요.
            // (힌트: if문 또는 filter/ifPresent 활용 가능)

        } catch (CustomException e) {
            System.out.println("-> [가로챔] 에러코드: " + e.getErrorCode().getCode()
                    + " | HTTP 상태: " + e.getErrorCode().getStatus()
                    + " | 메시지: " + e.getMessage());
        }


        System.out.println("\n=== 미션 3. 출금 처리 시 잔액 부족 검증 (filter + orElseThrow) ===");
        try {
            // TODO 3: findAccountBalance()로 잔액(5,000원)을 조회하고, 
            //         출금 요청 금액(10,000원)보다 잔액이 크거나 같을 때만 통과하도록 filter()를 걸고,
            //         조건 불만족 시 CustomException(ErrorCode.INSUFFICIENT_BALANCE)을 던지도록 완성해보세요.
            int withdrawAmount = 10000;
            // int currentBalance = findAccountBalance().filter(...).orElseThrow(...);

        } catch (CustomException e) {
            System.out.println("-> [가로챔] 에러코드: " + e.getErrorCode().getCode()
                    + " | HTTP 상태: " + e.getErrorCode().getStatus()
                    + " | 메시지: " + e.getMessage());
        }
    }

    // 모의 데이터 메서드들
    public static Optional<String> findUser(String userId) {
        return Optional.empty(); // 유저를 찾지 못함
    }

    public static boolean isEmailExist(String email) {
        return true; // 이미 존재하는 이메일이라고 가정
    }

    public static Optional<Integer> findAccountBalance() {
        return Optional.of(5000); // 현재 잔액 5,000원
    }
}
