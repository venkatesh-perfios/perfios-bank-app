package com.perfiosbank.test.checkbalance;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.checkbalance.CheckBalanceService;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;

public class TestCheckBalanceService {
	static User user;
	static LoginService loginService;
	User enteredDetails;
	CheckBalanceService checkBalanceService;
	
	@BeforeAll
	public static void setUpOnce() throws IOException {
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
	public void setUp() {
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test3@Services");
		checkBalanceService = new CheckBalanceService();
	}
	
	@AfterEach
	public void tearDown() {
		enteredDetails = null;
		checkBalanceService = null;
	}
	
	@Test
	public void givenValidUser_testCheckBalance_shouldReturnCurrentBalance() {
		try {
			double currentBalance = checkBalanceService.checkBalance(user, enteredDetails);
			assertTrue(currentBalance >= 1000.0);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the user's current balance!");
		}
	}

	@Test
	public void givenInvalidUsername_testCheckBalance_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setUsername("wrongUsername");
			checkBalanceService.checkBalance(user, enteredDetails);
		});
	}

	@Test
	public void givenInvalidPassword_testCheckBalance_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setPassword("wrongPassword");
			checkBalanceService.checkBalance(user, enteredDetails);
		});
	}
}
