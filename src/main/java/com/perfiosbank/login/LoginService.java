package com.perfiosbank.login;

import com.perfiosbank.exceptions.AccountFrozenException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AccountUtils;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.*;

public class LoginService {
    public User loginUser(User enteredDetails) throws AuthenticationFailedException, 
    		AccountFrozenException, Exception {
        ResultSet resultSet = LoginDao.getPasswordByUsername(enteredDetails.getUsername());
        boolean isUsernameIncorrect = !resultSet.next();
        if (isUsernameIncorrect) {
            throwAuthenticationFailedException();
        } else {
            boolean isPasswordIncorrect = !(new StrongPasswordEncryptor().checkPassword(
            		enteredDetails.getPassword(), resultSet.getString(1)));
            if (isPasswordIncorrect) {
                throwAuthenticationFailedException();
            }
        }

        if (AccountUtils.isAccountFrozen(enteredDetails.getUsername())) {
        	String msg = "Your account has been frozen for defaulting on your car loan!";
        	throw new AccountFrozenException(msg);
        }
        
        User userInNewSession = new User();
        userInNewSession.setUsername(enteredDetails.getUsername());
        userInNewSession.setPassword(resultSet.getString(1));

        return userInNewSession;
    }

    private void throwAuthenticationFailedException() throws AuthenticationFailedException {
        String msg = "Authentication failed! Either you haven't registered yet or " +
                "your username/password is incorrect.";
        throw new AuthenticationFailedException(msg);
    }
}
