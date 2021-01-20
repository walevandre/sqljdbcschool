package com.ua.foxminded.controller.service;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

class SchoolServiceTest {

    private StudentsDao studentsDao = Mockito.mock(StudentsDao.class);
    private CoursesDao coursesDao = Mockito.mock(CoursesDao.class);
    private GroupsDao groupsDao = Mockito.mock(GroupsDao.class);

    private SchoolService serviceSchool = new SchoolServiceImpl(studentsDao, coursesDao, groupsDao);

    @AfterEach
    void verifyNoMoreInteractionsAfterEachTest() {
        Mockito.verifyNoMoreInteractions(studentsDao);
        Mockito.verifyNoMoreInteractions(coursesDao);
        Mockito.verifyNoMoreInteractions(groupsDao);
    }

    @Test
    void getAllStudentsTest() throws DAOException, SchoolServiceException {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().id(1).firstName("John").lastName("Williams").groupId(1));
        expected.add(new Student().id(2).firstName("Natan").lastName("White").groupId(1));
        expected.add(new Student().id(3).firstName("Ethan").lastName("Hamilton").groupId(1));
        expected.add(new Student().id(4).firstName("Jakub").lastName("Bailey").groupId(2));

        Mockito.when(studentsDao.getAll()).thenReturn(expected);
        List<Student> actual = serviceSchool.getAllStudents();

        assertThat(actual, hasSize(4));
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertEquals(expected.get(1).id(), expected.get(1).id());
        assertEquals(expected.get(1).firstName(), expected.get(1).firstName());
        assertEquals(expected.get(1).lastName(), expected.get(1).lastName());
        assertEquals(expected.get(1).groupId(), expected.get(1).groupId());

