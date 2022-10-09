package com.perfiosbank.test.fixeddeposit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.perfiosbank.fixeddeposit.FixedDepositDao;
import com.perfiosbank.login.LoginService;
import com.perfiosbank.model.FixedDepositInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;

public class TestFixedDepositDao {
	static User user;
	static LoginService loginService;
	FixedDepositInfo fixedDepositInfo;
	String username;
	int id;
	
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
		fixedDepositInfo = new FixedDepositInfo();
		fixedDepositInfo.setUsername(user.getUsername());
		fixedDepositInfo.setPassword("Test3@Services");
		fixedDepositInfo.setPrincipal(7642.16);
		fixedDepositInfo.setEndDate("2023-10-20");
		fixedDepositInfo.setInterestRate(5.50);
		fixedDepositInfo.setMaturityAmount(78461.63);
		username = user.getUsername();
	}
	
	@AfterEach
	public void tearDown() {
		fixedDepositInfo = null;
		username = null;
	}
	
	@Test
	public void givenValidFixedDepositInfo_testOpenFixedDepositAccount_shouldOpenFixedDepositAccount() {
		try {
			int rowsAffected = FixedDepositDao.openFixedDepositAccount(fixedDepositInfo);
			assertEquals(1, rowsAffected);
			
			ResultSet resultSet = FixedDepositDao.getAllFixedDepositAccountsByUsername(fixedDepositInfo.getUsername());
			assertTrue(resultSet.next());
			assertEquals(fixedDepositInfo.getUsername(), resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have opened the FD account!");
		}
	}
	
	@Test
	public void givenValidUsername_testGetAgeByUsername_shouldReturnAge() {
		try {
			ResultSet resultSet = FixedDepositDao.getAgeByUsername(username);
			assertTrue(resultSet.next());
			assertTrue(resultSet.getInt(1) >= 18 && resultSet.getInt(1) <= 125);
			assertEquals(fixedDepositInfo.getUsername(), resultSet.getString(2));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the user's age!");
		}
	}

	@Test
	public void givenValidUsername_testGetAllFixedDepositAccountsByUsername_shouldReturnAllFixedDepositAccountsByUsername() {
		try {
			ResultSet resultSet = FixedDepositDao.getAllFixedDepositAccountsByUsername(username);
			assertTrue(resultSet.next());
			assertEquals(fixedDepositInfo.getUsername(), resultSet.getString("Username"));
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned the user's FDs!");
		}
	}

	@Test
	public void testGetAllFixedDepositAccounts_shouldReturnAllFixedDepositAccounts() {
		try {
			FixedDepositDao.getAllFixedDepositAccounts();
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have returned all FDs!");
		}
	}

	@Test
	public void givenValidId_testDeleteFixedDepositAccountById_shouldDeleteFixedDepositAccountById() {
		try {
			FixedDepositDao.openFixedDepositAccount(fixedDepositInfo);

			String getMaxIdSql = "select max(ID) from Fixed_Deposits";
			Statement statement = DatabaseUtils.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(getMaxIdSql);
			resultSet.next();
			id = resultSet.getInt(1);
			
			int rowsAffected = FixedDepositDao.deleteFixedDepositAccountById(id);
			assertEquals(1, rowsAffected);

			String getFixedDepositByIdSql = "select * from Fixed_Deposits where ID=" + id;
			statement = DatabaseUtils.getConnection().createStatement();
			resultSet = statement.executeQuery(getFixedDepositByIdSql);
			assertFalse(resultSet.next());
		} catch(Exception e) {
			e.printStackTrace();
			fail("Should have deleted user's FD!");
		}
	}
}
