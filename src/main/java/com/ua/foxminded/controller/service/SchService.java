package com.ua.foxminded.controller.service;

import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;

import java.util.List;

public interface SchService {

    List<Student> getAllStudents() throws SchoolServiceException;

    List<Course> getAllCourses() throws SchoolServiceException;

    List<Course> getCourseByStudent(Student student) throws SchoolServiceException;

    List<Group> findGroupsByParams(int studentsCount) throws SchoolServiceException;

    List<Student> findStudentsByParams(Course course) throws SchoolServiceException;

    Student addNewStudent(Student student) throws SchoolServiceException;

    boolean deleteStudent(Student student) throws SchoolServiceException;

    boolean addStudentToCourse(Student student, int courseId) throws SchoolServiceException;

    boolean removeStudentFromCourse(Student student, Course courses) throws SchoolServiceException;

}
