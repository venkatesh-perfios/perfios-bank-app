package com.perfiosbank.exceptions;

public class AccountFrozenException extends Exception {
	private static final long serialVersionUID = 1L;

	public AccountFrozenException(String msg) {
		super(msg);
	}
}
