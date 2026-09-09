package com.apitest.mytest.modern_java.ch02.predicate;

import com.apitest.mytest.modern_java.ch02.domain.Apple;

public interface ApplePredicate {
    boolean test(Apple apple);
}
