package com.apitest.mytest.modern_java.ch03;

import java.util.ArrayList;
import java.util.List;

public class TestLamda2 {
    @FunctionalInterface
    public interface filterCheck<T>{
        boolean test(T t);
    }

    public static void main(String[] args) {
        System.out.println("start");

    }

    public static <T> List<T> filterMethod(){
        List<T> resultList = new ArrayList<>();
        return resultList;
    }
}
