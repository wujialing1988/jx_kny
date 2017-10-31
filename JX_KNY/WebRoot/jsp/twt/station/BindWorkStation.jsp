<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=8" http-equiv="X-UA-Compatible">
<title>台位绑定工位</title>
<%
	String deskCode = request.getParameter("code");				// 获取URL传入的参数台位编号
	if (null == deskCode) deskCode = "301";				// *开发测试默认值
	String deskName = request.getParameter("name");	// 获取URL传入的参数台位名称
	if (null == deskName) deskName = "干阻棚HXN3";	// *开发测试默认值
	String mapcode = request.getParameter("mapcode");	// 获取URL传入的参数所属图
	if (null == mapcode) mapcode = "A";	// *开发测试默认值
%>
<style type="text/css">
	//表单控件隐藏时css样式，用于解决隐藏时有时控件无法显示的bug
	.my-disabled {
		color:gray;cursor:default;
	}
	.my-disabled .x-form-trigger-over {
	  	background-position:0 0 !important;
	  	border-bottom: 1px solid #B5B8C8;
	}
	.my-disabled .x-form-trigger-click{
 	 	background-position:0 0 !important;
  		border-bottom: 1px solid #B5B8C8;
	}
</style>

<script type="text/javascript">
	var deskName = '<%= deskName %>';
	var deskCode = '<%= deskCode %>';
	var mapcode = '<%= mapcode %>';
	//流水线新增、启用、作废状态
	var status_new = <%=RepairLine.NEW_STATUS%>;
	var status_use = <%=RepairLine.USE_STATUS%>;
	var status_nullify = <%=RepairLine.NULLIFY_STATUS%>;
	//工位新增、启用、作废状态
	var stationStatus_new = <%=WorkStation.NEW_STATUS%>;
	var stationStatus_use = <%=WorkStation.USE_STATUS%>;
	var stationStatus_nullify = <%=WorkStation.NULLIFY_STATUS%>;
	//流水线类型
	var type = <%=RepairLine.TYPE_TRAIN%>;
	//var type_parts = <%=RepairLine.TYPE_PARTS%>;	
</script>
<script language="javascript" src="<%=ctx %>/jsp/twt/station/WorkStationForSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/twt/station/BindWorkStation.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>