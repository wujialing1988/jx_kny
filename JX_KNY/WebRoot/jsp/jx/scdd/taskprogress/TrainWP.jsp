<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<jsp:directive.page import="com.yunda.jx.scdd.taskprogress.ProgressdataConfig"/>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/RowEditorGrid.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
try{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车作业进度</title>
<script type="text/javascript"><%--
	//工序卡检验项目对象
	var objList = <%=JSONUtil.write(ProgressdataConfig.getInstance().getContent())%>
	var fields = [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'兑现单主键', dataIndex:'rdpIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true,
		editor:{  id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
				  hiddenName: "trainTypeIDX",
				  returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
				  displayField: "shortName", valueField: "typeID",
				  pageSize: 20, minListWidth: 200,   //isCx:'no',
				  editable:true,
				  listeners : {   
                        "collapse" : function() { 
                        	//重新加载车号下拉数据
                            var trainNo_comb = Ext.getCmp("trainNo_comb");   
                            trainNo_comb.reset();  
                            Ext.getCmp("trainNo_comb").clearValue(); 
                            trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
                            trainNo_comb.cascadeStore();
                        }   
                    }}
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor: { xtype:'hidden' }
	},{
		header:'车号', dataIndex:'trainNo',
		editor:{ id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
				  hiddenName: "trainNo",
				  displayField: "trainNo", valueField: "trainNo",
				  pageSize: 20, minListWidth: 200, 
				  editable:true}
	},{
		header:'配属局', dataIndex:'bShortName',editor:{  maxLength:50 }
	},{
		header:'配属段', dataIndex:'dShortName', editor:{  maxLength:50 }
	},{
		header:'委修段', dataIndex:'delegateDShortname', editor:{  maxLength:50 }
	}];
	for( var i = 0; i < objList.length; i++) { //循环创建grid列头信息
		var object = objList[ i ];
		fields.push({header: object.name, dataIndex: object.key,width:60,xtype: object.dataType, editor:{ id: object.key}});
	}
	fields.push({header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000}});
--%>
	var rdp_status_nullfy = '<%=com.yunda.jx.jxgc.producttaskmanage.entity.TrainEnforcePlanRdp.STATUS_NULLIFY%>';//终止状态
</script>
<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/taskprogress/TrainEnforcePlanRdpSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/taskprogress/TrainWPDetail.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/scdd/taskprogress/TrainWP.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>