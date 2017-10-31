/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('RepairProjectSearcher');
	
	/** ************* 定义全局变量开始 ************* */
	RepairProjectSearcher.searchParams = {};
	RepairProjectSearcher.processIDX = "";										// 检修作业流程主键
	RepairProjectSearcher.nodeIDX = "";											// 检修作业流程节点主键
	RepairProjectSearcher.pTrainTypeIdx = "";									// 检修作业车型主键
	RepairProjectSearcher.labelWidth = 80,
	RepairProjectSearcher.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	RepairProjectSearcher.addFn = function() {
		if (!$yd.isSelectedRecord(RepairProjectSearcher.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(RepairProjectSearcher.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var jobNodeProjectDef = {};
			jobNodeProjectDef.nodeIDX = RepairProjectSearcher.nodeIDX;
			jobNodeProjectDef.projectIDX = ids[i];
			datas.push(jobNodeProjectDef);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/jobNodeProjectDef!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    RepairProjectSearcher.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	RepairProjectSearcher.searchForm = new Ext.form.FormPanel({
		labelWidth: RepairProjectSearcher.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			defaults:{
				columnWidth: .3, layout: 'form',
				defaults: {
					xtype: 'textfield', width: RepairProjectSearcher.fieldWidth
				}
			},
			items:[{												// 第1行第1列
				items: [{
					fieldLabel: '编码', name: 'repairProjectCode'
				}]
			}, {
				items: [{
					fieldLabel: '名称', name: 'repairProjectName'
				}]
			}, {													// 第1行第3列
				items: [{
					fieldLabel: '备注', name: 'remark'
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				RepairProjectSearcher.searchParams = RepairProjectSearcher.searchForm.getForm().getValues();
				// 重新加载表格
				RepairProjectSearcher.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				RepairProjectSearcher.searchForm.getForm().reset();
				RepairProjectSearcher.searchParams = {};
				// 重新加载表格
				RepairProjectSearcher.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	RepairProjectSearcher.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/repairProject!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/repairProject!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/repairProject!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    storeAutoLoad:false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'编码', dataIndex:'repairProjectCode', width: 120
		},{
			header:'车型主键', dataIndex:'pTrainTypeIdx', hidden:true
		},{
			header:'车型', dataIndex:'pTrainTypeShortname', hidden:true
		},{
			header:'名称', dataIndex:'repairProjectName', width: 240
		},{
			header:'检修项目类型', dataIndex:'repairProjectType', hidden:true
		},{
			header:'状态', dataIndex:'status', hidden:true
		},{
			header:'备注', dataIndex:'remark', width: 280
		},{
			header:'组成型号类型', dataIndex:'buildUpType', hidden:true
		},{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX', hidden:true
		},{
			header:'组成型号编码', dataIndex:'buildUpTypeCode', hidden:true
		},{
			header:'组成型号', dataIndex:'buildUpTypeName', hidden:true
		},{
			header:'操作', dataIndex:'idx', align:'center', width: 40, renderer: function(value){
				return "<img src='" + addIcon + "' alt='添加' style='cursor:pointer' onclick='RepairProjectSearcher.addFn()'/>";
			}
		}],
		
		// 重新编辑函数，双击表格行是，执行添加操作
		toEditFn: function(grid, rowIndex, e){
			RepairProjectSearcher.addFn();
		}
	});
	// 设置默认排序
	RepairProjectSearcher.grid.store.setDefaultSort('repairProjectName', 'ASC');
	//查询前添加过滤条件
	RepairProjectSearcher.grid.store.on('beforeload' , function(){
		RepairProjectSearcher.searchParams.pTrainTypeIdx = RepairProjectSearcher.pTrainTypeIdx;
		var searchParams = MyJson.deleteBlankProp(RepairProjectSearcher.searchParams);
		var whereList = [];
		for(prop in searchParams){
			if ('pTrainTypeIdx' == prop) {
				whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.EQ, stringLike: false});
				continue;
			}
			whereList.push({propName:prop, propValue:searchParams[prop]});
		}
		var sql = "IDX NOT IN(SELECT Project_IDX FROM JXGC_JobNode_Project_Def WHERE RECORD_STATUS = 0 AND Node_IDX IN(SELECT IDX FROM JXGC_Job_Process_Node_Def WHERE RECORD_STATUS = 0 AND Process_IDX = '" + RepairProjectSearcher.processIDX +"'))";
		whereList.push({sql: sql, compare:Condition.SQL});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	/** ************* 定义候选表格结束 ************* */
	
	RepairProjectSearcher.win = new Ext.Window({
		title:"选择作业项目",
		width: 800,
		height: 500,
		modal: true,
		layout: 'border',
		closeAction: 'hide',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 120,
			border: true,
			collapsible: true,
			items: [RepairProjectSearcher.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [RepairProjectSearcher.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: RepairProjectSearcher.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}], 
		listeners: {
			show: function(window){
				RepairProjectSearcher.grid.store.load();
			},
			hide: function(){
                RepairProject.grid.store.reload();
			}
		}
	});
	
});