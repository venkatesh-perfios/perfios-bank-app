package com.perfiosbank.checkbalance;

import com.perfiosbank.exceptions.AccountNotFoundException;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;

import java.sql.ResultSet;

public class CheckBalanceService {
    public double checkBalance(User userInSession, User enteredDetails) 
    		throws AuthenticationFailedException, AccountNotFoundException, Exception {
        String msg;

        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }

        if (AccountUtils.isAccountNotFound(userInSession)) {
            msg = "Please open an account before checking its balance!";
            throw new AccountNotFoundException(msg);
        }

        ResultSet resultSet = CheckBalanceDao.getCurrentBalance(userInSession);

        return resultSet.next() ? resultSet.getDouble(1) : 0.0;
    }
}
