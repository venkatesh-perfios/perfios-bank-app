package com.perfiosbank.withdraw;

import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class WithdrawDao {
	private static final String TABLE_NAME = "Transactions";
	
	public static int withdrawMoney(String username, String date, double amount, double newBalance) 
			throws Exception {
        String withdrawMoneySql = "insert into " + TABLE_NAME + "(Username, Date_and_Time, Type, Amount, Balance)" +
                " values('" + username + "', '" + date + "', 'W', " + amount + ", " + newBalance + ")";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeUpdate(withdrawMoneySql);
	}
}
