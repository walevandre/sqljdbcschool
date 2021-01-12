package com.ua.foxminded.controller.service;

import java.util.Random;

public class RandomlyGenerator {

    private static Random random = new Random();

    public static int getNumberFromRange(int min, int max) throws IllegalArgumentException {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return random.ints(min, max).findFirst().getAsInt();
    }

}
