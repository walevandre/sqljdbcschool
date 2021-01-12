package com.ua.foxminded.controller.service.reader;

import com.ua.foxminded.controller.service.reader.FileReader;
import com.ua.foxminded.controller.service.reader.ReaderException;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
}
