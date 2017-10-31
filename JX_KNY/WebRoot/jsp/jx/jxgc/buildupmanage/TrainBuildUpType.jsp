<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType" %>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.FixPlace" %>
<%@page import="com.yunda.jx.jxgc.buildupmanage.entity.FixBuildUpType" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车组成维护(已作废)</title>
<script language="javascript">
	//组成型号主键
	var partsBuildUpTypeIdx = "";
	//组成型号名称
	var partsBuildUpTypeName = "";
	//安装位置idx
	var fixPlaceIdx = "";
	//组成状态
	var buildUpStatus = "<%=BuildUpType.USE_STATUS%>";
	//组成类型
	var type_train = "<%=BuildUpType.TYPE_TRAIN%>";
	var type_parts = "<%=BuildUpType.TYPE_PARTS%>";
	//状态
	var status_new = <%=BuildUpType.NEW_STATUS%>;
	var status_use = <%=BuildUpType.USE_STATUS%>;
	var status_nullify = <%=BuildUpType.NULLIFY_STATUS%>;
	//是否为缺省组成
	var isDefault_yes = <%=BuildUpType.ISDEFAULT_YES%>;
	var isDefault_no = <%=BuildUpType.ISDEFAULT_NO%>;
	//是否虚拟位置
	var isVirtual = <%=FixPlace.ISVIRTUAL%>;
	var noVirtual = <%=FixPlace.NOTVIRTUAL%>;
	//是否缺省可安装组成型号
	var isDefault = <%=FixBuildUpType.DEFAULT%>;
	var noDefault = <%=FixBuildUpType.NODEFAULT%>;
	var isDefaultMean = "<%=FixBuildUpType.DEFAULT_MEAN%>";
	var noDefaultMean = "<%=FixBuildUpType.NODEFAULT_MEAN%>"; 
	//点击节点id
	var nodeId = "ROOT_0";
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/BuildUpTypeTree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/UpdateTrainBuild.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/buildupmanage/TrainBuildUpType.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>