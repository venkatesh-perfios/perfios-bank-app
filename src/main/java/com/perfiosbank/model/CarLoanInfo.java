package com.perfiosbank.model;

import java.util.HashMap;
import java.util.List;

public class CarLoanInfo {
	private String username;
	private String password;
	private double loanAmount;
	private String dueDate;
	private int cibilScore;
	HashMap<String, byte[]> uploadedFiles;
	List<String> uploadedFilenames;
	private double interestRate;
	private double dueAmount;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public double getLoanAmount() {
		return loanAmount;
	}
	
	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	public int getCibilScore() {
		return cibilScore;
	}
	
	public void setCibilScore(int cibilScore) {
		this.cibilScore = cibilScore;
	}
	
	public HashMap<String, byte[]> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFilenames(List<String> uploadedFilenames) {
		this.uploadedFilenames = uploadedFilenames;
	}

	public List<String> getUploadedFilenames() {
		return uploadedFilenames;
	}
	
	public void setUploadedFiles(HashMap<String, byte[]> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}
	
	public double getInterestRate() {
		return interestRate;
	}
	
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}
}
