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

		<title>Signup</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="signup.css">

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
				<ul class="navbar-nav mx-auto">
					<li id="open-account">
						<a class="nav-link" href="../open-account-page/open-account.jsp">Open Account</a>
					</li>
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
					<li id="change-password">
						<a class="nav-link" href="../change-password-page/change-password.jsp">Change Password</a>
					</li>
					<li id="close-account">
						<a class="nav-link" href="../close-account-page/close-account.jsp">Close Account</a>
					</li>
				</ul>
				<ul class="navbar-nav show-right">
					<% 
						Boolean isLoggedIn = (Boolean) request.getSession().getAttribute("isLoggedIn");
						if (!isLoggedIn) {
					%>
					<li id="signup">
						<a class="nav-link" href="signup.jsp">Signup</a>
					</li>
					<li id="login">
						<a class="nav-link" href="../login-page/login.jsp">Login</a>
					</li>
					<%
						} else {
					%>
					<li id="logout">
						<a class="nav-link" href="../logout-page/logout.jsp">Logout</a>
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
					<h2 class="card-header">Lets Get Started!</h2>
				</div>
				<form action="signup" method="post">
					<div class="form-group">
						<label for="exampleInputUsername" class="card-title">Username</label>
						<% 
							status = (String) request.getSession().getAttribute("usernameException");
							if (status == null) {
						%>
						<input type="text" name="username" value="<%= (request.getParameter("username") == null) ? "" : request.getParameter("username") %>" class="form-control" id="exampleInputUsername" aria-describedby="usernameHelp" placeholder="Enter your username" required>
						<%
							} else {
						%>
						<input type="text" name="username" value="<%= (request.getParameter("username") == null) ? "" : request.getParameter("username") %>" class="form-control is-invalid" id="exampleInputUsername" aria-describedby="usernameHelp" placeholder="Enter your username" required>
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
						<label for="exampleInputPassword" class="card-title">Password</label>
						<% 
							status = (String) request.getSession().getAttribute("passwordException");
							if (status == null) {
						%>
						<input type="password" name="password" value="<%= (request.getParameter("password") == null) ? "" : request.getParameter("password") %>" class="form-control" id="exampleInputPassword" aria-describedby="passwordHelp" placeholder="Enter your password" required>
						<%
							} else {
						%>
						<input type="password" name="password" value="<%= (request.getParameter("password") == null) ? "" : request.getParameter("password") %>" class="form-control is-invalid" id="exampleInputPassword" aria-describedby="passwordHelp" placeholder="Enter your password" required>
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
						<label for="exampleInputReenterPassword" class="card-title">Re-enter Password</label>
						<% 
							status = (String) request.getSession().getAttribute("reenterPasswordException");
							if (status == null) {
						%>
						<input type="password" name="reenteredPassword" value="<%= (request.getParameter("reenteredPassword") == null) ? "" : request.getParameter("reenteredPassword") %>" class="form-control" id="exampleInputReenterPassword" placeholder="Re-enter your password" required>
						<%
							} else {
						%>
						<input type="password" name="reenteredPassword" value="<%= (request.getParameter("reenteredPassword") == null) ? "" : request.getParameter("reenteredPassword") %>" class="form-control is-invalid" id="exampleInputReenterPassword" placeholder="Re-enter your password" required>
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
						<button type="submit" class="btn btn-success">Signup</button>
						
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

		<script type="text/javascript">
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
