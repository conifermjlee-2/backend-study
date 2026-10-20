package com.apitest.mytest.just_test;

import java.util.*;

class Employee{
    private int id;
    private String name;

    public int getId(){
        return id;
    };

    public void setId(int id){
        this.id = id;
    }

}

public class JustTest1 {
    public static void main(String[] args) throws InterruptedException{
        System.out.println("JustTest1 start");

//        List<Integer> list = new ArrayList<>();
        List<Integer> list = new Vector<>();

        Runnable task = () -> {
          for (int i = 0; i < 100_000; i++) {
              list.add(i);
          }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("list.size(): " + list.size());


//        List<Employee> list = new ArrayList<>();
//        Employee e = new Employee();
//        e.setId(100);
//        list.add(e);
//
//        List<Employee> copy = new ArrayList<>(list);   // 바구니만 새로 만듦
//
//        // 1. 바구니는 별개인지 확인
//        copy.add(new Employee());
//        System.out.println(list.size());   // 1 (원본은 그대로)
//        System.out.println(copy.size());   // 2
//
//        // 2. 안의 직원 객체는 같은 건지 확인
//        copy.get(0).setId(999);
//        System.out.println(list.get(0).getId());   // 999 (원본에도 반영됨)
//
//        // 3. 같은 객체가 맞는지 직접 확인
//        System.out.println(list.get(0) == copy.get(0));   // true
//        System.out.println(list.get(0) == copy.get(1));   // false

//        List<Integer> list = new ArrayList<>();
//        list.add(10);
//        list.add(20);
//        list.add(30);
//
//        List<Integer> copy = list;
//        copy.add(40);
//        System.out.println("list.size() :" +  list.size());




//        List<String> list = new ArrayList<>(List.of("A", "B", "C"));

        //예외 터진다
//        for (String s : list) {
//            if (s.equals("A")) {
//                list.remove(s);
//            }
//        }

//        List<Integer> list2 = new ArrayList<>(List.of(10,20,30));
//        list2.remove(1);
//        System.out.println("list2: " + list2);
//
//        list2.remove(Integer.valueOf(10));
//        System.out.println("list2: " + list2);
//
//        list2.remove(Integer.valueOf(50));
//        System.out.println("list2: " + list2);

//        List<String> list3 = new ArrayList<>(List.of("A","B","C"));
//        Iterator<String> it = list3.iterator();
//        while (it.hasNext()) {
//            System.out.println(it);
//            String s = it.next();
//            System.out.println(s);
//            if (s.equals("B")) {
//                it.remove();
//            }
//        }



//        Iterator<String> it = list3.iterator();
//        while (it.hasNext()) {
//            String s = it.next();
//            if (s.equals("B")) {
//                it.remove();
//            }
//        }
//        System.out.println("list3: " + list3);

//        list3.removeIf(s -> s.equals("A"));
//        System.out.println("list3: " + list3);

//        list3.remove(1);
//        System.out.println("list3: " + list3);
//
//        list3.remove("A");
//        System.out.println("list3: " + list3);
//
//        list3.remove("D");
//        System.out.println("list3: " + list3);



    }
}
