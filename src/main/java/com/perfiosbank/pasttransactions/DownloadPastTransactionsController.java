package com.perfiosbank.pasttransactions;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.perfiosbank.model.PastTransactionsInfo;
import com.perfiosbank.openaccount.OpenAccountDao;
import com.perfiosbank.utils.DateTimeUtils;

@WebServlet("/past-transactions-page/download-past-transactions")
public class DownloadPastTransactionsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		OutputStream outputStream = response.getOutputStream();

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=Past_Transactions.pdf");
	    
	    try {
		    Document document = new Document();
	        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
	        document.open();
	 
	        
	        Paragraph title = new Paragraph("Past Transactions", new Font(FontFamily.HELVETICA, 20, Font.BOLD));
	        title.setAlignment(Element.ALIGN_CENTER);
	        
	        
	        PdfPTable userInfoTable = new PdfPTable(3);
	        userInfoTable.setWidthPercentage(100);
	        userInfoTable.setSpacingBefore(10f);
	        userInfoTable.setSpacingAfter(10f);

	        float[] userInfoTableColumnWidths = {2f, 1f, 2f};
	        userInfoTable.setWidths(userInfoTableColumnWidths);

	        Font bold = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
	        Font regular = new Font(FontFamily.HELVETICA, 14);
	        
	        String usernameInSession = (String) request.getSession().getAttribute("usernameInSession");
	        ResultSet accountDetails = OpenAccountDao.getAccountByUsername(usernameInSession);
	        accountDetails.next();
	        String currentDateTime = DateTimeUtils.getCurrentDateTime();
	        
	        userInfoTable.addCell(getUserInfoCell("Full Name: " + accountDetails.getString(4) + " " + accountDetails.getString(5), regular, Element.ALIGN_LEFT, Element.ALIGN_TOP));
	        userInfoTable.addCell(getUserInfoCell("", regular, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
	        userInfoTable.addCell(getUserInfoCell("Username: " + usernameInSession, regular, Element.ALIGN_LEFT, Element.ALIGN_TOP));
	        userInfoTable.addCell(getUserInfoCell("Account Number: " + accountDetails.getString(1), regular, Element.ALIGN_LEFT, Element.ALIGN_TOP));
	        userInfoTable.addCell(getUserInfoCell("", regular, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
	        userInfoTable.addCell(getUserInfoCell("Date and Time of Generation: " + currentDateTime, regular, Element.ALIGN_LEFT, Element.ALIGN_TOP));
	        
	        
	        PdfPTable pastTransactionsTable = new PdfPTable(4);
	        pastTransactionsTable.setWidthPercentage(100);
	        pastTransactionsTable.setSpacingBefore(10f);
	        pastTransactionsTable.setSpacingAfter(10f);
	 
	        float[] pastTransactionsTableColumnWidths = {2f, 1f, 1f, 1f};
	        pastTransactionsTable.setWidths(pastTransactionsTableColumnWidths);

	        PdfPCell dateAndTimeHeader = getPastTransactionCell("Date and Time", bold);
	        PdfPCell typeHeader = getPastTransactionCell("Transaction Type", bold);
	        PdfPCell amountHeader = getPastTransactionCell("Amount", bold);
	        PdfPCell balanceHeader = getPastTransactionCell("Balance", bold);
	 
	        pastTransactionsTable.addCell(dateAndTimeHeader);
	        pastTransactionsTable.addCell(typeHeader);
	        pastTransactionsTable.addCell(amountHeader);
	        pastTransactionsTable.addCell(balanceHeader);
	        
	        List<PastTransactionsInfo> pastTransactions = (List<PastTransactionsInfo>) request.getSession().getAttribute("pastTransactions");
	        for (PastTransactionsInfo pastTransaction : pastTransactions) {
	        	PdfPCell dateAndTimeContent = getPastTransactionCell(pastTransaction.getDateAndTime(), regular);
	        	PdfPCell typeContent = getPastTransactionCell(pastTransaction.getType(), regular);
	        	PdfPCell amountContent = getPastTransactionCell("Rs. " + pastTransaction.getAmount(), regular);
	        	PdfPCell balanceContent = getPastTransactionCell("Rs. " + pastTransaction.getBalance(), regular);

		        pastTransactionsTable.addCell(dateAndTimeContent);
		        pastTransactionsTable.addCell(typeContent);
		        pastTransactionsTable.addCell(amountContent);
		        pastTransactionsTable.addCell(balanceContent);
	        }

	        document.add(title);
	        document.add(new Paragraph("\n"));
	        document.add(userInfoTable);
	        document.add(new Paragraph("\n"));
	        document.add(pastTransactionsTable);
	
	        document.close();
	        writer.close();
	    } catch(Exception e) {
			request.getSession().setAttribute("otherException", "Unable to download your past transactions "
					+ "at the moment! Try again later.");
		}
	}
	
	private PdfPCell getUserInfoCell(String content, Font font, int horizontalAlignment, int verticalAlignment) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        cell.setBorder(0);
        
        return cell;
	}
	
	private PdfPCell getPastTransactionCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        return cell;
	}
}
