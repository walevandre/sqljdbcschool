package com.ua.foxminded.controller.service;

import com.ua.foxminded.controller.dao.CoursesDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.StudentsDao;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;

import java.util.List;

public class SchoolServiceImpl implements SchoolService {

    private StudentsDao studentsDao;
    private CoursesDao coursesDao;
    private GroupsDao groupsDao;

    public SchoolServiceImpl(StudentsDao studentsDao, CoursesDao coursesDao, GroupsDao groupsDao) {
        this.studentsDao = studentsDao;
        this.coursesDao = coursesDao;
        this.groupsDao = groupsDao;
    }

    private static final String ACTIONS_EXCEPTION_MESSAGE = "Error by action: ";

    @Override
    public List<Student> getAllStudents() throws SchoolServiceException {
        try {
            return studentsDao.getAll();
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> getAllCourses() throws SchoolServiceException {
        try {
            return coursesDao.getAll();
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> getCourseByStudent(Student student) throws SchoolServiceException {
        try {
            return studentsDao.findCoursesByStudent(student).courses();
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public List<Group> findGroupsByParams(int studentsCount) throws SchoolServiceException {
        List<Group> groups;
        try {
            groups = groupsDao.findAllWithLessOrEqualsStudentCount(studentsCount);
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
        return groups;
    }

    @Override
    public List<Student> findStudentsByParams(Course course) throws SchoolServiceException {
        List<Student> students;
        try {
            students = coursesDao.findStudentsOnCourse(course);
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
        return students;
    }

    @Override
    public Student addNewStudent(Student student) throws SchoolServiceException {
        Student newStudent;
        try {
            newStudent = studentsDao.save(student);
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
        return newStudent;
    }

    @Override
    public boolean deleteStudent(Student student) throws SchoolServiceException {
        try {
            if (studentsDao.delete(student)) {
                return true;
            } else {
                return false;
            }
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public boolean addStudentToCourse(Student student, int courseId) throws SchoolServiceException {
        try {
            if (studentsDao.assignToCourse(courseId, student.id())) {
                return true;
            } else {
                return false;
            }
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public boolean removeStudentFromCourse(Student student, Course courses) throws SchoolServiceException {
        try {
            if (studentsDao.removeFromCourse(courses.id(), student.id())) {
                return true;
            } else {
                return false;
            }
        } catch (DAOException e) {
            throw new SchoolServiceException(ACTIONS_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }
}
