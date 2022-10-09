package com.perfiosbank.test.login;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.login.LoginDao;
import com.perfiosbank.model.User;
import com.perfiosbank.signup.SignupDao;

public class TestLoginDao {
	User testUserForLoginSignup;
	User testUserForAccounts;
	
	@BeforeEach
	public void setUp() {
		String testUsernameForLoginSignup = "TestSignupLogin";
		String testPasswordForLoginSignup = "Test1@SignupLogin";
		String testUsernameForAccounts = "TestServices";
		String testPasswordForAccounts = "Test3@Services";

		testUserForLoginSignup = new User();
		testUserForLoginSignup.setUsername(testUsernameForLoginSignup);
		testUserForLoginSignup.setPassword(testPasswordForLoginSignup);
		
		testUserForAccounts = new User();
		testUserForAccounts.setUsername(testUsernameForAccounts);
		testUserForAccounts.setPassword(testPasswordForAccounts);
	}
	
	@AfterEach
	public void setDown() {
		try {
			CloseAccountDao.removeUser(testUserForLoginSignup);
		} catch (Exception e) {
			fail("Should have removed user!");
			e.printStackTrace();
		}
		testUserForLoginSignup = null;
	}
	
	@Test
	public void givenExistingUsername_testGetPasswordByUsername_shouldReturnPassword() {
		try {
			SignupDao.signupUser(testUserForLoginSignup);
			ResultSet resultSet = LoginDao.getPasswordByUsername(testUserForLoginSignup.getUsername());
			assertTrue(resultSet.next());
			assertFalse(resultSet.next());
		} catch(Exception e) {
			fail("Should have returned a password!");
			e.printStackTrace();
		}
	}

	@Test
	public void givenNonExistingUsername_testGetPasswordByUsername_shouldReturnNothing() {
		try {
			ResultSet resultSet = LoginDao.getPasswordByUsername(testUserForLoginSignup.getUsername());
			assertFalse(resultSet.next());
		} catch(Exception e) {
			fail("Should have found no password!");
			e.printStackTrace();
		}
	}

	@Test
	public void givenExistingUsername_testGetAccountCountByUsername_shouldReturnOne() {
		try {
			ResultSet resultSet = LoginDao.getApprovedAccountCountByUsername(testUserForAccounts.getUsername());
			resultSet.next();
			int numberOfAccounts = resultSet.getInt(1);
			assertEquals(1, numberOfAccounts);
		} catch(Exception e) {
			fail("Should have returned 1!");
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenNonExistingUsername_testGetAccountCountByUsername_shouldReturnZero() {
		try {
			ResultSet resultSet = LoginDao.getApprovedAccountCountByUsername(testUserForLoginSignup.getUsername());
			resultSet.next();
			int numberOfAccounts = resultSet.getInt(1);
			assertEquals(0, numberOfAccounts);
		} catch(Exception e) {
			fail("Should have returned 0!");
			e.printStackTrace();
		}
	}
}