        Mockito.verify(studentsDao).getAll();
    }

    @Test
    void getAllStudentShouldThrowException() throws DAOException {
        Mockito.when(studentsDao.getAll()).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.getAllStudents());
        Mockito.verify(studentsDao).getAll();
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void getAllCoursesTest() throws DAOException, SchoolServiceException {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course().id(1).name("Algebra"));
        expected.add(new Course().id(2).name("Biology"));
        expected.add(new Course().id(3).name("Physics"));
        expected.add(new Course().id(4).name("Science"));

        Mockito.when(coursesDao.getAll()).thenReturn(expected);
        List<Course> actual = serviceSchool.getAllCourses();

        assertThat(actual, hasSize(4));
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).name(), actual.get(1).name());

        Mockito.verify(coursesDao).getAll();
    }

    @Test
    void getAllCoursesShouldThrowException() throws DAOException {
        Mockito.when(coursesDao.getAll()).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.getAllCourses());
        Mockito.verify(coursesDao).getAll();
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void getCourseByStudentTest() throws DAOException, SchoolServiceException {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course().id(1).name("Algebra"));
        expected.add(new Course().id(2).name("Biology"));
        Student student = new Student().id(1).firstName("John").lastName("Williams").groupId(1).courses(expected);

        Mockito.when(studentsDao.findCoursesByStudent(any())).thenReturn(student);
        List<Course> actual = serviceSchool.getCourseByStudent(student);

        assertThat(actual, hasSize(2));
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).name(), actual.get(1).name());

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(studentsDao).findCoursesByStudent(studentCaptor.capture());

        assertThat((studentCaptor.getAllValues()), hasSize(1));
        Student value = studentCaptor.getValue();

        assertEquals(student.id(), value.id());
        assertEquals(student.firstName(), value.firstName());
        assertEquals(student.lastName(), value.lastName());
        assertEquals(student.groupId(), value.groupId());
    }

    @Test
    void getCourseByStudentShouldThrowException() throws DAOException {
        Mockito.when(studentsDao.findCoursesByStudent(any())).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.getCourseByStudent(any()));
        Mockito.verify(studentsDao).findCoursesByStudent(any());
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void findGroupsByParamsTest() throws DAOException, SchoolServiceException {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().id(1).name("XX-10"));
        expected.add(new Group().id(3).name("SS-20"));
        expected.add(new Group().id(4).name("DD-20"));

        Mockito.when(groupsDao.findAllWithLessOrEqualsStudentCount(anyInt())).thenReturn(expected);
        List<Group> actual = serviceSchool.findGroupsByParams(10);

        assertThat(actual, hasSize(3));
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).name(), actual.get(1).name());

        Mockito.verify(groupsDao).findAllWithLessOrEqualsStudentCount(anyInt());
    }

    @Test
    void findGroupsByParamsShouldThrowException() throws DAOException {
        Mockito.when(groupsDao.findAllWithLessOrEqualsStudentCount(anyInt())).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.findGroupsByParams(anyInt()));
        Mockito.verify(groupsDao).findAllWithLessOrEqualsStudentCount(anyInt());
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void findStudentsByParamsTest() throws DAOException, SchoolServiceException {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().id(1).firstName("John").lastName("Williams").groupId(1));
        expected.add(new Student().id(2).firstName("Natan").lastName("White").groupId(1));
        expected.add(new Student().id(3).firstName("Ethan").lastName("Hamilton").groupId(1));
        expected.add(new Student().id(4).firstName("Jakub").lastName("Bailey").groupId(2));

        Course course = new Course().id(1).name("Biology");

        Mockito.when(coursesDao.findStudentsOnCourse(any())).thenReturn(expected);
        List<Student> actual = serviceSchool.findStudentsByParams(course);

        assertThat(actual, hasSize(4));
        assertThat(actual, not(IsEmptyCollection.empty()));

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        Mockito.verify(coursesDao).findStudentsOnCourse(courseCaptor.capture());

        assertThat((courseCaptor.getAllValues()), hasSize(1));
        Course value = courseCaptor.getValue();

        assertEquals(course.id(), value.id());
        assertEquals(course.name(), value.name());
    }

    @Test
    void findStudentsByParamsShouldThrowException() throws DAOException {
        Mockito.when(coursesDao.findStudentsOnCourse(any())).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.findStudentsByParams(any()));
        Mockito.verify(coursesDao).findStudentsOnCourse(any());
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void addNewStudentTest() throws DAOException, SchoolServiceException {

        Student expected = new Student().id(10).firstName("John").lastName("Hamilton").groupId(4);

        Mockito.when(studentsDao.save(any())).thenReturn(expected);
        Student actual = serviceSchool.addNewStudent(expected);

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.groupId(), actual.groupId());

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(studentsDao).save(studentCaptor.capture());
        actual = studentCaptor.getValue();

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.groupId(), actual.groupId());
    }

    @Test
    void addNewStudentShouldThrowException() throws DAOException {
        Mockito.when(studentsDao.save(any())).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.addNewStudent(any()));
        Mockito.verify(studentsDao).save(any());
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void deleteStudentTest() throws DAOException, SchoolServiceException {
        Mockito.when(studentsDao.delete(any())).thenReturn(true);
        Student expected = new Student().id(1).firstName("John").lastName("Hamilton");

        assertEquals(true, serviceSchool.deleteStudent(expected));

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(studentsDao).delete(studentCaptor.capture());

        Student value = studentCaptor.getValue();

        assertEquals(expected.id(), value.id());
        assertEquals(expected.firstName(), value.firstName());
        assertEquals(expected.lastName(), value.lastName());
        assertEquals(expected.groupId(), value.groupId());
    }

    @Test
    void deleteStudentShouldThrowException() throws DAOException {
        Mockito.when(studentsDao.delete(any())).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.deleteStudent(any()));
        Mockito.verify(studentsDao).delete(any());
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void deleteStudentShouldShouldReturnFalse() throws DAOException, SchoolServiceException {
        Mockito.when(studentsDao.delete(any())).thenReturn(false);
        assertEquals(false, serviceSchool.deleteStudent(any()));
        Mockito.verify(studentsDao).delete(any());
    }

    @Test
    void addStudentToCourseTest() throws DAOException, SchoolServiceException {
        Mockito.when(studentsDao.assignToCourse(anyInt(), anyInt())).thenReturn(true);
        Student expected = new Student().id(1).firstName("John").lastName("Hamilton");
        assertEquals(true, serviceSchool.addStudentToCourse(expected, 3));
        Mockito.verify(studentsDao).assignToCourse(3, expected.id());
    }

    @Test
    void addStudentShouldThrowException() throws DAOException {
        Mockito.when(studentsDao.assignToCourse(anyInt(), anyInt())).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () -> serviceSchool.addStudentToCourse(new Student().id(1), 1));
        Mockito.verify(studentsDao).assignToCourse(anyInt(), anyInt());
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void addStudentShouldShouldReturnFalse() throws DAOException, SchoolServiceException {
        Mockito.when(studentsDao.assignToCourse(anyInt(), anyInt())).thenReturn(false);
        assertEquals(false, serviceSchool.addStudentToCourse(new Student().id(1), 1));
        Mockito.verify(studentsDao).assignToCourse(anyInt(), anyInt());
    }

    @Test
    void removeStudentFromCourseTest() throws DAOException, SchoolServiceException {
        Student student = new Student().id(1).firstName("John").lastName("Hamilton");
        Course course = new Course().id(1).name("Biology");
        Mockito.when(studentsDao.removeFromCourse(anyInt(), anyInt())).thenReturn(true);
        assertEquals(true, serviceSchool.removeStudentFromCourse(student, course));
        Mockito.verify(studentsDao).removeFromCourse(anyInt(), anyInt());
    }

    @Test
    void removeStudentShouldThrowException() throws DAOException {
        Mockito.when(studentsDao.removeFromCourse(anyInt(), anyInt())).thenThrow(new DAOException("test"));
        Exception exception = assertThrows(SchoolServiceException.class, () ->
                serviceSchool.removeStudentFromCourse(new Student().id(1), new Course().id(1)));
        Mockito.verify(studentsDao).removeFromCourse(anyInt(), anyInt());
        assertEquals("Error by action: test", exception.getMessage());
    }

    @Test
    void removeStudentShouldShouldReturnFalse() throws DAOException, SchoolServiceException {
        Mockito.when(studentsDao.removeFromCourse(anyInt(), anyInt())).thenReturn(false);
        assertEquals(false, serviceSchool.removeStudentFromCourse(new Student().id(1), new Course().id(1)));
        Mockito.verify(studentsDao).removeFromCourse(anyInt(), anyInt());
    }


}
