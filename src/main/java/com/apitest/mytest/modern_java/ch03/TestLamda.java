package com.apitest.mytest.modern_java.ch03;

import com.apitest.mytest.modern_java.ch02.domain.Color;
import com.apitest.mytest.modern_java.ch03.domain.Apple;

import java.util.ArrayList;
import java.util.List;

public class TestLamda {
    // 인터페이스 기준 -> 여러 개 동작 가능하게
    @FunctionalInterface
    public interface TestInter<T> {
        String test(T t);
    }

    public static void main(String[] args) {
        System.out.println("TestLamda");
        List<Apple> listApples = new ArrayList<>();
        listApples.add(new Apple(Color.RED,1));
        listApples.add(new Apple(Color.GREEN,2));
        listApples.add(new Apple(Color.GREEN,3));


        List<Apple> filterColorFruitResult = filterColorFruit(listApples);
    }

    public static <T> List<T> filterColorFruit(T t){
        List<T> result = new ArrayList<>();
        return result;
    }

}
