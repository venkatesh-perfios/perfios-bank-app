package com.perfiosbank.closeaccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.ActiveFixedDepositAccountsFoundException;
import com.perfiosbank.exceptions.ActiveLoansFoundException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/close-account-page/close-account")
public class CloseAccountController extends HttpServlet {
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

		User enteredDetails = new User();
		enteredDetails.setUsername(username);
		enteredDetails.setPassword(password);
		
		try {
			CloseAccountService closeAccountService = new CloseAccountService();
			closeAccountService.closeAccount(userInSession, enteredDetails);
			request.getSession().setAttribute("success", "You have closed your account successfully!");
			request.getSession().setAttribute("isAccountOpened", false);
			request.getSession().setAttribute("isLoggedIn", false);
			request.getSession().setAttribute("username", null);
			request.getSession().setAttribute("password", null);
			response.sendRedirect("/PerfiosBank/landing-page/index.jsp");
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
			response.sendRedirect("close-account.jsp");
        } catch(ActiveFixedDepositAccountsFoundException activeFixedDepositAccountsFoundException) {
			request.getSession().setAttribute("otherException", activeFixedDepositAccountsFoundException.getMessage());
			response.sendRedirect("close-account.jsp");
        } catch(ActiveLoansFoundException activeLoansFoundException) {
			request.getSession().setAttribute("otherException", activeLoansFoundException.getMessage());
			response.sendRedirect("close-account.jsp");
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to close your account money at the moment! Try again later.");
			response.sendRedirect("close-account.jsp");
		}
	}
}
