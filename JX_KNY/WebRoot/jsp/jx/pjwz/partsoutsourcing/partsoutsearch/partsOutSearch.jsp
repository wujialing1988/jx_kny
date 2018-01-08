<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity.PartsOutsourcing" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>委外配件查询</title>
    <script type="text/javascript">
       var parts_out="<%=PartsOutsourcing.PARTS_STATUS_WW%>";
       var parts_back="<%=PartsOutsourcing.PARTS_STATUS_YFH%>";
    </script>
    <script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partbase/component/PartsExtendNoWin.js"></script>
    <script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
    <script language="javascript" src="<%=ctx%>/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingFactoryCombo.js"></script>
	<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partsoutsourcing/partsoutsearch/partsOutSearch.js"></script>
  </head>
  
  <body>
   
  </body>
</html>
