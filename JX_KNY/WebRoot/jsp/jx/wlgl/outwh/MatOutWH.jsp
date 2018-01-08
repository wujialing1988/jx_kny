<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager"%>
<%@page import="com.yunda.jx.wlgl.outwh.entity.MatOutWH"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物料出库</title>
<script type="text/javascript">
	var objList = <%=JSONUtil.write(MatTypeListManager.getMatType())%>
	var empId = '${sessionScope.emp.empid}';
	var TYPE_CKDJC = '<%=MatOutWH.TYPE_CKDJC%>'; //出库到机车
	var TYPE_QTYY = '<%=MatOutWH.TYPE_QTYY%>'; //其他原因出库
	var TYPE_YK = '<%=MatOutWH.TYPE_YK%>'; //移库出库
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>'
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/OmEmpBaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/outwh/MatOutWHFun.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/inwh/MatStockInfo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/outwh/MatOutWHTab2.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/outwh/MatOutWHTab1.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/wlgl/outwh/MatOutWH.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>