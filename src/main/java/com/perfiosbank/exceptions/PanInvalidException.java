package com.perfiosbank.exceptions;

public class PanInvalidException extends Exception {
    private static final long serialVersionUID = 1L;

	public PanInvalidException(String msg) {
        super(msg);
    }
}
