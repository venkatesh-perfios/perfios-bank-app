package com.perfiosbank.openaccount;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.perfiosbank.exceptions.AadhaarInvalidException;
import com.perfiosbank.exceptions.AccountAlreadyExistsException;
import com.perfiosbank.exceptions.AmountInvalidException;
import com.perfiosbank.exceptions.AmountLimitReachedException;
import com.perfiosbank.exceptions.AuthenticationFailedException;
import com.perfiosbank.exceptions.BelowMinBalanceException;
import com.perfiosbank.exceptions.FileInvalidException;
import com.perfiosbank.exceptions.NameInvalidException;
import com.perfiosbank.exceptions.PanInvalidException;
import com.perfiosbank.exceptions.PhoneInvalidException;
import com.perfiosbank.model.AccountInfo;
import com.perfiosbank.model.User;
import com.perfiosbank.utils.FileUtils;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/open-account-page/open-account")
@MultipartConfig
public class OpenAccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		User userInSession = new User();
		userInSession.setUsername((String) request.getSession().getAttribute("usernameInSession"));
		userInSession.setPassword((String) request.getSession().getAttribute("passwordInSession"));
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		int age = Integer.parseInt(request.getParameter("age"));
		long aadhaar = Long.parseLong(request.getParameter("aadhaar"));
		String pan = request.getParameter("pan");
		String address = request.getParameter("address");
		long phone = Long.parseLong(request.getParameter("phone"));
		double amount = Long.parseLong(request.getParameter("amount"));
		
		final String[] fileTypes = {"photo", "aadhaarProof", "panProof"};
		HashMap<String, byte[]> uploadedFiles = new HashMap<>();
		List<String> uploadedFilenames = new ArrayList<String>();
		for (String fileType : fileTypes) {
			Part filePart = request.getPart(fileType);
			uploadedFilenames.add(fileType + "," + filePart.getSubmittedFileName());
		    InputStream fileContent = filePart.getInputStream();
		    byte[] fileContentInBytes = FileUtils.readAllBytes(fileContent);
		    uploadedFiles.put(fileType, fileContentInBytes);
		}
	    
		User enteredDetails = new User();
		enteredDetails.setUsername(username);
		enteredDetails.setPassword(password);
		
		AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFirstName(firstName);
        accountInfo.setLastName(lastName);
        accountInfo.setAge(age);
        accountInfo.setAadhaar(aadhaar);
        accountInfo.setPan(pan);
        accountInfo.setAddress(address);
        accountInfo.setPhone(phone);
        accountInfo.setAmount(amount);
        accountInfo.setUploadedFiles(uploadedFiles);
        accountInfo.setUploadedFilenames(uploadedFilenames);
        
        try {
        	OpenAccountService openAccountService = new OpenAccountService();
        	String accountNumber = openAccountService.openAccount(userInSession, enteredDetails, accountInfo);
			request.getSession().setAttribute("success", "Here's your temporary account number: " 
        	+ accountNumber + ". It will become your permanent account number upon approval from our side");
			response.sendRedirect("open-account.jsp");
        } catch(AuthenticationFailedException authenticationFailedException) {
			request.getSession().setAttribute("authenticationException", authenticationFailedException.getMessage());
			response.sendRedirect("open-account.jsp");
        }catch(NameInvalidException nameInvalidException) {
        	String errorMessage = nameInvalidException.getMessage();
        	if (errorMessage.contains("first")) {
        		request.getSession().setAttribute("firstNameException", errorMessage);
        	} else {
        		request.getSession().setAttribute("lastNameException", errorMessage);
        	}
			response.sendRedirect("open-account.jsp");
		} catch(AadhaarInvalidException aadhaarInvalidException) {
			request.getSession().setAttribute("aadhaarException", aadhaarInvalidException.getMessage());
			response.sendRedirect("open-account.jsp");
		} catch(PanInvalidException panInvalidException) {
			request.getSession().setAttribute("panException", panInvalidException.getMessage());
			response.sendRedirect("open-account.jsp");
		} catch(PhoneInvalidException phoneInvalidExcetion) {
			request.getSession().setAttribute("phoneException", phoneInvalidExcetion.getMessage());
			response.sendRedirect("open-account.jsp");
		} catch(AmountInvalidException | BelowMinBalanceException | AmountLimitReachedException 
				amountExceptions) {
			request.getSession().setAttribute("amountException", amountExceptions.getMessage());
			response.sendRedirect("open-account.jsp");
		} catch(AccountAlreadyExistsException accountAlreadyExistsException) {
			request.getSession().setAttribute("otherException", accountAlreadyExistsException.getMessage());
			response.sendRedirect("open-account.jsp");
		} catch(FileInvalidException fileInvalidException) {
        	String[] filenameAndMessage = fileInvalidException.getMessage().split(",");
        	switch(filenameAndMessage[0]) {
        		case "photo":
                	request.getSession().setAttribute("photoException", filenameAndMessage[1]);
        			break;
        		case "aadhaarProof":
                	request.getSession().setAttribute("aadhaarProofException", filenameAndMessage[1]);
        			break;
    			default:
                	request.getSession().setAttribute("panProofException", filenameAndMessage[1]);
        			break;
        	}
			response.sendRedirect("open-account.jsp");
        } catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to open your account at the moment! Try again later.");
			response.sendRedirect("open-account.jsp");
		}
    }
}
