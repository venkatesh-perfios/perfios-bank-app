package com.perfiosbank.model;

public class AdminCarLoansInfo {
	int id;
	String username;
	double principal;
	String newStatus;
	int days;
	double dueAmount;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public double getPrincipal() {
		return principal;
	}
	
	public void setPrincipal(double principal) {
		this.principal = principal;
	}
	
	public String getNewStatus() {
		return newStatus;
	}
	
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	
	public int getDays() {
		return days;
	}
	
	public void setDays(int days) {
		this.days = days;
	}
	
	public double getDueAmount() {
		return dueAmount;
	}
	
	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}
}
