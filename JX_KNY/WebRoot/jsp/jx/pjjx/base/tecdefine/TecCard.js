/**
 * 基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('TecCard');                       // 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	TecCard.tecIDX = "###";
	TecCard.labelWidth = 80;
	TecCard.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自动设置【工艺卡编号】字段值
	TecCard.setTecNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_TEC_CARD_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					Ext.getCmp("tecCardNo_m").setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	// 按钮【维护工艺卡】触发的函数操作
	TecCard.configTecCardFn = function() {
		var sm = TecCard.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;0
		}
		// 加载基本信息表单数据
		var data = sm.getSelections()[0];
		TecCardConfig.form.getForm().loadRecord(data);
		
		// 加载所需物料信息
		TecCardMat.tecCardIDX = data.get('idx');
		TecCardMat.grid.store.load();
		// 隐藏”所需物料“Tab页的行编辑控件
		TecCardMat.grid.rowEditor.slideHide(); 
		
		// 加载配件检修工序信息
		TecCardWS.tecCardIDX = data.get('idx');
		TecCardWS.wsParentIDX = "ROOT_0";
		TecCardWS.grid.store.load();
		TecCardWS.tree.root.setText(data.get('tecCardNo'));
        TecCardWS.tree.root.reload();
        TecCardWS.tree.getRootNode().expand();
        // 设置工序列表表格的title
        TecCardWS.grid.setTitle(data.get('tecCardNo') + "&nbsp;-&nbsp;下级工序");
		
		// 显示【工艺维护】窗口
		TecCardConfig.win.show();
		
	}
	// 按钮【保存并维护】触发的函数操作
	TecCard.saveAndConfigFn = function() {
        //表单验证是否通过
        var form = TecCard.grid.saveForm.getForm(); 
        if (!form.isValid()) return;
        
        //获取表单数据前触发函数
        TecCard.grid.beforeGetFormData();
        var data = form.getValues();
        //获取表单数据后触发函数
        TecCard.grid.afterGetFormData();
        
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!TecCard.grid.beforeSaveFn(data)) return;
        
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: TecCard.grid, url: TecCard.grid.saveURL, jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    TecCard.grid.afterSaveSuccessFn(result, response, options);
                	var data = result.entity;
                	Ext.getCmp('idx_c').setValue(data.idx);
                	Ext.getCmp('tecCardNo_c').setValue(data.tecCardNo);
                	Ext.getCmp('tecCardDesc_c').setValue(data.tecCardDesc);
                	Ext.getCmp('seqNo_c').setValue(data.seqNo);
                	Ext.getCmp('tecIDX_c').setValue(data.tecIDX);
					TecCardConfig.win.show();
					// 加载所需物料信息
					TecCardMat.tecCardIDX = data.idx;
					TecCardMat.grid.store.load();
					// 隐藏”所需物料“Tab页的行编辑控件
					TecCardMat.grid.rowEditor.slideHide(); 
					
					// 加载配件检修工序信息
					TecCardWS.tecCardIDX = data.idx;
					TecCardWS.wsParentIDX = "ROOT_0";
	       		 	TecCardWS.grid.setTitle(data.tecCardNo + "&nbsp;-&nbsp;下级工序");
					TecCardWS.grid.store.load();
					TecCardWS.tree.root.setText(data.tecCardNo);
			        TecCardWS.tree.root.reload();
			        TecCardWS.tree.getRootNode().expand();
                } else {
                    TecCard.grid.afterSaveFailFn(result, response, options);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    }
    
    // 手动排序 
    TecCard.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/tecCard!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义上方表单开始 ************** */
	TecCard.form = new Ext.form.FormPanel({
		style: 'padding: 10px;',
		layout:"column",
		defaults:{
			defaults:{readOnly: true}
		},
		items:[{
			columnWidth:0.35,
			labelWidth: TecCard.labelWidth,
			layout:"form",
			items:[
				{
					xtype:"textfield",
					fieldLabel:"工艺编号",
					name: "tecNo",
					anchor: '95%',
					style: 'background:none; border: none;'
				}
			]
		},
		{
			columnWidth:0.65,
			layout:"form",
			labelWidth: TecCard.labelWidth,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"工艺名称",
					name: "tecName",
					anchor: '95%',
					style: 'background:none; border: none;'
				}
			]
		},
		{
			layout:"form",
			columnWidth:1,
			labelWidth: TecCard.labelWidth,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"工艺描述",
					maxLength:500,
					name:"tecDesc",
					anchor: '95%',
					style: 'background:none; border: none;'
				}
			]
		}]
	});
	/** ************** 定义上方表单结束 ************** */
	
	/** ************** 定义保存表单开始 ************** */
	TecCard.saveForm = new Ext.form.FormPanel({
		style: 'padding: 10px;',
		layout:"column",
		border:false, baseCls:'x-plain',
		items:[{
			columnWidth:0.6,
			labelWidth: TecCard.labelWidth,
			layout:"form",
			border:false, baseCls:'x-plain',
			items:[
				{
					xtype:"textfield",
					fieldLabel:"工艺卡编号",
					maxLength:30,
					name: "tecCardNo", id: "tecCardNo_m",
					allowBlank:false,
					width: TecCard.fieldWidth + 20
				}
			]
		},
		{
			columnWidth:0.4,
			layout:"form",
			border:false, baseCls:'x-plain',
			labelWidth: TecCard.labelWidth,
			items:[
				{
					xtype:"hidden",
					vtype:"positiveInt1To100",
					fieldLabel:"顺序号",
					name: "seqNo",
					allowBlank:false,
					width: TecCard.fieldWidth - 45
				}
			]
		},
		{
			layout:"form",
			columnWidth:1,
			border:false, baseCls:'x-plain',
			labelWidth: TecCard.labelWidth,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"工艺卡描述",
					name:"tecCardDesc",
					maxLength:500,
					width: 385,
					allowBlank:false,
					xtype:"textarea",
					height: 80
				}
			]
		},
		{
			layout:"form",
			columnWidth:1,
			border:false, baseCls:'x-plain',
			items:[
				{
					xtype:"hidden", fieldLabel:"idx主键", name:"idx"
				}, {
					xtype:"hidden", fieldLabel:"检修工艺idx主键", name:"tecIDX", id: "tecIDX_m"
				}
			]
		}]
	});
	/** ************** 定义保存表单结束 ************** */
	
	/** ************** 定义下方表格开始 ************** */
	TecCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/tecCard!pageList.action',                 // 装载列表数据的请求URL
	    saveURL: ctx + '/tecCard!saveOrUpdate.action',             // 保存数据的请求URL
	    deleteURL: ctx + '/tecCard!logicDelete.action',            // 删除数据的请求URL
	    saveWinWidth: 520,
	    saveWinHeight: 200,
	    // 自定义保存表单
	    saveForm: TecCard.saveForm,
	    tbar: ['add', {
	    	text:'维护工艺卡', iconCls:'configIcon', handler: TecCard.configTecCardFn
	    }, 'delete','->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				TecCard.moveOrder(TecCard.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				TecCard.moveOrder(TecCard.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				TecCard.moveOrder(TecCard.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				TecCard.moveOrder(TecCard.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', width:20, hidden:true, editor: { xtype:'hidden' }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, width:7, editor:{ xtype:'numberfield', maxLength:3, allowBlank:false }
		},{
			header:'工艺卡编号', dataIndex:'tecCardNo', width:30, editor:{ maxLength:30, allowBlank:false },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='TecCard.configTecCardFn()'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'工艺卡描述', dataIndex:'tecCardDesc', width:70, editor:{ maxLength:500, allowBlank:false }
		},{
			header:'检修工艺主键', dataIndex:'tecIDX', hidden:true
		}],
		afterShowSaveWin: function(){
			Ext.getCmp('tecIDX_m').setValue(TecCard.tecIDX);
			// 实现在选择记录只取插入新记录的功能
			var sm = TecCard.grid.getSelectionModel();
			if (sm.getCount() > 0) {
				TecCard.saveForm.find("name", "seqNo")[0].setValue(sm.getSelected().get('seqNo'));
			}
			TecCard.setTecNo();
		},
		createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
	            title:"新增", width:this.saveWinWidth, height:this.saveWinHeight, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.saveForm, 
	            buttons: [{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: this.saveFn
	            }, {
	                text: "保存并维护", iconCls: "configIcon", scope: this, handler: TecCard.saveAndConfigFn
	            }, {
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
	            }]
	        });
	    },
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 隐藏编辑窗口
	        this.saveWin.hide();
	    }
	});
	TecCard.grid.store.on('beforeload', function(){
		var searchParams = {tecIDX: TecCard.tecIDX};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	// 设置默认排序
	TecCard.grid.store.setDefaultSort('seqNo', 'ASC');
	/** ************** 定义下方表格结束 ************** */
	
	TecCard.win = new Ext.Window({
		title:"配件检修工艺卡维护",
		width:736,
		height:499,
		layout:"border",
		maximized:true,
		closeAction:'hide',
		items:[
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				border: false,
				items:TecCard.grid
			},
			{
				region:"north",
				border: false,
				frame: true,
				title: '工艺信息',
				collapsible: true,
				height:110,
				items:[TecCard.form]
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text:'关闭', iconCls:'closeIcon', handler: function() {
				TecCard.win.hide();
			}
		}]
	})
})
