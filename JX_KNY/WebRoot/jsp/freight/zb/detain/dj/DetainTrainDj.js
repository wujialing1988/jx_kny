/**
 * 扣车管理
 */
Ext.onReady(function(){
	
Ext.namespace('DetainTrainDj');                       //定义命名空间

//定义全局变量保存查询条件
DetainTrainDj.searchParam = {} ;
DetainTrainDj.labelWidth = 80;                        //表单中的标签名称宽度
DetainTrainDj.fieldWidth = 200;                       //表单中的标签宽度

DetainTrainDj.queryTimeout;	// 间隔查询

DetainTrainDj.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/detainTrain!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/detainTrain!applyDetainTrain.action',             //保存数据的请求URL
    singleSelect: true, 
    saveFormColNum:1,
    saveForm:DetainTrainDj.saveForm,
    tbar : ['add',{
    	text: i18n.VehicleDetainReg.deletebutt ,iconCls:"deleteIcon", handler: function(){
    		//判断是否选择了数据
    		var grid = DetainTrainDj.grid;
    		if(!$yd.isSelectedRecord(grid)) {
    			MyExt.Msg.alert(i18n.VehicleDetainReg.msg1);
    			return;
    		}
    		var ids = $yd.getSelectedIdx(grid);	
    		
    		var record = DetainTrainDj.grid.store.getById(ids[0]);
    		
    		var detainStatus = record.get("detainStatus");
    		if(detainStatus != '10'){
    			MyExt.Msg.alert(i18n.VehicleDetainReg.msg2);
    			return;			
    		}
    		
    		var cfg = {
    	        url: ctx + "/detainTrain!deleteDetain.action", 
    			params: {ids: ids},
    	        timeout: 600000,
    	        success: function(response, options){
    	            var result = Ext.util.JSON.decode(response.responseText);
    	            if (result.errMsg == null && result.success == true) {
    	                alertSuccess();
    	                DetainTrainDj.grid.store.load();
    	            } else {
    	                alertFail(result.errMsg);
    	            }
    	        },
    	        failure: function(response, options){
    	        	if(processTips) hidetip();
    		        Ext.Msg.alert(i18n.VehicleDetainReg.msg3, i18n.VehicleDetainReg.msg4 + "\n" + response.status + "\n" + response.responseText);
    		    }
    	    };
    	    Ext.Msg.confirm(i18n.VehicleDetainReg.msg3, i18n.VehicleDetainReg.msg5, function(btn){
    	        if(btn != 'yes')    return;
    	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    	    });
    		}
    	},'refresh','&nbsp;&nbsp;',
    {
    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:i18n.VehicleDetainReg.msg6, width:200,
    	listeners: {
    		keyup: function(filed, e) {
    			if (DetainTrainDj.queryTimeout) {
    				clearTimeout(DetainTrainDj.queryTimeout);
    			}
    			
    			DetainTrainDj.queryTimeout = setTimeout(function(){
					DetainTrainDj.grid.store.load();
    			}, 1000);
    		}
		}
    }],    
	fields: [
     	{
		header:i18n.VehicleDetainReg.trainTypeID, dataIndex:'trainTypeIdx',hidden:true,width: 120,editor: {
			fieldLabel: i18n.VehicleDetainReg.trainType,
			xtype: "Base_combo",
			hiddenName: "trainTypeIdx",
		    business: 'trainVehicleType',
		    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
            fields:['idx','typeName','typeCode'],
            returnField:[{widgetId:"trainTypeShortNameId",propertyName:"typeCode"}],
            queryParams: {'vehicleType':vehicleType},// 表示客货类型
		    displayField: "typeCode", valueField: "idx",
            pageSize: 20, minListWidth: 200,
            width: DetainTrainDj.fieldWidth,
            allowBlank: false,
            editable:false,	
			listeners : {   
                "collapse" : function() {   
                   //重新加载车号数据
                	var trainNo_combo = Ext.getCmp('trainNo');
                    trainNo_combo.reset();
                    trainNo_combo.clearValue();
                    trainNo_combo.queryParams.vehicleType = vehicleType ;
                    trainNo_combo.queryParams.trainTypeIDX = this.getValue();
                    trainNo_combo.cascadeStore();
                }   
            }
		}
	},
     	{
		header:i18n.VehicleDetainReg.trainType, dataIndex:'trainTypeCode',width: 120,editor: {
			id:'trainTypeShortNameId',xtype:'hidden',name: 'trainTypeCode'
		}
	},
     	{
		header:i18n.VehicleDetainReg.trainTypeName, dataIndex:'trainTypeName',hidden:true,width: 120,editor: {
			xtype:'hidden'
		}
	},
     	{
		header:i18n.VehicleDetainReg.trainNum, dataIndex:'trainNo',width: 120,editor: {
			id:'trainNo',
			fieldLabel: i18n.VehicleDetainReg.trainNum,
			width:DetainTrainDj.fieldWidth,
			xtype: "Base_combo",
			name:'trainNo',
			hiddenName: "trainNo",
		    business: 'jczlTrain',
		    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
            fields:['trainTypeIDX','trainNo','trainTypeShortName'],
            queryParams: {'vehicleType':vehicleType},// 表示客货类型
		    displayField: "trainNo", valueField: "trainNo",
            pageSize: 20, minListWidth: 200,
            allowBlank: false,
            disabled:false,
            editable:true	
		}
	}, {
		header:i18n.VehicleDetainReg.detainTypeCode, dataIndex:'detainTypeCode',width: 120,hidden:true,editor: {
			id:'detainTypeCode',xtype:'hidden',name: 'detainTypeCode'
		}
	},
     	{
		header:i18n.VehicleDetainReg.detainType, dataIndex:'detainTypeName',width: 120,editor: {
			id:'detainTypeName_combo',
			xtype: 'EosDictEntry_combo',
			hiddenName: 'detainTypeName',
			dicttypeid:'DETAIN_TRAIN_TYPE',
			allowBlank:false,
			displayField:'dictname',valueField:'dictname',
			hasEmpty:"false",
			returnField: [{widgetId:"detainTypeCode",propertyName:"dictid"}]
		}
	},{
		header:i18n.VehicleDetainReg.registrantId, dataIndex:'proposerIdx',hidden:true,width: 120,editor: {
			xtype:'hidden'
		}
	},
     	{
		header:i18n.VehicleDetainReg.registrantName, dataIndex:'proposerName',width: 120,editor: {xtype:'hidden'}
	},
       {
		header:i18n.VehicleDetainReg.registrantDate, dataIndex:'proposerDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{hidden:true, xtype:'my97date',format: 'Y-m-d H:i' }
	},
     	{
		header:i18n.VehicleDetainReg.detainReason, dataIndex:'detainReason',width: 120,editor: {
       		xtype: 'textarea',
       		fieldLabel: i18n.VehicleDetainReg.detainReason,
       		allowBlank:false,
       		maxLength:500,
       		height: 60,
       		name: 'detainReason'
		}
	}, {
		header:i18n.VehicleDetainReg.status, dataIndex:'detainStatus',width: 120,renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if(value == "10"){
						return  '<div style="background:#d2d6de;color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">未检修</div>';
					}else if(value == "20"){
						return  '<div style="background:#f39c12;color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">检修中</div>';
					}else if(value == "30"){
						return  '<div style="background:#00a65a;color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">已检修</div>';
					}
				},editor: {xtype:'hidden'}
	},{
		header:i18n.VehicleDetainReg.stationID, dataIndex:'siteID',width: 120,hidden:true ,editor: {
			xtype:'hidden'
		}
	},
     	{
		header:i18n.VehicleDetainReg.stationName, dataIndex:'siteName',width: 120,hidden:true , editor: {
			xtype:'hidden'
		}
	},{
		header:i18n.VehicleDetainReg.vehicleType, dataIndex:'vehicleType',hidden:true, editor: { xtype:"hidden",value:vehicleType }
	},{
		header:i18n.VehicleDetainReg.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		DetainTrainDj.searchParam = searchParam ;
        DetainTrainDj.grid.store.load();
	},
	beforeShowEditWin: function(record, rowIndex){
		return false ;
	}
	
});
// 页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:DetainTrainDj.grid });
	
// 查询前添加过滤条件
DetainTrainDj.grid.store.on('beforeload' , function(){
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('query_input').getValue())){
			whereList.push({ propName: 'trainNo', propValue: Ext.getCmp('query_input').getValue(), compare: Condition.EQ, stringLike: true });
		}
		whereList.push({ propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ, stringLike: false});		
		whereList.push({ propName: 'detainStatus', propValue:"10", compare: Condition.EQ, stringLike: false});
		whereList.push({ propName: 'proposerIdx', propValue:empid, compare: Condition.EQ, stringLike: false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

});