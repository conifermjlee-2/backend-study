package com.apitest.mytest.modern_java.ch03;

import com.apitest.mytest.modern_java.ch02.domain.Color;
import com.apitest.mytest.modern_java.ch03.domain.Apple;

import java.util.ArrayList;
import java.util.List;

public class TestLamda2 {

    @FunctionalInterface
    public interface FilterCheck<T>{
        boolean test(T t);
    }

    public static void main(String[] args) {
        System.out.println("start");
        List<Apple> appleList = new ArrayList<>();
        appleList.add(new Apple(Color.RED, 1));
        appleList.add(new Apple(Color.GREEN, 2));
        appleList.add(new Apple(Color.RED, 3));

        List<Apple> filterMethodList =  filterMethod(appleList, new FilterCheck<Apple>() {
            @Override
            public boolean test(Apple apple) {
                return apple.getColor().equals(Color.RED);
            }
        });
        System.out.println("filterMethodList: " + filterMethodList);
    }
    public static <T> List<T> filterMethod(List<T> list, FilterCheck t){
        List<T> resultList = new ArrayList<>();
        for (T fruit : list) {
            if (t.test(fruit)) {
                resultList.add(fruit);
            }

        }
        return resultList;
    }
}
