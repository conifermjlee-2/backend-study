# Java & Spring Boot 기능별 학습 저장소 (`my-test`)

이 디렉토리는 백엔드 개발 시 자주 사용되는 핵심 기능과 패턴들을 주제별/기능별로 직접 정리하고 실습하는 공간입니다.

---

## 📚 학습 목차

| 번호 | 주제 (폴더명) | 핵심 내용 |
| :--- | :--- | :--- |
| **01** | [`01_global_exception`](./01_global_exception/README.md) | `@RestControllerAdvice`, `orElseThrow()`, Optional 처리 전략 |
| **02** | `02_validation_and_binding` (예정) | `@Valid`, `@NotNull`, Custom Validator, BindingResult 처리 |
| **03** | `03_spring_security_jwt` (예정) | JwtFilter, SecurityConfig, Authentication/Authorization 흐름 |
| **04** | `04_jpa_relationships` (예정) | `@ManyToOne`, `@OneToMany`, N+1 문제 해결, Cascade 옵션 |
| **05** | `05_transactional_boundary` (예정) | `@Transactional` 롤백 원리, Checked vs Unchecked Exception |
| **06** | `06_dto_and_mapping` (예정) | Request/Response DTO 분리, Entity 변환 패턴 |

---

## 💡 학습 팁
* 각 폴더마다 **`README.md` (개념 및 원리)**와 **`.java` (실습/예제 코드)**를 짝지어 정리해 두면 나중에 프로젝트를 개발할 때 레퍼런스로 바로 찾아보기 좋습니다.
