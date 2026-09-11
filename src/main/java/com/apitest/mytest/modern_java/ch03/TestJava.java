package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestJava {

    // 1. 함수형 인터페이스 정의 (BufferedReader를 받아서 String을 반환하는 규격)
    @FunctionalInterface
    public interface BufferedReaderProcessor {
        String process(BufferedReader br) throws IOException;
    }

    // 2. 람다를 인수로 받아서 실행하는 메서드 (실행 어라운드 패턴)
    public static String processFile(BufferedReaderProcessor p) throws IOException {
        // test_sample.txt 파일을 열고 닫는 작업 사이에 외부에서 주입받은 람다(p.process)를 실행
        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"))) {
            return p.process(br);
        }
    }

    // 1. 메서드 분리
//    public static String processFileOne(){
//        try {
//            BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"));
//            return br.readLine();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    };

    @FunctionalInterface
    public interface BufferedReaderProcessorOne{
        String process(BufferedReader br) throws IOException;
    }

    // 2. 메서드 분리 + 인터페이스화
    public static String processFileOne(BufferedReaderProcessorOne p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"))) {
            return p.process(br);
        }
    };



    public static void main(String[] args) throws IOException {
        // 3. 람다를 전달해서 원하는 동작을 자유롭게 수행

        // 동작 1: 한 줄만 읽기
        String oneLine = processFile((BufferedReader br) -> br.readLine());
        System.out.println("One line: " + oneLine);

        // 동작 2: 두 줄 읽기
        String twoLines = processFile(br -> br.readLine() + " " + br.readLine());
        System.out.println("Two lines: " + twoLines);

//        try {
//            BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"));
//            System.out.println("my br(1):" + br.readLine());
//            System.out.println("my br(2):" + br.readLine());
//            System.out.println("my br(3):" + br.readLine());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        String result =  processFileOne( (BufferedReader br) -> br.readLine());
        System.out.println(": " + result);

    }
}