/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('WorkSeqSearcher');
	
	/** ************* 定义全局变量开始 ************* */
	WorkSeqSearcher.searchParams = {};
	WorkSeqSearcher.processIDX = "";										// 检修作业流程主键
	WorkSeqSearcher.nodeIDX = "";											// 检修作业流程节点主键
	WorkSeqSearcher.pTrainTypeIDX = "";									// 检修作业车型主键
	WorkSeqSearcher.labelWidth = 80,
	WorkSeqSearcher.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	WorkSeqSearcher.addFn = function() {
		if (!$yd.isSelectedRecord(WorkSeqSearcher.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(WorkSeqSearcher.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var jobNodeUnionWorkSeq = {};
			jobNodeUnionWorkSeq.nodeIDX = WorkSeqSearcher.nodeIDX;
			jobNodeUnionWorkSeq.recordCardIDX = ids[i];
			datas.push(jobNodeUnionWorkSeq);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/jobNodeUnionWorkSeq!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    WorkSeqSearcher.grid.store.reload();
                    TrainQR.grid.store.load();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	WorkSeqSearcher.searchForm = new Ext.form.FormPanel({
		labelWidth: WorkSeqSearcher.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			defaults:{
				columnWidth: .3, layout: 'form',
				defaults: {
					xtype: 'textfield', width: WorkSeqSearcher.fieldWidth
				}
			},
			items:[{												// 第1行第1列
				items: [{
					fieldLabel: '记录单编码', name: 'repairProjectCode'
					
				},{
					fieldLabel: '记录单名称', name: 'repairProjectName'
					
				}]
			}, {
				items: [{
					fieldLabel: '记录卡编码', name: 'workSeqCode'
				},{
					fieldLabel: '记录卡名称', name: 'workSeqName'
				}]
			}, {													// 第1行第3列
				items: [{
					fieldLabel: '描述', name: 'repairScope'
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				WorkSeqSearcher.searchParams = WorkSeqSearcher.searchForm.getForm().getValues();
				// 重新加载表格
				WorkSeqSearcher.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				WorkSeqSearcher.searchForm.getForm().reset();
				WorkSeqSearcher.searchParams = {};
				// 重新加载表格
				WorkSeqSearcher.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	WorkSeqSearcher.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workSeq!findWorkCardInfo.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workSeq!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workSeq!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    storeAutoLoad:false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"entityID", xtype:'hidden' }
		},{
			header:'机车检修记录单编号', dataIndex:'repairProjectCode',width:120, editor:{ maxLength:50 , allowBlank: false }
		},{
			header:'机车检修记录单名称', dataIndex:'repairProjectName',width:200, editor:{  maxLength:50 }
		},{
			header:'机车检修记录卡编号', dataIndex:'workSeqCode',width:120, editor:{  maxLength:50 , allowBlank: false }
		},{
			header:'机车检修记录卡名称', dataIndex:'workSeqName',width:200, editor:{  maxLength:50 }
		},{
			header:'描述', dataIndex:'repairScope',hidden: false,  width:200, editor:{xtype:'textarea',  maxLength:1000 },searcher: {disabled: true}
		},{
			header:'操作', dataIndex:'idx', align:'center', width: 40, renderer: function(value){
				return "<img src='" + addIcon + "' alt='添加' style='cursor:pointer' onclick='WorkSeqSearcher.addFn()'/>";
			}
		}],
		
		// 重新编辑函数，双击表格行是，执行添加操作
		toEditFn: function(grid, rowIndex, e){
			WorkSeqSearcher.addFn();
		}
	});
	// 设置默认排序
	WorkSeqSearcher.grid.store.setDefaultSort('workSeqCode', 'ASC');
	//查询前添加过滤条件
	WorkSeqSearcher.grid.store.on('beforeload' , function(){
		WorkSeqSearcher.searchParams = WorkSeqSearcher.searchForm.getForm().getValues();
		WorkSeqSearcher.searchParams = MyJson.deleteBlankProp(WorkSeqSearcher.searchParams);
		WorkSeqSearcher.searchParams.pTrainTypeIDX = WorkSeqSearcher.pTrainTypeIDX;
		WorkSeqSearcher.searchParams.processIDX = WorkSeqSearcher.processIDX;
		this.baseParams.whereListJson = Ext.util.JSON.encode(WorkSeqSearcher.searchParams);
	});
	/** ************* 定义候选表格结束 ************* */
	
	WorkSeqSearcher.win = new Ext.Window({
		title:"选择机车检修记录卡",
		width: 800,
		height: 500,
		modal: true,
		layout: 'border',
		closeAction: 'hide',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 140,
			border: true,
			collapsible: true,
			items: [WorkSeqSearcher.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [WorkSeqSearcher.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: WorkSeqSearcher.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}], 
		listeners: {
			show: function(window){
				WorkSeqSearcher.grid.store.load();
			},
			hide: function(){
                TrainQR.grid.store.reload();
			}
		}
	});
	
});