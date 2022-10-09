package com.perfiosbank.test.signup;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.exceptions.PasswordInvalidException;
import com.perfiosbank.exceptions.PasswordMismatchException;
import com.perfiosbank.exceptions.UsernameAlreadyExistsException;
import com.perfiosbank.exceptions.UsernameTooLongException;
import com.perfiosbank.model.User;
import com.perfiosbank.signup.SignupDao;
import com.perfiosbank.signup.SignupService;

public class TestSignupService {
	SignupService signupService;
	User user;
	String reenteredPassword;
	
	@BeforeEach
	public void setUp() {
		signupService = new SignupService();
		user = new User();
		user.setUsername("TestUser");
		user.setPassword("TestUser@Everywhere1.0");
		reenteredPassword = user.getPassword();
	}
	
	@AfterEach
	public void tearDown() {
		try {
			CloseAccountDao.removeUser(user);
		} catch (Exception e) {
			fail("Should have removed user!");
			e.printStackTrace();
		}
		user = null;
		reenteredPassword = null;
	}
	
	@Test
	public void givenValidUserAndValidReenteredPassword_testSignupUser_shouldSignupUser() {
		try {
			signupService.signupUser(user, reenteredPassword);
			ResultSet resultSet = SignupDao.getUserByUsername(user.getUsername());
			assertTrue(resultSet.next());
			assertFalse(resultSet.next());
		} catch(Exception e) {
			fail("Should have signed up the user!");
			e.printStackTrace();
		}
	}

	@Test
	public void givenExistingUsername_testSignupUser_shouldThrowUsernameAlreadyExistsException() {
		assertThrows(UsernameAlreadyExistsException.class, () -> {
			signupService.signupUser(user, reenteredPassword);
			signupService.signupUser(user, reenteredPassword);
		});
	}

	@Test
	public void givenVeryLongUsername_testSignupUser_shouldThrowUsernameTooLongExceptionException() {
		assertThrows(UsernameTooLongException.class, () -> {
			user.setUsername("ThisIsAVeryLongUsername");
			signupService.signupUser(user, reenteredPassword);
		});
	}

	@Test
	public void givenInvalidPassword_testSignupUser_shouldThrowPasswordInvalidException() {
		assertThrows(PasswordInvalidException.class, () -> {
			user.setPassword("invalidPassword");
			signupService.signupUser(user, reenteredPassword);
		});
	}

	@Test
	public void givenInvalidReenteredPassword_testSignupUser_shouldThrowPasswordMismatchException() {
		assertThrows(PasswordMismatchException.class, () -> {
			reenteredPassword = "mismatchingPassword";
			signupService.signupUser(user, reenteredPassword);
		});
	}
}
