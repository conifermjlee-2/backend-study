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


    public static void main(String[] args) throws IOException {
        map(
              Arrays.asList("모던","" ,"자바","", "인"),
//                (String s) -> s.length()
                String::length
        );

    }

    public static <T, R> void map(List<T> list, Function<T,R> f){
        List<R> result = new ArrayList<>();
        for (T s : list) {
            result.add(f.apply(s));
        }
    }

}
