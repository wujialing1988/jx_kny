/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
function appendx(str1, str2){
	if(str1 && str2)
		return str1 + "|" + str2;
	else if(str1)
		return str1;
	else if(str2)
		return str2;
	return "";
}
Ext.namespace('activityInfo');                       //定义命名空间
activityInfo.personGrid = new Ext.yunda.Grid({
		loadURL: ctx + '/wfprocessinst!findWFParticipant.action',                 //装载列表数据的请求URL
	    tbar:[],
	    fields: [{
			header:'IDX', dataIndex:'id', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'签名人', dataIndex:'disposePerson', editor: { }
		},{
			header:'签名时间', dataIndex:'disposeTime',xtype:'datecolumn', format: 'Y-m-d H:i', editor: { }
		}]
	});
//移除侦听器
activityInfo.personGrid.un('rowdblclick', activityInfo.personGrid.toEditFn, activityInfo.personGrid);
activityInfo.personGrid.store.on('beforeload',function(){
	this.baseParams.processinstId = processInstID;
	this.baseParams.activityinstid = activityinstid;
//	this.baseParams.currentState = "'4','10'";   //当前状态
})
jQuery.ajax({
	url: ctx + "/wfprocessinst!getActivityInfo.action",
	data:{processInstId: processInstID,activityInstId: activityinstid},
	dataType:"json",
	type:"post",
	success:function(data){
		var record=new Ext.data.Record();
		for(var i in data){
			record.set(i,data[i]);
		}
		activityInfo.baseForm.getForm().loadRecord(record);
		if(record.get("starttime")!=null){
			Ext.getCmp("starttime").setValue(new Date(record.get("starttime")).format("Y-m-d H:i")) ;
		}
		if(record.get("endtime")!=null){
			Ext.getCmp("endtime").setValue(new Date(record.get("endtime")).format("Y-m-d H:i")) ;
		}
	}
});
var action = '';
var trainX = '/trainEnforcePlanRdp';
var partsX = '/partsEnforcePlanRdp';
if(typeof(isparts) == "undefined" || !isparts){
	action = trainX;
}else{	
	action = partsX;
}
jQuery.ajax({
	url: ctx + action + "!getBeseInfo.action",
	data:{rdpId: rdpId},
	dataType:"json",
	type:"post",
	success:function(data){
		var record = new Ext.data.Record();
		for(var i in data){
			if(i == 'transInTime' || i == 'planTrainTime' || i == 'planBeginTime' || i == 'planEndTime'){					
				record.set(i, new Date(data[i]).format("Y-m-d H:i"));					
				continue;
			}
			record.set(i,data[i]);
		}
//		activityInfo.baseForm.getForm().loadRecord(record);
		if(action == partsX){
			record.set('repairClassRepairTime', appendx(record.get("repairClassName"), record.get("repairtimeName")));
			record.set('trainTypeTrainNo', appendx(record.get("unloadTrainTypeShortName"), record.get("unloadTrainNo")));
			activityInfo.titleForm_parts.getForm().loadRecord(record);
			activityInfo.titleForm.hide();
			activityInfo.titleForm_parts.show();
		}else{
			record.set('trainTypeTrainNo', appendx(record.get("trainTypeShortName"), record.get("trainNo")));
			record.set('repairClassRepairTime', appendx(record.get("repairClassName"), record.get("repairtimeName")));
			activityInfo.titleForm.getForm().loadRecord(record);
			activityInfo.titleForm_parts.hide();
			activityInfo.titleForm.show();
		}
	}
});
//页面自适应布局
var viewport = new Ext.Viewport({ 
	layout:'border', 
	items:[{
			region:"north",
			layout:"fit",
			height:100,
			split:true,
			maxSize:500,
			minSize:250,
			items:[activityInfo.titleForm, activityInfo.titleForm_parts]}
				,{
					title:"已签名人",
					region:"center",
					layout:"fit",
					items:[activityInfo.personGrid]
				}
	]
});
});	
