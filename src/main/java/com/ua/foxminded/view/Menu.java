package com.ua.foxminded.view;

import com.ua.foxminded.controller.service.RandomlyGenerator;
import com.ua.foxminded.controller.service.SchService;
import com.ua.foxminded.controller.service.SchoolServiceException;
import com.ua.foxminded.controller.service.reader.ConsoleReader;
import com.ua.foxminded.controller.service.reader.ReaderException;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final Logger log = LoggerFactory.getLogger(Menu.class);

    private static final String EMPTY_LINE = "";
    private static final int GROUP_SIZE = 20;

    private SchService schoolService;
    private FormatterToView toView;
    private ConsoleReader consoleReader;


    public Menu(SchService schoolService, FormatterToView toView, ConsoleReader consoleReader) {
        this.schoolService = schoolService;
        this.toView = toView;
        this.consoleReader = consoleReader;
    }

    public void showMenu() {
        while (true) {
            printMenu();
            try {
                int actionNumber = consoleReader.read();
                if (actionNumber == 0) {
                    log.info("Stop application");
                    break;
                } else {
                    doAction(actionNumber);
                }
            } catch (ReaderException e) {
                toView.stringToView(e.getMessage());
            } catch (MenuException e) {
                toView.stringToView(e.getMessage());
            }
        }
    }

    public void doAction(int actionNumber) throws MenuException {

        List<String> result = new ArrayList<>();
        try {
            switch (actionNumber) {
                case 1:
                    result.add("Groups with less or equals 20 students");
                    schoolService.findGroupsByParams(GROUP_SIZE).forEach(value -> result.add(value.name()));
                    toView.listToView(result);
                    break;
                case 2:
                    List<Course> courses = schoolService.getAllCourses();
                    Course randCourse = courses.get(RandomlyGenerator.getNumberFromRange(0, courses.size()));
                    result.add("Students on the " + randCourse.name() + " course:");
                    schoolService.findStudentsByParams(randCourse).forEach(value -> result.add(value.getFullName()));
                    toView.listToView(result);
                    break;
                case 3:
                    result.add("Add new student:");
                    result.add(schoolService.addNewStudent(new Student().firstName("John").lastName("Darwin")).getFullName());
                    toView.listToView(result);
                    break;
                case 4:
                    List<Student> students = schoolService.getAllStudents();
                    Student randStudent = students.get(RandomlyGenerator.getNumberFromRange(0, students.size()));
                    if (schoolService.deleteStudent(randStudent)) {
                        result.add("The student - " + randStudent.getFullName() + " has been deleted");
                    }
                    toView.listToView(result);
                    break;
                case 5:
                    students = schoolService.getAllStudents();
                    randStudent = students.get(RandomlyGenerator.getNumberFromRange(0, students.size()));
                    toView.stringToView("Choice number of course from the list:");
                    courses = schoolService.getAllCourses();
                    courses.forEach(course -> toView.stringToView(course.id() + ". " + course.name()));
                    int courseIndex = 0;
                    try {
                        courseIndex = consoleReader.read();
                    } catch (ReaderException e) {
                        throw new MenuException(e);
                    }
                    if (courseIndex > 0 && courseIndex <= courses.size()) {
                        result.add("The student - " + randStudent.getFullName() + " will be added to course - " + courses.get(courseIndex - 1).name());
                        if (schoolService.addStudentToCourse(randStudent, courseIndex)) {
                            result.add("Successfully");
                        } else {
                            result.add("Not successfully");
                        }
                    } else {
                        throw new MenuException("Course " + courseIndex + " not found");
                    }
                    toView.listToView(result);
                    break;
                case 6:
                    students = schoolService.getAllStudents();
                    randStudent = students.get(RandomlyGenerator.getNumberFromRange(0, students.size()));
                    List<Course> coursesByStudent = schoolService.getCourseByStudent(randStudent);
                    randCourse = coursesByStudent.get(RandomlyGenerator.getNumberFromRange(0, coursesByStudent.size()));

                    String studentName = randStudent.getFullName();
                    String courseName = randCourse.name();
                    result.add("The student - " + studentName + " will be removed from course - " + courseName);
                    if (schoolService.removeStudentFromCourse(randStudent, randCourse)) {
                        result.add("Successfully");
                    } else {
                        result.add("Not successfully");
                    }
                    toView.listToView(result);
                    break;
                default:
                    result.add("No actions");
                    toView.listToView(result);
                    break;
            }
        } catch (SchoolServiceException e) {
            throw new MenuException(e);
        }
    }

    private void printMenu() {
        toView.stringToView(EMPTY_LINE);
        toView.stringToView("---------------------------------------------------------");
        toView.stringToView("1. Find all groups with less or equals student count 20");
        toView.stringToView("2. Find all students related to course with given name");
        toView.stringToView("3. Add new student John Darwin");
        toView.stringToView("4. Delete student by STUDENT_ID");
        toView.stringToView("5. Add a student to the course (from a list)");
        toView.stringToView("6. Remove the student from one of his or her courses");
        toView.stringToView("0. Exit");
        toView.stringToView(EMPTY_LINE);
        toView.stringToView("Choose action number");
        toView.stringToView("---------------------------------------------------------");
        toView.stringToView(EMPTY_LINE);
    }
}
