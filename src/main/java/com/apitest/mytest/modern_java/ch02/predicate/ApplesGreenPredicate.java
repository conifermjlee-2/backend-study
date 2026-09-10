package com.apitest.mytest.modern_java.ch02.predicate;

import com.apitest.mytest.modern_java.ch02.domain.Apple;
import com.apitest.mytest.modern_java.ch02.domain.Color;

import java.util.ArrayList;
import java.util.List;

public class ApplesGreenPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple){
        return apple.getColor().equals(Color.GREEN);
    };
}
