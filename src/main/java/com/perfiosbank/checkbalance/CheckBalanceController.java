package com.perfiosbank.checkbalance;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/check-balance-page/check-balance")
public class CheckBalanceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");

		User userInSession = new User();
		userInSession.setUsername((String) request.getSession().getAttribute("usernameInSession"));
		userInSession.setPassword((String) request.getSession().getAttribute("passwordInSession"));
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		User enteredDetails = new User();
		enteredDetails.setUsername(username);
		enteredDetails.setPassword(password);
	
		try {
			CheckBalanceService checkBalanceService = new CheckBalanceService();
			double balance = checkBalanceService.checkBalance(userInSession, enteredDetails);
			request.getSession().setAttribute("success", "Your balance: " + balance);
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to display your balance at the moment! Try again later.");
		} finally {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("check-balance.jsp");
			requestDispatcher.include(request, response);
		}
	}
}
