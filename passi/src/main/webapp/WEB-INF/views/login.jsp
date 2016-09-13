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

<!-- Bootstrap libraries -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="<c:url value="/static/css/ie10-viewport-bug-workaround.css" />" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="<c:url value="/static/css/signin.css" />" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
</head>

<body onload="document.login.username.focus();">
	<div class="container">
		<form class="form-signin" action="<c:url value='/login' />" method="post">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<h2 class="form-signin-heading">Syötä opettajatunnuksesi palveluun</h2>
			<c:if test="${not empty error}"><h4 style="color: red">${error}</h4></c:if>
			<c:if test="${not empty message}"><h4 style="color: green">${message}</h4></c:if>
			<label for="username" class="sr-only">Käyttäjätunnus</label>&nbsp;
			<input type="text" id="username" name="username" class="form-control" placeholder="Käyttäjätunnus" autocomplete="off" required autofocus>&nbsp;
			<label for="password" class="sr-only">Salasana</label>&nbsp;
			<input type="password" id="password" name="password" class="form-control" placeholder="Salasana" required>
			<div class="checkbox">
			<label>&nbsp;<input type="checkbox" value="remember-me">Muista minut<br></label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit" value="Kirjaudu">Kirjaudu</button>
		</form>
	</div>

	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="<c:url value='/static/css/ie10-viewport-bug-workaround.js' />" /></script>

</body>
</html>