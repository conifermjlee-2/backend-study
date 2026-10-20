package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class T_26_10_20 {
    // 1. 함수형 인터페이스: 람다가 따라야 할 규격
    @FunctionalInterface
    interface BufferedReaderProcessor {
        String process(BufferedReader br) throws IOException;
    }

    // 2. 실행 어라운드 패턴: 준비와 정리는 고정, 실제 작업은 전달받음
    public static String processFile(BufferedReaderProcessor processor)
            throws IOException {

        // [준비] 파일 열기
        try (BufferedReader br =
                     new BufferedReader(new FileReader("test_sample.txt"))) {

            // [실제 작업] 전달받은 람다 실행
            return processor.process(br);

        } // [정리] 파일 자동 닫기
    }

    public static void main(String[] args) throws IOException {

        // 한 줄 읽기
        String result1 = processFile(br -> br.readLine());

        // 두 줄 읽기: 실제 작업만 변경
        String result2 = processFile(
                br -> br.readLine() + br.readLine()
        );

        System.out.println(result1);
        System.out.println(result2);
    }
}
