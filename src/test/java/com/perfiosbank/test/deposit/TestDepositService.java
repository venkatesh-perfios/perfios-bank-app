package com.perfiosbank.test.deposit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.deposit.DepositService;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.DepositWithdrawInfo;
import com.perfiosbank.model.User;

public class TestDepositService {
	static User user;
	static LoginService loginService;
	DepositService depositService;
	DepositWithdrawInfo depositInfo;
	
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
		depositService = new DepositService();
		depositInfo = new DepositWithdrawInfo();
		depositInfo.setUsername(user.getUsername());
		depositInfo.setPassword("Test3@Services");
		depositInfo.setAmount(50000);
	}
	
	@AfterEach
	public void tearDown() {
		depositService = null;
		depositInfo = null;
	}
	
	@Test
	public void givenValidUserAndValidDepositInfo_testDepositMoney_shouldDepositMoney() {
		try {
			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double oldBalanceFromDb = resultSet.getDouble(1);
			
			depositService.depositMoney(user, depositInfo);

			resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double newBalanceFromDb = resultSet.getDouble(1);
			newBalanceFromDb = Math.round(newBalanceFromDb * 100) / 100.0;
			double newBalanceExpected = Math.round((oldBalanceFromDb + depositInfo.getAmount()) * 100) / 100.0;
			
			assertEquals(newBalanceExpected, newBalanceFromDb);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have deposited the amount into user's account!");
		}
	}

	@Test
	public void givenInvalidUsername_testDepositMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			depositInfo.setUsername("wrongUsername");
			depositService.depositMoney(user, depositInfo);
		});
	}

	@Test
	public void givenInvalidPassword_testDepositMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			depositInfo.setPassword("wrongPassword");
			depositService.depositMoney(user, depositInfo);
		});
	}

	@Test
	public void givenNegativeAmount_testDepositMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			depositInfo.setAmount(-5164.46);
			depositService.depositMoney(user, depositInfo);
		});
	}

	@Test
	public void givenAmountWithTooManyDigitsAfterDecimal_testDepositMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			depositInfo.setAmount(5164.46534);
			depositService.depositMoney(user, depositInfo);
		});
	}

	@Test
	public void givenAmountEqualToZero_testDepositMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			depositInfo.setAmount(0);
			depositService.depositMoney(user, depositInfo);
		});
	}

	@Test
	public void givenAmountGreaterThanFiftyThousand_testDepositMoney_shouldThrowAmountLimitReachedException() {
		assertThrows(AmountLimitReachedException.class, () -> {
			depositInfo.setAmount(95413);
			depositService.depositMoney(user, depositInfo);
		});
	}
}
