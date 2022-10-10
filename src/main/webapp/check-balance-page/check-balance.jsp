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

		<title>Check Balance</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="check-balance.css">

		<!-- Optional JavaScript -->
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js"></script>
	</head>
	<body>
		<div class="page-container">
			<div class="header-container">
				<nav class="navbar navbar-expand-xl navbar-dark bg-dark">
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
						<ul class="navbar-nav show-right max-width">
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
						
						<ul class="navbar-nav show-right max-width">
							<li id="logout">
								<a class="nav-link" href="../logout-page/logout.jsp">Logout</a>
							</li>
						</ul>
						<%
							} else {
						%>
						<ul class="navbar-nav mx-auto">
							<li id="check-balance">
								<a class="nav-link" href="check-balance.jsp">Check Balance</a>
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
								<a class="nav-link" href="../car-loan-page/car-loan.jsp">Car Loan</a>
							</li>
							<li id="change-password">
								<a class="nav-link" href="../change-password-page/change-password.jsp">Change Password</a>
							</li>
							<li id="close-account">
								<a class="nav-link" href="../close-account-page/close-account.jsp">Close Account</a>
							</li>
						</ul>
						
						<ul class="navbar-nav show-right max-width">
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
				<div class="card center shadow-sm bg-body rounded" style="margin-right: 0;">
					<div class="card-body">
						<img src="../images/check_balance.jpg" class="image">
					</div>
				</div>
				<div class="card center shadow-sm bg-body rounded" style="margin-left: 0;">
					<div class="card-body" style="padding: 0">
						<div class="title-container">
							<h2 class="card-header">Check Your Balance Here!</h2>
						</div>
						<div style="padding: 16px">
							<form action="check-balance" method="post">
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
								<br>
								<br>
								<div class="btn-container">
									<button type="submit" class="btn btn-success">Check Balance</button>
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
					</div>
				</div>
			</div>
			
			<div class="bg-dark text-white footer-container">
				<span>(C) 2022 Perfios Bank. All rights reserved. </span>
                <span class="show-right hide-below-600">Made with <span style="color: #e25555;">❤</span> by Venkatesh</span>
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
