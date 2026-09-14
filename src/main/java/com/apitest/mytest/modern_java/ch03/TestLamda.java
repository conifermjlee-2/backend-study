package com.apitest.mytest.modern_java.ch03;

import com.apitest.mytest.modern_java.ch02.domain.Color;
import com.apitest.mytest.modern_java.ch03.domain.Apple;
import com.apitest.mytest.modern_java.ch03.domain.Banana;

import java.util.ArrayList;
import java.util.List;

public class TestLamda {
    // 인터페이스 기준 -> 여러 개 동작 가능하게
    @FunctionalInterface
    public interface TestInter<T> {
        boolean test(T t);
    }

    public static void main(String[] args) {
        System.out.println("TestLamda");
        List<Apple> listApples = new ArrayList<>();
        listApples.add(new Apple(Color.RED, 1));
        listApples.add(new Apple(Color.GREEN, 2));
        listApples.add(new Apple(Color.GREEN, 3));

        List<Apple> filterAppleColor = filterColorFruit(listApples, new TestInter<Apple>() {
            @Override
            public boolean test(Apple apple) {
                return apple.getColor().equals(Color.RED);
            }
        });
        System.out.println("filterAppleColor: " + filterAppleColor);


        List<Banana> listBanana = new ArrayList<>();
        listBanana.add(new Banana(Color.YELLOW, 1));
        listBanana.add(new Banana(Color.YELLOW, 2));
        listBanana.add(new Banana(Color.GREEN, 3));
        List<Banana> filterBananaColor = filterColorFruit(listBanana, new TestInter<Banana>() {
            @Override
            public boolean test(Banana banana) {
                return banana.getColor().equals(Color.YELLOW);
            }
        });
        System.out.println("filterBananaColor: " + filterBananaColor);
    }

    public static <T> List<T> filterColorFruit(List<T> list, TestInter t) {
        List<T> result = new ArrayList<>();
        for (T fruit : list) {
            if (t.test(fruit)) {
                result.add(fruit);
            }
        }
        return result;
    }

}
