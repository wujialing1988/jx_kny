<jsp:directive.page import="com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager"/>
<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/EdoProject.jspf"%>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.zbfw.entity.ZbFwWidi"%>
<%@page import="com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef"%>
<%@page import="com.yunda.zb.zbfw.entity.ZbglJobNodeExtConfigDef"%>
<%@page import="com.yunda.jxpz.utils.SystemConfigUtil" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation" %>
<%@page import="com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine" %>
<%@page import="com.yunda.zb.zbfw.entity.ZbFwWi" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列检车辆范围</title>
<script type="text/javascript">

	var processTips;

	function showtip(){
		var el;
		if(TrainType.selectWin.isVisible()){
			el = TrainType.selectWin.getEl();
		}else{
			el = Ext.getBody();
		}
		processTips = new Ext.LoadMask(el,{
			msg:"正在处理你的操作，请稍后...",
			removeMask:true
		});
		processTips.show();
	}
	
	function hidetip(){
		processTips.hide();
	}


	// 流水线新增、启用、作废状态
	var status_new = <%=RepairLine.NEW_STATUS%>;
	var status_use = <%=RepairLine.USE_STATUS%>;
	var status_nullify = <%=RepairLine.NULLIFY_STATUS%>;
	// 修车流水线
	var TYPE_TRAIN = <%=RepairLine.TYPE_TRAIN%>;
	// 工位新增、启用、作废状态
	var NEW_STATUS = '<%=WorkStation.NEW_STATUS%>';
	var USE_STATUS = '<%=WorkStation.USE_STATUS%>';
	var NULLIFY_STATUS = '<%=WorkStation.NULLIFY_STATUS%>';

	var zbfwIdx = ''; //全局变量
	
    var IS_BLANK_YES = '<%= ZbFwWidi.ZBFW_WIDI_IS_BLANK_YES %>'						// 是否必填 - 是
	var IS_BLANK_NO = '<%= ZbFwWidi.ZBFW_WIDI_IS_BLANK_NO %>'	
    var DATA_TYPE_ZF = '<%= ZbFwWidi.ZBFW_WIDI_DATA_TYPE_ZF %>'						// 数据值类型 - 字符
	var DATA_TYPE_SZ = '<%= ZbFwWidi.ZBFW_WIDI_DATA_TYPE_SZ %>'	                    // 数据值类型 - 数字
	
	var IS_LEAF_YES = '<%=ZbglJobProcessNodeDef.CONST_INT_IS_LEAF_YES%>';		// 是否叶子节点 - 是
	var IS_LEAF_NO = '<%=ZbglJobProcessNodeDef.CONST_INT_IS_LEAF_NO%>';			// 是否叶子节点 - 否
	
	var CODE_RULE_JOB_PROCESS_CODE = '<%=com.yunda.zb.zbfw.entity.ZbFw.CODE_RULE_JOB_PROCESS_CODE%>';					// 整备作业流程编码规则
	var CODE_RULE_JOB_PROCESS_NODE_CODE = '<%=ZbglJobProcessNodeDef.CODE_RULE_JOB_PROCESS_NODE_CODE%>';		// 整备作业流程节点编码规则
	
	
	// 作业节点扩展配置
	var EXT_TRAIN_STATUS = '<%= ZbglJobNodeExtConfigDef.EXT_TRAIN_STATUS %>';					// 机车状态设置
    var EXT_DICT_TYPE = '<%= ZbglJobNodeExtConfigDef.EXT_DICT_TYPE %>';					// 整备范围数据字典
    
    
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	<%-- 图片虚拟路径 --%>
	var deleteIcon = imgpath + '/delete.gif';
	var addIcon = imgpath + '/add.gif';
	var imgpathx = imgpath + '/del.png';
	
	// 整备项目是否核实照片
	var DOCHECK = <%=ZbFwWi.DOCHECK%>;
	var DONOTCHECK = <%=ZbFwWi.DONOTCHECK%>;
	
	// 客货区分
	var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>";
</script>
<%--<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/baseselect/TrainWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/baseselect/TrainTypeWin.js"></script>
--%>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/i18n-lang-ZbFw.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layout/TableFormLayout.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jczl/PartsTypeByBuildSelect.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/vis/util/DateUtil.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 

<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/JobNodeStationDefSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/zbfw/ZbRepairLine.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/zbfw/ZbWorkStationSearcher.js"></script>

<!-- 查询机车维护信息win -->
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/TrianInfoWin.js"></script>
<!-- 中间表jswin -->
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbfwTrianCenterWin.js"></script>

<!-- 车辆选择 -->
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/TrainVehicleTypeWin.js"></script>

<script language="javascript" src="<%=ctx %>/jsp/zb/util/UndertakeTrainTypeWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbFwcx.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbFwWidi.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbFwWi.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbFwTaskOpt.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbglJobNodeExtConfigDef.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbglJobProcessNodeDef.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbglJobProcessNodeRelDef.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/ZbFw.js"></script>
</head>
<body>
</body>
</html>