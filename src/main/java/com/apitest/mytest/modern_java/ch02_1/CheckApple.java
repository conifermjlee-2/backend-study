package com.apitest.mytest.modern_java.ch02_1;

import com.apitest.mytest.modern_java.ch02_1.domain.Apple;
import com.apitest.mytest.modern_java.ch02_1.domain.Color;

import java.util.ArrayList;
import java.util.List;

public class CheckApple {
    @FunctionalInterface
    public interface PredicateApple<T>{
        boolean check(T t);
    }

    public static List<Apple> filterLambdaRed(List<Apple> apples, PredicateApple<Apple> p){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: apples) {
            if (p.check(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("CheckApple start ");

        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple(Color.RED, 10.0));
        apples.add(new Apple(Color.RED, 20.0));
        apples.add(new Apple(Color.GREEN, 30.0));

//        List<Apple> redApples = filterRed(apples);
//        System.out.println("redApples: " + redApples.toString());
//
//        List<Apple> greenApples = filterGreen(apples);
//        System.out.println("greenApples: " + greenApples.toString());

        List<Apple> redApplesLambda = filterLambdaRed(apples, (apple) -> apple.getColor() == Color.RED);
        System.out.println("redApplesLambda: " + redApplesLambda.toString());
    }

//    public static List<Apple> filterRed(List<Apple> list){
//        List<Apple> result = new ArrayList<>();
//        for (Apple apple : list) {
//            if (apple.getColor() == Color.RED) {
//                result.add(apple);
//            }
//        }
//        return result;
//    }
//
//    public static List<Apple> filterGreen(List<Apple> list){
//        List<Apple> result = new ArrayList<>();
//        for (Apple apple : list) {
//            if (apple.getColor() == Color.GREEN) {
//                result.add(apple);
//            }
//        }
//        return result;
//    }



}
