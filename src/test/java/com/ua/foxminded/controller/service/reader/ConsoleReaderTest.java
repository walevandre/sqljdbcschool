package com.ua.foxminded.controller.service.reader;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleReaderTest {

    private BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    private ConsoleReader consoleReader = new ConsoleReader(bufferedReader);

    @Test
    void read() throws IOException, ReaderException {
        Mockito.when(bufferedReader.readLine()).thenReturn("5");
        int actual = consoleReader.read();
        assertEquals(5, actual);
        Mockito.verify(bufferedReader).readLine();
    }
}