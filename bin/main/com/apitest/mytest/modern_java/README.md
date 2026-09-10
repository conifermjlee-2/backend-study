# 모던 자바 인 액션 - 챕터 2: 동작 파라미터화 (Behavior Parameterization)

## 📂 추천 패키지 구조

요구사항 변화에 대응하는 과정(녹색 사과 필터링 -> 색상 파라미터화 -> 무게 파라미터화 -> 인터페이스화 -> 익명 클래스 -> 람다)을 순서대로 살펴보기 위해 다음과 같은 패키지 구조를 추천합니다.

```text
com.apitest.mytest.modern_java
└── ch02  // 챕터별로 패키지를 나누면 나중에 복습하기 편합니다.
    ├── domain
    │   ├── Apple.java  // 사과 모델 (현재 작성하신 코드 수정 필요)
    │   └── Color.java  // 색상 Enum
    ├── predicate       // (이후 2.2절부터 등장할 인터페이스와 구현체들)
    │   ├── ApplePredicate.java
    │   ├── AppleGreenColorPredicate.java
    │   └── AppleHeavyWeightPredicate.java
    └── FilteringApples.java // 2.1.1의 필터링 메서드와 메인(main) 메서드를 둘 실행 클래스
```

---

## 💻 구현 가이드

직접 코드를 작성해 보시면서 아래의 가이드를 참고해 보세요!

### 1. `Color.java` (Enum)
사과의 색상을 표현할 Enum 클래스를 `domain` 패키지에 생성합니다.

```java
package com.apitest.mytest.modern_java.ch02.domain;

public enum Color {
    RED,
    GREEN
}
```

### 2. `Apple.java` (도메인)
게터(Getter)와 세터(Setter), 그리고 출력을 위한 `toString()`을 구현합니다. (기존의 `String color` 대신 `Color` Enum을 사용합니다)

```java
package com.apitest.mytest.modern_java.ch02.domain;

public class Apple {
    private Color color; // String 대신 Enum 사용
    private int weight;  // 책에서는 size 대신 weight를 주로 사용합니다.

    public Apple(Color color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "color=" + color +
                ", weight=" + weight +
                '}';
    }
}
```

### 3. `FilteringApples.java` (메인 실행 클래스)
`filterGreenApples` 메서드를 구현하고, `main` 메서드에서 직접 테스트해 봅니다.

```java
package com.apitest.mytest.modern_java.ch02;

import com.apitest.mytest.modern_java.ch02.domain.Apple;
import com.apitest.mytest.modern_java.ch02.domain.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilteringApples {

    public static void main(String[] args) {
        // 테스트용 사과 인벤토리 생성
        List<Apple> inventory = Arrays.asList(
                new Apple(Color.GREEN, 80),
                new Apple(Color.GREEN, 155),
                new Apple(Color.RED, 120)
        );

        // 2.1.1 첫 번째 시도: 녹색 사과 필터링 실행
        List<Apple> greenApples = filterGreenApples(inventory);
        
        System.out.println("녹색 사과 필터링 결과: " + greenApples);
    }

    // 2.1.1 첫 번째 시도
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor() == Color.GREEN) {
                result.add(apple);
            }
        }
        return result;
    }
}
```
