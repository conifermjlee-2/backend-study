package com.apitest.mytest.modern_java.ch03.domain;

import com.apitest.mytest.modern_java.ch02.domain.Color;

public class Banana {
    private Color color;
    private int weight;

    //기본 생성자1
    public Banana(){};

    //매개변수 생성자
    public Banana(Color color, int weight){
        this.color = color;
        this.weight = weight;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Banana{" +
                "color=" + color +
                ", weight=" + weight +
                '}';
    }
}
