package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentsDao implements DAO<Student> {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TABLE_STUDENTS = "students";

    @Override
    public Optional<Student> getById(int id) throws DAOException {

        String sql = "SELECT " +
                "students.first_name AS first_name, " +
                "students.last_name AS last_name, " +
                "courses.id AS course_id, " +
                "courses.name AS course_name, " +
                "courses.description AS course_description, " +
                "groups.id AS group_id, " +
                "groups.name AS group_name " +
                "FROM " +
                "students " +
                "LEFT JOIN students_courses ON students.id = students_courses.student_id " +
                "LEFT JOIN courses ON students_courses.course_id = courses.id " +
                "LEFT JOIN groups ON students.group_id = groups.id " +
                "WHERE students.id = ?";

        Optional<Student> optStudent = null;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, id);
            ResultSet resultSet = pStatement.executeQuery();

            Student student = null;
            List<Course> courses = new ArrayList<>();

            if (resultSet.next()) {
                student = new Student()
                        .id(id)
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"));
                if (resultSet.getString("group_id") != null) {
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("group_name");
                    student.group(new Group().id(groupId).name(groupName)).groupId(groupId);
                }
                do {
                    if (resultSet.getString("course_name") != null) {
                        Course course = new Course()
                                .id(resultSet.getInt("course_id"))
                                .name(resultSet.getString("course_name"))
                                .description(resultSet.getString("course_description"));
                        courses.add(course);
                    }
                } while (resultSet.next());
                student.courses(courses);
                optStudent = Optional.ofNullable(student);
            }
        } catch (Exception e) {
            throw new DAOException("Cannot get student by id", e);
        }
        return optStudent;
    }

    @Override
    public List<Student> getAll() throws DAOException {

        String sql = "SELECT id, group_id, first_name, last_name FROM " + TABLE_STUDENTS;

        List<Student> students;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = pStatement.executeQuery();
            students = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int groupId = resultSet.getInt("group_id");
                String firstName = resultSet.getString("first_name").trim();
                String lastName = resultSet.getString("last_name").trim();
                students.add(new Student().id(id).groupId(groupId).firstName(firstName).lastName(lastName));
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot get all students", e);
        }
        return students;
    }

    @Override
    public Student save(Student student) throws DAOException {

        String sql = "INSERT INTO " + TABLE_STUDENTS + " (group_id, first_name, last_name) VALUES (?,?,?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pStatement.setInt(1, student.groupId());
            pStatement.setString(2, student.firstName());
            pStatement.setString(3, student.lastName());
            pStatement.executeUpdate();

            ResultSet insertedId = pStatement.getGeneratedKeys();
            if (insertedId.next()) {
                student.id(insertedId.getInt("id"));
                log.info("Added student " + student.toString());
            } else {
                throw new DAOException("Creating student failed, no ID obtained.", new SQLException());
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot insert into student", e);
        }
        return student;
    }

    @Override
    public void saveList(List<Student> students) throws DAOException {
        String sql = "INSERT INTO " + TABLE_STUDENTS + " (group_id, first_name, last_name) VALUES (?,?,?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            students.forEach(value -> {
                try {
                    pStatement.setInt(1, value.groupId());
                    pStatement.setString(2, value.firstName());
                    pStatement.setString(3, value.lastName());
                    pStatement.addBatch();
                } catch (SQLException e) {
                    log.debug("Cannot create batch", e);
                }
            });
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Cannot save list of students", e);
        }
    }

    @Override
    public Student update(Student student) throws DAOException {

        String sql = "UPDATE " + TABLE_STUDENTS + " SET group_id=?, first_name=?, last_name=? WHERE id=?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pStatement.setInt(1, student.groupId());
            pStatement.setString(2, student.firstName());
            pStatement.setString(3, student.lastName());
            pStatement.setInt(4, student.id());
            pStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Cannot update student by id=" + student.id(), e);
        }
        return student;
    }

    @Override
    public boolean delete(Student student) throws DAOException {

        boolean ifExecute = true;
        String sql = "DELETE FROM " + TABLE_STUDENTS + " WHERE id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, student.id());
            if (pStatement.executeUpdate() == 0) {
                ifExecute = false;
            } else {
                log.info("Deleted student " + student.toString());
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot delete student " + student.id(), e);
        }
        return ifExecute;
    }

    public boolean assignToCourse(int courseId, int studentId) throws DAOException {

        String sql = "INSERT INTO students_courses (course_id, student_id) VALUES (?,?)";

        return actionByCourse(courseId, studentId, sql, "assign");
    }

    public boolean removeFromCourse(int courseId, int studentId) throws DAOException {

        String sql = "DELETE FROM students_courses WHERE course_id=? AND student_id=?";

        return actionByCourse(courseId, studentId, sql, "remove");
    }

    private boolean actionByCourse(int courseId, int studentId, String sql, String action) throws DAOException {
        boolean ifExecute = true;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, courseId);
            pStatement.setInt(2, studentId);
            if (pStatement.executeUpdate() == 0) {
                ifExecute = false;
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot " + action + " student id=" + studentId + " to course id=" + courseId, e);
        }
        return ifExecute;
    }

    public Student findCoursesByStudent(Student student) throws DAOException {
        List<Course> courses;
        String sql = "SELECT " +
                "courses.id as id, " +
                "courses.name as name, " +
                "courses.description as description " +
                "FROM " +
                "courses " +
                "INNER JOIN students_courses ON courses.id = students_courses.course_id " +
                "INNER JOIN students ON students_courses.student_id = students.id " +
                "WHERE " +
                "students.id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            courses = new ArrayList<>();
            pStatement.setInt(1, student.id());
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                courses.add(new Course()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description")));
            }
            student.courses(courses);
        } catch (SQLException e) {
            throw new DAOException("Cannot find courses related to student", e);
        }
        return student;
    }
}
