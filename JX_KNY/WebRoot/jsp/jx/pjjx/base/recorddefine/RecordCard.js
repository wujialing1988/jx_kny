/**
 * 基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('RecordCard');                       // 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	RecordCard.qcItems = [];
	RecordCard.recordIDX = "###";
	RecordCard.idx = "###";
	RecordCard.labelWidth = 80;
	RecordCard.fieldWidth = 140;
	RecordCard.searchParams = {};
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自动生成“质量检测项”checkbox内容
	RecordCard.createQcItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	        editor.id = "checkItemId" + i;
	        editor.xtype = "checkbox";
	        editor.name = "qCContent"; 	//定义检查项名称规则
	        editor.boxLabel = field.qCItemName;
	        editor.inputValue = field.qCItemNo;
	        editor.width = 80;
	        RecordCard.qcItems.push(editor);
		}
	}();
	// 自动设置【记录卡编号】字段值
	RecordCard.setRecordCardNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_RECORD_CARD_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					Ext.getCmp("recordCardNo_m").setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
    
    // 手动排序 
    RecordCard.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/recordCard!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义上方表单开始 ************** */
	RecordCard.form = new Ext.form.FormPanel({
		style: 'padding: 10px;',
		layout:"column",
		labelWidth: RecordCard.labelWidth,
		defaults:{
			layout:"form", defaults: {
				xtype:"textfield", anchor: '95%', readOnly: true, style: 'background:none; border: none;'
			}
		},
		items:[{
			columnWidth:0.35,
			items:[{
				fieldLabel:"记录编号", name: "recordNo"
			}]
		}, {
			columnWidth:0.65,
			items:[{
				fieldLabel:"记录名称", name: "recordName"
			}]
		}, {
			columnWidth:1,
			items:[{
				fieldLabel:"记录描述", name:"recordDesc"
			}]
		}]
	});
	/** ************** 定义上方表单结束 ************** */
	
	/** ************** 定义保存表单开始 ************** */
	RecordCard.saveForm = new Ext.form.FormPanel({
		labelWidth: RecordCard.labelWidth,
		style: 'padding: 10px;',
		layout:"column",
		border:false, baseCls:'x-plain',
		defaults: {
			layout:"form",
			border:false, baseCls:'x-plain'
		},
		items:[{
			columnWidth:0.6,
			items:[{
					xtype:"textfield",
					fieldLabel:"记录卡编号",
					maxLength:30,
					name: "recordCardNo", id: "recordCardNo_m",
					allowBlank:false,
					width: RecordCard.fieldWidth + 20
				}]
		}, {
			columnWidth:0.4,
			items:[{
				xtype:"hidden",
				vtype:"positiveInt1To100",
				fieldLabel:"顺序号",
				maxLength:3,
				name: "seqNo",
				allowBlank:false,
				width: RecordCard.fieldWidth - 45
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"textfield",
				fieldLabel:"记录卡描述",
				anchor:"95%",
				maxLength:500,
				name:"recordCardDesc",
				allowBlank:false,
				xtype:"textarea",
				height: 70
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"hidden", fieldLabel:"idx主键", name:"idx"
			}, {
				xtype:"hidden", fieldLabel:"记录单idx主键", name:"recordIDX"
			}]
		}]
	});
	/** ************** 定义保存表单结束 ************** */
	
	/** ************** 定义下方表格开始 ************** */
	RecordCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/recordCard!pageList.action',                 // 装载列表数据的请求URL
	    saveURL: ctx + '/recordCard!saveOrUpdate.action',             // 保存数据的请求URL
	    deleteURL: ctx + '/recordCard!logicDelete.action',            // 删除数据的请求URL
	    // 自定义保存表单
	    saveForm: RecordCard.saveForm,
	    tbar: ['add', 'delete','->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				RecordCard.moveOrder(RecordCard.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				RecordCard.moveOrder(RecordCard.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				RecordCard.moveOrder(RecordCard.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				RecordCard.moveOrder(RecordCard.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', width:20, hidden:true
		},{
			header:'检修记录主键', dataIndex:'recordIDX', width:20, hidden:true
		},{
			header:'顺序号', dataIndex:'seqNo', hidden: true, width:20, editor:{ xtype:'numberfield', maxLength:3, allowBlank:false }
		},{
			header:'记录卡编号', dataIndex:'recordCardNo', width:30, editor:{ maxLength:30, allowBlank:false },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='RecordCard.grid.toEditFn(\""+ RecordCard.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'记录卡描述', dataIndex:'recordCardDesc', editor:{ maxLength:500, allowBlank:false }
		},{
			header:'质量检验', dataIndex:'qCContent', editor:{  maxLength:100 }
		}],
	    // 自定义保存窗口，动态生成”质量检查“ - checkboxgroup
		createSaveWin: function(){
	        this.saveWin = new Ext.Window({
	            title:"新增",
				width:850, height:600,
				plain: true, maximized:true, modal:true,
				closeAction: 'hide', 
				layout:"border",
				items:[{
					region:"north", height: 180,
					baseCls:"x-plain", border:false,		
					items:[{
						baseCls:"x-plain", border:false,		
						items:RecordCard.saveForm
					}, {
						xtype:"form",
						hidden:false,							// 新版设计取消了在工序里设置质量检查项，因此隐藏此部分表单（2014-11-12 by HeTao）
						id:"qcForm",
						labelWidth:100,
						labelAlign:"left",
						baseCls:"x-plain", border:false,
						labelWidth: RecordCard.labelWidth,
						region:"south",
						height:30,
						style: 'padding:0px 10px 10px 10px;',
						items:[
							{
								xtype:'compositefield', fieldLabel: '质量检查', items: RecordCard.qcItems, anchor:'50%'
							}
						]
					}],  
					buttonAlign: 'center',
		            buttons: [{
		                text: "保存", iconCls: "saveIcon", scope: this, handler: this.saveFn
		            }, {
		                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
		            }]
				}, {
					// 【检修检测项】表格
					region:"center",
					layout:"fit",
					baseCls:"x-plain", border:false,	
					items:[{
				        xtype: "tabpanel", border: false, activeTab:0, enableTabScroll:true, items:[{
				            title: "检修检测项", layout: "fit", frame: true, border: false, items: RecordRI.grid
				        }/*,{
			            	title: "配件清单", layout: "fit", border: false, items: PartsFwList.grid
			        	}*/]
					}]
				}]
	        });
	    },
		afterShowSaveWin: function(){
			RecordCard.saveForm.find("name", "recordIDX")[0].setValue(RecordCard.recordIDX);
			// 实现在选择记录只取插入新记录的功能
			var sm = RecordCard.grid.getSelectionModel();
			if (sm.getCount() > 0) {
				RecordCard.saveForm.find("name", "seqNo")[0].setValue(sm.getSelected().get('seqNo'));
			}
			RecordCard.setRecordCardNo();
			// 重置”质量检查“ - checkboxgroup
			Ext.getCmp("qcForm").getForm().reset();
			RecordRI.recordCardIDX = "###"; 
			RecordRI.grid.store.load(); 
			RecordCard.idx = "###"; 
			//PartsFwList.grid.store.load(); 
			RecordRI.grid.getTopToolbar().disable();
			//PartsFwList.grid.getTopToolbar().disable();
		},
	    // 打开编辑窗口后对”质量检查“项赋值
	    afterShowEditWin: function(record, rowIndex){
	    	var qCContent = record.get('qCContent');
	    	if (Ext.isEmpty(qCContent)) {
	    		for (j = 0; j < objList.length; j++) {
		    		var checkobj = Ext.getCmp("checkItemId" + j);
		    		checkobj.setValue(false);
	    		}
	    	} else {
		    	var qcItems = qCContent.split("|");
		    	for (j = 0; j < objList.length; j++) {
		    		var checkobj = Ext.getCmp("checkItemId" + j);
		    		checkobj.setValue(false);
		    		for (var i = 0; i < qcItems.length; i++) {
		    			if (checkobj.boxLabel == qcItems[i]) {
		    				checkobj.setValue(true);
		    			}
		    		}
		    	}
	    	}
	    	
	    	// 重新加载【检修检测项】表格
			RecordRI.recordCardIDX = record.get('idx');
			RecordCard.idx = record.get('idx');
			RecordRI.grid.store.load();
//			PartsFwList.grid.store.load();
			RecordRI.grid.getTopToolbar().enable();			
//			PartsFwList.grid.getTopToolbar().enable();		
	    },
	    // 保存操作之前对”质量检查“项进行赋值
	    beforeSaveFn: function(data){ 
	    	var qCContent = "";
		    for (var i = 0; i < objList.length; i++) {
		    	// 获取检查项checkbox对象
		    	var checkobj = Ext.getCmp("checkItemId" + i);
		    	if(!Ext.isEmpty(checkobj) && checkobj.checked){
		    		qCContent += "|" + checkobj.boxLabel;
		    	}
		    }
		    if (!Ext.isEmpty(qCContent)) {
				data.qCContent = qCContent.substr(1);
		    }
	    	return true; 
	    },
	    // 保存成功后，对页面信息进行回显
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        
	        var entity = result.entity;
	        RecordCard.saveForm.find("name", "idx")[0].setValue(entity.idx);
	        RecordCard.saveForm.find("name", "seqNo")[0].setValue(entity.seqNo);
	        RecordRI.recordCardIDX = entity.idx;
	        RecordCard.idx = entity.idx;
	        // 启用【检修检测项】表格的工具栏
	        RecordRI.grid.getTopToolbar().enable();
//	        PartsFwList.grid.getTopToolbar().enable();
//	        PartsFwList.grid.store.load();
	    }
	});
	RecordCard.grid.store.on('beforeload', function(){
		var searchParams = RecordCard.searchParams;
		searchParams.recordIDX = RecordCard.recordIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	// 设置默认排序
	RecordCard.grid.store.setDefaultSort('seqNo', 'ASC');
	/** ************** 定义下方表格结束 ************** */
	
	RecordCard.win = new Ext.Window({
		title:"配件检修记录卡维护",
		width:736, height:499,
		layout:"border",
		maximized:true,
		closeAction:'hide',
		items:[
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				border: false,
				items: RecordCard.grid
			},
			{
				region:"north",
				border: false,
				frame: true,
				title: '记录信息',
				collapsible: true,
				height:110,
				items:[RecordCard.form]
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text:'关闭', iconCls:'closeIcon', handler: function() {
				RecordCard.win.hide();
			}
		}]
	})
})
