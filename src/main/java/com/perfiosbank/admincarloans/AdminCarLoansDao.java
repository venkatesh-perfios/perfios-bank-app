package com.perfiosbank.admincarloans;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.utils.DatabaseUtils;

public class AdminCarLoansDao {
	private static final String TABLE_NAME = "Car_Loans";
	
	public static ResultSet getAllCarLoans() throws Exception {
		String getAllCarLoansById = "select * from " + TABLE_NAME + " where status='Pending'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansById); 
	}
	
	public static ResultSet getAllCarLoansById(int id) throws Exception {
		String getAllCarLoansById = "select * from " + TABLE_NAME + " where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansById); 
	}
	
	public static int changeStatusById(int id, String status) throws Exception {
		String changeStatusByIdSql = "update " + TABLE_NAME + " set status='" + status + "' where "
				+ "ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(changeStatusByIdSql); 
	}
}
