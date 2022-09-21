package com.perfiosbank.exceptions;

public class NotLoggedInException extends Exception {
    private static final long serialVersionUID = 1L;

	public NotLoggedInException(String msg) {
        super(msg);
    }
}
