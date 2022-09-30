package com.perfiosbank.exceptions;

public class TargetAccountNumberSameAsUserException extends Exception {
	private static final long serialVersionUID = 1L;

	public TargetAccountNumberSameAsUserException(String msg) {
		super(msg);
	}
}
