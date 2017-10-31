<jsp:directive.page import="com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager"/>
<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult" %>
<%@page import="com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq" %>
<%@page import="com.yunda.jxpz.utils.SystemConfigUtil" %>

<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车作业工单维护</title>
<script type="text/javascript">
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
</script>
<script type="text/javascript">
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layout/TableFormLayout.js"></script>
<!-- <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/RowEditorGrid.js"></script> -->
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jczl/PartsTypeByBuildSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/WorkStepResult.js"></script> <!-- 检测/修项目新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/DetectItem.js"></script> <!-- 检测项 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/PartsTypeForSelect.js"></script> <!-- 规格型号选择操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/PartsList.js"></script> <!-- 配件清单 -->

<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/QREdit_QrKey.js"></script> <!-- 作业工单新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/WorkStepEdit_QrKey.js"></script> <!-- 检测/修项目新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/WorkStep_QrKey.js"></script> <!-- 检测/修项目 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/workseq/TrainQR_QrKey.js"></script> <!-- 机车作业工单 -->

</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>