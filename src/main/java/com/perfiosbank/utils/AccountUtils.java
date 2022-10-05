package com.perfiosbank.utils;

import com.perfiosbank.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class AccountUtils {
	private final static String TABLE_NAME = "Accounts";
	
    public static boolean isAccountNotFound(User userInSession) throws SQLException, ClassNotFoundException {
        String getAccountNumberSql = "select Account_Number from " + TABLE_NAME + " where Username='" + 
        		userInSession.getUsername() + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(getAccountNumberSql);

        return !resultSet.next();
    }

    public static boolean isAmountInvalid(double amount) {
        return !(Pattern.matches("^\\d+[.]?\\d*$", Double.toString(amount))
                && amount != 0.0 && Double.toString(amount).split("\\.")[1].length() <= 2);
    }

    public static boolean isBelowMinBalance(double amount) {
        return amount < 1000.0;
    }
    
    public static boolean isAccountFrozen(String username) throws Exception {
    	String isAccountFrozenSql = "select Is_Frozen from " + TABLE_NAME + " where Username='"
    			+ username + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(isAccountFrozenSql);
        if (!resultSet.next()) {
        	return false;
        } else {
        	return resultSet.getInt(1) == 1;
        }
    }
}
