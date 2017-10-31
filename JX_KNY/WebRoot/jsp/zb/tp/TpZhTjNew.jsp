<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/Attachment.jspf" %>
<%@include file="/jsp/jx/include/MultiSelect.jspf" %>
<%@include file="/frame/jspf/TreeFilter.jspf" %> 
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.zb.tp.entity.ZbglTp" %>
<%@page import="com.yunda.zb.common.ZbConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/ExportFile.js"></script>
	<script language="javascript" src="TpZhTjNew.js"></script>
	<script language="javascript">
		/**检修类型：碎修*/
	    var REPAIRCLASS_SX = '<%=ZbConstants.REPAIRCLASS_SX%>';
	    /**检修类型：临修*/
	    var REPAIRCLASS_lX = '<%=ZbConstants.REPAIRCLASS_LX%>';
	</script>
  <body>
  </body>
</html>
