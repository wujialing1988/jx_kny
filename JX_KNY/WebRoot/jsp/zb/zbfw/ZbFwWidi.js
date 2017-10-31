/**
 * 整备作业项目数据项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbFwWidi');                       //定义命名空间
   /* ************* 定义全局变量开始 ************* */
	ZbFwWidi.zbfwwiIDX = "";	// 整备范围作业项主键
	ZbFwWidi.searchParams = {};
   /* ************* 定义全局变量结束 ************* */
 // 手动排序 
 ZbFwWidi.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/zbFwWidi!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
ZbFwWidi.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/zbFwWidi!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbFwWidi!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbFwWidi!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,
    storeAutoLoad: false,
    title:'数据项',
    tbar: ['add', 'delete','->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				ZbFwWidi.moveOrder(ZbFwWidi.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				ZbFwWidi.moveOrder(ZbFwWidi.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				ZbFwWidi.moveOrder(ZbFwWidi.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				ZbFwWidi.moveOrder(ZbFwWidi.grid, ORDER_TYPE_BOT);
			}
		}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'整备作业项主键', dataIndex:'zbfwwiIDX',hidden:true, editor:{maxLength:50,xtype:'hidden', id:'zbfwwiIDX_z'}
	},{
		header:'编号', dataIndex:'dICode',hidden:true, editor:{  maxLength:50 }
	},{
		header:'名称', dataIndex:'dIName', editor:{  maxLength:100,	allowBlank: false }
	},{
		header:'标准', dataIndex:'dIStandard', editor:{  maxLength:500 ,	allowBlank: false}
	},{
		header:'数据类型', dataIndex:'dIClass', editor:{ 
				id:'dIClass_m',
				xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[DATA_TYPE_ZF,"字符" ], [DATA_TYPE_SZ, "数字"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
 				allowBlank: false 
          }
	},{
		header:'是否必填', dataIndex:'isBlank',editor:{ 
				id:'isBlank_m',
				xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[IS_BLANK_YES, "是"], [IS_BLANK_NO, "否"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
 				allowBlank: false 
          } 
	},{
		header:'顺序号', dataIndex:'seqNo',width:60,editor:{ xtype:'numberfield',xtype:'hidden'}
	}],
	beforeAddButtonFn: function(){
		   ZbFwWidi.zbfwwiIDX = ZbFwWi.grid.saveForm.getForm().findField("idx").getValue();
			var zw = ZbFwWidi.grid.getSelectionModel();
			// 设置一个临时变量，用于记录当前以选中的第一条记录的“排序号”，用于实现在选择记录只取插入新记录的功能
			if (zw.getCount() > 0) {
				ZbFwWidi.tempSeqNo = zw.getSelected().get('seqNo');
			} else {
				ZbFwWidi.tempSeqNo = undefined;
			}
			return true;   	
	 },
   afterAddButtonFn: function(){  
			// 设置字段默认值
			Ext.getCmp('zbfwwiIDX_z').setValue(ZbFwWidi.zbfwwiIDX);
			Ext.getCmp('dIClass_m').setValue(DATA_TYPE_SZ);
			Ext.getCmp('isBlank_m').setValue(IS_BLANK_YES);
//			ZbFwWidi.grid.store.removeAll(); 
//			ZbFwWidi.grid.getTopToolbar().disable();
	},
   afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
			// 保存成功后启用排序功能按钮
//		   this.getTopToolbar().enable();
		  // ZbFwWidi.zbfwwiIDX = ZbFwWi.grid.saveForm.getForm().findField("zbfwIDX").getValue();
//		   ZbFwWidi.zbfwwiIDX = result.entity.zbfwwiIDX;
//   	       ZbFwWidi.grid.store.reload();
//   	       ZbFwWidi.zbfwwiIDX = "";
    },
	   /**
	     * 保存失败之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
	     * @param {} result 服务器端返回的json对象
	     * @param {} response 原生的服务器端返回响应对象
	     * @param {} options 参数项
	     */
	afterSaveFailFn: function(result, response, options){
	        this.store.reload();
	        alertFail(result.errMsg);
			// 保存失败后启用排序功能按钮
//			this.getTopToolbar().enable();
	 }

});
	// 设置默认排序
	ZbFwWidi.grid.store.setDefaultSort('seqNo', 'ASC');
	// 表格数据加载前的参数设置
   ZbFwWidi.grid.store.on('beforeload', function(){
   	   var searchParams = ZbFwWidi.searchParams;
		searchParams.zbfwwiIDX = ZbFwWidi.zbfwwiIDX;
//		alert(ZbFwWidi.zbfwwiIDX);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
});