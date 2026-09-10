package com.apitest.mytest.modern_java.ch02.domain;

public class Car {
    private String name;
    private long price;
    private double weight;

    public Car(String name, long price, double weight){
        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                '}';
    }
}
