package com.apitest.mytest.thisisjava.ch11;

public class Account {
    //기본 생성자 없으면 자동 생성
    public Account(){}

    public long balance;

    public long getBalance(){
        return balance;
    }

    // 입금
    public void deposit(int money){
        balance += money;
    }

    // 출금 -> 예외처리를 호출한 쪽에서 처리
    public void withdraw(int money) throws InsufficientException{
        if (balance < money) {
            throw new InsufficientException("잔고부족 : " +  (money-balance) + " 부족 " );
        }
        balance -= money;
    }

}
