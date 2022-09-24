package com.perfiosbank.closeaccount;

import java.sql.Statement;

import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;

public class CloseAccountDao {
	public static int removeUser(User userInSession) throws Exception {
        String removeUserSql = "delete from Users where Username='" + userInSession.getUsername() + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeUpdate(removeUserSql);
	}
}
