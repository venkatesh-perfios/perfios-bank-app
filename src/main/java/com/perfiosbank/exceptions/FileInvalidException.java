package com.perfiosbank.exceptions;

public class FileInvalidException extends Exception {
	private static final long serialVersionUID = 1L;

	public FileInvalidException(String msg) {
		super(msg);
	}
}
