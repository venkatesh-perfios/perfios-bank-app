package com.perfiosbank.transfer;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.TargetAccountNumberSameAsUserException;
import com.perfiosbank.model.TransferInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/transfer-page/transfer")
public class TransferController extends HttpServlet {
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
		String targetAccountNumber = request.getParameter("target");
		double amount = Double.parseDouble(request.getParameter("amount"));
		
        TransferInfo transferInfo = new TransferInfo();
        transferInfo.setUsername(username);
        transferInfo.setPassword(password);
        transferInfo.setTargetAccountNumber(targetAccountNumber);
        transferInfo.setAmount(amount);
        
        try {
        	TransferService transferService = new TransferService();
        	transferService.transferMoney(userInSession, transferInfo);
			request.getSession().setAttribute("success", "You have transferred Rs. " + amount + 
					" into account " + targetAccountNumber + " successfully!");
        } catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(AmountInvalidException | AmountLimitReachedException | BelowMinBalanceException 
        		amountExceptions) {
			request.getSession().setAttribute("amountException", amountExceptions.getMessage());
        } catch(TargetAccountNumberSameAsUserException | AccountNotFoundException targetExceptions) {
			request.getSession().setAttribute("targetException", targetExceptions.getMessage());
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to transfer Rs. " + amount + 
					" into account " + targetAccountNumber + " at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("transfer.jsp");
			rd.include(request, response);
		}
	}
}
