<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.perfiosbank.utils.SessionUtils, java.sql.ResultSet,
	com.perfiosbank.openaccount.OpenAccountDao" %>
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
							<li id="open-account">
								<a class="nav-link" href="open-account.jsp">Open Account</a>
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
	
		<%
			ResultSet resultSet = OpenAccountDao.getPendingAccountByUsername(
					(String) request.getSession().getAttribute("usernameInSession"));
			boolean ans = resultSet.next();
			if(!ans) {
		%>
		
			<div class="content-container">
				<div class="card center">
					<div class="card-body">
						<div class="title-container">
							<h2 class="card-header">Open Your Account Here!</h2>
						</div>
						<form action="open-account" method="post" enctype="multipart/form-data">
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
								<label for="inputGroupFile02" class="card-title">Photograph (in .pdf format)</label>
								<% 
									status = (String) request.getSession().getAttribute("photoException");
									if (status == null) {
								%>
								<input type="file" name="photo" class="form-control" id="inputGroupFile02" required>
								<%
									} else {
								%>
								<input type="file" name="photo" class="form-control is-invalid" id="inputGroupFile02" required>
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
								<label for="exampleInputFirstName">First Name</label>
								<% 
									status = (String) request.getSession().getAttribute("firstNameException");
									if (status == null) {
								%>
								<input type="text" name="firstName" value="<%= (request.getParameter("firstName") == null) ? "" : request.getParameter("firstName") %>" class="form-control" id="exampleInputFirstName" aria-describedby="firstNameHelp" placeholder="Enter your first name" required>
								<%
									} else {
								%>
								<input type="text" name="firstName" value="<%= (request.getParameter("firstName") == null) ? "" : request.getParameter("firstName") %>" class="form-control is-invalid" id="exampleInputFirstName" aria-describedby="firstNameHelp" placeholder="Enter your first name" required>
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
								<label for="exampleInputLastName">Last Name</label>
								<% 
									status = (String) request.getSession().getAttribute("lastNameException");
									if (status == null) {
								%>
								<input type="text" name="lastName" value="<%= (request.getParameter("lastName") == null) ? "" : request.getParameter("lastName") %>" class="form-control" id="exampleInputLastName" aria-describedby="lastNameHelp" placeholder="Enter your last name" required>
								<%
									} else {
								%>
								<input type="text" name="lastName" value="<%= (request.getParameter("lastName") == null) ? "" : request.getParameter("lastName") %>" class="form-control is-invalid" id="exampleInputLastName" aria-describedby="lastNameHelp" placeholder="Enter your last name" required>
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
								<label for="exampleInputAge">Age</label>
								<% 
									status = (String) request.getSession().getAttribute("ageException");
									if (status == null) {
								%>
								<input type="number" name="age" min="18" max="125" step="1" pattern="\d+" value="<%= (request.getParameter("age") == null) ? "" : request.getParameter("age") %>" class="form-control" id="exampleInputAge" placeholder="Enter your age" required>
								<%
									} else {
								%>
								<input type="number" name="age" min="18" max="125" step="1" pattern="\d+" value="<%= (request.getParameter("age") == null) ? "" : request.getParameter("age") %>" class="form-control is-invalid" id="exampleInputAge" placeholder="Enter your age" required>
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
								<label for="exampleInputAadhaar">Aadhaar Number</label>
								<% 
									status = (String) request.getSession().getAttribute("aadhaarException");
									if (status == null) {
								%>
								<input type="number" name="aadhaar" value="<%= (request.getParameter("aadhaar") == null) ? "" : request.getParameter("aadhaar") %>" class="form-control" id="exampleInputAadhaar" placeholder="Enter your aadhaar number" required>
								<%
									} else {
								%>
								<input type="number" name="aadhaar" value="<%= (request.getParameter("aadhaar") == null) ? "" : request.getParameter("aadhaar") %>" class="form-control is-invalid" id="exampleInputAadhaar" placeholder="Enter your aadhaar number" required>
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
								<label for="inputGroupFile02" class="card-title">Aadhaar Proof (in .pdf format)</label>
								<% 
									status = (String) request.getSession().getAttribute("aadhaarProofException");
									if (status == null) {
								%>
								<input type="file" name="aadhaarProof" class="form-control" id="inputGroupFile02" required>
								<%
									} else {
								%>
								<input type="file" name="aadhaarProof" class="form-control is-invalid" id="inputGroupFile02" required>
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
								<label for="inputGroupFile02" class="card-title">PAN Proof (in .pdf format)</label>
								<% 
									status = (String) request.getSession().getAttribute("panProofException");
									if (status == null) {
								%>
								<input type="file" name="panProof" class="form-control" id="inputGroupFile02" required>
								<%
									} else {
								%>
								<input type="file" name="panProof" class="form-control is-invalid" id="inputGroupFile02" required>
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
								<input type="number" name="phone" value="<%= (request.getParameter("phone") == null) ? "" : request.getParameter("phone") %>" class="form-control" id="exampleInputPhone" placeholder="Enter your phone number" required>
								<%
									} else {
								%>
								<input type="number" name="phone" value="<%= (request.getParameter("phone") == null) ? "" : request.getParameter("phone") %>" class="form-control is-invalid" id="exampleInputPhone" placeholder="Enter your phone number" required>
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
								<input type="number" name="amount" step=".01" value="<%= (request.getParameter("amount") == null) ? "" : request.getParameter("amount") %>" class="form-control" id="exampleInputAmount" placeholder="Enter your amount to deposit" required>
								<%
									} else {
								%>
								<input type="number" name="amount" step=".01" value="<%= (request.getParameter("amount") == null) ? "" : request.getParameter("amount") %>" class="form-control is-invalid" id="exampleInputAmount" placeholder="Enter your amount to deposit" required>
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
							<%
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
			
			<div class="bg-dark text-white footer-container">
				<span>(C) 2022 Perfios Bank. All rights reserved. </span>
                <span class="show-right hide-below-600">Made with <span style="color: #e25555;">❤</span> by Venkatesh</span>
		    </div>
		<%
			} else {
		%>
			<div class="content-container">
				<div class="card center" style="width: 50%; margin-top: 15%">
					<div class="card-body">
						<div class="title-container">
							<h2 class="card-header">
								Here's your temporary account number: <%= resultSet.getString(1) %>.
								<br>
								<br>
								It will become your permanent account number upon approval from our side.
								<br>
								<br>
								If rejected, your account will be automatically deleted.
							</h2>
						</div>
					</div>
				</div>
			</div>
		<%
			}
		%>
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
