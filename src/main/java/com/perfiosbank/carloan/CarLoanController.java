package com.perfiosbank.carloan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.DurationRangeException;
import com.perfiosbank.exceptions.EndDateInvalidException;
import com.perfiosbank.exceptions.FileInvalidException;
import com.perfiosbank.model.CarLoanInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.FileUtils;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/car-loan-page/car-loan")
@MultipartConfig
public class CarLoanController extends HttpServlet {
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
		String password = request.getParameter("password");
		double loanAmount = Double.parseDouble(request.getParameter("amount"));
		int days = Integer.parseInt(request.getParameter("days"));
		System.out.println(days);
		int cibilScore = Integer.parseInt(request.getParameter("cibil"));
		
		final String[] FILENAMES = {"cibilReport", "identityProof", "addressProof", "incomeProof"};
		HashMap<String, byte[]> uploadedFiles = new HashMap<>();
		List<String> uploadedFilenames = new ArrayList<String>();
		for (String FILENAME : FILENAMES) {
			Part filePart = request.getPart(FILENAME);
			uploadedFilenames.add(FILENAME + "," + filePart.getSubmittedFileName());
		    InputStream fileContent = filePart.getInputStream();
		    byte[] fileContentInBytes = FileUtils.readAllBytes(fileContent);
		    uploadedFiles.put(FILENAME, fileContentInBytes);
		}
	    
		CarLoanInfo carLoanInfo = new CarLoanInfo();
		carLoanInfo.setUsername(username);
		carLoanInfo.setPassword(password);
		carLoanInfo.setLoanAmount(loanAmount);
		carLoanInfo.setDays(days);
		carLoanInfo.setCibilScore(cibilScore);
		carLoanInfo.setUploadedFiles(uploadedFiles);
		carLoanInfo.setUploadedFilenames(uploadedFilenames);
		
		try {
			CarLoanService carLoanService = new CarLoanService();
			carLoanService.applyCarLoan(userInSession, carLoanInfo);
			request.getSession().setAttribute("success", "You have applied for your car loan successfully!");	
		} catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
        } catch(AmountInvalidException amountInvalidException) {
			request.getSession().setAttribute("amountException", amountInvalidException.getMessage());
        } catch(EndDateInvalidException | DurationRangeException endDateExceptions) {
        	request.getSession().setAttribute("endDateException", endDateExceptions.getMessage());
        } catch(FileInvalidException fileInvalidException) {
        	String[] filenameAndMessage = fileInvalidException.getMessage().split(",");
        	switch(filenameAndMessage[0]) {
        		case "cibilReport":
                	request.getSession().setAttribute("cibilReportException", filenameAndMessage[1]);
        			break;
        		case "identityProof":
                	request.getSession().setAttribute("identityProofException", filenameAndMessage[1]);
        			break;
        		case "addressProof":
                	request.getSession().setAttribute("addressProofException", filenameAndMessage[1]);
        			break;
        		default:
                	request.getSession().setAttribute("incomeProofException", filenameAndMessage[1]);
        			break;
        	}
        } catch(Exception e) {
        	e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to process your car loan application at the "
					+ "moment! Try again later.");
		} finally {
			RequestDispatcher rd = request.getRequestDispatcher("car-loan.jsp");
			rd.include(request, response);
		}
	}
}
