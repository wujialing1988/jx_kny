<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/jsp/jx/include/EdoProject.jspf" %>
<%@page import="com.yunda.jx.jxgc.tpmanage.entity.FaultTicket" %>
<%
try{
	String idx = request.getParameter("idx");	// 获取检修记录单idx
//  	if (null == idx) idx = "158ADF91891F408589BD66812A9378B8";	// *开发测试默认值parts
  	//  if (null == idx) idx = "EC846544F2784FE2BB41630A44AAADE4";	// *开发测试默认值train
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检修记录单扫描查看</title>

<link href="<%=ctx%>/jsp/jx/pjwz/query/css/PartsRdpSearch.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
	#id_jx_record_card_detail {
		height:100%;
		overflow: scroll;
	}
	#id_jx_record_card_detail table {
		width: 100%;
		border-right:1px solid rgb(208,208,208);
		border-bottom:1px solid rgb(208,208,208);
	}
	#id_jx_record_card_detail table thead tr td {
		background:url(<%=ctx%>/jsp/jx/pjwz/query/css/images/table_thead_bg.gif) repeat;
		/*text-align:center;*/
		padding: 2px 2px 2px 5px;
		height:23px;
	}
	#id_jx_record_card_detail table tr td {
		font-size: 12px;
		padding: 2px 2px 2px 5px;
		height:23px;
		border-left:1px solid rgb(208,208,208);
		border-top:1px solid rgb(208,208,208);
	}
	#id_jx_record_card_detail table table {
	padding: 0px 0px 0px 0px;
		width: 100%;
		height:100%;
		border: none;
	}
	

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


	#record_card_detail {
		height:100%;
		overflow: scroll;
	}
	#record_card_detail table {
		width: 100%;
		border-right:1px solid rgb(208,208,208);
		border-bottom:1px solid rgb(208,208,208);
	}
	#record_card_detail table thead tr td {
		background:url(<%=ctx%>/jsp/jx/pjwz/query/css/images/table_thead_bg.gif) repeat;
		/*text-align:center;*/
		padding: 2px 2px 2px 5px;
		height:23px;
	}
	#record_card_detail table tr td {
		font-size: 12px;
		padding: 2px 2px 2px 5px;
		height:23px;
		border-left:1px solid rgb(208,208,208);
		border-top:1px solid rgb(208,208,208);
	}
	
	#record_card_detail table table {
	padding: 0px 0px 0px 0px;
		width: 100%;
		height:100%;
		border: none;
	}
</style>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ux/layouts/RowLayout.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpNoticeDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpNoticeNew.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsDismantleHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsInstallHis.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpExpendMatQuery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpRecordCardNew.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpRecord.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/integrateQuery/PartsRdpDetail.js"></script> <!-- pj检修记录卡展示 -->

<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/turnover/PartsZzjhQuery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workhis/workplan/TrainWorkTicketDetail.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/workhis/workplan/TrainWorkTicket.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkCardNew.js"></script> <!-- 机车检修记录卡展示 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/PartsFixRegisterHis.js"></script> <!-- 上车配件JS --> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/PartsUnloadRegisterHis.js"></script> <!-- 下车配件JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/QualityControlResultHis.js"></script> <!-- 质量检查JS --> 
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanWorkTaskHis.js"></script> <!-- 机车作业任务记录JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanDetectResultHis.js"></script> <!-- 检测结果记录JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanGanttHis.js"></script> <!-- 机车检修计划甘特图JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWordCardView_CommHis.js"></script> <!-- 作业工单显示界面信息JS -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanWorkCard.js"></script> <!-- 作业工单查询 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanCommHis.js"></script> <!-- 机车检修作业计划详细信息展示 -->

<script language="javascript" src="CreateShowTable.js"></script> <!-- table展示 -->
<script language="javascript" src="RecordCardNew.js"></script> <!-- 检修记录卡展示 -->
<script language="javascript" src="RecordScanQuery.js"></script> 
<script type="text/javascript">
	// 机车检修提票状态
	var STATUS_DRAFT = '<%=FaultTicket.STATUS_DRAFT%>';//未处理
	var STATUS_OPEN = '<%=FaultTicket.STATUS_OPEN%>'; //处理中
	var STATUS_OVER = '<%=FaultTicket.STATUS_OVER%>';//已处理
	
   <%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx%>/frame/resources/images/toolbar';
	<%-- 打印图标虚拟路径 --%>
	var printerImg = imgpath + '/printer.png';
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