<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件检修作业</title>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	var teamOrgId = '${sessionScope.org.orgid}'; <%-- 当前登录人员部门ID --%>
	var teamOrgName = '${sessionScope.org.orgname}'; <%-- 当前登录人员部门名称 --%>
	var PARTS_STATUS_ZC = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.PARTS_STATUS_ZC%>';  //配件状态--在册
	var STATUS_WQD = '<%=com.yunda.jx.pjjx.partsrdp.entity.PartsRdp.STATUS_WQD%>';  //兑现单状态--未启动
	var STATUS_JXZ = '<%=com.yunda.jx.pjjx.partsrdp.entity.PartsRdp.STATUS_JXZ%>';  //兑现单状态--检修中
	var STATUS_DYS = '<%=com.yunda.jx.pjjx.partsrdp.entity.PartsRdp.STATUS_DYS%>';  //兑现单状态--待验收
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	<%-- 图片虚拟路径 --%>
	var imgpathx = imgpath + '/delete.gif';
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/partsrdp/PartsRdpChooseWP.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/TeamEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/partsrdp/PartsRdpChooseEmp.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/partsrdp/PartsRdp.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>