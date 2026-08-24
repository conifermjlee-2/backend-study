# 📄 [Practice 02] 실무 엔티티 매핑 완전 정복

> **대상 실습 파일:** [`Item.java`](../src/main/java/com/apitest/mytest/jpa/practice02/Item.java), [`Practice02Test.java`](../src/test/java/com/apitest/mytest/jpa/practice02/Practice02Test.java)  
> **상위 목차:** [`03_jpa/README.md`](./README.md)

---

## 🎯 학습 목표
1. 실무에서 가장 기본이 되는 엔티티 매핑 어노테이션(`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated`, `@Transient`)을 체득한다.
2. **"기본 생성자는 왜 필수이고, 왜 `protected`로 두어야 하는가?"**를 객체 지향과 프록시 관점에서 이해한다.
3. **`@Enumerated(EnumType.ORDINAL)`을 실무에서 쓰면 왜 대형 장애가 발생하는지** 이유를 파악한다.

---

## 1. 📊 실무 필수 매핑 어노테이션 요약

| 어노테이션 | 주요 속성 | 실무 활용 및 설명 |
| :--- | :--- | :--- |
| **`@Entity`** | `name` | JPA가 관리하는 엔티티 객체임을 선언. **기본 생성자 필수** |
| **`@Table`** | `name`, `uniqueConstraints` | 매핑할 DB 테이블명을 명시. (생략 시 클래스명을 카멜→스네이크로 변환) |
| **`@Id`** | - | 엔티티의 기본키(PK) 지정 |
| **`@GeneratedValue`** | `strategy` | PK 자동 생성 전략 (`IDENTITY`: MySQL/H2, `SEQUENCE`: Oracle, `AUTO`) |
| **`@Column`** | `name`, `nullable`, `length`, `updatable` | 컬럼명, NOT NULL 제약조건, 최대 길이, 수정 가능 여부 설정 |
| **`@Enumerated`** | `EnumType.STRING` (⭐필수) | 자바 Enum 매핑. **`ORDINAL`(숫자)은 절대 금지!** |
| **`@Transient`** | - | DB 컬럼으로 만들지 않고 메모리에서만 임시 계산용으로 쓸 필드 |

---

## 2. ⚠️ [면접 & 실무 단골] 실무 핵심 포인트

### ① 왜 `@Enumerated(EnumType.ORDINAL)`은 금지인가?
* `ORDINAL`은 Enum 선언 순서(0, 1, 2...)를 DB에 숫자로 저장합니다.
* 만약 중간에 새로운 상태가 추가되어 순서가 바뀌면(예: `STOP_SALE`이 첫 번째로 들어오면), **기존 DB에 저장되어 있던 모든 데이터의 의미가 완전히 뒤틀리는 대형 장애**가 발생합니다.
* 따라서 무조건 **`@Enumerated(EnumType.STRING)`**으로 문자열(`"ON_SALE"`) 그대로 저장해야 안전합니다.

### ② 왜 기본 생성자는 `protected`로 제한할까?
* JPA 구현체(Hibernate)는 런타임에 리플렉션과 프록시(CGLIB) 기술을 사용해 객체를 생성하므로 **인자 없는 기본 생성자**가 반드시 필요합니다.
* 하지만 `public`으로 열어두면 다른 개발자가 `new Item()`으로 필수 값도 없는 불완전한 객체를 마구 생성할 위험이 있습니다.
* 반면 `private`으로 닫아버리면 Hibernate가 상속 기반 프록시 객체를 만들지 못합니다.
* 결론: **JPA 프록시도 만족하고 무분별한 new 생성도 방어하는 `protected` (`@NoArgsConstructor(access = AccessLevel.PROTECTED)`)가 실무 표준(국룰)**입니다.

---

## 3. 🎯 미션 정답지 (`Item.java`)

```java
package com.apitest.mytest.jpa.practice02;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 실무 국룰: protected 기본 생성자
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "stock_qty", nullable = false)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING) // ⭐ 필수: 반드시 STRING 사용
    @Column(name = "status", nullable = false, length = 20)
    private ItemStatus status;

    @Transient // DB 컬럼 매핑 제외 (임시 계산용)
    private Double tempDiscountRate;

    @Column(name = "created_at", nullable = false, updatable = false) // 수정 불가 필드
    private LocalDateTime createdAt;

    // 비즈니스 생성자
    public Item(String name, Integer price, Integer stockQuantity, ItemStatus status) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.tempDiscountRate = 0.0;
        this.createdAt = LocalDateTime.now();
    }

    // == 비즈니스 로직 메서드 == //
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
```

---

## 4. 🧪 테스트 실행 및 검증
[Practice02Test.java](../src/test/java/com/apitest/mytest/jpa/practice02/Practice02Test.java) 파일을 열고 테스트를 실행하면 모든 미션이 통과(초록불)하는 것을 확인할 수 있습니다.
