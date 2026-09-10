package com.apitest.mytest.modern_java.ch02;

import com.apitest.mytest.modern_java.ch02.domain.Apple;
import com.apitest.mytest.modern_java.ch02.domain.Color;
import com.apitest.mytest.modern_java.ch02.predicate.ApplePredicate;
import com.apitest.mytest.modern_java.ch02.predicate.ApplesGreenPredicate;
import com.apitest.mytest.modern_java.ch02.predicate.ApplesHeavWeightPredicate;

import java.util.ArrayList;
import java.util.List;

public class FilteringApples {
    public static void main(String[] args) {

        System.out.println("FilteringApples start");
        Apple apple_1 = new Apple(Color.RED, 1);
        Apple apple_2 = new Apple(Color.GREEN, 2);
        Apple apple_3 = new Apple(Color.GREEN, 3);
        Apple apple_4 = new Apple(Color.GREEN, 4);

        List<Apple> inventory = new ArrayList<>();
        inventory.add(apple_1);
        inventory.add(apple_2);
        inventory.add(apple_3);
        inventory.add(apple_4);

        List<Apple> greenApples = filterApplesByColor(inventory, Color.GREEN);
        System.out.println("녹색 사과 필터링 결과: " + greenApples);

        List<Apple> weightCheck = filterApplesByWeight(inventory, 2);
        System.out.println("2키로 초과 : " + weightCheck);

        List<Apple> applesGreenPredicate = filterApples(inventory, new ApplesGreenPredicate());
        System.out.println("applesGreenPredicate 필터링 결과: " + applesGreenPredicate);

        List<Apple> applesHeavWeightPredicate = filterApples(inventory, new ApplesHeavWeightPredicate());
        System.out.println("applesHeavWeightPredicate 필터링 결과: " + applesHeavWeightPredicate);
    }

    //색상 필터링
    public static List<Apple> filterApplesByColor (List<Apple> inventory, Color color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple :  inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }
        return result;
    }

    //무게 필터링
    public static List<Apple> filterApplesByWeight (List<Apple> inventory, int weight) {
        List<Apple> result =  new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApples (List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    };
}
