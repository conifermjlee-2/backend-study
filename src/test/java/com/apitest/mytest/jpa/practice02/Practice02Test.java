package com.apitest.mytest.jpa.practice02;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [Practice 02] 엔티티 매핑 실습 & 검증 테스트
 *
 * 💡 Item.java의 TODO를 완성한 뒤 이 테스트를 실행(▶️)하여 초록불을 띄우세요!
 */
@SpringBootTest
@Transactional
class Practice02Test {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("미션 1. Item 엔티티 저장 및 기본키/컬럼 매핑 검증")
    void mission1_saveAndFind() {
        // [TODO 1] Item 객체를 생성하고 persist로 저장해보세요.
        Item item = new Item("맥북 프로 16", 3500000, 10, ItemStatus.ON_SALE);
        em.persist(item);

        em.flush();
        em.clear(); // 1차 캐시를 비워 DB에서 순수 SELECT 쿼리가 나가도록 강제

        // [TODO 2] em.find()로 저장된 Item을 조회하세요.
        Item found = em.find(Item.class, item.getId());

        // 검증
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("맥북 프로 16");
        assertThat(found.getPrice()).isEqualTo(3500000);
        assertThat(found.getStockQuantity()).isEqualTo(10);
        assertThat(found.getStatus()).isEqualTo(ItemStatus.ON_SALE);
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("미션 2. @Enumerated(EnumType.STRING)이 DB에 문자로 저장되는지 Native SQL로 검증")
    void mission2_enumTypeString_verification() {
        Item item = new Item("아이폰 16", 1500000, 20, ItemStatus.ON_SALE);
        em.persist(item);
        em.flush();

        // Native SQL로 DB 테이블(items)의 status 컬럼 원시값을 직접 확인
        // 만약 ORDINAL이면 0(숫자)이 나오고, STRING이면 'ON_SALE'(문자열)이 나옵니다.
        Object rawStatus = em.createNativeQuery("SELECT status FROM items WHERE id = :id")
                .setParameter("id", item.getId())
                .getSingleResult();

        System.out.println(">>> DB에 실제 저장된 status 컬럼 값: " + rawStatus);
        assertThat(rawStatus).isEqualTo("ON_SALE");
    }

    @Test
    @DisplayName("미션 3. @Transient 필드는 DB에 저장되지 않는지 검증")
    void mission3_transientField() {
        Item item = new Item("에어팟 프로", 350000, 50, ItemStatus.ON_SALE);
        item.setTempDiscountRate(0.15); // 임시 할인율 15% 세팅

        em.persist(item);
        em.flush();
        em.clear();

        // DB에서 다시 조회했을 때, @Transient 필드는 DB 컬럼에 없으므로 기본값(0.0 또는 null) 상태여야 함
        Item found = em.find(Item.class, item.getId());
        assertThat(found.getTempDiscountRate()).isNull(); // DB에서 새로 읽어왔으므로 0.15가 아닌 null
    }

    @Test
    @DisplayName("미션 4. updatable = false 설정된 createdAt이 수정되지 않는지 검증")
    void mission4_updatableFalse() {
        Item item = new Item("아이패드 에어", 900000, 30, ItemStatus.ON_SALE);
        em.persist(item);
        em.flush();

        LocalDateTime originalCreatedAt = item.getCreatedAt();

        // 가격 수정
        item.changePrice(850000);
        em.flush();
        em.clear();

        Item found = em.find(Item.class, item.getId());
        assertThat(found.getPrice()).isEqualTo(850000);
        // createdAt은 updatable = false 이므로 최초 생성 시간이 유지되어야 함
        assertThat(found.getCreatedAt()).isEqualTo(originalCreatedAt);
    }
}
