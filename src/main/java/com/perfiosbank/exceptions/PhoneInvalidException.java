package com.perfiosbank.exceptions;

public class PhoneInvalidException extends Exception {
    private static final long serialVersionUID = 1L;

	public PhoneInvalidException(String msg) {
        super(msg);
    }
}
