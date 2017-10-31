<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicket" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提票工长派工</title>
<script type="text/javascript">		
	//提票状态
	var STATUS_DRAFT = <%=FaultTicket.STATUS_DRAFT%>;
	var STATUS_OPEN = <%=FaultTicket.STATUS_OPEN%>;
	var STATUS_OVER = <%=FaultTicket.STATUS_OVER%>;	
		
	var STATUS_DRAFT_CH = '<%=FaultTicket.STATUS_DRAFT_CH%>';
	var STATUS_OPEN_CH = '<%=FaultTicket.STATUS_OPEN_CH%>';
	var STATUS_OVER_CH = '<%=FaultTicket.STATUS_OVER_CH%>';
    
    var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var img = '<%=ctx %>/frame/resources/images/toolbar/pjgl.gif';<%-- 图片虚拟路径 --%>
</script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/TeamEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/tpmanage/FaultEmpSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/tpmanage/FaultTicketGzpg.js"></script>
</head>
<body>
</body>
</html>