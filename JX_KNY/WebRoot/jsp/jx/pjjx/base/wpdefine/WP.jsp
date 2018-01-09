<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/OrderManager.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjjx.base.wpdefine.entity.WPNode"%>
<%@page import="com.yunda.jx.pjjx.base.wpdefine.entity.WPNodeSeq"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检修需求维护</title>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-TruckFaultReg.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WpNodeStationDef.js"></script>  <!-- 作业工位  -->
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WpNodeChooseWorkStation.js"></script>  <!-- 作业工位选择界面  -->
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WPNode_tree.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/EquipForWPSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/EquipForWP.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/PartsTypeForWPSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/PartsTypeForWP.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/TecForWPSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/TecForWP.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/RecordForWPSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/RecordForWP.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/TecCardForWPNodeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/TecCardForWPNode.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/RecordCardForWPNodeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/RecordCardForWPNode.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/MatInforList.js"></script> <!-- 物料信息 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WPNodeSeq.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WPNode.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WPMatSearch.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WPMat.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjjx/base/wpdefine/WP.js"></script>
<script type="text/javascript">
	var IS_LEAF_YES = '<%= WPNode.CONST_INT_IS_LEAF_YES%>';
	var IS_LEAF_NO = '<%= WPNode.CONST_INT_IS_LEAF_NO%>';
	
	var SEQ_CLASS_FS = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_FS %>'					// 完成-开始（编码）
	var SEQ_CLASS_FS_NAME = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_FS_NAME %>'			// 完成-开始（名称）
	var SEQ_CLASS_SS = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_SS %>'					// 开始-开始（编码）
	var SEQ_CLASS_SS_NAME = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_SS_NAME %>'			// 开始-开始（名称）
	var SEQ_CLASS_SF = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_SF %>'					// 开始-完成（编码）
	var SEQ_CLASS_SF_NAME = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_SF_NAME %>'			// 开始-完成（名称）
	var SEQ_CLASS_FF = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_FF %>'					// 完成-完成（编码）
	var SEQ_CLASS_FF_NAME = '<%= WPNodeSeq.CONST_STR_SEQ_CLASS_FF_NAME %>'			// 完成-完成（名称）
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	<%-- 图片虚拟路径 --%>
	var deleteIcon = imgpath + '/delete.gif';
	var addIcon = imgpath + '/add.gif';
</script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>