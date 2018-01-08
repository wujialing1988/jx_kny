<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车零部件</title>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jcbm/jcpjzd/jcpjzdBuildPartsType.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partcode/Dictionary.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partcode/LodopBuildCodeFuncs.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partcode/qrcode.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jcbm/jcpjzd/JcpjzdBuildCodeFun.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jcbm/jcpjzd/JcpjzdBuildCode.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jcbm/jcpjzd/JcpjzdBuild.js"></script>
<script type="text/javascript">
	var status_add = '0';   //新增
	var status_use = '1';   //启用
	var status_invalid = '2';  //作废
	var yes = '<%=Constants.YES%>';    //是
	var no = '<%=Constants.NO%>';     //否
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