package com.perfiosbank.test.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.exceptions.AccountFrozenException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.signup.SignupService;

public class TestLoginService {
	static SignupService signupService;
	static User user;
	static String reenteredPassword;
	LoginService loginService;
	User enteredDetails;
	
	@BeforeAll
	public static void setUpOnce() {
		user = new User();
		user.setUsername("TestUser");
		user.setPassword("TestUser@Everywhere1.0");
		reenteredPassword = user.getPassword();
		
		signupService = new SignupService();
		try {
			signupService.signupUser(user, reenteredPassword);
		} catch(Exception e) {
			fail("Should have signed up the user!");
			e.printStackTrace();
		}
	}
	
	@AfterAll
	public static void tearDownOnce() {
		try {
			CloseAccountDao.removeUser(user);
		} catch (Exception e) {
			fail("Should have removed user!");
			e.printStackTrace();
		}
		user = null;
		reenteredPassword = null;
	}
	
	@BeforeEach
	public void setUp() {
		loginService = new LoginService();
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword(user.getPassword());
	}
	
	@AfterEach
	public void tearDown() {
		loginService = null;
		enteredDetails = null;
	}
	
	@Test
	public void givenValidUser_testLoginUser_shouldLoginUser() {
		try {
			User userInSession = loginService.loginUser(enteredDetails);
			assertEquals(enteredDetails.getUsername(), userInSession.getUsername());
		} catch(Exception e) {
			fail("Should have logged in the user!");
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenInvalidUsername_testLoginUser_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setUsername("wrongUsername");
			loginService.loginUser(enteredDetails);
		});
	}

	@Test
	public void givenInvalidPassword_testLoginUser_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setPassword("wrongPassword");
			loginService.loginUser(enteredDetails);
		});
	}

	@Test
	public void givenFrozenAccount_testLoginUser_shouldThrowAccountFrozenException() {
		assertThrows(AccountFrozenException.class, () -> {
			User testUserForFrozenAccount = new User();
			testUserForFrozenAccount.setUsername("TestFrozenAccount");
			testUserForFrozenAccount.setPassword("Test4@FrozenAccount");
			loginService.loginUser(testUserForFrozenAccount);
		});
	}
}
