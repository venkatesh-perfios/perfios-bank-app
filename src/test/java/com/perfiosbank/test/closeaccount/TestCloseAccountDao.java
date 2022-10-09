package com.perfiosbank.test.closeaccount;

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

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.signup.SignupDao;

public class TestCloseAccountDao {
	static User user;
	User enteredDetails;
	String username;
	
	@BeforeAll
	public static void setUpOnce() throws IOException {
		user = new User();
		user.setUsername("TestCloseAccount");
		user.setPassword("Test5@CloseAccount");
		try {
			SignupDao.signupUser(user);
			User userInSession = new LoginService().loginUser(user);
			user.setPassword(userInSession.getPassword());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have signed up the user!");
		}
	}
	
	@AfterAll
	public static void tearDownOnce() {
		user = null;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test5@CloseAccount");
		username = user.getUsername();
	}
	
	@AfterEach
	public void tearDown() {
		enteredDetails = null;
		username = null;
	}
	
	@Test
	public void givenValidUser_testRemovedUser_shouldRemoveUser() {
		try {
			int rowsAffected = CloseAccountDao.removeUser(enteredDetails);
			assertEquals(1, rowsAffected);
			
			ResultSet resultSet = SignupDao.getUserByUsername(enteredDetails.getUsername());
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have removed the user!");
		}
	}

	@Test
	public void givenValidUsername_testGetNumberOfApprovedLoans_shouldReturnNumberOfApprovedLoans() {
		try {
			int numberOfApprovedLoans = CloseAccountDao.getNumberOfApprovedLoans(username);
			assertTrue(numberOfApprovedLoans >= 0);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the number of approved loans for the user!");
		}
	}
}
