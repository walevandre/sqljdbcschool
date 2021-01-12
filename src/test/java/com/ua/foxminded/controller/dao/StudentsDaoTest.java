package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.testdata.GeneratorData;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

class StudentsDaoTest {

    private static StudentsDao studentsDao = new StudentsDao();

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
    void getByIdStudentTest() throws DAOException {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course().id(1).name("Algebra"));
        courses.add(new Course().id(2).name("Biology"));
        courses.add(new Course().id(3).name("Physics"));
        courses.add(new Course().id(4).name("Science"));
        Student expected = new Student()
                .id(1)
                .firstName("John")
                .lastName("Williams")
                .group(new Group().id(1).name("XX-10"))
                .groupId(1)
                .courses(courses);
        Student actual = studentsDao.getById(1).get();
        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.group(), actual.group());
        assertEquals(expected.groupId(), actual.groupId());
        assertEquals(expected.courses(), actual.courses());
    }

    @Test
    void saveStudentTest() throws DAOException {
        Student expected = new Student()
                .firstName("John")
                .lastName("Hamilton")
                .groupId(1)
                .group(new Group().id(1).name("XX-10"))
                .courses(new ArrayList<>());
        int insertId = studentsDao.save(expected).id();
        Student actual = studentsDao.getById(insertId).get();
        expected.id(actual.id());

        assertEquals(expected, actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.group(), actual.group());
        assertEquals(expected.groupId(), actual.groupId());
        assertEquals(expected.courses(), actual.courses());
    }

    @Test
    public void updateStudentTest() throws DAOException {
        Student expected = studentsDao.getById(1).get().firstName("David");
        studentsDao.update(expected);
        Student actual = studentsDao.getById(1).get();

        assertEquals(expected, actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.group(), actual.group());
        assertEquals(expected.groupId(), actual.groupId());
        assertEquals(expected.courses(), actual.courses());
    }

    @Test
    void removeStudentTest() throws DAOException {
        int countBeforeRemove = studentsDao.getAll().size();
        studentsDao.delete(studentsDao.getById(2).get());
        int countAfterRemove = studentsDao.getAll().size();
        assertEquals(countBeforeRemove, countAfterRemove + 1);
    }

    @Test
    void assignStudentToCourseTest() throws DAOException {
        int studentId = 3;
        List<Course> courses = new ArrayList<>();
        courses.add(new Course().id(2).name("Biology"));
        courses.add(new Course().id(3).name("Physics"));
        courses.add(new Course().id(4).name("Science"));
        Student expected = new Student()
                .id(studentId)
                .firstName("Ethan")
                .lastName("Hamilton")
                .group(new Group().id(1).name("XX-10"))
                .groupId(1)
                .courses(courses);

        studentsDao.assignToCourse(4, studentId);
        Student actual = studentsDao.getById(studentId).get();
        assertEquals(expected, actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.group(), actual.group());
        assertEquals(expected.groupId(), actual.groupId());
        assertEquals(expected.courses(), actual.courses());
    }

    @Test
    void removeStudentFromCourse() throws DAOException {

        int studentId = 4;
        List<Course> courses = new ArrayList<>();
        courses.add(new Course().id(2).name("Biology"));

        Student expected = new Student()
                .id(studentId)
                .firstName("Jakub")
                .lastName("Bailey")
                .group(new Group().id(2).name("ZZ-20"))
                .groupId(2)
                .courses(courses);

        studentsDao.removeFromCourse(5, studentId);
        Student actual = studentsDao.getById(studentId).get();
        assertEquals(expected, actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.group(), actual.group());
        assertEquals(expected.groupId(), actual.groupId());
        assertEquals(expected.courses(), actual.courses());
    }

    @Test
    void findCoursesByStudentTest() throws DAOException {
        StudentsDao studentsDao = new StudentsDao();
        List<Course> expected = new ArrayList<>();
        expected.add(new Course().id(1).name("Algebra"));
        expected.add(new Course().id(2).name("Biology"));
        expected.add(new Course().id(3).name("Physics"));
        expected.add(new Course().id(4).name("Science"));

        Student student = new Student()
                .id(1)
                .firstName("John")
                .lastName("Williams")
                .group(new Group().id(1).name("XX-10"))
                .groupId(1);

        List<Course> actual = studentsDao.findCoursesByStudent(student).courses();

        assertThat(actual, hasSize(4));
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).name(), actual.get(1).name());
    }

    @Test
    public void getStudentsByIdThrowsException() throws DAOException, SQLException {
        Student student = null;

        Exception exception = assertThrows(DAOException.class, ()->studentsDao.update(student));
        assertEquals("Cannot update student by id=" + student.id(), exception.getMessage());
    }
}
