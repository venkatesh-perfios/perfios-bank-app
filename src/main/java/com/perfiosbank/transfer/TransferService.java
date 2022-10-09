package com.perfiosbank.transfer;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.exceptions.*;
import com.perfiosbank.model.User;
import com.perfiosbank.model.TransferInfo;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;
import com.perfiosbank.utils.DateTimeUtils;
import com.perfiosbank.withdraw.WithdrawDao;

import java.sql.ResultSet;

public class TransferService {
    String targetUsername;

    public void transferMoney(User userInSession, TransferInfo transferInfo) 
    		throws AuthenticationFailedException, TargetAccountNumberSameAsUserException, 
    		AmountInvalidException, AmountLimitReachedException, BelowMinBalanceException, Exception {
        String msg;
        
        User enteredDetails = new User();
        enteredDetails.setUsername(transferInfo.getUsername());
        enteredDetails.setPassword(transferInfo.getPassword());
        
        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }

        if (isTargetAccountNumberNotFound(transferInfo.getTargetAccountNumber())) {
            msg = "The account number " + transferInfo.getTargetAccountNumber() + " doesn't exist! Re-check "
            		+ "your entered target account number.";
            throw new AccountNotFoundException(msg);
        }
        
        if (isTargetAccountNumberSameAsUser(userInSession.getUsername(), transferInfo.getTargetAccountNumber())) {
        	msg = "The target account number is the same as your account number! Do a deposit instead.";
        	throw new TargetAccountNumberSameAsUserException(msg);
        }

        if (AccountUtils.isAmountInvalid(transferInfo.getAmount())) {
            msg = "Please enter a valid amount! Here are some tips:\n" +
                    "1. Don't enter any alphabet or special character\n" +
                    "2. Don't enter 0 as the amount to withdraw\n" +
                    "3. Don't enter more than 2 numbers after the decimal point";
            throw new AmountInvalidException(msg);
        }

        if (transferInfo.getAmount() > 1000000) {
            msg = "You cannot transfer more than Rs. 1000000 at a time!";
            throw new AmountLimitReachedException(msg);
        }

        ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(userInSession.getUsername());
        if (!resultSet.next()) {
        	throw new Exception();
        }
        double currentUserBalance = resultSet.getDouble(1);
        double newUserBalance = currentUserBalance - transferInfo.getAmount();
        if (isBalanceInsufficient(newUserBalance)) {
        	msg = "There is insufficient balance in your account to withdraw the said amount!";
        	throw new InsufficientBalanceException(msg);
        }
        
        if (AccountUtils.isBelowMinBalance(newUserBalance)) {
            msg = "You must maintain a minimum balance of Rs. 1000 at all times!";
            throw new BelowMinBalanceException(msg);
        }

        String withdrawDate = DateTimeUtils.getCurrentDateTime();
        if (WithdrawDao.withdrawMoney(transferInfo.getUsername(), withdrawDate, transferInfo.getAmount(),  
        		newUserBalance) != 1) {
        	throw new Exception();
        }

        resultSet = CheckBalanceDao.getCurrentBalanceByUsername(targetUsername);
        if (!resultSet.next()) {
        	throw new Exception();
        }
        double currentTargetBalance = resultSet.getDouble(1);
        double newTargetBalance = currentTargetBalance + transferInfo.getAmount();
        String depositDate = DateTimeUtils.getCurrentDateTime();
        if (DepositDao.depositMoney(targetUsername, depositDate, transferInfo.getAmount(), 
        		newTargetBalance) != 1) {
        	throw new Exception();
        }
    }

    private boolean isTargetAccountNumberNotFound(String targetAccountNumber) throws Exception {
    	ResultSet resultSet = TransferDao.getAccountByAccountNumber(targetAccountNumber);
    	
        if (!resultSet.next()) {
            return true;
        } else {
            targetUsername = resultSet.getString(2);
            return false;
        }
    }
    
    private boolean isTargetAccountNumberSameAsUser(String username, String targetAccountNumber) 
    		throws Exception {
    	ResultSet resultSet = TransferDao.getAccountByUsername(username);
    	resultSet.next();
    	String userAccountNumber = resultSet.getString("Account_Number");
    	
    	return userAccountNumber.equals(targetAccountNumber);
    }
    
    private boolean isBalanceInsufficient(double newBalance) {
    	return newBalance < 0;
    }
}
