package com.perfiosbank.signup;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.InvalidPasswordException;
import com.perfiosbank.exceptions.PasswordMismatchException;
import com.perfiosbank.exceptions.UsernameAlreadyExistsException;
import com.perfiosbank.exceptions.UsernameTooLongException;
import com.perfiosbank.model.User;
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
			request.getSession().setAttribute("success", "You have signed up successfully!");
			request.getSession().setAttribute("isLoggedIn", true);
			request.getSession().setAttribute("usernameInSession", username);
			request.getSession().setAttribute("passwordInSession", password);
		} catch(UsernameAlreadyExistsException | UsernameTooLongException usernameException) {
			request.getSession().setAttribute("usernameException", usernameException.getMessage());
		} catch (InvalidPasswordException passwordException) {
			request.getSession().setAttribute("passwordException", passwordException.getMessage());
		} catch(PasswordMismatchException reenterPasswordException) {
			request.getSession().setAttribute("reenterPasswordException", reenterPasswordException.getMessage());
		} catch (Exception e) {
			request.getSession().setAttribute("otherException", "Unable to signup at the moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("signup.jsp");
			rd.include(request, response);
		}
	}
}
