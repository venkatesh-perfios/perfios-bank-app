package com.perfiosbank.test.transfer;

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
import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.InsufficientBalanceException;
import com.perfiosbank.exceptions.TargetAccountNumberSameAsUserException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.TransferInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.transfer.TransferDao;
import com.perfiosbank.transfer.TransferService;

public class TestTransferService {
	static User user;
	static LoginService loginService;
	TransferService transferService;
	TransferInfo transferInfo;
	
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
		transferService = new TransferService();
		transferInfo = new TransferInfo();
		transferInfo.setUsername(user.getUsername());
		transferInfo.setPassword("Test3@Services");
		transferInfo.setTargetAccountNumber("PBIN1000000002");
		transferInfo.setAmount(762.16);
	}
	
	@AfterEach
	public void tearDown() {
		transferService = null;
		transferInfo = null;
	}
	
	@Test
	public void givenValidUserAndValidTransferInfo_testTransferMoney_shouldTransferMoney() {
		try {
			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double senderOldBalanceFromDb = resultSet.getDouble(1);

			resultSet = CheckBalanceDao.getCurrentBalanceByUsername("PB");
			resultSet.next();
			double receiverOldBalanceFromDb = resultSet.getDouble(1);
			
			transferService.transferMoney(user, transferInfo);
			
			resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double senderNewBalanceFromDb = resultSet.getDouble(1);
			double expectedSenderNewBalanceFromDb = Math.round((senderOldBalanceFromDb - transferInfo.getAmount()) * 100) / 100.0;

			assertEquals(expectedSenderNewBalanceFromDb, senderNewBalanceFromDb);
			
			resultSet = CheckBalanceDao.getCurrentBalanceByUsername("PB");
			resultSet.next();
			double receiverNewBalanceFromDb = resultSet.getDouble(1);
			double expectedReceiverNewBalanceFromDb = Math.round((receiverOldBalanceFromDb + transferInfo.getAmount()) * 100) / 100.0;
			
			assertEquals(expectedReceiverNewBalanceFromDb, receiverNewBalanceFromDb);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have transferred the money!");
		}
	}

	@Test
	public void givenInvalidUsername_testTransferMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			transferInfo.setUsername("wrongUsername");
			transferService.transferMoney(user, transferInfo);
		});
	}

	@Test
	public void givenInvalidPassword_testTransferMoney_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			transferInfo.setPassword("wrongPassword");
			transferService.transferMoney(user, transferInfo);
		});
	}

	@Test
	public void givenNonExistingTargetAccountNumber_testTransferMoney_shouldThrowAccountNotFoundException() {
		assertThrows(AccountNotFoundException.class, () -> {
			transferInfo.setTargetAccountNumber("NotAnAccountNumber");
			transferService.transferMoney(user, transferInfo);
		});
	}

	@Test
	public void givenUserAccountNumberAsTarget_testTransferMoney_shouldTargetAccountNumberSameAsUserException() {
		assertThrows(TargetAccountNumberSameAsUserException.class, () -> {
			ResultSet resultSet = TransferDao.getAccountByUsername(user.getUsername());
			resultSet.next();
			transferInfo.setTargetAccountNumber(resultSet.getString("Account_Number"));
			transferService.transferMoney(user, transferInfo);
		});
	}
	
	@Test
	public void givenNegativeAmount_testTransferMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			transferInfo.setAmount(-5164.46);
			transferService.transferMoney(user, transferInfo);
		});
	}

	@Test
	public void givenAmountWithTooManyDigitsAfterDecimal_testTransferMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			transferInfo.setAmount(5164.46534);
			transferService.transferMoney(user, transferInfo);
		});
	}

	@Test
	public void givenAmountEqualToZero_testTransferMoney_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			transferInfo.setAmount(0);
			transferService.transferMoney(user, transferInfo);
		});
	}

	@Test
	public void givenAmountGreaterThanTenLakh_testTransferMoney_shouldThrowAmountLimitReachedException() {
		assertThrows(AmountLimitReachedException.class, () -> {
			transferInfo.setAmount(2431560);
			transferService.transferMoney(user, transferInfo);
		});
	}

	@Test
	public void givenAmountGreaterThanBalance_testTransferMoney_shouldThrowInsufficientBalanceException() {
		assertThrows(InsufficientBalanceException.class, () -> {
			transferInfo.setAmount(999999);
			transferService.transferMoney(user, transferInfo);
		});
	}
	
	@Test
	public void givenAmountWhichBringsBalanceBelowThousand_testTransferMoney_shouldThrowBelowMinBalanceException() {
		assertThrows(BelowMinBalanceException.class, () -> {
			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(user.getUsername());
			resultSet.next();
			double oldBalanceFromDb = resultSet.getDouble(1);
			oldBalanceFromDb = Math.round(oldBalanceFromDb * 100) / 100;
			
			transferInfo.setAmount(Math.min(oldBalanceFromDb, 1000000) - 1);
			transferService.transferMoney(user, transferInfo);
		});
	}
}
