package com.perfiosbank.carloan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
import com.perfiosbank.utils.DateTimeUtils;

public class CarLoanService {
	public void applyCarLoan(User userInSession, CarLoanInfo carLoanInfo) 
			throws AuthenticationFailedException, AccountNotFoundException, AmountInvalidException, 
			EndDateInvalidException, DurationRangeException, FileInvalidException, Exception {
		String msg;

        User enteredDetails = new User();
        enteredDetails.setUsername(carLoanInfo.getUsername());
        enteredDetails.setPassword(carLoanInfo.getPassword());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = simpleDateFormat.parse(DateTimeUtils.getCurrentDateTime().substring(0, 10));
        Date endDate = simpleDateFormat.parse(carLoanInfo.getDueDate());
        long differenceInTime = endDate.getTime() - now.getTime();
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInTime);
        
        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
        	System.out.println(userInSession.getUsername());
        	System.out.println(userInSession.getPassword());
        	System.out.println(enteredDetails.getUsername());
        	System.out.println(enteredDetails.getPassword());
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
        
        if (isDueDateInvalid(carLoanInfo.getDueDate())) {
        	msg = "Please enter a valid year!";
        	throw new EndDateInvalidException(msg);
        }
        
        if (isDueDateExpired(differenceInTime) || isDurationRangeInvalid(differenceInDays)) {
        	msg = "Car Loan duration must be for a minimum of 1 year and a maximum of 7 years from now!";
        	throw new DurationRangeException(msg);
        }
        
        for (String filename : carLoanInfo.getUploadedFilenames()) {
        	String type = filename.split(",")[0];
        	String actualFilename = filename.split(",")[1];
	        if (isFilenameInvalid(actualFilename)) {
	        	msg = type + ", Please upload a pdf file!";
	        	throw new FileInvalidException(msg);
	        }
        }

        carLoanInfo.setInterestRate(getInterestRate(carLoanInfo.getCibilScore(), differenceInDays));
        carLoanInfo.setDueAmount(getDueAmount(carLoanInfo, differenceInDays));
        
        if (CarLoanDao.saveLoanApplication(carLoanInfo) != 1) {
        	throw new Exception();
        }
	}
	
	private boolean isFilenameInvalid(String filename) {
		return !(filename.substring(filename.length() - 3).equals("pdf"));
	}
	
	private boolean isDueDateInvalid(String endDate) {
		return endDate.split("-")[0].length() > 4;
	}
	
	private boolean isDueDateExpired(long differenceInTime) throws Exception {
        return differenceInTime <= 0;
	}

	private boolean isDurationRangeInvalid(long differenceInDays) throws Exception {
        boolean lessThanAYear = differenceInDays < 365;
        boolean moreThanSevenYears = differenceInDays > 2555;
        
        return lessThanAYear || moreThanSevenYears;
	}

    private double getInterestRate(int cibil, long differenceInDays) throws Exception {
    	if (cibil >= 775) {
	    	if (differenceInDays <= 1825) {
	    		return 7.85;
	    	} else {
	    		return 7.95;
	    	}
    	} else if (cibil >= 750) {
	    	if (differenceInDays <= 1825) {
	    		return 8.20;
	    	} else {
	    		return 8.45;
	    	}
    	} else if (cibil >= 700) {
	    	if (differenceInDays <= 1825) {
	    		return 8.45;
	    	} else {
	    		return 8.55;
	    	}
    	} else {
	    	if (differenceInDays <= 1825) {
	    		return 8.80;
	    	} else {
	    		return 8.90;
	    	}
    	}
    } 
    
    private double getDueAmount(CarLoanInfo carLoanInfo, long differenceInDays) {
    	return carLoanInfo.getLoanAmount() * Math.pow(1 + carLoanInfo.getInterestRate() / 100, differenceInDays / 365.0);
    }
}
