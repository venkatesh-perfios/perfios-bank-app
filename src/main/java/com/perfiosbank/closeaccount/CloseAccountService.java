package com.perfiosbank.closeaccount;

import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.ActiveLoansFoundException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;

public class CloseAccountService {
    public void closeAccount(User userInSession, User enteredDetails) 
    		throws AuthenticationFailedException, AccountNotFoundException, 
    		ActiveLoansFoundException, Exception {
        String msg;

        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }
        
        if (AccountUtils.isAccountNotFound(userInSession)) {
            msg = "Please open an account before closing it!";
            throw new AccountNotFoundException(msg);
        }

        if (CloseAccountDao.getNumberOfApprovedLoans(userInSession.getUsername()) != 0) {
        	msg = "You cannot close your account when you have active loans to clear!";
        	throw new ActiveLoansFoundException(msg);
        }
        
        if (CloseAccountDao.removeUser(userInSession) != 1) {
        	throw new Exception();
        }
    }
}
