package com.apitest.mytest.modern_java.ch03;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class T_26_10_20_2 {


    public static void main(String[] args) throws IOException {
        int number = 101;  // 람다 바깥에서 선언한 변수

//        Runnable r = () -> System.out.println(number);
//        r.run();
        number = 20; // 컴파일 오류
    }


}
