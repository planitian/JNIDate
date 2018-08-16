package com.example.datebase.bean;

public class NullGood implements GoodNull {
    @Override
    public boolean isNull() {
        return true;
    }
}
