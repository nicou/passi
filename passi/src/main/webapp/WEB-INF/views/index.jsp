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
<meta name="author" content="Mika Ropponen, Roope Heinonen" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>Työkykypassi&nbsp;&bull;&nbsp;Tietohaku</title>

<!-- CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/main.css" />" />
</head>

<body>
<!-- FORM[0]: SELECT GROUP AND GET RELATED STUDENTS-->
<c:url value="/getGroupStudents" var="getGroupStudentsUrl" />
<form id="getGroupStudents" action="${getGroupStudentsUrl}" method="post" accept-charset="UTF-8">
<input type="hidden" id="groupID" name="groupID" value="" />
<input type="hidden" id="returnPage" name="returnPage" value="index" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<!-- FORM[1]: SELECT STUDENT -->
<form id="selectStudent" action="selectStudent" method="post" accept-charset="UTF-8">
<input type="hidden" id="username" name="username" value="" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<!-- Header embedded with currentPage parameter [/WEB-INF/views/pagename.jsp] -->
<jsp:include page="include/header.jsp">
	<jsp:param name="currentPage" value="${pageContext.request.servletPath}" />
</jsp:include>

<div class="container-fluid">
  	<div class="page-header text-left">
    	<h2 class="cursor-default">Tietohaku</h2>
  	</div>
</div>

<div class="container-fluid bg-3 text-center">
  	<div class="row">
    	<div class="col-sm-4 text-left">
    	
    		<div class="row">
    			<h3 class="cursor-default">Ryhmät</h3>
    			<c:choose>
    				<c:when test="${not empty groups}">
      					<table class="table table-hover">
      						<c:forEach var="group" items="${groups}" varStatus="loop">  
      							<tr onclick="var f1=document.getElementById('getGroupStudents');f1.groupID.value='${group.groupID}';f1.submit();" class="${selectedGroupID == group.groupID ? 'bold' : ''}"><td>${group.groupName}</td></tr>
      						</c:forEach>
      					</table>
      				</c:when>
      				<c:otherwise>
      					<table class="table">
      						<tr><td>Ei Ryhmiä</td></tr>
      					</table>
      				</c:otherwise>
      			</c:choose>
      		</div>

    		<div class="row">
      			<h3 class="cursor-default">Jäsenet</h3>
      			<c:choose>
      				<c:when test="${not empty groupStudents}">
      					<table class="table table-hover">
      					<c:forEach var="student" items="${groupStudents}" varStatus="loop"> 
      						<tr onclick="var f2=document.getElementById('selectStudent');f2.username.value='${student.username}';f2.submit();" class="${selectedStudentObject.username == student.username ? 'bold' : ''}"><td><c:out value="${student.firstname}" />&nbsp;<c:out value="${student.lastname}" /></td></tr>     					
      					</c:forEach>
      					</table>
      				</c:when>
      				<c:otherwise>
      					<table class="table">
      						<tr><td>Jäsenlista on tyhjä</td></tr>
      					</table>
      				</c:otherwise>
      			</c:choose>
    		</div>
    		
    		<div class="row">
      			<h3 class="cursor-default">Tiedot</h3>
      			<c:choose>
      				<c:when test="${selectedStudentObject.username != ''}">	
      					<table class="table cursor-default">
      						<tr><th scope="row" class="text-right">Etunimi</th><td class="wide"><c:out value="${selectedStudentObject.firstname}" /></td></tr>
      						<tr><th scope="row" class="text-right">Sukunimi</th><td class="wide"><c:out value="${selectedStudentObject.lastname}" /></td></tr>
      						<tr><th scope="row" class="text-right">Sähköposti</th><td class="wide"><c:out value="${selectedStudentObject.email}" /></td></tr>
      						<tr><th scope="row" class="text-right">Oppilaitos</th><td class="wide"><c:out value="${selectedStudentObject.school}" /></td></tr>
      					</table>
      				</c:when>
      				<c:otherwise>
      					<table class="table">
      						<tr><td>Ei tietoja</td></tr>
      					</table>
      				</c:otherwise>
      			</c:choose>
    		</div>
  		</div>
  		
  		<div class="col-sm-8 text-left">
  			<div class="row">
  				<h3 class="cursor-default">Tehtäväkortti</h3>
  				<div style="border: 1px solid #DCDCDC; height: 500px;">
  				</div>
  			</div>
  		</div>
  		
  	</div>
</div>

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="<c:url value="/static/script/index.js" />"></script>

</body>
</html>