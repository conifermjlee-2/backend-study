package com.apitest.mytest.modern_java.ch03;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Predicate;


public class T_26_10_20_2 {

    @FunctionalInterface
    public interface Function<T, R>{
        R apply(T t);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("T_26_10_20_2 start");

        List<Integer> integerList = map(
                Arrays.asList("모던", "", "자바", "인", "", "액션"),
                (String s) -> s.length()
        );
        System.out.println("integerList: " + integerList);

        // 기본형 특화 -> 참조형 안써도 된다 오토박싱 비용 아낀다
//        IntPredicate evenNumbers = (int number) -> number % 2 == 0;
//        boolean resultEven = evenNumbers.test(1000);
//        System.out.println("resultEven : " + resultEven);
//
//        Predicate<Integer> oddNumber = (Integer i) -> i % 2 != 0;
//        boolean resultOdd =  oddNumber.test(2000);
//        System.out.println("resultOdd: " + resultOdd);

    }

    public static <T, R> List<R> map(List<T> list, Function<T, R > f){
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(f.apply(t));
        }
        return result;
    }


}
