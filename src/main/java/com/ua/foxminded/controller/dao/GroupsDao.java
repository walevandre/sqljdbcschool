package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.domain.Group;
import com.ua.foxminded.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupsDao implements DAO<Group> {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TABLE_GROUPS = "groups";

    @Override
    public Optional<Group> getById(int id) throws DAOException {
        String sql = "SELECT " +
                "groups.name AS group_name, " +
                "students.id AS student_id, " +
                "students.first_name AS student_first_name, " +
                "students.last_name AS student_last_name " +
                "FROM " +
                "groups " +
                "LEFT JOIN students ON groups.id = students.group_id " +
                "WHERE groups.id = ?";
        Optional<Group> optGroups = null;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, id);
            ResultSet resultSet = pStatement.executeQuery();
            Group group = null;
            List<Student> students = new ArrayList<>();

            if (resultSet.next()) {
                group = new Group().id(id).name(resultSet.getString("group_name"));
                do {
                    if (resultSet.getString("student_first_name") != null) {
                        Student student = new Student()
                                .id(resultSet.getInt("student_id"))
                                .firstName(resultSet.getString("student_first_name"))
                                .lastName(resultSet.getString("student_last_name"))
                                .groupId(id);
                        students.add(student);
                    }
                } while (resultSet.next());
                group.students(students);
                optGroups = Optional.ofNullable(group);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot get group by id=" + id, e);
        }
        return optGroups;
    }

    @Override
    public List<Group> getAll() throws DAOException {
        String sql = "SELECT id, name FROM " + TABLE_GROUPS;
        List<Group> groups;

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            groups = new ArrayList<>();
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                groups.add(new Group().id(id).name(name));
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot get all groups", e);
        }
        return groups;
    }

    @Override
    public Group save(Group groups) throws DAOException {
        String sql = "INSERT INTO " + TABLE_GROUPS + " (name) VALUES (?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pStatement.setString(1, groups.name());
            pStatement.executeUpdate();

            ResultSet resultSet = pStatement.getGeneratedKeys();
            if (resultSet.next()) {
                groups.id(resultSet.getInt("id"));
            } else {
                throw new DAOException("Creating group failed, no ID obtained", new SQLException());
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot insert into groups", e);
        }
        return groups;
    }

    @Override
    public int[] saveList(List<Group> groups) throws DAOException {

        String sql = "INSERT INTO " + TABLE_GROUPS + " (name) VALUES (?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            groups.forEach(value -> {
                try {
                    pStatement.setString(1, value.name());
                    pStatement.addBatch();
                } catch (SQLException e) {
                    log.debug("Cannot create the batch");
                }
            });
            int[] insertedId = pStatement.executeBatch();
            if (insertedId.length == 0) {
                throw new DAOException("Nothing was inserted");
            }
            return insertedId;
        } catch (SQLException e) {
            throw new DAOException("Cannot save list of groups", e);
        }
    }

    @Override
    public Group update(Group group) throws DAOException {
        String sql = "UPDATE " + TABLE_GROUPS + " SET name=? WHERE id=?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, group.name());
            pStatement.setInt(2, group.id());
            if (pStatement.executeUpdate() == 0) {
                throw new DAOException("No update by id=" + group.id());
            }
            return group;
        } catch (SQLException e) {
            throw new DAOException("Cannot update groups by id=" + group.id(), e);
        }
    }

    @Override
    public boolean delete(Group group) throws DAOException {
        boolean ifExecute = true;
        String sql = "DELETE FROM " + TABLE_GROUPS + " WHERE id=?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, group.id());
            if (pStatement.executeUpdate() == 0) {
                ifExecute = false;
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot delete group by id=" + group.id(), e);
        }
        return ifExecute;
    }

    public List<Group> findAllWithLessOrEqualsStudentCount(int studentCount) throws DAOException {
        List<Group> groups;
        String sql = "SELECT " +
                "COUNT(students.group_id) AS cnt, " +
                "students.group_id as group_id, " +
                "groups.name as group_name " +
                "FROM " +
                "students " +
                "INNER JOIN groups ON students.group_id = groups.id " +
                "WHERE " +
                "students.group_id <> 0 " +
                "GROUP BY " +
                "students.group_id, groups.name " +
                "HAVING " +
                "COUNT(students.group_id) <= ? " +
                "ORDER BY students.group_id";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            groups = new ArrayList<>();
            pStatement.setInt(1, studentCount);
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                groups.add(new Group().id(resultSet.getInt("group_id")).name(resultSet.getString("group_name")));
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot find all groups with less or equals student count=" + studentCount, e);
        }
        return groups;
    }
}
