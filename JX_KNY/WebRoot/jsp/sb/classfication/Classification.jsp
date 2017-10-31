<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.sb.classfication.entity.Classification" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>设备类别</title>
		<script type="text/javascript">
			/** 叶子节点：否【0】 */
			var IS_LEAF_NO = '<%= Classification.IS_LEAF_NO %>';
			/** 叶子节点：是【1】 */
			var IS_LEAF_YES = '<%= Classification.IS_LEAF_YES %>';
		</script>
		<script type="text/javascript" src="Classification.js"></script>
	</head>
	<body>
	</body>
</html>