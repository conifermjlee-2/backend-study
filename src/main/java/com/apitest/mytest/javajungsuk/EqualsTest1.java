package com.apitest.mytest.javajungsuk;

import java.util.HashSet;
import java.util.Objects;

class Employee {
    private Integer id;
    private String firstname;
    private String lastName;
    private String department;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;

        Employee e = (Employee) o;
        return Objects.equals(this.id, e.id);   // id가 null이어도 안전
    }
}

public class EqualsTest1 {


    public static void main(String[] args) {
        System.out.println("EqualsTest1 start");
        Employee e1 = new Employee();
        Employee e2 = new Employee();
        e1.setId(100);
        e2.setId(100);
        System.out.println(e1 == e2);
        System.out.println("e1.equals(e2) : " + e1.equals(e2)); // true

        String s1 = new String("Test");
        String s2 = new String("Test");

        System.out.println("s1 == s2: " + s1 == s2);         // false : 주소 비교 (서로 다른 객체)
        System.out.println("s1.equals(s2): "  + s1.equals(s2));   // true  : 내용 비교

        HashSet<Employee> hashSet = new HashSet<>();
        hashSet.add(e1);
        hashSet.add(e2);
        System.out.println("hashSet.size() : " + hashSet.size()); // hashSet.size() : 1 => hashCode() 오버라이딩 구현해서 중복 체크!
    }
}
