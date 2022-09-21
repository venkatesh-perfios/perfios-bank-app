package com.perfiosbank.exceptions;

public class InvalidPasswordException extends Exception {
    private static final long serialVersionUID = 1L;

	public InvalidPasswordException(String msg) {
        super(msg);
    }
}
