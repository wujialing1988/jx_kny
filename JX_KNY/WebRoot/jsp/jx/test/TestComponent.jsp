<%@include file="/frame/jspf/header.jspf" %>
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.yhgl.entity.AcOperator" %>
<%@page import="com.yunda.base.filter.LoginPurviewCheckFilter" %>
<%@page import="com.yunda.frame.yhgl.manager.EosDictEntrySelectManager" %>
<%try{ 
	AcOperator acOperator = (AcOperator) session.getAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME);
	System.out.println(acOperator.getUserid());
	System.out.println(acOperator.getOperatorname());
	
	
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript">
		var getNodeStatus = function(status){
			if(status == node_status_new) return "未处理";
			if(status == node_status_handling) return "处理中";
			if(status == node_status_handled) return "已处理";
			return "";
		}
	</script>
	<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>	
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/OmEmpBaseCombo.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/employee/OmEmployeeMultSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/jxgc/component/TrainTypeTreeSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/scdd/FactorTrainSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsTypeWidgetSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/PartsBuildSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/BuildUpTypeTree.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsTypeAndQuotaSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationWin.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/basejs/OmOrganizationTreeWin.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BureauSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/GyjcFactorySelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/PartsAccountSelect.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseComboTree.js"></script>
	<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BaseMultyComboTree.js"></script>
    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/ProfessionalType.js"></script>
    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>
    <script language="javascript" src="<%=ctx%>/jsp/jx/test/TestComponent.js"></script>
  </head>
  
  <body>   
	<table>
		<tr>					
		</tr>
	</table>	
  </body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>
