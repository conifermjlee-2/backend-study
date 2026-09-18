package com.apitest.mytest.modern_java.ch03;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FuntionTest2 {
    @FunctionalInterface
    public interface Function<T, R>{
        R test(T t);
    }

    public static void main(String[] args) {
        System.out.println("FuntionTest2 start" );
        testMethod1(
                Arrays.asList("모던","자바","인"),
//                (ele) -> "test"
                (ele) -> 2
        );
    }

    public static <T, R> void testMethod1(List<T> list, Function<T, R> f){
        System.out.println("testMethod start");

    }
}
