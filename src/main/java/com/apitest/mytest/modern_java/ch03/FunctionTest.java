package com.apitest.mytest.modern_java.ch03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionTest {
    @FunctionalInterface
    public interface Function<T, R>{
        R apply(T t);
    }

    public static void main(String[] args) {
        System.out.println("FunctionTest");

        testMethod(
                Arrays.asList("모던","자바","인"),
                (ele) -> ele
        );
    }

    // 리스트 문자 열 세서 넣기
    public static <T, R> List<R> testMethod(List<T> list, Function<T, R> f){
        List<R> resultList = new ArrayList<>();
        for (T t : list) {
            R result =  f.apply(t);
            resultList.add(result);
        }
        return resultList;
    }
}
