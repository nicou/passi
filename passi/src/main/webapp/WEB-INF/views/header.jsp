<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<nav class="navbar navbar-default">
  	<div class="container-fluid">
    	<div class="navbar-header">
      		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
       		<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
      		</button>
      		<a class="navbar-brand" href="#">Hallintasivu</a>
    	</div>
    	<div class="collapse navbar-collapse" id="myNavbar">
      		<ul class="nav navbar-nav">
        		<li class="active"><a href="index.jsp">Etusivu</a></li>
        		<li><a href="group.jsp">Ryhmät</a></li>
        		<li><a href="#">Jäsenet</a></li>
        		<li><a href="#">Ohjaus</a></li>
        		<li><a href="#">Tiedot</a></li>
     		</ul>
      		<ul class="nav navbar-nav navbar-right">
        		<li>
        		<c:url value="/logout" var="logoutUrl" />
        		<form id="logout" action="${logoutUrl}" method="post">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
				<c:if test="${pageContext.request.userPrincipal.name != null}">
        			<a href="javascript:document.getElementById('logout').submit()"><span class="glyphicon glyphicon-log-in"></span>&nbsp;&nbsp;Kirjaudu ulos</a>
				</c:if>
        		</li>
      		</ul>
    	</div>
  	</div>
</nav>