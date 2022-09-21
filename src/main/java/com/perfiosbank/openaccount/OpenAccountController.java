package com.perfiosbank.openaccount;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AadhaarInvalidException;
import com.perfiosbank.exceptions.AccountAlreadyExistsException;
import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.PanInvalidException;
import com.perfiosbank.exceptions.PhoneInvalidException;
import com.perfiosbank.model.AccountInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/open-account-page/open-account")
public class OpenAccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		User userInSession = new User();
		userInSession.setUsername((String) request.getSession().getAttribute("usernameInSession"));
		userInSession.setPassword((String) request.getSession().getAttribute("passwordInSession"));
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		long aadhaar = Long.parseLong(request.getParameter("aadhaar"));
		String pan = request.getParameter("pan");
		String address = request.getParameter("address");
		long phone = Long.parseLong(request.getParameter("phone"));
		double amount = Long.parseLong(request.getParameter("amount"));
		
		User enteredDetails = new User();
		enteredDetails.setUsername(username);
		enteredDetails.setPassword(password);
		
		AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFirstName(firstName);
        accountInfo.setLastName(lastName);
        accountInfo.setAadhaar(aadhaar);
        accountInfo.setPan(pan);
        accountInfo.setAddress(address);
        accountInfo.setPhone(phone);
        accountInfo.setAmount(amount);
        
        try {
        	OpenAccountService openAccountService = new OpenAccountService();
        	openAccountService.openAccount(userInSession, enteredDetails, accountInfo);
			request.getSession().setAttribute("success", "You have opened an account successfully!");
        } catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(AadhaarInvalidException aadhaarInvalidException) {
			request.getSession().setAttribute("aadhaarException", aadhaarInvalidException.getMessage());
		} catch(PanInvalidException panInvalidException) {
			request.getSession().setAttribute("panException", panInvalidException.getMessage());
		} catch(PhoneInvalidException phoneInvalidExcetion) {
			request.getSession().setAttribute("phoneException", phoneInvalidExcetion.getMessage());
		} catch(AmountInvalidException | BelowMinBalanceException | AmountLimitReachedException 
				amountExceptions) {
			request.getSession().setAttribute("amountException", amountExceptions.getMessage());
		} catch(AccountAlreadyExistsException | AccountNotFoundException accountExceptions) {
			request.getSession().setAttribute("otherException", accountExceptions.getMessage());
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to open your account at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("open-account.jsp");
			rd.include(request, response);
		}   
	}
}
