package com.perfiosbank.admincarloans;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.CarLoanRepaymentInfo;
import com.perfiosbank.utils.DatabaseUtils;

public class AdminCarLoansDao {
	private static final String CAR_LOANS_TABLE_NAME = "Car_Loans";
	private static final String CAR_LOAN_REPAYMENTS_TABLE_NAME = "Car_Loan_Repayments";
	
	public static ResultSet getAllPendingCarLoans() throws Exception {
		String getAllCarLoansById = "select * from " + CAR_LOANS_TABLE_NAME + " where status='Pending'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansById); 
	}
	
	public static ResultSet getCarLoanById(int id) throws Exception {
		String getAllCarLoansById = "select * from " + CAR_LOANS_TABLE_NAME + " where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansById);
	}
	
	public static int changeStatusById(int id, String status) throws Exception {
		String changeStatusByIdSql = "update " + CAR_LOANS_TABLE_NAME + " set status='" + status + "' where "
				+ "ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(changeStatusByIdSql); 
	}

	public static int initializeRepayment(CarLoanRepaymentInfo carLoanRepaymentInfo) 
			throws Exception {
		String initializeRepaymentSql = "insert into " + CAR_LOAN_REPAYMENTS_TABLE_NAME + "("
				+ "Loan_ID, Username, Start_Date, Has_Started, End_Date, Has_Ended, EMI, Misses, Penalty) "
				+ "values(" + carLoanRepaymentInfo.getLoanId() + ", '" + carLoanRepaymentInfo.getUsername() + "', '" + 
				carLoanRepaymentInfo.getStartDate() + "', " + carLoanRepaymentInfo.getHasStarted()
				+ ", '" + carLoanRepaymentInfo.getEndDate() + "', " + 
				carLoanRepaymentInfo.getHasEnded() + ", " + carLoanRepaymentInfo.getEmi() + ", " + 
				carLoanRepaymentInfo.getMisses() + ", " + carLoanRepaymentInfo.getPenalty() + ")";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(initializeRepaymentSql);
	}
	
	public static int removeRejectedCarLoansByUsername(String username) throws Exception {
		String removeRejectedCarLoansByUsernameSql = "delete from " + CAR_LOANS_TABLE_NAME
				+ " where Status='Rejected' and Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeUpdate(removeRejectedCarLoansByUsernameSql);
	}
}
