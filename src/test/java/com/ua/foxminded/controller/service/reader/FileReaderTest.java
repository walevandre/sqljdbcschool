package com.ua.foxminded.controller.service.reader;

import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.reader.FileReader;
import com.ua.foxminded.controller.service.reader.ReaderException;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileReaderTest {
    private static final String FILE = "testdata/dataForTestReader.txt";
    private FileReader fileReader = new FileReader();

    @Test
    void getFileFromResourceAsListTest() throws ReaderException {

        List<String> actual = fileReader.getFileFromResourceAsList(FILE);

        assertThat(actual, hasSize(3));
        assertThat(actual, not(IsEmptyCollection.empty()));
        assertThat(actual, hasItems("Algebra","Biology","Chemistry"));
    }

    @Test
    void getFileFromResourceAsListTestShouldThrowException(){
        Exception exception = assertThrows(ReaderException.class, () -> fileReader.getFileFromResourceAsList("testdata/test.txt"));
        assertEquals(ReaderException.class.getName() + ": file not found! testdata/test.txt", exception.getMessage());
    }
}
