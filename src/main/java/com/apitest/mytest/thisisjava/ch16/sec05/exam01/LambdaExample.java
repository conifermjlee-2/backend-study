package com.apitest.mytest.thisisjava.ch16.sec05.exam01;

public class LambdaExample {
    public static void main(String[] args) {
        Person person = new Person();

//        person.action((x, y) -> x + y);
//        person.action((x, y) -> Computer.staticMethod(x, y));
        // 1. 클래스::메서드 참조 (위 모양에서 진화)
        person.action(Computer::staticMethod);

        Computer com = new Computer();

//        person.action((x, y) -> x+y);
//        person.action((x, y) -> com.instanceMethod(x, y));
        person.action(com::instanceMethod);



    }
}
