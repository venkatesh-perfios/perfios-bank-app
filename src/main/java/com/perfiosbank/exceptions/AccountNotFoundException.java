package com.perfiosbank.exceptions;

public class AccountNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String msg) {
        super(msg);
    }
}
