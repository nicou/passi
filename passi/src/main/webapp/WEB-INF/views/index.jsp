<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
/* Disabled for now to fix browser back-button function
int timeout = session.getMaxInactiveInterval();
String contextPath = request.getContextPath();
response.setHeader("Refresh", timeout + "; URL = " + contextPath + "/expired");
*/
%>

<!DOCTYPE html>
<html>
<head>
<meta name="author" content="Mika Ropponen, Nico Hagelberg" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Työkykypassi&nbsp;&bull;&nbsp;Tietohaku</title>

<!-- CSS -->
<link rel="stylesheet" href="<c:url value="/static/style/bootstrap.min.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/static/style/main.css" />" />

<!--[if lt IE 9]>
  <link rel="stylesheet" href="<c:url value="/static/style/ie.css" />" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>

<body>

<!-- Header embedded with currentPage parameter [/WEB-INF/views/pagename.jsp] -->
<jsp:include page="include/header.jsp">
	<jsp:param name="currentPage" value="${pageContext.request.servletPath}" />
</jsp:include>

<div class="container-fluid bg-3">

	<div class="col-sm-3 text-left col-selections" style="border: 1px solid #92d3ed; border-radius: 15px; background-color: #d3edf8; padding-bottom: 15px;">
    	
		<div class="row row-padding">
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

		<div class="row row-padding">
			<h4>2. Valitse tehtäväalue</h4>
			<c:choose>
			<c:when test="${selectedGroup > 0}">
				<c:url var="selectCategoryURL" value="/selectCategory" />
				<form action="${selectCategoryURL}" method="POST" accept-charset="UTF-8">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    			<div class="form-group">
      				<select class="form-control ${selectedGroup > 0 && selectedCategory == 0 ? 'highlight' : ''}" id="categoryID" name="categoryID" onchange="this.form.submit();">
      					<option value="0" ${selectedCategory == 0 ? 'selected' : ''}> --- Valitse tehtäväalue --- </option>
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

		<div class="row row-padding">
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
		
		<div class="row row-padding">
			<h4 class="cursor-default">4. Valitse Jäsen</h4>
   		 	<c:choose>
    			<c:when test="${not empty groupMembers}">
    				<c:url var="selectMemberURL" value="/selectMember" />
					<form id="selectMember" action="${selectMemberURL}" method="POST" accept-charset="UTF-8">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="hidden" id="userID" name="userID" value="" />
   					<table class="table table-hover table-select-user">
    				<c:forEach var="member" items="${groupMembers}">
    					<c:choose>
    						<c:when test="${isAnsweredMap[member.userID] == 1}">
    							<tr onclick="document.forms['selectMember'].elements['userID'].value='${member.userID}';document.forms['selectMember'].submit(${selectedWorksheet == 0 ? 'return false;' : ''});" class="table-top-border ${selectedMember == member.userID ? 'bold' : ''}"><td><c:out value="${member.firstname}" />&nbsp;<c:out value="${member.lastname}" />
    							<c:if test="${selectedWorksheet > 0}"><div style="float: right; color: green; font-weight: bold;">&#10003;</div></c:if></td></tr>   					
    						</c:when>
    						<c:otherwise>
    							<tr onclick="document.forms['selectMember'].elements['userID'].value='${member.userID}';document.forms['selectMember'].submit(${selectedWorksheet == 0 ? 'return false;' : ''});" class="table-top-border ${selectedMember == member.userID ? 'bold' : ''}"><td><c:out value="${member.firstname}" />&nbsp;<c:out value="${member.lastname}" />
    							<c:if test="${selectedWorksheet > 0}"><div style="float: right; color: red; font-weight: bold;">&#10007;</div></c:if></td></tr>
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
		
		<div class="row row-padding sidebar-instructors">
    	<c:choose>
    		<c:when test="${selectedGroup > 0}">
    		<h4 style="font-weight: bold; margin-top: 0; padding-top: 0;"><c:out value="${fn:length(instructorsDetails) > 1 ? 'Työkykypassiryhmän ohjaajat' : 'Työkykypassiryhmän ohjaaja'}" /></h4>
    		<c:forEach var="instructor" items="${instructorsDetails}">
    		<span class="bold"><c:out value="${instructor.firstname} ${instructor.lastname }" /></span><br />
    		<c:out value="${instructor.email}" />
    		<hr />
    		</c:forEach>
    		</c:when>
    		<c:otherwise></c:otherwise>
    	</c:choose>
    	</div>

	</div>
	
	<!-- WORKSHEET CONTENT -->
	<div class="col-sm-8 text-left col-content" style="margin: 0 10px 50px 10px; border: 0px dashed #696969; border-radius: 20px;">
	
	<c:choose>
	<c:when test="${selectedMember > 0}">  
		<div class="row row-padding">
  			<h2><c:out value="${worksheetContent.worksheetHeader}" />&nbsp;&nbsp;&bull;&nbsp; <c:out value="${memberDetails.firstname}" />&nbsp;<c:out value="${memberDetails.lastname}" /></h2>		
  			<p class="lead"><c:out value="${worksheetContent.worksheetPreface}" /></p>
  			<p class="lead"><c:out value="${worksheetContent.worksheetPlanning}" /></p>
  			<div class="well consolas">
    		<c:choose>
  			<c:when test="${worksheetAnswers.answerID > 0}">
  				<c:out value="${worksheetAnswers.answerPlanning}" />
  			</c:when>
  			<c:otherwise>
  				Ei vastausta
  			</c:otherwise>
  			</c:choose>
    		</div>
 		</div>
 					
  		<c:forEach var="waypoint" items="${worksheetContent.waypoints}" varStatus="loop">
  			<div class="row row-padding">
  				<div class="panel panel-default">
  				<div class="panel-heading"><strong><c:out value="${loop.count}" />.&nbsp;<c:out value="${waypoint.waypointTask}" /></strong></div>
  				<div class="panel-body">

  				<c:choose>
  				<c:when test="${worksheetAnswers.answerID > 0}">
  					<div class="row row-padding">
  					<div class="col-xs-12 col-md-3">
  					<c:set var="imageName" value="${waypoint.waypointID}-${worksheetAnswers.userID}" />
  					<c:url var="imageLink" value="/download/${imageName}/jpg" />
  					<div class="well-image-container">
  					<!-- ANSWER IMAGE -->
  					<a href="${imageLink}" class="lightbox_trigger"><img src="${imageLink}" onerror="this.style.display='none'" class="well-image" draggable="false" alt="Valokuva" /></a><br />
  					<div class="image-click">Klikkaa</div>
					</div>
					</div>
					<div class="col-xs-12 col-md-9">						
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
  					
  					
  					<!-- Feedback section -->
  					<c:set var="feedbackContent" value="${worksheetAnswers.waypoints[loop.index].answerWaypointInstructorComment}" />
					<c:set var="wpID" value="${worksheetAnswers.waypoints[loop.index].answerWaypointID}" />
					<c:set var="instructorRating" value="${worksheetAnswers.waypoints[loop.index].answerWaypointInstructorRating}" />
  					<div>
  					<hr />
  					<h5 style="font-weight: bold;">Arviointi</h5>
  					<span class="label label-danger toast error-toast-${wpID}">Ole hyvä ja vastaa molempiin kenttiin</span>
					<span class="label label-success toast success-toast-${wpID}">Arviointi onnistui!</span>
					<c:choose>
					<c:when test="${not empty feedbackContent}">
						<div class="hasFeedback">
						<p>
							<span class="consolas"><c:out value="${feedbackContent}" /></span>
							<br /><br />
							<button type="button" class="btn btn-sm btn-secondary" onClick="$('#feedback-div-${wpID}').slideToggle(); this.blur();">Näytä arviointilomake</button>
						</p>
						</div>
					</c:when>
					<c:otherwise>
						<div class="text-info"><p>Arvio vastaus kirjallisesti sekä väripainikkeella</p></div>
					</c:otherwise>
					</c:choose>
					
					<c:url var="saveWaypointFeedback" value="/saveWaypointFeedback" />
					<form id="saveFeedback-${wpID}" action="${saveWaypointFeedback}" method="post" accept-charset="UTF-8">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="hidden" name="answerWaypointID" value="${wpID}" />
					<input type="hidden" name="instructorRating" value="${instructorRating}" />
					<div class="row" id="feedback-div-${wpID}" style="${not empty feedbackContent ? 'display: none;' : ''}">
					<div class="col-xs-12">
					</div>
					<div class="col-xs-12 form-group">
						<textarea maxlength="1000" rows="5" id="instructorComment" name="instructorComment" class="form-control teacher-assessment-text-${wpID} assessment-textarea" placeholder="Anna palautetta"><c:out value="${feedbackContent}" /></textarea>
					</div>
					<div class="col-xs-6">
						<ul class="${not empty feedbackContent ? 'teacher-multichoice-selected' : ''} teacher-multichoices" id="multichoices-${wpID}">
							<li onclick="document.forms['saveFeedback-${wpID}'].elements['instructorRating'].value='1';" class="custom-ball button-green ${instructorRating == 1 ? 'button-green-selected' : 'button-green'}" id="ball-${loop.index}"></li>
							<li onclick="document.forms['saveFeedback-${wpID}'].elements['instructorRating'].value='2';" class="custom-ball button-yellow ${instructorRating == 2 ? 'button-yellow-selected' : 'button-yellow'}" id="ball-${loop.index}"></li>
							<li onclick="document.forms['saveFeedback-${wpID}'].elements['instructorRating'].value='3';" class="custom-ball button-red ${instructorRating == 3 ? 'button-red-selected' : 'button-red'}" id="ball-${loop.index}"></li>		
						</ul>
					</div>
					<div class="col-xs-6 text-right">
						<button class="palaute btn btn-default btn-md" value="${wpID}">Tallenna</button><br />
					</div>

					</div>

					</form>
  					
  					</div>
  					
  					</div>
  					
  					</div>
  				</c:when>
  				<c:otherwise>
  					<span class="consol">Ei vastausta</span>
  				</c:otherwise>
  				</c:choose>
  				</div>
  				</div>
  				
  				<!-- FEEDBACK SECTION -->
				<div style="padding: 0 0 0 15px; display: block; position: relative;">
					<div style="padding-top: 10px;" id="arviointi-${loop.index}" class="collapse">

					</div>
				</div>
			</div>
  		</c:forEach>
  		
  		  		<div class="row row-padding">
  				<div class="panel panel-default">
  				<div class="panel-heading"><strong>Koko tehtäväkortin koostepalaute</strong></div>
  				<div class="panel-body">
  				
  				<form action="saveInstructorComment" method="post" accept-charset="UTF-8">
  				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  				<input type="hidden" name="answerID" value="${worksheetAnswers.answerID }" />
  				<div class="row">
  				<div class="col-xs-12 form-group">
  					<textarea name="instructorComment" class="form-control" rows="7" maxlength="1000" placeholder="Anna koostepalaute"><c:if test="${not empty worksheetAnswers.answerInstructorComment }"><c:out value="${worksheetAnswers.answerInstructorComment}" /></c:if></textarea>
  				</div>
  				<div class="col-xs-12 text-right">
  					<label style="margin-right: 15px;">
						<input type="checkbox" name="feedback_complete" value="true" <c:if test="${worksheetAnswers.feedbackComplete }">checked</c:if>/> Merkitse koko tehtäväkortti arvostelluksi
					</label>
  					<button class="btn btn-secondary" type="submit">Lähetä</button>
  				</div>
  				</div>
  				</form>
  				
  				</div>
  				</div>
  				</div>
	</c:when>
	<c:otherwise>
		<div class="row row-padding">
  			<h2>Työterveyslaitoksen Työkykypassi</h2>
  			<div class="alert alert-warning">
   				<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    			<strong>Tervetuloa työkykypassiin, <c:out value="${userDetails.firstname} ${userDetails.lastname}" /></strong>
  			</div>
  			<div class="alert alert-info">
    			<strong>Käyttövinkki<br /></strong> Tee valinnat pudotusvalikoista vaiheittain ja valitse lopuksi ryhmän jäsen, jonka vastauksia haluat tarkastella.<br /><br />
    			<strong>Esimerkki<br /></strong> Lähihoidon opiskelijat, Kätilöopisto <strong>></strong> Ammatin työkykyvalmiudet <strong>></strong> Turvallisuuskävely <strong>></strong> Maija Talkanen
  			</div>
  		</div>
	</c:otherwise>
	</c:choose>
</div>
</div>

<div id="lightbox">
	<div id="content">
        <img src="#" class="rotateImage" draggable="false" alt="Valokuva"/>
    </div>
</div>
	

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="<c:url value="/static/script/bootstrap.min.js" />"></script>
<script src="<c:url value="/static/script/jquery-index.js" />"></script>

<c:if test="${not empty message }">
<span class="label label-success toast" id="successtoast"><c:out value="${message }"></c:out></span>
<script type="text/javascript">$('#successtoast').fadeIn(1000).delay(2000).fadeOut(1000);</script>
</c:if>
<script>
$('select').change(function() {
	$(this).blur();
});
</script>
<c:set var="message" scope="session" value="" />

</body>
</html>