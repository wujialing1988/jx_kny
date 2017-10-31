<jsp:directive.page import="com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件分类</title>
<script>
	var status_use = '<%=PartsType.STATUS_USE%>';   //启用
	var isHeight = <%=PartsType.IS_HEIGHT%>;   //是高价
	var noHeight = <%=PartsType.NO_HEIGHT%>;   //是高价
	var trainData = [] ; 
	Ext.Ajax.request({
        url: ctx + '/trainTypeToParts!getTrains.action',
        params:[],
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.list != null) {
            	trainData = result.list[0];
            }
        },
        failure: function(response, options){
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
	
</script>	
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/exchangeboundary/PartsTypeForTranSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/exchangeboundary/PartsTypeSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/pjwz/partbase/exchangeboundary/PartsClassNew.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>