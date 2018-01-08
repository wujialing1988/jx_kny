<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard"%>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask"%>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.entity.Worker"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工长派工</title>
<script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
<script language="javascript">	
	<%-- 状态-初始化 --%>
    var status_new = "<%=WorkCard.STATUS_NEW%>";
    <%-- 状态-已开放 --%>
    var status_open = "<%=WorkCard.STATUS_OPEN%>";
    <%-- 状态-处理中 --%>
    var status_handling = "<%=WorkCard.STATUS_HANDLING%>";
    <%-- 状态-已处理 --%>
    var status_handled = "<%=WorkCard.STATUS_HANDLED%>";
    <%-- 状态-终止 --%>
    var status_finished = "<%=WorkCard.STATUS_TERMINATED%>";
    
    /* 状态-待领取 */
    var task_status_waitingforget = "<%=WorkTask.STATUS_WAITINGFORGET %>";
    /* 状态-待处理 */
    var task_status_waitingforhandle = "<%=WorkTask.STATUS_WAITINGFORHANDLE %>";
    /* 状态-已处理 */
    var task_status_handled = "<%=WorkTask.STATUS_HANDLED %>";
    /* 状态-终止 */
    var task_status_finished = "<%=WorkTask.STATUS_FINISHED %>";
    

	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>
	var img = imgpath + '/pjgl.gif';<%-- 图片虚拟路径 --%>
		
	var team = {};
	jQuery.ajax({
		url: ctx + "/omOrganizationSelect!getCurrentTeam.action",
		type:"post",
		dataType:"json",
		success:function(data){
			if(!data.team){
				Ext.Msg.alert("系统提示","你没有所属的班组，请联系管理员！");
				return;
			}
			team = data.team;
		}
	});

	var processTips;

	function showtip(){
		var el;
		if(WorkStationEmp.selectWin.isVisible()){
			el = WorkStationEmp.selectWin.getEl();
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
	var isFullDipatch = 'false';//是否全部派工，默认为false不是全部派工，为true是全部派工
	var isClickForeman = false;//是否已点击“已派工”tab， 默认为false未点击，为true已点击
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/TeamEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/RepairActivityMultyCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/dispatch/WorkStationEmpSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/dispatch/foreman.js"></script>
</head>
<body>

</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>