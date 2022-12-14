package com.perfiosbank.pasttransactions;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.PastTransactionsInfo;
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
			
			List<PastTransactionsInfo> pastTransactions = new ArrayList<>();
			while (resultSet.next()) {
				PastTransactionsInfo pastTransaction = new PastTransactionsInfo();
				pastTransaction.setDateAndTime(resultSet.getString(1));
				pastTransaction.setType(resultSet.getString(2));
				pastTransaction.setAmount(resultSet.getDouble(3));
				pastTransaction.setBalance(resultSet.getDouble(4));
				pastTransactions.add(pastTransaction);
			}
			request.getSession().setAttribute("pastTransactions", pastTransactions);
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to display your past transactions "
					+ "at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("past-transactions.jsp");
			rd.include(request, response);
		}
	}
}
