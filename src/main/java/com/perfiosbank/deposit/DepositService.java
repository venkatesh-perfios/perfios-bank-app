package com.perfiosbank.deposit;

import com.perfiosbank.checkbalance.CheckBalanceService;
import com.perfiosbank.exceptions.*;
import com.perfiosbank.model.DepositWithdrawInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;
import com.perfiosbank.utils.DateTimeUtils;

public class DepositService {
    public void depositMoney(User userInSession, DepositWithdrawInfo depositInfo) 
    		throws AuthenticationFailedException, AccountNotFoundException, AmountInvalidException,
            AmountLimitReachedException, Exception {
        String msg;

        User enteredDetails = new User();
        enteredDetails.setUsername(depositInfo.getUsername());
        enteredDetails.setPassword(depositInfo.getPassword());
        
        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }

        if (AccountUtils.isAccountNotFound(userInSession)) {
            msg = "Please open an account before depositing money into it!";
            throw new AccountNotFoundException(msg);
        }

        if (AccountUtils.isAmountInvalid(depositInfo.getAmount())) {
            msg = "Please enter a valid amount! Here are some tips:<br>" +
                    "1. Don't enter any alphabet or special character<br>" +
                    "2. Don't enter 0 as the amount to deposit<br>" +
                    "3. Don't enter more than 2 numbers after the decimal point";
            throw new AmountInvalidException(msg);
        }

        if (depositInfo.getAmount() < 1000) {
            msg = "You cannot invest less than Rs. 1000 into fixed deposit!";
            throw new AmountLimitReachedException(msg);
        }

        if (depositInfo.getAmount() > 150000) {
            msg = "You cannot invest more than Rs. 150000 into fixed deposit!";
            throw new AmountLimitReachedException(msg);
        }

        CheckBalanceService checkBalance = new CheckBalanceService();
        double currentBalance = checkBalance.checkBalance(userInSession, enteredDetails);
        double newBalance = currentBalance + depositInfo.getAmount();
        String date = DateTimeUtils.getCurrentDateTime();
        if (DepositDao.depositMoney(depositInfo.getUsername(), date, depositInfo.getAmount(), newBalance) != 1) {
			throw new Exception();
        }
    }
}
