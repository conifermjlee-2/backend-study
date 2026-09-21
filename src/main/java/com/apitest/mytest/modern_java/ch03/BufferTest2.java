package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferTest2 {
    public interface BrInterFace<T>{
        String brProcess(T t) throws IOException;
    }

    public static void main(String[] args) {
        System.out.println("BufferTest2");
        String brMethod2Res = brMethod2(
//                new BrInterFace<BufferedReader>(){
                new BrInterFace<BufferedReader>(){
                    @Override
                    public String brProcess(BufferedReader br) throws IOException{
                        return br.readLine();
                    }
                }
        );


        System.out.println("brMethod2Res : " + brMethod2Res);
    }

    public static String brMethod2(BrInterFace<BufferedReader> bp) {
        System.out.println("brMethod2 start");
        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"))) {
            return bp.brProcess(br);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}




//package com.apitest.mytest.modern_java.ch03;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class BufferTest2 {
//    public interface BrInterFace<T>{
//        String process(T t) throws IOException;   // throws 추가
//    }
//
//    public static void main(String[] args) {
//        System.out.println("BufferTest2");
//        brMethod2(new BrInterFace<BufferedReader>(){
//            @Override
//            public String process(BufferedReader br) throws IOException {   // String, throws 추가
//                return br.readLine();
//            }
//        });
//    }
//
//    public static void brMethod2(BrInterFace<BufferedReader> b) {   // 파라미터 추가
//        System.out.println("brMethod2 start ");
//
//        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"))) {
//            String result = b.process(br);   // 넘겨받은 b를 실제로 사용
//            System.out.println(result);
//        } catch (Exception e) {
//            System.out.println("Exception : " + e);
//        }
//    }
//}