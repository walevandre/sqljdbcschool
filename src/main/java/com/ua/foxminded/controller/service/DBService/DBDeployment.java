package com.ua.foxminded.controller.service.DBService;

public class DBDeployment {

    private SqlExecutor sqlExecutor;
    private static final String TABLES_CREATION_SQL = "sql/tablesCreation.sql";
    private static final String DROP_TABLES_SQL = "sql/droptables.sql";

    public DBDeployment(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    public void deploy() throws SqlExecuteException {
        sqlExecutor.executeSqlFile(TABLES_CREATION_SQL);
    }

    public void dropAllTables() throws SqlExecuteException {
        sqlExecutor.executeSqlFile(DROP_TABLES_SQL);
    }
}
