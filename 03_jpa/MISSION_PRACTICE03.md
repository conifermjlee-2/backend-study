# 📄 [Practice 03] 영속성 컨텍스트 (Persistence Context) — 문제지

> **상위 목차:** [`03_jpa/README.md`](./README.md) · 이 파일은 **문제지**입니다. 정답 코드는 없습니다.
> 다 풀거나 막히면 알려주세요 — `README_PRACTICE03.md`로 정답지 + 해설을 정리해 드립니다.

---

## 🎯 이 미션의 목표

`03_jpa` 9단계 중 **⭐ 제일 중요**하다고 표시된 3단계입니다. Step 1(ORM 개념), Step 2(엔티티 매핑)까지는
"어떻게 매핑하는가"를 배웠다면, 이번 단계는 **매핑된 엔티티가 실제로 살아있는 동안 무슨 일이 벌어지는가**를 눈으로 확인하는 단계예요.

이 미션을 끝내면 다음 4가지 질문에 코드로 답할 수 있어야 합니다.

1. `em.find()`를 같은 트랜잭션에서 두 번 부르면 DB에 두 번 갔다올까?
2. `item.changePrice(1000)`만 호출하고 `save()`를 안 불렀는데 왜 DB에 반영될까?
3. 영속성 컨텍스트를 벗어난(준영속) 엔티티를 고치면 왜 반영이 안 될까?
4. `em.persist()`를 호출한 순간 진짜로 `INSERT` SQL이 나갈까?

---

## 🧠 미리 알아야 할 개념 (짧게)

| 용어 | 한 줄 설명 |
| :--- | :--- |
| **영속성 컨텍스트** | `EntityManager`가 관리하는 엔티티들의 논리적 저장소. 트랜잭션과 생명주기를 같이 함. |
| **1차 캐시** | 영속성 컨텍스트 내부의 `Map<PK, Entity>`. 같은 PK를 다시 찾으면 SQL 없이 여기서 반환. |
| **엔티티 생명주기** | 비영속(new) → **영속**(persist) → **준영속**(detach) → 삭제(remove) |
| **더티 체킹** | flush 시점에 스냅샷과 현재 필드를 비교해서 바뀐 게 있으면 자동으로 UPDATE 쿼리 생성 |
| **쓰기 지연 SQL 저장소** | `persist()`해도 바로 INSERT하지 않고 모아뒀다가 `flush()` 시점에 한꺼번에 내보냄 |
| **flush vs commit** | flush = SQL을 DB로 전송(트랜잭션은 안 끝남). commit = flush + 트랜잭션 종료 |

> 힌트: `application.yml`(또는 `application-test.yml`)에 `spring.jpa.show-sql: true`,
> `spring.jpa.properties.hibernate.format_sql: true`를 켜두면 콘솔에서 SQL이 언제 나가는지 직접 눈으로 볼 수 있어요.
> 이번 미션은 **콘솔 로그를 직접 읽는 것 자체가 학습**입니다.

---

## 🗂️ 실습 위치

- 새로 만들 파일: `src/test/java/com/apitest/mytest/jpa/practice03/Practice03Test.java`
- 사용할 엔티티: `com.apitest.mytest.jpa.practice02.Item` / `ItemStatus` (이미 있는 것 재사용, 새 엔티티 안 만들어도 됨)
- 테스트 방식: `@DataJpaTest` + `EntityManager` 직접 주입 (Spring Data Repository 안 만들어도 됨)

시작 스켈레톤은 이렇게 잡으면 됩니다. **본문은 전부 TODO — 채워 넣는 게 미션입니다.**

```java
package com.apitest.mytest.jpa.practice03;

import com.apitest.mytest.jpa.practice02.Item;
import com.apitest.mytest.jpa.practice02.ItemStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class Practice03Test {

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("미션 1: 1차 캐시 - 같은 트랜잭션에서 두 번 find하면 같은 참조가 반환된다")
    void mission1_firstLevelCache() {
        // TODO
    }

    @Test
    @DisplayName("미션 2: 더티 체킹 - save() 없이 필드만 바꿔도 flush 시점에 반영된다")
    void mission2_dirtyChecking() {
        // TODO
    }

    @Test
    @DisplayName("미션 3: 준영속 - detach 후 바꾼 값은 flush해도 반영되지 않는다")
    void mission3_detachedEntity() {
        // TODO
    }

    @Test
    @DisplayName("미션 4: 쓰기 지연 - persist 직후엔 INSERT가 안 나가고 flush 시점에 나간다")
    void mission4_writeBehind() {
        // TODO
    }
}
```

---

## 🧩 미션 1. 1차 캐시 (First-level Cache)

**질문:** 같은 `id`로 `em.find()`를 두 번 호출하면 SELECT 쿼리가 몇 번 나갈까요?

