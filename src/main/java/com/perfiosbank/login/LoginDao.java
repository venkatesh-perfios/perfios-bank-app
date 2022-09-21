package com.perfiosbank.login;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class LoginDao {
	private static final String TABLE_NAME = "Users";
	
	public static ResultSet getPasswordByUsername(String username) throws Exception {
        String getPasswordSql = "select Password from " + TABLE_NAME + " where Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
        return statement.executeQuery(getPasswordSql);
	}
}
