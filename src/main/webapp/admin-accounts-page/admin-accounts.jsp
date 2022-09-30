<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.perfiosbank.utils.SessionUtils, java.sql.ResultSet, 
	com.perfiosbank.adminaccounts.AdminAccountsDao" %>
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

		<title>Admin Dashboard</title>

		<!-- Latest compiled and minified CSS -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="admin-accounts.css">
		
		<!-- Optional JavaScript -->
		<!-- jQuery first, then Popper.js, then Bootstrap JS -->
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
	</head>
	<body>
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
			<a class="navbar-brand" href="#">Perfios Bank</a>
			<ul class="navbar-nav mx-auto">
				<li id="admin-accounts">
					<a class="nav-link" href="admin-accounts.jsp">Accounts</a>
				</li>
				<li id="admin-car-loans">
					<a class="nav-link" href="../admin-car-loans-page/admin-car-loans.jsp">Car Loans</a>
				</li>
			</ul>
			<ul class="navbar-nav show-right">
				<li id="logout">
					<a class="nav-link" href="../logout-page/logout.jsp">Logout</a>
				</li>
			</ul>
		</nav>
	
		<%
			ResultSet resultSet = AdminAccountsDao.getAllAccounts();
			if (resultSet == null || !resultSet.next()) {
		%>
			<div class="card" style="width: 45%; margin: 15% auto; text-align: center">
				<div class="card-body">
					<div class="title-container">
						<h2 class="card-header" style="color: green">You don't have any active 
						account opening application(s) to review at the moment!</h2>
					</div>
				</div>
			</div>
		<%
			} else {
		%>
			<div class="content-container">
				<div class="card" style="width: 90%;">
					<div class="card-body" style="padding: 0">
						<div class="title-container">
							<h2 class="card-header">Account Opening Applications</h2>
						</div>
						<div style="padding: 16px">
							<table class="table table-striped table-hover table-bordered">
								<thead>
									<tr class="table-dark">
										<th scope="col">Account Number</th>
										<th scope="col">Username</th>
										<th scope="col">Photo</th>
										<th scope="col">First Name</th>
										<th scope="col">Last Name</th>
										<th scope="col">Age</th>
										<th scope="col">Aadhaar</th>
										<th scope="col">Aadhaar Proof</th>
										<th scope="col">PAN</th>
										<th scope="col">PAN Proof</th>
										<th scope="col">Address</th>
										<th scope="col">Phone Number</th>
										<th scope="col">Amount</th>
										<th scope="col">Accept</th>
										<th scope="col">Reject</th>
									</tr>
								</thead>
								<tbody>
								<%
									do {
								%>
									    <tr>
									      <td><%= resultSet.getString(1) %></td>
									      <td><%= resultSet.getString(2) %></td>
									      <td>
											<div class="btn-container">
												<form action="download-file" method="get">
													<input type="hidden" name="accountNumber" value="<%= resultSet.getString(1) %>" />
													<input type="hidden" name="filename" value="Photo" />
													<button type="submit" class="btn btn-primary">Download</button>
												</form>
											</div>
									      </td>
									      <td><%= resultSet.getString(4) %></td>
									      <td><%= resultSet.getString(5) %></td>
									      <td><%= resultSet.getInt(6) %></td>
									      <td><%= resultSet.getLong(7) %></td>
									      <td>
											<div class="btn-container">
												<form action="download-file" method="get">
													<input type="hidden" name="accountNumber" value="<%= resultSet.getString(1) %>" />
													<input type="hidden" name="filename" value="Aadhaar_Proof" />
													<button type="submit" class="btn btn-primary">Download</button>
												</form>
											</div>
									      </td>
									      <td><%= resultSet.getString(9) %></td>
									      <td>
											<div class="btn-container">
												<form action="download-file" method="get">
													<input type="hidden" name="accountNumber" value="<%= resultSet.getString(1) %>" />
													<input type="hidden" name="filename" value="PAN_Proof" />
													<button type="submit" class="btn btn-primary">Download</button>
												</form>
											</div>
									      </td>
									      <td><%= resultSet.getString(11) %></td>
									      <td><%= resultSet.getLong(12) %></td>
									      <td><%= resultSet.getDouble(13) %></td>
									      <td>
											<div class="btn-container">
												<form action="change-status" method="post">
													<input type="hidden" name="accountNumber" value="<%= resultSet.getString(1) %>" />
													<input type="hidden" name="username" value="<%= resultSet.getString(2) %>" />
													<input type="hidden" name="amount" value="<%= resultSet.getDouble(13) %>" />
													<input type="hidden" name="newStatus" value="Approved" />
													<button type="submit" class="btn btn-success">Approve</button>
												</form>
											</div>
									      </td>
									      <td>
											<div class="btn-container">
												<form action="change-status" method="post">
													<input type="hidden" name="accountNumber" value="<%= resultSet.getString(1) %>" />
													<input type="hidden" name="username" value="<%= resultSet.getString(2) %>" />
													<input type="hidden" name="amount" value="<%= resultSet.getDouble(13) %>" />
													<input type="hidden" name="newStatus" value="Rejected" />
													<button type="submit" class="btn btn-danger">Reject</button>
												</form>
											</div>
									      </td>
									    </tr>
								<%
									} while (resultSet.next());
								%>
								</tbody>
							</table>
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
							
							status = (String) request.getSession().getAttribute("Approved");
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
							
							status = (String) request.getSession().getAttribute("Rejected");
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
					</div>
				</div>
			</div>
		<%
			}
		%>
		
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
