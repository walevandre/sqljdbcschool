package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.DBService.DBDeployment;
import com.ua.foxminded.controller.service.DBService.SqlExecuteException;
import com.ua.foxminded.controller.service.testdata.GeneratorData;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupsDaoTest {

    private static GroupsDao groupsDao = new GroupsDao();

    @BeforeEach
    void setup() {
        DBDeployment dbDeployment = new DBDeployment();
        try {
            dbDeployment.deploy();
        } catch (SqlExecuteException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getGroupByIDTest() throws DAOException {
        List<Student> students = new ArrayList<>();
        students.add(new Student().id(9).firstName("Jakub").lastName("George").groupId(3));
        students.add(new Student().id(10).firstName("Orion").lastName("Smith").groupId(3));
        Group expected = new Group().id(3).name("SS-20").students(students);
        Group actual = groupsDao.getById(3).get();

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertThat(actual.students(), hasSize(2));
        assertEquals("Jakub", actual.students().get(0).firstName());
        assertEquals("Orion", actual.students().get(1).firstName());
    }

    @Test
    void saveGroupTest() throws DAOException {
        Group expected = new Group().name("WW-30").students(new ArrayList<>());
        int insertId = groupsDao.save(expected).id();
        expected.id(insertId);
        Group actual = groupsDao.getById(insertId).get();

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
    }

    @Test
    void updateGroupTest() throws DAOException {
        Group expected = groupsDao.getById(4).get().name("RR-40");
        groupsDao.update(expected);
        Group actual = groupsDao.getById(4).get();

        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
    }

    @Test
    void deleteGroupTest() throws DAOException {
        int countBeforeDelete = groupsDao.getAll().size();
        groupsDao.delete(new Group().id(5));
        int countAfterDelete = groupsDao.getAll().size();

        assertEquals(countAfterDelete, countBeforeDelete - 1);
    }

    @Test
    void findAllWithLessOrEqualsStudentCountTest() throws DAOException {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().id(1).name("XX-10"));
        expected.add(new Group().id(3).name("SS-20"));
        expected.add(new Group().id(4).name("DD-20"));

        List<Group> actual = groupsDao.findAllWithLessOrEqualsStudentCount(3);

        assertThat(actual, hasSize(3));
        assertThat(actual, not(IsEmptyCollection.empty()));

        assertEquals(expected.get(1).id(), actual.get(1).id());
        assertEquals(expected.get(1).name(), actual.get(1).name());
    }
}
