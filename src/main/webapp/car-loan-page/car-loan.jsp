<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.perfiosbank.utils.SessionUtils" %>
<%
	SessionUtils.updateSessionAttributes(request);
	String toHighlight = request.getRequestURI().split("/")[3];
	String status;
%>

<!DOCTYPE html>

<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<title>Car Loan</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="car-loan.css">

		<!-- Optional JavaScript -->
		<!-- jQuery first, then Popper.js, then Bootstrap JS -->
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
	</head>
	<body>
		<div class="page-container">
			<div class="header-container">
				<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
					<a class="navbar-brand" href="../landing-page/index.jsp">Perfios Bank</a>
					<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
						<span class="navbar-toggler-icon"></span>
					</button>
					<div class="collapse navbar-collapse" id="navbarNav">
						<% 
							Boolean isLoggedIn = (Boolean) request.getSession().getAttribute("isLoggedIn");
							if (!isLoggedIn) {
						%>
						<ul class="navbar-nav mx-auto">
						</ul>
						<ul class="navbar-nav show-right">
							<li id="signup">
								<a class="nav-link" href="../signup-page/signup.jsp">Signup</a>
							</li>
							<li id="login">
								<a class="nav-link" href="../login-page/login.jsp">Login</a>
							</li>
						</ul>
						<%
							} else {
							Boolean isAccountOpened = (Boolean) request.getSession().getAttribute("isAccountOpened");
							if (!isAccountOpened) {
						%>
						<ul class="navbar-nav mx-auto">				
							<li>
								<a class="nav-link" href="../open-account-page/open-account.jsp">Open Account</a>
							</li>
						</ul>
						
						<ul class="navbar-nav show-right">
							<li id="logout">
								<a class="nav-link" href="../logout-page/logout.jsp">Logout</a>
							</li>
						</ul>
						<%
							} else {
						%>
						<ul class="navbar-nav mx-auto">
							<li id="check-balance">
								<a class="nav-link" href="../check-balance-page/check-balance.jsp">Check Balance</a>
							</li>
							<li id="deposit">
								<a class="nav-link" href="../deposit-page/deposit.jsp">Deposit</a>
							</li>
							<li id="withdraw">	
								<a class="nav-link" href="../withdraw-page/withdraw.jsp">Withdraw</a>
							</li>
							<li id="transfer">
								<a class="nav-link" href="../transfer-page/transfer.jsp">Transfer</a>
							</li>
							<li id="past-transactions">
								<a class="nav-link" href="../past-transactions-page/past-transactions.jsp">View Past Transactions</a>
							</li>
							<li id="fixed-deposit">
								<a class="nav-link" href="../fixed-deposit-page/fixed-deposit.jsp">Fixed Deposit</a>
							</li>
							<li id="car-loan">
								<a class="nav-link" href="car-loan.jsp">Car Loan</a>
							</li>
							<li id="change-password">
								<a class="nav-link" href="../change-password-page/change-password.jsp">Change Password</a>
							</li>
							<li id="close-account">
								<a class="nav-link" href="../close-account-page/close-account.jsp">Close Account</a>
							</li>
						</ul>
						
						<ul class="navbar-nav show-right">
							<li id="logout">
								<a class="nav-link" href="../logout-page/logout.jsp">Logout</a>
							</li>
						</ul>
						<%
							}
						}
						%>
					</div>
				</nav>
			</div>
			
			<div class="content-container">
				<div class="card shadow-sm bg-body rounded" style="margin-right: 0;">
					<div class="card-body" style="text-align: center; margin-top: 30%;">
						<h1>Car Loan Interest Rates</h1>
						<br>
						<table class="table table-striped table-hover table-bordered">
							<thead>
								<tr class="table-dark">
									<th scope="col">CIBIL Score</th>
									<th scope="col">Interest Rate for 365 to 1825 days</th>
									<th scope="col">Interest Rate for above 1825 days</th>
								</tr>
							</thead>
							<tbody>
							    <tr>
							      <td>775 and above</td>
							      <td>7.85%</td>
							      <td>7.95%</td>
							    </tr>
							    <tr>
							      <td>750-774</td>
							      <td>8.20%</td>
							      <td>8.30%</td>
							    </tr>
							    <tr>
							      <td>700-749</td>
							      <td>8.45%</td>
							      <td>8.55%</td>
							    </tr>
							    <tr>
							      <td>300-699</td>
							      <td>8.80%</td>
							      <td>8.90%</td>
							    </tr>
						    </tbody>
					    </table>
					</div>
				</div>
				<div class="card shadow-sm bg-body rounded" style="margin-left: 0;">
					<div class="card-body" style="padding: 0">
						<div class="title-container">
							<h2 class="card-header">Apply For Car Loan Here!</h2>
						</div>
						<div style="padding: 16px">
							<form action="car-loan" method="post" enctype="multipart/form-data">
								<% 
									status = (String) request.getSession().getAttribute("authenticationException");
									if (status == null) {
								%>
								<div class="form-group">
									<label for="exampleInputUsername" class="card-title">Username</label>
									<input type="text" name="username" value="<%= (request.getParameter("username") == null) ? "" : request.getParameter("username") %>" class="form-control" id="exampleInputUsername" aria-describedby="usernameHelp" placeholder="Enter your username" required>
								</div>
								<br>
								<div class="form-group">
									<label for="exampleInputPassword" class="card-title">Password</label>
									<input type="password" name="password" value="<%= (request.getParameter("password") == null) ? "" : request.getParameter("password") %>" class="form-control" id="exampleInputPassword" aria-describedby="passwordHelp" placeholder="Enter your password" required>
								</div>
								<br>
								<%
									} else {
								%>
								<div class="form-group">
									<label for="exampleInputUsername" class="card-title">Username</label>
									<input type="text" name="username" value="<%= (request.getParameter("username") == null) ? "" : request.getParameter("username") %>" class="form-control is-invalid" id="exampleInputUsername" aria-describedby="usernameHelp" placeholder="Enter your username" required>
								</div>	
								<br>
								<div class="form-group">
									<label for="exampleInputPassword" class="card-title">Password</label>
									<input type="password" name="password" value="<%= (request.getParameter("password") == null) ? "" : request.getParameter("password") %>" class="form-control is-invalid" id="exampleInputPassword" aria-describedby="passwordHelp" placeholder="Enter your password" required>
								    <div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
								    </div>
								</div>
								<br>
								<%
									}
								%>
								<div class="form-group">
									<label for="exampleInputAmount">Car Loan Amount</label>
									<% 
										status = (String) request.getSession().getAttribute("amountException");
										if (status == null) {
									%>
									<input type="number" name="amount" step=".01" value="<%= (request.getParameter("amount") == null) ? "" : request.getParameter("amount") %>" class="form-control" id="exampleTargetAmount" placeholder="Enter your desired car loan amount" required>
									<%
										} else {
									%>
									<input type="number" name="amount" step=".01" value="<%= (request.getParameter("amount") == null) ? "" : request.getParameter("amount") %>" class="form-control is-invalid" id="exampleTargetAmount" placeholder="Enter your desired car loan amount" required>
								    <div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
							    	</div>
									<%
										}
									%>
								</div>
								<br>
						        <div class="form-group col-lg-12 col-sm-6">
						            <label for="endDate">Due Date</label>
									<% 
										status = (String) request.getSession().getAttribute("endDateException");
										if (status == null) {
									%>
						            <input name="endDate" id="endDate" class="form-control" value="<%= (request.getParameter("endDate") == null) ? "" : request.getParameter("endDate") %>" type="date" required/>
									<%
										} else {
									%>
						            <input name="endDate" id="endDate" class="form-control is-invalid" value="<%= (request.getParameter("endDate") == null) ? "" : request.getParameter("endDate") %>" type="date" required/>
									<div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
							    	</div>
									<%
										}
									%>
						        </div>
								<br>
								<div class="form-group">
									<label for="exampleInputCibil">CIBIL Score</label>
									<% 
										status = (String) request.getSession().getAttribute("cibilException");
										if (status == null) {
									%>
									<input type="number" name="cibil" min="300" max="900" step="1" pattern="\d+" value="<%= (request.getParameter("cibil") == null) ? "" : request.getParameter("cibil") %>" class="form-control" id="exampleInputCibil" placeholder="Enter your CIBIL score" required>
									<%
										} else {
									%>
									<input type="number" name="cibil" min="300" max="900" step="1" pattern="\d+" value="<%= (request.getParameter("cibil") == null) ? "" : request.getParameter("cibil") %>" class="form-control is-invalid" id="exampleInputCibil" placeholder="Enter your CIBIL score" required>
								    <div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
								    </div>
									<%
										}
									%>
								</div>
								<br>
								<div class="form-group">
									<label for="inputGroupFile02" class="card-title">CIBIL Report (in .pdf format)</label>
									<% 
										status = (String) request.getSession().getAttribute("cibilReportException");
										if (status == null) {
									%>
									<input type="file" name="cibilReport" class="form-control" id="inputGroupFile02" required>
									<%
										} else {
									%>
									<input type="file" name="cibilReport" class="form-control is-invalid" id="inputGroupFile02" required>
									<div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
								    </div>
									<%
										}
									%>
								</div>
								<br>
								<div class="form-group">
									<label for="inputGroupFile02" class="card-title">Identity Proof (such as Passport/ PAN Card/ Voters ID card/ Driving License etc in .pdf format)</label>
									<% 
										status = (String) request.getSession().getAttribute("identityProofException");
										if (status == null) {
									%>
									<input type="file" name="identityProof" class="form-control" id="inputGroupFile02" required>
									<%
										} else {
									%>
									<input type="file" name="identityProof" class="form-control is-invalid" id="inputGroupFile02" required>
									<div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
								    </div>
									<%
										}
									%>
								</div>
								<br>
								<div class="form-group">
									<label for="inputGroupFile02" class="card-title">Address Proof (such as Driving License/Voters ID card/Telephone Bill etc in .pdf format)</label>
									<% 
										status = (String) request.getSession().getAttribute("addressProofException");
										if (status == null) {
									%>
									<input type="file" name="addressProof" class="form-control" id="inputGroupFile02" required>
									<%
										} else {
									%>
									<input type="file" name="addressProof" class="form-control is-invalid" id="inputGroupFile02" required>
									<div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
								    </div>
									<%
										}
									%>
								</div>
								<br>
								<div class="form-group">
									<label for="inputGroupFile02" class="card-title">Income Proof (latest salary slip in .pdf format)</label>
									<% 
										status = (String) request.getSession().getAttribute("incomeProofException");
										if (status == null) {
									%>
									<input type="file" name="incomeProof" class="form-control" id="inputGroupFile02" required>
									<%
										} else {
									%>
									<input type="file" name="incomeProof" class="form-control is-invalid" id="inputGroupFile02" required>
									<div class="invalid-feedback">
								    	<%
											out.println(status);
								    	%>
								    </div>
									<%
										}
									%>
								</div>
								<br>
								<div class="btn-container">
									<button type="submit" class="btn btn-success">Apply Now</button>
									
								</div>
								<% 
									status = (String) request.getSession().getAttribute("success");
									if (status != null) {
								%>
								<br>
							    <div class="valid-status-container">
							    	<%
										out.println(status);
							    	%>
							    </div>
								<%
									}
									
									status = (String) request.getSession().getAttribute("otherException");
									if (status != null) {
								%>
								<br>
							    <div class="invalid-status-container">
							    	<%
										out.println(status);
							    	%>
							    </div>
								<%
									}
								%>
							</form>
						</div>
						<div style="text-align: center">
							<a href="/PerfiosBank/GetCarLoansController">View your car loans instead?</a>
						</div>
						<br>
					</div>
				</div>
			</div>
			
			<div class="bg-dark text-white footer-container">
				<span>(C) 2022 Perfios Bank. All rights reserved. </span>
                <span class="show-right">Made with <span style="color: #e25555;">❤</span> by Venkatesh</span>
		    </div>
		</div>
		
		<script type="text/javascript">
		    function preventBack() { 
		    	window.history.forward(); 
		    }
		    setTimeout("preventBack()", 0);
		    window.onunload = function () { null };
    
			function highlight(toHighlight) {
				deselect();
				var id = toHighlight.split(".")[0];
				if (id === "index") {
					return;
				}
				var li = document.getElementById(id);
				li.style.backgroundColor = "white";
				li.childNodes[0].nextSibling.style.color = "black";
			}
	
			function deselect() {
				var liElements = document.getElementsByTagName("li");
				for (var li of liElements) {
					li.className = ""
				}
			}
			
			highlight('<%= toHighlight %>');
		</script>
	</body>
</html>
