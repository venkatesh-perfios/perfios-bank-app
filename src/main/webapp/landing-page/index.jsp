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
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
			<a class="navbar-brand" href="index.jsp">Perfios Bank</a>
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

        <section class="bg-light">
            <div class="container">
                <div class="row">
                    <div class="col-lg-6 order-2 order-lg-1">
                        <h1>Welcome to Perfios Bank!</h1>
                        <p class="lead">Access our banking services at your fingertips</p>
                        <p class="lead">Anytime</p>
                        <p class="lead">Anywhere</p>  
						<% 
							if (!isLoggedIn) {
						%>
                        <p><a href="../signup-page/signup.jsp" class="btn btn-primary shadow mr-2">Signup</a>&nbsp&nbsp&nbsp<a href="../login-page/login.jsp" class="btn btn-primary shadow mr-2">Login</a></p>
                        <%
							}
                        %>
                    </div>
                	<div class="col-lg-6 order-1 order-lg-2"><img src="../images/logo_all.png" class="logo-all" alt="landing image" class="img-fluid"></div>
            	</div>
        	</div>
        </section>

		<div class="center">
			<div class="card-body">
				<div>
					<h2 class="card-header">Our Services</h2>
				</div>
			</div>
		</div>

		<div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel">
			<div class="carousel-indicators">
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" aria-label="Slide 2"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" aria-label="Slide 3"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="3" aria-label="Slide 4"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="4" aria-label="Slide 5"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="5" aria-label="Slide 6"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="6" aria-label="Slide 7"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="7" aria-label="Slide 8"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="8" aria-label="Slide 9"></button>
				<button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="9" aria-label="Slide 10"></button>
		  	</div>
		  	<div class="carousel-inner">
				<div class="carousel-item active">
					<img src="../images/signup.jpg">
					<div class="carousel-caption d-none d-md-block">
						<h5>Signup</h5>
			  		</div>
				</div>
				<div class="carousel-item">
					<img src="../images/login.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block">
						<h5>Login</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/open_account.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block dark-theme">
						<h5>Open Account</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/check_balance.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block">
						<h5>Check Balance</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/deposit.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block">
						<h5>Deposit</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/withdraw.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block">
						<h5>Withdraw</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/transfer.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block dark-theme">
						<h5>Transfer</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/past_transactions.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block dark-theme">
						<h5>View Past Transactions</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/change_password.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block">
						<h5>Change Password</h5>
					</div>
				</div>
				<div class="carousel-item">
					<img src="../images/close_account.jpg" class="d-block w-100">
					<div class="carousel-caption d-none d-md-block">
						<h5>Close Account</h5>
					</div>
				</div>
		  	</div>
			<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
				<span class="carousel-control-prev-icon" aria-hidden="true"></span>
				<span class="visually-hidden">Previous</span>
			</button>
			<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
				<span class="carousel-control-next-icon" aria-hidden="true"></span>
				<span class="visually-hidden">Next</span>
			</button>
		</div>

		<div class="bg-dark text-white">
			<div class="footer-container">
				<div class="row">
					<div class="col-md-7 text-md-left">
						<p class="mb-md-0">(C) 2022 Perfios Bank. All rights reserved. </p>
					</div>
					<div class="col-md-5 text-md-right">
                    	<p class="mb-0 credits">Made with <span style="color: #e25555;">❤</span> by Venkatesh</p>
					</div>
	            </div>
	        </div>
	    </div>

		<script type="text/javascript">
			function highlight(toHighlight) {
				deselect();
				var id = toHighlight.split(".")[0];
				if (id === "index") {
					return;
				}
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
