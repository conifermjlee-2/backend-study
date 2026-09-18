package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferTest {
    public interface BufferedReaderProcessor{
        String process(BufferedReader br);
    }

    public static void main(String[] args) {
        System.out.println("BufferTest start");
//        brMethod();
//        brMethod( (br) -> br.readLine() );
            brMethod( (br) -> {
                try {
                    return br.readLine();
                } catch (IOException e){
                    System.out.println("IOException : " + e);
                    return null;
                }
            }

        );


    }

    public static void brMethod(BufferedReaderProcessor b) {
        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"))) {
            b.process(br);
        } catch (IOException e) {
            System.out.println("IOException start: " + e);
        }
    }
}
