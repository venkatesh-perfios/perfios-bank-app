package com.perfiosbank.login;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AccountFrozenException;
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

			ResultSet resultSet = LoginDao.getAccountCountByUsername(username);
			if (resultSet == null) {
				throw new Exception();
			}
			
			resultSet.next();
			if (resultSet.getInt(1) == 0) {
				request.getSession().setAttribute("isAccountOpened", false);
			} else {
				request.getSession().setAttribute("isAccountOpened", true);
			}
			request.getSession().setAttribute("success", "You have logged in successfully!");
			request.getSession().setAttribute("isLoggedIn", true);
			request.getSession().setAttribute("usernameInSession", userInNewSession.getUsername());
			request.getSession().setAttribute("passwordInSession", userInNewSession.getPassword());
			response.sendRedirect("/PerfiosBank/landing-page/index.jsp");
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
			response.sendRedirect("login.jsp");
		} catch(AccountFrozenException accountFrozenException) {
			request.getSession().setAttribute("otherException", accountFrozenException.getMessage());
			response.sendRedirect("login.jsp");
		} catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to login at the moment! Try again later.");
			response.sendRedirect("login.jsp");
		}
	}
}
