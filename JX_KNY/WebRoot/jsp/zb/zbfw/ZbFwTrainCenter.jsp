<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@include file="/jsp/jx/include/MultiSelect.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.zbfw.entity.ZbfwTrainCenter" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/zb/zbfw/editZbFwWin.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/zb/zbfw/ZbFwTrainCenter.js"></script>
	<script language="javascript">
		/**本段字符串替代*/
	    var BD = '<%=ZbfwTrainCenter.BD%>';
	    /**非本段字符串替代*/
	    var FBD = '<%=ZbfwTrainCenter.FBD%>';
	</script>
  <body>
  </body>
</html>
