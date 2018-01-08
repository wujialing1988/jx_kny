<jsp:directive.page import="com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager"/>
<jsp:directive.page import="com.yunda.jxpz.utils.SystemConfigUtil"/>
<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/ReportManager.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.repairrequirement.entity.RepairProject" %>
<%@page import="com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult" %>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业工单处理优化-历史数据迁移</title>
<script type="text/javascript">
	//组成型号状态
	var TYPE_TRAIN = <%=BuildUpType.TYPE_TRAIN%>
	var TYPE_PARTS = <%=BuildUpType.TYPE_PARTS%>
	var TYPE_VIRTUAL = <%=BuildUpType.TYPE_VIRTUAL%>
	var USE_STATUS = <%=BuildUpType.USE_STATUS%>
	//工序卡启用状态
	var WORKSEQ_STATUS_USE = '<%=Constants.STATUS_USE %>';//业务状态为启用
	//作业工单质量检验项目对象
	var objList = <%=JSONUtil.write(WorkSeqManager.getWorkSeqQualityControl("null"))%>
	
	var isDefault_yes = <%=WorkStepResult.ISDEFAULT_YES%>;//是否默认的处理结果 是
	var isDefault_no = <%=WorkStepResult.ISDEFAULT_NO%>;//是否默认的处理结果 否
	
	var isNotBlank_yes = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_YES %>";//是否必填 0必填
	var isNotBlank_no = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_NO %>";//是否必填 1非必填
	
	var STATUS_ADD = '<%=Constants.STATUS_NEW %>'; //业务状态为新增
  	var STATUS_USE = '<%=Constants.STATUS_USE %>';//业务状态为启用
  	var STATUS_INVALID = '<%=Constants.STATUS_INVALID %>'; //业务状态为报废
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/ProjectToRecordCard.js"></script><!-- 机车检修项目 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>