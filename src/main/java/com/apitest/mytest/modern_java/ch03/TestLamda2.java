package com.apitest.mytest.modern_java.ch03;

import com.apitest.mytest.modern_java.ch02.domain.Color;
import com.apitest.mytest.modern_java.ch03.domain.Apple;
import com.apitest.mytest.modern_java.ch03.domain.Banana;

import java.util.ArrayList;
import java.util.List;

public class TestLamda2 {
    public static void main(String[] args) {
        System.out.println("start TestLamda2");

//        List<Apple> appleList =  TestLamda2.<Apple>filterMethod();
        List<Apple> appleList =  filterMethod(); // 왼쪽 타입이 자동 추론된다
        System.out.println("appleList: " +appleList);
    }
    public static <T> List<T> filterMethod () {
        List<T> resList = new ArrayList<>();
        return resList;
    }

    public static class TestLamda3 {

        @FunctionalInterface
        public interface FilterInter<T>{
            boolean filter(T t);
        }

        public static <T> List<T> filterMethod(List<T> list, FilterInter<T> f){
            List<T> resultList = new ArrayList<>();
            for (T fruit : list) {
                if (f.filter(fruit)) {
                    resultList.add(fruit);
                }
            }
            return resultList;
        }

        public static void main(String[] args) {
            // 과일 필터 메서드 만들거야 -> 사과, 바나나 기준 -> 인터페이스 상위에서 필터 하는 메서드
            System.out.println("TestLamda3");
            List<Apple> appleList = new ArrayList<>();
            appleList.add(new Apple(Color.RED,1));
            appleList.add(new Apple(Color.RED,2));
            appleList.add(new Apple(Color.GREEN,3));

            List<Banana> bananaList = new ArrayList<>();
            bananaList.add(new Banana(Color.YELLOW,4));
            bananaList.add(new Banana(Color.YELLOW,5));
            bananaList.add(new Banana(Color.GREEN,6));


            List<Apple> getRedApples = filterMethod(appleList, (Apple apple) -> apple.getColor().equals(Color.RED));
    //        List<Apple> getRedApples = filterMethod(appleList, new FilterInter<Apple>(){
    //            @Override
    //            public boolean filter(Apple apple){
    //              return apple.getColor().equals(Color.RED);
    //            };
    //        });
            System.out.println("getRedApples: " + getRedApples);


            List<Banana> getYellowBanana = filterMethod(bananaList, (Banana banana) -> banana.getColor().equals(Color.YELLOW));
            System.out.println("getYellowBanana: " + getYellowBanana);

        }





    }
}
