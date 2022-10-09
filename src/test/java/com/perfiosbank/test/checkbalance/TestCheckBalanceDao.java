package com.perfiosbank.test.checkbalance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;

public class TestCheckBalanceDao {
	static User user;
	static LoginService loginService;
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
	public void setUp() {
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test3@Services");
	}
	
	@AfterEach
	public void tearDown() {
		enteredDetails = null;
	}
	
	@Test
	public void givenValidUsername_testGetCurrentBalanceByUsername_shouldReturnCurrentBalance() {
		try {
			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(enteredDetails.getUsername());
			assertTrue(resultSet.next());
			assertTrue(resultSet.getDouble(1) >= 1000.0);
			assertFalse(resultSet.next());
		} catch(Exception e) {
			fail("Should have returned the current balance!");
			e.printStackTrace();
		}
	}
}
