<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
		
		<% String ctx = request.getContextPath(); %>
		
		<link rel="stylesheet" href="<%=ctx %>/jsp/sb/check/statistic/fullcalendar/css/bootstrap.css">
		<link rel="stylesheet" href="<%=ctx %>/jsp/sb/check/statistic/fullcalendar/css/fullcalendar.css">
		<link rel="stylesheet" href="<%=ctx %>/jsp/sb/check/statistic/fullcalendar/css/fullcalendar.print.css" media='print'>
		<link rel="stylesheet" href="<%=ctx %>/jsp/sb/check/statistic/fullcalendar/css/style.css">
		
		<script type="text/javascript">
			var ctx = '<%=ctx %>';
			var equipmentCode = '<%=request.getParameter("equipmentCode") %>';
			var queryDate = '<%=request.getParameter("queryDate") %>';
		</script>
		
		<script src="<%=ctx %>/jsp/sb/check/statistic/fullcalendar/js/jquery.min.js" type="text/javascript"></script>
		<script src="<%=ctx %>/jsp/sb/check/statistic/fullcalendar/js/fullcalendar.min.js"></script>
		<script src="<%=ctx %>/jsp/sb/check/statistic/fullcalendar/js/custom.js"></script>
	</head>
	<body>
	<!--默认是最小化的形式，需要改成最大就把container样式换成container-fluid-->
    <div class="container">
                    <div class="box-content box-nomargin">
                        <div class="calendar"></div>
                    </div>
    </div>
	</body>
</html>