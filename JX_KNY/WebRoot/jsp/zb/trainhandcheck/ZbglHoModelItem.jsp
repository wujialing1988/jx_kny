<%@include file="/frame/jspf/header.jspf" %>  
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车交接项模板—交接项</title>
<script language="javascript">
	var objList = [];
	var statusFields = [];
	var searchStatusFields = [];
	var cfg = {
	        url: ctx + "/zbglHoModelItem!findStatusByDicTypeID.action",	        
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entity != null) {
	            	objList = result.entity;	            	
					for (var i = 0; i < objList.length; i++) {
					    var field = objList[ i ];
					    var editor = {};  //定义状态项
					    editor.id = "statusId" + i;
					    editor.xtype = "checkbox";
					    editor.name = "handOverItemStatus"; //定义状态项名称规则
					    editor.boxLabel = field.dictname;
					    editor.inputValue  = field.dictname ;
					    editor.width = 100 ;
					    statusFields.push(editor);  
					    editor = {};
					    editor.id = "searchStatusId" + i;
					    editor.xtype = "checkbox";
					    editor.name = "handOverItemStatus"; //定义状态项名称规则
					    editor.boxLabel = field.dictname;
					    editor.inputValue  = field.dictname ;
					    editor.width = 100 ; 
					    searchStatusFields.push(editor);     
					}    
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
</script>
<script language="javascript" src="<%=ctx %>/jsp/zb/trainhandcheck/ZbglHoModelItemResult.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/zb/trainhandcheck/ZbglHoModelItem.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>