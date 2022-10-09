package com.perfiosbank.fixeddeposit;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountRangeException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.DurationRangeException;
import com.perfiosbank.exceptions.EndDateInvalidException;
import com.perfiosbank.exceptions.InsufficientBalanceException;
import com.perfiosbank.model.FixedDepositInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/fixed-deposit-page/fixed-deposit")
public class FixedDepositController extends HttpServlet {
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
		double principal = Double.parseDouble(request.getParameter("amount"));
		String endDate = request.getParameter("endDate");
		
		FixedDepositInfo fixedDepositInfo = new FixedDepositInfo();
		fixedDepositInfo.setUsername(username);
		fixedDepositInfo.setPassword(password);
		fixedDepositInfo.setPrincipal(principal);
		fixedDepositInfo.setEndDate(endDate);
		
		try {
			FixedDepositService fixedDepositService = new FixedDepositService();
			fixedDepositService.startFixedDeposit(userInSession, fixedDepositInfo);
			request.getSession().setAttribute("success", "You have started your fixed deposit successfully!");	
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(AmountInvalidException | AmountRangeException | InsufficientBalanceException | 
				BelowMinBalanceException amountExceptions) {
			request.getSession().setAttribute("amountException", amountExceptions.getMessage());
        } catch(EndDateInvalidException | DurationRangeException endDateExceptions) {
        	request.getSession().setAttribute("endDateException", endDateExceptions.getMessage());
        } catch(Exception e) {
        	e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to start your Fixed Deposit "
					+ "account at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("fixed-deposit.jsp");
			rd.include(request, response);
		}
	}
}
