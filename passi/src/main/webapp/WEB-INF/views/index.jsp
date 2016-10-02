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
<c:url var="getGroupDataUrl" value="/getGroupData" />
<form id="getGroupData" action="${getGroupDataUrl}" method="post" accept-charset="UTF-8">
<input type="hidden" id="groupID" name="groupID" value="" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<!-- FORM[1]: SELECT STUDENT -->
<c:url var="getMemberAnswersUrl" value="/getMemberAnswers" />
<form id="getMemberAnswers" action="${getMemberAnswersUrl}" method="post" accept-charset="UTF-8">
<input type="hidden" id="userID" name="userID" value="" />
<input type="hidden" id="groupID" name="groupID" value="" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<!-- Header embedded with currentPage parameter [/WEB-INF/views/pagename.jsp] -->
<jsp:include page="include/header.jsp">
	<jsp:param name="currentPage" value="${pageContext.request.servletPath}" />
</jsp:include>

<div class="container-fluid bg-3 text-center">
  	<div class="row">
    	<div class="col-sm-4 text-left">
    	
    		<div class="row">
    			<h3 class="cursor-default">Ryhmät</h3>
    			<c:choose>
    				<c:when test="${not empty groups}">
      					<table class="table table-hover">
      						<c:forEach var="group" items="${groups}">  
      							<tr onclick="var f1=document.getElementById('getGroupData');f1.groupID.value='${group.groupID}';f1.submit();" class="${selectedGroupObject.groupID == group.groupID ? 'bold' : ''}"><td>${group.groupName}</td></tr>
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
      				<c:when test="${not empty groupMembers}">
      					<table class="table table-hover">
      					<c:forEach var="member" items="${groupMembers}"> 
      						<tr onclick="var f2=document.getElementById('getMemberAnswers');f2.userID.value='${member.userID}';f2.groupID.value='${selectedGroupObject.groupID}';f2.submit();" class="${selectedMemberObject.userID == member.userID ? 'bold' : ''}"><td><c:out value="${member.firstname}" />&nbsp;<c:out value="${member.lastname}" /></td></tr>     					
      					</c:forEach>
      					</table>
      				</c:when>
      				<c:otherwise>
      					<table class="table">
      						<tr><td>Ryhmää ei valittu</td></tr>
      					</table>
      				</c:otherwise>
      			</c:choose>
    		</div>
    		
    		<div class="row">
      			<h3 class="cursor-default">Tiedot</h3>
      			<c:choose>
      				<c:when test="${selectedMemberObject.userID != 0}">	
      					<table class="table cursor-default">
      						<tr><th scope="row" class="text-right">Etunimi</th><td class="wide"><c:out value="${selectedMemberObject.firstname}" /></td></tr>
      						<tr><th scope="row" class="text-right">Sukunimi</th><td class="wide"><c:out value="${selectedMemberObject.lastname}" /></td></tr>
      						<tr><th scope="row" class="text-right">Sähköposti</th><td class="wide"><c:out value="${selectedMemberObject.email}" /></td></tr>
      						<tr><th scope="row" class="text-right">Puhelin</th><td class="wide"><c:out value="${selectedMemberObject.phone}" /></td></tr>
      					</table>
      				</c:when>
      				<c:otherwise>
      					<table class="table">
      						<tr><td>Opiskelijaa ei valittu</td></tr>
      					</table>
      				</c:otherwise>
      			</c:choose>
    		</div>
    				
			<div class="row">
      			<h3 class="cursor-default">Ohjaaja</h3>
      			<c:choose>
      				<c:when test="${selectedGroupObject.groupID != 0}">	
      					<table class="table cursor-default">
      						<tr><th scope="row" class="text-right">Etunimi</th><td class="wide"><c:out value="${selectedGroupObject.instructors[0].firstname}" /></td></tr>
      						<tr><th scope="row" class="text-right">Sukunimi</th><td class="wide"><c:out value="${selectedGroupObject.instructors[0].lastname}" /></td></tr>
      						<tr><th scope="row" class="text-right">Sähköposti</th><td class="wide"><c:out value="${selectedGroupObject.instructors[0].email}" /></td></tr>
      						<tr><th scope="row" class="text-right">Puhelin</th><td class="wide"><c:out value="${selectedGroupObject.instructors[0].phone}" /></td></tr>
      					</table>
      				</c:when>
      				<c:otherwise>
      					<table class="table">
      						<tr><td>Ryhmää ei valittu</td></tr>
      					</table>
      				</c:otherwise>
      			</c:choose>
    		</div>
    		
  		</div>
  		
  		<div class="col-sm-8 text-left">
  			<c:choose>
  				<c:when test = "${not empty worksheets || not empty answers}">
  					<c:forEach var="worksheet" items="${worksheets}" varStatus="loop">
  						<h3><c:out value="${worksheet.worksheetHeader}" /></h3>
  						<p><c:out value="${worksheet.worksheetPreface}" /></p>
  						<p><strong><c:out value="${worksheet.worksheetPlanning}" /></strong></p>
  						<pre><c:out value="${answers[loop.index].answerPlanning}" /></pre><br />
  						<c:forEach var="waypoint" items="${worksheet.waypoints}" varStatus="loopInner">
  							<p><strong><c:out value="${loopInner.count}" />.&nbsp;<c:out value="${waypoint.waypointTask}" /></strong></p>
  							<p>Monivalinnan vastaus: <code><c:out value="${answers[loop.index].waypoints[loopInner.index].optionID}" /></code></p>
  							<pre><c:out value="${answers[loop.index].waypoints[loopInner.index].answerWaypointText}" /></pre><br />
  						</c:forEach>
  					</c:forEach>
  				</c:when>
  				<c:otherwise>
  					<h3>Tehtäväkortit</h3>
  					<div class="alert alert-info">
    				<strong>Info!</strong> Valitse ensin ryhmä ja sitten ryhmän jäsen.
  					</div>
  				</c:otherwise>
  			</c:choose>
  		</div>
  	</div>
</div>

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="<c:url value="/static/script/index.js" />"></script>

</body>
</html>