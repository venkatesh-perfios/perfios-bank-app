package com.perfiosbank.exceptions;

public class UsernameTooLongException extends Exception {
    private static final long serialVersionUID = 1L;

	public UsernameTooLongException(String msg) {
        super(msg);
    }
}
