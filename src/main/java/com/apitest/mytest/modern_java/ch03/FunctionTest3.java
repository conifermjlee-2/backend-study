package com.apitest.mytest.modern_java.ch03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionTest3 {
    @FunctionalInterface
    public interface Function<T, R>{
        R test(T t);
    }
    public static void main(String[] args) {
        System.out.println("FunctionTest3  start");
        List<Integer> testMethodList = testMethod(
                Arrays.asList("모던","" ,"자바","", "인"),
                (String  ele) -> ele.length()
//                String::length // 메서드 참고 (ele) -> ele.length() = (Sring ele) -> ele.length() => 공통 전달 부분 제거
        );
        System.out.println("testMethodList : " + testMethodList);
    }

    public static <T, R> List<R> testMethod(List<T> list, Function<T, R> f){
        List<R> listResult = new ArrayList<>();
        for (T ele : list) {
            System.out.println(ele);
            R resultCount = f.test(ele);
            listResult.add(resultCount);
        }
        System.out.println("testMethod start");
        return listResult;
    };
}
