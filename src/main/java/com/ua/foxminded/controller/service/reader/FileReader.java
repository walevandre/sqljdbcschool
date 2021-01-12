package com.ua.foxminded.controller.service.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;


public class FileReader {

    public InputStream getFileFromResourceAsStream(String fileName) throws ReaderException {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new ReaderException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public List<String> getFileFromResourceAsList(String fileName) throws ReaderException {

        List<String> stringList = null;

        try {
            InputStreamReader in = new InputStreamReader(getFileFromResourceAsStream(fileName));
            stringList = new BufferedReader(in).lines().collect(Collectors.toList());
        } catch (ReaderException e) {
            throw new ReaderException(e);
        }
        return stringList;

    }
}
