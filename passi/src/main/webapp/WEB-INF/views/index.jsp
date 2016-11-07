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

<style>

</style>
</head>

<body>

<!-- Header embedded with currentPage parameter [/WEB-INF/views/pagename.jsp] -->
<jsp:include page="include/header.jsp">
	<jsp:param name="currentPage" value="${pageContext.request.servletPath}" />
</jsp:include>

<div class="container-fluid bg-3">

	<div class="col-sm-3 text-left col-selections" style="border: 1px solid #92d3ed; border-radius: 15px; background-color: #d3edf8; padding-bottom: 15px;">
    	
		<div class="row">
			<h4>1. Valitse Ryhmä</h4>	
			<c:url var="selectGroupURL" value="/selectGroup" />
			<form action="${selectGroupURL}" method="POST" accept-charset="UTF-8">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    		<div class="form-group">
      		<select class="form-control ${selectedGroup == 0 ? 'highlight' : ''}" id="groupID" name="groupID" onchange="this.form.submit();">
      			<option value="0" ${selectedGroup == 0 ? 'selected' : ''}> --- Valitse Ryhmä --- </option>
      			<c:forEach var="group" items="${groups}">
        			<option value="${group.groupID}" ${selectedGroup == group.groupID ? 'selected' : ''}><c:out value="${group.groupName}" /></option>
        		</c:forEach>
      		</select>
    		</div>
    		</form>	
		</div>

		<div class="row">
			<h4>2. Valitse Kategoria</h4>
			<c:choose>
			<c:when test="${selectedGroup > 0}">
				<c:url var="selectCategoryURL" value="/selectCategory" />
				<form action="${selectCategoryURL}" method="POST" accept-charset="UTF-8">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    			<div class="form-group">
      				<select class="form-control ${selectedGroup > 0 && selectedCategory == 0 ? 'highlight' : ''}" id="categoryID" name="categoryID" onchange="this.form.submit();">
      					<option value="0" ${selectedCategory == 0 ? 'selected' : ''}> --- Valitse Kategoria --- </option>
      					<c:forEach var="c" items="${categories}">
        					<option value="${c.categoryID}" ${selectedCategory == c.categoryID ? 'selected' : ''}><c:out value="${c.categoryName}" /></option>
        				</c:forEach>
      				</select>
    			</div>
    			</form>
			</c:when>
			<c:otherwise>
				<form action="#" method="post">
    			<div class="form-group">
      			<select class="form-control" disabled>
      				<option value="0" ${selectedCategory == 0 ? 'selected' : ''}> --- Ei Kategorioita --- </option>
      			</select>
    			</div>
    			</form>
			</c:otherwise>
			</c:choose>
		</div>

		<div class="row">
			<h4>3. Valitse Tehtäväkortti</h4>
			<c:choose>
			<c:when test="${selectedCategory > 0 && fn:length(worksheets) > 0}">
				<c:url var="selectWorksheetURL" value="/selectWorksheet" />
				<form action="${selectWorksheetURL}" method="POST" accept-charset="UTF-8">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    			<div class="form-group">
      				<select class="form-control ${selectedCategory > 0 && selectedWorksheet == 0 ? 'highlight' : ''}" id="worksheetID" name="worksheetID" onchange="this.form.submit();">
      					<option value="0" ${selectedWorksheet == 0 ? 'selected' : ''}> --- Valitse Tehtäväkortti --- </option>
     	 				<c:forEach var="w" items="${worksheets}">
       		 				<option value="${w.worksheetID}" ${selectedWorksheet == w.worksheetID ? 'selected' : ''}><c:out value="${w.worksheetHeader}" /></option>
     	  				</c:forEach>
     	 			</select>
    			</div>
    			</form>
			</c:when>
			<c:otherwise>
				<form action="#" method="post">
    			<div class="form-group">
      			<select class="form-control" disabled>
      				<option value="0" ${selectedWorksheet == 0 ? 'selected' : ''}> --- Ei Tehtäväkortteja --- </option>
      			</select>
    			</div>
    			</form>
			</c:otherwise>
			</c:choose>
		</div>
		
		<div class="row">
			<h4 class="cursor-default">4. Valitse Jäsen</h4>
   		 	<c:choose>
    			<c:when test="${not empty groupMembers}">
    				<c:url var="selectMemberURL" value="/selectMember" />
					<form id="selectMember" action="${selectMemberURL}" method="POST" accept-charset="UTF-8">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="hidden" id="userID" name="userID" value="" />
   					<table class="table table-hover">
    				<c:forEach var="member" items="${groupMembers}">
    					<c:choose>
    						<c:when test="${isAnsweredMap[member.userID] == 1}">
    							<tr onclick="document.forms['selectMember'].elements['userID'].value='${member.userID}';document.forms['selectMember'].submit();" class="table-top-border ${selectedMember == member.userID ? 'bold' : ''}"><td><c:out value="${member.firstname}" />&nbsp;<c:out value="${member.lastname}" />
    							<div style="float: right; color: green; font-weight: bold;">&#10003;</div></td></tr>   					
    						</c:when>
    						<c:otherwise>
    							<tr onclick="document.forms['selectMember'].elements['userID'].value='${member.userID}';document.forms['selectMember'].submit();" class="table-top-border ${selectedMember == member.userID ? 'bold' : ''}"><td><c:out value="${member.firstname}" />&nbsp;<c:out value="${member.lastname}" />
    							<div style="float: right; color: red; font-weight: bold;">&#10007;</div></td></tr>
    						</c:otherwise>
    					</c:choose>
    				</c:forEach>
    				</table>
    				</form>
    			</c:when>
    		<c:otherwise>
    		<table class="table table">
    		<tr style="border-top: 2px solid #696969;"><td>Ei jäseniä</td></tr>
    		</table>
    		</c:otherwise>
    		</c:choose>
		</div>
		
		<div class="row">
    		<c:choose>
				<c:when test="${selectedMember > 0}">
				<h4 class="cursor-default">Jäsentiedot</h4>
    			<table class="table table-condensed">
    			<tr><th scope="row" class="text-right">Etunimi</th><td class="text-left" style="width: 100%;"><c:out value="${memberDetails.firstname}" /></td></tr>
    			<tr><th scope="row" class="text-right">Sukunimi</th><td class="text-left" style="width: 100%;"><c:out value="${memberDetails.lastname}" /></td></tr>
    			<tr><th scope="row" class="text-right">Sähköposti</th><td class="text-left" style="width: 100%;"><c:out value="${memberDetails.email}" /></td></tr>
    			<tr><th scope="row" class="text-right">Puhelin</th><td class="text-left" style="width: 100%;"><c:out value="${memberDetails.phone}" /></td></tr>
    			</table>
    			</c:when>
    			<c:otherwise></c:otherwise>
    		</c:choose>
		</div>
		
		<div class="row">
    		<c:choose>
    			<c:when test="${selectedGroup > 0}">
    			<h4><c:out value="${fn:length(instructorsDetails) > 1 ? 'Ohjaajat' : 'Ohjaaja'}" /></h4>
    			<table class="table table-condensed" class="hide-in-mobile">
    			<c:forEach var="instructor" items="${instructorsDetails}">
    			<tr><th scope="row" class="text-right">Etunimi</th><td class="text-left" style="width: 100%;"><c:out value="${instructor.firstname}" /></td></tr>
    			<tr><th scope="row" class="text-right">Sukunimi</th><td class="text-left" style="width: 100%;"><c:out value="${instructor.lastname}" /></td></tr>
    			<tr><th scope="row" class="text-right">Sähköposti</th><td class="text-left" style="width: 100%;"><c:out value="${instructor.email}" /></td></tr>
    			<tr><th scope="row" class="text-right">Puhelin</th><td class="text-left" style="width: 100%;"><c:out value="${instructor.phone}" /></td></tr>
    			<tr><th scope="row" class="text-right">&nbsp;</th><td class="text-left" style="width: 100%;">&nbsp;</td></tr>
    			</c:forEach>
   				</table>
    			</c:when>
    			<c:otherwise></c:otherwise>
    		</c:choose>
		</div>

	</div>
	
	<!-- WORKSHEET CONTENT -->
	<div class="col-sm-8 text-left col-content" style="margin: 0 10px 50px 10px; border: 0px dashed #696969; border-radius: 20px;">
	
	<c:choose>
	<c:when test = "${selectedMember > 0}">  
		<div class="row">
  			<h2><c:out value="${worksheetContent.worksheetHeader}" />&nbsp;&nbsp;&bull;&nbsp; <c:out value="${memberDetails.firstname}" />&nbsp;<c:out value="${memberDetails.lastname}" /></h2>		
  			<p class="lead"><c:out value="${worksheetContent.worksheetPreface}" /></p>
  			<p class="lead"><c:out value="${worksheetContent.worksheetPlanning}" /></p>
  			<div class="well consolas">
    		<c:choose>
  			<c:when test="${not empty worksheetAnswers}">
  				<c:out value="${worksheetAnswers.answerPlanning}" />
  			</c:when>
  			<c:otherwise>
  				Ei vastausta
  			</c:otherwise>
  			</c:choose>
    		</div>
 		</div>
 					
  		<c:forEach var="waypoint" items="${worksheetContent.waypoints}" varStatus="loop">
  			<div class="row">
  				<div class="panel panel-default">
  				<div class="panel-heading"><strong><c:out value="${loop.count}" />.&nbsp;<c:out value="${waypoint.waypointTask}" /></strong></div>
  				<div class="panel-body" style="min-height: 100px;">

  				<c:choose>
  				<c:when test="${not empty worksheetAnswers}">
  					<div style="border: 0px dashed #696969; display: block; position: relative; height: auto; overflow: auto;">
  					<c:set var="imageName" value="${waypoint.waypointID}-${worksheetAnswers.userID}" />
  					<c:url var="imageLink" value="/download/${imageName}/jpg" />
  					<img src="${imageLink}" onerror="this.style.display='none'" class="well-image" align="left" draggable="false" />								
  					<p>
  					Monivalinnan vastaus:&nbsp;
  					<c:choose>
  					<c:when test="${worksheetAnswers.waypoints[loop.index].optionID > 0}">
  						<code><c:out value="${worksheetAnswers.waypoints[loop.index].optionText}" /></code>
	  				</c:when>
  					<c:otherwise>
  						<code>Ei valintaa</code>
  					</c:otherwise>
  					</c:choose>
  					</p>
  					<span class="consolas"><c:out value="${worksheetAnswers.waypoints[loop.index].answerWaypointText}" /></span>
  					
  					<!-- FEEDBACK BUTTON -->
  					<button style="position: absolute; right: 0; bottom: 0;" type="button" data-toggle="collapse" data-target="#arviointi-${loop.index}" class="btn btn-md btn-default pull-right assesment-button">Arvioi</button>
					</div>
  				</c:when>
  				<c:otherwise>
  					<span class="consol">Ei vastausta</span>
  				</c:otherwise>
  				</c:choose>
  				</div>
  				</div>
  				
  				<!-- FEEDBACK SECTION -->
				<div style="display: block; position: relative;">
					<div style="padding-top: 10px;" id="arviointi-${loop.index}" class="collapse">
					<c:set var="feedbackContent" value="${worksheetAnswers.waypoints[loop.index].answerWaypointInstructorComment}" />
					<c:choose>
					<c:when test="${not empty feedbackContent}">
						<p><strong>Tämä vastaus on jo arvioitu. Voit halutessasi muuttaa palautetta.</strong></p>
					</c:when>
					<c:otherwise>
						<p><strong>Arvio vastaus kirjallisesti sekä väripainikkeella</strong></p>
					</c:otherwise>
					</c:choose>
					
					<!-- variables -->
					<c:set var="wpID" value="${worksheetAnswers.waypoints[loop.index].answerWaypointID}" />
					<c:set var="instructorRating" value="${worksheetAnswers.waypoints[loop.index].answerWaypointInstructorRating}" />
					
					<c:url var="saveWaypointFeedback" value="/saveWaypointFeedback" />
					<form id="saveFeedback-${wpID}" action="${saveWaypointFeedback}" method="post" accept-charset="UTF-8">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="hidden" id="answerWaypointID" name="answerWaypointID" value="${wpID}" />
					<input type="hidden" id="instructorRating" name="instructorRating" value="${instructorRating}" />
					
					<table>
						<tr>
							<td>
								<div class="form-group">
									
									<textarea rows="4" id="instructorComment" name="instructorComment" class="form-control teacher-assesment-text-${wpID} assesment-textarea" placeholder="Anna palautetta"><c:out value="${feedbackContent}" /></textarea>
								</div>
							</td>
							<td>
								<ul class="${not empty feedbackContent ? 'teacher-multichoice-selected' : ''} teacher-multichoices pull-left" id="multichoices-${wpID}">
									<li onclick="document.forms['saveFeedback-${wpID}'].elements['instructorRating'].value='1';" class="custom-ball button-green ${instructorRating == 1 ? 'button-green-selected' : 'button-green'}" id="ball-${loop.index}"></li>
									<li onclick="document.forms['saveFeedback-${wpID}'].elements['instructorRating'].value='2';" class="custom-ball button-yellow ${instructorRating == 2 ? 'button-yellow-selected' : 'button-yellow'}" id="ball-${loop.index}"></li>
									<li onclick="document.forms['saveFeedback-${wpID}'].elements['instructorRating'].value='3';" class="custom-ball button-red ${instructorRating == 3 ? 'button-red-selected' : 'button-red'}" id="ball-${loop.index}"></li>
								</ul>
							</td>
						</tr>
					</table>
					<button class="palaute btn btn-md btn-default" value="${wpID}">Lähetä</button>
					<span class="label label-danger error-toast-${wpID}">Täytä molemmat kentät</span>
					</form>
					<span class="label label-success success-toast-${wpID}">Arviointi onnistui!</span>
					</div>
				</div>
			</div>
  		</c:forEach>
	</c:when>
	<c:otherwise>
		<div class="row">
  			<h2>Tehtäväkortit</h2>
  			<div class="alert alert-info">
    			<strong>Käyttövinkki:</strong> Tee valinnat pudotusvalikoista vaiheittain ja valitse lopuksi ryhmän jäsen, joka on jo vastannut tehtäväkorttiin.<br /><br />
    			<strong>Esimerkki:</strong> Lähihoidon opiskelijat, Kätilöopisto <strong>></strong> Ammatin työkykyvalmiudet <strong>></strong> Turvallisuuskävely <strong>></strong> Maija Talkanen
  			</div>
  		</div>
	</c:otherwise>
	</c:choose>
</div>
</div>

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="<c:url value="/static/script/jquery-index.js" />"></script>

</body>
</html>