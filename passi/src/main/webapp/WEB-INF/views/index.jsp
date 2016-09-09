<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PASSI</title>
</head>
<body>
<h3>PASSI</h3>
<p>Tervetuloa <c:out value="${user}" />... käyttöoikeutesi on <c:out value="${role == 'ROLE_ADMIN' ? 'ADMIN' : 'USER'}" /></p>
<c:url value="/logout" var="logoutUrl" />
<form id="logout" action="${logoutUrl}" method="post" >
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<c:if test="${pageContext.request.userPrincipal.name != null}">
<a href="javascript:document.getElementById('logout').submit()">Kirjaudu ulos</a>
</c:if>
</body>
</html>