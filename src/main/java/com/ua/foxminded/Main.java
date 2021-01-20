package com.ua.foxminded;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.DBService.SqlExecutor;
import com.ua.foxminded.controller.service.SchoolService;
import com.ua.foxminded.controller.service.SchoolServiceImpl;
import com.ua.foxminded.controller.service.reader.ConsoleReader;
import com.ua.foxminded.controller.service.testdata.*;
import com.ua.foxminded.view.FormatterToView;
import com.ua.foxminded.view.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        log.info("Start application");

        GroupsDao groupsDao = new GroupsDao();
        CoursesDao coursesDao = new CoursesDao();
        StudentsDao studentsDao = new StudentsDao();
        DBDeployment dbDeployment = new DBDeployment(new SqlExecutor());
        CoursesGenerator coursesGenerator = new CoursesGenerator();
        GroupsGenerator groupsGenerator = new GroupsGenerator();
        StudentsGenerator studentsGenerator = new StudentsGenerator();
        GeneratorData testData = new GeneratorData(studentsGenerator, coursesGenerator, groupsGenerator);

        SchoolService schoolService = new SchoolServiceImpl(studentsDao, coursesDao, groupsDao);
        FormatterToView toView = new FormatterToView();
        ConsoleReader consoleReader = new ConsoleReader(new BufferedReader(new InputStreamReader(System.in)));
        Menu menu = new Menu(schoolService, toView, consoleReader);

        try {
            dbDeployment.deploy();
            testData.generateAndStore(studentsDao, coursesDao, groupsDao);
        } catch (SqlExecuteException | GeneratorDataException e) {
            log.error(e.getMessage());
        } catch (RuntimeException e) {
            log.error("System error: Generate test data. " + e);
        }

        try {
            menu.showMenu();
        } catch (RuntimeException e) {
            log.error("System error: " + e);
        }
    }
}
