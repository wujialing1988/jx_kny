/**
 * 配件清单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsFwList');                       //定义命名空间
PartsFwList.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/partsFwList!findPartsList.action',                 //装载列表数据的请求URL        
    deleteURL: ctx + '/partsFwList!delete.action',
    saveURL: ctx + '/partsFwList!saveOrUpdate.action',             //保存数据的请求URL
    tbar:[{
    	text:"选择配件",iconCls:"addIcon" ,handler: function(){
    			PartsTypeForSelect.win.show();
    			PartsTypeForSelect.grid.store.load();
    	}
    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"", xtype:'hidden' }
	},{
		header:'关联主键', dataIndex:'relationIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'规格型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'位置', dataIndex:'place', editor:{ maxLength:500 }
	},{
		header:'类型', dataIndex:'type', width:50,
		editor:{
        	xtype: 'combo',
            fieldLabel: '类型',
            hiddenName:'type',
            store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[TYPE_DISMANTLE,TYPE_DISMANTLE],[TYPE_INSTALL,TYPE_INSTALL],[TYPE_INSEPARAB,TYPE_INSEPARAB]]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local',
			value:TYPE_DISMANTLE,
			editable: false,
			allowBlank: false
		}
	},{
		header:'规格型号', dataIndex:'specificationModel',width:50, editor:{ disabled:true, maxLength:50 }
	},{
		header:'配件名称', dataIndex:'partsName',width:50, editor:{ disabled:true, maxLength:50 }
	},{
		header:'计量单位', dataIndex:'unit',width:50, editor:{disabled:true, maxLength:50 }
	}],
	defaultData: {idx: null},                 //新增时默认Record记录值，必须配置
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

//添加过滤默认过滤信息
PartsFwList.grid.store.on('beforeload',function(){
	var searchParam = {};
	searchParam.relationIDX = RecordCard.idx ; //检修记录卡主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});