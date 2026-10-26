package com.apitest.mytest.thisisjava.ch16.sec05.exam03;

public class LambdaExample {
    public static void main(String[] args) {
        Person person = new Person();
        person.createMember1((id)-> new Member(id));
        person.createMember1(Member::new);


        person.createMember2(new Creatable2() {
            @Override
            public Member create(String id, String name) {
                return new Member(id, name);
            }
        });
        person.createMember2((id, name)-> new Member(id, name));
        person.createMember2(Member::new);
    }
}
