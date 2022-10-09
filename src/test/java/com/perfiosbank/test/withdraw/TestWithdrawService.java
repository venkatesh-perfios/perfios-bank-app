package com.perfiosbank.test.withdraw;

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
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
//import com.perfiosbank.exceptions.BelowMinBalanceException;
//import com.perfiosbank.exceptions.InsufficientBalanceException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.DepositWithdrawInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.withdraw.WithdrawService;

public class TestWithdrawService {
	static User user;
	static LoginService loginService;
	WithdrawService withdrawService;
	DepositWithdrawInfo withdrawInfo;
	
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
		withdrawService = new WithdrawService();
		withdrawInfo = new DepositWithdrawInfo();
		withdrawInfo.setUsername(user.getUsername());
		withdrawInfo.setPassword("Test3@Services");
		withdrawInfo.setAmount(931.48);
	}
	
	@AfterEach
	public void tearDown() {
		withdrawService = null;
		withdrawInfo = null;
	}

	@Test
	public void givenValidUserAndValidWithdrawInfo_testWithdrawMoney_shouldWithdrawMoney() {
		try {
			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double oldBalanceFromDb = resultSet.getDouble(1);
			
			withdrawService.withdrawMoney(user, withdrawInfo);

			resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double newBalanceFromDb = resultSet.getDouble(1);
			newBalanceFromDb = Math.round(newBalanceFromDb * 100) / 100;
			double newBalanceExpected = Math.round((oldBalanceFromDb - withdrawInfo.getAmount()) * 100) / 100;
			
			assertEquals(newBalanceExpected, newBalanceFromDb);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have deposited the amount into user's account!");
		}
	}

	@Test
	public void givenInvalidUsername_testWithdrawMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			withdrawInfo.setUsername("wrongUsername");
			withdrawService.withdrawMoney(user, withdrawInfo);
		});
	}

	@Test
	public void givenInvalidPassword_testWithdrawMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			withdrawInfo.setPassword("wrongPassword");
			withdrawService.withdrawMoney(user, withdrawInfo);
		});
	}

	@Test
	public void givenNegativeAmount_testWithdrawMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			withdrawInfo.setAmount(-5164.46);
			withdrawService.withdrawMoney(user, withdrawInfo);
		});
	}

	@Test
	public void givenAmountWithTooManyDigitsAfterDecimal_testWithdrawMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			withdrawInfo.setAmount(5164.46534);
			withdrawService.withdrawMoney(user, withdrawInfo);
		});
	}

	@Test
	public void givenAmountEqualToZero_testWithdrawMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			withdrawInfo.setAmount(0);
			withdrawService.withdrawMoney(user, withdrawInfo);
		});
	}

	@Test
	public void givenAmountGreaterThanTenThousand_testWithdrawMoney_shouldThrowAmountLimitReachedException() {
		assertThrows(AmountLimitReachedException.class, () -> {
			withdrawInfo.setAmount(12535);
			withdrawService.withdrawMoney(user, withdrawInfo);
		});
	}
	
//	@Test
//	public void givenAmountGreaterThanBalance_testWithdrawMoney_shouldThrowInsufficientBalanceException() {
//		assertThrows(InsufficientBalanceException.class, () -> {
//			withdrawInfo.setAmount(9999);
//			for (int i = 0; i < 10; ++i) {
//				withdrawService.withdrawMoney(user, withdrawInfo);
//			}
//		});
//	}
//	
//	@Test
//	public void givenAmountWhichBringsBalanceBelowThousand_testWithdrawMoney_shouldThrowBelowMinBalanceException() {
//		assertThrows(BelowMinBalanceException.class, () -> {
//			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
//			resultSet.next();
//			double oldBalanceFromDb = resultSet.getDouble(1);
//			oldBalanceFromDb = Math.round(oldBalanceFromDb * 100) / 100;
//			
//			for (int i = 0; i < 10; ++i) {
//				withdrawInfo.setAmount(Math.min(oldBalanceFromDb, 10000) - 1);
//				withdrawService.withdrawMoney(user, withdrawInfo);
//			}
//		});
//	}
}
