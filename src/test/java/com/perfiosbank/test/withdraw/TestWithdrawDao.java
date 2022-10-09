package com.perfiosbank.test.withdraw;

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
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DateTimeUtils;
import com.perfiosbank.withdraw.WithdrawDao;

public class TestWithdrawDao {
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
		amount = 467.12;
		
		CheckBalanceService checkBalanceService = new CheckBalanceService();
		User enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test3@Services");
		double oldBalance = checkBalanceService.checkBalance(user, enteredDetails);
		oldBalance = Math.round(oldBalance * 100) / 100;
		newBalance = oldBalance - amount;
	}
	
	@AfterEach
	public void tearDown() {
		username = null;
		date = null;
		amount = 0.0;
		newBalance = 0.0;
	}
	
	@Test
	public void withdrawMoney() {
		try {
			int rowsAffected = WithdrawDao.withdrawMoney(username, date, amount, newBalance);
			assertEquals(1, rowsAffected);

			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double newBalanceFromDb = resultSet.getDouble(1);
			assertEquals(newBalanceFromDb, newBalance);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have withdrawn the amount from user's account!");
		}
	}
}
