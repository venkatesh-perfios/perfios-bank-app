package com.perfiosbank.exceptions;

public class BelowMinBalanceException extends Exception {
    private static final long serialVersionUID = 1L;

	public BelowMinBalanceException(String msg) {
        super(msg);
    }
}
