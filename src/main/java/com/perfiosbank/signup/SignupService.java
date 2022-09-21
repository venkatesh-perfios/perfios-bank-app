package com.perfiosbank.signup;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.perfiosbank.exceptions.InvalidPasswordException;
import com.perfiosbank.exceptions.PasswordMismatchException;
import com.perfiosbank.exceptions.UsernameAlreadyExistsException;
import com.perfiosbank.exceptions.UsernameTooLongException;
import com.perfiosbank.model.User;

public class SignupService {
    public void signupUser(User user, String reenteredPassword) 
    		throws UsernameAlreadyExistsException, UsernameTooLongException, InvalidPasswordException, 
    		PasswordMismatchException, Exception {
		String msg;
		
		if (isUsernameAlreadyExists(user.getUsername())) {
			msg = "Username already taken! Try again with a different username.";
			throw new UsernameAlreadyExistsException(msg);
		}
		
		if (isUsernameTooLong(user.getUsername())) {
			msg = "Your username is too long! Please ensure it doesn't have more than 20 characters.";
			throw new UsernameTooLongException(msg);
		}
		
		if (isPasswordInvalid(user.getPassword())) {
			msg = "Invalid Password! Please ensure your password satisfies the following criteria:<br>" +
			"1. Has at least 8 characters<br>" +
			"2. Contains at least 1 digit<br>" +
			"3. Contains at least 1 small letter<br>" +
			"4. Contains at least 1 capital letter<br>" +
			"5. Contains at least 1 of these special characters: @, #, $, %, ^, &, +, =";
			throw new InvalidPasswordException(msg);
		}
		
		if (isPasswordMismatch(user.getPassword(), reenteredPassword)) {
			msg = "Your entered password doesn't match with the reentered password! Try again.";
			throw new PasswordMismatchException(msg);
		}
		
		if (SignupDao.signupUser(user) != 1) {
			throw new Exception();
		}
	}
		
	private boolean isUsernameAlreadyExists(String username) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = SignupDao.getUserByUsername(username);
		return resultSet.next();
	}
		
	private boolean isUsernameTooLong(String username) {
		return username.length() > 20;
	}
		
	private boolean isPasswordInvalid(String password) {
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		return !password.matches(pattern);
	}
		
	private boolean isPasswordMismatch(String password, String reenteredPassword) {
		return !password.equals(reenteredPassword);
	}
}
