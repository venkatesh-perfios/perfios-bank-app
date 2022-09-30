package com.perfiosbank.changepassword;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.InvalidPasswordException;
import com.perfiosbank.exceptions.NewPasswordSameAsCurrentException;
import com.perfiosbank.exceptions.PasswordMismatchException;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/change-password-page/change-password")
public class ChangePasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		User userInSession = new User();
		userInSession.setUsername((String) request.getSession().getAttribute("usernameInSession"));
		userInSession.setPassword((String) request.getSession().getAttribute("passwordInSession"));
		
		String username = request.getParameter("username");
		String currentPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String reenteredNewPassword = request.getParameter("reenteredPassword");
		
        User enteredDetails = new User();
        enteredDetails.setUsername(username);
        enteredDetails.setPassword(currentPassword);
	
        try {
        	ChangePasswordService changePasswordService = new ChangePasswordService();
        	User updatedUserInSession = changePasswordService.changePassword(userInSession, enteredDetails, newPassword, 
        			reenteredNewPassword);
        	
			request.getSession().setAttribute("success", "You have changed your password successfully!");
			request.getSession().setAttribute("passwordInSession", updatedUserInSession.getPassword());
        } catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException",
					authenticationFailedException.getMessage());
		} catch (NewPasswordSameAsCurrentException | InvalidPasswordException newPasswordExceptions) {
			request.getSession().setAttribute("newPasswordException", newPasswordExceptions.getMessage());
		} catch(PasswordMismatchException reenterPasswordException) {
			request.getSession().setAttribute("reenterPasswordException", 
					reenterPasswordException.getMessage());
		} catch (Exception e) {
			request.getSession().setAttribute("otherException", "Unable to change your password at the "
					+ "moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("change-password.jsp");
			rd.include(request, response);
		}
	}
}
