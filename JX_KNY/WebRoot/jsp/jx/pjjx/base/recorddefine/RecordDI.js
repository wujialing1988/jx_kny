/**
 * 检测项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RecordDI');                       //定义命名空间
	
	
	/** ************** 定义全局变量开始 ************** */
	RecordDI.rIIDX = "###";
	RecordDI.searchParams = {};
	// 全局记录行号
	RecordDI.rowIndex = 0 ;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	
	// 自动设置【记录卡编号】字段值
	RecordDI.setRecordRiNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_RECORD_DI_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					Ext.getCmp("dataItemNo_m").setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	// 手动排序 
    RecordDI.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/recordDI!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	/** ************** 定义全局函数结束 ************** */
	
	RecordDI.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/recordDI!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/recordDI!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/recordDI!logicDelete.action',            //删除数据的请求URL
	    tbar:['add', 'delete', '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				RecordDI.moveOrder(RecordDI.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				RecordDI.moveOrder(RecordDI.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				RecordDI.moveOrder(RecordDI.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				RecordDI.moveOrder(RecordDI.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true,  width: 20, editor: { xtype:'hidden' }
		},{
			header:'检修检测项主键', dataIndex:'rIIDX', hidden: true,  width: 20, editor:{  maxLength:50, id:'rIIDX_m' }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, width: 10,  editor:{ xtype:'numberfield', id:'seqNo_m', maxLength:3, disabled: true }
		},{
			header:'编号', dataIndex:'dataItemNo', width: 20,  editor:{  maxLength:30, id:'dataItemNo_m' }
		},{
			header:'数据项名称', dataIndex:'dataItemName', width: 50,  editor:{  maxLength:50, allowBlank: false }
		},{
			header:'检具', dataIndex:'checkTools', width: 20,  editor:{  maxLength:50 }
		},{
			header:'最小范围值', dataIndex:'minResult', width: 30,  editor:{ xtype:"numberfield", decimalPrecision: 4, maxLength:9, vtype: 'nonNegativeNumber'  }		
		},{
			header:'最大范围值', dataIndex:'maxResult', width: 30,  editor:{ xtype:"numberfield", decimalPrecision: 4, maxLength:9, vtype: 'nonNegativeNumber'  }		
		},{
			header:'是否必填', dataIndex:'isBlank', width: 35, renderer: function(v){
				if (v == IS_BLANK_YES) return '是';
				if (v == IS_BLANK_NO) return '否';
			}, editor:{  
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
				value:IS_BLANK_YES,
 				allowBlank: false 
        	}
		},{
			header:'检测项编码', dataIndex:'checkID', width: 25,hidden:true,
        	editor: {
        		id:"check_Base_combo",
        		xtype:'PartsCheckItem_SelectWin',
        		editable:true,
        		fieldLabel : "检测项编码",
        		listeners:{
        			"show":function(me){
        				var record = RecordDI.grid.store.getAt(RecordDI.rowIndex);
						me.setDisplayValue(record.data.checkID,record.data.checkID);
        			},
        			"afterrender":function(me){
        				// 第一次加载时
        				var record = RecordDI.grid.store.getAt(RecordDI.rowIndex);
        				if(!Ext.isEmpty(record.data.checkID)){
							me.setDisplayValue(record.data.checkID,record.data.checkID);
        				}
        			}
        		}
			}
		}],
		beforeEditFn: function(rowEditor, rowIndex){
			RecordDI.rowIndex = rowIndex ;
	        return true;
	    },
		beforeAddButtonFn: function(){
//			Ext.getCmp("check_Base_combo").clearValue();
			var sm = RecordDI.grid.getSelectionModel();
			// 设置一个临时变量，用于记录当前以选中的第一条记录的“排序号”，用于实现在选择记录只取插入新记录的功能
			if (sm.getCount() > 0) {
				RecordDI.tempSeqNo = sm.getSelected().get('seqNo');
			} else {
				RecordDI.tempSeqNo = undefined;
			}
			return true;   	
	    },
		afterAddButtonFn: function(){  
			// 设置字段默认值
			RecordDI.setRecordRiNo();
			Ext.getCmp('rIIDX_m').setValue(RecordDI.rIIDX);
			if (!RecordDI.tempSeqNo) {
				Ext.getCmp('seqNo_m').setValue(RecordDI.grid.store.getCount());
			} else {
				Ext.getCmp('seqNo_m').setValue(RecordDI.tempSeqNo);
			}
			Ext.getCmp('isBlank_m').setValue(IS_BLANK_YES);
			// 新增时禁用排序功能按钮
			this.getTopToolbar().disable();
		},
		beforeSaveFn: function(rowEditor, changes, record, rowIndex){
			if(changes.checkID){
				record.data.checkID = changes.checkID;
			}
//			else{
//				record.data.checkID = "";
//			}
			if(record.get("maxResult") && record.get("minResult") && record.get("minResult") > record.get("maxResult")){
				alertFail("最小范围值不能比最大范围值大！");
				return false ;
			}else return true;
	    },
		cancelFn: function(rowEditor, pressed){
	        var idx = rowEditor.record.get(this.storeId);
	        //如果是临时记录，删除该记录
	        if(idx == null || '' == idx.trim()) {
	            this.store.removeAt(rowEditor.rowIndex);
	            rowEditor.grid.view.refresh();
	        }
	        rowEditor.grid.view.refresh();
			// 取消时启用排序功能按钮
			this.getTopToolbar().enable();
	    },
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
			// 保存成功后启用排序功能按钮
			this.getTopToolbar().enable();
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
			this.getTopToolbar().enable();
	    }
	});
	// 设置默认排序
	RecordDI.grid.store.setDefaultSort('seqNo', 'ASC');
	RecordDI.grid.store.on('beforeload', function(){
		var searchParams = RecordDI.searchParams; 
		searchParams.rIIDX = RecordDI.rIIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
});