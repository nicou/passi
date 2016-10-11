<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
<form id="getGroupData">
<input type="hidden" id="groupID" name="groupID" value="" />

</form>
<!-- FORM[1]: SELECT STUDENT -->
<c:url var="getAnswersUrl" value="/getAnswers" />
<form id="getAnswers" action="${getAnswersUrl}" method="post" accept-charset="UTF-8">
<input type="hidden" id="userID" name="userID" value="" />
<input type="hidden" id="groupID" name="groupID" value="" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<!-- Header embedded with currentPage parameter [/WEB-INF/views/pagename.jsp] -->
<jsp:include page="include/header.jsp">
	<jsp:param name="currentPage" value="${pageContext.request.servletPath}" />
</jsp:include>

<div class="container-fluid bg-3 text-center">

<div class="col-sm-4 text-left" id="leftnav-padding">
    	
<div class="row">
	<h3 class="cursor-default">Ryhmät</h3>
	
	<c:url var="getGroupDataUrl" value="/getGroupData" />
	<form action="${getGroupDataUrl}" method="post" accept-charset="UTF-8">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div class="form-group" id="no-padding">
      	<select class="form-control" id="groupID" name="groupID" onchange="this.form.submit();">
      		<option disabled selected> -- Valitse Ryhmä -- </option>
      		<c:forEach var="group" items="${groups}">
        		<option value="${group.groupID}" ${selectedGroupObject.groupID == group.groupID ? 'selected' : ''}><c:out value="${group.groupName}" /></option>
        	</c:forEach>
      	</select>
    </div>
    </form>
	
</div>
			
<div class="row">
	<h3 class="cursor-default">Jäsenet</h3>
    <c:choose>
    <c:when test="${not empty groupMembers}">
    <table class="table table-hover">
    <c:forEach var="member" items="${groupMembers}"> 
    <tr onclick="var f2=document.getElementById('getAnswers');f2.userID.value='${member.userID}';f2.groupID.value='${selectedGroupObject.groupID}';f2.submit();" class="${selectedMemberObject.userID == member.userID ? 'bold' : ''}"><td><c:out value="${member.firstname}" />&nbsp;<c:out value="${member.lastname}" /></td></tr>     					
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
    <h3 class="cursor-default"><c:out value="${fn:length(selectedGroupObject.instructors) > 1 ? 'Ohjaajat' : 'Ohjaaja'}" /></h3>
    <c:choose>
    <c:when test="${selectedGroupObject.groupID != 0}">	
    <table class="table cursor-default" class="hide-in-mobile">
    <c:forEach var="instructor" items="${selectedGroupObject.instructors}">
    <tr><th scope="row" class="text-right">Etunimi</th><td class="wide"><c:out value="${instructor.firstname}" /></td></tr>
    <tr><th scope="row" class="text-right">Sukunimi</th><td class="wide"><c:out value="${instructor.lastname}" /></td></tr>
    <tr><th scope="row" class="text-right">Sähköposti</th><td class="wide"><c:out value="${instructor.email}" /></td></tr>
    <tr><th scope="row" class="text-right">Puhelin</th><td class="wide"><c:out value="${instructor.phone}" /></td></tr>
    <tr><th scope="row" class="text-right">&nbsp;</th><td class="wide">&nbsp;</td></tr>
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
    		
</div>
  		
<div class="col-sm-8 text-left" id="worksheet-padding">

<c:choose>
<c:when test = "${not empty worksheets || not empty answers}">
<c:forEach var="worksheet" items="${worksheets}" varStatus="loop">
  					
	<div>
  		<h3><c:out value="${worksheet.worksheetHeader}" />&nbsp;&nbsp;&bull;&nbsp;&nbsp;<c:out value="${selectedMemberObject.firstname} ${selectedMemberObject.lastname}" /></h3>		
  		<p><c:out value="${worksheet.worksheetPreface}" /></p>
  	</div>
  						
  	<div class="row">
  		<h4><c:out value="${worksheet.worksheetPlanning}" /></h4>
  		<div class="well">
    	<c:choose>
  		<c:when test="${not empty answers[loop.index].answerPlanning}">
  		<c:out value="${answers[loop.index].answerPlanning}" />
  		</c:when>
  		<c:otherwise>
  		Ei suunnitelmaa
  		</c:otherwise>
  		</c:choose>
    	</div>
 	</div>
 						
  	<c:forEach var="waypoint" items="${worksheet.waypoints}" varStatus="loopInner">
  						
  		<div class="row">			
  			<div class="panel panel-default">
  			<div class="panel-heading"><strong><c:out value="${loopInner.count}" />.&nbsp;<c:out value="${waypoint.waypointTask}" /></strong></div>
  			<div class="panel-body">
  			<c:choose>
  			<c:when test="${not empty answers[loop.index].waypoints[loopInner.index].answerWaypointText}">
  										
  			<!-- Image -->
  			<c:set var="imageName" value="${waypoint.waypointID}-${answers[loop.index].userID}" />
  			<c:url var="imageLink" value="/download/${imageName}/jpg" />
  			<img src="${imageLink}" onerror="this.style.display = 'none'" class="well-image" align="left" draggable="false" />
  											
  			<p>
  			Monivalinnan vastaus:&nbsp;
  			<c:choose>
  			<c:when test="${answers[loop.index].waypoints[loopInner.index].optionID > 0}">
  			<code><c:out value="${answers[loop.index].waypoints[loopInner.index].optionText}" /></code>
  			</c:when>
  			<c:otherwise>
  			<code>Ei valintaa</code>
  			</c:otherwise>
  			</c:choose>
  			</p>							
  			<span class="consol"><c:out value="${answers[loop.index].waypoints[loopInner.index].answerWaypointText}" /></span>
  			</c:when>
  			<c:otherwise>
  			<span class="consol">Ei vastausta</span>
  			</c:otherwise>
  			</c:choose>
  			</div>
  			</div>
		</div>	
  	
  	</c:forEach>
  	
</c:forEach>
</c:when>
<c:otherwise>
  	<h3>Tehtäväkortit</h3>
  	<div class="alert alert-info">
    <strong>Info!</strong> Valitse ryhmä ja sitten ryhmän jäsen.
  	</div>
</c:otherwise>
</c:choose>
</div>
 
</div>

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="<c:url value="/static/script/index.js" />"></script>

</body>
</html>