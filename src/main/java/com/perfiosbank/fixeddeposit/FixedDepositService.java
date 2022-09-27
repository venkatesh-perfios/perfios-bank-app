package com.perfiosbank.fixeddeposit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.perfiosbank.checkbalance.CheckBalanceService;
import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.DurationRangeException;
import com.perfiosbank.exceptions.AmountRangeException;
import com.perfiosbank.exceptions.EndDateInvalidException;
import com.perfiosbank.exceptions.InsufficientBalanceException;
import com.perfiosbank.model.FixedDepositInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;
import com.perfiosbank.utils.DateTimeUtils;
import com.perfiosbank.withdraw.WithdrawDao;

public class FixedDepositService {
	public void startFixedDeposit(User userInSession, FixedDepositInfo fixedDepositInfo) 
			throws AuthenticationFailedException, AccountNotFoundException, AmountInvalidException, 
			AmountRangeException, InsufficientBalanceException, BelowMinBalanceException, 
			EndDateInvalidException, DurationRangeException, Exception {
		String msg;

        User enteredDetails = new User();
        enteredDetails.setUsername(fixedDepositInfo.getUsername());
        enteredDetails.setPassword(fixedDepositInfo.getPassword());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = simpleDateFormat.parse(DateTimeUtils.getCurrentDateTime().substring(0, 10));
        Date endDate = simpleDateFormat.parse(fixedDepositInfo.getEndDate());
        long differenceInTime = endDate.getTime() - now.getTime();
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInTime);
        
        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }

        if (AccountUtils.isAccountNotFound(userInSession)) {
            msg = "Please open an account before depositing money into it!";
            throw new AccountNotFoundException(msg);
        }

        if (AccountUtils.isAmountInvalid(fixedDepositInfo.getPrincipal())) {
            msg = "Please enter a valid amount! Here are some tips:<br>" +
                    "1. Don't enter any alphabet or special character<br>" +
                    "2. Don't enter 0 as the amount to deposit<br>" +
                    "3. Don't enter more than 2 numbers after the decimal point";
            throw new AmountInvalidException(msg);
        }
        
        if (isAmountRangeInvalid(fixedDepositInfo.getPrincipal())) {
        	msg = "Fixed Deposit account must have a minimum of Rs. 1000 and a maximum of Rs. 100000";
        	throw new AmountRangeException(msg);
        }
        
        if (isEndDateInvalid(fixedDepositInfo.getEndDate())) {
        	msg = "Please enter a valid year!";
        	throw new EndDateInvalidException(msg);
        }
        
        if (isEndDateExpired(differenceInTime) || isDurationRangeInvalid(differenceInDays)) {
        	msg = "Fixed Deposit account must be for a minimum of 7 days and a maximum of 10 years from now!";
        	throw new DurationRangeException(msg);
        }
        
        CheckBalanceService checkBalance = new CheckBalanceService();
        double currentBalance = checkBalance.checkBalance(userInSession, enteredDetails);
        double newBalance = currentBalance - fixedDepositInfo.getPrincipal();
        if (isBalanceInsufficient(newBalance)) {
        	msg = "There is insufficient balance in your account to withdraw the said amount!";
        	throw new InsufficientBalanceException(msg);
        }
        if (AccountUtils.isBelowMinBalance(newBalance)) {
            msg = "You must maintain a minimum balance of Rs. 1000 at all times!";
            throw new BelowMinBalanceException(msg);
        }
        
        fixedDepositInfo.setInterestRate(getInterestRate(differenceInDays));
        fixedDepositInfo.setMaturityAmount(getMaturityAmount(fixedDepositInfo, differenceInDays));
        
        if (WithdrawDao.withdrawMoney(fixedDepositInfo.getUsername(), DateTimeUtils.getCurrentDateTime(), 
        		fixedDepositInfo.getPrincipal(), newBalance) != 1) {
        	throw new Exception();
        }

        if (FixedDepositDao.openFixedDepositAccount(fixedDepositInfo) != 1) {
        	throw new Exception();
        }
	}
	
	private boolean isAmountRangeInvalid(double amount) {
		return amount < 1000 || amount > 100000;
	}
	
	private boolean isEndDateInvalid(String endDate) {
		return endDate.split("-")[0].length() > 4;
	}
	
	private boolean isEndDateExpired(long differenceInTime) throws Exception {
        return differenceInTime <= 0;
	}
	
	private boolean isDurationRangeInvalid(long differenceInDays) throws Exception {
        boolean lessThanAWeek = differenceInDays < 7;
        boolean moreThanTenYears = differenceInDays > 3650;
        
        return lessThanAWeek || moreThanTenYears;
	}

    private boolean isBalanceInsufficient(double newBalance) {
    	return newBalance < 0;
    }
    
    private double getInterestRate(long differenceInDays) {
    	if (differenceInDays >= 7 && differenceInDays <= 179) {
    		return 3.90;
    	} else if (differenceInDays >= 180 && differenceInDays <= 364) {
    		return 4.60;
    	} else if (differenceInDays >= 365 && differenceInDays <= 1824) {
    		return 5.50;
    	} else {
    		return 6.50;
    	}
    } 
    
    private double getMaturityAmount(FixedDepositInfo fixedDepositInfo, long differenceInDays) {
    	return fixedDepositInfo.getPrincipal() * Math.pow(1 + fixedDepositInfo.getInterestRate() / 100, 
    			differenceInDays / 365);
    }
}
