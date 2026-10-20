package com.apitest.mytest.thisisjava.ch05.fortynine;

public class Test49 {
    public static void main(String[] args) {
        String kind1 = "car";
        String kind2 = kind1;
        kind1 = null;
        System.out.println("kind2 : " + kind2);
    }
}
