package com.perfiosbank.deposit;

import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class DepositDao {
	private static final String TABLE_NAME = "Transactions";
	
	public static int depositMoney(String username, String date, double amount, double newBalance) throws Exception {
        String depositMoneySql = "insert into " + TABLE_NAME + "(Username, Date_and_Time, Type, Amount, Balance)" +
                " values('" + username + "', '" + date + "', 'D', " + amount + ", " + newBalance + ")";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeUpdate(depositMoneySql);
	}
}
