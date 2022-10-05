package com.perfiosbank.carloan;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.CarLoanInfo;
import com.perfiosbank.utils.DatabaseUtils;

public class CarLoanDao {
	private static final String CAR_LOANS_TABLE_NAME = "Car_Loans";
	private static final String CAR_LOAN_REPAYMENTS_TABLE_NAME = "Car_Loan_Repayments";
	private static final String ACCOUNTS_TABLE = "Accounts";
	
	public static int saveLoanApplication(CarLoanInfo carLoanInfo) throws Exception {
		String saveLoanApplication = "insert into " + CAR_LOANS_TABLE_NAME + "(Username, Loan_Amount, Due_Date, Cibil_Score, "
				+ "Cibil_Report, Identity_Proof, Address_Proof, Income_Proof, Interest_Rate, Due_Amount, Status) values(?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = DatabaseUtils.getConnection().prepareStatement(saveLoanApplication);
		preparedStatement.setString(1, carLoanInfo.getUsername());
		preparedStatement.setDouble(2, carLoanInfo.getLoanAmount());
		preparedStatement.setString(3, carLoanInfo.getDueDate());
		preparedStatement.setInt(4, carLoanInfo.getCibilScore());
		preparedStatement.setBytes(5, carLoanInfo.getUploadedFiles().get("cibilReport"));
		preparedStatement.setBytes(6, carLoanInfo.getUploadedFiles().get("identityProof"));
		preparedStatement.setBytes(7, carLoanInfo.getUploadedFiles().get("addressProof"));
		preparedStatement.setBytes(8, carLoanInfo.getUploadedFiles().get("incomeProof"));
		preparedStatement.setDouble(9, carLoanInfo.getInterestRate());
		preparedStatement.setDouble(10, carLoanInfo.getDueAmount());
		preparedStatement.setString(11, "Pending");
		
		return preparedStatement.executeUpdate();
	}
	
	public static ResultSet getAllCarLoansByUsername(String username) throws Exception {
		String getAllCarLoansByUsername = "select * from " + CAR_LOANS_TABLE_NAME + " where Username='" + username + 
				"' order by Due_Date";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansByUsername); 
	}

	public static ResultSet getCarLoanById(int id) throws Exception {
		String getAllCarLoansById = "select * from " + CAR_LOANS_TABLE_NAME + " where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansById); 
	}
	
	public static ResultSet isAccountFrozenByUsername(String username) throws Exception {
		String isAccountFrozenSql = "select Is_Frozen from " + ACCOUNTS_TABLE + " where "
				+ "Username='" + username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(isAccountFrozenSql);
	}
	
	public static ResultSet getAllCarLoanRepayments() throws Exception {
		String getCarLoanRepaymentByUsernameSql = "select * from " + CAR_LOAN_REPAYMENTS_TABLE_NAME;
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeQuery(getCarLoanRepaymentByUsernameSql);
	}
	
	public static int removeCarLoanRepaymentById(int id) throws Exception {
		String removeCarLoanRepaymentByIdSql = "delete from " + CAR_LOAN_REPAYMENTS_TABLE_NAME + 
				" where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeUpdate(removeCarLoanRepaymentByIdSql);
	}

	public static int removeCarLoanById(int id) throws Exception {
		String removeCarLoanByIdSql = "delete from " + CAR_LOANS_TABLE_NAME + 
				" where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeUpdate(removeCarLoanByIdSql);
	}
	
	public static int updateMissesById(int newMisses, int id) throws Exception {
		String incrementMissesByIdSql = "update " + CAR_LOAN_REPAYMENTS_TABLE_NAME + " set Misses=" + newMisses
				+ " where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeUpdate(incrementMissesByIdSql);
	}
	
	public static int setAccountAsFrozenByUsername(String username) throws Exception {
		String setAccountAsFrozenByUsernameSql = "update " + ACCOUNTS_TABLE + " set Is_Frozen=0 where Username='"
				+ username + "'";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeUpdate(setAccountAsFrozenByUsernameSql);
	}

	public static int updateLoanHasEndedById(int id) throws Exception {
		String updateLoanHasEndedByIdSql = "update " + CAR_LOAN_REPAYMENTS_TABLE_NAME + " set Has_Ended=1 where "
				+ "ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeUpdate(updateLoanHasEndedByIdSql);
	}

	public static int updateLoanHasStartedById(int id) throws Exception {
		String updateLoanHasStartedByIdSql = "update " + CAR_LOAN_REPAYMENTS_TABLE_NAME + " set Has_Started=1 where "
				+ "ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeUpdate(updateLoanHasStartedByIdSql);
	}

	public static ResultSet getCarLoanRepaymentByLoanId(int loanId) throws Exception {
		String getCarLoanRepaymentByLoanIdSql = "select * from " + CAR_LOAN_REPAYMENTS_TABLE_NAME
				+ " where Loan_ID=" + loanId;
		Statement statement = DatabaseUtils.getConnection().createStatement();

		return statement.executeQuery(getCarLoanRepaymentByLoanIdSql);
	}
}
