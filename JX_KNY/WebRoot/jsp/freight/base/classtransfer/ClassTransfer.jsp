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
<script type="text/javascript">
	var empId = '${sessionScope.emp.empid}';
	var empName = '${sessionScope.emp.empname}';
	
	var dynamicColumuns = 
[{
		header:'当前值班员id', dataIndex:'empid', hidden:true, width: 120,editor: {xtype:"hidden"}
	},
     	{
		header:'当前值班员', dataIndex:'empname',width: 120,editor: {}
	},
     	{
		header:'交接值班员id', dataIndex:'transferEmpid',hidden:true,width: 120,editor: {xtype:"hidden"},searcher: { hidden: true }
	},
     	{
		header:'交接值班员', dataIndex:'transferName',width: 120,editor: {}
	},
     	{
		header:'当前班次编码', dataIndex:'classNo',hidden:true,width: 120,editor: {xtype:"hidden"},searcher: { hidden: true }
	},
     	{
		header:'当前班次', dataIndex:'className',width: 120,editor: {}
	},
     	{
		header:'交接班次编码', dataIndex:'transferClassNo',hidden:true,width: 120,editor: {xtype:"hidden"},searcher: { hidden: true }
	},
     	{
		header:'交接班次名称', dataIndex:'transferClassName',width: 120,editor: {}
	},
		{
		header:'交接项目', dataIndex:'details', hidden:true, width: 120,editor: {}
	}, {
		header:'交接时间', dataIndex:'transferDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' },searcher: { hidden: true }
	},
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}];
	
		
	$.ajax({
		type:'GET',
		url: ctx + "/zbglHoModelItem!findItemList.action",
		async:false,
		data: {"parentName":"肯尼亚货车运用"},
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
			MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
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