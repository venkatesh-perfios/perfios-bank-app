package com.perfiosbank.changepassword;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.PasswordMismatchException;
import com.perfiosbank.exceptions.PasswordInvalidException;
import com.perfiosbank.exceptions.NewPasswordSameAsCurrentException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AuthenticationUtils;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class ChangePasswordService {
    public User changePassword(User userInSession, User enteredDetails, String newPassword, String reenteredNewPassword) 
    		throws NewPasswordSameAsCurrentException, PasswordInvalidException, 
    		PasswordMismatchException, AuthenticationFailedException, Exception {
        String msg;

        if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
            msg = "Authentication failed! Please re-check your username/password.";
            throw new AuthenticationFailedException(msg);
        }
        
        if (isNewPasswordSameAsCurrent(newPassword, enteredDetails.getPassword())) {
        	msg = "Your new password is the same as the current password!";
        	throw new NewPasswordSameAsCurrentException(msg);
        }

        if (isPasswordInvalid(newPassword)) {
            msg = "Please ensure your password satisfies the following criteria:<br>" +
                    "1. Has at least 8 characters<br>" +
                    "2. Contains at least 1 digit<br>" +
                    "3. Contains at least 1 small letter<br>" +
                    "4. Contains at least 1 capital letter<br>" +
                    "5. Contains at least 1 of these special characters: @, #, $, %, ^, &, +, =";
            throw new PasswordInvalidException(msg);
        }

        if (isPasswordMismatch(newPassword, reenteredNewPassword)) {
            msg = "Your password doesn't match with the reentered password! Try again.";
            throw new PasswordMismatchException(msg);
        }

        String encryptedNewPassword = encryptPassword(newPassword);
        if (ChangePasswordDao.changePassword(encryptedNewPassword, userInSession) != 1) {
        	throw new Exception();
        }

        User updatedUserInSession = new User();
        updatedUserInSession.setUsername(userInSession.getUsername());
        updatedUserInSession.setPassword(encryptedNewPassword);
        
        return updatedUserInSession;
    }

    private boolean isNewPasswordSameAsCurrent(String newPassword, String currentPassword) {
    	return newPassword.equals(currentPassword);
    }
    
    private boolean isPasswordInvalid(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return !password.matches(pattern);
    }

    private boolean isPasswordMismatch(String password, String reenteredPassword) {
        return !password.equals(reenteredPassword);
    }

    private String encryptPassword(String password) {
        return new StrongPasswordEncryptor().encryptPassword(password);
    }
}
