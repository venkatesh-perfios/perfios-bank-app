package com.perfiosbank.login;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class LoginDao {
	private static final String USERS_TABLE_NAME = "Users";
	private static final String ACCOUNTS_TABLE_NAME = "Accounts";
	
	public static ResultSet getPasswordByUsername(String username) throws Exception {
        String getPasswordSql = "select Password from " + USERS_TABLE_NAME + " where Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
        return statement.executeQuery(getPasswordSql);
	}
	
	public static ResultSet getApprovedAccountCountByUsername(String username) throws Exception {
		String getAccountCountByUsernameSql = "select count(*) from " + ACCOUNTS_TABLE_NAME + " where "
				+ "Username='" + username + "' and status='Approved'";
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeQuery(getAccountCountByUsernameSql);
	}
}
