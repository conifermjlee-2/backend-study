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
            // new IllegalArgumentException("인자오류") 없으면 catch 문 들어감
            String account = getBankAccount().orElseThrow( () -> new IllegalArgumentException("인자오류") );
            System.out.println("계좌번호: " + account);
        } catch (Exception e) {
            System.out.println("예외 가로챔 성공!: " + e.getMessage());
        }

        System.out.println("\n=== 미션 3. map() - 안전하게 데이터 가공 및 필드 추출 ===");
        // TODO 3: findEmail()을 호출하여 이메일이 존재하면 "@" 앞의 아이디 부분만 추출하고,
        //         없으면 "unknown"이 들어가도록 완성해보세요. (힌트: .map() + .orElse())
        // String emailId = findEmail().map(...).orElse("unknown");

        String emailId = findEmail().map( email -> email.split("@")[0] ).orElse("unknown");
        System.out.println("emailId : " + emailId);


        System.out.println("\n=== 미션 4. filter() - 조건 검증 후 예외 던지기 ===");
        // TODO 4: findUserAge()를 호출하여 나이가 19세 이상이면 통과하고,
        //         나이가 없거나 19세 미만이면 "미성년자는 접근 불가합니다." 예외(IllegalStateException)를 던지도록 해보세요.


        try {
            Integer age = findUserAge()
                    .filter(chkAge -> chkAge >= 19)
                    .orElseThrow(() -> new IllegalStateException("나이가 없거나 19세 미만"));
            System.out.println(" age is : " + age);
        } catch (Exception e) {
            System.out.println("검증 실패 원인: " +  e.getMessage());
        }

        System.out.println("\n=== 미션 5. ifPresentOrElse() - 분기 처리 (Java 9+) ===");
        // TODO 5: findLoginId()를 호출하여 아이디가 있으면 "로그인 성공: [아이디]"를 출력하고,
        //         없으면 "로그인 실패: 세션이 만료되었습니다."를 출력하도록 완성해보세요.
        findLoginId().ifPresentOrElse(
                id -> System.out.println("로그인 성공 : " + id),
                () -> System.out.println("login fail")
        );

        // TODO:
        findLoginId().ifPresent( id -> System.out.println("일반아이디: " + id));

    }

    // 모의 데이터 메서드들
    public static Optional<String> getNickname() { return Optional.empty(); }
    public static Optional<String> getBankAccount() { return Optional.empty(); }
    public static Optional<String> findEmail() {
        return Optional.empty();
//        return Optional.of("developer@kakao.com");
    }
    public static Optional<Integer> findUserAge() { return Optional.of(16); } // 16세 (조건 미달)
    public static Optional<String> findLoginId() { 
        return Optional.empty();
//        return Optional.of("manajini");
    }

}
