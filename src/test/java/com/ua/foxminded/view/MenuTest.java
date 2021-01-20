package com.ua.foxminded.view;

import com.ua.foxminded.controller.service.SchoolService;
import com.ua.foxminded.controller.service.SchoolServiceImpl;
import com.ua.foxminded.controller.service.SchoolServiceException;
import com.ua.foxminded.controller.service.reader.ConsoleReader;
import com.ua.foxminded.controller.service.reader.ReaderException;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

class MenuTest {

    private SchoolService schService = Mockito.mock(SchoolServiceImpl.class);
    private FormatterToView toView = Mockito.mock(FormatterToView.class);
    private ConsoleReader consoleReader = Mockito.mock(ConsoleReader.class);


    Menu menu = new Menu(schService, toView, consoleReader);

    @AfterEach
    void verifyNoMoreInteractionsAfterEachTest() {
        Mockito.verifyNoMoreInteractions(schService);
        Mockito.verifyNoMoreInteractions(toView);
    }

//    @Test
//    void showMenuTest() throws ReaderException {
//
//        Mockito.when(consoleReader.read()).thenReturn(1);
//        menu.showMenu();
//        Mockito.verify(consoleReader).read();
//    }

    @Test
    void doActionWhenInputOne() throws MenuException, SchoolServiceException {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group().id(1).name("XX-10"));
        groups.add(new Group().id(3).name("SS-20"));


        Mockito.when(schService.findGroupsByParams(anyInt())).thenReturn(groups);

        menu.doAction(1);

        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(schService).findGroupsByParams(anyInt());
        Mockito.verify(toView).listToView(listCaptor.capture());
        List<String> value = listCaptor.getValue();

        assertThat(value, hasSize(3));

        assertEquals("Groups with less or equals 20 students", value.get(0));
        assertEquals("XX-10", value.get(1));
        assertEquals("SS-20", value.get(2));

    }

    @Test
    void doActionWhenInputTwo() throws MenuException, SchoolServiceException {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course().id(1).name("Algebra"));
        List<Student> students = new ArrayList<>();
        students.add(new Student().id(9).firstName("Jakub").lastName("George").courses(courses));
        students.add(new Student().id(10).firstName("Orion").lastName("Smith").courses(courses));

        Mockito.when(schService.getAllCourses()).thenReturn(courses);
        Mockito.when(schService.findStudentsByParams(any())).thenReturn(students);

        menu.doAction(2);

        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(schService).getAllCourses();
        Mockito.verify(schService).findStudentsByParams(any());
        Mockito.verify(toView).listToView(listCaptor.capture());
        List<String> value = listCaptor.getValue();

        assertThat(value, hasSize(3));

        assertEquals("Students on the Algebra course:", value.get(0));
        assertEquals("Jakub George", value.get(1));
        assertEquals("Orion Smith", value.get(2));
    }

    @Test
    void doActionWhenInputThree() throws SchoolServiceException, MenuException {
        Student student = new Student().id(1).firstName("John").lastName("Darwin");

        Mockito.when(schService.addNewStudent(any())).thenReturn(student);

        menu.doAction(3);

        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(schService).addNewStudent(any());
        Mockito.verify(toView).listToView(listCaptor.capture());
        List<String> value = listCaptor.getValue();

        assertThat(value, hasSize(2));

        assertEquals("Add new student:", value.get(0));
        assertEquals("John Darwin", value.get(1));
    }

    @Test
    void doActionWhenInputFour() throws SchoolServiceException, MenuException {
        List<Student> students = new ArrayList<>();
        students.add(new Student().id(9).firstName("Jakub").lastName("George"));

        Mockito.when(schService.deleteStudent(any())).thenReturn(true);
        Mockito.when(schService.getAllStudents()).thenReturn(students);

        menu.doAction(4);

        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(schService).getAllStudents();
        Mockito.verify(schService).deleteStudent(any());
        Mockito.verify(toView).listToView(listCaptor.capture());
        List<String> value = listCaptor.getValue();

        assertThat(value, hasSize(1));

        assertEquals("The student - Jakub George has been deleted", value.get(0));
    }

    @Test
    void doActionWhenInputFive() throws SchoolServiceException, ReaderException, MenuException {
        List<Student> students = new ArrayList<>();
        students.add(new Student().id(9).firstName("Jakub").lastName("George"));

        List<Course> courses = new ArrayList<>();
        courses.add(new Course().id(1).name("Algebra"));

        Mockito.when(schService.getAllCourses()).thenReturn(courses);
        Mockito.when(schService.getAllStudents()).thenReturn(students);
        Mockito.when(schService.addStudentToCourse(any(), anyInt())).thenReturn(true);
        Mockito.when(consoleReader.read()).thenReturn(1);

        menu.doAction(5);

        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(schService).getAllStudents();
        Mockito.verify(schService).getAllCourses();
        Mockito.verify(schService).addStudentToCourse(any(), anyInt());
        Mockito.verify(toView, Mockito.times(courses.size() + 1)).stringToView(anyString());
        Mockito.verify(toView).listToView(listCaptor.capture());
        List<String> value = listCaptor.getValue();

        assertThat(value, hasSize(2));

        assertEquals("The student - Jakub George will be added to course - Algebra", value.get(0));
    }

    @Test
    void doActionWhenInputSix() throws SchoolServiceException, MenuException {
        List<Student> students = new ArrayList<>();
        students.add(new Student().id(9).firstName("Jakub").lastName("George"));

        List<Course> courses = new ArrayList<>();
        courses.add(new Course().id(1).name("Algebra"));

        Mockito.when(schService.getCourseByStudent(any())).thenReturn(courses);
        Mockito.when(schService.getAllStudents()).thenReturn(students);
        Mockito.when(schService.removeStudentFromCourse(any(), any())).thenReturn(true);

        menu.doAction(6);

        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(schService).getAllStudents();
        Mockito.verify(schService).getCourseByStudent(any());
        Mockito.verify(schService).removeStudentFromCourse(any(), any());
        Mockito.verify(toView).listToView(listCaptor.capture());
        List<String> value = listCaptor.getValue();

        assertThat(value, hasSize(2));

        assertEquals("The student - Jakub George will be removed from course - Algebra", value.get(0));
    }

    @Test
    void doActionWhenInputAny() throws SchoolServiceException, MenuException {
        menu.doAction(Mockito.intThat(arg -> arg != null || arg > 6));

        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(toView).listToView(listCaptor.capture());
        List<String> value = listCaptor.getValue();

        assertThat(value, hasSize(1));

        assertEquals("No actions", value.get(0));
    }
}
