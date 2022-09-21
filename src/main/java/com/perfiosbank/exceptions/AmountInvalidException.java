package com.perfiosbank.exceptions;

public class AmountInvalidException extends Exception {
    private static final long serialVersionUID = 1L;

	public AmountInvalidException(String msg) {
        super(msg);
    }
}
