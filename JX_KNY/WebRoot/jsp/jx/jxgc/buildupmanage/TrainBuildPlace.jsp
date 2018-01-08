<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType" %>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace" %>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车组成维护</title>
<script language="javascript">	
	var partsBuildUpTypeIdx = "";//根节点组成型号主键	
	var partsBuildUpTypeName = "";//根节点组成型号名称	
	var buildUpTypeIdx = "";//组成型号主键	
	var fixPlaceIdx = "";//安装位置idx		
	var parentIdx = "";//上级位置idx	
	//是否缺省可安装组成型号
	var isDefault = <%=FixBuildUpType.DEFAULT%>;
	var noDefault = <%=FixBuildUpType.NODEFAULT%>;
	var isDefaultMean = "<%=FixBuildUpType.DEFAULT_MEAN%>";
	var noDefaultMean = "<%=FixBuildUpType.NODEFAULT_MEAN%>";
	
	var nodeId = "";//点击节点id	
	var parentNodeId = "";//上级节点id	
	//组成状态
	var status_new = <%=BuildUpType.NEW_STATUS%>;
	var status_use = <%=BuildUpType.USE_STATUS%>;
	var status_nullify = <%=BuildUpType.NULLIFY_STATUS%>;
	//组成类型
	var type_train = <%=BuildUpType.TYPE_TRAIN%>;
	var type_parts = <%=BuildUpType.TYPE_PARTS%>;
	var type_virtual = <%=BuildUpType.TYPE_VIRTUAL%>;
	//是否为标准组成
	var isDefault_yes = <%=BuildUpType.ISDEFAULT_YES%>;
	var isDefault_no = <%=BuildUpType.ISDEFAULT_NO%>;
	//位置类型
	var structure_place = <%=BuildUpPlace.TYPE_STRUCTURE%>;
	var fix_place = <%=BuildUpPlace.TYPE_FIX%>;
	var virtual_place = <%=BuildUpPlace.TYPE_VIRTUAL%>;
	
	var trainTypeIDX = '';//车型主键	
	var partsTypeIDX = '';//配件主键	
	var type = '';//组成类型全局变量	
	var pageType = 1;//页面类型-1表示是机车组成维护	
	var placeFaultIDX = "";//故障现象主键
	//是否默认处理方法
	var method_default = <%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFaultMethod.ISDEFAULT%>;
	var method_nodefault = <%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFaultMethod.NODEFAULT%>;
	var image_path = '<%=ctx+BuildUpType.IMAGE_PATH%>';//图标路径
	var rootParentIdx = '<%=BuildUpType.PARENT_IDX%>';//车组成根节点位置ID
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/FixBuildUpType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/PlaceFaultMethod.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/PlaceFault.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/BuildUpPlace.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/TrainPlaceBuildUp.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/VirtualPlaceBuildUp.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/TrainBuildPlace.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>