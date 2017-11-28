/**
 * 检测项目结果 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkStepResult');                       //定义命名空间
	WorkStepResult.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/workStepResult!pageQuery.action',                 //装载列表数据的请求URL	    
    	deleteURL: ctx + '/workStepResult!logicDelete.action',
	    saveURL: ctx + '/workStepResult!saveWorkStepResult.action',             //保存数据的请求URL
	    tbar:['add','delete',{
	    	text:"刷新",iconCls:"refreshIcon" ,handler: function(){
	    		WorkStepResult.grid.store.reload();
	    	}
	    }],
	    page: false,
	    storeAutoLoad: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'工步主键', dataIndex:'workStepIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'处理结果编码', dataIndex:'resultCode',hidden:true, editor:{id:"resultCodeId", disabled:true, maxLength:50 }
		},{
			header:'处理结果名称', dataIndex:'resultName', width:170,editor:{
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
		defaultData: {idx: null, isDefault: isDefault_yes},                //新增时默认Record记录值，必须配置
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
	        return true;
	    }
	    
	});
	WorkStepResult.grid.store.setDefaultSort('updateTime', 'DESC');//设置默认排序

	//添加过滤默认过滤信息
	WorkStepResult.grid.store.on('beforeload',function(){
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