package com.perfiosbank.model;

import java.util.HashMap;
import java.util.List;

public class AccountInfo {
    private String firstName;
    private String lastName;
    private int age;
    private long aadhaar;
    private String pan;
    private String address;
    private long phone;
    private double amount;
	HashMap<String, byte[]> uploadedFiles;
	List<String> uploadedFilenames;

    public HashMap<String, byte[]> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(HashMap<String, byte[]> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<String> getUploadedFilenames() {
		return uploadedFilenames;
	}

	public void setUploadedFilenames(List<String> uploadedFilenames) {
		this.uploadedFilenames = uploadedFilenames;
	}

	public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public long getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(long aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
