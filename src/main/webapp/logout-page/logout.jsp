<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.perfiosbank.utils.SessionUtils" %>
<%
	SessionUtils.updateSessionAttributes(request);
	request.getSession().setAttribute("isLoggedIn", false);
	request.getSession().setAttribute("username", null);
	request.getSession().setAttribute("password", null);
	String status;
%>

<!DOCTYPE html>

<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<title>Logout</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="logout.css">

		<!-- Optional JavaScript -->
		<!-- jQuery first, then Popper.js, then Bootstrap JS -->
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
	</head>
	<body>
		<nav class="navbar navbar-expand-lg navbar-light bg-light">
			<a class="navbar-brand" href="../landing-page/index.jsp">Perfios Bank</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav mx-auto">
					<li id="open-account">
						<a class="nav-link" href="../open-account-page/open-account.jsp">Open Account</a>
					</li>
					<li id="check-balance">
						<a class="nav-link" href="../balance-page/balance.jsp">Check Balance</a>
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
					<li id="change-password">
						<a class="nav-link" href="../change-password-page/change-password.jsp">Change Password</a>
					</li>
					<li id="close-account">
						<a class="nav-link" href="../close-account/close-account.jsp">Close Account</a>
					</li>
				</ul>
				<ul class="navbar-nav show-right">
					<% 
						Boolean isLoggedIn = (Boolean) request.getSession().getAttribute("isLoggedIn");
						if (!isLoggedIn) {
					%>
					<li id="signup">
						<a class="nav-link" href="../signup-page/signup.jsp">Signup</a>
					</li>
					<li id="login">
						<a class="nav-link" href="../login-page/login.jsp">Login</a>
					</li>
					<%
						} else {
					%>
					<li id="logout">
						<a class="nav-link" href="logout.jsp">Logout</a>
					</li>
					<%
						}
					%>
				</ul>
			</div>
		</nav>

		<div class="card center">
			<div class="card-body">
				<div class="title-container">
					<h2 class="card-header">You have been logged out successfully!</h2>
				</div>
			</div>
		</div>
	</body>
</html>
