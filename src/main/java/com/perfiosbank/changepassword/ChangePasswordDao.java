package com.perfiosbank.changepassword;

import java.sql.Statement;

import com.perfiosbank.model.User;
import com.perfiosbank.utils.DatabaseUtils;

public class ChangePasswordDao {
	public static int changePassword(String encryptedNewPassword, User userInSession) throws Exception {
        String changePasswordSql = "update Users set Password='" + encryptedNewPassword + 
        		"' where Username='" + userInSession.getUsername() + "'";
        Statement statement = DatabaseUtils.getConnection().createStatement();
        
        return statement.executeUpdate(changePasswordSql);
	}
}
