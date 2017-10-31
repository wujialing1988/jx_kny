<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %>

<%@include file="/jsp/jx/include/MultiSelect.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.EntityUtil" %>
<%@page import="com.yunda.zb.tp.entity.ZbglTpTrackRdp" %>
<%@page import="com.yunda.zb.tp.entity.ZbglTpTrackRdpRecord" %>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %>
<%@page import="com.yunda.zb.common.ZbConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提票跟踪单查询</title>
<script language="javascript">
    
    //buildTree需要使用的属性
    var partsBuildUpTypeIdx = "";//组成型号主键	
	var partsBuildUpTypeName = "";//组成型号名称
	var trainNo = '';
	var trainTypeIDX = '';
	var trainTypeShortName = '';
	var trackIDX = '';
	var jt6IDX = '';
	var dId = '';
	var dName = '';	
	var warningDesc = '';
	var OTHERID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.OTHERID%>";//”其它“故障主键ID
	var CUSTOMID = "<%=com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault.CUSTOMID%>";//*自定义故障现象主键（-1111）
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';<%-- 图片虚拟目录 --%>	

	//结束跟踪
	var TRACKOVER = '<%=ZbglTpTrackRdp.TRACKOVER%>';
	//正在跟踪
	var TRACKING = '<%=ZbglTpTrackRdp.TRACKING%>';
	
	//已处理
	var ALREADY = <%=ZbglTpTrackRdpRecord.ALREADY%>;
	//处理中
	var UNREADY = <%=ZbglTpTrackRdpRecord.UNREADY%>;
	
	//本次跟踪结束

	//本次跟踪开始
	
	
	//jt6提票状态中文
	var STATUS_INIT_CH = '<%=ZbglTp.STATUS_INIT_CH%>';	
	var STATUS_DRAFT_CH = '<%=ZbglTp.STATUS_DRAFT_CH%>';
	var STATUS_OPEN_CH = '<%=ZbglTp.STATUS_OPEN_CH%>';
	var STATUS_OVER_CH = '<%=ZbglTp.STATUS_OVER_CH%>';
	var STATUS_CHECK_CH = '<%=ZbglTp.STATUS_CHECK_CH%>';
	
		//jt6提票状态
	var STATUS_INIT = '<%=ZbglTp.STATUS_INIT%>';	
	var STATUS_DRAFT = '<%=ZbglTp.STATUS_DRAFT%>';
	var STATUS_OPEN = '<%=ZbglTp.STATUS_OPEN%>';
	var STATUS_OVER = '<%=ZbglTp.STATUS_OVER%>';
	var STATUS_CHECK = '<%=ZbglTp.STATUS_CHECK%>';
	
	/**检修类型：碎修*/
    var REPAIRCLASS_SX = '<%=ZbConstants.REPAIRCLASS_SX%>';
    /**检修类型：临修*/
    var REPAIRCLASS_lX = '<%=ZbConstants.REPAIRCLASS_LX%>';
    
    //跟踪过
	var ISTRACKED_YES = <%=ZbglTp.ISTRACKED_YES%>;
	//未跟踪
	var ISTRACKED_NO = <%=ZbglTp.ISTRACKED_NO%>;
	
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script><!--FIXME 调用检修业务类  -->

<script language="javascript" src="<%=ctx %>/jsp/zb/tp/BuildUpTypeTree.js"></script>
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="ZbglTpTrackTp.js"></script>
	
<script language="javascript" src="ZbglTpShowWin.js"></script>
<script language="javascript" src="ZbglTpInfoFormWin.js"></script>
<script language="javascript" src="ZbglTpTrackThisTime.js"></script>
<script language="javascript" src="ZbglTpTrack.js"></script>
</head>
<body>
</body>
</html>