package com.perfiosbank.exceptions;

public class PasswordInvalidException extends Exception {
    private static final long serialVersionUID = 1L;

	public PasswordInvalidException(String msg) {
        super(msg);
    }
}
