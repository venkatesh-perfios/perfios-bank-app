package com.perfiosbank.test.pasttransactions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.pasttransactions.PastTransactionsDao;

public class TestPastTransactionsDao {
	static User user;
	static LoginService loginService;
	String username;
	
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
		username = user.getUsername();
	}
	
	@AfterEach
	public void tearDown() {
		username = null;
	}

	@Test
	public void givenValidUsername_testPastTransactionsByUsername_shouldReturnPastTransactions() {
		try {
			ResultSet resultSet = PastTransactionsDao.getPastTransactionsByUsername(username);
			while (resultSet.next()) {
				assertEquals(username, resultSet.getString(5));
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the user's past transactions!");
		}
	}
}
