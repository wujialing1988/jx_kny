<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/ReportManager.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.pjjx.base.recorddefine.entity.RecordDI"%>
<%@page import="com.yunda.jx.pjjx.base.recorddefine.entity.PartsStepResult"%>
<%@page import="com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager"%>
<%@page import="com.yunda.jx.pjjx.base.recorddefine.entity.PartsFwList"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>记录单</title>
<script type="text/javascript">
	var objList = <%=JSONUtil.write(QCItemManager.getQCContent(null))%>
	var TYPE_DISMANTLE = "<%=PartsFwList.TYPE_DISMANTLE%>";
	var TYPE_INSTALL = "<%=PartsFwList.TYPE_INSTALL%>";
	var TYPE_INSEPARAB = "<%=PartsFwList.TYPE_INSEPARAB%>";
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/PartsCheckItemSelect.js"></script>
<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/recorddefine/RecordDI.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/recorddefine/PartsFwList.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/recorddefine/PartsTypeForSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/recorddefine/RecordRI.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/recorddefine/PartsStepResult.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/recorddefine/RecordCard.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/recorddefine/Record.js"></script>
<script type="text/javascript">
	var IS_BLANK_YES = '<%= RecordDI.CONST_INT_IS_BLANK_YES %>'						// 是否必填 - 是
	var IS_BLANK_NO = '<%= RecordDI.CONST_INT_IS_BLANK_NO %>'						// 是否必填 - 否
	
	var isDefault_yes = <%=PartsStepResult.ISDEFAULT_YES%>;//是否默认的处理结果 是
	var isDefault_no = <%=PartsStepResult.ISDEFAULT_NO%>;//是否默认的处理结果 否
</script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>