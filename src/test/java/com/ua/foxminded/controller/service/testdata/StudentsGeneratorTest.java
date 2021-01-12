package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.service.testdata.GeneratorDataException;
import com.ua.foxminded.controller.service.testdata.StudentsGenerator;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

class StudentsGeneratorTest {

    private StudentsDao studentsDao = Mockito.mock(StudentsDao.class);
    private CoursesDao coursesDao = Mockito.mock(CoursesDao.class);
    private GroupsDao groupsDao = Mockito.mock(GroupsDao.class);

    private StudentsGenerator studentsGenerator = new StudentsGenerator();

    @Test
    void generateRandomlyTest() throws GeneratorDataException, DAOException {

        studentsGenerator.generateRandomly(studentsDao);

        ArgumentCaptor<List<Student>> studentsCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(studentsDao).saveList(studentsCaptor.capture());
        List<Student> value = studentsCaptor.getValue();

        assertThat(value, hasSize(200));
        assertEquals(Student.class, value.get(0).getClass());
    }

    @Test
    void assignCourseForStudentRandomlyTest() throws DAOException, GeneratorDataException {

        List<Student> students = new ArrayList<>();
        students.add(new Student().id(1).firstName("John").lastName("Williams").groupId(1));
        students.add(new Student().id(2).firstName("Natan").lastName("White").groupId(1));
        students.add(new Student().id(3).firstName("Ethan").lastName("Hamilton").groupId(1));
        students.add(new Student().id(4).firstName("Jakub").lastName("Bailey").groupId(2));

        List<Course> courses = new ArrayList<>();
        courses.add(new Course().id(1).name("Algebra"));
        courses.add(new Course().id(2).name("Biology"));
        courses.add(new Course().id(3).name("Physics"));
        courses.add(new Course().id(4).name("Science"));

        Mockito.when(studentsDao.getAll()).thenReturn(students);
        Mockito.when(coursesDao.getAll()).thenReturn(courses);

        studentsGenerator.assignCourseForStudentRandomly(studentsDao, coursesDao);
        Mockito.verify(studentsDao, Mockito.atLeast(8)).assignToCourse(anyInt(), anyInt());
    }

    @Test
    void assignStudentsToGroupsTest() throws GeneratorDataException, DAOException {

        List<Student> students = new ArrayList<>();
        students.add(new Student().id(1).firstName("John").lastName("Williams").groupId(1));
        students.add(new Student().id(2).firstName("Natan").lastName("White").groupId(1));
        students.add(new Student().id(3).firstName("Ethan").lastName("Hamilton").groupId(1));
        students.add(new Student().id(4).firstName("Jakub").lastName("Bailey").groupId(2));

        List<Group> groups = new ArrayList<>();
        groups.add(new Group().id(1).name("XX-10"));
        groups.add(new Group().id(3).name("SS-20"));
        groups.add(new Group().id(4).name("DD-20"));

        Mockito.when(studentsDao.getAll()).thenReturn(students);
        Mockito.when(groupsDao.getAll()).thenReturn(groups);

        studentsGenerator.assignStudentsToGroups(studentsDao, groupsDao);

        Mockito.verify(studentsDao, Mockito.times(4)).update(Mockito.any());
    }
}
