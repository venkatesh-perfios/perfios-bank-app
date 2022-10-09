package com.perfiosbank.withdraw;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.InsufficientBalanceException;
import com.perfiosbank.model.DepositWithdrawInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/withdraw-page/withdraw")
public class WithdrawController extends HttpServlet {
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
		double amount = Double.parseDouble(request.getParameter("amount"));
		
		DepositWithdrawInfo withdrawInfo = new DepositWithdrawInfo();
		withdrawInfo.setUsername(username);
		withdrawInfo.setPassword(password);
		withdrawInfo.setAmount(amount);
		
		try {
			WithdrawService withdrawService = new WithdrawService();
			withdrawService.withdrawMoney(userInSession, withdrawInfo);
			request.getSession().setAttribute("success", "You have withdrawn Rs. " + amount + " from your account successfully!");
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(AmountInvalidException | AmountLimitReachedException | InsufficientBalanceException | 
        		BelowMinBalanceException amountExceptions) {
			request.getSession().setAttribute("amountException", amountExceptions.getMessage());
        } catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to withdraw money from your account at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("withdraw.jsp");
			rd.include(request, response);
		}  
	}
}
