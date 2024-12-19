package com.library.management.exception;

public class DuplicateBorrowerEmailException extends RuntimeException {
    public DuplicateBorrowerEmailException(String message) {
        super(message);
    }
}
