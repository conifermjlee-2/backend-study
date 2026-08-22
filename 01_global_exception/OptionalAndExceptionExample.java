package com.apitest.mytest.globalexception;

import java.util.Optional;

/**
 * [실습 예제] Optional 처리 및 예외 발생/대체값 패턴 정리
 */
public class OptionalAndExceptionExample {

    public static void main(String[] args) {
        System.out.println("=== 1. orElseThrow() 테스트 (값이 없으면 즉시 중단 및 예외 발생) ===");
        try {
            findUserById("not_exist_user")
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. (400 Bad Request 대상)"));
        } catch (IllegalArgumentException e) {
            System.out.println("-> 예외 발생! 스프링에서는 이 예외가 @RestControllerAdvice로 전달됩니다.");
            System.out.println("-> 에러 메시지: " + e.getMessage());
        }

        System.out.println("\n=== 2. orElse(기본값) 테스트 (값이 없어도 에러가 아닌 정상 흐름) ===");
        // 예: 최초 등록 시 이전 리비전 번호가 없으면 1L로 시작
        Long revisionNumber = findLatestRevisionNumber("new_server")
                .map(num -> num + 1L)
                .orElse(1L); // 없으면 1L 기본값
        System.out.println("-> 계산된 리비전 번호: " + revisionNumber + " (기본값 1L 적용)");

        System.out.println("\n=== 3. ifPresent(동작) 테스트 (있을 때만 실행, 없으면 무시) ===");
        findScheduleSetting("my_schedule")
                .ifPresent(schedule -> System.out.println("-> 스케줄 설정이 존재하여 비활성화 처리를 진행합니다: " + schedule));

        findScheduleSetting("empty_schedule")
                .ifPresent(schedule -> System.out.println("-> 이 줄은 실행되지 않습니다."));
    }

    // 모의(Mock) 데이터 조회 메서드들
    private static Optional<String> findUserById(String id) {
        if ("admin".equals(id)) {
            return Optional.of("관리자");
        }
        return Optional.empty(); // 없으면 빈 Optional 반환
    }

    private static Optional<Long> findLatestRevisionNumber(String serverName) {
        return Optional.empty(); // 최초 생성 서버라 이전 버전 없음
    }

    private static Optional<String> findScheduleSetting(String name) {
        if ("my_schedule".equals(name)) {
            return Optional.of("매일 자정 동기화");
        }
        return Optional.empty();
    }
}
