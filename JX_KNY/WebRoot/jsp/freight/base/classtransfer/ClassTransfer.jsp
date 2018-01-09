<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=ctx %>/frame/resources/jquery/jquery.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/i18n/<%=browserLang %>/freight/i18n-lang-ClassTransfer.js"></script>
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	
	var dynamicColumuns = 
[{
		header:i18n.ClassTransfer.empid, dataIndex:'empid', hidden:true, width: 120,editor: {xtype:"hidden"}
	},
     	{
		header:i18n.ClassTransfer.empname, dataIndex:'empname',width: 120,editor: {}
	},
     	{
		header:i18n.ClassTransfer.transferEmpid, dataIndex:'transferEmpid',hidden:true,width: 120,editor: {xtype:"hidden"},searcher: { hidden: true }
	},
     	{
		header:i18n.ClassTransfer.transferName, dataIndex:'transferName',width: 120,editor: {}
	},
     	{
		header:i18n.ClassTransfer.classNo, dataIndex:'classNo',hidden:true,width: 120,editor: {xtype:"hidden"},searcher: { hidden: true }
	},
     	{
		header:i18n.ClassTransfer.className, dataIndex:'className',width: 120,editor: {}
	},
     	{
		header:i18n.ClassTransfer.transferClassNo, dataIndex:'transferClassNo',hidden:true,width: 120,editor: {xtype:"hidden"},searcher: { hidden: true }
	},
     	{
		header:i18n.ClassTransfer.transferClassName, dataIndex:'transferClassName',width: 120,editor: {}
	},
		{
		header:i18n.ClassTransfer.details, dataIndex:'details', hidden:true, width: 120,editor: {}
	}, {
		header:i18n.ClassTransfer.transferDate, dataIndex:'transferDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' },searcher: { hidden: true }
	},
		{
		header:i18n.ClassTransfer.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}];
	
		
	$.ajax({
		type:'GET',
		url: ctx + "/zbglHoModelItem!findItemList.action",
		async:false,
		data: {"parentName":i18n.ClassTransfer.knyTruckUse},
		success: function(response, options){
			var result = Ext.util.JSON.decode(response);
			if(result.list){
				var list = result.list ;
				for (var i = 0; i < list.length; i++) {
					var obj = list[i] ;
					dynamicColumuns.push({
						header:obj.handOverItemName, dataIndex:'item'+i,width: 120,editor: {}
					});
				}
			}
		},
		failure: function(response, options){
			MyExt.Msg.alert(i18n.ClassTransfer.alertRemaindMes + "\n" + response.status + "\n" + response.responseText);
		}
	});
	
</script>
<title>班次交接</title>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmEmployeeSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/freight/base/classtransfer/ClassTransfer.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>