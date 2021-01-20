package com.ua.foxminded.controller.service;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomlyGeneratorTest {

    @RepeatedTest(10)
    void getNumberFromRangeTest() {
        int rand = RandomlyGenerator.getNumberFromRange(0, 5);
        assertEquals(true, (rand >= 0 && rand < 5));
    }

    @Test
    void getNumberFromRangeShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> RandomlyGenerator.getNumberFromRange(5, 4));
        assertEquals("max must be greater than min", exception.getMessage());
    }
}