/**
 * 作业节点 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WPNode');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	WPNode.wPIDX = "###";							// 当前作业节点树的 - 作业流程主键
	WPNode.parentWPNodeIDX = "###";					// 当前作业节点树的 - 上级作业节点主键
	WPNode.searchParams = {};	
	// diamond, dot, star, triangle, triangleDown, square,llipse, circle, database, box, text	['圆形','circle'],['圆柱形','database'],['椭圆形','llipse'],
	var showFlowData =[['正方形','square'],['凌形','diamond'],['圆点形','dot'],['星形','star'],['三角形','triangle'],['下三角形','triangleDown'],
					['盒形','box'],['文本框','text']];
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 新增函数
	WPNode.addFn =  function(isLeaf) {
		var wpNode = {};
		wpNode.wpIDX = WPNode.wPIDX;
		wpNode.parentWPNodeIDX = WPNode.parentWPNodeIDX;
		wpNode.wpNodeName = "<新节点>";
		wpNode.showFlag = 'square';
		wpNode.isLeaf = isLeaf;
		
		// 指定插入位置的新增功能,如果选择了一条（或多条）记录，则在已选择记录的第一条（从上往下）之前新增记录
		var sm = WPNode.grid.getSelectionModel();
		if (sm.getCount() > 0) {
			var firstSlectedRecord = sm.getSelections()[0];
			wpNode.seqNo = firstSlectedRecord.get('seqNo');
		}
		
		// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wPNode!saveOrUpdate.action',
            jsonData: wpNode,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载【作业节点表格】
                    WPNode.grid.store.reload();
                	// 重新加载【作业节点树】
                    WPNode_tree.reload(WPNode_tree.treePath);
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
	}
	// 手动排序 
    WPNode.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/wPNode!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	/** ************** 定义全局函数结束 ************** */
    
    /** ************** 定义节点编辑表单开始 ************** */
	WPNode.saveForm = new Ext.form.FormPanel({
		style: "padding:0 15px;",
		layout:"column",
		items:[{
			layout:"form",
			columnWidth:0.6,
			items:[{
				xtype:"textfield",
				name:"wpNodeName",
				fieldLabel:"节点名称",
				maxLength:50,
				allowBlank: false,
				anchor:"90%"
			},{
					
				xtype: 'compositefield', id:'nodeStartTime_id',fieldLabel: '开始时间', combineErrors: false, 
				items: [{
					xtype: 'label',
					text: '第',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {
					xtype: 'numberfield', id: 'startDay', name: 'startDay', width: 45, validator: function(value) {
						if (Ext.isEmpty(value)) {
							return null;
						}
						if (parseInt(value) < 0) {
							return "请输入正整数";
						}						
					}
				}, {
					xtype: 'label',
					text: '天',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {
					xtype:"my97date", id: 'startTime', name: 'startTime', width: 80, format: "H:i",
			        	my97cfg: {dateFmt:"HH:mm"}		
				}]
			
			}]
		}, {
			layout:"form",
			columnWidth:0.4,
			items:[{
				xtype: 'compositefield', fieldLabel: '工期', combineErrors: false, allowBlank:false,
				items: [{
					xtype: 'numberfield', id: 'ratedPeriod_H', name: 'ratedPeriod_h', width: 60, validator: function(value) {
						if (parseInt(value) < 0) {
							return "请输入正整数";
						}
						var mValue = Ext.getCmp('ratedPeriod_M').getValue();
						if (Ext.isEmpty(value) && Ext.isEmpty(mValue)) {
							return '工期不能为空'
						} else {
							if (value.length > 2) {
								return "该输入项最大长度为2";
							} else if (Ext.isEmpty(mValue) || parseInt(mValue) < 60){
								Ext.getCmp('ratedPeriod_H').clearInvalid();
								Ext.getCmp('ratedPeriod_M').clearInvalid();
							}
						}
					}
				}, {
					xtype: 'label',
					text: '时',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {
					xtype: 'numberfield', id: 'ratedPeriod_M', name: 'ratedPeriod_m', width: 60, validator: function(value) {
						if (parseInt(value) < 0) {
							return "请输入正整数";
						}
						var hValue = Ext.getCmp('ratedPeriod_H').getValue();
						if (Ext.isEmpty(value) && Ext.isEmpty(hValue)) {
							return '工期不能为空'
						} else {
							if (parseInt(value) >= 60) {
								return "不能超过60分钟";
							} else if (hValue.length <= 2){
								Ext.getCmp('ratedPeriod_H').clearInvalid();
								Ext.getCmp('ratedPeriod_M').clearInvalid();
							}
						}
					}
				}, {
					xtype: 'label',
					text: '分',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}]
			},{
				
					xtype: 'compositefield', id:'nodeEndTime_id',fieldLabel: '结束时间', combineErrors: false, 
					items: [{
						xtype: 'label',
						text: '第',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {
						xtype: 'numberfield', id: 'endDay', name: 'endDay', width: 45, validator: function(value) {
							if (Ext.isEmpty(value)) {
								return null;
							}
							if (parseInt(value) < 0) {
								return "请输入正整数";
							}						
						}
					}, {
						xtype: 'label',
						text: '天',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {
						xtype:"my97date", id: 'endTime', name: 'endTime', width: 80, format: "H:i",
				        	my97cfg: {dateFmt:"HH:mm"}		
					}]		            
				
			}]
		
		}, /*{
			layout:"form",
			columnWidth:1,
			items:[{				
			    id:"showFlag_s",
	        	xtype: 'combo',
	            fieldLabel: '流程显示样式',
	            hiddenName:'showFlag',
	            store:new Ext.data.SimpleStore({
				     fields: ['K','V'],
					data : showFlowData
				}),
				triggerAction:'all',
				valueField:'V',
				displayField:'K', 
				value: 'diamond',
				mode: 'local',
				editable: false,
				allowBlank: false
			}]
		},*/ {
			layout:"form",
			columnWidth:1,
			items:[{
				xtype:"textarea",
				name:"wpNodeDesc",
				fieldLabel:"节点描述",
				maxLength:500,
				anchor:"97%"
			}]
		}, {
			// 【作业节点】保存表单的隐藏字段
			layout:"form",
			columnWidth:1,
			defaultType:'hidden',
			items:[{
				fieldLabel:"idx主键", name:"idx"
			},{
				fieldLabel:"顺序号", name:"seqNo"
			},{
				fieldLabel:"是否子节点", name:"isLeaf"
			},{
				fieldLabel:"作业流程主键", name:"wpIDX"
			},{
				fieldLabel:"上级作业节点主键", name:"parentWPNodeIDX"
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text:'保存', iconCls:'saveIcon', handler: function() {WPNode.grid.saveFn()}
		}]
	});
	/** ************** 定义节点编辑表单结束 ************** */
	
	/** ************** 定义节点编辑窗口开始 ************** */
	WPNode.saveWin = new Ext.Window({
		width:772,
		height:572,
		layout:"border",
		closeAction:"hide",
		modal:true,
		items:[
			{
				region:"center",
				layout:"fit",
				items:WPNodeSeq.grid				// 【前后置节点行编辑表格】
			},
			{
				region:"north",
				frame:true,
				height:190,
				items:WPNode.saveForm				// 【节点编辑表单】
			}
		],
		buttonAlign: "center",
		buttons: [{
            text: "关闭", iconCls: "closeIcon", handler: function(){ 
            	this.findParentByType('window').hide(); 
            }
        }]
	})
	/** ************** 定义节点编辑窗口结束 ************** */
    
	WPNode.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wPNode!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/wPNode!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPNode!logicDelete.action',            //删除数据的请求URL
	    viewConfig:null,
	    saveForm: WPNode.saveForm,
	    saveWin: WPNode.saveWin,
	    refreshButtonFn: function(){                        //点击刷新按钮触发的函数
	        WPNode.grid.store.reload();
	    },
	    tbar:[{
	    	text:"新增父节点", iconCls:"addIcon", handler: function () {WPNode.addFn(IS_LEAF_NO)}
	    },{
	    	text:"新增子节点", iconCls:"addIcon", handler: function () {WPNode.addFn(IS_LEAF_YES)}
	    }, 'delete', 'refresh', '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				WPNode.moveOrder(WPNode.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				WPNode.moveOrder(WPNode.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				WPNode.moveOrder(WPNode.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				WPNode.moveOrder(WPNode.grid, ORDER_TYPE_BOT);
			}
		}],
		
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业流程主键', dataIndex:'wpIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:false, width:60, editor:{ xtype:'numberfield', maxLength:3 }
		},{
			header:'上级作业节点', dataIndex:'parentWPNodeIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'节点名称', dataIndex:'wpNodeName', width:200,  editor:{  maxLength:50 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='WPNode.grid.toEditFn(\""+ WPNode.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'节点描述', dataIndex:'wpNodeDesc', editor:{  maxLength:500 }, width:480
		},{
			header:'工期', dataIndex:'ratedPeriod', width:160, editor:{ xtype:'numberfield', maxLength:6 }, renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				return WP.formatTime(value);
			}
		},{
			header:'开始时间', dataIndex:'startTime', width: 120,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				if (!Ext.isEmpty(value)) {
					return "第" + record.get('startDay')+ "天 " + value;
				}
			}
		},{
			header:'结束时间', dataIndex:'endTime',  width: 120,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				if (!Ext.isEmpty(value)) {
					return "第" + record.get('endDay')+ "天 " + value;
				}
			}
		},{
			header:'是否是叶子节点', dataIndex:'isLeaf', hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
		},{
			header:'前置节点主键', dataIndex:'preWPNodeIDX', hidden:true, editor:{ disabled: true }
		},{
			header:'前置节点', dataIndex:'preWPNodeName', width:70, hidden:true,  editor:{ disabled: true }
		},{
			header:'前置节点(序号)', dataIndex:'preWPNodeSeqNo', width:125,  editor:{ disabled: true }
		},{
			header:'开始天数', dataIndex:'startDay', width: 120,hidden:true, editor:{ xtype:'numberfield', maxLength:12 }			
		},{
			header:'结束天数', dataIndex:'endDay', width: 120,hidden:true, editor:{ xtype:'numberfield', maxLength:12 }
		},{
			header:'显示图标样式', dataIndex:'showFlag', width: 120,hidden:true, editor:{ xtype:'numberfield', maxLength:12 }
		}],
		
		afterDeleteFn: function(){ 
        	// 重新加载【作业节点树】
            WPNode_tree.reload(WPNode_tree.treePath);
		},
		
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 重新加载【作业节点树】
            WPNode_tree.reload(WPNode_tree.treePath);
	    },
	    
	    afterShowEditWin: function(record, rowIndex){
	    	this.saveWin.setTitle("前置节点编辑");
	    	
	    	// 重新加载【节点编辑】节点前后置关系表格数据
	    	WPNodeSeq.wpIDX = record.get('wpIDX');						// 设置作业流程主键
	    	WPNodeSeq.wpNodeIDX = record.get('idx');					// 设置作业节点主键
	    	WPNodeSeq.parentWPNodeIDX = record.get('parentWPNodeIDX');	// 设置上级作业节点主键
	    	WPNodeSeq.grid.store.load();
	    	
	    	// 设置“工期”字段
	    	var ratedPeriod = record.get('ratedPeriod');
	    	if (!Ext.isEmpty(ratedPeriod)) {
				var ratedPeriod_h = Math.floor(ratedPeriod/60);
				var ratedPeriod_m = ratedPeriod%60;
				// 设置 额定工期_时
				Ext.getCmp("ratedPeriod_H").setValue(ratedPeriod_h);
				// 设置 额定工期_分
				Ext.getCmp("ratedPeriod_M").setValue(ratedPeriod_m);
	    	}
	    	
	    	// 如果是父节点，则不能对“额定工期”字段进行编辑
	    	if (record.get('isLeaf') == IS_LEAF_NO) {
	    		Ext.getCmp('ratedPeriod_H').disable().clearInvalid();
	    		Ext.getCmp('ratedPeriod_M').disable();
	    	} else {
	    		Ext.getCmp('ratedPeriod_H').enable();
	    		Ext.getCmp('ratedPeriod_M').enable();
	    	}
	    },
	    
	    // 重新保存方法，完善对“额定工期（分钟）”字段保存时的特殊处理
		beforeSaveFn: function(data){ 
			var ratedPeriod_h = data.ratedPeriod_h;
			var ratedPeriod_m = data.ratedPeriod_m;
			data.ratedPeriod = parseInt(ratedPeriod_h * 60);
			if (!Ext.isEmpty(ratedPeriod_m)) {
				 data.ratedPeriod += parseInt(ratedPeriod_m);
			}
			delete data.ratedPeriod_h;
			delete data.ratedPeriod_m;
			return true; 
		},
		
	    beforeGetFormData: function(){
	    	if (this.saveForm.find('name', 'isLeaf')[0].getValue() == IS_LEAF_NO) {
		    	Ext.getCmp('ratedPeriod_H').enable();
	    		Ext.getCmp('ratedPeriod_M').enable();
	    	}
	    },
	    
	    afterGetFormData: function(){    
	    	if (this.saveForm.find('name', 'isLeaf')[0].getValue() == IS_LEAF_NO) {
		    	Ext.getCmp('ratedPeriod_H').disable();
	    		Ext.getCmp('ratedPeriod_M').disable();
	    	}
	    }
	});
	// 设置默认排序字段为“顺序号（升序）”
	WPNode.grid.store.setDefaultSort("seqNo", "ASC");
	WPNode.grid.store.on('beforeload', function(){
		WPNode.searchParams.wpIDX = WPNode.wPIDX;
		WPNode.searchParams.parentWPNodeIDX = WPNode.parentWPNodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(WPNode.searchParams);
		
	});
});