package com.perfiosbank.test.openaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.AccountInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.openaccount.OpenAccountDao;
import com.perfiosbank.signup.SignupService;
import com.perfiosbank.utils.FileUtils;

public class TestOpenAccountDao {
	static SignupService signupService;
	static User user;
	static String reenteredPassword;
	LoginService loginService;
	String newAccountNumber;
	String username;
	AccountInfo accountInfo;
	
	@BeforeAll
	public static void setUpOnce() {
		user = new User();
		user.setUsername("TestOpenAccount");
		user.setPassword("Test2@OpenAccount");
		reenteredPassword = user.getPassword();
		
		signupService = new SignupService();
		try {
			signupService.signupUser(user, reenteredPassword);
			User userInSession = new LoginService().loginUser(user);
			user.setPassword(userInSession.getPassword());
		} catch(Exception e) {
			fail("Should have signed up the user!");
			e.printStackTrace();
		}
	}
	
	@AfterAll
	public static void tearDownOnce() {
		try {
			CloseAccountDao.removeUser(user);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have removed the user!");
		}
		user = null;
		reenteredPassword = null;
		signupService = null;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		newAccountNumber = "PBIN1000000100";
		username = user.getUsername();
		
		File file = new File("src/main/resources/linuxcommands.pdf");
		final String[] fileTypes = {"photo", "aadhaarProof", "panProof"};
		HashMap<String, byte[]> uploadedFiles = new HashMap<>();
		List<String> uploadedFilenames = new ArrayList<String>();
		for (String fileType : fileTypes) {
			uploadedFilenames.add(fileType + "," + file.getName());
			InputStream fileContent = new FileInputStream(file);
		    byte[] fileContentInBytes = FileUtils.readAllBytes(fileContent);
		    uploadedFiles.put(fileType, fileContentInBytes);
		}
		
		accountInfo = new AccountInfo();
		accountInfo.setFirstName("Venkatesh");
		accountInfo.setLastName("S");
		accountInfo.setAge(22);
		accountInfo.setAadhaar(576843012091L);
		accountInfo.setPan("CEGPS1926Z");
		accountInfo.setAddress("Bengaluru");
		accountInfo.setPhone(9382650714L);
		accountInfo.setAmount(10000.0);
		accountInfo.setUploadedFilenames(uploadedFilenames);
		accountInfo.setUploadedFiles(uploadedFiles);
	}
	
	@AfterEach
	public void tearDown() {
		newAccountNumber = null;
		username = null;
		accountInfo = null;
	}
	
	@Test
	public void givenValidAccountNumberValidUserAndValidAccount_testOpenAccount_shouldOpenAccount() {
		try {
			int rowsAffected = OpenAccountDao.openAccount(newAccountNumber, user, accountInfo);
			assertEquals(1, rowsAffected);
			
			ResultSet resultSet = OpenAccountDao.getPendingAccountByUsername(user.getUsername());
			assertTrue(resultSet.next());
			assertEquals(newAccountNumber, resultSet.getString("Account_Number"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have submitted the user's account opening application!");
		}
	}
	
	@Test
	public void testGetMaxAccountNumber_shouldReturnMaxAccountNumber() {
		try {
			ResultSet resultSet = OpenAccountDao.getMaxAccountNumber();
			assertTrue(resultSet.next());
			assertEquals(newAccountNumber, resultSet.getString(1));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the max account number!");
		}
	}

	@Test
	public void givenValidUsername_testGetAccountByUsername_shouldReturnAccount() {
		try {
			ResultSet resultSet = OpenAccountDao.getAccountByUsername(username);
			assertTrue(resultSet.next());
			assertEquals(username, resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the corresponding account!");
		}
	}

	@Test
	public void givenValidUsername_testGetPendingAccountByUsername_shouldReturnPendingAccount() {
		try {
			ResultSet resultSet = OpenAccountDao.getPendingAccountByUsername(username);
			assertTrue(resultSet.next());
			assertEquals(username, resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the corresponding account!");
		}
	}
}
