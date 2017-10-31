Ext.onReady(function(){
//定义命名空间
Ext.namespace("PartsZzjhsc");
PartsZzjhsc.trainno = ""; //车号
PartsZzjhsc.traintypeIdx = "";//车型主键
PartsZzjhsc.trainshortname = "";//车型名称
PartsZzjhsc.repairClassName = "";//修程名称
PartsZzjhsc.repairtimeName = "";//修次名称
PartsZzjhsc.workPlanIdx = "";//作业计划兑现单ID

PartsZzjhsc.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/offPartList!pageQuery.action',                 //装载列表数据的请求URL
    border:false,
    viewConfig: null,
    tbar:null,
	fields: [
		{header:"idx",dataIndex:"idx",hidden:true},
		{header:"配件名称",width:200,dataIndex:"partsName"},
		{header:"位置",width:120,dataIndex:"wzmc"},
		{header:"计划下车日期",width:180,dataIndex:"planOffTime",xtype:'datecolumn', format: "Y-m-d"},
		{header:"计划上车日期",width:200,dataIndex:"planOnTime",xtype:'datecolumn',format: "Y-m-d"}]
	});
		
PartsZzjhsc.grid.store.on('beforeload', function() {
	var whereList = [];
	whereList.push({
		propName : 'workPlanIDX',
		propValue : zajcIdx,
		compare : Condition.EQ,
		stringLike : false
	});
	if (!Ext.isEmpty(zajcIdx)) {
		sql = "idx not in (select t.off_parts_list_idx from pjwz_parts_zzjh t where t.work_plan_idx = '" + zajcIdx + "' and t.off_parts_list_idx is not null)" ;
		whereList.push({compare: Condition.SQL, sql: sql});
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
	
PartsZzjhsc.titleForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.5, 
            items: [
        		{ fieldLabel:"机车号", name:"trainTypeTrainNo", width:80, style:"border:none;background:none;", readOnly:true, id:"train_Id"}
            ]
        }, {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.5, 
            items: [
            	{ fieldLabel:"修程修次", name:"repairClassRepairTime", id:"xcxc", width:150, style:"border:none;background:none;", readOnly:true}
                
            ]
        }]
    }]
});
	

//生成周转计划
PartsZzjhsc.win = new Ext.Window({
    title: "周转计划编制", maximizable:false, layout: "fit", width: 800, height: 500, plain: true,
		closeAction: "hide", modal: true, buttonAlign:"center",
		items:[{
			xtype: "panel", layout: "border", frame:true,
			items:[{
	            region: 'north', layout: "fit", width: 800, height: 30, plain: true,collapsible:false,
	            bodyBorder: false,items:[PartsZzjhsc.titleForm]
	        },{
	            region : 'center',  bodyBorder: false, layout:"fit",
	            items : [PartsZzjhsc.grid]}
	            ]
		}],
    buttons: [{
        id: "searchBtn1", text: "生成周转计划", iconCls: "saveIcon",
        handler: function(){  
        	var repairClassName, repairtimeName;
        	if(PartsZzjhWin.repairClassAndTime != ""){
        		repairClassName = PartsZzjhWin.repairClassAndTime.split("|")[0];
        		repairtimeName = PartsZzjhWin.repairClassAndTime.split("|")[1];
        	}
        	 if (!$yd.isSelectedRecord(PartsZzjhsc.grid)) return;
			 Ext.Msg.confirm("提示  ", "确定生成周转计划？  ", function(btn) {
				if (btn != 'yes') return;
				if (self.loadMask) self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + "/partsZzjh!saveOffPartList.action",
					params: {
						ids: $yd.getSelectedIdx( PartsZzjhsc.grid, PartsZzjhsc.grid.storeId),
						repairClassName : repairClassName,
						repairtimeName : repairtimeName,
						workPlanIdx :PartsZzjhWin.workPlanIdx
					},
					success: function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						var result = Ext.util.JSON.decode(response.responseText);
						if (result.success == true) {
							PartsZzjhWin.grid.store.reload();
							PartsZzjhsc.win.hide();
							MyExt.Msg.alert("操作成功！");							
						} else {
							alertFail(result.errMsg);
						}
					},
					failure: function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
			});
        }       
    }, {
        text:"关闭", iconCls:"closeIcon",
        handler:function(){	PartsZzjhsc.win.hide()}
    }]
});

PartsZzjhsc.toEditFn = function(){
	if(PartsZzjhWin.workPlanIdx != ""){
		Ext.getCmp("train_Id").setValue(PartsZzjhWin.trainTypeAndNo);
		Ext.getCmp("xcxc").setValue(PartsZzjhWin.repairClassAndTime);
//	}else{
//		Ext.getCmp("train_Id").setValue(PartsZzjhsc.trainshortname + "|" +PartsZzjhsc.trainno);
//		Ext.getCmp("xcxc").setValue(PartsZzjhsc.repairClassName + "|" + PartsZzjhsc.repairtimeName);
	}
	PartsZzjhsc.win.show();
	PartsZzjhsc.grid.store.load();
}
});