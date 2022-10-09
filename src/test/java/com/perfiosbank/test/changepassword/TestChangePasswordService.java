package com.perfiosbank.test.changepassword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.changepassword.ChangePasswordDao;
import com.perfiosbank.changepassword.ChangePasswordService;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.NewPasswordSameAsCurrentException;
import com.perfiosbank.exceptions.PasswordInvalidException;
import com.perfiosbank.exceptions.PasswordMismatchException;
import com.perfiosbank.login.LoginDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;

public class TestChangePasswordService {
	static User user;
	static LoginService loginService;
	ChangePasswordService changePasswordService;
	User enteredDetails;
	String newPassword;
	String reenteredPassword;
	
	@BeforeAll
	public static void setUpOnce() throws Exception {
		user = new User();
		user.setUsername("TestServices");
		user.setPassword("Test3@Services");
		
		loginService = new LoginService();
		try {
			User userInSession = loginService.loginUser(user);
			user.setPassword(userInSession.getPassword());
		} catch(Exception e) {
			fail("Should have logged in the user!");
			e.printStackTrace();
		}
	}
	
	@AfterAll
	public static void tearDownOnce() {
		user = null;
		loginService = null;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		changePasswordService = new ChangePasswordService();
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test3@Services");
		newPassword = "Test3@Services2.0";
		reenteredPassword = newPassword;
	}
	
	@AfterEach
	public void tearDown() {
		changePasswordService = null;
		enteredDetails = null;
		newPassword = null;
		reenteredPassword = null;
	}
	
	@Test
	public void givenValidUserValidNewPasswordAndValidReenteredPassword_testChangePassword_shouldChangePassword() {
		try {
			User updatedUserInSession = changePasswordService.changePassword(user, enteredDetails, newPassword, reenteredPassword);

			ResultSet resultSet = LoginDao.getPasswordByUsername(enteredDetails.getUsername());
			resultSet.next();
			String newEncryptedPasswordFromDb = resultSet.getString(1);
			assertEquals(newEncryptedPasswordFromDb, updatedUserInSession.getPassword());
			
			ChangePasswordDao.changePassword(user.getPassword(), user);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have changed user's current password to the new password!");
		}
	}
	
	@Test
	public void givenInvalidUsername_testChangePassword_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setUsername("wrongUsername");
			changePasswordService.changePassword(user, enteredDetails, newPassword, reenteredPassword);
		});
	}

	@Test
	public void givenInvalidCurrentPassword_testChangePassword_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setPassword("wrongPassword");
			changePasswordService.changePassword(user, enteredDetails, newPassword, reenteredPassword);
		});
	}

	@Test
	public void givenNewPasswordSameAsCurrentPassword_testChangePassword_shouldNewPasswordSameAsCurrentException() {
		assertThrows(NewPasswordSameAsCurrentException.class, () -> {
			newPassword = "Test3@Services";
			reenteredPassword = newPassword;
			changePasswordService.changePassword(user, enteredDetails, newPassword, reenteredPassword);
		});
	}

	@Test
	public void givenInvalidNewPassword_testChangePassword_shouldThrowPasswordInvalidException() {
		assertThrows(PasswordInvalidException.class, () -> {
			newPassword = "invalidPassword";
			reenteredPassword = newPassword;
			changePasswordService.changePassword(user, enteredDetails, newPassword, reenteredPassword);
		});
	}

	@Test
	public void givenInvalidReenteredPassword_testChangePassword_shouldThrowPasswordMismatchException() {
		assertThrows(PasswordMismatchException.class, () -> {
			reenteredPassword = "mismatchingPassword";
			changePasswordService.changePassword(user, enteredDetails, newPassword, reenteredPassword);
		});
	}
}
