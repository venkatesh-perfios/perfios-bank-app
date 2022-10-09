package com.perfiosbank.test.carloan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
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

import com.perfiosbank.admincarloans.AdminCarLoansService;
import com.perfiosbank.carloan.CarLoanDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.AdminCarLoansInfo;
import com.perfiosbank.model.CarLoanInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.openaccount.OpenAccountDao;
import com.perfiosbank.utils.DatabaseUtils;
import com.perfiosbank.utils.FileUtils;

public class TestCarLoanDao {
	static User user;
	static LoginService loginService;
	static HashMap<String, byte[]> uploadedFiles;
	static List<String> uploadedFilenames;
	CarLoanInfo carLoanInfo;
	String username;
	int loanRepaymentId;
	int loanId;
	
	@BeforeAll
	public static void setUpOnce() throws Exception {
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

		File file = new File("linuxcommands.pdf");
		final String[] fileTypes = {"cibilReport", "identityProof", "addressProof", "incomeProof"};
		uploadedFiles = new HashMap<>();
		uploadedFilenames = new ArrayList<String>();
		for (String fileType : fileTypes) {
			uploadedFilenames.add(fileType + "," + file.getName());
			InputStream fileContent = new FileInputStream(file);
		    byte[] fileContentInBytes = FileUtils.readAllBytes(fileContent);
		    uploadedFiles.put(fileType, fileContentInBytes);
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

		carLoanInfo = new CarLoanInfo();
		carLoanInfo.setUsername(user.getUsername());
		carLoanInfo.setPassword("Test3@Services");
		carLoanInfo.setLoanAmount(7614.36);
		carLoanInfo.setDays(762);
		carLoanInfo.setCibilScore(736);
		carLoanInfo.setInterestRate(8.40);
		carLoanInfo.setDueAmount(16384.23);
		carLoanInfo.setUploadedFilenames(uploadedFilenames);
		carLoanInfo.setUploadedFiles(uploadedFiles);
		
		CarLoanDao.saveLoanApplication(carLoanInfo);
		ResultSet resultSet = CarLoanDao.getAllCarLoansByUsername(carLoanInfo.getUsername());
		resultSet.next();
		AdminCarLoansInfo adminCarLoansInfo = new AdminCarLoansInfo();
		adminCarLoansInfo.setId(resultSet.getInt("ID"));
		adminCarLoansInfo.setUsername(carLoanInfo.getUsername());
		adminCarLoansInfo.setPrincipal(carLoanInfo.getLoanAmount());
		adminCarLoansInfo.setNewStatus("Approved");
		adminCarLoansInfo.setDays(carLoanInfo.getDays());
		adminCarLoansInfo.setDueAmount(carLoanInfo.getDueAmount());
		AdminCarLoansService adminCarLoansService = new AdminCarLoansService();
		
		adminCarLoansService.reviewCarLoanApplication(adminCarLoansInfo);
		resultSet = CarLoanDao.getCarLoanRepaymentByLoanId(adminCarLoansInfo.getId());
		resultSet.next();
		
		loanId = adminCarLoansInfo.getId();
		loanRepaymentId = resultSet.getInt("ID");
	}
	
	@AfterEach
	public void tearDown() {
		try {
			String removeAllCarLoansByUsername = "delete from Car_Loans where Username='" + user.getUsername() + "'";
			Statement statement = DatabaseUtils.getConnection().createStatement();
			statement.executeUpdate(removeAllCarLoansByUsername);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have removed all of user's car loans!");
		}
		username = null;
		carLoanInfo = null;
		loanRepaymentId = 0;
		loanId = 0;
	}
	
	@Test
	public void givenValidCarLoanInfo_testSaveLoanApplication_shouldApplyForCarLoan() {
		try {
			int rowsAffected = CarLoanDao.saveLoanApplication(carLoanInfo);
			assertEquals(1, rowsAffected);

			ResultSet resultSet = CarLoanDao.getAllCarLoansByUsername(carLoanInfo.getUsername());
			assertTrue(resultSet.next());
			assertEquals(carLoanInfo.getUsername(), resultSet.getString("Username"));
			assertTrue(resultSet.next());
			assertEquals(carLoanInfo.getUsername(), resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have applied for the user's car loan!");
		}
	}
	
	@Test
	public void givenValidUsername_testGetAllCarLoansByUsername_shouldReturnAllCarLoansByUsername() {
		try {
			ResultSet resultSet = CarLoanDao.getAllCarLoansByUsername(username);
			while (resultSet.next()) {
				assertEquals(username, resultSet.getString("Username"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned all car loans by username!");
		}
	}

	@Test
	public void givenValidLoanId_testGetCarLoanById_shouldReturnCarLoanCorrespondingToId() {
		try {
			ResultSet resultSet = CarLoanDao.getCarLoanById(loanId);
			assertTrue(resultSet.next());
			assertEquals(loanId, resultSet.getInt("ID"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned all car loans by username!");
		}
	}

	@Test
	public void givenValidUsername_testIsAccountFrozenByUsername_shouldReturnWhetherAccountIsFrozenOrNot() {
		try {
			ResultSet resultSet = CarLoanDao.isAccountFrozenByUsername(carLoanInfo.getUsername());
			assertTrue(resultSet.next());
			assertEquals(username, resultSet.getString("Username"));
			assertTrue(resultSet.getInt("Is_Frozen") == 0 || resultSet.getInt("Is_Frozen") == 1);
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned all car loans by username!");
		}
	}

	@Test
	public void testGetAllCarLoanRepayments_shouldReturnAllCarLoanRepayments() {
		try {
			ResultSet resultSet = CarLoanDao.getAllCarLoanRepayments();
			assertTrue(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned all car loan repayments!");
		}
	}
	
	@Test
	public void givenValidLoanId_testRemoveCarLoanRepaymentById_shouldRemoveCarLoanRepaymentCorrespondingToId() {
		try {
			int rowsAffected = CarLoanDao.removeCarLoanRepaymentById(loanRepaymentId);
			assertEquals(1, rowsAffected);
			
			ResultSet resultSet = CarLoanDao.getCarLoanRepaymentByLoanId(loanId);
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have removed the corresponding car loan!");
		}
	}

	@Test
	public void givenValidLoanId_testRemoveCarLoanById_shouldRemoveCarLoanCorrespondingToId() {
		try {
			int rowsAffected = CarLoanDao.removeCarLoanById(loanId);
			assertEquals(1, rowsAffected);
			
			ResultSet resultSet = CarLoanDao.getCarLoanById(loanId);
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have removed the corresponding car loan!");
		}
	}
	
	@Test
	public void givenValidNewMissesAndValidId_testUpdateMissesById_shouldUpdateMissesCorrespondingToId() {
		try {
			int rowsAffected = CarLoanDao.updateMissesById(2, loanRepaymentId);
			assertEquals(1, rowsAffected);

			ResultSet resultSet = CarLoanDao.getCarLoanRepaymentByLoanId(loanId);
			assertTrue(resultSet.next());
			assertEquals(2, resultSet.getInt("Misses"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have updated the corresponding number of misses!");
		}
	}

	@Test
	public void givenValidUsername_testSetAccountAsFrozenByUsername_shouldFreezeAccountCorrespondingToUsername() {
		try {
			int rowsAffected = CarLoanDao.setAccountAsFrozenByUsername(carLoanInfo.getUsername());
			assertEquals(1, rowsAffected);

			ResultSet resultSet = OpenAccountDao.getAccountByUsername(carLoanInfo.getUsername());
			assertTrue(resultSet.next());
			assertEquals(0, resultSet.getInt("Is_Frozen"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have frozen the corresponding account!");
		}
	}

	@Test
	public void givenValidId_testUpdateLoanHasEndedById_shouldEndLoanCorrespondingToId() {
		try {
			int rowsAffected = CarLoanDao.updateLoanHasEndedById(loanRepaymentId);
			assertEquals(1, rowsAffected);

			ResultSet resultSet = CarLoanDao.getCarLoanRepaymentByLoanId(loanId);
			assertTrue(resultSet.next());
			assertEquals(1, resultSet.getInt("Has_Ended"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have ended the corresponding loan!");
		}
	}

	@Test
	public void givenValidId_testUpdateLoanHasStartedById_shouldStartLoanCorrespondingToId() {
		try {
			int rowsAffected = CarLoanDao.updateLoanHasStartedById(loanRepaymentId);
			assertEquals(1, rowsAffected);

			ResultSet resultSet = CarLoanDao.getCarLoanRepaymentByLoanId(loanId);
			assertTrue(resultSet.next());
			assertEquals(1, resultSet.getInt("Has_Started"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have started the corresponding loan!");
		}
	}

	@Test
	public void givenValidLoanId_testGetCarLoanRepaymentByLoanId_shouldReturnCarLoanRepaymentCorrespondingToLoanId() {
		try {
			ResultSet resultSet = CarLoanDao.getCarLoanRepaymentByLoanId(loanId);
			assertTrue(resultSet.next());
			assertEquals(loanId, resultSet.getInt("Loan_ID"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the corresponding car loan repayment!");
		}
	}
}
