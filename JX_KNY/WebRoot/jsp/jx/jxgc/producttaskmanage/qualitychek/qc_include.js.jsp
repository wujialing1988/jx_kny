<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.util.JSONUtil"%>
<%@page import="com.yunda.jx.jxgc.repairrequirement.manager.WorkSeqManager"%>
<%@page import="com.yunda.jxpz.utils.SystemConfigUtil"%>
<%@page import="com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask"%>

var task_finished = "<%=WorkTask.STATUS_FINISHED %>";
var workTaskComplete = "<%=WorkTask.STATUS_FINISHED %>";


//工序卡检验项目对象
var objList = <%=JSONUtil.write(WorkSeqManager.getWorkSeqQualityControl("null"))%>


var fields = [{
	header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
},{
	header:'作业卡主键', dataIndex:'workCardIDX',hidden:true, editor:{  maxLength:50 }
},{
	header:'工步主键', dataIndex:'workStepIDX',hidden:true, editor:{  maxLength:50 }
},{
	header:'作业任务编码', dataIndex:'workTaskCode',hidden:true, editor:{  maxLength:50 }
},{
	header:'检测/修项目', dataIndex:'workTaskName', editor:{  maxLength:50 }
},{
	header:'作业类型', dataIndex:'workTaskType',hidden:true, editor:{  maxLength:50 }
},{
	header:'检修内容', dataIndex:'repairContent',hidden:true, editor:{  maxLength:500 }
},{
	header:'技术要求或标准规定', dataIndex:'repairStandard',width:200, editor:{ maxLength:1000 }
},{
	header:'检修方法', dataIndex:'repairMethod',hidden:true, editor:{  maxLength:500 }
},{
	header:'检修结果主键', dataIndex:'repairResultIdx',hidden:true, editor:{  maxLength:50 }
},{
	header:'检修代码', dataIndex:'resultCode',hidden:true, editor:{  maxLength:500 }
},{
	header:'作业结果', dataIndex:'resultName', editor:{  maxLength:50 }
},{
	header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
},{
	header:'状态', dataIndex:'status',hidden:true, editor:{  maxLength:64 }
}];
	
	var quaFields = [];
	for( var i = 0; i < objList.length; i++) { //循环创建grid列头信息
		var object = objList[ i ];
		quaFields.push({header: object.checkItemName, dataIndex: object.checkItemCode,width:60, editor:{ },sortable: false});
	}

