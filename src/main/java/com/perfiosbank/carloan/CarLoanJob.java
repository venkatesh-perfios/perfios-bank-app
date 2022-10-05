package com.perfiosbank.carloan;

import java.sql.ResultSet;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.transfer.TransferDao;
import com.perfiosbank.utils.DateTimeUtils;
import com.perfiosbank.withdraw.WithdrawDao;

public class CarLoanJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Car Loan Repayment Job is running.....");
		
		String TARGET_ACCOUNT_NUMBER = "PBIN1000000002";
		
		try {
			ResultSet carLoanRepaymentInfo = CarLoanDao.getAllCarLoanRepayments();
			
			while (carLoanRepaymentInfo.next()) {
				int id = carLoanRepaymentInfo.getInt(1);
				int loanId = carLoanRepaymentInfo.getInt(2);
				String username = carLoanRepaymentInfo.getString(3);
				String startDate = carLoanRepaymentInfo.getString(4);
				int hasStarted = carLoanRepaymentInfo.getInt(5);
				String endDate = carLoanRepaymentInfo.getString(6);
				int hasEnded = carLoanRepaymentInfo.getInt(7);
				double emi = carLoanRepaymentInfo.getDouble(8);
				int misses = carLoanRepaymentInfo.getInt(9);
				double penalty = carLoanRepaymentInfo.getDouble(10);
				
				ResultSet isAccountFrozenResultSet = CarLoanDao.isAccountFrozenByUsername(username);
				isAccountFrozenResultSet.next();
				if (isAccountFrozenResultSet.getInt(1) == 0) {
					if (hasEnded == 1) {
						double paymentToMake = misses * (emi + penalty);
						
						int status = transferMoneyToBank(username, id, paymentToMake, misses, TARGET_ACCOUNT_NUMBER);
						
						if (status == 1) {
							if (CarLoanDao.removeCarLoanById(loanId) != 1) {
								throw new Exception();
							}
						}
					} else if (hasStarted == 1) {
						String currentDate = DateTimeUtils.getCurrentDateTime().substring(0, 10);
						String onlyDate = currentDate.substring(8, 10);
						
						if (onlyDate.equals("01")) {
							double paymentToMake = (misses + 1) * emi + misses * penalty;
							
							int status = transferMoneyToBank(username, id, paymentToMake, misses, TARGET_ACCOUNT_NUMBER);
							
							if (currentDate.equals(endDate)) {
								if (CarLoanDao.updateLoanHasEndedById(id) != 1) {
									throw new Exception();
								}
								
								if (status == 1) {
									if (CarLoanDao.removeCarLoanById(loanId) != 1) {
										throw new Exception();
									}
								}
							}
						}
					} else {
						String currentDate = DateTimeUtils.getCurrentDateTime().substring(0, 10);
						if (currentDate.equals(startDate)) {
							double paymentToMake = (misses + 1) * emi + misses * penalty;
							
							transferMoneyToBank(username, id, paymentToMake, misses, TARGET_ACCOUNT_NUMBER);
							
							if (CarLoanDao.updateLoanHasStartedById(id) != 1) {
								throw new Exception();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int transferMoneyToBank(String username, int id, double paymentToMake, int misses, String TARGET_ACCOUNT_NUMBER) 
			throws Exception {
		String withdrawDate = DateTimeUtils.getCurrentDateTime();
        ResultSet currentBalanceResultSet = CheckBalanceDao.getCurrentBalanceByUsername(username);
        currentBalanceResultSet.next();
        double currentUserBalance = currentBalanceResultSet.getDouble(1);
        double newUserBalance = currentUserBalance - paymentToMake;
        if (newUserBalance < 0) {
        	int newMisses = misses + 1;
        	
        	if (CarLoanDao.updateMissesById(newMisses, id) != 1) {
        		throw new Exception();
        	}
        	
        	if (newMisses == 3) {
        		if (CarLoanDao.setAccountAsFrozenByUsername(username) != 1) {
        			throw new Exception();
        		}
        	}
        	
        	return -1;
        }
        if (WithdrawDao.withdrawMoney(username, withdrawDate, paymentToMake, newUserBalance) != 1) {
        	throw new Exception();
        }
        
        String targetUsername = getTargetUsername(TARGET_ACCOUNT_NUMBER);
        currentBalanceResultSet = CheckBalanceDao.getCurrentBalanceByUsername(targetUsername);
        if (!currentBalanceResultSet.next()) {
        	throw new Exception();
        }
        double currentTargetBalance = currentBalanceResultSet.getDouble(1);
        double newTargetBalance = currentTargetBalance + paymentToMake;
        String depositDate = DateTimeUtils.getCurrentDateTime();
        if (DepositDao.depositMoney(targetUsername, depositDate, paymentToMake, newTargetBalance) != 1) {
        	throw new Exception();
        }
        
    	if (CarLoanDao.updateMissesById(0, id) != 1) {
    		throw new Exception();
    	}
    	
    	return 1;
	}
	
	private String getTargetUsername(String targetAccountNumber) throws Exception {
    	ResultSet resultSet = TransferDao.getAccountByAccountNumber(targetAccountNumber);
    	
        if (!resultSet.next()) {
            throw new Exception();
        } else {
        	return resultSet.getString(2);
        }
	}
}
