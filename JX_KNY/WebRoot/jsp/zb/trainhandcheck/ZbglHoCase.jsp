<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@include file="/jsp/jx/include/MultiSelect.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %>
<%@page import="com.yunda.zb.common.ZbConstants" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车迎检操作</title>
<script type="text/javascript">
	var operatorid = '${sessionScope.users.operatorid}';
	var partsBuildUpTypeIdx = "";//组成型号主键	
	var partsBuildUpTypeName = "";//组成型号名称
	var trainNo = '';
	var trainTypeIDX = '';
	var trainTypeShortName = '';
	var dId = '';
	var dName = '';	
	var warningDesc = '';
	var OTHERID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.OTHERID%>";//”其它“故障主键ID
	var CUSTOMID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.CUSTOMID%>";//*自定义故障现象主键（-1111）
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>	
	
	//提票状态
	var STATUS_INIT = '<%=ZbglTp.STATUS_INIT%>';
	var STATUS_DRAFT = '<%=ZbglTp.STATUS_DRAFT%>';
	var STATUS_OPEN = '<%=ZbglTp.STATUS_OPEN%>';
	var STATUS_OVER = '<%=ZbglTp.STATUS_OVER%>';
	var STATUS_CHECK = '<%=ZbglTp.STATUS_CHECK%>';
	
	var STATUS_INIT_CH = '<%=ZbglTp.STATUS_INIT_CH%>';	
	var STATUS_DRAFT_CH = '<%=ZbglTp.STATUS_DRAFT_CH%>';
	var STATUS_OPEN_CH = '<%=ZbglTp.STATUS_OPEN_CH%>';
	var STATUS_OVER_CH = '<%=ZbglTp.STATUS_OVER_CH%>';
	var STATUS_CHECK_CH = '<%=ZbglTp.STATUS_CHECK_CH%>';
	
	var tpStatus = [[STATUS_INIT,STATUS_INIT_CH],[STATUS_DRAFT,STATUS_DRAFT_CH]];
	
	var UPLOADPATH_TP = '<%=ZbConstants.UPLOADPATH_TP%>';
	
	/**检修类型：碎修*/
    var REPAIRCLASS_SX = '<%=ZbConstants.REPAIRCLASS_SX%>';
    /**检修类型：临修*/
    var REPAIRCLASS_lX = '<%=ZbConstants.REPAIRCLASS_LX%>';
	
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%= ctx %>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/trainhandcheck/HandOverOperateForm.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/trainhandcheck/ZbglHoModelItemResultSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/zb/trainhandcheck/HandOverModelList.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>  <!-- 临碎修提票 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>  <!-- 临碎修提票 -->
<script language="javascript" src="<%=ctx %>/jsp/zb/tp/BuildUpTypeTree.js"></script>  <!-- 临碎修提票 -->
<script language="javascript" src="<%=ctx %>/jsp/zb/tp/ZbglTpWin.js"></script> <!-- 临碎修提票 -->
<script language="javascript" src="<%=ctx %>/jsp/zb/trainhandcheck/ZbglHoCase.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>