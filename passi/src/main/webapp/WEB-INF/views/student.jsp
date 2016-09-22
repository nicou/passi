<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

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
<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/main.css" />" />
</head>

<body>
<!-- FORM[0]: SELECT GROUP -->
<c:url value="/student" var="studentUrl" />
<form id="getGroupStudents" action="getGroupStudents" method="post" accept-charset="UTF-8">
<input type="hidden" id="groupID" name="groupID" value="" />
<input type="hidden" id="returnPage" name="returnPage" value="student" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>


<!-- Header embedded with currentPage parameter [/WEB-INF/views/pagename.jsp] -->
<jsp:include page="include/header.jsp">
	<jsp:param name="currentPage" value="${pageContext.request.servletPath}" />
</jsp:include>

<div class="container-fluid">
  	<div class="page-header text-left">
    	<h2 class="cursor-default">Jäsenet</h2>
  	</div>
</div>

<div class="container-fluid bg-3 text-center">
  	<div class="row">
    	<div class="col-sm-4 text-left">
    		
    		<!-- Navigation tabs -->
    		<ul class="nav nav-tabs">
    			<li class="active"><a data-toggle="tab" href="#add" onclick="this.blur();">Lisää</a></li>
    			<li class=""><a data-toggle="tab" href="#edit" onclick="this.blur();">Muokkaa</a></li>
    			<li class=""><a data-toggle="tab" href="#del" onclick="this.blur();">Poista</a></li>
    		</ul>
    		
    		<div class="tab-content">
    		
    			<!-- tab: add group -->
  				<div id="add" class="tab-pane fade in active">
    				<h4>Lisää opiskelija ryhmään</h4>
    				
    				<c:if test="${not empty message}">
    					<div class="alert alert-info">
   							<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    						<strong>Info!</strong>&nbsp;&nbsp;<c:out value="${message}" />
  						</div>
    				</c:if>
    				
    				<c:url value="/addStudent" var="addStudent" />
    				<form:form role="form" class="form-horizontal" modelAttribute="newStudent" action="${addStudent}" method="post" accept-charset="UTF-8">
  						<div class="form-group">
  							<input required="required" placeholder="Ryhmätunnus" type="text" id="groupID" name="groupID" class="form-control" autocomplete="off" maxlength="20" />
  						</div>
  						<div class="form-group">
							<form:input required="required" placeholder="Käyttäjätunnus" path="username" cssClass="form-control" autocomplete="off" maxlength="20" />
						</div>
						<div class="form-group">
							<form:input required="required" placeholder="Salasana" path="password" cssClass="form-control" autocomplete="off" maxlength="80" />
						</div>
						<div class="form-group">
							<form:input required="required" placeholder="Etunimi" path="firstname" cssClass="form-control" autocomplete="off" maxlength="50" />
						</div>
						<div class="form-group">
							<form:input required="required" placeholder="Sukunimi" path="lastname" cssClass="form-control" autocomplete="off" maxlength="50" />
						</div>
						<div class="form-group">
							<form:input required="required" placeholder="Oppilaitos" path="school" cssClass="form-control" autocomplete="off" maxlength="50" />
						</div>
						<div class="form-group">
							<form:input required="required" placeholder="Sähköposti" path="email" cssClass="form-control" autocomplete="off" maxlength="80" />
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-default form-control">LISÄÄ</button>
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    				</form:form>
  				</div>
  				
  				<!-- tab: edit group -->
  				<div id="edit" class="tab-pane fade">
    				<h4>Muokkaa Opiskelijaa</h4>
  				</div>
  				
  				<!-- tab: delete group -->
  				<div id="del" class="tab-pane fade">
    				<h4>Poista Opiskelija</h4>
    				<c:url value="/delStudent" var="delStudent" />
					<form class="form-horizontal" action="${delStudent}" method="post" accept-charset="UTF-8">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<div class="form-group">
							<input required="required" type="text" id="studentID" name="studentID" class="form-control" autocomplete="off" maxlength="20" placeholder="Anna poistettavan oppilaan käyttäjätunnus" />
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-default form-control">POISTA</button>
						</div>
					</form>
  				</div>
			</div>		
    	</div>
    	
    	<!-- group information table -->
    	<div class="col-sm-8 text-left">
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
      		<c:choose>
      			<c:when test="${not empty groups}">
      				<table class="table table-hover">
      					<thead>
      						<tr><th class="text-center">Tunnus</th><th>Ryhmän nimi</th><th class="text-center">Jäseniä</th></tr>
      					</thead>
      					<tbody>
      						<c:forEach var="group" items="${groups}" varStatus="loop">
      						<tr onclick="var f1=document.getElementById('getGroupStudents');f1.groupID.value='${group.groupID}';f1.submit();" class="${selectedGroupID == group.groupID ? 'bold' : ''}"><td class="text-center"><c:out value="${group.groupID}" /></td><td class="text-nowrap"><c:out value="${group.groupName}" /></td><td class="text-center"><c:out value="${group.numGroupMembers}" /></td></tr>     					
      						</c:forEach>
      					</tbody>
      				</table>
      			</c:when>
      			<c:otherwise>
      				<table class="table">
      					<thead>
      						<tr><th class="text-left">Tunnus</th><th>Ryhmän nimi</th><th class="text-center">Jäseniä</th></tr>
      					</thead>
      					<tbody>
      						<tr><td>Ei ryhmiä</td></tr>
      					</tbody>
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