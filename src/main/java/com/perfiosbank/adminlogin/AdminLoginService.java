package com.perfiosbank.adminlogin;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;

import java.sql.*;

public class AdminLoginService {
    public User loginUser(User enteredDetails) throws AuthenticationFailedException, Exception {
        ResultSet resultSet = AdminLoginDao.getPasswordByUsername(enteredDetails.getUsername());
        boolean isUsernameIncorrect = !resultSet.next();
        if (isUsernameIncorrect) {
            throwAuthenticationFailedException();
        } else {
            boolean isPasswordIncorrect = !(enteredDetails.getPassword()
            		.equals(resultSet.getString(1)));
            if (isPasswordIncorrect) {
                throwAuthenticationFailedException();
            }
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
