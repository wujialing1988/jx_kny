<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@page import="com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>质量检验</title>
		<script type="text/javascript">
			var CONST_INT_CHECK_WAY_CJ = <%=JCQCItemDefine.CONST_INT_CHECK_WAY_CJ%>;
			var CONST_INT_CHECK_WAY_BJ = <%=JCQCItemDefine.CONST_INT_CHECK_WAY_BJ%>;			
			var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
			var img = imgpath + '/pjgl.gif';<%-- 图片虚拟路径 --%>
			
			var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>"; // 10 货车 20 客车   
			
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
		<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/qualitychek/qc_include.js.jsp"></script>
		<script type="text/javascript" src="<%=ctx%>/jsp/jx/jxgc/gongdanwh/TrainGDWorker.js"></script> <!-- 机作人员记录JS -->
		<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/gongdanwh/QualityControlResult.js"></script> <!-- 质量检查JS -->
		<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/gongdanwh/TrainGDWorkTask.js"></script> <!-- 机作业项记录JS -->
		<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/gongdanwh/TrainGDDetectResult.js"></script> <!-- 检测结果记录JS -->
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jczl/qualitycontrol/TrainWordCardView_Comm.js"></script> <!-- 作业工单显示界面信息JS -->
		<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle_biz.js"></script><!-- 质量检查列表按钮操作方法JS -->
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle_base.js"></script><!-- 质量检查列表双击方法JS -->
	    <script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle_async.js"></script><!-- 质量检查（抽检）列表JS -->
	    <script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle.js"></script><!-- 质量检查（必检）列表JS -->
	</head>
	  
	<body>
	</body>
</html>
