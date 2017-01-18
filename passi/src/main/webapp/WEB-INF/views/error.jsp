<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
<meta name="author" content="Mika Ropponen, Roope Heinonen, Nico Hagelberg" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Työkykypassi&nbsp;&nbsp;&bull;&nbsp;&nbsp;Virhe</title>
<!-- CSS -->
<link rel="stylesheet" href="<c:url value="/static/style/bootstrap.min.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/main.css" />" />

<!-- Favicons -->
<link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/static/favicon/favicon-32x32.png" />">
<link rel="icon" type="image/png" sizes="96x96" href="<c:url value="/static/favicon/favicon-96x96.png" />">
<link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/static/favicon/favicon-16x16.png" />">

<!--[if lt IE 9]>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 text-center">
				<h1>PASSI</h1>
				<h2>Pyynnön käsittelyssä tapahtui virhe</h2>
				<p style="margin-top: 20px;">
				Jos ongelma toistuu, ota yhteyttä Mikko Nykäseen <small>(mikko.nykanen(at)ttl.fi)</small>
				</p>
				<button type="button" onclick="window.history.back();" class="btn btn-primary" style="margin-top: 10px;">Palaa takaisin</button>
			</div>
		</div>	
	</div>
	
	<!-- Script -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="<c:url value="/static/js/bootstrap.min.js" />"></script>
</body>
</html>