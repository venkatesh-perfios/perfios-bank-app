package com.perfiosbank.carloan;

import java.io.IOException;

import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.utils.SessionUtils;

@WebServlet("/car-loan-page/get-car-loans")
public class GetCarLoansController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);

		response.setContentType("text/html");
		
		try {
			String usernameInSession = (String) request.getSession().getAttribute("usernameInSession");
			ResultSet resultSet = CarLoanDao.getAllCarLoansByUsername(usernameInSession);
			request.getSession().setAttribute("carLoans", resultSet);
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to display your Car Loan(s) "
					+ "at the moment! Try again later.");
		} finally {
			response.sendRedirect("car-loan-page/get-car-loans.jsp");
		}
	}
}
