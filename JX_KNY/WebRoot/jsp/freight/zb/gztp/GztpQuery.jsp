<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.freight.zb.gztp.entity.Gztp" %>
<%@page import="com.yunda.freight.zb.plan.entity.ZbglRdpPlan" %>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	/** 票活状态：初始化 */
	var STATUS_INIT = '<%=Gztp.STATUS_INIT %>';
	var STATUS_INIT_CH = '<%=Gztp.STATUS_INIT_CH %>';
	
	/** 票活状态：已处理 */
	var STATUS_OVER = '<%=Gztp.STATUS_OVER %>';
	var STATUS_OVER_CH = '<%=Gztp.STATUS_OVER_CH %>';
	
	/** 票活状态：已检验 */
    var STATUS_CHECKED = '<%=Gztp.STATUS_CHECKED %>';
    var STATUS_CHECKED_CH = '<%=Gztp.STATUS_CHECKED_CH %>';
	
	/** 处理类型：登记 */
	var HANDLE_TYPE_REG = '<%=Gztp.HANDLE_TYPE_REG %>';
	var HANDLE_TYPE_REG_CH = '<%=Gztp.HANDLE_TYPE_REG_CH %>';
	
	/** 处理类型：上报 */
	var HANDLE_TYPE_REP = '<%=Gztp.HANDLE_TYPE_REP %>';
	var HANDLE_TYPE_REP_CH = '<%=Gztp.HANDLE_TYPE_REP_CH %>';
	
	// 列检计划状态
	var STATUS_UNRELEASED = '<%= ZbglRdpPlan.STATUS_UNRELEASED %>'		// 未启动
	var STATUS_HANDLING = '<%= ZbglRdpPlan.STATUS_HANDLING %>'			// 已启动
	var STATUS_INTERRUPT = '<%= ZbglRdpPlan.STATUS_INTERRUPT %>'		// 中断
	var STATUS_DELAY = '<%= ZbglRdpPlan.STATUS_DELAY %>'				// 延期
	var STATUS_HANDLED = '<%= ZbglRdpPlan.STATUS_HANDLED %>'			// 已完工

	var siteId = '<%= EntityUtil.findSysSiteId(null) %>';           // 站场ID
	
	/** 子系统编码：00货车列检，01客车列检，10货车计划修，11客车计划修 */
	var subSysCode = '<%=request.getParameter("subSysCode") %>';
	/** 车辆类型：10货车，20客车 */
	var vehicleType = '<%=request.getParameter("vehicleType") %>';
	
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	<%-- 图片虚拟路径 --%>
	var deleteIcon = imgpath + '/delete.gif';
	var addIcon = imgpath + '/add.gif';	
	
</script>
<title>列检_故障提票查看</title>

<script language="javascript" src="<%=ctx%>/jsp/freight/zb/gztp/MatTypeUseListQuery.js"></script> <!-- 物料列表 -->
<script language="javascript" src="<%=ctx%>/jsp/freight/zb/gztp/GztpTicketQuery.js"></script> <!-- 故障提票列表 -->
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>