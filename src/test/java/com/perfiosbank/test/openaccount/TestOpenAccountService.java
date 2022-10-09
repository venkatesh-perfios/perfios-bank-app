package com.perfiosbank.test.openaccount;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.exceptions.AadhaarInvalidException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.FileInvalidException;
import com.perfiosbank.exceptions.NameInvalidException;
import com.perfiosbank.exceptions.PanInvalidException;
import com.perfiosbank.exceptions.PhoneInvalidException;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.AccountInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.openaccount.OpenAccountService;
import com.perfiosbank.signup.SignupService;
import com.perfiosbank.utils.FileUtils;

public class TestOpenAccountService {

	static SignupService signupService;
	static User user;
	static String reenteredPassword;
	LoginService loginService;
	OpenAccountService openAccountService;
	User enteredDetails;
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
		openAccountService = new OpenAccountService();
		enteredDetails = new User();
		enteredDetails.setUsername(user.getUsername());
		enteredDetails.setPassword("Test2@OpenAccount");
		
		File file = new File("linuxcommands.pdf");
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
		openAccountService = null;
		enteredDetails = null;
		accountInfo = null;
	}

	@Test
	public void givenValidUserAndValidAccountInfo_testOpenAccount_shouldOpenAccount() {
		try {
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should have submitted the user's account opening application!");
		}
	}
	
	@Test
	public void givenInvalidUsername_testOpenAccount_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setUsername("wrongUsername");
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenInvalidPassword_testOpenAccount_shouldThrowAuthenticationFailedException() {
		assertThrows(AuthenticationFailedException.class, () -> {
			enteredDetails.setPassword("wrongPassword");
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenInvalidFirstName_testOpenAccount_shouldThrowNameInvalidException() {
		assertThrows(NameInvalidException.class, () -> {
			accountInfo.setFirstName("8");
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenInvalidLastName_testOpenAccount_shouldThrowNameInvalidException() {
		assertThrows(NameInvalidException.class, () -> {
			accountInfo.setLastName("8");
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenInvalidAadhaar_testOpenAccount_shouldThrowAadhaarInvalidException() {
		assertThrows(AadhaarInvalidException.class, () -> {
			accountInfo.setAadhaar(154632L);
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenInvalidPan_testOpenAccount_shouldThrowPanInvalidException() {
		assertThrows(PanInvalidException.class, () -> {
			accountInfo.setPan("sbjdkf");
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenInvalidPhone_testOpenAccount_shouldThrowPhoneInvalidException() {
		assertThrows(PhoneInvalidException.class, () -> {
			accountInfo.setPhone(84651565L);
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenNegativeAmount_testOpenAccount_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			accountInfo.setAmount(-5164.46);
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenAmountWithTooManyDigitsAfterDecimal_testOpenAccount_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			accountInfo.setAmount(5164.46534);
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenAmountEqualToZero_testOpenAccount_shouldThrowAmountInvalidException() {
		assertThrows(AmountInvalidException.class, () -> {
			accountInfo.setAmount(0);
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenAmountBelowOneThousand_testOpenAccount_shouldThrowBelowMinBalanceException() {
		assertThrows(BelowMinBalanceException.class, () -> {
			accountInfo.setAmount(465.13);
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}

	@Test
	public void givenNonPdfFile_testOpenAccount_shouldThrowFileInvalidException() {
		assertThrows(FileInvalidException.class, () -> {
			File file = new File("Core_Java.txt");
			InputStream fileContent = new FileInputStream(file);
			
			List<String> uploadedFilenames = accountInfo.getUploadedFilenames();
			uploadedFilenames.remove(2);
			uploadedFilenames.add("panProof,Core_Java.txt");
			accountInfo.setUploadedFilenames(uploadedFilenames);
			
			HashMap<String, byte[]> uploadedFiles = accountInfo.getUploadedFiles();
			uploadedFiles.remove("panProof");
			uploadedFiles.put("panProof", FileUtils.readAllBytes(fileContent));
			accountInfo.setUploadedFiles(uploadedFiles);
			
			openAccountService.openAccount(user, enteredDetails, accountInfo);
		});
	}
}
