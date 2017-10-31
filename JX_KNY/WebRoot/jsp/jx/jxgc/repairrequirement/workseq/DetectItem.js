/**
 * 检测项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('DetectItem');                       //定义命名空间
DetectItem.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/detectItem!pageQuery.action',                 //装载列表数据的请求URL        
    deleteURL: ctx + '/detectItem!logicDelete.action',
    saveURL: ctx + '/detectItem!saveOrUpdate.action',             //保存数据的请求URL
    tbar:['add', 'delete',{
	    	text:"刷新",iconCls:"refreshIcon" ,handler: function(){
	    		DetectItem.grid.store.reload();
	    	}
	    }],
    page: false,
    storeAutoLoad: false,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"", xtype:'hidden' }
	},{
		header:'工步主键', dataIndex:'workStepIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'检测项编码', dataIndex:'detectItemCode',width:50, editor:{id:"detectItemCodeId", disabled:true, maxLength:50 }
	},{
		header:'检测内容', dataIndex:'detectItemContent', editor:{allowBlank:false,  maxLength:500 }
	},{
		header:'检测标准', dataIndex:'detectItemStandard', editor:{ allowBlank:false, maxLength:100 },searcher: {disabled: true}
	},{
		header:'值类型', dataIndex:'detectResulttype', width:50,editor:{
			allowBlank:false,
			id:"detectResulttype_combo",
			xtype: 'EosDictEntry_combo',
			hiddenName: 'detectResulttype',
			dicttypeid:'JXGC_DETECT_ITEM_DATA_TYPE',
			displayField:'dictname',valueField:'dictname',
			returnField:[{widgetId:"dictItemCodeId",propertyName:"dicttypeid"}],
			listeners: {
			'select' : function(){
		 				 		if("数字" == this.getValue()){
		 				 			Ext.getCmp("minResult").setDisabled(false) ;
		 				 			Ext.getCmp("maxResult").setDisabled(false) ;
		 				 		}else{
		 				 			Ext.getCmp("minResult").setDisabled(true) ;
		 				 			Ext.getCmp("maxResult").setDisabled(true) ;
		 				 		}
		 				 	}
			}
		}
	},{
			header:'最小范围值', dataIndex:'minResult',  editor:{id:"minResult", xtype:"numberfield", maxLength:6, vtype: 'nonNegativeFloat'  ,disabled: true}		
		},{
			header:'最大范围值', dataIndex:'maxResult',  editor:{id:"maxResult", xtype:"numberfield", maxLength:6, vtype: 'nonNegativeFloat' ,disabled: true }		
		},{
		header:'是否必填', dataIndex:'isNotBlank', width:50,
		renderer : function(v){if(v==isNotBlank_yes)return "必填";else if(v == isNotBlank_no) return "非必填"; else return ""},
		editor:{
			id:"isNotBlank_combo",
        	xtype: 'combo',
            fieldLabel: '是否必填',
            hiddenName:'isNotBlank',
            store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[isNotBlank_yes,'必填'],[isNotBlank_no,'非必填']]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local',
			value:isNotBlank_yes,
			editable: false,
			allowBlank: false
		}
    },{
    	header: "排序", dataIndex: "sortSeq", width:50, editor:{ vtype:"nonNegativeInt", maxLength:8}
    },{
		header:'字典项编码', dataIndex:'dictItemCode',hidden:true, editor:{xtype:'hidden', id:"dictItemCodeId", maxLength:128 }
	},{
		header:'业务状态', dataIndex:'status',hidden:true, width:50, editor:{ xtype:'hidden' },searcher: {disabled: true}
	}],
	defaultData: {idx: null},                 //新增时默认Record记录值，必须配置
    /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Ext.ux.grid.RowEditor} rowEditor This object
     * @param {Object} changes Object with changes made to the record.
     * @param {Ext.data.Record} record The Record that was edited.
     * @param {Number} rowIndex The rowIndex of the row just edited
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(rowEditor, changes, record, rowIndex){
    	record.data.workStepIDX = WorkStepEdit.idx ;
        if("数字" == record.get("detectResulttype") && record.get("minResult") && record.get("maxResult") && record.get("minResult") > record.get("maxResult")){
				alertFail("最小范围值不能比最大范围值大！");
				return false ;
			}else if("数字" != record.get("detectResulttype")){
				delete record.data.minResult ;
				delete record.data.maxResult ;
				return true;
			}else return true;
    },
    beforeEditFn: function(rowEditor, rowIndex){
    	var record = rowEditor.grid.store.getAt(rowIndex);
    	setTimeout(function(){ //延迟加载该方法体，实现赋值			
				Ext.getCmp("detectItemCodeId").disable();
				if(Ext.isEmpty(Ext.getCmp("detectItemCodeId").getValue())){
					Ext.Ajax.request({
				        url: ctx + "/codeRuleConfig!getConfigRule.action",
				        params: {ruleFunction : "JXGC_DETECT_ITEM_DETECT_ITEM_CODE"},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            Ext.getCmp("detectItemCodeId").setValue(result.rule) ;
				        }
			    	});
				}			
				Ext.getCmp("detectResulttype_combo").setDisplayValue(record.get("detectResulttype"),record.get("detectResulttype"));
				Ext.getCmp("isNotBlank_combo").setValue(record.get("isNotBlank"));
				if(Ext.isEmpty(Ext.getCmp("isNotBlank_combo").getValue()) && Ext.isEmpty(record.get("isNotBlank"))){
					Ext.getCmp("isNotBlank_combo").setValue(isNotBlank_yes);//新增默认为0必填 	
				}				
			},100)
			if("数字" == record.get("detectResulttype")){
				Ext.getCmp("minResult").setValue("");
	 			Ext.getCmp("maxResult").setValue("");
	 			Ext.getCmp("minResult").setDisabled(false) ;
	 			Ext.getCmp("maxResult").setDisabled(false) ;
	 		}else{
	 			Ext.getCmp("minResult").setDisabled(true) ;
	 			Ext.getCmp("maxResult").setDisabled(true) ;
	 		}
	        return true;
    },
    /**
     * 保存失败之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
    afterSaveFailFn: function(result, response, options){
        alertFail(result.errMsg);
    }
});
DetectItem.grid.store.setDefaultSort('updateTime', 'DESC');//设置默认排序

//添加过滤默认过滤信息
DetectItem.grid.store.on('beforeload',function(){
	var searchParam = {};
	searchParam.workStepIDX = WorkStepEdit.idx ; //检修工序卡主键
	var whereList = [] ;
	for (prop in searchParam) {		
		if(prop == 'workStepIDX'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

});