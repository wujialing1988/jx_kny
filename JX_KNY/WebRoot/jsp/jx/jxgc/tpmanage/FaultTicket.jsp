<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/jx/include/MultiSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicket" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>故障提票</title>
<script type="text/javascript">
	var STATUS_DRAFT = <%=FaultTicket.STATUS_DRAFT%>;
	var STATUS_OPEN = <%=FaultTicket.STATUS_OPEN%>;
	var STATUS_OVER = <%=FaultTicket.STATUS_OVER%>;
	var STATUS_DRAFT_CH = '<%=FaultTicket.STATUS_DRAFT_CH%>';
	var STATUS_OPEN_CH = '<%=FaultTicket.STATUS_OPEN_CH%>';
	var STATUS_OVER_CH = '<%=FaultTicket.STATUS_OVER_CH%>';
	
	var partsBuildUpTypeIdx = "";//组成型号主键	
	var partsBuildUpTypeName = "";//组成型号名称
	var trainNo = '';
	var trainTypeIDX = '';
	var trainTypeShortName = '';
	var OTHERID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.OTHERID%>";//”其它“故障主键ID
	var CUSTOMID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.CUSTOMID%>";//*自定义故障现象主键（-1111）
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var type = "";
	
	var systemOrgname = "${ sessionScope.overseaName}"; 
	var systemOrgid = "${ sessionScope.oversea}";
	var empId = '${sessionScope.emp.empid}'; //用户ID
	var empName = '${sessionScope.emp.empname}';//用户名
	
	// 客货类型
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";	
	
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="BuildUpTypeTree.js"></script>
<script language="javascript" src="FaultTicketWin.js"></script>
<script language="javascript" src="FaultTicket.js"></script>
</head>
<body>
</body>
</html>