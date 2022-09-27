package com.perfiosbank.fixeddeposit;

import java.io.IOException;

import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.utils.SessionUtils;

@WebServlet("/fixed-deposit-page/get-fixed-deposits")
public class GetFixedDepositsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);

		response.setContentType("text/html");
		
		try {
			String usernameInSession = (String) request.getSession().getAttribute("usernameInSession");
			ResultSet resultSet = FixedDepositDao.getAllFixedDepositAccountsByUsername(usernameInSession);
			request.getSession().setAttribute("fixedDeposits", resultSet);
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to display your Fixed Deposit(s) "
					+ "account at the moment! Try again later.");
		} finally {
			response.sendRedirect("fixed-deposit-page/get-fixed-deposits.jsp");
		}
	}
}
