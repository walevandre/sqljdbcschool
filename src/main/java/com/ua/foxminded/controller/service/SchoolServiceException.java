package com.ua.foxminded.controller.service;

public class SchoolServiceException extends Exception {
    public SchoolServiceException() {
        super();
    }

    public SchoolServiceException(String message) {
        super(message);
    }

    public SchoolServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchoolServiceException(Throwable cause) {
        super(cause);
    }
}
