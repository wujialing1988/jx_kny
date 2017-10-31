<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@ page import="com.eos.workflow.api.BPSServiceClientFactory"%>
<%@ taglib uri="http://eos.primeton.com/tags/workflow" prefix="wf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>流程图</title>
    <style type="text/css">
    	<%
    		if(request.getParameter("color")!=null){
    		    out.print("input{ color: red;}");
    		}
    	%>
    </style>
    <%--<script type="text/javascript" src="workflow-tag.js"></script>
	<script type="text/javascript" src="workflow.js"></script>--%>
	<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/jx/workflow/Graphic.js"></script>
    <script language="javascript">	
		var ctx = '<%=ctx %>';
		var rdpId = '${ param.rdpId }';
		var iszb = '${ param.zb }';
		var isparts = '${ param.isparts }';
	</script>
	<script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
	<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/activityInfo.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/workflow/WorkflowGraphic.js"></script>
  </head>
  <body style="background-color:${ (param.color == null) ? ('white') : (param.color) }">
  <%
  	try{
  	    BPSServiceClientFactory.getLoginManager().setCurrentUser("0","0");
  	    %>
	  	<c:choose>
	  		<c:when test="${not empty param.processInstID}">
			    <wf:processGraph processInstID="${param.processInstID}" onclick="aclick(this, rdpId)"/>
	  		</c:when>
	  		<c:when test="${not empty param.processID}">
			    <wf:processGraph processID="${param.processID}" />
	  		</c:when>
	  		<c:when test="${not empty param.processName}">
			    <wf:processGraph processName="${param.processName}"/>
	  		</c:when>
	  	</c:choose>
  	    <%
  	}catch(Exception ex){
  	    out.print(ex.getMessage());
  	}
  %>
  </body>
</html>
