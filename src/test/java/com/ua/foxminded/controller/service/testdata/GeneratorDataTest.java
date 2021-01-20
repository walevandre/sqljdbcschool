package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.DBService.SqlExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class GeneratorDataTest {

    private StudentsGenerator studentsGenerator = Mockito.mock(StudentsGenerator.class);
    private CoursesGenerator coursesGenerator = Mockito.mock(CoursesGenerator.class);
    private GroupsGenerator groupsGenerator = Mockito.mock(GroupsGenerator.class);
    private GeneratorData generatorData = new GeneratorData(studentsGenerator, coursesGenerator, groupsGenerator);

    @AfterEach
    void verifyNoMoreInteractionsAfterEachTest() {
        Mockito.verifyNoMoreInteractions(studentsGenerator);
        Mockito.verifyNoMoreInteractions(coursesGenerator);
        Mockito.verifyNoMoreInteractions(groupsGenerator);
    }
    @Test
    void generateAndStore() throws GeneratorDataException, SqlExecuteException {

        GroupsDao groupsDao = new GroupsDao();
        CoursesDao coursesDao = new CoursesDao();
        StudentsDao studentsDao = new StudentsDao();

        DBDeployment dbDeployment = new DBDeployment(new SqlExecutor());
        dbDeployment.deploy();
        generatorData.generateAndStore(studentsDao, coursesDao, groupsDao);

        Mockito.verify(studentsGenerator).assignStudentsToGroups(any(), any());
        Mockito.verify(studentsGenerator).generateRandomly(any());
        Mockito.verify(studentsGenerator).assignCourseForStudentRandomly(any(), any());
        Mockito.verify(coursesGenerator).generateFromFile(anyString(), any());
        Mockito.verify(groupsGenerator).generateRandomly(any());
    }
}