<%@include file="/frame/jspf/header.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/jx/pjwz/partbase/component/PartsManageDeptTree.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上车配件信息查询</title>
<script type="text/javascript">
	var PARTS_STATUS_YSC = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.PARTS_STATUS_YSC%>';  //配件状态--已上车
	var MANAGE_DEPT_TYPE_ORG = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.MANAGE_DEPT_TYPE_ORG%>';  //责任部门类型--机构
	var MANAGE_DEPT_TYPE_WH = '<%=com.yunda.jx.pjwz.partsmanage.entity.PartsAccount.MANAGE_DEPT_TYPE_WH%>';  //责任部门类型--库房
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsmanage/PartsAccountAboardSearch.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
</body>
</html>