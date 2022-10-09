package com.perfiosbank.adminaccounts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.model.AdminAccountsInfo;
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
		
		AdminAccountsInfo adminAccountsInfo = new AdminAccountsInfo();
		adminAccountsInfo.setAccountNumber(accountNumber);
		adminAccountsInfo.setUsername(username);
		adminAccountsInfo.setAmount(amount);
		adminAccountsInfo.setNewStatus(newStatus);
		
		try {
			AdminAccountsService adminAccountsService = new AdminAccountsService();
			String message = adminAccountsService.reviewAccountApplication(adminAccountsInfo);
			request.getSession().setAttribute(newStatus, message);
		} catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to change the status "
					+ "at the moment! Try again later.");
		} finally {
			response.sendRedirect("admin-accounts.jsp");
		}
	}
}
