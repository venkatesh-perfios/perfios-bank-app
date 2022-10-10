package com.perfiosbank.adminaccounts;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class AdminAccountsDao {
	private static final String TABLE_NAME = "Accounts";
	
	public static ResultSet getAllAccounts() throws Exception {
		String getAllAccountsSql = "select * from " + TABLE_NAME + " where status='Pending'";
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeQuery(getAllAccountsSql);
	}

	public static ResultSet getAllAccountsByAccountNumber(String accountNumber) throws Exception {
		String getAllCarLoansById = "select * from " + TABLE_NAME + " where Account_Number='" + accountNumber + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansById);
	}
	
	public static int changeStatusByAccountNumber(String accountNumber, String status) throws Exception {
		String changeStatusByIdSql = "update " + TABLE_NAME + " set status='" + status + "' where "
				+ "Account_Number='" + accountNumber + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(changeStatusByIdSql); 
	}
	
	public static int removeAccountByUsername(String username) throws Exception {
		String removeAccountByUsernameSql = "delete from " + TABLE_NAME + " where Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(removeAccountByUsernameSql);
	}
}
