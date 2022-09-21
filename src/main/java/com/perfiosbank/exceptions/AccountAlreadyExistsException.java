package com.perfiosbank.exceptions;

public class AccountAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

	public AccountAlreadyExistsException(String msg) {
        super(msg);
    }
}
