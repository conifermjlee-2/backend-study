package com.apitest.mytest.modern_java.ch02.predicate;

import com.apitest.mytest.modern_java.ch02.domain.Apple;

public class ApplesHeavWeightPredicate implements ApplePredicate{
    @Override
    public boolean test(Apple apple){
        return apple.getWeight() > 2;
    };
}
