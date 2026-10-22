package com.apitest.mytest.modern_java.ch03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredicateTeest {
    @FunctionalInterface
    public interface Predicate<T>{
        boolean test(T t);
    }

    public static void main(String[] args) {
        System.out.println("PredicateTeest");
        List<String> strings = Arrays.asList("모던","","자바","인","","액션");
        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> filterRes = filter(strings, nonEmptyStringPredicate);
        System.out.println("filterRes : " + filterRes);
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p){
        List<T> result = new ArrayList<>();
        for (T ele : list) {
            if (p.test(ele)) {
                result.add(ele);
            }
        }
        return result;
    }

//    public static <T> List<T> filter(List<T> list, Predicate<T> p){
//        List<T> result = new ArrayList<>();
//        for (T ele : list) {
//            if (p.test(ele)) {
//                result.add(ele);
//            }
//
//        }
//        return result;
//    }
}
