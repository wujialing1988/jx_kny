<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/frame/jspf/header.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
	<head>
		<title>流程状态</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="<%=ctx%>/snaker/styles/css/style.css" type="text/css" media="all" />
		<link rel="stylesheet" href="<%=ctx%>/snaker/styles/css/snaker.css" type="text/css" media="all" />
		<script src="<%=ctx%>/snaker/styles/js/raphael-min.js" type="text/javascript"></script>
		<script src="<%=ctx%>/snaker/styles/js/jquery-ui-1.8.4.custom/js/jquery.min.js" type="text/javascript"></script>
		<script src="<%=ctx%>/snaker/styles/js/jquery-ui-1.8.4.custom/js/jquery-ui.min.js" type="text/javascript"></script>
		<script src="<%=ctx%>/snaker/styles/js/snaker/snaker.designer.js" type="text/javascript"></script>
		<script src="<%=ctx%>/snaker/styles/js/snaker/snaker.model.js" type="text/javascript"></script>
		<script src="<%=ctx%>/snaker/styles/js/snaker/snaker.editors.js" type="text/javascript"></script>

<script type="text/javascript">
	function display(process, state) {
		/** view*/
		$('#snakerflow').snakerflow($.extend(true,{
			basePath : ctx + "/snaker/styles/js/snaker/",
            ctxPath : ctx,
            orderId : '<%=request.getParameter("orderId")%>',
			restore : eval("(" + process + ")"),
			editable : false
			},eval("(" + state + ")")
		));
	}
</script>
</head>
	<body>
		<div id="snakerflow" style="width:100%;" />
		
		<script type="text/javascript">
		$.ajax({
				type:'GET',
				url: ctx + "/snakerApprovalRecord!flowDiagram.action",
				data:'processId=<%=request.getParameter("processId")%>&orderId=<%=request.getParameter("orderId")%>',
				async: false,
				globle:false,
				error: function(){
					alert('数据处理错误！');
					return false;
				},
				success: function(data){
					display(data.process, data.state);
				}
			});
		</script>
	</body>
</html>
