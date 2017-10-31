/**
 * 机车使用别变更台账 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainUseChange');                       //定义命名空间
TrainUseChange.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainUseChange!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainUseChange!saveOrUpdateTrainUse.action',             //保存数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"trainId", xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', editor:{id:"trainTypeIDX_chg", xtype:'hidden', maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeName', editor:{id:"trainTypeName_chg",  maxLength:20 ,disabled:true }
	},{
		header:'车号', dataIndex:'trainNo', editor:{id:"trainNo_chg",  maxLength:50 ,disabled:true}
	},{
		header:'原使用别', dataIndex:'oldTrainUseID', editor:{id:"oldTrainUseID_chg",disabled:true}
	},{
		header:'原使用别', dataIndex:'oldTrainUseName', editor:{id:"oldTrainUseName_chg", xtype:'hidden',  maxLength:50 }
	},{
		header:'新使用别ID', dataIndex:'newTrainUseID', editor:{
			id:"newTrainUseID_combo_Id", allowBlank:false,
			xtype: 'Base_combo',fieldLabel: "新使用别",
			hiddenName: 'newTrainUseID',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',			
			fields: ["useID","useName"],displayField:'useName',valueField:'useID',
			returnField:[{widgetId:"newTrainUseNameId",propertyName:"useName"}]
		}
	},{
		header:'新使用别', dataIndex:'newTrainUseName', editor:{id:"newTrainUseNameId", xtype:'hidden',  maxLength:50 }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'变更时间', dataIndex:'changeTime', xtype:'datecolumn', editor:{xtype:'hidden' }
	}],
	afterSaveSuccessFn: function(result, response, options){
        JczlTrain.grid.store.reload();
        alertSuccess();
        TrainUseChange.grid.saveWin.hide();
    },
    beforeSaveFn: function(data){ 
    	if(data.oldTrainUseName == data.newTrainUseName){
    		MyExt.Msg.alert("【原使用别】和【新使用别】不能相同！");
    		return false;
    	}
    	return true; 
    }
});
//创建调出窗口
TrainUseChange.grid.createSaveWin();
});