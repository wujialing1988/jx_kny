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
<title>机车检修项目</title>
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
  	
  	//作业工单质量检验项目对象
	var objList = <%=JSONUtil.write(WorkSeqManager.getWorkSeqQualityControl("null"))%>
	
	var isDefault_yes = <%=WorkStepResult.ISDEFAULT_YES%>;//是否默认的处理结果 是
	var isDefault_no = <%=WorkStepResult.ISDEFAULT_NO%>;//是否默认的处理结果 否
	
	var isNotBlank_yes = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_YES %>";//是否必填 0必填
	var isNotBlank_no = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_NO %>";//是否必填 1非必填
	
	var STATUS_ADD = '<%=Constants.STATUS_NEW %>'; //业务状态为新增
  	var STATUS_USE = '<%=Constants.STATUS_USE %>';//业务状态为启用
  	var STATUS_INVALID = '<%=Constants.STATUS_INVALID %>'; //业务状态为报废
	var TYPE_UNLOAD = "<%=com.yunda.jx.jxgc.repairrequirement.entity.PartsList.TYPE_UNLOAD %>";//类型为下车配件
	var TYPE_ABOARD = "<%=com.yunda.jx.jxgc.repairrequirement.entity.PartsList.TYPE_ABOARD %>";//类型为上车配件
	var TYPE_AC = "<%=com.yunda.jx.jxgc.repairrequirement.entity.PartsList.TYPE_AC %>";//类型为在车配件
	
	// 客货类型
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";	
	
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<!-- 作业工单质量检查项维护 -->
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layout/TableFormLayout.js"></script>
<!-- <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/RowEditorGrid.js"></script> -->
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jczl/PartsTypeByBuildSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/WorkStepResult.js"></script> <!-- 检测/修项目新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/DetectItem.js"></script> <!-- 检测项 -->

<!-- 质量控制配置在作业工单上 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/QREdit_QrKey.js"></script> <!-- 作业工单新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/WorkStepEdit_QrKey.js"></script> <!-- 检测/修项目新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/WorkStep_QrKey.js"></script> <!-- 检测/修项目 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/PartsTypeForSelect.js"></script> <!-- 规格型号选择操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/PartsList.js"></script> <!-- 配件清单 -->

<!--修次选择 <script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/RepairTimeSelect.js"></script> -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/RepairProjectEdit.js"></script> <!-- 检修项目编辑页面操作 -->

<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/WorkSeqSelect.js"></script><!-- 检修工单选择 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/RPToWS.js"></script><!-- 检修项目作业工单 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/RepairProject.js"></script><!-- 机车检修项目 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>