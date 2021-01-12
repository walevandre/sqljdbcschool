package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.reader.FileReader;
import com.ua.foxminded.controller.service.reader.ReaderException;
import com.ua.foxminded.domain.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CoursesGenerator implements Generator<CoursesDao> {

    private static final Logger log = LoggerFactory.getLogger(CoursesGenerator.class);

    @Override
    public void generateFromFile(String filename, CoursesDao coursesDao) throws GeneratorDataException {
        FileReader fileReader = new FileReader();
        List<String> stringList = null;

        try {
            List<Course> coursesList = new ArrayList<>();
            stringList = fileReader.getFileFromResourceAsList(filename);
            if (!stringList.isEmpty() && stringList != null) {
                stringList.forEach(value -> coursesList.add(new Course().name(value)));
                coursesDao.saveList(coursesList);
            } else {
                log.debug("Empty list of courses to save in DB");
                throw new GeneratorDataException("Empty list of courses to save in DB");
            }
        } catch (ReaderException | DAOException e) {
            log.error("Failed to generate courses.", e);
            throw new GeneratorDataException("Failed to generate courses.", e);
        }
    }

    @Override
    public void generateRandomly(CoursesDao coursesDao) throws GeneratorDataException {
        log.warn("Unsupported operation");
        throw new GeneratorDataException("Unsupported operation");
    }
}
