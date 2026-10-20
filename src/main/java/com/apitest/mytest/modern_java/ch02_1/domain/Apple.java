package com.apitest.mytest.modern_java.ch02_1.domain;

public class Apple {
    private Color color;
    private double weight;

    public Apple(){};

    public Apple(Color color, double weight){
        this.color = color;
        this.weight = weight;
    }

    public Color getColor(){
        return color;
    }

    public double getWeight(){
        return weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "color=" + color +
                ", weight=" + weight +
                '}';
    }

}
