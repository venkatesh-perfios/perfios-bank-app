package com.perfiosbank.withdraw;

import java.sql.Statement;

import com.perfiosbank.model.DepositWithdrawInfo;
import com.perfiosbank.utils.DatabaseUtils;

public class WithdrawDao {
	private static final String TABLE_NAME = "Transactions";
	public static int withdrawMoney(DepositWithdrawInfo withdrawInfo, String date, double newBalance) 
			throws Exception {
        String withdrawMoneySql = "insert into " + TABLE_NAME + "(Username, Date_and_Time, Type, Amount, Balance)" +
                " values('" + withdrawInfo.getUsername() + "', '" + date + "', 'W', " + withdrawInfo.getAmount() +
                ", " + newBalance + ")";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeUpdate(withdrawMoneySql);
	}
}
