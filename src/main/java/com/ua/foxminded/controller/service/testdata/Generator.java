package com.ua.foxminded.controller.service.testdata;

public interface Generator<T> {

    void generateFromFile(String fileName, T t) throws GeneratorDataException;

    void generateRandomly (T t) throws GeneratorDataException;
}
