package com.apitest.mytest.jpa.practice01;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [Practice 01] ORM 개념 이해
 *
 * 💡 학습 목표:
 * 1. 순수 JDBC로 회원 하나를 저장/조회할 때 필요한 코드량을 직접 체감한다.
 * 2. 같은 작업을 JPA(EntityManager)로 하면 얼마나 줄어드는지 비교한다.
 * 3. 테스트 실행 로그(Hibernate SQL)를 보고, 결국 JPA도 내부적으로는
 *    똑같은 SQL을 만들어서 실행한다는 사실을 확인한다. (마법이 아니다!)
 *
 * 실행: ./gradlew test --tests "*Practice01Test"
 */
@SpringBootTest
class Practice01Test {

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManager em;

    /**
     * 📝 미션 1. 순수 JDBC 방식 직접 완성해보기
     * - 목표: SQL 작성, 파라미터 바인딩, ResultSet 수동 매핑의 번거로움을 직접 체감합니다.
     */
    @Test
    void rawJdbcWay() throws Exception {
        // [TODO 1-1] INSERT: dataSource에서 커넥션을 얻어 member 테이블에 name="raw-jdbc-member", email="raw@jdbc.com"을 insert 하세요.
        try (Connection conn = dataSource.getConnection();
             PreparedStatement insert = conn.prepareStatement(
                     "INSERT INTO member (name, email) VALUES (?, ?)"
             )) {
            insert.setString(1, "raw-jdbc-member");
            insert.setString(2, "raw@jdbc.com");
            insert.executeUpdate();
        }

        // [TODO 1-2] SELECT: email이 "raw@jdbc.com"인 회원의 name을 조회하여 foundName 변수에 담으세요.
        String foundName = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement select = conn.prepareStatement(
                     "SELECT name FROM member WHERE email = ?"
             )) {
            select.setString(1, "raw@jdbc.com");
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) {
                    foundName = rs.getString("name");
                }
            }
        }

        // 검증 (TODO를 완료하면 초록불이 뜹니다!)
        assertThat(foundName).isEqualTo("raw-jdbc-member");
    }

    /**
     * 📝 미션 2. JPA(ORM) 방식 직접 완성해보기
     * - 목표: SQL 없이 객체를 저장(persist)하고 PK로 조회(find)하는 JPA의 편리함을 체감합니다.
     */
    @Test
    @Transactional
    void ormWay() {
        // [TODO 2-1] Member 객체를 생성("orm-member", "orm@jpa.com")하고, EntityManager(em)를 사용해 저장하세요.
        Member member = new Member("orm-member", "orm@jpa.com");
        em.persist(member);
        
        em.flush(); // 쓰기 지연 저장소의 쿼리를 DB로 즉시 전송 (로그 확인용)

        // [TODO 2-2] EntityManager(em)의 find() 메서드를 사용해 방금 저장한 회원을 PK(id)로 조회하세요.
        Member found = em.find(Member.class, member.getId());

        // 검증 (TODO를 완료하면 초록불이 뜹니다!)
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("orm-member");
        assertThat(found.getEmail()).isEqualTo("orm@jpa.com");
    }
}
