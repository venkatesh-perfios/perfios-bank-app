package com.perfiosbank.exceptions;

public class NameInvalidException extends Exception {
	private static final long serialVersionUID = 1L;

	public NameInvalidException(String msg) {
		super(msg);
	}
}
