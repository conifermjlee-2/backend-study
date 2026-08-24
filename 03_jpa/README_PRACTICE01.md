# [Practice 01] ORM 개념 이해

> 실습 코드: [`Member.java`](../src/main/java/com/apitest/mytest/jpa/practice01/Member.java), [`Practice01Test.java`](../src/test/java/com/apitest/mytest/jpa/practice01/Practice01Test.java)
> 실행: `./gradlew test --tests "*Practice01Test"`

---

## 1. JPA가 왜 필요한가 — 순수 JDBC의 문제점

`rawJdbcWay()` 테스트에서 회원 한 명을 저장하고 조회하는 데 필요했던 것들:

```java
try (Connection conn = dataSource.getConnection();
     PreparedStatement insert = conn.prepareStatement(
             "insert into member (name, email) values (?, ?)")) {
    insert.setString(1, "raw-jdbc-member");
    insert.setString(2, "raw@jdbc.com");
    insert.executeUpdate();
}
```

이 짧은 예제에서도 벌써 문제가 보입니다.

1. **SQL이 문자열이라 컴파일러가 못 잡아준다.** `member` 테이블명이나 `name` 컬럼명에 오타를 내도 컴파일은 성공하고, 실행해봐야 에러가 난다.
2. **파라미터 바인딩을 손으로 해야 한다.** `setString(1, ...)`, `setString(2, ...)` — 순서가 하나만 바뀌어도 버그.
3. **ResultSet → 객체 매핑을 직접 짜야 한다.** `rs.getString("name")`처럼 컬럼명을 또 문자열로 반복 입력.
4. **자원 해제(try-with-resources)를 매번 신경 써야 한다.** 안 닫으면 커넥션 풀 고갈.
5. **테이블이 100개, 필드가 20개면?** 이 보일러플레이트가 그대로 곱해진다.

즉, JDBC는 "자바 객체"와 "테이블 row"를 개발자가 손으로 일일이 변환해줘야 하는 도구입니다.

---

## 2. ORM(Object-Relational Mapping)이란

`ormWay()` 테스트가 한 일:

```java
Member member = new Member("orm-member", "orm@jpa.com");
em.persist(member);          // SQL 없음 — 객체를 그냥 저장

Member found = em.find(Member.class, member.getId()); // SQL 없음 — 객체를 그냥 조회
```

SQL을 한 줄도 쓰지 않았는데 데이터가 저장되고 조회됩니다. 실행 로그(`show-sql: true`)를 보면 Hibernate가 뒤에서 실제로는 이런 SQL을 만들어서 실행한 걸 확인할 수 있습니다.

```sql
insert into member (email, name) values (?, ?)
select m1_0.id, m1_0.email, m1_0.name from member m1_0 where m1_0.id = ?
```

**ORM은 "객체(Object)"와 "관계형 DB의 테이블(Relational)"을 자동으로 매핑(Mapping)해주는 기술**입니다. 개발자는 `Member`라는 자바 객체를 다루고, ORM 프레임워크(Hibernate)가 그걸 SQL로 번역해서 DB에 반영합니다.

즉 관점의 전환이 핵심입니다.
- JDBC: **"member 테이블의 email 컬럼"**을 다룬다 (테이블 중심)
- JPA/ORM: **"Member 객체의 email 필드"**를 다룬다 (객체 중심)

---

## 3. JPA는 표준(인터페이스), Hibernate는 구현체

여기서 헷갈리기 쉬운 부분: 방금 쓴 `EntityManager.persist()`, `em.find()`는 **JPA 표준 인터페이스**입니다. 실제로 그걸 구현해서 SQL을 만들고 실행하는 건 **Hibernate**라는 별도의 라이브러리입니다.

```
JPA (jakarta.persistence.*)          ← 인터페이스/명세 (자바 진영의 표준 규격)
   ▲
   │ implements
   │
Hibernate (org.hibernate.*)          ← 실제 구현체 (SQL 생성, 캐싱, 더티체킹 등 실제 동작)
```

비유하자면 **JDBC와 JDBC 드라이버**의 관계와 똑같습니다.
- `java.sql.Connection`은 인터페이스, 실제로 커넥션을 만드는 건 `org.h2.Driver` 같은 드라이버 구현체
- `jakarta.persistence.EntityManager`는 인터페이스, 실제로 동작하는 건 Hibernate의 `SessionImpl`

이 프로젝트의 `build.gradle`에 있는 `spring-boot-starter-data-jpa`는 내부적으로 Hibernate를 기본 구현체로 가져옵니다. 그래서 코드에서는 `jakarta.persistence.EntityManager`(표준)를 주입받아 쓰지만, 실제로 SQL을 만들어 실행하는 엔진은 Hibernate입니다.

**왜 표준/구현체로 분리했을까?** JPA(표준)에만 의존해서 코드를 짜면, 나중에 Hibernate 대신 EclipseLink 같은 다른 구현체로 바꿔도 애플리케이션 코드는 그대로 유지됩니다. (실무에서 실제로 구현체를 바꾸는 일은 드물지만, "표준에 의존하고 구현체는 갈아끼울 수 있게" 설계하는 게 자바 진영의 일반적인 패턴입니다 — JDBC, SLF4J도 같은 구조.)

---

---

## 4. 🎯 미션 정답지 (Practice01Test.java 해설)

### 📝 미션 1. 순수 JDBC 방식 (`rawJdbcWay`)
```java
// [TODO 1-1] INSERT: SQL 작성 및 파라미터 바인딩
try (Connection conn = dataSource.getConnection();
     PreparedStatement insert = conn.prepareStatement(
             "insert into member (name, email) values (?, ?)")) {
    insert.setString(1, "raw-jdbc-member");
    insert.setString(2, "raw@jdbc.com");
    insert.executeUpdate();
}

// [TODO 1-2] SELECT: 쿼리 실행 및 ResultSet 수동 매핑
String foundName = null;
try (Connection conn = dataSource.getConnection();
     PreparedStatement select = conn.prepareStatement(
             "select name from member where email = ?")) {
    select.setString(1, "raw@jdbc.com");
    try (ResultSet rs = select.executeQuery()) {
        if (rs.next()) {
            foundName = rs.getString("name");
        }
    }
}
```

### 📝 미션 2. JPA(ORM) 방식 (`ormWay`)
```java
// [TODO 2-1] 객체 생성 및 EntityManager로 영속화 (저장)
Member member = new Member("orm-member", "orm@jpa.com");
em.persist(member);

em.flush(); // 쓰기 지연 저장소의 쿼리를 DB로 즉시 전송

// [TODO 2-2] PK(id)로 엔티티 조회
Member found = em.find(Member.class, member.getId());
```

---

## 5. 핵심 요약 비교표

| | JDBC | JPA (Hibernate) |
| :--- | :--- | :--- |
| 다루는 대상 | SQL 문자열, ResultSet | 자바 객체 |
| 매핑 | 개발자가 직접 수동 매핑 | 어노테이션 기반 자동 |
| SQL 작성 | 직접 작성 | 자동 생성 (필요시 JPQL로 직접 제어 가능) |
| 표준화 | O (`java.sql.*`) | O (`jakarta.persistence.*`, 구현체는 Hibernate 등) |

다음 단계(2단계)에서는 `Member` 엔티티에 실제로 어떤 매핑 어노테이션(`@Column`, `@Table`, 기본키 전략)이 붙을 수 있는지, 그리고 어노테이션만 붙였을 뿐인데 Hibernate가 필드 값을 어떻게 읽고 쓰는지(리플렉션)를 다룹니다.
