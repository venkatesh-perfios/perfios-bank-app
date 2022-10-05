package com.perfiosbank.fixeddeposit;

import java.sql.ResultSet;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.utils.DateTimeUtils;


public class FixedDepositJob implements Job {
	@SuppressWarnings("resource")
	@Override
	public void execute(JobExecutionContext context) {
		System.out.println("Fixed Deposit Job is running.....");
		
		ResultSet fixedDepositResultSet = null;
		
		try {
			fixedDepositResultSet = FixedDepositDao.getAllFixedDepositAccounts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (!fixedDepositResultSet.next()) {
				return;
			} else {
			    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			    Date now = simpleDateFormat.parse(DateTimeUtils.getCurrentDateTime().substring(0, 10));
			    
				do {
			        Date endDate = simpleDateFormat.parse(fixedDepositResultSet.getString(4));
			        long differenceInTime = endDate.getTime() - now.getTime();
			        
			        if (differenceInTime <= 0) {
			        	String username = fixedDepositResultSet.getString(2);
			            ResultSet currentBalanceResultSet = CheckBalanceDao.getCurrentBalanceByUsername(username);
			            double currentBalance = currentBalanceResultSet.next() ? currentBalanceResultSet.getDouble(1) : 0.0;
			            double maturityAmount = fixedDepositResultSet.getDouble(6);
			            double newBalance = currentBalance + maturityAmount;
			            String date = DateTimeUtils.getCurrentDateTime();
			            
			            if (DepositDao.depositMoney(username, date, maturityAmount, newBalance) != 1) {
			    			throw new Exception();
			            }
			            
			            if (FixedDepositDao.deleteFixedDepositAccountById(fixedDepositResultSet.getInt(1)) != 1) {
			            	throw new Exception();
			            }
			        } else {
			        	break;
			        }
				} while (fixedDepositResultSet.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
