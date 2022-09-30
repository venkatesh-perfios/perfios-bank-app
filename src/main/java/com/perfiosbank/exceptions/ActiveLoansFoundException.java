package com.perfiosbank.exceptions;

public class ActiveLoansFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ActiveLoansFoundException(String msg) {
		super(msg);
	}
}
