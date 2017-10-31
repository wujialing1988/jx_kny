<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=ctx%>/frame/yhgl/css/welcome-page.css" rel="stylesheet" type="text/css" />
<title>配件条码二维码生成</title>	
	<script type="text/javascript">
		var num = 1;
		var imp = [];
	</script>
	<script type="text/javascript" src="jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="Dictionary.js"></script>
	<script type="text/javascript" src="LodopFuncs.js"></script>
	<script type="text/javascript" src="qrcode.js"></script>
	<script type="text/javascript" src="PartsCode.js"></script>	
	<script type="text/javascript" src="PartCodeGenerate.js"></script>	
</head>
<body>
	<%--<div id="content" class="greetingAndLinkTitleText">
        <form>
            每个编码生成数量：
            <input id="Radio1" type="radio" name="createNum" checked="true" value="1" /> 1
            <input id="Radio2" type="radio" style="margin-left: 25px;" name="createNum" value="2" /> 2
            <input id="Radio3" type="radio" style="margin-left: 25px;" name="createNum" value="3" /> 3
            <input id="Radio4" type="radio" style="margin-left: 25px;" name="createNum" value="4" /> 4
            <input id="Radio5" type="radio" style="margin-left: 25px;" name="createNum" value="5" /> 5
        </form>
        <span style="margin-left:64px;">编码数量：</span>
        <input id="Number" type="text" style="margin-top:10px;width:100px;"/>
        <input id="QRcode" type="button" style="margin-left: 100px;" value="生成二维码" />
        <input id="print" type="button" value="打印" />
    </div>
    <div id="qrcode" style="width:100px; height:0px; margin-top:15px;"></div>
    <div id="qrcodelist" style="width:100px; height:100px; margin-top:15px; margin-left: 50px;"></div>
    --%></body>
</html>