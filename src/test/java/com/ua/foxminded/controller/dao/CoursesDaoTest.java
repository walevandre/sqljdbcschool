package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.testdata.GeneratorData;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Student;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CoursesDaoTest {

    private static CoursesDao coursesDao = new CoursesDao();


    @BeforeAll
    static void setup() {
        DBDeployment dbDeployment = new DBDeployment();
        try {
            dbDeployment.deploy();
        } catch (SqlExecuteException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getByIdCourse() throws DAOException {
        int courseId = 1;
        Course expected = new Course().id(courseId).name("Algebra");
        Course actual = coursesDao.getById(courseId).get();

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
    }

    @Test
    void saveCourseTest() throws DAOException {
        Course expected = new Course().name("new course").students(new ArrayList<>());
        int insertId = coursesDao.save(expected).id();
        expected.id(insertId);
        Course actual = coursesDao.getById(insertId).get();

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
    }

    @Test
    void deleteCourseTest() throws DAOException {
        int countCoursesBeforeDelete = coursesDao.getAll().size();
        coursesDao.delete(new Course().id(6));
        int countCoursesAfterDelete = coursesDao.getAll().size();
        assertEquals(countCoursesAfterDelete, countCoursesBeforeDelete - 1);
    }

    @Test
    void updateCourseTest() throws DAOException {
        Course expected = coursesDao.getById(3).get().name("Course");
        coursesDao.update(expected);
        Course actual = coursesDao.getById(3).get();

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
    }

    @Test
    void findStudentsByCourseTest() throws DAOException {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().id(1).firstName("John").lastName("Williams").groupId(1));
        expected.add(new Student().id(2).firstName("Natan").lastName("White").groupId(1));
        expected.add(new Student().id(3).firstName("Ethan").lastName("Hamilton").groupId(1));
        expected.add(new Student().id(4).firstName("Jakub").lastName("Bailey").groupId(2));

        List<Student> actual = coursesDao.findStudentsOnCourse(new Course().id(2));

        assertThat(actual, hasSize(4));
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).firstName(), actual.get(1).firstName());
        assertEquals(expected.get(1).lastName(), actual.get(1).lastName());
        assertEquals(expected.get(1).groupId(), actual.get(1).groupId());
    }
}
