package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.DBService.SqlExecutor;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Student;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoursesDaoTest {

    private static CoursesDao coursesDao = new CoursesDao();
    private DBDeployment dbDeployment = new DBDeployment(new SqlExecutor());


    @BeforeEach
    void setup() {
        try {
            dbDeployment.deploy();
        } catch (SqlExecuteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void dropTable() {
        try {
            dbDeployment.dropAllTables();
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
    void saveListTest() throws DAOException {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course().name("Course1"));
        courses.add(new Course().name("Course2"));
        courses.add(new Course().name("Course3"));

        int[] insertedId = coursesDao.saveList(courses);
        assertEquals(courses.size(), insertedId.length);
        assertEquals("Algebra", coursesDao.getById(insertedId[0]).get().name());
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

    @Test
    void getByIdShouldThrowException() {
        dropTable();
        Course course = new Course().id(1);
        Exception exception = assertThrows(DAOException.class, () -> coursesDao.getById(course.id()));
        assertEquals("Cannot get course with id 1", exception.getMessage());
    }

    @Test
    void getAllShouldThrowException() {
        dropTable();
        Exception exception = assertThrows(DAOException.class, () -> coursesDao.getAll());
        assertEquals("Cannot get all courses", exception.getMessage());
    }

    @Test
    void saveShouldThrowException() {
        dropTable();
        Course course = new Course().name("nameCourse");
        Exception exception = assertThrows(DAOException.class, () -> coursesDao.save(course));
        assertEquals("Cannot insert into course", exception.getMessage());
    }

    @Test
    void saveListShouldThrowException() {
        List<Course> courses = new ArrayList<>();
        Exception exception = assertThrows(DAOException.class, () -> coursesDao.saveList(courses));
        assertEquals("Nothing was inserted", exception.getMessage());

        dropTable();
        courses.add(new Course().name("Course1"));
        courses.add(new Course().name("Course2"));
        courses.add(new Course().name("Course3"));
        exception = assertThrows(DAOException.class, () -> coursesDao.saveList(courses));
        assertEquals("Cannot save list of courses", exception.getMessage());
    }

    @Test
    void updateShouldThrowException() {
        Course course = new Course().id(100);
        Exception exception = assertThrows(DAOException.class, () -> coursesDao.update(course));
        assertEquals("No update by id=100", exception.getMessage());

        dropTable();
        exception = assertThrows(DAOException.class, () -> coursesDao.update(course));
        assertEquals("Cannot update courses by id=100", exception.getMessage());
    }

    @Test
    void deleteShouldReturnFalse() throws DAOException {
        Course course = new Course().id(100);
        assertEquals(false, coursesDao.delete(course));
    }

    @Test
    void deleteShouldThrowException() {
        dropTable();
        Course course = new Course().id(1).name("Algebra");
        Exception exception = assertThrows(DAOException.class, () -> coursesDao.delete(course));
        assertEquals("Cannot delete course with id 1", exception.getMessage());
    }

    @Test
    void findStudentsOnCourseShouldThrowException() {
        dropTable();
        Course course = new Course().id(1);
        Exception exception = assertThrows(DAOException.class, () -> coursesDao.findStudentsOnCourse(course));
        assertEquals("Cannot find students related to course with id 1", exception.getMessage());
    }
}
