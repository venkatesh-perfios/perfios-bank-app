package com.perfiosbank.deposit;

import java.sql.Statement;

import com.perfiosbank.model.DepositWithdrawInfo;
import com.perfiosbank.utils.DatabaseUtils;

public class DepositDao {
	private static final String TABLE_NAME = "Transactions";
	
	public static int depositMoney(DepositWithdrawInfo depositInfo, String date, double newBalance) throws Exception {
        String depositMoneySql = "insert into " + TABLE_NAME + "(Username, Date_and_Time, Type, Amount, Balance)" +
                " values('" + depositInfo.getUsername() + "', '" + date + "', 'D', " + depositInfo.getAmount() +
                ", " + newBalance + ")";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeUpdate(depositMoneySql);
	}
}
