package com.example.demoException;

public class FoodOrderNotFoundException extends RuntimeException {

    public FoodOrderNotFoundException(String message) {
        super(message);
    }
}
