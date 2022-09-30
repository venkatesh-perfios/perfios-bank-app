package com.perfiosbank.adminaccounts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.closeaccount.CloseAccountDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.DateTimeUtils;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/admin-accounts-page/change-status")
public class AdminAccountsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		String accountNumber = request.getParameter("accountNumber");
		String username = request.getParameter("username");
		double amount = Double.parseDouble(request.getParameter("amount"));
		String newStatus = request.getParameter("newStatus");
		
		try {
			if (AdminAccountsDao.changeStatusByAccountNumber(accountNumber, newStatus) != 1) {
				throw new Exception();
			}
			
			if (newStatus.equals("Approved")) {
				double currentBalance = 0.0;
		        double newBalance = currentBalance + amount;
		        String date = DateTimeUtils.getCurrentDateTime();
				
		        if (DepositDao.depositMoney(username, date, amount, newBalance) != 1) {
		        	throw new Exception();
		        }
		        
				request.getSession().setAttribute(newStatus, username + "'s account opening application has been approved "
						+ "successfully!");
			} else {
				User userInSession = new User();
				userInSession.setUsername(username);
				if (CloseAccountDao.removeUser(userInSession) != 1) {
					throw new Exception();
				}
				
				request.getSession().setAttribute(newStatus, username + "'s account opening application has been rejected "
						+ "successfully!");
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to change the status "
					+ "at the moment! Try again later.");
		} finally {
			response.sendRedirect("admin-accounts.jsp");
		}
	}
}
