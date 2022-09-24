package com.perfiosbank.checkbalance;

import java.sql.ResultSet;

import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class CheckBalanceDao {
	private static final String TABLE_NAME = "Transactions";
	
	public static ResultSet getCurrentBalance(String username) throws Exception {
        String getCurrentBalanceSql = "select balance from " + TABLE_NAME + " where Username='" + username + 
        		"' order by id DESC LIMIT 1";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeQuery(getCurrentBalanceSql);
	}
}
