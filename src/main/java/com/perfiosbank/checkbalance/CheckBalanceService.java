package com.perfiosbank.checkbalance;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AuthenticationUtils;

import java.sql.ResultSet;

public class CheckBalanceService {
    public double checkBalance(User userInSession, User enteredDetails) 
    		throws AuthenticationFailedException, Exception {
        String msg;

        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }
        
		ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(userInSession.getUsername());

        return resultSet.next() ? resultSet.getDouble(1) : 0.0;
    }
}
