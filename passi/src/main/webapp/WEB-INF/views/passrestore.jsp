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

<c:choose>
<c:when test="${not empty msg}">
	<p class="text-center" style="margin: 25px auto;">
		<c:out value="${msg }" />
		<c:if test="${not empty success && !success }">
			<br /><br />
			<a href="<c:url value="/passrestore" />">Palaa salasanan palautukseen</a>
		</c:if>
	</p>
</c:when>
<c:otherwise>
	<p class="text-center" style="margin: 25px auto;">Syötä alla olevaan kenttään sähköpostiosoitteesi jolla olet rekisteröitynyt työkykypassiin. Linkki salasanan vaihtoon lähetetään sinulle sähköpostitse.</p>
	<form class="form-horizontal" action="passrestore" method="POST">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<div class="input-group">
				<input type="email" name="email" class="form-control" maxlength="50" placeholder="Sähköpostiosoite" />
				<div class="input-group-btn">
					<button class="btn btn-primary" type="submit" name="action" value="sendlink">Lähetä</button>
				</div>
			</div>
	</form>
</c:otherwise>
</c:choose>

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