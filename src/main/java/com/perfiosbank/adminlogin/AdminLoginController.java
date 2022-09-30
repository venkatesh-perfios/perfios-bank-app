package com.perfiosbank.adminlogin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/admin-login-page/admin-login")
public class AdminLoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User enteredDetails = new User();
		enteredDetails.setUsername(username);
		enteredDetails.setPassword(password);
		
		try {
			AdminLoginService adminLoginService = new AdminLoginService();
			User userInNewSession = adminLoginService.loginUser(enteredDetails);

			request.getSession().setAttribute("success", "You have logged in successfully!");
			request.getSession().setAttribute("isLoggedIn", true);
			request.getSession().setAttribute("usernameInSession", userInNewSession.getUsername());
			request.getSession().setAttribute("passwordInSession", userInNewSession.getPassword());
			response.sendRedirect("/PerfiosBank/admin-accounts-page/admin-accounts.jsp");
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
			response.sendRedirect("admin-login.jsp");
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to login at the moment! Try again later.");
			response.sendRedirect("admin-login.jsp");
		}
	}
}
