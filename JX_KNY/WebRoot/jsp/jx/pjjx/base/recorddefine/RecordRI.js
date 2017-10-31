/**
 * 检修检测项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RecordRI');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	RecordRI.recordCardIDX = "###";
	RecordRI.labelWidth = 100;
	RecordRI.fieldWidth = 140;
	RecordRI.searchParams = {};
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自动设置【记录卡编号】字段值
	RecordRI.setRecordRiNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_RECORD_RI_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					Ext.getCmp("repairItemNo_m").setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	// 手动排序 
    RecordRI.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/recordRI!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义保存表单开始 ************** */
	RecordRI.saveForm = new Ext.form.FormPanel({
		style: 'padding: 10px;',
		labelWidth: RecordRI.labelWidth,
		layout:"column",
		defaults:{
			columnWidth:1, layout:"form", defaults:{
				xtype:'textfield',
				width: RecordRI.fieldWidth,
				allowBlank:false
			}
		},
		items:[{
			columnWidth:.4,
			items:[{
				fieldLabel:"检修检测项编号",
				maxLength:30,
				name: "repairItemNo", id: "repairItemNo_m"
			}]
		}, {
			columnWidth:0.6,
			items:[{
				fieldLabel:"检修检测项名称",
				maxLength:50,
				name: "repairItemName",
				anchor:"85%"
			}]
		}, {
			items:[{
				fieldLabel:"技术要求",
				anchor:"95%",
				maxLength:500,
				name:"repairStandard",
				xtype:"textarea",
				height: 70
			},
			//由于添加了一张新表作为默认结果值存放，此处通过数据字典方式的默认结果放弃使用
			{
			
				fieldLabel:"默认结果",
				anchor:"30%",
				allowBlank:true,
				maxLength:30,
				editable:true,
				name: 'defaultResult', 
				xtype: 'EosDictEntry_combo', 
				dicttypeid: 'PJJX_RECORD_RI_RESULT',
				displayField:'dictname',valueField:'dictname',
				anchor:'40%',
				hidden :true
			}]
		}, {
			columnWidth:0.4,
			items:[{
				fieldLabel:"检具",
				maxLength:50,
				name:"checkTools",allowBlank:true
			}]
		}, {
			defaults:{
				xtype:'hidden',
				allowBlank:true
			},
			items:[{
				fieldLabel:"idx主键", name:"idx"
			}, {
				fieldLabel:"记录卡idx主键", name:"recordCardIDX"
			}, {
				fieldLabel:"顺序号", name:"seqNo"
			}]
		}]
	});
	/** ************** 定义保存表单结束 ************** */

	/** ************** 定义检修检测项表格开始 ************** */
	RecordRI.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/recordRI!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/recordRI!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/recordRI!logicDelete.action',            //删除数据的请求URL
	    tbar:['search', 'add', 'delete','->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				RecordRI.moveOrder(RecordRI.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				RecordRI.moveOrder(RecordRI.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				RecordRI.moveOrder(RecordRI.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				RecordRI.moveOrder(RecordRI.grid, ORDER_TYPE_BOT);
			}
		}],
	    // 自定义保存表单
	    saveForm: RecordRI.saveForm,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, width: 10, editor: { xtype:'hidden' }
		},{
			header:'记录卡主键', dataIndex:'recordCardIDX', hidden:true,  width: 10,  editor:{  maxLength:50 }, searcher:{xtype: 'hidden'}
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, width: 7,  editor:{ xtype:'numberfield', maxLength:3 }, searcher:{xtype: 'hidden'}
		},{
			header:'检修检测项编号', dataIndex:'repairItemNo', width: 13,  editor:{  maxLength:30 }, searcher:{fieldLabel: '检测项编号'},
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='RecordRI.grid.toEditFn(\""+ RecordRI.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'检修检测项名称', dataIndex:'repairItemName',  width: 20, editor:{  maxLength:50 }, searcher:{fieldLabel: '检测项名称'}
		},{
			header:'技术要求', dataIndex:'repairStandard', width: 55,  editor:{  maxLength:500 }
		},{
			header:'检具', dataIndex:'checkTools', width: 55,  editor:{  maxLength:500 }
		},{
			header:'默认结果', dataIndex:'defaultResult', width: 15,  editor:{  maxLength:30 }, searcher:{xtype: 'hidden'}
		}],
		searchFn: function(searchParam){
			searchParam.recordCardIDX = RecordRI.recordCardIDX;
	    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
	        });	
	    },
	    // 自定义保存窗口，动态生成”质量检查“ - checkboxgroup
		createSaveWin: function(){
	        this.saveWin = new Ext.Window({
	            title:"新增",
				width:1200,
				height:600,
				plain: true,
				modal:true,
				closeAction: 'hide', 
				layout:"border",
				items:[{
					region:"north", layout:'fit',
					frame:true,
					height: 195,
					items:RecordRI.saveForm,
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
					//baseCls:"x-plain", 
					items: RecordDI.grid,
					xtype: "fieldset",
					title: '检修检测项'				
				},{
					region:'west',
				    style: "padding:5px",
					width: 200,
					xtype: "fieldset",
					items: [PartsStepResult.grid],
					split: false,
					layout: 'fit',
					title: '默认检测/检修结果'
				}]
	        });
	    },
		afterShowSaveWin: function(){
			RecordRI.saveForm.find("name", "recordCardIDX")[0].setValue(RecordRI.recordCardIDX);
			// 实现在选择记录只取插入新记录的功能
			var sm = RecordRI.grid.getSelectionModel();
			if (sm.getCount() > 0) {
				RecordRI.saveForm.find("name", "seqNo")[0].setValue(sm.getSelected().get('seqNo'));
			}
			RecordRI.setRecordRiNo();
			RecordDI.grid.store.removeAll(); 
			RecordDI.grid.getTopToolbar().disable();
			
			// 禁用【默认检测/检修结果】表格的工具栏
			PartsStepResult.grid.store.removeAll(); 
	        PartsStepResult.grid.getTopToolbar().disable();
		},
		afterShowEditWin: function(record, rowIndex){
		 	// 重新加载【检修检测项】表格
			RecordDI.rIIDX = record.get('idx');
			//this.saveForm.find('name', 'defaultResult')[0].setDisplayValue(record.get('defaultResult'), record.get('defaultResult'));
			RecordDI.grid.store.load();
			RecordDI.grid.getTopToolbar().enable();
			
			//加载默认grid
			PartsStepResult.grid.store.load();
			// 启用【默认检测/检修结果】表格的工具栏
	        PartsStepResult.grid.getTopToolbar().enable();
		},
	    // 保存成功后，对页面信息进行回显
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        
	        var entity = result.entity;
	        RecordRI.saveForm.find("name", "idx")[0].setValue(entity.idx);
	        RecordRI.saveForm.find("name", "seqNo")[0].setValue(entity.seqNo);
	        RecordDI.rIIDX = entity.idx;
	        // 启用【检修检测项】表格的工具栏
	        RecordDI.grid.getTopToolbar().enable();
	        
	        // 启用【默认检测/检修结果】表格的工具栏
	        PartsStepResult.grid.getTopToolbar().enable();
	    },
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
			//获取默认结果中的默认值
			//获取总条数
   			var recordCount = PartsStepResult.grid.store.getCount();
   			if(recordCount > 0){
    			for(var i = 0;i<recordCount;i++){
	    			//获取每一行对象
	    			var recordV = PartsStepResult.grid.store.getAt(i);
					//比较唯一标示funccode
					if(recordV.data.isDefault == isDefault_yes){
		 				rowEditor.defaultResult = recordV.data.resultName;
		       			return true;
	    			}
	   			}
	   		}
	 		rowEditor.defaultResult = '';
   			return true;
	    }
	});
	
	RecordRI.grid.store.on('beforeload', function(){
		var searchParams = RecordRI.searchParams;
		searchParams.recordCardIDX = RecordRI.recordCardIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	// 设置默认排序
	RecordRI.grid.store.setDefaultSort('seqNo', 'ASC');
	/** ************** 定义检修检测项表格结束 ************** */
	
});