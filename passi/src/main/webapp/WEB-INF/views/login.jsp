<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
int timeout = session.getMaxInactiveInterval();
String contextPath = request.getContextPath();
response.setHeader("Refresh", timeout + "; URL = " + contextPath + "/expired");
%>

<!DOCTYPE html>
<html>
<head>
<title>Passi&nbsp;&nbsp;&bull;&nbsp;&nbsp;Login</title>

<meta name="author" content="Mika Ropponen" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- bootstrap libraries -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>

<body onload="document.login.username.focus();">
	<div class="container" style="text-align: center; font: normal 12px Consolas;">
		<div class="row">
			<div class="col-sm-offset-5 col-sm-2">
				<h1>PASSI</h1>
				<h4>Kirjaudu sisään</h4>
				<c:if test="${not empty error}"><p style="color: red">${error}</p></c:if>
				<c:if test="${not empty message}"><p style="color: green">${message}</p></c:if>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-offset-5 col-sm-2">
				<form role="form" class="form-horizontal" name="login" action="<c:url value='/login' />" method="post">
				<input class="form-control" type="text" name="username" value="" placeholder="Käyttäjänimi" autocomplete="off" style="text-align: center !important;" />
				<br />
				<input class="form-control" type="password" name="password" placeholder="Salasana" autocomplete="off" style="text-align: center !important;" />
				<br />
				<input class="btn btn-default form-control login-submit" name="submit" type="submit" value="KIRJAUDU" />
				<br />
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
				<div style="color: #696969; display: inline-block; padding-top: 15px; text-align: left;">
					Username: Donald<br />Password: Trump
				</div>
			</div>			
		</div>		
	</div>
</body>
</html>