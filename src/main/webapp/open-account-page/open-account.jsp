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

		<title>Open Account</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="open-account.css">

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
						<a class="nav-link" href="open-account.jsp">Open Account</a>
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
					<h2 class="card-header">Open Your Account Here!</h2>
				</div>
				<form action="open-account" method="post">
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
						<label for="exampleInputFirstName">First Name</label>
						<input type="text" name="firstName" value="<%= (request.getParameter("firstName") == null) ? "" : request.getParameter("firstName") %>" class="form-control" id="exampleInputFirstName" aria-describedby="firstNameHelp" placeholder="Enter your first name" required>
					</div>
					<br>
					<div class="form-group">
						<label for="exampleInputLastName">Last Name</label>
						<input type="text" name="lastName" value="<%= (request.getParameter("lastName") == null) ? "" : request.getParameter("lastName") %>" class="form-control" id="exampleInputLastName" aria-describedby="lastNameHelp" placeholder="Enter your last name" required>
					</div>
					<br>
					<div class="form-group">
						<label for="exampleInputAadhaar">Aadhaar Number</label>
						<% 
							status = (String) request.getSession().getAttribute("aadhaarException");
							if (status == null) {
						%>
						<input type="number" name="aadhaar" value="<%= (request.getParameter("aadhaar") == null) ? "" : request.getParameter("aadhaar") %>" class="form-control" id="exampleInputAadhaar" placeholder="Enter your aadhaar number" required>
						<%
							} else {
						%>
						<input type="text" name="aadhaar" value="<%= (request.getParameter("aadhaar") == null) ? "" : request.getParameter("aadhaar") %>" class="form-control is-invalid" id="exampleInputAadhaar" placeholder="Enter your aadhaar number" required>
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
						<label for="exampleInputPan">PAN</label>
						<% 
							status = (String) request.getSession().getAttribute("panException");
							if (status == null) {
						%>
						<input type="text" name="pan" value="<%= (request.getParameter("pan") == null) ? "" : request.getParameter("pan") %>" class="form-control" id="examplePan" placeholder="Enter your PAN" required>
						<%
							} else {
						%>
						<input type="text" name="pan" value="<%= (request.getParameter("pan") == null) ? "" : request.getParameter("pan") %>" class="form-control is-invalid" id="examplePan" placeholder="Enter your PAN" required>
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
						<label for="exampleInputAddress">Address</label>
						<textarea name="address" rows="4" cols="10" class="form-control" id="exampleInputAddress" aria-describedby="addressHelp" placeholder="Enter your address" required><%= (request.getParameter("address") == null) ? "" : request.getParameter("address") %></textarea>
					</div>
					<br>
					<div class="form-group">
						<label for="exampleInputPhone">Phone Number</label>
						<% 
							status = (String) request.getSession().getAttribute("phoneException");
							if (status == null) {
						%>
						<input type="number" name="phone" value="<%= (request.getParameter("phone") == null) ? "" : request.getParameter("phone") %>" class="form-control" id="examplePhone" placeholder="Enter your phone number" required>
						<%
							} else {
						%>
						<input type="number" name="phone" value="<%= (request.getParameter("phone") == null) ? "" : request.getParameter("phone") %>" class="form-control is-invalid" id="examplePhone" placeholder="Enter your phone number" required>
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
						<label for="exampleInputAmount">Amount</label>
						<% 
							status = (String) request.getSession().getAttribute("amountException");
							if (status == null) {
						%>
						<input type="number" name="amount" value="<%= (request.getParameter("amount") == null) ? "" : request.getParameter("amount") %>" class="form-control" id="exampleAmount" placeholder="Enter your amount to deposit" required>
						<%
							} else {
						%>
						<input type="number" name="amount" value="<%= (request.getParameter("amount") == null) ? "" : request.getParameter("amount") %>" class="form-control is-invalid" id="exampleAmount" placeholder="Enter your amount to deposit" required>
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
						<button type="submit" class="btn btn-success">Open Account</button>
						
					</div>
					<br>
					<% 
						status = (String) request.getSession().getAttribute("success");
						if (status != null) {
					%>
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