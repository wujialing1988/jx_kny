<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=8" http-equiv="X-UA-Compatible">
<title>机车入段(台位图)</title>
<style type="text/css">
	//表单控件隐藏时css样式，用于解决隐藏时有时控件无法显示的bug
	.my-disabled {
		color:gray;cursor:default;
	}
	.my-disabled .x-form-trigger-over {
	  	background-position:0 0 !important;
	  	border-bottom: 1px solid #B5B8C8;
	}
	.my-disabled .x-form-trigger-click{
 	 	background-position:0 0 !important;
  		border-bottom: 1px solid #B5B8C8;
	}
</style>

<script type="text/javascript">
	var TRAINTOGO_ZB = '<%=TrainAccessAccount.TRAINTOGO_ZB%>';		
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/twt/workplanmanage/TrainAccessAccountIn.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>