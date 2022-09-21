package com.perfiosbank.login;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.*;

public class LoginService {
    public User loginUser(User enteredDetails) throws AuthenticationFailedException, Exception {
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
