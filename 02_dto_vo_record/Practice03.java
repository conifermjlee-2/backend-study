package com.apitest.mytest.dtovorecord;

/**
 * [Practice 03] atworks 실무 스타일 계층형 DTO 흐름 (Request ➔ Command ➔ Result ➔ Response)
 * 
 * 💡 학습 목표:
 * 1. 왜 Controller의 Request DTO를 Service에 그대로 넘기지 않고 Command로 변환하는지 이해한다.
 * 2. 왜 Entity를 Controller/클라이언트에 직접 노출하지 않고 Result ➔ Response로 감싸 반환하는지 이해한다.
 * 3. record를 활용한 깔끔한 DTO 변환 체인(`toCommand()`, `from()`)을 구현한다.
 */
public class Practice03 {

    // =========================================================================
    // 1. Controller 계층 DTO: Request & Response
    // =========================================================================
    // HTTP 요청 바디 매핑 객체
    public record CreateUserRequest(
            String loginId,
            String password,
            String name
    ) {
        public CreateUserRequest {
            if (loginId == null || loginId.isBlank()) throw new IllegalArgumentException("loginId는 필수입니다.");
            if (password == null || password.isBlank()) throw new IllegalArgumentException("password는 필수입니다.");
            if (name == null || name.isBlank()) throw new IllegalArgumentException("name은 필수입니다.");
        }

        // TODO 1-1: Request를 Service 전용 파라미터인 Command로 변환하는 메서드 작성
        public CreateUserCommand toCommand() {
            return new CreateUserCommand(loginId, password, name);
        }
    }

    // 클라이언트에게 반환할 최종 JSON 응답 객체 (비밀번호 같은 민감정보 제외!)
    public record UserResponse(
            Long id,
            String loginId,
            String name
    ) {
        // TODO 1-2: Service 결과인 UserResult로부터 Response를 생성하는 정적 팩토리 메서드 작성
        public static UserResponse from(UserResult result) {
            return new UserResponse(result.id(), result.loginId(), result.name());
        }
    }

    // =========================================================================
    // 2. Service 계층 DTO: Command & Result
    // =========================================================================
    // Service 비즈니스 메서드의 입력 규격 (웹 계층과 분리된 순수 비즈니스 파라미터)
    public record CreateUserCommand(
            String loginId,
            String password,
            String name
    ) {
    }

    // Service 비즈니스 메서드의 출력 규격 (Entity 대신 반환)
    public record UserResult(
            Long id,
            String loginId,
            String name
    ) {
        // TODO 2: Mock User Entity로부터 UserResult를 생성하는 정적 팩토리 메서드 작성
        public static UserResult from(MockUserEntity entity) {
            return new UserResult(entity.getId(), entity.getLoginId(), entity.getName());
        }
    }

    // =========================================================================
    // 3. 모의 도메인 엔티티 (Mock User Entity)
    // =========================================================================
    public static class MockUserEntity {
        private final Long id;
        private final String loginId;
        private final String passwordHash; // 민감정보: 절대 클라이언트에 노출되면 안 됨!
        private final String name;

        public MockUserEntity(Long id, String loginId, String passwordHash, String name) {
            this.id = id;
            this.loginId = loginId;
            this.passwordHash = passwordHash;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getLoginId() { return loginId; }
        public String getPasswordHash() { return passwordHash; }
        public String getName() { return name; }
    }

    // =========================================================================
    // 4. 모의 서비스 (Mock Service)
    // =========================================================================
    public static class MockUserService {
        public UserResult createUser(CreateUserCommand command) {
            System.out.println("-> [Service] 사용자 생성 비즈니스 로직 실행: " + command.name());
            
            // 가상의 엔티티 저장 (비밀번호 암호화 가정)
            String encryptedPassword = "HASHED_" + command.password();
            MockUserEntity savedEntity = new MockUserEntity(100L, command.loginId(), encryptedPassword, command.name());

            // 엔티티를 직접 반환하지 않고 Result DTO로 변환하여 반환
            return UserResult.from(savedEntity);
        }
    }

    // =========================================================================
    // 5. 메인 실행 (Controller ➔ Service ➔ Controller 전체 흐름 시뮬레이션)
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== 1. [Controller] 클라이언트 요청 수신 (CreateUserRequest) ===");
        CreateUserRequest request = new CreateUserRequest("conifer", "secret1234", "코니퍼");
        System.out.println("요청 데이터: " + request);

        System.out.println("\n=== 2. [Controller ➔ Service] Request를 Command로 변환하여 서비스 호출 ===");
        CreateUserCommand command = request.toCommand();
        System.out.println("변환된 Command: " + command);

        MockUserService userService = new MockUserService();
        UserResult result = userService.createUser(command);

        System.out.println("\n=== 3. [Service ➔ Controller] 서비스 실행 결과(UserResult) 수신 ===");
        System.out.println("수신된 Result: " + result);

        System.out.println("\n=== 4. [Controller] Result를 클라이언트용 UserResponse로 변환 ===");
        UserResponse response = UserResponse.from(result);
        System.out.println("최종 JSON 응답 DTO: " + response);
        System.out.println("응답 ID: " + response.id() + ", 닉네임: " + response.name());
        // 비밀번호(passwordHash)가 전혀 노출되지 않고 안전하게 반환됨!
    }
}
