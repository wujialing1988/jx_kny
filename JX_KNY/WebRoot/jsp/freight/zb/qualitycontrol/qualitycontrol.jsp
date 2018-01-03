<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@page import="com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlItemDefine" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>整备质量检查维护</title>

<script type="text/javascript">
    var IS_DEFAULT_NO = '<%= ZbglQualityControlItemDefine.IS_DEFAULT_NO %>';            // 是否默认 - 否
    var IS_DEFAULT_YES = '<%= ZbglQualityControlItemDefine.IS_DEFAULT_YES %>';          // 是否默认 - 是
    
    var siteID = '<%= EntityUtil.findSysSiteId(null) %>';           // 站场ID
   	/** 车辆类型：10货车，20客车 */
	var vehicleType = '<%=request.getParameter("vehicleType") %>';
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-QualityInspectionConfig.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/qualitycontrol/zbglItemEmpDefine.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/qualitycontrol/qualityControlItemEmpDefine.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/qualitycontrol/qualityControlItemDefine.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/freight/zb/qualitycontrol/qualitycontrol.js"></script> <!-- 主界面布局 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>