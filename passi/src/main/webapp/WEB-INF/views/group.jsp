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

<title>Työkykypassi&nbsp;&bull;&nbsp;Ryhmähallinta</title>

<!-- CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/main.css" />" />
</head>

<body>

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
    		<ul class="nav nav-tabs" id="navTabs">
    			<li class="${empty selectedTab ? 'active' : ''}"><a data-toggle="tab" href="#add" onclick="this.blur();">Luo uusi ryhmä</a></li>
    			<li class="${selectedTab == 'edit' ? 'active' : ''}"><a data-toggle="tab" href="#edit" onclick="this.blur();">Muokkaa ryhmää</a></li>
    			<li class="${selectedTab == 'edit' ? 'active' : ''}"><a data-toggle="tab" href="#users" onclick="this.blur();">Ryhmän opiskelijat</a></li>
    		</ul>
    		
    		<div class="tab-content" style="padding: 15px;">
    		
    			<!-- tab: add group -->
  				<div id="add" class="tab-pane fade in active">
    				<c:url value="/addGroup" var="addGroup" />
    				<form:form role="form" class="form-horizontal" modelAttribute="newGroup" action="${addGroup}" method="post" accept-charset="UTF-8">
  						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  						<div class="form-group">
							<label>Ryhmän nimi ja tunnus</label>
							<form:input required="required" placeholder="Kirjoita ryhmän nimi ja tunnus" path="groupName" cssClass="form-control" autocomplete="off" maxlength="20" />
							<small class="text-muted">Esimerkki: Autoalan perustutkinto, Omnian ammattiopisto: AUTB6</small>
						</div>
						<div class="form-group">
							<label>Ryhmän liittymisavain</label>
							<form:input required="required" placeholder="Kirjoita ryhmän liittymisavain" path="groupKey" cssClass="form-control" autocomplete="off" maxlength="50" />
							<small class="text-muted">Käytä liittymisavaimena pienillä kirjaimilla kirjoitettu sanaa</small>
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-default form-control">Tallenna</button>
						</div>
    				</form:form>
  				</div>
  				
  				<!-- tab: edit group -->
  				<div id="edit" class="tab-pane fade">
    				<c:url value="/editGroup" var="editGroup" />
    				<form:form role="form" class="form-horizontal" modelAttribute="newGroup" action="${editGroup}" method="post" accept-charset="UTF-8">
    					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    					<form:input id="editGroupID" path="groupID" type="hidden" value="0" />
  						<div class="form-group">
  							<label>Ryhmän nimi ja tunnus</label>
							<form:input id="editGroupName" required="required" placeholder="Kirjoita ryhmän nimi ja tunnus" path="groupName" cssClass="form-control" autocomplete="off" maxlength="20" />
							<small class="text-muted">Esimerkki: Autoalan perustutkinto, Omnian ammattiopisto: AUTB6</small>
						</div>
						<div class="form-group">
							<label>Ryhmän liittymisavain</label>
							<form:input id="editGroupKey" required="required" placeholder="Kirjoita ryhmän liittymisavain" path="groupKey" cssClass="form-control" autocomplete="off" maxlength="50" />
							<small class="text-muted">Käytä liittymisavaimena pienillä kirjaimilla kirjoitettu sanaa</small>
						</div>
    					<div class="form-group">
    					<button type="button" onclick="clearEditFields()" class="btn btn-default pull-left" style="width: 45%">Peruuta</button>
    					<button type="submit" class="btn btn-default pull-right" style="width: 45%;">Tallenna</button>
    					</div>
    				</form:form>
  				</div>
  				
  				<!-- tab: manage group users -->
  				<div id="users" class="tab-pane fade" style="padding: 15px;">
					<table class="table table-striped">
						<thead>
						<tr>
							<td>Oppilas</td>
							<td></td>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td>Jaakko Pavunvarsi</td>
							<td>[x]</td>
						</tr>
						<tr>
							<td>Jaakko Pavunvarsi</td>
							<td>[x]</td>
						</tr>
						<tr>
							<td>Jaakko Pavunvarsi</td>
							<td>[x]</td>
						</tr>
						<tr>
							<td>Jaakko Pavunvarsi</td>
							<td>[x]</td>
						</tr>
						</tbody>
					</table>
  				</div>
  				
			</div>	
			
			<!-- Message panel -->
    		<c:if test="${not empty message}">
    			<div class="alert alert-info">
   					<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    				<strong>Info!</strong>&nbsp;&nbsp;<c:out value="${message}" />
  				</div>
    		</c:if>
    			
    	</div>
    	
    	<!-- group information table -->
    	<div class="col-sm-8 text-left">
      		<c:choose>
      			<c:when test="${not empty groups}">
      				<form:form action="/passi/delGroup" method="post" accept-charset="UTF-8">
	      			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      				<table class="table table-striped">
      					<thead>
      						<tr>
	      						<th class="text-center">ID</th>
	      						<th>Ryhmän nimi&nbsp;&nbsp;[&nbsp;A - Ö&nbsp;]</th>
	      						<th>Liittymisavain</th>
	      						<th>Hallinta</th>
      						</tr>
      					</thead>
      					<tbody>
      						<c:forEach var="group" items="${groups}" varStatus="loop">
      						<tr>
	      						<td class="text-center">
	      							<c:out value="${group.groupID}" />
	      						</td>
	      						<td class="text-nowrap">
	      							<a href="#!"><c:out value="${group.groupName}" /></a>
	      						</td>
	      						<td class="text-left">
	      							<c:out value="${group.groupKey}" />
	      						</td>
	      						<td>
	      							<button onclick="editGroup(${group.groupID}); this.blur();" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span></button>
	      							<button value="${group.groupID}" name="groupID" type="submit" class="btn btn-default" onclick="if(!confirm('Haluatko varmasti poistaa ryhmän pysyvästi?')){return false;}else{this.submit()}; this.blur();"><span class="glyphicon glyphicon-trash"></span></button>
	      						</td>
      						</tr>    					
      						</c:forEach>
      					</tbody>
      				</table>
      				</form:form>
      			</c:when>
      			<c:otherwise>
      				<table class="table">
      					<thead>
      						<tr><th class="text-left">ID</th><th>Ryhmän nimi&nbsp;&nbsp;[&nbsp;A - Ö&nbsp;]</th><th>Liittymisavain</th></tr>
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
<script src="<c:url value="/static/script/editgroup.js" />"></script>

</body>
</html>