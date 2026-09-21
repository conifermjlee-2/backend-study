package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferTest {
    public interface BufferedReaderProcessor {
        String process(BufferedReader br) throws IOException;
    }

    public static void main(String[] args) {
        System.out.println("BufferTest start");
        // 아래 3가지 다 같음
//        brMethod( (br) -> br.readLine() );
//        brMethod( BufferedReader::readLine);

        String brOne = brMethod((br) -> {
            try {
                return br.readLine();
            } catch (IOException e) {
                System.out.println("IOException : " + e);
                return null;
            }
        });

        System.out.println("brOne : " + brOne);


    }

    public static String brMethod(BufferedReaderProcessor b) {
        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"))) {
            return b.process(br);
        } catch (IOException e) {
            return "IOException start: " + e;
        }
    }
}
