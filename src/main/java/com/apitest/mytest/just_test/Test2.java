package com.apitest.mytest.just_test;
import java.util.*;

public class Test2 {
    public static void main(String[] args) throws Exception {
        List<Integer> list = new Vector<>();

        Runnable task = () -> {
            for (int i = 0; i < 100000; i++) {
                if (list.size() < 1500) {
                    list.add(i);
                }
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("제한: 1500, 실제: " + list.size());
    }
}
