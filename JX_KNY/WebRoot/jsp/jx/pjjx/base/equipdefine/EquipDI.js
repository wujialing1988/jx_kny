/**
 * 机务设备检测数据项定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('EquipDI');                       //定义命名空间
	EquipDI.equipCardIDX = "###";//设备工单主键
	/** ************** 定义工单定义信息表单开始 ************** */
	EquipDI.cardForm = new Ext.form.FormPanel({
		labelWidth: EquipDI.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 10px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{													// 第1行第2列
				columnWidth: .5,
				layout: 'form',
				items: [{
					id:"equipCardNo",
					fieldLabel: '工单编码',
					maxLength:50,allowBlank: false,
					name: 'equipCardNo',
					xtype: 'textfield',
					width: EquipDI.fieldWidth
				}]
			},{												// 第1行第1列
				columnWidth: .5,
				layout: 'form',
				items: [{
					id:"deviceTypeName_comb", editable:false,selectOnFocus:false,forceSelection:false,typeAhead:false,
	    	    		 xtype: 'Base_combo',hiddenName: "deviceTypeName",fieldLabel:"设备类别",
		 				 entity:"com.yunda.jxpz.equipinfo.entity.DeviceType",displayField:"deviceTypeName",valueField:"deviceTypeName",fields:["deviceTypeCode","deviceTypeName"],			 
		 				 returnField:[{widgetId:"deviceTypeCode",propertyName:"deviceTypeCode"}], 
		 				 allowBlank: false
				},{
					id:"deviceTypeCode",
					fieldLabel: '设备类别编码',
					name: 'deviceTypeCode',
					xtype: 'hidden'
				}]
			},  {													// 第1行第3列
				columnWidth: 1,
				layout: 'form',
				items: [{
					id:"equipCardDesc",
					fieldLabel: '工单描述',
					maxLength:500,
					name: 'equipCardDesc',
					xtype: 'textarea',
					style:"width:80%",
					width: EquipDI.fieldWidth
				},{
					id:"cardId",
					fieldLabel: '主键',
					name: 'idx',
					xtype: 'hidden'
				}]
			}]
		}]
});
	/** ************** 定义工单定义信息表单结束 ************** */
	EquipDI.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/equipDI!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/equipDI!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/equipDI!logicDelete.action',            //删除数据的请求URL
	    tbar: ['add','delete'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'设备工单主键', dataIndex:'equipCardIDX',hidden:true,  editor:{  xtype:'hidden' }
		},{
			header:'检测项编号', dataIndex:'dataItemNo', editor:{ id:"dataItemNo",allowBlank: false, maxLength:30 }
		},{
			header:'检测项名称', dataIndex:'dataItemName', editor:{  allowBlank: false,maxLength:50 }
		},{
			header:'检测项描述', dataIndex:'dataItemDesc', editor:{  maxLength:500 }
		},{
			header:'单位', dataIndex:'unit', editor:{  maxLength:20 }
		},{
			header:'默认值', dataIndex:'defaultValue', editor:{ xtype:'numberfield' }
		},{
			header:'顺序号', dataIndex:'seqNo', editor:{ vtype:'positiveInt', maxLength:3 }
		}],
		defaultData: {idx: ''},           //新增时默认Record记录值
		beforeAddButtonFn: function(){
			Ext.Ajax.request({
		        url: ctx + "/codeRuleConfig!getConfigRule.action",
		        params: {ruleFunction : "JCZL_INSPECT_PLAN_PLAN_CODE"},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            Ext.getCmp("dataItemNo").setValue(result.rule);
		        }
			});
			return true;   	
	    },
		beforeSaveFn: function(rowEditor, changes, record, rowIndex){
			record.data.equipCardIDX = EquipDI.equipCardIDX ; //设备工单主键
	        return true;
	    }
	});
	EquipDI.grid.store.setDefaultSort("seqNo","ASC");
	EquipDI.grid.store.on('beforeload',function(){
		var searchParam = {};
		searchParam.equipCardIDX = EquipDI.equipCardIDX ; //设备工单主键
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	})
	
	// 作业工单编辑窗口
	EquipDI.editWin = new Ext.Window({
		title: "作业工单编辑",width: 1000, height: 500, maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: false , buttonAlign:"center",
		items: [{
			xtype: "panel", layout: "border",
			items:[{
				height: 160,layout: "fit",
				region : 'north',
		        bodyBorder: false,frame: true,
		        autoScroll : true,
		        items:[ EquipDI.cardForm ],
				buttons: [{
					xtype: "button", text: '保存', iconCls: 'saveIcon', handler: function() {
						//表单验证是否通过
				        var form = EquipDI.cardForm.getForm(); 
				        if (!form.isValid()) return;
				        var data = form.getValues();
						Ext.Ajax.request({
				            url: ctx + '/equipCard!saveOrUpdate.action',
				            jsonData: data,
				            success: function(response, options){
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {
				                    alertSuccess();
				                    EquipCard.grid.store.load();
				                    EquipDI.equipCardIDX = result.entity.idx;
									EquipDI.grid.store.load();
									EquipDI.grid.getTopToolbar().enable();
									Ext.getCmp("cardId").setValue(result.entity.idx);
				                } else {
				                    alertFail(result.errMsg);
				                }
				            },
				            failure: function(response, options){
				                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				            }
				        });
						// 重新加载表格
						EquipDI.grid.store.load();
					}
				}, {
					xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
						EquipDI.editWin.hide();
						// 重新加载列表
						EquipCard.grid.store.load();
					}
				}],
				buttonAlign: 'center'
			},{
				region : 'center',
				bodyBorder: false,
		        layout: "fit",
		        items:[ EquipDI.grid ]
			}]
		}]
     });
});