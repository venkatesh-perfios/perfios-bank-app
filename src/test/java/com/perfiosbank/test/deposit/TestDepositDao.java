package com.perfiosbank.test.deposit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.checkbalance.CheckBalanceService;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DateTimeUtils;

public class TestDepositDao {
	static User user;
	static LoginService loginService;
	String username;
	String date;
	double amount;
	double newBalance;
	
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
		date = DateTimeUtils.getCurrentDateTime();
		amount = 4831.43;
		
		CheckBalanceService checkBalanceService = new CheckBalanceService();
		User enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test3@Services");
		double oldBalance = checkBalanceService.checkBalance(user, enteredDetails);
		newBalance = oldBalance + amount;
		newBalance = Math.round(newBalance * 100) / 100.0;
	}
	
	@AfterEach
	public void tearDown() {
		username = null;
		date = null;
		amount = 0.0;
		newBalance = 0.0;
	}
	
	@Test
	public void givenValidUsernameValidDateValidAmountAndValidNewBalance_testDepositMoney_shouldDepositMoney() {
		try {
			int rowsAffected = DepositDao.depositMoney(username, date, amount, newBalance);
			assertEquals(1, rowsAffected);

			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double newBalanceFromDb = resultSet.getDouble(1);
			newBalanceFromDb = Math.round(newBalanceFromDb * 100) / 100.0;
			
			assertEquals(newBalanceFromDb, newBalance);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have deposited the amount into user's account!");
		}
	}
}
