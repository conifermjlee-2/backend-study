package com.apitest.mytest.modern_java.ch03.domain;

@FunctionalInterface
public interface Animal<T> {
    String name(T name);
}
