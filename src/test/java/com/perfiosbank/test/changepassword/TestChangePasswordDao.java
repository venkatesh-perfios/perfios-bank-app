package com.perfiosbank.test.changepassword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.changepassword.ChangePasswordDao;
import com.perfiosbank.login.LoginDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;

public class TestChangePasswordDao {
	static User user;
	static LoginService loginService;
	String encryptedNewPassword;
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
		enteredDetails.setPassword("Test3@Services2.0");
		encryptedNewPassword = new StrongPasswordEncryptor().encryptPassword(enteredDetails.getPassword());
	}
	
	@AfterEach
	public void tearDown() {
		enteredDetails = null;
		encryptedNewPassword = null;
	}
	
	@Test
	public void givenValidEncryptedPasswordAndValidUser_testChangePassword_shouldChangePassword() {
		try {
			int rowsAffected = ChangePasswordDao.changePassword(encryptedNewPassword, enteredDetails);
			assertEquals(1, rowsAffected);
			
			ResultSet resultSet = LoginDao.getPasswordByUsername(enteredDetails.getUsername());
			resultSet.next();
			String newEncryptedPasswordFromDb = resultSet.getString(1);
			assertEquals(newEncryptedPasswordFromDb, encryptedNewPassword);
			
			ChangePasswordDao.changePassword(user.getPassword(), enteredDetails);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have changed user's current password to the new password!");
		}
	}
}
