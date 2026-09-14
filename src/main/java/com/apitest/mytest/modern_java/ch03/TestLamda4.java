package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestLamda4 {
    public static void main(String[] args) {

        System.out.println("TestLamda4 start");

        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt1"))) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            System.out.println("IOException : " + e);
        }


//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader("test_sample.txt"));
//            System.out.println(br.readLine());
//        } catch (IOException e) {
//            System.out.println("first fail:" + e);
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    System.out.println("close fail:" + e);
//                }
//            }
//        }






    }
}
