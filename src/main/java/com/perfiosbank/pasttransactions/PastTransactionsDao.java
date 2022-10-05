package com.perfiosbank.pasttransactions;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class PastTransactionsDao {
	public static ResultSet getPastTransactions(String username) throws Exception {
        String getPastTransactions = "select Date_and_Time, Type, Amount, Balance from Transactions "
        		+ "where Username='" + username + "' order by ID desc";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeQuery(getPastTransactions);
	}
}