- [ ] `Item.newInstance(...)` 대신 기존 생성자로 `Item`을 하나 만들어 `em.persist()`한다.
- [ ] `em.persist()` 직후 **아직 flush 전인데도** `em.find(Item.class, id)`가 성공하는 이유를 주석으로 적어본다.
- [ ] `em.find(Item.class, id)`를 **두 번** 호출해서 두 변수(`a`, `b`)에 담는다.
- [ ] `assertThat(a).isSameAs(b)`로 **같은 참조**인지 검증한다. (`equals`가 아니라 `==` 수준 동일성!)
- [ ] 콘솔에서 SELECT 쿼리가 몇 번 찍혔는지 확인하고, 왜 그런지 주석으로 남긴다.

---

## 🧩 미션 2. 더티 체킹 (Dirty Checking)

**질문:** `item.changePrice(999)`만 호출하고 `repository.save()`나 `em.merge()`를 부르지 않았는데 DB는 어떻게 바뀔까요?

- [ ] 엔티티를 persist하고 `em.flush()` + `em.clear()`로 일단 DB에 확실히 반영 + 영속성 컨텍스트를 비운다.
- [ ] `em.find()`로 다시 조회해서 **영속 상태의 `item`**을 얻는다.
- [ ] `item.changePrice(999)`만 호출한다. (save 계열 메서드 호출 금지!)
- [ ] `em.flush()`를 호출한 뒤 콘솔에서 `UPDATE` 쿼리가 자동으로 나가는지 확인한다.
- [ ] `em.clear()` 후 `em.find()`로 다시 조회해서 가격이 999로 바뀌어 있는지 `assertThat`으로 검증한다.
- [ ] 왜 `save()`를 호출하지 않아도 UPDATE가 나가는지, Hibernate가 무엇과 무엇을 비교하는지 한 문장으로 주석을 남긴다.

---

## 🧩 미션 3. 준영속 (Detached) 상태

**질문:** 영속성 컨텍스트에서 떨어져 나간(준영속) 엔티티를 고치면 왜 DB에 반영이 안 될까요?

- [ ] 엔티티를 persist + flush + clear로 DB에 반영한다.
- [ ] `em.find()`로 다시 조회한다.
- [ ] `em.detach(item)`을 호출해서 **준영속 상태**로 만든다.
- [ ] 그 다음 `item.changePrice(1)`처럼 필드를 바꿔본다.
- [ ] `em.flush()`를 호출해도 UPDATE 쿼리가 **나가지 않는다**는 것을 확인한다.
- [ ] `em.clear()` 후 다시 `em.find()`로 조회해서 가격이 **여전히 원래 값**인지 `assertThat`으로 검증한다.
- [ ] 미션 2와 무엇이 다른지(영속 vs 준영속) 한 문장으로 비교 주석을 남긴다.

---

## 🧩 미션 4. 쓰기 지연 SQL 저장소 (Write-behind)

**질문:** `em.persist()`를 부르는 순간 바로 `INSERT` SQL이 나갈까요, 나중에 나갈까요?

- [ ] `Item`을 2~3개 만들어 각각 `em.persist()`만 호출한다. (flush는 아직 호출하지 않음!)
- [ ] persist 직후 콘솔에 `INSERT` 로그가 찍혔는지 확인하고 주석으로 관찰 내용을 적는다.
- [ ] 이후 `em.createQuery("select count(i) from Item i", Long.class).getSingleResult()`처럼 **JPQL 쿼리를 실행**해본다.
- [ ] 이 시점에 콘솔에 `INSERT`들이 먼저 찍히고 그 다음 `SELECT COUNT`가 찍히는 걸 확인한다. (이걸 **flush-then-query**, 자동 flush라고 부릅니다.)
- [ ] 왜 JPA가 쿼리 실행 전에 알아서 flush를 하는지(정합성 문제) 한 문장으로 주석을 남긴다.

---

## ✅ 최종 체크리스트

- [ ] `./gradlew.bat test --tests "com.apitest.mytest.jpa.practice03.Practice03Test"` 가 전부 초록불
- [ ] 4개 미션 각각에 "왜 이렇게 동작하는가" 주석이 최소 1줄씩 있다
- [ ] `equals()`가 아니라 `isSameAs()`(`==`)로 1차 캐시를 검증했다
- [ ] `save()`/`merge()`를 한 번도 명시적으로 호출하지 않고도 미션 2가 통과한다
- [ ] 미션 2와 미션 3의 차이를 말로 설명할 수 있다 (영속 vs 준영속)

다 되면 알려주세요. 코드 리뷰하고 `README_PRACTICE03.md` 정답지 + "왜(Why)" 정리본을 만들어 드릴게요.
