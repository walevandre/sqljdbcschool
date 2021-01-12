package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;

public class GeneratorData {

    private static final String COURSES_TEST_DATA = "testData/courses.txt";

    public void generateAndStore(StudentsDao studentsDao, CoursesDao coursesDao, GroupsDao groupsDao) throws GeneratorDataException {

        Generator coursesGenerator = new CoursesGenerator();
        Generator groupsGenerator = new GroupsGenerator();
        StudentsGenerator studentsGenerator = new StudentsGenerator();

            coursesGenerator.generateFromFile(COURSES_TEST_DATA, coursesDao);
            groupsGenerator.generateRandomly(groupsDao);
            studentsGenerator.generateRandomly(studentsDao);
            studentsGenerator.assignCourseForStudentRandomly(studentsDao, coursesDao);
            studentsGenerator.assignStudentsToGroups(studentsDao, groupsDao);
    }
}
