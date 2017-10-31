/**
 * 提票过程角色配置UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('FaultTicketRuleDefine');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	FaultTicketRuleDefine.firstLoad = true;						// 定义一个标识变量，该变量用以标识QCItem.grid.store的load事件仅在页面初始化时才生效，详见QCItem.grid.store.on('load', function() {})
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 该方法用于根据【角色】的表记录，对【角色】表格的显示（或隐藏）和数据容器的加载
	FaultTicketRuleDefine.showRoleViewFn = function(data) {		
			FaultTicketRoleDefineView.faultTicketType = data.faultTicketType;
			FaultTicketRoleDefineView.grid.store.load();
	}
	
//	 // 手动排序 
//    FaultTicketRuleDefine.moveOrder = function(grid, orderType) {
//    	if(!$yd.isSelectedRecord(grid)) {
//			return;
//		}
//		var idx = $yd.getSelectedIdx(grid)[0];
//		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
//        	scope: grid,
//        	url: ctx + '/faultTicketCheckRule!moveOrder.action',
//			params: {idx: idx, orderType: orderType}
//        }));
//    }
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义提票规则表格开始 ************** */
	FaultTicketRuleDefine.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/faultTicketCheckRule!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/faultTicketCheckRule!saveOrUpdate.action',             //保存数据的请求URL
	    storeId:'faultTicketType', 
	    tbar: [ 'refresh', '->', {
//			text:'置顶', iconCls:'moveTopIcon', handler: function() {
//				FaultTicketRuleDefine.moveOrder(tpRoleDefine.grid, ORDER_TYPE_TOP);
//			}
//		}, {
//			text:'上移', iconCls:'moveUpIcon', handler: function() {
//				FaultTicketRuleDefine.moveOrder(tpRoleDefine.grid, ORDER_TYPE_PRE);
//			}
//		}, {
//			text:'下移', iconCls:'moveDownIcon', handler: function() {
//				FaultTicketRuleDefine.moveOrder(tpRoleDefine.grid, ORDER_TYPE_NEX);
//			}
//		}, {
//			text:'置底', iconCls:'moveBottomIcon', handler: function() {
//				FaultTicketRuleDefine.moveOrder(tpRoleDefine.grid, ORDER_TYPE_BOT);
//			}
		}],
	    title: '提票过程配置',
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'顺序号', dataIndex:'seqNo', width:50, editor:{  maxLength: 3  }
		},{
			header:'提票类型', dataIndex:'faultTicketType',width:200, editor:{ readOnly: true }
		},{
			header:'是否确认', dataIndex:'isAffirm', editor:{ xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[IS_N, "否"], [IS_Y, "是"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
				value:IS_N
        	}, renderer: function(v) {
        		if (v == IS_Y) return "是";
        		else return "否";
        	}
		
		},{
			header:'是否验收', dataIndex:'isCheck', editor:{ 
				xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[IS_N, "否"], [IS_Y, "是"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
				value:IS_N
        	}, renderer: function(v) {
        		if (v == IS_Y) return "是";
        		else return "否";
        	}
		},{
			header:'已处理卡控', dataIndex:'isCheckControl', editor:{ 
				xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[IS_N, "否"], [IS_Y, "是"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
				value:IS_N
        	}, renderer: function(v) {       		
        		if (v == IS_Y) return "是";
        		else return "否";
        	}
		}], 
	    saveFn: function(rowEditor, changes, record, rowIndex){
	    	if(Ext.isEmpty(record.data.isAffirm)){
	    		record.data.isAffirm = IS_N;
	    	}
	    	if(Ext.isEmpty(record.data.isCheckControl)){
	    		record.data.isCheckControl = IS_N;
	    	}
	    	if(Ext.isEmpty(record.data.isCheck)){
	    		record.data.isCheck = IS_N;
	    	}

    		if(self.loadMask)   self.loadMask.show();
			var cfg = {
	            scope: FaultTicketRuleDefine.grid, url: this.saveURL, jsonData: record.data,
	            success: function(response, options){
	                if(this.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    this.afterSaveSuccessFn(result, response, options);
	                } else {
	                    this.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    },
		listeners:{
			// 行单击的事件监听函数
			rowclick: function (grid, rowIndex ) {				
				FaultTicketRuleDefine.showRoleViewFn(this.store.getAt(rowIndex).data);
			}
		}
		
	});
	// 默认以“顺序号”升序排序
	FaultTicketRuleDefine.grid.store.setDefaultSort('seqNo', 'ASC');
	
	/** ************** 定义提票规则表格结束 ************** */
	
	//页面自适应布局
	new Ext.Viewport({
		layout:'border', 
		items:[{
			region:'west',
			width:600,
			layout:'fit',
			items:FaultTicketRuleDefine.grid,
			split:true
		}, {
			region:'center',
			layout:'fit',
			items:FaultTicketRoleDefineView.grid
		}]
	});
	
});