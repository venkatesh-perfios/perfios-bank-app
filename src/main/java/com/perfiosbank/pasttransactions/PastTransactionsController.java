package com.perfiosbank.pasttransactions;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AccountNotFoundException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

import javax.servlet.annotation.WebServlet;

@WebServlet("/past-transactions-page/past-transactions")
public class PastTransactionsController extends HttpServlet {
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
			PastTransactionsService pastTransactionsService = new PastTransactionsService();
			ResultSet resultSet = pastTransactionsService.viewPastTransactions(userInSession, enteredDetails);
			request.getSession().setAttribute("pastTransactions", resultSet);
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(AccountNotFoundException accountNotFoundException) {
			request.getSession().setAttribute("otherException", accountNotFoundException.getMessage());
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to display your past transactions "
					+ "at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("past-transactions.jsp");
			rd.include(request, response);
		}  
	}
}
