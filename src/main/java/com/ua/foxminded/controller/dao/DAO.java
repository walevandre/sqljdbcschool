package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.DAOException;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    Optional<T> getById(int id) throws DAOException;

    List<T> getAll() throws DAOException;

    T save(T t) throws DAOException;

    void saveList (List<T> tList) throws DAOException;

    T update(T t) throws DAOException;

    boolean delete(T t) throws DAOException;
}
