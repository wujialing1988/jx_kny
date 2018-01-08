<jsp:directive.page import="com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager"/>
<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/EdoProject.jspf"%>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf"%>
<%@include file="/frame/jspf/FileUploadField.jspf"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.yunda.jx.jxgc.processdef.entity.JobProcessDef"%>
<%@page import="com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef"%>
<%@page import="com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef"%>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine" %>
<%@page import="com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult" %>
<%@page import="com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef" %>
<%@page import="com.yunda.jxpz.utils.SystemConfigUtil" %>

<%
try{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检修作业流程维护</title>
<style type="text/css">	
	<%-- 修改网页链接<a>访问后、鼠标移上时的字体样式 --%>
	a:VISITED { color:blue; }
	a:HOVER { color:green; font-weight:bold; }
</style>
<script type="text/javascript">
	var STATUS_XZ = '<%=JobProcessDef.CONST_INT_STATUS_XZ%>';			// 流程状态 - 新增
	var STATUS_QY = '<%=JobProcessDef.CONST_INT_STATUS_QY%>';			// 流程状态 - 启用
	var STATUS_ZF = '<%=JobProcessDef.CONST_INT_STATUS_ZF%>';			// 流程状态 - 作废
	
	var IS_LEAF_YES = '<%=JobProcessNodeDef.CONST_INT_IS_LEAF_YES%>';		// 是否叶子节点 - 是
	var IS_LEAF_NO = '<%=JobProcessNodeDef.CONST_INT_IS_LEAF_NO%>';			// 是否叶子节点 - 否
	
	var CODE_RULE_JOB_PROCESS_CODE = '<%=JobProcessDef.CODE_RULE_JOB_PROCESS_CODE%>';					// 配件检修作业流程编码规则
	var CODE_RULE_JOB_PROCESS_NODE_CODE = '<%=JobProcessNodeDef.CODE_RULE_JOB_PROCESS_NODE_CODE%>';		// 配件检修作业流程节点编码规则
	
	var SEQ_CLASS_FS = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_FS %>'					// 完成-开始（编码）
	var SEQ_CLASS_FS_NAME = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_FS_NAME %>'			// 完成-开始（名称）
	var SEQ_CLASS_SS = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_SS %>'					// 开始-开始（编码）
	var SEQ_CLASS_SS_NAME = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_SS_NAME %>'			// 开始-开始（名称）
	var SEQ_CLASS_SF = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_SF %>'					// 开始-完成（编码）
	var SEQ_CLASS_SF_NAME = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_SF_NAME %>'			// 开始-完成（名称）
	var SEQ_CLASS_FF = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_FF %>'					// 完成-完成（编码）
	var SEQ_CLASS_FF_NAME = '<%= JobProcessNodeDef.CONST_STR_SEQ_CLASS_FF_NAME %>'			// 完成-完成（名称）
	
	// 工位新增、启用、作废状态
	var NEW_STATUS = '<%=WorkStation.NEW_STATUS%>';
	var USE_STATUS = '<%=WorkStation.USE_STATUS%>';
	var NULLIFY_STATUS = '<%=WorkStation.NULLIFY_STATUS%>';
	
	
	// 作业节点扩展配置
	var EXT_TRAIN_STATUS = '<%= JobNodeExtConfigDef.EXT_TRAIN_STATUS %>';					// 机车状态设置
    var EXT_CHECK_CONTROL = '<%= JobNodeExtConfigDef.EXT_CHECK_CONTROL %>';					// 质量检查卡控
    var EXT_CHECK_CONTROL_NONE = '<%= JobNodeExtConfigDef.EXT_CHECK_CONTROL_NONE %>';					// 质量检查卡控
    var EXT_CHECK_CONTROL_CURRENT = '<%= JobNodeExtConfigDef.EXT_CHECK_CONTROL_CURRENT %>';				// 质量检查卡控
    var EXT_CHECK_CONTROL_ALL = '<%= JobNodeExtConfigDef.EXT_CHECK_CONTROL_ALL %>';						// 质量检查卡控
    
    var EXT_CHECK_TICKET = '<%= JobNodeExtConfigDef.EXT_CHECK_TICKET %>';					// 检查提票卡控
    var EXT_CHECK_TICKET_NONE = '<%= JobNodeExtConfigDef.EXT_CHECK_TICKET_NONE %>';					// 检查提票卡控 不设卡控
    var EXT_CHECK_TICKET_CURRENT = '<%= JobNodeExtConfigDef.EXT_CHECK_TICKET_CURRENT %>';				// 检查提票卡控 - 卡控当前节点 
    
    
    
	// 流水线新增、启用、作废状态
	var status_new = <%=RepairLine.NEW_STATUS%>;
	var status_use = <%=RepairLine.USE_STATUS%>;
	var status_nullify = <%=RepairLine.NULLIFY_STATUS%>;
	// 修车流水线
	var TYPE_TRAIN = <%=RepairLine.TYPE_TRAIN%>;
	
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	<%-- 图片虚拟路径 --%>
	var deleteIcon = imgpath + '/delete.gif';
	var addIcon = imgpath + '/add.gif';
	
	//作业工单质量检验项目对象
	var objList = <%=JSONUtil.write(WorkSeqManager.getWorkSeqQualityControl("null"))%>
	
	var isDefault_yes = <%=WorkStepResult.ISDEFAULT_YES%>;//是否默认的处理结果 是
	var isDefault_no = <%=WorkStepResult.ISDEFAULT_NO%>;//是否默认的处理结果 否
	
	var isNotBlank_yes = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_YES %>";//是否必填 0必填
	var isNotBlank_no = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_NO %>";//是否必填 1非必填
	
	var PLANMODE_AUTO = '<%=JobProcessNodeDef.PLANMODE_AUTO%>';  //计划模式 - 自动 
	var PLANMODE_MUNAUL = '<%=JobProcessNodeDef.PLANMODE_MUNAUL%>';  //计划模式 - 手动
	var PLANMODE_TIMER = '<%=JobProcessNodeDef.PLANMODE_TIMER%>';  //计划模式 - 定时
	
	// 客货类型
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";		
	
</script>
<!-- 作业工单质量检查项维护 -->
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-TruckFaultReg.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layout/TableFormLayout.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jczl/PartsTypeByBuildSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/WorkStepResult.js"></script> <!-- 检测/修项目新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/DetectItem.js"></script> <!-- 检测项 -->
	<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jcbm/jcpjzd/JcpjzdBuildWin.js"></script><!-- 大部件下拉树 -->
<!-- 质量控制配置在作业工单上 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/QREdit_QrKey.js"></script> <!-- 作业工单新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/WorkStepEdit_QrKey.js"></script> <!-- 检测/修项目新增编辑操作 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/repairrequirement/generator/workseq/WorkStep_QrKey.js"></script> <!-- 检测/修项目 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/RPToWS.js"></script> <!-- 作业工单查看 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobProcessNodeDefWin.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/vis/util/DateUtil.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobProcessGantt.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/WorkSeqSearcher.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/RepairLine.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/WorkStationSearcher.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/WorkStation.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobNodeExtConfigDef.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjjx/base/wpdefine/MatInforList.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobNodeMatDef.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobProcessNodeRelDef.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobProcessNodeDef.js"></script>

<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobProcessPartsOffList.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/generatorProcessdef/JobProcessDef.js"></script>


</head>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>