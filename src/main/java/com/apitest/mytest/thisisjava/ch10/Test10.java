package com.apitest.mytest.thisisjava.ch10;

public class Test10 {
    public static void main(String[] args) {
        Account account = new Account();

        account.deposit(100);
        System.out.println("현재 금액:" + account.getBalance());

        // 호출한쪽에서 예외 처리
        try {
            account.withdraw(1000);
        } catch (InsufficientException e) {
            String message = e.getMessage();
            System.out.println(message);
        }

        System.out.println("예금액: "+ account.getBalance());

    }
}
