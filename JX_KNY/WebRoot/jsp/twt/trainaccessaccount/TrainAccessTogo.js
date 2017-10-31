/**
 * 机车入段去向 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainAccessTogo');                       //定义命名空间
TrainAccessTogo.labelWidth = 90;
TrainAccessTogo.fieldWidth = 150;
TrainAccessTogo.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
TrainAccessTogo.saveWin = null;
TrainAccessTogo.afterSaveSuccessFn = function(result, response, options) {
	//台位图调用和web页面分别调用此页面时需重写此方法
}
TrainAccessTogo.afterSaveFailFn = function(result, response, options) {
	//台位图调用和web页面分别调用此页面时需重写此方法
}
TrainAccessTogo.closeWin = function() {
	if (TrainAccessTogo.saveWin != null)
		TrainAccessTogo.saveWin.hide();
	else {
		parent.window.opener = null;
		parent.window.open('','_self');
		parent.window.close();
	}
}
TrainAccessTogo.saveFn = function() {
    var form = TrainAccessTogo.saveForm.getForm(); 
    if (!form.isValid()) return;
    form.findField("trainNo").enable();
	form.findField("trainTypeShortName").enable();
    var data = form.getValues();   
    if (Ext.isEmpty(data.trainNo) && !Ext.isEmpty(Ext.get("trainNo_Togo_comb").dom.value)) {
		data.trainNo = Ext.get("trainNo_Togo_comb").dom.value;
	}
    
    if(TrainAccessTogo.loadMask)   TrainAccessTogo.loadMask.show();
    var cfg = {
        scope: this, url: ctx + '/trainAccessAccount!confirmTogo.action', jsonData: data,
        success: function(response, options){
            if(TrainAccessTogo.loadMask)   TrainAccessTogo.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                TrainAccessTogo.afterSaveSuccessFn(result, response, options);
            } else {
                TrainAccessTogo.afterSaveFailFn(result, response, options);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
}
TrainAccessTogo.saveForm = new Ext.form.FormPanel({
    layout: "form",     border: false,      style: "padding:10px",      labelWidth: TrainAccessTogo.labelWidth, plain:true, 
    baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "98%" }, buttonAlign:'center',
    items: [{xtype:"panel", border:false, baseCls:"x-plain", layout:"column", align:"center", 
    		items:[{
		    	baseCls:"x-plain", align:"center", style:"padding:3px", layout:"form", defaultType:"textfield", 
		    	labelWidth:TrainAccessTogo.labelWidth, columnWidth: .5, 
		    	items:[{		    			
		    			disabled: true,    			 
						fieldLabel: "车型",
						hiddenName: "trainTypeShortName", 
						returnField: [{widgetId:"trainTypeIDX_Togo_Id",propertyName:"typeID"}],
						displayField: "shortName", valueField: "shortName",
						pageSize: 0, minListWidth: 200,
						editable:true,
						allowBlank: false,
						forceSelection: true,
						xtype: "Base_combo",
			        	business: 'trainType',													
						fields:['typeID','shortName'],
						queryParams: {'isCx':'yes'},
						entity:'com.yunda.jx.base.jcgy.entity.TrainType',
						listeners : {   
				        	"select" : function() {   
				            	//重新加载车号下拉数据
				                var trainNo_comb = Ext.getCmp("trainNo_Togo_comb");   
				                trainNo_comb.reset();  
				                trainNo_comb.clearValue(); 
				                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
				                trainNo_comb.cascadeStore();	
				        	}   
				    	}, width: TrainAccessTogo.fieldWidth
		    		},{
		    			id:'trainTypeIDX_Togo_Id', name:'trainTypeIDX',xtype:'hidden'     		
		    		},{
		    			fieldLabel: "入段时间", name: "inTime", disabled: true, width: TrainAccessTogo.fieldWidth
		    		},{
		    			fieldLabel: "计划出段时间", name: "planOutTime", xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false,
		    			width: TrainAccessTogo.fieldWidth
		    		}]
    		},{
		    	baseCls:"x-plain", align:"center", style:"padding:3px", layout:"form", defaultType:"textfield", 
		    	labelWidth:TrainAccessTogo.labelWidth, columnWidth: .5, 
		    	items:[{
		    			disabled: true,    			
						id:"trainNo_Togo_comb",	
						fieldLabel: "车号",
						hiddenName: "trainNo", 
						displayField: "trainNo", valueField: "trainNo",
						pageSize: 20, minListWidth: 200,
						minChars : 1,
						minLength : 4, 
						maxLength : 5,
//						vtype: "numberInt",			
						xtype: "Base_combo",
						business: 'trainNo',
						entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
						fields:["trainNo","makeFactoryIDX","makeFactoryName",
						{name:"leaveDate", type:"date", dateFormat: 'time'},
						"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
						"bId","dId","bName","dName","bShortName","dShortName"],
						queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'true'},
						isAll: 'yes',
						editable:true,
						allowBlank: false,
						listeners : {
							"beforequery" : function(){
								//选择车号前先选车型
								var trainTypeShortName =  TrainAccessTogo.saveForm.getForm().findField("trainTypeShortName").getValue();
								if(Ext.isEmpty(trainTypeShortName)){
									MyExt.Msg.alert("请先选择车型！");
									return false;
								}
							}
						}, width: TrainAccessTogo.fieldWidth
		    		},{
		    			xtype: 'Base_comboTree',hiddenName: 'toGo',
						fieldLabel: '入段去向',
						allowBlank: false,
						treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
						queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},
						selectNodeModel: 'leaf',
						width: TrainAccessTogo.fieldWidth
		    		},{ xtype: 'hidden', name: 'idx'}]
    		}
    ]}],
    buttons: [{
        text: "确定入段去向", iconCls: "saveIcon", handler: TrainAccessTogo.saveFn
    }, {
        text: "关闭", iconCls: "closeIcon", handler: TrainAccessTogo.closeWin 
    }]
});

});