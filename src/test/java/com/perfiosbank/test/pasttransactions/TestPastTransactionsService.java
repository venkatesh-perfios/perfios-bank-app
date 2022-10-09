package com.perfiosbank.test.pasttransactions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.pasttransactions.PastTransactionsService;

public class TestPastTransactionsService {
	static User user;
	static LoginService loginService;
	PastTransactionsService pastTransactionsService;
	User enteredDetails;
	
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
	public void setUp() throws Exception {
		pastTransactionsService = new PastTransactionsService();
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test3@Services");
	}
	
	@AfterEach
	public void tearDown() {
		pastTransactionsService = null;
		enteredDetails = null;
	}

	@Test
	public void givenValidUser_testViewPastTransactions_shouldReturnPastTransactions() {
		try {
			ResultSet resultSet = pastTransactionsService.viewPastTransactions(user, enteredDetails);
			while (resultSet.next()) {
				assertEquals(enteredDetails.getUsername(), resultSet.getString(5));
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the user's past transactions!");
		}
	}

	@Test
	public void givenInvalidUsername_testPastTransactionsMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setUsername("wrongUsername");
			pastTransactionsService.viewPastTransactions(user, enteredDetails);
		});
	}

	@Test
	public void givenInvalidPassword_testPastTransactionsMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setPassword("wrongPassword");
			pastTransactionsService.viewPastTransactions(user, enteredDetails);
		});
	}

}
