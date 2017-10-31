<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.tecorder.entity.ZbglTecOrder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>技术指令及措施</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/zb/tecorder/ZbglTecInfo.js"></script>	
<script language="javascript" src="<%=ctx %>/jsp/zb/tecorder/ZbglTecOrder.js"></script>	
<script language="javascript">
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	<%-- 图片虚拟路径 --%>
	var imgpathx = imgpath + '/pjgl.gif';
	
	<%-- 销号方式-单次销号 --%>
	var COMPLETE_SINGAL = <%=ZbglTecOrder.COMPLETE_SINGAL%>;
	<%-- 销号方式-多次销号 --%>
	var COMPLETE_MANY = <%=ZbglTecOrder.COMPLETE_MANY%>;
	<%-- 指令单状态-新增  --%>
	var STATUS_NEW = '<%=ZbglTecOrder.STATUS_NEW%>';
	<%-- 指令单状态-发布 --%>
	var STATUS_PUBLISH = '<%=ZbglTecOrder.STATUS_PUBLISH%>';
	
	var COMPLETE_SINGAL_CH = '<%=ZbglTecOrder.COMPLETE_SINGAL_CH%>';	
	var COMPLETE_MANY_CH = '<%=ZbglTecOrder.COMPLETE_MANY_CH%>';
	var STATUS_NEW_CH = '<%=ZbglTecOrder.STATUS_NEW_CH%>';	
	var STATUS_PUBLISH_CH = '<%=ZbglTecOrder.STATUS_PUBLISH_CH%>';
	
	var STATUS_CANCEL = '<%=ZbglTecOrder.STATUS_CANCEL%>';
	
    var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	
	var Nowdate = new Date();
	var releaseDate = Nowdate.format("Y-m-d H:i");
	
</script>
</head>
<body>
</body>
</html>