package com.perfiosbank.test.closeaccount;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.closeaccount.CloseAccountService;
import com.perfiosbank.exceptions.ActiveFixedDepositAccountsFoundException;
import com.perfiosbank.exceptions.ActiveLoansFoundException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.signup.SignupDao;

public class TestCloseAccountService {
	static User user;
	CloseAccountService closeAccountService;
	User enteredDetails;
	
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
		closeAccountService = new CloseAccountService();
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test5@CloseAccount");
	}
	
	@AfterEach
	public void tearDown() {
		enteredDetails = null;
	}
	
	@Test
	public void givenInvalidUsername_testCloseAccount_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setUsername("wrongUsername");
			closeAccountService.closeAccount(user, enteredDetails);
		});
	}

	@Test
	public void givenInvalidPassword_testCloseAccount_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setPassword("wrongPassword");
			closeAccountService.closeAccount(user, enteredDetails);
		});
	}

	@Test
	public void givenUserWithActiveFixedDepositAccounts_testCloseAccount_shouldThrowActiveFixedDepositAccountsFoundException() {
		assertThrows(ActiveFixedDepositAccountsFoundException.class, () -> {
			enteredDetails.setUsername("finalz");
			enteredDetails.setPassword("venky@2K");
			user.setUsername(enteredDetails.getUsername());
			user.setPassword(new StrongPasswordEncryptor().encryptPassword(enteredDetails.getPassword()));
			closeAccountService.closeAccount(user, enteredDetails);
		});
	}
	
	@Test
	public void givenUserWithActiveLoans_testCloseAccount_shouldThrowActiveLoansFoundException() {
		assertThrows(ActiveLoansFoundException.class, () -> {
			enteredDetails.setUsername("final");
			enteredDetails.setPassword("venky@2K");
			user.setUsername(enteredDetails.getUsername());
			user.setPassword(new StrongPasswordEncryptor().encryptPassword(enteredDetails.getPassword()));
			closeAccountService.closeAccount(user, enteredDetails);
		});
	}

	@Test
	public void givenValidUser_testCloseAccount_shouldRemoveUser() {
		try {
			user.setUsername(enteredDetails.getUsername());
			user.setPassword(new StrongPasswordEncryptor().encryptPassword(enteredDetails.getPassword()));
			closeAccountService.closeAccount(user, enteredDetails);
			
			ResultSet resultSet = SignupDao.getUserByUsername(enteredDetails.getUsername());
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have removed the user!");
		}
	}
}

