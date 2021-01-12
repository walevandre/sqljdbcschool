package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.domain.Course;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoursesDao implements DAO<Course> {

    private static final String TABLE_COURSES = "courses";

    @Override
    public Optional<Course> getById(int id) throws DAOException {

        Optional<Course> optCourses = null;
        String sql = "SELECT " +
                "courses.name AS course_name, " +
                "courses.description AS description, " +
                "students.id AS student_id, " +
                "students.first_name AS students_first_name, " +
                "students.last_name AS students_last_name, " +
                "groups.id AS group_id, " +
                "groups.name AS group_name " +
                "FROM " +
                "courses " +
                "LEFT JOIN students_courses ON courses.id = students_courses.course_id " +
                "LEFT JOIN students ON students.id = students_courses.student_id " +
                "LEFT JOIN groups ON students.group_id = groups.id " +
                "WHERE courses.id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, id);
            ResultSet resultSet = pStatement.executeQuery();
            Course course = null;
            List<Student> students = new ArrayList<>();
            if (resultSet.next()) {
                course = new Course()
                        .id(id)
                        .name(resultSet.getString("course_name"))
                        .description(resultSet.getString("description"));
            }
            do {
                if (resultSet.getString("students_first_name") != null) {
                    Student student = new Student().id(resultSet.getInt("student_id"))
                            .firstName(resultSet.getString("students_first_name"))
                            .lastName(resultSet.getString("students_last_name"));
                    if (resultSet.getString("group_name") != null) {
                        student.group(new Group()
                                .id(resultSet.getInt("group_id"))
                                .name(resultSet.getString("group_name")));
                    }
                    students.add(student);
                }

            } while (resultSet.next());
            course.students(students);
            optCourses = Optional.ofNullable(course);
        } catch (SQLException e) {
            throw new DAOException("Cannot get course by id", e);
        }
        return optCourses;
    }

    @Override
    public List<Course> getAll() throws DAOException {
        List<Course> courses;
        String sql = "SELECT id, name, description FROM " + TABLE_COURSES;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            courses = new ArrayList<>();
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                courses.add(new Course().id(id).name(name).description(description));
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot get all courses", e);
        }
        return courses;
    }

    @Override
    public Course save(Course course) throws DAOException {

        String sql = "INSERT INTO " + TABLE_COURSES + " (name, description) VALUES(?,?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pStatement.setString(1, course.name());
            pStatement.setString(2, course.description());
            pStatement.executeUpdate();

            ResultSet resultSet = pStatement.getGeneratedKeys();
            if (resultSet.next()) {
                course.id(resultSet.getInt("id"));
            } else {
                throw new DAOException("Creating course failed, no ID obtained", new SQLException());
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot insert into course", e);
        }
        return course;
    }

    @Override
    public void saveList(List<Course> courses) throws DAOException {

        String sql = "INSERT INTO " + TABLE_COURSES + " (name, description) VALUES (?,?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            courses.forEach(value -> {
                try {
                    pStatement.setString(1, value.name());
                    pStatement.setString(2, value.description());
                    pStatement.addBatch();
                } catch (SQLException e) {
                }
            });
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Cannot save list of courses", e);
        }
    }

    @Override
    public Course update(Course course) throws DAOException {

        String sql = "UPDATE " + TABLE_COURSES + " SET name=?, description=? WHERE id=?";


        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, course.name());
            pStatement.setString(2, course.description());
            pStatement.setInt(3, course.id());
            pStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Cannot update courses by id=" + course.id(), e);
        }
        return course;
    }

    @Override
    public boolean delete(Course course) throws DAOException {
        boolean ifExecute = true;
        String sql = "DELETE FROM " + TABLE_COURSES + " WHERE id=?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, course.id());
            if (pStatement.executeUpdate() == 0) {
                ifExecute = false;
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot delete course " + course.toString(), e);
        }
        return ifExecute;
    }

    public List<Student> findStudentsOnCourse(Course course) throws DAOException {
        List<Student> students;
        String sql = "SELECT " +
                "students.id as id, " +
                "students.group_id as group_id, " +
                "students.first_name as first_name, " +
                "students.last_name as last_name " +
                "FROM " +
                "students " +
                "INNER JOIN students_courses ON students.id = students_courses.student_id " +
                "INNER JOIN courses ON students_courses.course_id = courses.id " +
                "WHERE " +
                "courses.id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            students = new ArrayList<>();
            pStatement.setInt(1, course.id());
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                students.add(new Student()
                        .id(resultSet.getInt("id"))
                        .groupId(resultSet.getInt("group_id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name")));
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot find students related to course", e);
        }
        return students;
    }
}
