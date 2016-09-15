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
<meta name="author" content="Roope Heinonen, Mika Ropponen" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>Työkykypassi&nbsp;&bull;&nbsp;Jäsenhallinta</title>

<!-- CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/index.css" />" />
</head>

<body>
<!-- FORM[0]: SELECT GROUP -->
<form id="getGroupStudents" action="getGroupStudents" method="post" accept-charset="UTF-8">
<input type="hidden" id="groupID" name="groupID" value="" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<!-- FORM[1]: SELECT STUDENT -->
<form id="selectStudent" action="selectStudent" method="post" accept-charset="UTF-8">
<input type="hidden" id="studentID" name="studentID" value="" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<nav class="navbar navbar-default">
  	<div class="container-fluid">
    	<div class="navbar-header">
      		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
       		<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
      		</button>
      		<a class="navbar-brand" href="#">Jäsenhallinta</a>
    	</div>
    	<div class="collapse navbar-collapse" id="myNavbar">
      		<ul class="nav navbar-nav">
        		<li><a href="index.jsp">Etusivu</a></li>
        		<li><a href="group.jsp">Ryhmät</a></li>
        		<li class="active"><a href="member.jsp">Jäsenet</a></li>
        		<li><a href="rating.jsp">Arviointi</a></li>
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

<div class="container-fluid">
  	<div class="page-header text-left">
    	<h2>Ryhmähallinta</h2>
  	</div>
</div>

<div class="container-fluid bg-3 text-center">
  	<div class="row">
    	<div class="col-sm-4 text-left">
    		<h3>Ryhmät</h3>
    		<c:if test="${not empty selectedGroup}">
    			<c:set var="selected_group" value="${selectedGroup}" />
    		</c:if>
    		<form action="#" method="POST">
      		<table class="table table-hover">
      			<c:forEach var="group" items="${groups}" varStatus="loop">  
      				<tr onclick="var f1=document.getElementById('getGroupStudents');f1.groupID.value='${group.groupID}';f1.submit();" class="${selectedGroup.groupID == group.groupID ? 'bold' : ''}"><td>${group.groupName}</td></tr>
      			</c:forEach>
      		</table>
      		</form>
    	</div>
    	<div class="col-sm-4 text-left">
      		<h3>Opiskelijat</h3>
      		<c:choose>
      			<c:when test="${not empty groupStudents}">
      				<table class="table table-hover">
      				<c:forEach var="student" items="${groupStudents}" varStatus="loop"> 
      					<tr onclick="var f2=document.getElementById('selectStudent');f2.studentID.value='${student.studentID}';f2.submit();" class="${selectedStudent.studentID == student.studentID ? 'bold' : ''}"><td><c:out value="${student.firstname}" />&nbsp;<c:out value="${student.lastname}" /></td></tr>     					
      				</c:forEach>
      				</table>
      			</c:when>
      			<c:otherwise>
      				<table class="table">
      					<tr><td>Valitse Ryhmä</td></tr>
      				</table>
      			</c:otherwise>
      		</c:choose>
    	</div>
    	<div class="col-sm-4 text-left">
      		<h3>Tiedot</h3>
      		<c:choose>
      			<c:when test="${selectedStudent.studentID > 0}">	
      				<table class="table">
      					<tr><th scope="row" class="text-right">Etunimi</th><td><c:out value="${selectedStudent.firstname}" /></td></tr>
      					<tr><th scope="row" class="text-right">Sukunimi</th><td><c:out value="${selectedStudent.lastname}" /></td></tr>
      					<tr><th scope="row" class="text-right">Sähköposti</th><td><c:out value="${selectedStudent.email}" /></td></tr>
      					<tr><th scope="row" class="text-right">Oppilaitos</th><td><c:out value="${selectedStudent.school}" /></td></tr>
      				</table>
      			</c:when>
      			<c:otherwise>
      				<table class="table">
      					<tr><td>Valitse opiskelija</td></tr>
      				</table>
      			</c:otherwise>
      		</c:choose>
    	</div>
  	</div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="<c:url value="/static/script/index.js" />"></script>

</body>
</html>