package com.apitest.mytest.thisisjava.ch06;

public class SingletonExample {
    public static void main(String[] args) {
        System.out.println("SingletonExample start" );

        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        System.out.println(s1 == s2);
    }
}
