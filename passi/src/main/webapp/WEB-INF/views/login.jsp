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
<title>Työkykypassi&nbsp;&bull;&nbsp;Kirjaudu sisään</title>

<meta name="author" content="Roope Heinonen, Mika Ropponen" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- Custom styles for this template -->
<link href="<c:url value="/static/style/login.css" />" rel="stylesheet">

</head>

<body onload="document.login.username.focus();">

<div class="container">

<div class="login-panel">
			
<div style="display: block; text-align: center !important;">
<h2 class="form-signin-heading">Anna tunnuksesi</h2>
<c:if test="${not empty error}"><h4 style="color: red">${error}</h4></c:if>
<c:if test="${not empty message}"><h4 style="color: green">${message}</h4></c:if>
</div>

<form class="form-signin" action="<c:url value='/login' />" method="post">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
<table>		
<tr><td><input type="text" id="username" name="username" class="form-control" placeholder="Käyttäjätunnus" autocomplete="off" required autofocus /></td></tr>
<tr><td><input type="password" id="password" name="password" class="form-control" placeholder="Salasana" required /></td></tr>
<tr><td><button class="btn btn-lg btn-primary btn-block" type="submit" value="Kirjaudu">Kirjaudu</button></td></tr>
</table>
</form>

</div>

</div>

</body>
</html>