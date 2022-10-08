package com.perfiosbank.carloan;

import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.DurationRangeException;
import com.perfiosbank.exceptions.EndDateInvalidException;
import com.perfiosbank.exceptions.FileInvalidException;
import com.perfiosbank.model.CarLoanInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;

public class CarLoanService {
	public void applyCarLoan(User userInSession, CarLoanInfo carLoanInfo) 
			throws AuthenticationFailedException, AccountNotFoundException, AmountInvalidException, 
			EndDateInvalidException, DurationRangeException, FileInvalidException, Exception {
		String msg;

        User enteredDetails = new User();
        enteredDetails.setUsername(carLoanInfo.getUsername());
        enteredDetails.setPassword(carLoanInfo.getPassword());

        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }
        
        if (AccountUtils.isAccountNotFound(userInSession)) {
            msg = "Please open an account before depositing money into it!";
            throw new AccountNotFoundException(msg);
        }

        if (AccountUtils.isAmountInvalid(carLoanInfo.getLoanAmount())) {
            msg = "Please enter a valid amount! Here are some tips:<br>" +
                    "1. Don't enter any alphabet or special character<br>" +
                    "2. Don't enter 0 as the amount to deposit<br>" +
                    "3. Don't enter more than 2 numbers after the decimal point";
            throw new AmountInvalidException(msg);
        }
        
        for (String filename : carLoanInfo.getUploadedFilenames()) {
        	String type = filename.split(",")[0];
        	String actualFilename = filename.split(",")[1];
	        if (isFilenameInvalid(actualFilename)) {
	        	msg = type + ", Please upload a pdf file!";
	        	throw new FileInvalidException(msg);
	        }
        }

        carLoanInfo.setInterestRate(getInterestRate(carLoanInfo.getCibilScore(), carLoanInfo.getDays()));
        carLoanInfo.setDueAmount(getDueAmount(carLoanInfo, carLoanInfo.getDays()));
        
        if (CarLoanDao.saveLoanApplication(carLoanInfo) != 1) {
        	throw new Exception();
        }
	}
	
	private boolean isFilenameInvalid(String filename) {
		return !(filename.substring(filename.length() - 3).equals("pdf"));
	}

    private double getInterestRate(int cibil, int days) throws Exception {
    	if (cibil >= 775) {
	    	if (days <= 1825) {
	    		return 7.85;
	    	} else {
	    		return 7.95;
	    	}
    	} else if (cibil >= 750) {
	    	if (days <= 1825) {
	    		return 8.20;
	    	} else {
	    		return 8.30;
	    	}
    	} else if (cibil >= 700) {
	    	if (days <= 1825) {
	    		return 8.45;
	    	} else {
	    		return 8.55;
	    	}
    	} else {
	    	if (days <= 1825) {
	    		return 8.80;
	    	} else {
	    		return 8.90;
	    	}
    	}
    }
    
    private double getDueAmount(CarLoanInfo carLoanInfo, int days) {
    	return carLoanInfo.getLoanAmount() * Math.pow(1 + carLoanInfo.getInterestRate() / 100, days / 365.0);
    }
}
