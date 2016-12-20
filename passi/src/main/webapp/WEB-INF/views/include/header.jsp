<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default">
  	<div class="container-fluid">
    	<div class="navbar-header">
      		<div class="navbar-header">
      		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
       		<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
        	<span class="icon-bar"></span>
      		</button>
      		<span class="navbar-brand cursor-default">Hallintasivut</span>
    	</div>
    	</div>
    	<div class="collapse navbar-collapse" id="myNavbar">
      		<ul class="nav navbar-nav">
        		<li class="${param.currentPage == '/WEB-INF/views/index.jsp' ? 'active' : ''}"><a href="<c:url value="/index" />">Tietohaku</a></li>
        		<li class="${param.currentPage == '/WEB-INF/views/group.jsp' ? 'active' : ''}"><a href="<c:url value="/index/group" />">Ryhmät</a></li>
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