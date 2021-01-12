package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.RandomlyGenerator;
import com.ua.foxminded.controller.service.reader.FileReader;
import com.ua.foxminded.controller.service.reader.ReaderException;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StudentsGenerator implements Generator<StudentsDao> {

    private static final Logger log = LoggerFactory.getLogger(StudentsGenerator.class);

    private static final String FIRST_NAME_TEST_DATA = "testData/firstName.txt";
    private static final String LAST_NAME_TEST_DATA = "testData/lastName.txt";

    private static final int MIN_GROUP_SIZE = 10;
    private static final int MAX_GROUP_SIZE = 30;

    private static final int INDEX_COURSE_ID = 0;
    private static final int INDEX_STUDENT_ID = 1;

    private static final int MIN_COUNT_COURSE = 1;
    private static final int MAX_COUNT_COURSE = 3;

    private static final int INDEX_FIRST_NAME = 0;
    private static final int INDEX_LAST_NAME = 1;
    private static final int COUNT_NAME_PARAM = 2;

    private static final int COUNT_STUDENT_FOR_TEST = 200;

    private static final int FIRST_INDEX_LIST = 0;
    private static final int ONE_ITEM_LIST = 1;


    @Override
    public void generateFromFile(String fileName, StudentsDao studentsDao) throws GeneratorDataException {
        log.warn("Unsupported operation");
        throw new GeneratorDataException("Unsupported operation");
    }

    @Override
    public void generateRandomly(StudentsDao studentsDao) throws GeneratorDataException {

        FileReader fileReader = new FileReader();
        try {
            List<String> firstNameList = fileReader.getFileFromResourceAsList(FIRST_NAME_TEST_DATA);
            List<String> lastNameList = fileReader.getFileFromResourceAsList(LAST_NAME_TEST_DATA);
            if ((!firstNameList.isEmpty() && firstNameList != null) && (!lastNameList.isEmpty() && lastNameList != null)) {

                Set<Student> nameSet = new HashSet<>();
                String[] arrayNames = new String[COUNT_NAME_PARAM];
                while (nameSet.size() < COUNT_STUDENT_FOR_TEST) {
                    int indexFirstName = RandomlyGenerator.getNumberFromRange(0, firstNameList.size());
                    int indexLastName = RandomlyGenerator.getNumberFromRange(0, lastNameList.size());
                    arrayNames[INDEX_FIRST_NAME] = firstNameList.get(indexFirstName);
                    arrayNames[INDEX_LAST_NAME] = lastNameList.get(indexLastName);
                    nameSet.add(new Student().firstName(firstNameList.get(indexFirstName)).lastName(lastNameList.get(indexLastName)));
                }
                List<Student> students = new ArrayList<>(nameSet);
                studentsDao.saveList(students);
            } else {
                log.error("Error while generate randomly names. The list of first name ot last name is empty");
                throw new GeneratorDataException("Error while generate randomly names. The list of first name ot last name is empty");
            }
        } catch (ReaderException | DAOException e) {
            log.error("Failed to generate students.", e);
            throw new GeneratorDataException("Failed to generate students.", e);
        }
    }

    public void assignCourseForStudentRandomly(StudentsDao studentsDao, CoursesDao coursesDao) throws GeneratorDataException {

        try {
            List<Course> courses = coursesDao.getAll();
            List<Student> students = studentsDao.getAll();
            if (!courses.isEmpty() && !students.isEmpty()) {
                students.forEach(value -> assignCourse(value.id(), courses, studentsDao));
            } else {
                log.error("Failed to assign course for student. Empty course list or student list");
                throw new GeneratorDataException("Failed to assign course for student. Empty course list or student list");
            }
        } catch (DAOException e) {
            log.error("Failed to assign course for student.", e);
            throw new GeneratorDataException("Failed to assign course for student.", e);
        }
    }

    private void assignCourse(int studentId, List<Course> courses, StudentsDao studentsDao) {

        Set<List<Integer>> relations = new HashSet<>();
        int countCourses = RandomlyGenerator.getNumberFromRange(MIN_COUNT_COURSE, MAX_COUNT_COURSE);
        while (relations.size() <= countCourses) {
            int courseId = courses.get(RandomlyGenerator.getNumberFromRange(0, courses.size())).id();
            List<Integer> listId = new LinkedList<>();
            listId.add(courseId);
            listId.add(studentId);
            relations.add(listId);
        }
        relations.forEach(value -> {
            try {
                studentsDao.assignToCourse(value.get(INDEX_COURSE_ID), value.get(INDEX_STUDENT_ID));
            } catch (DAOException e) {
                log.error("Failed to assign course for student.", e);
            }
        });
    }

    public void assignStudentsToGroups(StudentsDao studentsDao, GroupsDao groupsDao) throws GeneratorDataException {
        try {
            List<Student> students = studentsDao.getAll();
            List<Group> groups = groupsDao.getAll();
            groups.forEach(value -> {
                if (!students.isEmpty()) {
                    List<Student> studentsInGroup = new ArrayList<>();
                    int countStudents = RandomlyGenerator.getNumberFromRange(MIN_GROUP_SIZE, MAX_GROUP_SIZE);
                    for (int i = 0; i < countStudents; i++) {
                        int randomStudent;
                        if (students.size() == ONE_ITEM_LIST) {
                            randomStudent = FIRST_INDEX_LIST;
                        } else {
                            randomStudent = RandomlyGenerator.getNumberFromRange(0, students.size());

                        }
                        studentsInGroup.add(students.get(randomStudent).groupId(value.id()));
                        students.remove(randomStudent);
                        if (students.isEmpty()) {
                            break;
                        }
                    }
                    studentsInGroup.forEach(v -> {
                        try {
                            studentsDao.update(v);
                        } catch (DAOException e) {
                            log.error("Failed to assign student to group.", e);
                        }
                    });
                }
            });
        } catch (DAOException e) {
            log.error("Failed to assign student to group.", e);
            throw new GeneratorDataException("Failed to assign student to group.", e);
        }
    }
}
