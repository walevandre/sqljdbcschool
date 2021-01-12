package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.testdata.CoursesGenerator;
import com.ua.foxminded.controller.service.testdata.GeneratorDataException;
import com.ua.foxminded.domain.Course;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseGeneratorTest {

    private StudentsDao studentsDao = Mockito.mock(StudentsDao.class);
    private CoursesDao coursesDao = Mockito.mock(CoursesDao.class);
    private GroupsDao groupsDao = Mockito.mock(GroupsDao.class);

    private CoursesGenerator coursesGenerator = new CoursesGenerator();
    private static final String COURSES_TEST_DATA = "testData/courses.txt";

    @Test
    void generateFromFileTest() throws GeneratorDataException, DAOException {

        coursesGenerator.generateFromFile(COURSES_TEST_DATA, coursesDao);

        ArgumentCaptor<List<Course>> coursesCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(coursesDao).saveList(coursesCaptor.capture());

        List<Course> value = coursesCaptor.getValue();

        assertThat(value, hasSize(10));
        assertEquals(Course.class, value.get(0).getClass());
        assertEquals("Algebra", value.get(0).name());

    }
}
