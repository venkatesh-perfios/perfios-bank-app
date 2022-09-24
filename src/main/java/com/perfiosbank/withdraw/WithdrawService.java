package com.perfiosbank.withdraw;

import com.perfiosbank.checkbalance.CheckBalanceService;
import com.perfiosbank.exceptions.*;
import com.perfiosbank.model.User;
import com.perfiosbank.model.DepositWithdrawInfo;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;
import com.perfiosbank.utils.DateTimeUtils;

public class WithdrawService {
    public void withdrawMoney(User userInSession, DepositWithdrawInfo withdrawInfo) 
    		throws AuthenticationFailedException, AccountNotFoundException, AmountInvalidException, 
    		AmountLimitReachedException, InsufficientBalanceException, BelowMinBalanceException, Exception {
        String msg;
        
        User enteredDetails = new User();
        enteredDetails.setUsername(withdrawInfo.getUsername());
        enteredDetails.setPassword(withdrawInfo.getPassword());
        
        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }

        if (AccountUtils.isAccountNotFound(userInSession)) {
            msg = "Please open an account before withdrawing money from it!";
            throw new AccountNotFoundException(msg);
        }

        if (AccountUtils.isAmountInvalid(withdrawInfo.getAmount())) {
            msg = "Please enter a valid amount! Here are some tips:<br>" +
                    "1. Don't enter any alphabet or special character<br>" +
                    "2. Don't enter 0 as the amount to withdraw<br>" +
                    "3. Don't enter more than 2 numbers after the decimal point";
            throw new AmountInvalidException(msg);
        }

        if (withdrawInfo.getAmount() > 10000) {
            msg = "You cannot withdraw more than Rs. 10000 at a time!";
            throw new AmountLimitReachedException(msg);
        }

        CheckBalanceService checkBalance = new CheckBalanceService();
        double currentBalance = checkBalance.checkBalance(userInSession, enteredDetails);
        double newBalance = currentBalance - withdrawInfo.getAmount();
        if (isBalanceInsufficient(newBalance)) {
        	msg = "There is insufficient balance in your account to withdraw the said amount!";
        	throw new InsufficientBalanceException(msg);
        }
        
        if (AccountUtils.isBelowMinBalance(newBalance)) {
            msg = "You must maintain a minimum balance of Rs. 1000 at all times!";
            throw new BelowMinBalanceException(msg);
        }

        String date = DateTimeUtils.getCurrentDateTime();
        if (WithdrawDao.withdrawMoney(withdrawInfo, date, newBalance) != 1) {
			throw new Exception();
        }
    }
    
    private boolean isBalanceInsufficient(double newBalance) {
    	return newBalance < 0;
    }
}
