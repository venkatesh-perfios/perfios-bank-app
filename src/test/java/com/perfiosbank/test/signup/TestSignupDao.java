package com.perfiosbank.test.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.model.User;
import com.perfiosbank.signup.SignupDao;

public class TestSignupDao {
	User user;
	
	@BeforeEach
	public void setUp() {
		user = new User();
		user.setUsername("TestUser");
		user.setPassword("TestUser@Everywhere1.0");
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
	}
	
	@Test
	public void givenValidUser_testSignupUser_shouldSignupUser() {
		try {
			int rowsAffected = SignupDao.signupUser(user);
			assertEquals(1, rowsAffected);
		} catch (Exception e) {
			fail("Should have signed up user!");
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenExistingUsername_testGetUserByUsername_shouldReturnUser() {
		try {
			SignupDao.signupUser(user);
			ResultSet resultSet = SignupDao.getUserByUsername(user.getUsername());
			assertTrue(resultSet.next());
			assertFalse(resultSet.next());
		} catch(Exception e) {
			fail("Should have found exactly 1 user!");
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenNonExistingUsername_testGetUserByUsername_shouldReturnNothing() {
		try {
			ResultSet resultSet = SignupDao.getUserByUsername(user.getUsername());
			assertFalse(resultSet.next());
		} catch(Exception e) {
			fail("Should have found no user!");
			e.printStackTrace();
		}
	}
}
