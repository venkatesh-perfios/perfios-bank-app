package com.perfiosbank.test.carloan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.carloan.CarLoanDao;
import com.perfiosbank.carloan.CarLoanService;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.FileInvalidException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.CarLoanInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;
import com.perfiosbank.utils.FileUtils;

public class TestCarLoanService {
	static User user;
	static LoginService loginService;
	CarLoanService carLoanService;
	CarLoanInfo carLoanInfo;
	
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
			String removeAllCarLoansByUsername = "delete from Car_Loans where Username='" + user.getUsername() + "'";
			Statement statement = DatabaseUtils.getConnection().createStatement();
			statement.executeUpdate(removeAllCarLoansByUsername);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have removed all of user's car loans!");
		}
		user = null;
		loginService = null;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		carLoanService = new CarLoanService();
		
		File file = new File("src/main/resources/linuxcommands.pdf");
		final String[] fileTypes = {"cibilReport", "identityProof", "addressProof", "incomeProof"};
		HashMap<String, byte[]> uploadedFiles = new HashMap<>();
		List<String> uploadedFilenames = new ArrayList<String>();
		for (String fileType : fileTypes) {
			uploadedFilenames.add(fileType + "," + file.getName());
			InputStream fileContent = new FileInputStream(file);
		    byte[] fileContentInBytes = FileUtils.readAllBytes(fileContent);
		    uploadedFiles.put(fileType, fileContentInBytes);
		}
		
		carLoanInfo = new CarLoanInfo();
		carLoanInfo.setUsername(user.getUsername());
		carLoanInfo.setPassword("Test3@Services");
		carLoanInfo.setLoanAmount(7614.36);
		carLoanInfo.setDays(1967);
		carLoanInfo.setCibilScore(736);
		carLoanInfo.setInterestRate(8.40);
		carLoanInfo.setDueAmount(16384.23);
		carLoanInfo.setUploadedFilenames(uploadedFilenames);
		carLoanInfo.setUploadedFiles(uploadedFiles);
	}
	
	@AfterEach
	public void tearDown() {
		carLoanService = null;
		carLoanInfo = null;
	}
	
	@Test
	public void givenValidUserAndValidCarLoanInfo_testApplyCarLoan_shouldApplyForCarLoan() {
		try {
			carLoanService.applyCarLoan(user, carLoanInfo);
			
			ResultSet resultSet = CarLoanDao.getAllCarLoansByUsername(carLoanInfo.getUsername());
			assertTrue(resultSet.next());
			assertEquals(carLoanInfo.getUsername(), resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have applied for car loan!");
		}
	}

	@Test
	public void givenInvalidUsername_testApplyCarLoan_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			carLoanInfo.setUsername("wrongUsername");
			carLoanService.applyCarLoan(user, carLoanInfo);
		});
	}

	@Test
	public void givenInvalidPassword_testApplyCarLoan_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			carLoanInfo.setPassword("wrongPassword");
			carLoanService.applyCarLoan(user, carLoanInfo);
		});
	}


	@Test
	public void givenNegativeAmount_testApplyCarLoan_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			carLoanInfo.setLoanAmount(-5164.46);
			carLoanService.applyCarLoan(user, carLoanInfo);
		});
	}

	@Test
	public void givenAmountWithTooManyDigitsAfterDecimal_testApplyCarLoan_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			carLoanInfo.setLoanAmount(5164.46534);
			carLoanService.applyCarLoan(user, carLoanInfo);
		});
	}

	@Test
	public void givenAmountEqualToZero_testApplyCarLoan_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			carLoanInfo.setLoanAmount(0);
			carLoanService.applyCarLoan(user, carLoanInfo);
		});
	}

	@Test
	public void givenNonPdfFile_testOpenAccount_shouldThrowFileInvalidException() {
		assertThrows(FileInvalidException.class, () -> {
			File file = new File("src/main/resources/Core_Java.txt");
			InputStream fileContent = new FileInputStream(file);
			
			List<String> uploadedFilenames = carLoanInfo.getUploadedFilenames();
			uploadedFilenames.remove(3);
			uploadedFilenames.add("incomeProof,Core_Java.txt");
			carLoanInfo.setUploadedFilenames(uploadedFilenames);
			
			HashMap<String, byte[]> uploadedFiles = carLoanInfo.getUploadedFiles();
			uploadedFiles.remove("incomeProof");
			uploadedFiles.put("incomeProof", FileUtils.readAllBytes(fileContent));
			carLoanInfo.setUploadedFiles(uploadedFiles);
			
			carLoanService.applyCarLoan(user, carLoanInfo);
		});
	}
}
