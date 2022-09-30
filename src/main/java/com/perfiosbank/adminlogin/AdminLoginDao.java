package com.perfiosbank.adminlogin;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class AdminLoginDao {
	private static final String USERS_TABLE_NAME = "Admins";
	
	public static ResultSet getPasswordByUsername(String username) throws Exception {
        String getPasswordSql = "select Password from " + USERS_TABLE_NAME + " where Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
        return statement.executeQuery(getPasswordSql);
	}
}
