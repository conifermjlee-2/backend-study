package com.apitest.mytest.globalexception;

import java.util.Optional;

public class Practice01 {
    public static void main(String[] args) {
        System.out.println("=== 미션 1. orElse 기본값 테스트 ===");
        // TODO 1: getNickname()을 호출하고, 값이 없으면 "손님(GUEST)"이 들어가도록 완성해보세요.
        String nickname = getNickname().orElse(" 손님");
        System.out.println("닉네임: " + nickname);

        System.out.println("\n=== 미션 2. orElseThrow 예외 발생 테스트 ===");
        try {
            // TODO 2: getBankAccount()를 호출하고, 값이 없으면 IllegalArgumentException을 던지도록 완성해보세요.
            String account = getBankAccount().orElseThrow( () -> new IllegalArgumentException("인자오류") );
            System.out.println("계좌번호: " + account);
        } catch (Exception e) {
            System.out.println("예외 가로챔 성공!: " + e.getMessage());
        }

    }



    //  일부러 없는거
    public static Optional<String> getNickname(){
        return Optional.empty();
    }

    private static Optional<String> getBankAccount() {
        return Optional.empty(); // 계좌 정보가 없음
    }


}
