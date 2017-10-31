<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>承修车型</title>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/baseselect/TrainWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/baseselect/TrainTypeWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/baseselect/XcWin.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/undertakemanage/UndertakeTrain.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/undertakemanage/UndertakeTrainTypeRC.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/undertakemanage/UndertakeTrainType.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>