<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>工序确认</title>
		<script type="text/javascript">
			var plantSeq = '......';
			<%--
				if(session.getAttribute("plant") != null && !"".equals(session.getAttribute("plant"))){
				    com.yunda.system.omorganization.entity.OmOrganization plant = com.yunda.jx.component.manager.OmOrganizationSelectManager.getOrgById(Long.valueOf(session.getAttribute("plant").toString()));
				    %>
			plantSeq = '<%=plant.getOrgseq() %>';
				    <%
				}
			--%>
		</script>
		<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
		<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/TaskConfirm/WorkSeqConfirm.js"></script>
	</head>
	<body>
	</body>
</html>