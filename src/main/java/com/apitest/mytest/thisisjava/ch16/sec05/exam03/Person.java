package com.apitest.mytest.thisisjava.ch16.sec05.exam03;

public class Person {
    String id = "id_1";
    String name = "winter";

    public Member createMember1(Creatable1 creatable1){
        return creatable1.create(id);
    }

    public Member createMember2(Creatable2 creatable1){
        return creatable1.create(id, name);
    }
}
