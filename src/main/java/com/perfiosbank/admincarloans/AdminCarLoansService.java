package com.perfiosbank.admincarloans;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.model.AdminCarLoansInfo;
import com.perfiosbank.model.CarLoanRepaymentInfo;
import com.perfiosbank.utils.DateTimeUtils;

public class AdminCarLoansService {
	public String reviewCarLoanApplication(AdminCarLoansInfo adminCarLoansInfo) throws Exception {
		if (AdminCarLoansDao.changeStatusById(adminCarLoansInfo.getId(), adminCarLoansInfo.getNewStatus()) != 1) {
			throw new Exception();
		}
		
		if (adminCarLoansInfo.getNewStatus().equals("Approved")) {
			String currentDate = DateTimeUtils.getCurrentDateTime();
			
	        CarLoanRepaymentInfo carLoanRepaymentInfo = new CarLoanRepaymentInfo();
	        carLoanRepaymentInfo.setUsername(adminCarLoansInfo.getUsername());
	        carLoanRepaymentInfo.setLoanId(adminCarLoansInfo.getId());
	        carLoanRepaymentInfo.setStartDate(getStartDate(currentDate));
	        carLoanRepaymentInfo.setHasStarted(0);
	        carLoanRepaymentInfo.setEndDate(getEndDate(carLoanRepaymentInfo.getStartDate(), adminCarLoansInfo.getDays()));
	        carLoanRepaymentInfo.setHasEnded(0);
	        carLoanRepaymentInfo.setEmi(adminCarLoansInfo.getDueAmount() / getDuration(carLoanRepaymentInfo.getStartDate(), carLoanRepaymentInfo.getEndDate()));
	        carLoanRepaymentInfo.setMisses(0);
	        carLoanRepaymentInfo.setPenalty(0.02 * carLoanRepaymentInfo.getEmi());
	        
	        if (AdminCarLoansDao.initializeRepayment(carLoanRepaymentInfo) != 1) {
	        	throw new Exception();
	        }
	        
			ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(adminCarLoansInfo.getUsername());
			double currentBalance = resultSet.next() ? resultSet.getDouble(1) : 0.0;
	        double newBalance = currentBalance + adminCarLoansInfo.getPrincipal();
	        String date = DateTimeUtils.getCurrentDateTime();
			
	        if (DepositDao.depositMoney(adminCarLoansInfo.getUsername(), date, adminCarLoansInfo.getPrincipal(), newBalance) != 1) {
	        	throw new Exception();
	        }
	        
	        AdminCarLoansDao.removeRejectedCarLoansByUsername(adminCarLoansInfo.getUsername());
	        
	        return adminCarLoansInfo.getUsername() + "'s loan application has been approved successfully!";
		} else {
	        return adminCarLoansInfo.getUsername() + "'s loan application has been rejected successfully!";
		}
	}

    private String getStartDate(String currentDate) {
    	String[] currentDateSplit = currentDate.split("-");
    	
    	String startDate = "";
    	if (Integer.parseInt(currentDateSplit[1]) + 2 > 12) {
    		startDate += (Integer.parseInt(currentDateSplit[0]) + 1) + "-" + ((Integer.parseInt(currentDateSplit[1]) + 2) % 12) + "-01";
    	} else {
    		startDate += currentDateSplit[0] + "-" + (Integer.parseInt(currentDateSplit[1]) + 2) + "-01";
    	}
    	
    	return startDate;
    }
    
    private String getEndDate(String startDateString, int days) throws Exception {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    	Date startDate = formatter.parse(startDateString);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(startDate);
    	calendar.add(Calendar.DATE, days);
    	String endDate = formatter.format(calendar.getTime());
    	endDate = endDate.substring(0, 8) + "01";
    	
    	return endDate;
    }
    
    private int getDuration(String startDate, String endDate) {
    	String[] startDateSplit = startDate.split("-");
    	String[] endDateSplit = endDate.split("-");
    	
    	int duration = 0;
    	if (Integer.parseInt(startDateSplit[1]) <= Integer.parseInt(endDateSplit[1])) {
    		duration += ((Integer.parseInt(endDateSplit[0]) - Integer.parseInt(startDateSplit[0])) * 12) + 
    				(Integer.parseInt(endDateSplit[1]) - Integer.parseInt(startDateSplit[1]));
    	} else {
    		duration += ((Integer.parseInt(endDateSplit[0]) - Integer.parseInt(startDateSplit[0]) - 1) * 12) + 
    				(12 - Integer.parseInt(startDateSplit[1])) + 
    				Integer.parseInt(endDateSplit[1]);
    	}

		return ++duration;
    }
}
