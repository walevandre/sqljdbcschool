package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.DBService.SqlExecutor;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

class StudentsDaoTest {

    private static StudentsDao studentsDao = new StudentsDao();
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
    void saveListStudentsTest() throws DAOException {
        List<Student> students = new ArrayList<>();
        students.add(new Student().firstName("John").lastName("Williams").groupId(1));
        students.add(new Student().firstName("Natan").lastName("White").groupId(1));
        students.add(new Student().firstName("Ethan").lastName("Hamilton").groupId(1));
        students.add(new Student().firstName("Jakub").lastName("Bailey").groupId(2));

        int[] insertedId = studentsDao.saveList(students);
        assertEquals(students.size(), insertedId.length);

        Student actual = studentsDao.getById(insertedId[0]).get();
        assertEquals("John", actual.firstName());
        assertEquals("Williams", actual.lastName());
        assertEquals(1, actual.groupId());

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
    void getStudentsByIdShouldThrowExceptionWhenTableNotFound() {
        dropTable();
        Student student = new Student().id(1);
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.getById(student.id()));
        assertEquals("Cannot get student by id=" + student.id(), exception.getMessage());
    }

    @Test
    void getAllStudentsByIdShouldThrowExceptionWhenTableNotFound() {
        dropTable();
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.getAll());
        assertEquals("Cannot get all students", exception.getMessage());
    }

    @Test
    void saveShouldThrowExceptionWhenTableNotFound() {
        dropTable();
        Student student = new Student().firstName("John").lastName("Williams");
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.save(student));
        assertEquals("Cannot insert into student", exception.getMessage());
    }


    @Test
    void saveListShouldThrowExceptionWhenTableNotFound() {
        dropTable();
        List<Student> students = new ArrayList<>();
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.saveList(students));
        assertEquals("Cannot save list of students", exception.getMessage());
    }

    @Test
    void saveListShouldThrowExceptionWhenNothingWasInserted() {
        List<Student> students = new ArrayList<>();
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.saveList(students));
        assertEquals("Nothing was inserted", exception.getMessage());
    }

    @Test
    void updateStudentsByIdShouldThrowExceptionWhenTableNotFound() {
        dropTable();
        Student student = new Student().id(1);
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.update(student));
        assertEquals("Cannot update student by id=" + student.id(), exception.getMessage());
    }

    @Test
    void updateStudentsByIdShouldThrowExceptionWhenIdNoExists() {
        Student student = new Student().id(-1);
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.update(student));
        assertEquals("No update by id=" + student.id(), exception.getMessage());
    }

    @Test
    void deleteShouldThrowExceptionWhenIdNoExists() {
        dropTable();
        Student student = new Student().id(1);
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.delete(student));
        assertEquals("Cannot delete student " + student.id(), exception.getMessage());
    }

    @Test
    void deleteReturnsFalse() throws DAOException {
        Student student = new Student().id(-1);
        assertEquals(false, studentsDao.delete(student));
    }

    @Test
    void assignToCourseShouldThrowExceptionWhenIdNoExists() {
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.assignToCourse(-1, 100));
        assertEquals("Cannot assign student id=100 to course id=-1", exception.getMessage());
    }

    @Test
    void removeFromCourseShouldThrowExceptionWhenIdNoExists() {
        dropTable();
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.removeFromCourse(1, 1));
        assertEquals("Cannot remove student id=1 to course id=1", exception.getMessage());
    }

    @Test
    void removeFromCourseReturnsFalse() throws DAOException {
        assertEquals(false, studentsDao.removeFromCourse(100, 100));
    }

    @Test
    void findCourseByStudentShouldThrowException(){
        dropTable();
        Exception exception = assertThrows(DAOException.class, () -> studentsDao.findCoursesByStudent(new Student().id(1)));
        assertEquals("Cannot find courses related to student with id 1", exception.getMessage());
    }
}
