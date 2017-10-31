/**
 * 机车作业进度项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainWPDetail');                       //定义命名空间
TrainWPDetail.labelWidth = 120;
TrainWPDetail.trainWPIDX = "";
TrainWPDetail.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainWPDetail!findPageProgressList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainWPDetail!saveTrainWPDetail.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainWPDetail!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad : false,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业进度主键', dataIndex:'trainWPIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'进度项代码', dataIndex:'progressCode', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'进度项名称', dataIndex:'progressName'
	},{
		header:'进度项值', dataIndex:'progressValue'
	},{
		header:'数据来源', dataIndex:'dataSource', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'数据类型', dataIndex:'dataType', hidden:true, editor: { xtype:'hidden' }
	}],
	tbar:[],
    afterShowEditWin: function(record, rowIndex){
    	var record = TrainWPDetail.grid.store.getAt(rowIndex);
    	var dataType = record.get("dataType");
    	if(dataType == "timecolumn"){
    		Ext.getCmp("type_0").hide();
	        Ext.getCmp("type_1").show();
	        Ext.getCmp("type_1").setValue(record.get("progressValue"));
	        Ext.getCmp("type_2").hide();
	        Ext.getCmp("type_3").hide();
    	}else if(dataType == "numberfield"){
    		Ext.getCmp("type_0").hide();
	        Ext.getCmp("type_1").hide();
	        Ext.getCmp("type_2").show();
	        Ext.getCmp("type_2").setValue(record.get("progressValue"));
	        Ext.getCmp("type_3").hide();
    	}else if(dataType == "datecolumn"){
    		Ext.getCmp("type_0").hide();
	        Ext.getCmp("type_1").hide();
	        Ext.getCmp("type_2").hide();
	        Ext.getCmp("type_3").show();
	        Ext.getCmp("type_3").setValue(record.get("progressValue"));
    	}
    },
    beforeSaveFn: function(data){ 
    	var dataType = data.dataType;
    	if(dataType == "textfield"){
    		if(data["type_0"] == ""){
    			 MyExt.Msg.alert("进度项值不能为空！");
       			 return;
    		}
    		data.progressValue = data["type_0"];
    	}else if(dataType == "timecolumn"){
    		if(data["type_1"] == ""){
    			 MyExt.Msg.alert("进度项值不能为空！");
       			 return;
    		}
    		data.progressValue = data["type_1"];
    	}else if(dataType == "numberfield"){
    		if(data["type_2"] == ""){
    			 MyExt.Msg.alert("进度项值不能为空！");
       			 return;
    		}
    		data.progressValue = data["type_2"];
    	}else if(dataType == "datecolumn"){
    		if(data["type_3"] == ""){
    			 MyExt.Msg.alert("进度项值不能为空！");
       			 return;
    		}
    		data.progressValue = data["type_3"];
    	}
        for(var i in data){
        	if(i.indexOf("_") > 0) delete data[i]
        }
        data.trainWPIDX = TrainWPDetail.trainWPIDX;
    	return true; 
    },
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
        TrainWPDetail.grid.saveWin.hide();
    }
});
//信息表单
TrainWPDetail.grid.saveForm = new Ext.form.FormPanel({
    layout: "form", 	border: false, 		style: "padding:10px", labelWidth: TrainWPDetail.labelWidth,
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
    items: [{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [
    	{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            	 columnWidth: 1, 
            items: [
				{ name: "progressName", fieldLabel: "进度项名称",  maxLength: 50,readOnly:true, 
						style:"background:#CEF;color:#00F;"},
				{id:"type_0", fieldLabel: "进度项值",  maxLength: 50, width:TrainWPDetail.labelWidth},
				{id:"type_1", fieldLabel: "进度项值",  maxLength: 50,xtype: "my97date", hidden : true,format: "Y-m-d H:i", width:130 },
				{id:"type_2", fieldLabel: "进度项值",  maxLength: 50,xtype: "numberfield", hidden : true, width:TrainWPDetail.labelWidth },
				{id:"type_3", fieldLabel: "进度项值",  maxLength: 50,xtype: "my97date", hidden : true, width:TrainWPDetail.labelWidth },
				{id:"idx",xtype: "hidden", name: "idx"},
				{id:"数据类型",xtype: "hidden", name: "dataType"},
				{id:"作业进度主键",xtype: "hidden", name: "trainWPIDX"},
				{id:"作业进度项代码",xtype: "hidden", name: "progressCode"}
            ]
    	}
    ]
}]
});
TrainWPDetail.grid.createSaveWin();
TrainWPDetail.grid.saveWin.modal = true;
TrainWPDetail.grid.store.on("beforeload", function(){
	this.baseParams.trainWPIDX = TrainWPDetail.trainWPIDX;
});
});