package com.ua.foxminded;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.SchService;
import com.ua.foxminded.controller.service.SchoolService;
import com.ua.foxminded.controller.service.reader.ConsoleReader;
import com.ua.foxminded.controller.service.testdata.GeneratorData;
import com.ua.foxminded.controller.service.testdata.GeneratorDataException;
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

        DBDeployment dbDeployment = new DBDeployment();
        GeneratorData testData = new GeneratorData();
        GroupsDao groupsDao = new GroupsDao();
        CoursesDao coursesDao = new CoursesDao();
        StudentsDao studentsDao = new StudentsDao();

        SchService schoolService = new SchoolService(studentsDao, coursesDao, groupsDao);
        FormatterToView toView = new FormatterToView();
        ConsoleReader consoleReader = new ConsoleReader(new BufferedReader(new InputStreamReader(System.in)));
        Menu menu = new Menu(schoolService, toView, consoleReader);

        try {
            coursesDao.getById(100);
        } catch (DAOException e) {
            e.printStackTrace();
        }


//        try {
//            dbDeployment.deploy();
//            testData.generateAndStore(studentsDao, coursesDao, groupsDao);
//        } catch (SqlExecuteException | GeneratorDataException e) {
//            log.error(e.getMessage());
//        } catch (RuntimeException e) {
//            log.error("System error: Generate test data. " + e);
//        }
//
//        try {
//            menu.showMenu();
//        } catch (RuntimeException e) {
//            log.error("System error: " + e);
//        }
    }
}
