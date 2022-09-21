package com.perfiosbank.exceptions;

public class UsernameAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

	public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }
}
