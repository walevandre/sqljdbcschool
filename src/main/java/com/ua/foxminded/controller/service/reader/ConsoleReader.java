package com.ua.foxminded.controller.service.reader;

import java.io.BufferedReader;
import java.io.IOException;

public class ConsoleReader {

    private BufferedReader bufferedReader;

    public ConsoleReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public int read() throws ReaderException {
        try {
            String line = bufferedReader.readLine();
            return Integer.parseInt(line);
        } catch (IOException e) {
            throw new ReaderException(e);
        } catch (NumberFormatException e){
            throw new ReaderException("Input data must be numeric", e);
        }
    }
}
