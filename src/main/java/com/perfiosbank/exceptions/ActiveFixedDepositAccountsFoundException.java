package com.perfiosbank.exceptions;

public class ActiveFixedDepositAccountsFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ActiveFixedDepositAccountsFoundException(String msg) {
		super(msg);
	}
}
