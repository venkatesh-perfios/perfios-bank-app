package com.perfiosbank.exceptions;

public class NewPasswordSameAsCurrentException extends Exception {
	private static final long serialVersionUID = 1L;

	public NewPasswordSameAsCurrentException(String msg) {
		super(msg);
	}
}
