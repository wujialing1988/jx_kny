<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>作业卡处理</title>
		<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
		<script type="text/javascript">	
	
			var workCardOpen = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard.STATUS_OPEN %>';<%-- 开放的作业卡 --%>
			var workCardHandling = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard.STATUS_HANDLING %>';<%-- 处理中的作业卡 --%>
			var workCardHanded = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard.STATUS_HANDLED %>';<%-- 处理完成的作业卡 --%>
			var task_finished = "<%=com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask.STATUS_HANDLED %>";<%-- 作业任务完成 --%>
			var install_reason = "修程";<%--安装原因--%> 
			function showtip(o){
				var dom = o || Ext.getBody();		
				if(!o && !win.handlerWin.hidden){
					dom = win.handlerWin.getEl();
				}
				processTips = new Ext.LoadMask(dom,{
					msg:"正在处理你的操作，请稍后...",
					removeMask:true
				});
				processTips.show();
			}
			
			function hidetip(){
				processTips.hide();
			}
			var btns = ["btnReceive","btnOngoing","btnComplete"];
			
			
			var uninst = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard.WORK_SEQ_TYPE_UNINST %>';
			var install = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard.WORK_SEQ_TYPE_INSTALL %>';
		
			//组成状态-启用
			var buildUpStatus = "<%=com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType.USE_STATUS%>";
		
			var teamName = '${ sessionScope.treamName }';
			var empId = '${sessionScope.emp.empid}';
			var dxStatus = 'DX';<%-- 下车配件为待修状态 --%>
			var isNotBlank_yes = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_YES %>";//是否必填 0必填
			var isNotBlank_no = "<%=com.yunda.jx.jxgc.repairrequirement.entity.DetectItem.ISNOTBLANK_NO %>";//是否必填 1非必填
		</script>
		<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/InsertCharField.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/repairbase/BuildUpTypeTree.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/tecProcessmanage/TecProcessNodeSelect.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/PartsAccountSelect.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/TeamEmployeeSelect.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/WorkTask/WorkTask_Worker.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/WorkTask/WorkTask_Form.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/WorkTask/WorkTask_Item.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/WorkTask/WorkTask_Win.js"></script>
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/WorkTask/WorkTask_Handler.js"></script><!-- 待处理任务 -->
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/WorkTask/WorkTask_Handled.js"></script><!-- 已处理任务 -->
		<script type="text/javascript" src="<%=ctx %>/jsp/jx/jxgc/WorkTask/WorkTask.js"></script><!-- 主界面 -->
		
	</head>
	<body>
	</body>
</html>