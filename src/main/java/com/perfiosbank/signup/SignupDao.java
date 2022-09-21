package com.perfiosbank.signup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.perfiosbank.model.User;
import com.perfiosbank.utils.AuthenticationUtils;
import com.perfiosbank.utils.DatabaseUtils;

public class SignupDao {
	private static final String TABLE_NAME = "Users";
	
	public static int signupUser(User user) throws Exception {
		String signupUserSql = "insert into " + TABLE_NAME + "(Username, Password) " + "values('" + user.getUsername() + 
				"', '" + AuthenticationUtils.encryptPassword(user.getPassword()) + "')";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(signupUserSql);
	}
	
	public static ResultSet getUserByUsername(String username) throws ClassNotFoundException, SQLException {
		String getUserSql = "select * from " + TABLE_NAME + " where " + "Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getUserSql);
	}
}
