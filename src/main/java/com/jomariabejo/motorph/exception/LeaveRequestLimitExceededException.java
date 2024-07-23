package com.jomariabejo.motorph.exception;

public class LeaveRequestLimitExceededException extends RuntimeException {

    public LeaveRequestLimitExceededException(String message) {
        super(message);
    }
}
