package com.perfiosbank.test.fixeddeposit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountRangeException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.DurationRangeException;
import com.perfiosbank.exceptions.EndDateInvalidException;
import com.perfiosbank.fixeddeposit.FixedDepositDao;
import com.perfiosbank.fixeddeposit.FixedDepositService;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.FixedDepositInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;

public class TestFixedDepositService {
	static User user;
	static LoginService loginService;
	FixedDepositService fixedDepositService;
	FixedDepositInfo fixedDepositInfo;
	
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
		try {
			String deleteFixedDepositAccountsByUsername = "delete from Fixed_Deposits where Username='" + user.getUsername() + "'";
			Statement statement = DatabaseUtils.getConnection().createStatement();
			statement.executeUpdate(deleteFixedDepositAccountsByUsername);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have closed the user's FD accounts!");
		}
		user = null;
		loginService = null;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		fixedDepositService = new FixedDepositService();
		fixedDepositInfo = new FixedDepositInfo();
		fixedDepositInfo.setUsername(user.getUsername());
		fixedDepositInfo.setPassword("Test3@Services");
		fixedDepositInfo.setPrincipal(7642.16);
		fixedDepositInfo.setEndDate("2023-10-20");
		fixedDepositInfo.setInterestRate(5.50);
		fixedDepositInfo.setMaturityAmount(2846.63);
	}
	
	@AfterEach
	public void tearDown() {
		fixedDepositService = null;
		fixedDepositInfo = null;
	}
	
	@Test
	public void givenValidUserAndValidFixedFixedDeposit_testStartFixedFixedDeposit_shouldOpenFixedFixedDepositAccount() {
		try {
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);

			ResultSet resultSet = FixedDepositDao.getAllFixedDepositAccountsByUsername(fixedDepositInfo.getUsername());
			assertTrue(resultSet.next());
			assertEquals(fixedDepositInfo.getUsername(), resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have opened the user's FD account!");
		}
	}
	
	@Test
	public void givenInvalidUsername_testStartFixedDeposit_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			fixedDepositInfo.setUsername("wrongUsername");
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenInvalidPassword_testStartFixedDeposit_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			fixedDepositInfo.setPassword("wrongPassword");
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenNegativeAmount_testStartFixedDeposit_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			fixedDepositInfo.setPrincipal(-5164.46);
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenAmountWithTooManyDigitsAfterDecimal_testStartFixedDeposit_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			fixedDepositInfo.setPrincipal(5164.46534);
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenAmountEqualToZero_testStartFixedDeposit_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			fixedDepositInfo.setPrincipal(0);
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenAmountLesserThanThousand_testStartFixedDeposit_shouldThrowAmountRangeException() {
		assertThrows(AmountRangeException.class, () -> {
			fixedDepositInfo.setPrincipal(946.48);
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenAmountGreaterThanOneLakh_testStartFixedDeposit_shouldThrowAmountRangeException() {
		assertThrows(AmountRangeException.class, () -> {
			fixedDepositInfo.setPrincipal(642513.19);
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenInvalidYear_testStartFixedDeposit_shouldThrowEndDateInvalidException() {
		assertThrows(EndDateInvalidException.class, () -> {
			fixedDepositInfo.setEndDate("202231-11-19");
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenExpiredEndDate_testStartFixedDeposit_shouldThrowDurationRangeException() {
		assertThrows(DurationRangeException.class, () -> {
			fixedDepositInfo.setEndDate("2022-09-23");
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}

	@Test
	public void givenTooLongDuration_testStartFixedDeposit_shouldThrowDurationRangeException() {
		assertThrows(DurationRangeException.class, () -> {
			fixedDepositInfo.setEndDate("2038-09-23");
			fixedDepositService.startFixedDeposit(user, fixedDepositInfo);
		});
	}
}
