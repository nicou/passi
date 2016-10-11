<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
<title>Passi&nbsp;&nbsp;&bull;&nbsp;&nbsp;Expired</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- css -->
<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/main.css" />" />

<!-- bootstrap libraries -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container" id="expired-session-styles">
		<div class="row">
			<div class="col-sm-12">
				<h1>PASSI</h1>
				<h4>Istuntosi on vanhentunut</h4>
				<p><a href="/passi/login">Kirjaudu uudelleen</a></p>
			</div>
		</div>	
	</div>
</body>
</html>