package com.perfiosbank.openaccount;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.AccountInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;

public class OpenAccountDao {
	private static final String TABLE_NAME = "Accounts";
	
	public static int openAccount(String newAccountNumber, User userInSession, AccountInfo accountInfo) throws Exception {
		String openAccountSql = "insert into " + TABLE_NAME + " values('" + newAccountNumber + "', '" +
				userInSession.getUsername() + "', '" + accountInfo.getFirstName() + "', '" + 
				accountInfo.getLastName() + "', " + accountInfo.getAge() + ", " + accountInfo.getAadhaar() + 
				", '" + accountInfo.getPan() + "', '" + accountInfo.getAddress() + "', " + 
				accountInfo.getPhone() + ", " + accountInfo.getAmount() + ")";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(openAccountSql);
	}
	
	public static ResultSet getMaxAccountNumber() throws Exception {
		String maxAccountNumberSql = "select max(Account_Number) from " + TABLE_NAME;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(maxAccountNumberSql);
	}
	
	public static ResultSet getAccountByUsername(String username) throws Exception {
		String getAccountSql = "select * from " + TABLE_NAME + " where Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeQuery(getAccountSql);
	}
}
