package com.ua.foxminded.controller.dao.exceptions;

public class NoDBPropertiesException extends Exception {
    public NoDBPropertiesException() {
        super();
    }

    public NoDBPropertiesException(String message) {
        super(message);
    }

    public NoDBPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDBPropertiesException(Throwable cause) {
        super(cause);
    }
}
