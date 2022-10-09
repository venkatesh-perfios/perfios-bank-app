package com.perfiosbank.adminaccounts;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.model.AdminAccountsInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DateTimeUtils;

public class AdminAccountsService {
	public String reviewAccountApplication(AdminAccountsInfo adminAccountsInfo) throws Exception {
		if (AdminAccountsDao.changeStatusByAccountNumber(adminAccountsInfo.getAccountNumber(), 
				adminAccountsInfo.getNewStatus()) != 1) {
			throw new Exception();
		}
		
		if (adminAccountsInfo.getNewStatus().equals("Approved")) {
			double currentBalance = 0.0;
	        double newBalance = currentBalance + adminAccountsInfo.getAmount();
	        String date = DateTimeUtils.getCurrentDateTime();
			
	        if (DepositDao.depositMoney(adminAccountsInfo.getUsername(), date, 
	        		adminAccountsInfo.getAmount(), newBalance) != 1) {
	        	throw new Exception();
	        }
	        
			return adminAccountsInfo.getUsername() + "'s account opening application has been approved successfully!";
		} else {
			User userInSession = new User();
			userInSession.setUsername(adminAccountsInfo.getUsername());
			if (CloseAccountDao.removeUser(userInSession) != 1) {
				throw new Exception();
			}
			
			return adminAccountsInfo.getUsername() + "'s account opening application has been rejected successfully!";
		}
	}
}
