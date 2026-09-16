package com.apitest.mytest.modern_java.ch03;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Arrays;
import java.util.List;

public class ConsumerTest {
    // 반환 타입 void 인터페이스
    @FunctionalInterface
    public interface Consumer<T>{
        void accept(T t);
    }

    public static void main(String[] args) {
        System.out.println("ConsumerTest");

        forEach(
                Arrays.asList(1,2,3),
                (Integer i) -> System.out.println(i)
        );

        forEach(
                Arrays.asList("A","b","c"),
                (String i) -> System.out.println(i)
        );
    }

    public static <T> List<T> forEach(List<T> lists, Consumer<T> c){
        List<T> result = new ArrayList<>();
        T testInteger = null;
        for (T list : lists) {
            c.accept(list);
        }

        return result;
    }
}
