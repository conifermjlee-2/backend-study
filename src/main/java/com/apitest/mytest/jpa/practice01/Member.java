package com.apitest.mytest.jpa.practice01;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [Practice 01] ORM이 다루는 대상: 순수 자바 객체(POJO) + 매핑 어노테이션
 *
 * 상세 매핑 옵션(@Column, @Table, 기본키 전략 등)은 2단계에서 다룹니다.
 * 여기서는 "자바 객체 하나가 테이블 하나에 대응된다"는 ORM의 기본 개념만 보여주기 위한
 * 최소한의 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
