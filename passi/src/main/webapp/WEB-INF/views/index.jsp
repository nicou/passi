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
<title>Työkykypassi&nbsp;&bull;&nbsp;Etusivu</title>

<meta name="author" content="Mika Ropponen" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- bootstrap libraries -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/index.css" />">
</head>
<body>
	<nav class="navbar navbar-static-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
			</div>
			<!-- close navbar-header -->
			<div id="navbar" class="navbar-collapse collapse text-center">
				<ul class="nav navbar-nav">
					<li><a href="" class="">Ryhmät</a></li>
					<li><a href="" class="">Opiskelijat</a></li>
					<li><a href="" class="">Lisätiedot</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
		<!-- close container -->
	</nav>
	<!-- close navigation -->

	<!--<div class="col-xs-12">
                <div class="row">
                  <div class="container-fluid col-xs-12">
                    <div id="lisaaryhmabutton">Lisää ryhmä</div>
                  </div>
                  <div class="container col-xs-12 grouplistcontainer">
                    <div class="col-xs-4 col-sm-5 col-md-6">
                      <ul id="grouplist">
                        <li>Ryhmä 1</li>
                        <li>Ryhmä 2</li>
                        <li>Ryhmä japesoft</li>
                        <li>Ryhmä nyberg</li>
                        <li>Ryhmä Kuumostus</li>
                        <li>Ryhmä Nollaus</li>
                        <li>Ryhmä No hmm tuota</li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>-->

	<!-- Huom! Tarvitaan määrittelyt -->
	<div id="services" class="container-fluid text-center">
		<h2></h2>
		<br>
		<div class="row slideanim">
			<div class="panelcontainer col-sm-4">
				<span class="glyphicon glyphicon-user logo-large"></span>
				<h2>Ryhmät</h2>
				<p>Selaa omia ryhmiäsi</p>
			</div>
			<div class="panelcontainer col-sm-4">
				<span class="glyphicon glyphicon-education logo-medium"></span>
				<h2>Opiskelijat</h2>
				<p>Selaa opiskelijoitasi</p>
			</div>
			<div class="panelcontainer col-sm-4">
				<span class="glyphicon glyphicon-plus logo-small"></span>
				<h2>Lisää Ryhmä</h2>
				<p>Lisää uusi ryhmä</p>
			</div>
		</div>
	</div>
	<div style="margin: 100px;">
		<c:url value="/logout" var="logoutUrl" />
		<form id="logout" action="${logoutUrl}" method="post" >
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
		<c:if test="${pageContext.request.userPrincipal.name != null}">
		<a href="javascript:document.getElementById('logout').submit()">Kirjaudu ulos</a>
		</c:if>
	</div>
</body>
</html>