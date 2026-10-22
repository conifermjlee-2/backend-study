package com.apitest.mytest.modern_java.ch03;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class T_26_10_20_2 {
    @FunctionalInterface
    public interface Function<T,R>{
        R apply(T t);
    }

    @FunctionalInterface
    public interface Predicate<T>{
        boolean test(T t);
    }


    public static void main(String[] args) throws IOException {
        List<Integer> mapResult = map(
              Arrays.asList("모던","" ,"자바","", "인"),
//                (String s) -> s.length()
                String::length
        );
        System.out.println("mapResult: "+ mapResult);

//        Runnable r = () -> System.out.println("test Runnable");
//        r.run();

        List<String> strings = Arrays.asList("모던","","자바","인","","액션");
        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();

        List<String> filterResult = filter(strings, nonEmptyStringPredicate);
        System.out.println("filterResult: "+ filterResult);
    }

    public static <T, R> List<R> map(List<T> list, Function<T,R> f){
        List<R> result = new ArrayList<>();
        for (T s : list) {
            result.add(f.apply(s));
        }
        return result;
    };

    public static <T> List<T> filter(List<T> list, Predicate<T> p){
        List<T> result = new ArrayList<>();
        for (T s :list) {
            if (p.test(s)) {
                result.add(s);
            }
        }

        return result;
    }

}
