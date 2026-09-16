package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LamDaMain {
    // 3.3.2 2단계 : 함수형 인터페이스
    @FunctionalInterface
    public interface BufferedReaderProcessor{
        String process(BufferedReader br) throws IOException;
    }

    //  3.3.3 3단계 : 동작 실행
    public static String processFile(BufferedReaderProcessor b) throws IOException {
        try (BufferedReader br =  new BufferedReader(new FileReader("test_sample.txt"))) {
            return b.process(br);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("ch3");
        //3.2.2 함수 디스크립터
        process( () -> System.out.println("test") );
//        process(new Runnable(){
//            @Override
//            public void run(){
//                System.out.println("test");
//            }
//        });

        // 3.3.4 4단계 : 람다 전달
        String processFileRes =  processFile( (br) -> br.readLine() );
        System.out.println("processFileRes : " + processFileRes);
    }

    //3.2.2 함수 디스크립터
    public static void process(Runnable r){
        r.run();
    }


// Runnable 인터페이스와 같은 시그니처를 갖는 람다식이 온다.
}


