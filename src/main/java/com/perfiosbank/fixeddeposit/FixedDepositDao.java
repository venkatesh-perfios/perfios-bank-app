package com.perfiosbank.fixeddeposit;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.FixedDepositInfo;
import com.perfiosbank.utils.DatabaseUtils;

public class FixedDepositDao {
	private static final String TABLE_NAME = "Fixed_Deposits";
	
	public static int openFixedDepositAccount(FixedDepositInfo fixedDepositInfo) throws Exception {
		String startFixedDepositSql = "insert into " + TABLE_NAME + "(Username, Principal, End_Date, "
				+ "Interest_Rate, Maturity_Amount) values" + "('" + fixedDepositInfo.getUsername() + 
				"', " + fixedDepositInfo.getPrincipal() + ", '" + fixedDepositInfo.getEndDate() + 
				"', " + fixedDepositInfo.getInterestRate() + ", " + fixedDepositInfo.getMaturityAmount() + 
				")";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(startFixedDepositSql);
	}
	
	public static ResultSet getAllFixedDepositAccountsByUsername(String username) throws Exception {
		String getAllFixedDepositsByUsername = "select * from " + TABLE_NAME + " where Username='" + username + 
				"' order by End_Date";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllFixedDepositsByUsername); 
	}
	
	public static ResultSet getAllFixedDepositAccounts() throws Exception {
		String getAllFixedDeposits = "select * from " + TABLE_NAME + " order by End_Date";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllFixedDeposits); 
	}
	
	public static int deleteFixedDepositAccountById(int id) throws Exception {
		String deleteFixedDepositAccountByIdSql = "delete from " + TABLE_NAME + " where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(deleteFixedDepositAccountByIdSql);
	}
}
