/**
 * 检测项目结果 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsStepResult');                       //定义命名空间
	
	PartsStepResult.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/partsStepResult!pageQuery.action',                 //装载列表数据的请求URL	    
    	deleteURL: ctx + '/partsStepResult!deletePartsStepResult.action',
	    saveURL: ctx + '/partsStepResult!savePartsStepResult.action',             //保存数据的请求URL
	    tbar:['add','delete',{
	    	text:"刷新",iconCls:"refreshIcon" ,handler: function(){
	    		PartsStepResult.grid.store.reload();
	    	}
	    }],
	    page: false,
	    storeAutoLoad: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'检修检测项主键', dataIndex:'recordRIIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'处理结果', dataIndex:'resultName', width:170,editor:{
				allowBlank: false, maxLength:50
			}
		},{
			header:'默认', dataIndex:'isDefault', 
			editor:{
				xtype: 'combo',
	            fieldLabel: '',
	            width:20,
	            hiddenName:'isDefault',
	            store:new Ext.data.SimpleStore({
				    fields: ['v', 't'],
					data : [[isDefault_yes,'是'],[isDefault_no,'否']]
				}),
				valueField:'v',
				displayField:'t',
				triggerAction:'all',
				mode:'local',
				value:isDefault_yes,
				editable: false,
				allowBlank: false
			},
			 renderer : function(v){if(v==isDefault_yes)return "是";else return "否";}
		}],
		defaultData: {idx: null, isDefault: isDefault_no},                //新增时默认Record记录值，必须配置
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
	    	record.data.recordRIIDX = RecordDI.rIIDX ;//传递配件检修检测项id
	        return true;
	    },
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        RecordRI.grid.store.reload();
	        
	        alertSuccess();
	    },
	    afterDeleteFn:function(){
	    	RecordRI.grid.store.reload();
	    }
	    
	});
	//PartsStepResult.grid.store.setDefaultSort('idx', 'DESC');//设置默认排序

	//添加过滤默认过滤信息
	PartsStepResult.grid.store.on('beforeload',function(){
		var searchParam = {};
		searchParam.recordRIIDX = RecordDI.rIIDX ; //配件检修检测项主键
		var whereList = [] ;
		for (prop in searchParam) {	
            if(prop == 'recordRIIDX'){
                whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
	        }else{
	            whereList.push({propName:prop, propValue: searchParam[prop]}) ;
	        }
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
});