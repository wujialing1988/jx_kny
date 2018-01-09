<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page import="com.yunda.jx.pjjx.partsrdp.entity.PartsRdp" %> 
<%@page import="com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus" %>
<%@page import="com.yunda.jx.pjwz.partsmanage.entity.PartsAccount" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件综合查询</title>

<link href="<%=ctx%>/jsp/jx/pjwz/query/css/PartsRdpSearch.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
	#id_record_card_detail {
		height:100%;
		overflow: scroll;
	}
	#id_record_card_detail table {
		width: 100%;
		border-right:1px solid rgb(208,208,208);
		border-bottom:1px solid rgb(208,208,208);
	}
	#id_record_card_detail table thead tr td {
		background:url(<%=ctx%>/jsp/jx/pjwz/query/css/images/table_thead_bg.gif) repeat;
		/*text-align:center;*/
		padding: 2px 2px 2px 5px;
		height:23px;
	}
	#id_record_card_detail table tr td {
		font-size: 12px;
		padding: 2px 2px 2px 5px;
		height:23px;
		border-left:1px solid rgb(208,208,208);
		border-top:1px solid rgb(208,208,208);
	}
	
	#id_record_card_detail table table {
	padding: 0px 0px 0px 0px;
		width: 100%;
		height:100%;
		border: none;
	}
</style>
<script type="text/javascript">

    /* 配件状态 */
      var PARTS_STATUS_ZC = '<%= PartsAccount.PARTS_STATUS_ZC%>';	//在册状态
      var PARTS_STATUS_DX = '<%= PartsAccount.PARTS_STATUS_DX%>';	//待修
      var PARTS_STATUS_WWX = '<%= PartsAccount.PARTS_STATUS_WWX%>';	//委外修
      var PARTS_STATUS_ZX = '<%= PartsAccount.PARTS_STATUS_ZX%>';	//自修
      var PARTS_STATUS_DBF = '<%= PartsAccount.PARTS_STATUS_DBF%>';	//待报废
      var PARTS_STATUS_DJY = '<%= PartsAccount.PARTS_STATUS_DJY%>';	//待校验
      var PARTS_STATUS_LH = '<%= PartsAccount.PARTS_STATUS_LH%>';	//良好
  
    
    var PARTS_RDP_STATUS_WKF = '<%=IPartsRdpStatus.CONST_STR_STATUS_WKF%>';			// 作业工单状态 - 未开放
	var PARTS_RDP_STATUS_DLQ = '<%=IPartsRdpStatus.CONST_STR_STATUS_DLQ%>';			// 作业工单状态 - 待领取
	var PARTS_RDP_STATUS_DCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_DCL%>';			// 作业工单状态 - 待处理
	var PARTS_RDP_STATUS_YCL = '<%=IPartsRdpStatus.CONST_STR_STATUS_YCL%>';			// 作业工单状态 - 已处理
	<%-- 已处理状态包含以下状态 --%>
	var PARTS_RDP_STATUS_XJ = '<%=IPartsRdpStatus.CONST_STR_STATUS_XJ%>';			// 作业工单状态 - 修竣
	var PARTS_RDP_STATUS_ZLJYZ = '<%=IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ%>';		// 作业工单状态 - 质量检验中
    
    <%-- 回退标识 --%>
	var IS_BACK_YES = '<%= IPartsRdpStatus.CONST_INT_IS_BACK_YES %>'					// 回退标识 - 是
	var IS_BACK_NO = '<%= IPartsRdpStatus.CONST_INT_IS_BACK_NO %>'					
	
    <%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx%>/frame/resources/images/toolbar';
	<%-- 打印图标虚拟路径 --%>
	var printerImg = imgpath + '/printer.png';
	
</script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>

<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsmanage/PartsAcountTurnOver.js"></script><!-- 配件周转历史记录 -->
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpNoticeDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpNoticeNew.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsDismantleHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsInstallHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpExpendMatQuery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpRecordCardNew.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpRecord.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsAccountOrRdp.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsAccount.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsIntegrateQuery.js"></script>
</head>

<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>
