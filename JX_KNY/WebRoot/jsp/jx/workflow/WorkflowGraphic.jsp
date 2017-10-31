<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@ page import="com.eos.workflow.api.BPSServiceClientFactory"%>
<%@ taglib uri="http://eos.primeton.com/tags/workflow" prefix="wf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>检修工艺流</title>
    <%--<script type="text/javascript" src="workflow-tag.js"></script>
	<script type="text/javascript" src="workflow.js"></script>--%>
	<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/jx/workflow/Graphic.js"></script>
    <script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/activityInfo.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/workflow/WorkflowGraphic.js"></script>
	<script type="text/javascript">
		var rdpId = '${param.rdpId}';
		var isparts = '${ param.isparts }';
	</script>
  </head>
  
  <body style='background-color:white'>
  <%
  	try{
  	    BPSServiceClientFactory.getLoginManager().setCurrentUser("0","0");
  	    %>
	  	<c:choose>
	  		<c:when test="${not empty param.processInstID}">
			    <wf:processGraph processInstID="${param.processInstID}" processName="${param.processName}" onclick="aclick(this,rdpId)"/>
	  		</c:when>
	  		<c:when test="${not empty param.processID}">
			    <wf:processGraph processID="${param.processID}" processName="${param.processName}"/>
	  		</c:when>
	  	</c:choose>
  	    <%
  	}catch(Exception ex){
  	    out.print(ex.getMessage());
  	}
  %>
  </body>
</html>
