package com.perfiosbank.admincarloans;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.model.AdminCarLoansInfo;
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
		int days = Integer.parseInt(request.getParameter("days"));
		double dueAmount = Double.parseDouble(request.getParameter("dueAmount"));
		
		AdminCarLoansInfo adminCarLoansInfo = new AdminCarLoansInfo();
		adminCarLoansInfo.setId(id);
		adminCarLoansInfo.setUsername(username);
		adminCarLoansInfo.setPrincipal(principal);
		adminCarLoansInfo.setNewStatus(newStatus);
		adminCarLoansInfo.setDays(days);
		adminCarLoansInfo.setDueAmount(dueAmount);
		
		try {
			AdminCarLoansService adminCarLoansService = new AdminCarLoansService();
			String message = adminCarLoansService.reviewCarLoanApplication(adminCarLoansInfo);
			request.getSession().setAttribute(newStatus, message);
		} catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to change the status "
					+ "at the moment! Try again later.");
		} finally {
			response.sendRedirect("admin-car-loans.jsp");
		}
	}
}
