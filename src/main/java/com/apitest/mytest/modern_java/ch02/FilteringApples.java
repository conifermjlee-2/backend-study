package com.apitest.mytest.modern_java.ch02;

import com.apitest.mytest.modern_java.ch02.domain.Apple;
import com.apitest.mytest.modern_java.ch02.domain.Color;

import java.util.ArrayList;
import java.util.List;

public class FilteringApples {
    public static void main(String[] args) {

        System.out.println("FilteringApples start");
        Apple apple_1 = new Apple(Color.RED, 1);
        Apple apple_2 = new Apple(Color.GREEN, 2);
        Apple apple_3 = new Apple(Color.GREEN, 3);

        List<Apple> inventory = new ArrayList<>();
        inventory.add(apple_1);
        inventory.add(apple_2);
        inventory.add(apple_3);

        List<Apple> greenApples =  filterGreenApples(inventory);
        System.out.println("녹색 사과 필터링 결과: " + greenApples);
    }

    public static List<Apple> filterGreenApples (List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple :  inventory) {
            if (apple.getColor().equals(Color.GREEN)) {
                result.add(apple);
            }
        }
        return result;
    }
}
