package com.perfiosbank.admincarloans;

import java.io.IOException;

import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.utils.DateTimeUtils;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/admin-car-loans-page/change-status")
public class AdminCarLoansController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		int id = Integer.parseInt(request.getParameter("id"));
		String username = request.getParameter("username");
		double principal = Double.parseDouble(request.getParameter("principal"));
		String newStatus = request.getParameter("newStatus");
		
		try {
			if (AdminCarLoansDao.changeStatusById(id, newStatus) != 1) {
				throw new Exception();
			}
			
			if (newStatus.equals("Approved")) {
				ResultSet resultSet = CheckBalanceDao.getCurrentBalance(username);
				double currentBalance = resultSet.next() ? resultSet.getDouble(1) : 0.0;
		        double newBalance = currentBalance + principal;
		        String date = DateTimeUtils.getCurrentDateTime();
				
		        if (DepositDao.depositMoney(username, date, principal, newBalance) != 1) {
		        	throw new Exception();
		        }
		        
				request.getSession().setAttribute(newStatus, username + "'s loan application has been approved "
						+ "successfully!");
			} else {
				request.getSession().setAttribute(newStatus, username + "'s loan application has been rejected "
						+ "successfully!");
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to change the status "
					+ "at the moment! Try again later.");
		} finally {
			response.sendRedirect("admin-car-loans.jsp");
		}
	}
}
