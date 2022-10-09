package com.perfiosbank.test.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.transfer.TransferDao;

public class TestTransferDao {
	static User user;
	static LoginService loginService;
	String targetAccountNumber;
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
		targetAccountNumber = "PBIN1000000001";
		username = user.getUsername();
	}
	
	@AfterEach
	public void tearDown() {
		targetAccountNumber = null;
		username = null;
	}
	
	@Test
	public void givenExistingTargetAccountNumber_testGetAccountByAccountNumber_shouldReturnAccount() {
		try {
			ResultSet resultSet = TransferDao.getAccountByAccountNumber(targetAccountNumber);
			assertTrue(resultSet.next());
			assertEquals(targetAccountNumber, resultSet.getString("Account_Number"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the corresponding account!");
		}
	}

	@Test
	public void givenNonExistingTargetAccountNumber_testGetAccountByAccountNumber_shouldReturnNothing() {
		try {
			targetAccountNumber = "NotAnAccountNumber";
			ResultSet resultSet = TransferDao.getAccountByAccountNumber(targetAccountNumber);
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned nothing!");
		}
	}

	@Test
	public void givenUsername_testGetAccountByUsername_shouldReturnAccount() {
		try {
			ResultSet resultSet = TransferDao.getAccountByUsername(username);
			assertTrue(resultSet.next());
			assertEquals(username, resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the corresponding account!");
		}
	}
}
