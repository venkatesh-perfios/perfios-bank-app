package com.perfiosbank.carloan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.perfiosbank.model.CarLoanInfo;
import com.perfiosbank.utils.DatabaseUtils;

public class CarLoanDao {
	private static final String TABLE_NAME = "Car_Loans";
	
	public static int saveLoanApplication(CarLoanInfo carLoanInfo) throws Exception {
		String saveLoanApplication = "insert into " + TABLE_NAME + "(Username, Loan_Amount, Due_Date, Cibil_Score, "
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
		String getAllCarLoansByUsername = "select * from " + TABLE_NAME + " where Username='" + username + 
				"' order by Due_Date";
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansByUsername); 
	}

	public static ResultSet getAllCarLoansById(int id) throws Exception {
		String getAllCarLoansById = "select * from " + TABLE_NAME + " where ID=" + id;
		Statement statement = DatabaseUtils.getConnection().createStatement();
		
		return statement.executeQuery(getAllCarLoansById); 
	}
}
