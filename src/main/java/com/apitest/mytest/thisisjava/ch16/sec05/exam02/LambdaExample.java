package com.apitest.mytest.thisisjava.ch16.sec05.exam02;

public class LambdaExample {
    public static void main(String[] args) {

        Person person = new Person();
//        person.ordering((a, b)-> a.compareToIgnoreCase(b));
        // 매개변수 메소드 참조 (클래스 이름::인스턴스메소드)
        person.ordering(String::compareToIgnoreCase);
    }

}
