package com.apitest.mytest.thisisjava.ch16.sec05.exam03;

public class Member {
    String id = "no1";
    String name = "kim";

    public Member (String id) {
        this.id = id;
        System.out.println("id is " + id);
    }

    public Member (String id, String name) {
        this.id = id;
        this.name = name;
        System.out.println("id is " + id + " name is :" + name);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
