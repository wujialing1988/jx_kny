<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>值班员信息确认</title>
<script language="javascript">	
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jxgc/TaskConfirm/CheckFault.js"></script>
<script type="text/javascript">
var empId = '${sessionScope.emp.empid}';
var empName = '${sessionScope.emp.empname}';

//故障状态
var STATUS_DRAFT = <%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_DRAFT%>;
var STATUS_OPEN = <%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_OPEN%>;
var STATUS_OVER = <%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_OVER%>;
var STATUS_ZLX = <%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_ZLX%>;
var STATUS_LWFX = <%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_LWFX%>;
var STATUS_CANCEL = <%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_CANCEL%>;

var STATUS_DRAFT_CH = '<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_DRAFT_CH%>';
var STATUS_OPEN_CH = '<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_OPEN_CH%>';
var STATUS_OVER_CH = '<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_OVER_CH%>';
var STATUS_ZLX_CH = '<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_ZLX_CH%>';
var STATUS_LWFX_CH = '<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_LWFX_CH%>';
var STATUS_CANCEL_CH = '<%=com.yunda.jx.jczl.faultmanager.entity.FaultNotice.STATUS_CANCEL_CH%>';

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