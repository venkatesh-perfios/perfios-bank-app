package com.perfiosbank.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/login-page/login")
public class LoginController extends HttpServlet {
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
			LoginService loginService = new LoginService();
			User userInNewSession = loginService.loginUser(enteredDetails);
			request.getSession().setAttribute("success", "You have logged in successfully!");
			request.getSession().setAttribute("isLoggedIn", true);
			request.getSession().setAttribute("usernameInSession", userInNewSession.getUsername());
			request.getSession().setAttribute("passwordInSession", userInNewSession.getPassword());
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
		} catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to login at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
			rd.include(request, response);
		}
	}
}
