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
<jsp:include page="header.jsp">
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
    		<h3 class="cursor-default">Lohko 1</h3>
    	</div>
    	<div class="col-sm-4 text-left">
      		<h3 class="cursor-default">Lohko 2</h3>
    	</div>
    	<div class="col-sm-4 text-left">
      		<h3 class="cursor-default">Lohko 3</h3>
    	</div>
  	</div>
</div>

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="<c:url value="/static/script/index.js" />"></script>

</body>
</html>