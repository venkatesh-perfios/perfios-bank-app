package com.perfiosbank.closeaccount;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;

public class CloseAccountDao {
	public static int removeUser(User userInSession) throws Exception {
        String removeUserSql = "delete from Users where Username='" + userInSession.getUsername() + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeUpdate(removeUserSql);
	}
	
	public static int getNumberOfActiveFixedDepositAccountsByUsername(String username) throws Exception {
		String getNumberOfActiveFixedDepositAccountsByUsernameSql = "select count(*) from Fixed_Deposits "
				+ "where Username='" + username + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(getNumberOfActiveFixedDepositAccountsByUsernameSql);
        resultSet.next();
        
        return resultSet.getInt(1);
	}
	
	public static int getNumberOfApprovedLoansByUsername(String username) throws Exception {
		String getNumberOfApprovedLoansByUsernameSql = "select count(*) from Car_Loans where Status='Approved' and "
				+ "Username='" + username + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(getNumberOfApprovedLoansByUsernameSql);
        resultSet.next();
        
        return resultSet.getInt(1);
	}
}
