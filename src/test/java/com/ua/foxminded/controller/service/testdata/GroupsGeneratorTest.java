package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.testdata.GeneratorDataException;
import com.ua.foxminded.controller.service.testdata.GroupsGenerator;
import com.ua.foxminded.domain.Group;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupsGeneratorTest {

    private GroupsDao groupsDao = Mockito.mock(GroupsDao.class);
    private GroupsGenerator groupsGenerator = new GroupsGenerator();

    @Test
    void generateRandomlyTest() throws GeneratorDataException, DAOException {

        groupsGenerator.generateRandomly(groupsDao);

        ArgumentCaptor<List<Group>> groupsCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(groupsDao).saveList(groupsCaptor.capture());
        List<Group> value = groupsCaptor.getValue();

        assertThat(value, hasSize(10));
        assertEquals(Group.class, value.get(0).getClass());
    }


}
