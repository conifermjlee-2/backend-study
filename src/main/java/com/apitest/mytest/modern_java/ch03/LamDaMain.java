package com.apitest.mytest.modern_java.ch03;

public class LamDaMain {
    public static void main(String[] args){
        System.out.println("ch3");
        process( () -> System.out.println("test") );
    }

    public static void process(Runnable r){
        r.run();
    }


// Runnable 인터페이스와 같은 시그니처를 갖는 람다식이 온다.
}


