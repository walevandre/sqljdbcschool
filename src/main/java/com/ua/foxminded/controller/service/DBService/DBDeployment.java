package com.ua.foxminded.controller.service.DBService;

public class DBDeployment {

    private static final String TABLES_CREATION_SQL = "sql/tablesCreation.sql";

    public void deploy() throws SqlExecuteException {
        SqlExecutor sqlExecutor = new SqlExecutor();
        sqlExecutor.executeSqlFile(TABLES_CREATION_SQL);
    }
}
