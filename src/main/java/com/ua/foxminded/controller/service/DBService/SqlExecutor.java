package com.ua.foxminded.controller.service.DBService;

import com.ua.foxminded.controller.dao.DataSource;
import com.ua.foxminded.controller.service.reader.FileReader;
import com.ua.foxminded.controller.service.reader.ReaderException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);

    public void executeSqlFile(String sqlFile) throws SqlExecuteException {

        FileReader fileReader = new FileReader();

        try (Connection conn = DataSource.getConnection()) {

            ScriptRunner runner = new ScriptRunner(conn);
            runner.setAutoCommit(false);
            runner.setStopOnError(true);
            runner.setSendFullScript(false);

            InputStream inputStream = fileReader.getFileFromResourceAsStream(sqlFile);
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Reader reader = new BufferedReader(streamReader);
            runner.runScript(reader);

        } catch (SQLException | ReaderException e) {
            log.debug("Failed to execute " + sqlFile, e);
            throw new SqlExecuteException("Failed to execute " + sqlFile, e);
        }
    }
}
