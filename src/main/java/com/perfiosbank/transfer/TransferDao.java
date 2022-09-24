package com.perfiosbank.transfer;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class TransferDao {
	private static final String ACCOUNTS_TABLE_NAME = "Accounts";
	
	public static ResultSet getAccountByAccountNumber(String targetAccountNumber) throws Exception {
        String getAccountByAccountNumberSql = "select * from " + ACCOUNTS_TABLE_NAME + " where " + 
        		"Account_Number='" + targetAccountNumber + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeQuery(getAccountByAccountNumberSql);
	}
}
