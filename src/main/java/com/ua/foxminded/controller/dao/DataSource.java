package com.ua.foxminded.controller.dao;

import com.ua.foxminded.controller.dao.exceptions.NoDBPropertiesException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static BasicDataSource ds = new BasicDataSource();
    private static final String DATABASE_PROPERTIES = "database.properties";

    static {
        Properties props = new Properties();
        try(InputStream in = DataSource.class.getClassLoader().getResourceAsStream(DATABASE_PROPERTIES)){
            if (in == null){
                throw new NoDBPropertiesException("file not found " + DATABASE_PROPERTIES);
            }
            props.load(in);
            String driver = props.getProperty("jdbc.driver");
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setMinIdle(5);
            ds.setMaxIdle(10);
            ds.setMaxOpenPreparedStatements(100);

        } catch (IOException e) {
            log.error("Unable to read database.properties", e);
        } catch (NoDBPropertiesException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DataSource(){ }
}
