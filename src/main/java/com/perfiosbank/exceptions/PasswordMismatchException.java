package com.perfiosbank.exceptions;

public class PasswordMismatchException extends Exception {
    private static final long serialVersionUID = 1L;

	public PasswordMismatchException(String msg) {
        super(msg);
    }
}
