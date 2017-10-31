<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType" %>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.BuildUpPlace" %>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车组成查看</title>
<script language="javascript">
	//根节点组成型号主键
	var partsBuildUpTypeIdx = "";
	//根节点组成型号名称
	var partsBuildUpTypeName = "";
	//组成型号主键
	var buildUpTypeIdx = "";
	//安装位置idx
	var fixPlaceIdx = "";	
	//上级位置idx
	var parentIdx = "";
	
	//是否缺省可安装组成型号
	var isDefault = <%=FixBuildUpType.DEFAULT%>;
	var noDefault = <%=FixBuildUpType.NODEFAULT%>;
	var isDefaultMean = "<%=FixBuildUpType.DEFAULT_MEAN%>";
	var noDefaultMean = "<%=FixBuildUpType.NODEFAULT_MEAN%>";
	//点击节点id
	var nodeId = "";
	//上级节点id
	var parentNodeId = "";
	
	//状态
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
	//车型主键
	var trainTypeIDX = '';
	//配件主键
	var partsTypeIDX = '';
	//组成类型
	var type = '';
	//页面类型-1表示是机车组成维护
	var pageType = 1;
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/query/FixBuildUpType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/query/PlaceFault.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/query/BuildUpPlace.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/query/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/query/TrainPlaceBuildUp.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/query/VirtualPlaceBuildUp.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/query/TrainBuildPlace.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>