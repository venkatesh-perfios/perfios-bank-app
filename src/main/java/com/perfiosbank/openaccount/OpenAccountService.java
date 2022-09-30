package com.perfiosbank.openaccount;

import java.sql.ResultSet;


import java.util.Arrays;
import java.util.regex.Pattern;

import com.perfiosbank.exceptions.AadhaarInvalidException;
import com.perfiosbank.exceptions.AccountAlreadyExistsException;
import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.FileInvalidException;
import com.perfiosbank.exceptions.PanInvalidException;
import com.perfiosbank.exceptions.PhoneInvalidException;
import com.perfiosbank.model.AccountInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AccountUtils;
import com.perfiosbank.utils.AuthenticationUtils;

public class OpenAccountService {
    public String openAccount(User userInSession, User enteredDetails, AccountInfo accountInfo) 
    		throws AccountAlreadyExistsException, AccountNotFoundException, AadhaarInvalidException, 
    		PanInvalidException, PhoneInvalidException, AmountInvalidException, BelowMinBalanceException, 
    		AuthenticationFailedException, AmountLimitReachedException, FileInvalidException, Exception {
		String msg;
			
		if (AuthenticationUtils.isUserNotAuthenticated(userInSession, enteredDetails)) {
			msg = "Authentication failed! Please re-check your username/password.";
			throw new AuthenticationFailedException(msg);
		}
		
		if (isAccountAlreadyExists(userInSession)) {
			msg = "You already have an account!";
			throw new AccountAlreadyExistsException(msg);
		}
		
		if (isAadhaarInvalid(accountInfo.getAadhaar())) {
			msg = "Please enter a valid, 12-digit Aadhar card number!";
			throw new AadhaarInvalidException(msg);
		}
		
		if (isPanInvalid(accountInfo.getPan(), accountInfo)) {
			msg = "Please enter a valid, 10-character PAN card number!";
			throw new PanInvalidException(msg);
		}
		
		if (isPhoneInvalid(accountInfo.getPhone())) {
			msg = "Please enter a valid, 10-digit phone number!\nDo not enter the country code or any special " +
			  "characters (such as spaces, hyphens, parentheses etc).";
			throw new PhoneInvalidException(msg);
		}
		
		if (AccountUtils.isAmountInvalid(accountInfo.getAmount())) {
			msg = "Please enter a valid amount! Here are some tips:<br>" +
			  "1. Don't enter any alphabet or special character<br>" +
			  "2. Don't enter 0 as the amount to deposit<br>" +
			  "3. Don't enter more than 2 numbers after the decimal point";
			throw new AmountInvalidException(msg);
		}
		
		if (AccountUtils.isBelowMinBalance(accountInfo.getAmount())) {
			msg = "You must maintain a minimum balance of Rs. 1000 at all times!";
			throw new BelowMinBalanceException(msg);
		}
		
        for (String filename : accountInfo.getUploadedFilenames()) {
        	String type = filename.split(",")[0];
        	String actualFilename = filename.split(",")[1];
	        if (isFilenameInvalid(actualFilename)) {
	        	msg = type + ", Please upload a pdf file!";
	        	throw new FileInvalidException(msg);
	        }
        }
		
		String newAccountNumber = generateAccountNumber();
		
		if (OpenAccountDao.openAccount(newAccountNumber, userInSession, accountInfo) != 1) {
			throw new Exception();
		}

//		DepositWithdrawInfo depositInfo = new DepositWithdrawInfo();
//		depositInfo.setUsername(enteredDetails.getUsername());
//		depositInfo.setPassword(enteredDetails.getPassword());
//		depositInfo.setAmount(accountInfo.getAmount());
//		new DepositService().depositMoney(userInSession, depositInfo);
		
		return newAccountNumber;
	}
	
	private String generateAccountNumber() throws Exception {
		final String BASE_ACCOUNT_NUMBER = "PBIN";
		final long HUNDRED_CRORES = 100_00_00_000;
		ResultSet resultSet = OpenAccountDao.getMaxAccountNumber();
		resultSet.next();
		
		String maxAccountNumber = resultSet.getString(1);
		String newAccountNumber;
		if (maxAccountNumber == null) {
			newAccountNumber = BASE_ACCOUNT_NUMBER + HUNDRED_CRORES;
		} else {
			newAccountNumber = BASE_ACCOUNT_NUMBER + (Long.parseLong(maxAccountNumber.substring(4)) + 1);
		}
		
		return newAccountNumber;
	}
		
	private boolean isAccountAlreadyExists(User userInSession) throws Exception {
		return OpenAccountDao.getAccountByUsername(userInSession.getUsername()).next();
	}
		
	private boolean isAadhaarInvalid(long aadhar) {
		return Long.toString(aadhar).length() != 12;
	}
		
	private boolean isPanInvalid(String pan, AccountInfo accountInfo) {
		if (pan.length() != 10) {
			return true;
		} else {
			for (int i = 0; i < 10; ++i) {
				char currentLetter = pan.charAt(i);
				
				switch (i) {
				  case 0:
				  case 1:
				  case 2:
				  case 9:
				      if (!(currentLetter >= 'A' && currentLetter <= 'Z')) {
				          return true;
				      }
				      break;
				  case 3:
				      final String[] VALID_CHARS = new String[] {"P", "C", "H", "A", "B", "G", "J", "L", "F", "T"};
				      if (!Arrays.asList(VALID_CHARS).contains(Character.toString(currentLetter))) {
				          return true;
				      }
				      break;
				  case 4:
				      if (pan.charAt(3) == 'P') {
				          if (!(currentLetter == accountInfo.getLastName().charAt(0))) {
				              return true;
				          }
				      } else {
				          if (!(currentLetter == accountInfo.getFirstName().charAt(0))) {
				              return true;
				          }
				      }
				      break;
				  case 5:
				  case 6:
				  case 7:
				  case 8:
				      if (!(currentLetter >= '1' && currentLetter <= '9')) {
				          return true;
				      }
				      break;
				}
			}
		}
		return false;
	}

	private boolean isPhoneInvalid(long phone) {
		return !Pattern.matches("^\\d{10}$", Long.toString(phone));
	}

	private boolean isFilenameInvalid(String filename) {
		return !(filename.substring(filename.length() - 3).equals("pdf"));
	}	
}
