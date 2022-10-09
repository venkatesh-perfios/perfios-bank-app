package com.perfiosbank.fixeddeposit;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.FixedDepositInfo;
import com.perfiosbank.utils.DatabaseUtils;

public class FixedDepositDao {
	private static final String FIXED_DEPOSITS_TABLE_NAME = "Fixed_Deposits";
	private static final String ACCOUNTS_TABLE_NAME = "Accounts";
	
	public static int openFixedDepositAccount(FixedDepositInfo fixedDepositInfo) throws Exception {
		String startFixedDepositSql = "insert into " + FIXED_DEPOSITS_TABLE_NAME + "(Username, Principal, End_Date, "
				+ "Interest_Rate, Maturity_Amount) values" + "('" + fixedDepositInfo.getUsername() + 
				"', " + fixedDepositInfo.getPrincipal() + ", '" + fixedDepositInfo.getEndDate() + 
				"', " + fixedDepositInfo.getInterestRate() + ", " + fixedDepositInfo.getMaturityAmount() + 
				")";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(startFixedDepositSql);
	}
	
	public static ResultSet getAgeByUsername(String username) throws Exception {
		String getAgeByUsernameSql = "select Age, Username from " + ACCOUNTS_TABLE_NAME + " where Username='" + 
				username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeQuery(getAgeByUsernameSql);
	}
	
	public static ResultSet getAllFixedDepositAccountsByUsername(String username) throws Exception {
		String getAllFixedDepositsByUsername = "select * from " + FIXED_DEPOSITS_TABLE_NAME + " where Username='" + username + 
				"' order by End_Date";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllFixedDepositsByUsername); 
	}
	
	public static ResultSet getAllFixedDepositAccounts() throws Exception {
		String getAllFixedDeposits = "select * from " + FIXED_DEPOSITS_TABLE_NAME + " order by End_Date";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllFixedDeposits);
	}
	
	public static int deleteFixedDepositAccountById(int id) throws Exception {
		String deleteFixedDepositAccountByIdSql = "delete from " + FIXED_DEPOSITS_TABLE_NAME + " where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(deleteFixedDepositAccountByIdSql);
	}
}
