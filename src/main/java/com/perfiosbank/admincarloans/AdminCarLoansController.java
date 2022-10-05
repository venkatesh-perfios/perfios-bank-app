package com.perfiosbank.admincarloans;

import java.io.IOException;

import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.perfiosbank.checkbalance.CheckBalanceDao;
import com.perfiosbank.deposit.DepositDao;
import com.perfiosbank.model.CarLoanRepaymentInfo;
import com.perfiosbank.utils.DateTimeUtils;
import com.perfiosbank.utils.SessionUtils;

@WebServlet("/admin-car-loans-page/change-status")
public class AdminCarLoansController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionUtils.updateSessionAttributes(request);
		request.getSession().setAttribute("refresh", false);
		
		response.setContentType("text/html");
		
		int id = Integer.parseInt(request.getParameter("id"));
		String username = request.getParameter("username");
		double principal = Double.parseDouble(request.getParameter("principal"));
		String newStatus = request.getParameter("newStatus");
		String dueDate = request.getParameter("dueDate");
		double dueAmount = Double.parseDouble(request.getParameter("dueAmount"));
		
		try {
			if (AdminCarLoansDao.changeStatusById(id, newStatus) != 1) {
				throw new Exception();
			}
			
			if (newStatus.equals("Approved")) {
				String currentDate = DateTimeUtils.getCurrentDateTime();
				
		        CarLoanRepaymentInfo carLoanRepaymentInfo = new CarLoanRepaymentInfo();
		        carLoanRepaymentInfo.setUsername(username);
		        carLoanRepaymentInfo.setLoanId(id);
		        carLoanRepaymentInfo.setStartDate(getStartDate(currentDate));
		        carLoanRepaymentInfo.setHasStarted(0);
		        carLoanRepaymentInfo.setEndDate(dueDate.substring(0, 8) + "01");
		        carLoanRepaymentInfo.setHasEnded(0);
		        carLoanRepaymentInfo.setEmi(dueAmount / getDuration(carLoanRepaymentInfo.getStartDate(), carLoanRepaymentInfo.getEndDate()));
		        carLoanRepaymentInfo.setMisses(0);
		        carLoanRepaymentInfo.setPenalty(0.02 * carLoanRepaymentInfo.getEmi());
		        
		        if (AdminCarLoansDao.initializeRepayment(carLoanRepaymentInfo) != 1) {
		        	throw new Exception();
		        }
		        
		        if (AdminCarLoansDao.removeRejectedCarLoansByUsername(username) != 1) {
		        	throw new Exception();
		        }
		        
				ResultSet resultSet = CheckBalanceDao.getCurrentBalanceByUsername(username);
				double currentBalance = resultSet.next() ? resultSet.getDouble(1) : 0.0;
		        double newBalance = currentBalance + principal;
		        String date = DateTimeUtils.getCurrentDateTime();
				
		        if (DepositDao.depositMoney(username, date, principal, newBalance) != 1) {
		        	throw new Exception();
		        }
		        
				request.getSession().setAttribute(newStatus, username + "'s loan application has been approved "
						+ "successfully!");
			} else {
				request.getSession().setAttribute(newStatus, username + "'s loan application has been rejected "
						+ "successfully!");
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("otherException", "Unable to change the status "
					+ "at the moment! Try again later.");
		} finally {
			response.sendRedirect("admin-car-loans.jsp");
		}
	}
	
    private String getStartDate(String currentDate) {
    	String[] currentDateSplit = currentDate.split("-");
    	
    	String startDate = "";
    	if (Integer.parseInt(currentDateSplit[1]) + 2 > 12) {
    		startDate += (Integer.parseInt(currentDateSplit[0]) + 1) + "-" + ((Integer.parseInt(currentDateSplit[1]) + 2) % 12) + "-01";
    	} else {
    		startDate += currentDateSplit[0] + "-" + (Integer.parseInt(currentDateSplit[1]) + 2) + "-01";
    	}
    	
    	return startDate;
    }
    
    private int getDuration(String startDate, String endDate) {
    	String[] startDateSplit = startDate.split("-");
    	String[] endDateSplit = endDate.split("-");
    	
    	int duration = 0;
    	if (Integer.parseInt(startDateSplit[1]) <= Integer.parseInt(endDateSplit[1])) {
    		duration += ((Integer.parseInt(endDateSplit[0]) - Integer.parseInt(startDateSplit[0])) * 12) + 
    				(Integer.parseInt(endDateSplit[1]) - Integer.parseInt(startDateSplit[1]));
    	} else {
    		duration += ((Integer.parseInt(endDateSplit[0]) - Integer.parseInt(startDateSplit[0]) - 1) * 12) + 
    				(12 - Integer.parseInt(startDateSplit[1])) + 
    				Integer.parseInt(endDateSplit[1]);
    	}

		return ++duration;
    }
}
