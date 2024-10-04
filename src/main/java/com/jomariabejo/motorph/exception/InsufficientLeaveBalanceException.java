package com.jomariabejo.motorph.exception;

public class InsufficientLeaveBalanceException extends RuntimeException {

    public InsufficientLeaveBalanceException() {
        super();
    }

    public InsufficientLeaveBalanceException(String message) {
        super(message);
    }

    public InsufficientLeaveBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientLeaveBalanceException(Throwable cause) {
        super(cause);
    }
}
