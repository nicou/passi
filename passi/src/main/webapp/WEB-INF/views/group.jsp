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

<title>Työkykypassi&nbsp;&bull;&nbsp;Ryhmähallinta</title>

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

<!-- Header embedded with currentPage parameter [/WEB-INF/views/pagename.jsp] -->
<jsp:include page="include/header.jsp">
	<jsp:param name="currentPage" value="${pageContext.request.servletPath}" />
</jsp:include>

<div class="container-fluid">
  	<div class="page-header text-left">
    	<h2 class="cursor-default">Ryhmät</h2>
  	</div>
</div>

<div class="container-fluid bg-3 text-center">
  	<div class="row">
    	<div class="col-sm-4 text-left">
    		
    		<!-- Navigation tabs -->
    		<ul class="nav nav-tabs">
    			<li class="active"><a data-toggle="tab" href="#" onclick="this.blur();">Luo uusi</a></li>
    			<li class=""><a data-toggle="tab" href="#" onclick="this.blur();">Muokkaa</a></li>
    			<li class=""><a data-toggle="tab" href="#" onclick="this.blur();">Poista</a></li>
    		</ul>
    		<br />
    		<c:url value="/createGroup" var="createGroup" />
    		<form:form role="form" class="form-horizontal" modelAttribute="newGroup" action="${createGroup}" method="post" accept-charset="UTF-8">
    			<form:hidden path="groupID" value="0" />
				<div class="form-group">
					<form:input placeholder="Kirjoita ryhmän tunnus" path="groupAbbr" cssClass="form-control" autocomplete="off" maxlength="50" />
				</div>
				<div class="form-group">
					<form:input placeholder="Kirjoita ryhmän nimi" path="groupName" cssClass="form-control" autocomplete="off" maxlength="100" />
				</div>
				<form:hidden path="groupLeadID" value="3" />
				<form:hidden path="groupLeadName" value="" />
				<div class="form-group">
					<button type="submit" class="btn btn-default form-control">LISÄÄ</button>
				</div>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    		</form:form>
    	</div>
    	<div class="col-sm-8 text-left">
      		<c:choose>
      			<c:when test="${not empty groups}">
      				<table class="table table-hover">
      					<thead>
      						<tr><th class="text-center">ID</th><th>Ryhmän nimi</th><th class="text-center">Vastuuhenkilö</th><th class="text-center">Jäseniä</th></tr>
      					</thead>
      					<tbody>
      						<c:forEach var="group" items="${groups}" varStatus="loop">
      						<tr><td class="text-center"><c:out value="${group.groupAbbr}" /></td><td class="text-nowrap"><c:out value="${group.groupName}" /></td><td class="text-center"><c:out value="${group.groupLeadName}" /></td><td class="text-center"><c:out value="${group.numGroupMembers}" /></td></tr>     					
      						</c:forEach>
      					</tbody>
      				</table>
      			</c:when>
      			<c:otherwise>
      				<table class="table">
      					<tr><td>Ei ryhmiä</td></tr>
      				</table>
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