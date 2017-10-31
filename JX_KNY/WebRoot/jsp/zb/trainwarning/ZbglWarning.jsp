<%@include file="/frame/jspf/header.jspf" %> 
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@include file="/jsp/jx/include/MultiSelect.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@page import="com.yunda.zb.trainwarning.entity.ZbglWarning" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车检测预警</title>
<script language="javascript">
	//票活状态
	var STATUS_TODO = '<%=ZbglWarning.STATUS_TODO%>';
	var STATUS_RELEASE = '<%=ZbglWarning.STATUS_RELEASE%>';
	var STATUS_RELEASED = '<%=ZbglWarning.STATUS_RELEASED%>';
	var STATUS_NOTICE = '<%=ZbglWarning.STATUS_NOTICE%>';
	var STATUS_CANCEL = '<%=ZbglWarning.STATUS_CANCEL%>';
	
	var STATUS_TODO_CH = '<%=ZbglWarning.STATUS_TODO_CH%>';
	var STATUS_RELEASE_CH = '<%=ZbglWarning.STATUS_RELEASE_CH%>';
	var STATUS_RELEASED_CH = '<%=ZbglWarning.STATUS_RELEASED_CH%>';
	var STATUS_NOTICE_CH = '<%=ZbglWarning.STATUS_NOTICE_CH%>';
	var STATUS_CANCEL_CH = '<%=ZbglWarning.STATUS_CANCEL_CH%>';
	
	//提票
	var partsBuildUpTypeIdx = "";//组成型号主键	
	var partsBuildUpTypeName = "";//组成型号名称
	var trainNo = '';
	var trainTypeIDX = '';
	var trainTypeShortName = '';
	var dId = '';
	var dName = '';	
	var OTHERID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.OTHERID%>";//”其它“故障主键ID
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>	
</script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/tp/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/tp/ZbglTpWin.js"></script>
<script language="javascript" src="ZbglWarning.js"></script>
</head>
<body>
</body>
</html>