package com.perfiosbank.adminaccounts;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.utils.SessionUtils;

@WebServlet("/admin-accounts-page/download-file")
public class AdminAccountsDownloadFileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);

		String accountNumber = request.getParameter("accountNumber");
		String filename = request.getParameter("filename");
		OutputStream outputStream = response.getOutputStream();

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + accountNumber + "_" + filename + ".pdf");
	
		try {
			ResultSet resultSet = AdminAccountsDao.getAllAccountsByAccountNumber(accountNumber);
			resultSet.next();
            Blob blob = resultSet.getBlob(filename);
            byte[] blobInBytes = blob.getBytes(1, (int) blob.length());
            outputStream.write(blobInBytes);
        } catch (Exception e) {
			request.getSession().setAttribute("otherException", "Unable to download at the moment! "
					+ "Try again later.");
		}
	}
}
