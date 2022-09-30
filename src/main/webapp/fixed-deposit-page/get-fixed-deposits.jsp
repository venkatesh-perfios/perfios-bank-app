<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.perfiosbank.utils.SessionUtils, java.sql.ResultSet" %>
<%
	SessionUtils.updateSessionAttributes(request);
	String toHighlight = "fixed-deposit";
	String status;
%>

<!DOCTYPE html>

<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<title>Fixed Deposit</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="fixed-deposit.css">
		

		<!-- Optional JavaScript -->
		<!-- jQuery first, then Popper.js, then Bootstrap JS -->
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
	</head>
	<body>
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
						<a class="nav-link" href="fixed-deposit.jsp">Fixed Deposit</a>
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
	
		<%
			ResultSet resultSet = (ResultSet) request.getSession().getAttribute("fixedDeposits");
			if (resultSet == null || !resultSet.next()) {
		%>
			<div class="card center" style="width: 45%; margin: 15% auto;">
				<div class="card-body">
					<div class="title-container">
						<h2 class="card-header" style="color: red">You don't have any active FD account(s) at the moment!</h2>
					</div>
					<div class="">
						<a href="fixed-deposit.jsp">Open a fixed deposit account now?</a>
					</div>
				</div>
			</div>
		<%
			} else {
		%>
			<div class="content-container">
				<div class="card center">
					<div class="card-body">
						<div class="title-container">
							<h2 class="card-header">Here You Go!</h2>
						</div>
						<table class="table table-striped table-hover table-bordered">
							<thead>
								<tr class="table-dark">
									<th scope="col">Principal Amount</th>
									<th scope="col">Maturity Date</th>
									<th scope="col">Interest Rate</th>
									<th scope="col">Interest Amount</th>
									<th scope="col">Maturity Amount</th>
								</tr>
							</thead>
							<tbody>
							<%
								do {
							%>
								    <tr>
								      <td><%= resultSet.getDouble(3) %></td>
								      <td><%= resultSet.getString(4) %></td>
								      <td><%= resultSet.getDouble(5) %></td>
								      <td><%= (double) Math.round((resultSet.getDouble(6) - resultSet.getDouble(3)) * 100) / 100 %></td>
								      <td><%= resultSet.getDouble(6) %></td>
								    </tr>
							<%
								} while (resultSet.next());
							%>
							</tbody>
						</table>
						<br>
						<div class="center">
							<a href="fixed-deposit.jsp">Open another fixed deposit account?</a>
						</div>
					</div>
				</div>
			</div>
		<%
			}
		%>

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
