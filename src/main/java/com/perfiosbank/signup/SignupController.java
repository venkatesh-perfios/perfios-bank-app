package com.perfiosbank.signup;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.PasswordInvalidException;
import com.perfiosbank.exceptions.PasswordMismatchException;
import com.perfiosbank.exceptions.UsernameAlreadyExistsException;
import com.perfiosbank.exceptions.UsernameTooLongException;
import com.perfiosbank.login.LoginDao;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.AuthenticationUtils;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/signup-page/signup")
public class SignupController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String reenteredPassword = request.getParameter("reenteredPassword");
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		
		try {
			SignupService signupService = new SignupService();
			signupService.signupUser(user, reenteredPassword);

			ResultSet resultSet = LoginDao.getApprovedAccountCountByUsername(username);
			if (resultSet == null) {
				throw new Exception();
			}
			
			resultSet.next();
			if (resultSet.getInt(1) == 0) {
				request.getSession().setAttribute("isAccountOpened", false);
			} else {
				request.getSession().setAttribute("isAccountOpened", true);
			}
			
			request.getSession().setAttribute("success", "You have signed up successfully!");
			request.getSession().setAttribute("isLoggedIn", true);
			request.getSession().setAttribute("usernameInSession", username);
			request.getSession().setAttribute("passwordInSession",  AuthenticationUtils.encryptPassword(password));
			response.sendRedirect("/PerfiosBank/landing-page/index.jsp");
		} catch(UsernameAlreadyExistsException | UsernameTooLongException usernameException) {
			request.getSession().setAttribute("usernameException", usernameException.getMessage());
			response.sendRedirect("signup.jsp");
		} catch (PasswordInvalidException passwordException) {
			request.getSession().setAttribute("passwordException", passwordException.getMessage());
			response.sendRedirect("signup.jsp");
		} catch(PasswordMismatchException reenterPasswordException) {
			request.getSession().setAttribute("reenterPasswordException", reenterPasswordException.getMessage());
			response.sendRedirect("signup.jsp");
		} catch (Exception e) {
			request.getSession().setAttribute("otherException", "Unable to signup at the moment! Try again later.");
			response.sendRedirect("signup.jsp");
		}
	}
}
