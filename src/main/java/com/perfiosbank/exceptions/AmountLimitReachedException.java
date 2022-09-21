package com.perfiosbank.exceptions;

public class AmountLimitReachedException extends Exception {
    private static final long serialVersionUID = 1L;

	public AmountLimitReachedException(String msg) {
        super(msg);
    }
}
