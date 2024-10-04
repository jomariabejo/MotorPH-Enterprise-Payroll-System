package com.jomariabejo.motorph.exception;

import org.hibernate.exception.ConstraintViolationException;

public abstract class AbstractDuplicateEntryHandler {

    protected void handleDuplicateEntryException(Exception ex) {
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException constraintEx = (ConstraintViolationException) ex;
            handleConstraintViolationException(constraintEx);
        } else {
            // Handle other types of exceptions or log them
            System.err.println("Unhandled exception occurred: " + ex.getMessage());
        }
    }

    protected abstract void handleConstraintViolationException(ConstraintViolationException ex);
}
