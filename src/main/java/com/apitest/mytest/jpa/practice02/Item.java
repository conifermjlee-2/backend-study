package com.apitest.mytest.jpa.practice02;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * [Practice 02] 실무형 엔티티 매핑
 *
 * 🎯 요구사항:
 * 1. 이 클래스를 JPA 엔티티로 등록하고, 매핑될 테이블명을 "items"로 지정하세요.
 * 2. id: PK로 지정하고, DB의 AUTO_INCREMENT를 따르는 IDENTITY 전략을 적용하세요.
 * 3. name: DB 컬럼명을 "item_name", 길이를 100, NULL 불가(nullable = false)로 지정하세요.
 * 4. price: NULL 불가(nullable = false)로 지정하세요.
 * 5. stockQuantity: DB 컬럼명을 "stock_qty", NULL 불가로 지정하세요.
 * 6. status: ItemStatus Enum을 사용하되, 반드시 문자열(EnumType.STRING)로 저장되도록 매핑하세요.
 * 7. tempDiscountRate: 계산용 임시 필드입니다. DB 테이블의 컬럼으로 생성되지 않도록 매핑에서 제외하세요.
 * 8. createdAt: 최초 등록일시입니다. 등록 후 수정되지 않도록(updatable = false) 설정하세요.
 * 9. 기본 생성자는 외부에서 new Item()으로 무분별하게 생성하지 못하도록 protected로 제한하세요.
 */
// TODO 1: @Entity 및 @Table(name = "items") 어노테이션을 선언하세요.
@Entity
@Table(name = "items")
@Getter
// TODO 2: Lombok을 사용해 protected 접근 제어자를 가진 기본 생성자를 만드세요. (@NoArgsConstructor)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    // TODO 3: @Id 와 @GeneratedValue(strategy = GenerationType.IDENTITY)를 설정하세요.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 4: @Column(name = "item_name", nullable = false, length = 100)을 설정하세요.
    @Column(name = "item_name", nullable = false, length = 100)
    private String name;

    // TODO 5: @Column(nullable = false)을 설정하세요.
    @Column(nullable = false)
    private Integer price;

    // TODO 6: @Column(name = "stock_qty", nullable = false)을 설정하세요.
    @Column(name = "stock_qty", nullable = false)
    private Integer stockQuantity;

    // TODO 7: @Enumerated(EnumType.STRING) 및 @Column(name = "status", nullable = false, length = 20)을 설정하세요.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ItemStatus status;

    // TODO 8: @Transient 어노테이션을 붙여 DB 컬럼에서 제외시키세요.
    @Transient
    private Double tempDiscountRate;

    // TODO 9: @Column(name = "created_at", nullable = false, updatable = false)을 설정하세요.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 생성자 (비즈니스 생성자)
    public Item(String name, Integer price, Integer stockQuantity, ItemStatus status) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.tempDiscountRate = 0.0;
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    // == 비즈니스 로직 메서드 (Setter 대신 도메인 메서드 사용) == //
    public void changePrice(Integer newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
        this.price = newPrice;
    }

    public void changeStatus(ItemStatus newStatus) {
        this.status = newStatus;
    }

    public void setTempDiscountRate(Double rate) {
        this.tempDiscountRate = rate;
    }
}
