<%@page import="org.springframework.security.core.authority.SimpleGrantedAuthority"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
if (auth.isAuthenticated() && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
	pageContext.forward("/init");
}
%>

<%
int timeout = session.getMaxInactiveInterval();
String contextPath = request.getContextPath();
response.setHeader("Refresh", timeout + "; URL = " + contextPath + "/expired");
%>

<!DOCTYPE html>
<html lang="fi">
<head>
<title>Työkykypassi&nbsp;&bull;&nbsp;Kirjaudu sisään</title>

<meta name="author" content="Mika Ropponen" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">

<!-- Custom styles for this template -->
<link href="<c:url value="/static/style/login.css" />" rel="stylesheet">

</head>

<body onload="document.getElementById('username').focus();">

<div class="container">

<div class="login-panel">
			
<div id="login-panel-style">
<h2 class="form-signin-heading">Anna tunnuksesi</h2>
<c:if test="${not empty error}"><h4 class="color-red">${error}</h4></c:if>
<c:if test="${not empty message}"><h4 class="color-green">${message}</h4></c:if>
</div>

<form class="form-signin" action="<c:url value='/login' />" method="post">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
<table>		
<tr><td><input type="text" id="username" name="username" class="form-control" placeholder="Käyttäjätunnus" autocomplete="off" required autofocus /></td></tr>
<tr><td><input type="password" id="password" name="password" class="form-control" placeholder="Salasana" required /></td></tr>
<tr><td><button class="btn btn-lg btn-primary btn-block" type="submit" value="Kirjaudu">Kirjaudu</button></td></tr>
</table>
</form>

<!-- Registration link -->
<c:url var="registrationURL" value="/registration" />
<p>
	<a href="${registrationURL}">Rekisteröidy</a>
	<br/>
	<small>
	<a href="passrestore">Salasanan palautus</a>
	</small>
	</p>

</div>

</div>

<pre style="display: inline-block; margin-top: 20px; padding: 10px 15px 10px 15px;">username = admin<br />password = passw</pre>

<div class="row">
<div class="col-xs-12 col-md-8 col-md-push-2 front-info">
<p>
Työkykypassi – mobiilisovellus on kehitetty Työterveyslaitoksen koordinoimassa Combo -hankkeessa. Sovelluksen tarkoituksena on vahvistaa opiskelijoiden työkykyosaamista ammatillisissa oppilaitoksissa. Hanketta on rahoittanut Euroopan sosiaalirahasto (ESR) / Pohjois-Pohjanmaan elinkeino-, liikenne- ja ympäristökeskus. Sovellukseen kehittämiseen ovat osallistuneet Haaga-Helia Ammattikorkeakoulu, Työterveyslaitos, Saku Ry ja Stadin ammattiopisto.
</p>

<img class="front-logo" src="static/img/kehittaja_haaga-helia.jpg" alt="Haaga-Helia Logo">
<img class="front-logo" src="static/img/kehittaja_ttl.jpg" alt="TTL Logo">
<img class="front-logo" src="static/img/kehittaja_saku.png" alt="Saku Logo">
<img class="front-logo" src="static/img/kehittaja_vipuvoimaa.png" alt="Vipuvoimaa logo">
<img class="front-logo" src="static/img/kehittaja_eu_esr.png" alt="EU ESR Logo">
<img class="front-logo" src="static/img/kehittaja_ely.jpg" alt="ELY Logo">
<img class="front-logo" src="static/img/kehittaja_stadin_ammattiopisto.jpg" alt="Stadin ammattiopisto Logo">

<p>
Sovelluksen sisällössä on hyödynnetty Alpo.fi-sivujen tehtäviä, jotka on aiemmin laadittu yhteistyössä seuraavien tahojen kanssa:
</p>

<img class="front-logo" src="static/img/yhteistyo_smartmoves.png" alt="Smartmoves logo">
<img class="front-logo" src="static/img/yhteistyo_ehyt.jpg" alt="Ehyt logo">
<img class="front-logo" src="static/img/yhteistyo_ye.jpg" alt="YE Logo">
<img class="front-logo" src="static/img/yhteistyo_sydanliitto.jpg" alt="Sydänliitto logo">
<img class="front-logo" src="static/img/yhteistyo_nuortenakatemia.jpg" alt="Nuorten akatemia logo">

<p>
Rekisteriseloste on saatavilla <a href="/passi/static/combo_rekisteriseloste.pdf" target="_blank" rel="noopener">täältä</a>
</p>
<br />

</div>
</div>

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<c:set var="message" scope="session" value="" />

</body>
</html>