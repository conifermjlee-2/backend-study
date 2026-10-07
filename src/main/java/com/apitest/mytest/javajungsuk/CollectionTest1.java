package com.apitest.mytest.javajungsuk;

import java.util.*;

public class CollectionTest1 {
    public static void main(String[] args) {
        System.out.println("CollectionTest1 start");

        // 1. set 중복 x
        Set<String> sets = new HashSet<>();
        sets.add("a");
        sets.add("b");
        sets.add("a");
        System.out.println("sets: " + sets);
        System.out.println(sets.size()); // 2
        System.out.println(sets.contains("b")); // true

        // 2. List 순회 + 정렬
        List<String> lists = new ArrayList<>(List.of("c","a","b"));
        System.out.println("before sort lists : " + lists);
        Collections.sort(lists);
        for (String s : lists) {
            System.out.println(s);
        }

        Map<String, Integer> maps = new HashMap<>();
        maps.put("one",1);
        maps.put("two",2);
        maps.put("three",3);

        for (Map.Entry<String, Integer> e : maps.entrySet()) {
            System.out.println(e.getKey() + " = "+ e.getValue());
        }


    }
}
