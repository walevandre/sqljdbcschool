package com.ua.foxminded.controller.service.DBService;

public class SqlExecuteException extends Exception {
    public SqlExecuteException() {
        super();
    }

    public SqlExecuteException(String message) {
        super(message);
    }

    public SqlExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlExecuteException(Throwable cause) {
        super(cause);
    }
}
