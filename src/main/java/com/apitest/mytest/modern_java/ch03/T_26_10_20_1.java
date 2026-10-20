package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class T_26_10_20_1 {

    @FunctionalInterface
    public interface BufferedReaderProcessor {
        String process(BufferedReader br) throws IOException;
    }

    public static void main(String[] args){
        System.out.println("T_26_10_20_1 start");

        String me1 =  method1(new BufferedReaderProcessor () {
            @Override
            public String process(BufferedReader br) throws IOException {
                return br.readLine() + br.readLine();
            }
        });

        System.out.println("me1: " + me1);

        String me2 = method1((br) -> br.readLine());
        System.out.println("me2: " + me2);
    }

    public static String method1(BufferedReaderProcessor p){
        System.out.println("method1 start");
        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"));) {
            System.out.println();
            return p.process(br);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
