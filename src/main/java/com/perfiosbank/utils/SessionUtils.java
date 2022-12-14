package com.perfiosbank.utils;

import javax.servlet.http.HttpServletRequest;

public class SessionUtils {
	public static void updateSessionAttributes(HttpServletRequest request) {
		Boolean refresh = (Boolean) request.getSession().getAttribute("refresh");

		if (refresh) {
			Boolean isLoggedIn = (Boolean) request.getSession().getAttribute("isLoggedIn");
			Boolean isAccountOpened = (Boolean) request.getSession().getAttribute("isAccountOpened");
			String usernameInSession = (String) request.getSession().getAttribute("usernameInSession");
			String passwordInSession = (String) request.getSession().getAttribute("passwordInSession");
			request.getSession().invalidate();
			if (isLoggedIn == null) {
				request.getSession().setAttribute("isLoggedIn", false);
			} else {
				request.getSession().setAttribute("isLoggedIn", isLoggedIn);
			}
			if (isAccountOpened == null) {
				request.getSession().setAttribute("isAccountOpened", false);
			} else {
				request.getSession().setAttribute("isAccountOpened", isAccountOpened);
			}
			request.getSession().setAttribute("usernameInSession", usernameInSession);
			request.getSession().setAttribute("passwordInSession", passwordInSession);
			request.getSession().setAttribute("refresh", false);
		} else {
			request.getSession().setAttribute("refresh", true);
		}
	}
}
