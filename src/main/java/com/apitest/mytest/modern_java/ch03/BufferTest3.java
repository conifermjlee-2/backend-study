package com.apitest.mytest.modern_java.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferTest3 {
    public interface BrInterFace3 {
        String brProcess(BufferedReader t) throws IOException;
    }

    public static void main(String[] args) {

        String brMethod3Res = brMethod3(
             new BrInterFace3(){
                 @Override
                 public String brProcess(BufferedReader br) throws IOException{
                     return  br.readLine() + br.readLine();
                 }
             }
        );


        System.out.println("brMethod3Res : " + brMethod3Res);
    }

    public static String brMethod3(BrInterFace3 paramBr) {
        System.out.println("brMethod3 start");

        try (BufferedReader br = new BufferedReader(new FileReader("test_sample.txt"))) {
//            return br.readLine();
            return paramBr.brProcess(br);
        } catch (IOException e) {
            return "IOException : " + e;
        }
    }
}
