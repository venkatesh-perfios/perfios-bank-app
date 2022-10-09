package com.perfiosbank.pasttransactions;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AuthenticationUtils;

import java.sql.ResultSet;

public class PastTransactionsService {
    public ResultSet viewPastTransactions(User userInSession, User enteredDetails) 
    		throws AuthenticationFailedException, Exception {
        String msg;

        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }

        return PastTransactionsDao.getPastTransactionsByUsername(userInSession.getUsername());
    }
}
