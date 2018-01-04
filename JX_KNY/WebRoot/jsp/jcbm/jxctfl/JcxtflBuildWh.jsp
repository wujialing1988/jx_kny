<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车系统分类维护</title>

<script language="javascript">
    /** 车辆类型：10货车，20客车 */
    var vehicleType = '<%=request.getParameter("vehicleType") %>';
    vehicleType = null; // 当不需要过滤车辆类型
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/i18n-lang-ZbFw.js"></script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-JcxtflBuildWh.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/baseselect/TrainWin.js"></script>
<%--<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/baseselect/TrainTypeWin.js"></script>--%>
<!-- 车辆选择 -->
<script language="javascript" src="<%=ctx %>/jsp/zb/zbfw/TrainVehicleTypeWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jcbm/jxctfl/JcxtflBuildWh.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/JobProcessDefSelect.js"></script>

</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>