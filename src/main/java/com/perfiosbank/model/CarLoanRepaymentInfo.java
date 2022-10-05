package com.perfiosbank.model;

public class CarLoanRepaymentInfo {
	private int loanId;
	private String username;
	private String startDate;
	private int hasStarted;
	private String endDate;
	private int hasEnded;
	private double emi;
	private int misses;
	private double penalty;
	
	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public int getHasStarted() {
		return hasStarted;
	}
	
	public void setHasStarted(int hasStarted) {
		this.hasStarted = hasStarted;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getHasEnded() {
		return hasEnded;
	}
	
	public void setHasEnded(int hasEnded) {
		this.hasEnded = hasEnded;
	}
	
	public double getEmi() {
		return emi;
	}
	
	public void setEmi(double emi) {
		this.emi = emi;
	}
	
	public int getMisses() {
		return misses;
	}
	
	public void setMisses(int misses) {
		this.misses = misses;
	}
	
	public double getPenalty() {
		return penalty;
	}
	
	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}
}
