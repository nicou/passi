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
<meta name="author" content="Mika Ropponen, Roope Heinonen, Nico Hagelberg" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Työkykypassi&nbsp;&bull;&nbsp;Salasanan palautus</title>

<!-- CSS -->
<link rel="stylesheet" href="<c:url value="/static/style/bootstrap.min.css" />" />

<!-- Favicons -->
<link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/static/favicon/favicon-32x32.png" />">
<link rel="icon" type="image/png" sizes="96x96" href="<c:url value="/static/favicon/favicon-96x96.png" />">
<link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/static/favicon/favicon-16x16.png" />">

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
<!--[if lt IE 9]>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>

<body>

<div class="container">
<div class="row">
<div class="col-sm-offset-3 col-sm-6 regform">
<h3 class="text-center">Salasanan palautus</h3>

<p class="text-center" style="margin: 25px auto;">Kirjoita uusi salasanasi.</p>

<form class="form-horizontal" action="passreset" method="POST">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<input type="hidden" name="token" value="${token }" />
		<div class="form-group">
			<label class="control-label">Salasana</label>
			<input type="password" name="password" class="form-control" placeholder="Salasana" />
		</div>
		<div class="form-group">
			<label class="control-label">Salasana uudelleen</label>
			<input type="password" name="passwordagain" class="form-control" placeholder="Salasana uudelleen" />
		</div>
		<div class="form-group">
			<button class="btn btn-primary btn-block" type="submit" name="action" name="resetpass">Lähetä</button>
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

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="<c:url value="/static/script/bootstrap.min.js" />"></script>

</body>
</html>