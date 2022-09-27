package com.perfiosbank.exceptions;

public class EndDateInvalidException extends Exception {
	private static final long serialVersionUID = 1L;

	public EndDateInvalidException(String msg) {
		super(msg);
	}
}
