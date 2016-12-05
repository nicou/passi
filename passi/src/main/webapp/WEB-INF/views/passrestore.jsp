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

<title>Työkykypassi&nbsp;&bull;&nbsp;Salasanan palautus</title>

<!-- Styles -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />

<style>
body {
background: #fff;
background: linear-gradient(#e6e6e6, #fff, #fff);
background-repeat: no-repeat;
padding-top: 30px;
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
<h3 class="text-center">Salasanan palautus</h3>

<p class="text-center" style="margin: 25px auto;">Syötä alla olevaan kenttään joko käyttäjätunnuksesi tai sähköpostiosoitteesi. Linkki salasanan vaihtoon lähetetään sähköpostiisi.</p>

<spring:url var="registrationURL" value="/registration" />

<form class="form-horizontal" action="passrestore" method="POST">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<div class="input-group">
			<input name="identifier" class="form-control" maxlength="50" placeholder="Käyttäjätunnus tai sähköpostiosoite" />
			<div class="input-group-btn">
				<button class="btn btn-primary" type="submit">Lähetä</button>
			</div>
		</div>
</form>

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