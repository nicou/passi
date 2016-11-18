<%@ page session="false" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="fi">
<head>
<meta name="author" content="Mika Ropponen" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>Työkykypassi&nbsp;&bull;&nbsp;Rekisteröinti</title>

<!-- Styles -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />

<style>
html {
background-color: #baccde;
height: 100%;
}
body {
background-color: transparent;
padding-top: 50px;
text-align: center;
}
h3 {
	padding-left: 15px;
	padding-top: 15px;
}
label {
	text-align: right !important;
	white-space: no-wrap !important;
}
.form-group {
	margin: 0;
	padding-left: 30px;
	padding-right: 30px;
}
.regform {
	background-color: #FFFFFF;
	border-radius: 20px;
	box-shadow: 0 0 10px #696969;
	margin-bottom: 50px;
	text-align: left;
	padding-bottom: 30px;
}
</style>

</head>

<body>

<div class="container">
<div class="row">
<div class="col-sm-offset-3 col-sm-6 regform">
<h3>Ryhmänohjaajan rekisteröinti</h3>

<spring:url var="registrationURL" value="/registration" />

<form:form class="form-horizontal" modelAttribute="userForm" action="${registrationUrl}" method="POST">
	
	<form:hidden path="userID" />
		
	<spring:bind path="username">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label">Käyttäjätunnus</label>
			<form:input path="username" class="form-control" maxlength="20" />
			<form:errors path="username" class="control-label" />
		</div>
	</spring:bind>
	
	<spring:bind path="firstname">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label">Etunimi</label>
			<form:input path="firstname" class="form-control" maxlength="40" />
			<form:errors path="firstname" class="control-label" />
		</div>
	</spring:bind>
	
	<spring:bind path="lastname">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label">Sukunimi</label>
			<form:input path="lastname" class="form-control" maxlength="60" />
			<form:errors path="lastname" class="control-label" />
		</div>
	</spring:bind>

	<spring:bind path="email">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label">Sähköposti</label>
			<form:input path="email" class="form-control" maxlength="80" />
			<form:errors path="email" class="control-label" />
		</div>
	</spring:bind>
	
	<spring:bind path="phone">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label">Puhelin</label>
			<form:input path="phone" class="form-control" maxlength="40" />
			<form:errors path="phone" class="control-label" />
		</div>
	</spring:bind>

	<spring:bind path="password">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label">Salasana</label>
			<form:password path="password" class="form-control" maxlength="80" />
			<form:errors path="password" class="control-label" />
		</div>
	</spring:bind>

	<spring:bind path="confirmPassword">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label">Vahvista salasana</label>
			<form:password path="confirmPassword" class="form-control" maxlength="80" />
			<form:errors path="confirmPassword" class="control-label" />
		</div>
	</spring:bind>
	
	<div class="form-group">
		<br />
		<button class="btn btn-md btn-primary btn-block" type="submit">Lähetä</button>
	</div>
	
</form:form>

<c:url var="returnURL" value="/login" />
<div style="text-align: center;">
<br />
<a href="${returnURL}">Palaa kirjautumiseen</a>
<br />
</div>

</div>
</div>
</div>

<!-- Script libraries -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>
</html>