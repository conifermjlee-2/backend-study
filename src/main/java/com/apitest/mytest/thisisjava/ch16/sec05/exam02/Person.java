package com.apitest.mytest.thisisjava.ch16.sec05.exam02;

public class Person {
    public void ordering(Comparable comparable){
        String a = "홍길동";
        String b = "김길동";

        int result = comparable.compare(a, b);
        String msg = "";
        System.out.println("숫자 : " + result);
        if (result < 0) {
            msg = a + "는 " + b + "보다 앞에 옵니다.";
        } else {
            msg = a + "는 " + b + "보다 뒤 옵니다.";
        }
        System.out.println("msg : " + msg);

    }
}
