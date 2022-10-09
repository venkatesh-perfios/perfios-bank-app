package com.perfiosbank.openaccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.AccountInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;

public class OpenAccountDao {
	private static final String TABLE_NAME = "Accounts";
	
	public static int openAccount(String newAccountNumber, User userInSession, AccountInfo accountInfo) throws Exception {
		String openAccountSql = "insert into " + TABLE_NAME + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = DatabaseUtils.getConnection().prepareStatement(openAccountSql);
		preparedStatement.setString(1, newAccountNumber);
		preparedStatement.setString(2, userInSession.getUsername());
		preparedStatement.setBytes(3, accountInfo.getUploadedFiles().get("photo"));
		preparedStatement.setString(4, accountInfo.getFirstName());
		preparedStatement.setString(5, accountInfo.getLastName());
		preparedStatement.setInt(6, accountInfo.getAge());
		preparedStatement.setLong(7, accountInfo.getAadhaar());
		preparedStatement.setBytes(8, accountInfo.getUploadedFiles().get("aadhaarProof"));
		preparedStatement.setString(9, accountInfo.getPan());
		preparedStatement.setBytes(10, accountInfo.getUploadedFiles().get("panProof"));
		preparedStatement.setString(11, accountInfo.getAddress());
		preparedStatement.setLong(12, accountInfo.getPhone());
		preparedStatement.setDouble(13, accountInfo.getAmount());
		preparedStatement.setString(14, "Pending");
		preparedStatement.setInt(15, 0);
		
		return preparedStatement.executeUpdate();
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
	
	public static ResultSet getPendingAccountByUsername(String username) throws Exception {
		String getAccountSql = "select * from " + TABLE_NAME + " where Username='" + username + 
				"' and status='Pending'";
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeQuery(getAccountSql);
	}
}
