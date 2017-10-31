<jsp:directive.page import="com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager"/>
<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件质量记录单默认派工</title>
<script type="text/javascript">
    //TODO 确定无用就删除
	//质量记录单启用新增作废状态（可通用）
	var STATUS_ADD = <%=com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq.STATUS_ADD%>
	var STATUS_USE = <%=com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq.STATUS_USE%>
	var STATUS_INVALID = <%=com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq.STATUS_INVALID%>
	var buildUpType = 2;  //配件组成类型
</script>
<script type="text/javascript">
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/dispatch/PartsDefaultQR.js"></script> <!-- 机车质量记录单 -->

</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>