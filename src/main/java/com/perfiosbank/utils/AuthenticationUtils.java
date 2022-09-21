package com.perfiosbank.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.perfiosbank.model.User;

public class AuthenticationUtils {
	public static String encryptPassword(String password) {
		return new StrongPasswordEncryptor().encryptPassword(password);
	}
	
    public static boolean isUserNotAuthenticated(User userInSession, User enteredDetails) {
        boolean isUsernameIncorrect = !(enteredDetails.getUsername().equals(userInSession.getUsername()));
        boolean isPasswordIncorrect = !(new StrongPasswordEncryptor().checkPassword(enteredDetails.getPassword(),
        		userInSession.getPassword()));

        return isUsernameIncorrect || isPasswordIncorrect;
    }
}
