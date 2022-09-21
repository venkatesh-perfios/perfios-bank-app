<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.perfiosbank.utils.SessionUtils" %>
<%
	request.getSession().setAttribute("refresh", true);
	SessionUtils.updateSessionAttributes(request);
	String toHighlight = request.getRequestURI().split("/")[3];
%>

<!DOCTYPE html>

<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<title>Welcome!</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="index.css">

		<!-- Optional JavaScript -->
		<!-- jQuery first, then Popper.js, then Bootstrap JS -->
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
	</head>
	<body>
		<nav class="navbar navbar-expand-lg navbar-light bg-light">
			<a class="navbar-brand" href="index.jsp">Perfios Bank</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav mx-auto">
					<li>
						<a class="nav-link" href="../open-account-page/open-account.jsp">Open Account</a>
					</li>
					<li>
						<a class="nav-link" href="../balance-page/balance.jsp">Check Balance</a>
					</li>
					<li>
						<a class="nav-link" href="../deposit-page/deposit.jsp">Deposit</a>
					</li>
					<li>
						<a class="nav-link" href="../withdraw-page/withdraw.jsp">Withdraw</a>
					</li>
					<li>
						<a class="nav-link" href="../transfer-page/transfer.jsp">Transfer</a>
					</li>
					<li>
						<a class="nav-link" href="../past-transactions-page/past-transactions.jsp">View Past Transactions</a>
					</li>
					<li>
						<a class="nav-link" href="../change-password-page/change-password.jsp">Change Password</a>
					</li>
					<li>
						<a class="nav-link" href="../close-account/close-account.jsp">Close Account</a>
					</li>
				</ul>
				<ul class="navbar-nav show-right">
					<% 
						Boolean isLoggedIn = (Boolean) request.getSession().getAttribute("isLoggedIn");
						if (!isLoggedIn) {
					%>
					<li>
						<a class="nav-link" href="../signup-page/signup.jsp">Signup</a>
					</li>
					<li>
						<a class="nav-link" href="../login-page/login.jsp">Login</a>
					</li>
					<%
						} else {
					%>
					<li>
						<a class="nav-link" href="../logout-page/logout.jsp">Logout</a>
					</li>
					<%
						}
					%>
				</ul>
			</div>
		</nav>

		<script type="text/javascript">
			function highlight(toHighlight) {
				deselect();
				var id = toHighlight.split(".")[0];
				if (id === "index") {
					return;
				}
				var li = document.getElementById(id);
				li.className = "active"
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
    