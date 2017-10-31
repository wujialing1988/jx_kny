/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('VNodeCaseDelay');
	
	/** **************** 定义全局变量开始 **************** */
	VNodeCaseDelay.labelWidth = 100;
	VNodeCaseDelay.fieldWidth = 140;
	
	VNodeCaseDelay.idx = null;				// 机车检修作业流程节点主键
	VNodeCaseDelay.nodeName = null;			// 机车检修作业流程节点名称
	VNodeCaseDelay.workPlanIDX = null;		// 机车检修作业流程计划主键
	VNodeCaseDelay.trainInfo = null;		// 机车检修作业流程计划车型信息，如：SS4B_0006_辅修_一次 
	VNodeCaseDelay.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."});
	/** **************** 定义全局变量结束 **************** */
	// 上方表格【工序延误记录表格】定义
	VNodeCaseDelay.grid = new Ext.yunda.Grid({
		toEditFn: Ext.emptyFn,
		storeAutoLoad: false,
		loadURL: ctx + '/nodeCaseDelay!findChildren.action',
		tbar:null, viewConfig: null,
		page:false, singleSelect:true, remoteSort: false,
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden:true
		}, {
			dataIndex: 'nodeName', header: '节点名称', width: 150
		}, {
			dataIndex: 'nodeCaseIdx', header: '节点主键', hidden:true
		}, {
			dataIndex: 'nodeIDX', header: '节点定义主键', hidden:true
		}, {
			dataIndex: 'tecProcessCaseIDX', header: '流程主键', hidden:true
		}, {
			dataIndex: 'rdpIDX', header: '作业计划主键', hidden:true
		}, {
			dataIndex: 'delayType', header: '延误类型', hidden:true
		}, {
			dataIndex: 'delayTypeName', header: '延误类型', width: 100
		}, {
			dataIndex: 'delayTime', header: '延误时间', width: 120, renderer: function(v){
				return formatTime(v, 'm');
			}
		}, {
			dataIndex: 'delayReason', header: '延误原因', width: 160
		}, {
			dataIndex: 'planBeginTime', header: '计划开始时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}, {
			dataIndex: 'planEndTime', header: '计划结束时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}, {
			dataIndex: 'realBeginTime', header: '实际开始时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}]
	});
	
	// Grid数据加载前的查询条件传递 
	VNodeCaseDelay.grid.store.on('beforeload', function(){
		this.baseParams.nodeCaseIdx = VNodeCaseDelay.idx;
	});
	VNodeCaseDelay.grid.store.on('load', function(me, records, options){
		// TODO
	});
	
	// Grid行选择事件监听，选择一条记录后，加载该工序延误记录进行编辑
	VNodeCaseDelay.grid.getSelectionModel().on('rowselect', function(sm, rowIndex, r){
		Ext.getCmp('edit_form').getForm().reset();
		// 加载工序延误记录
		Ext.getCmp('edit_form').getForm().loadRecord(r);
		// 回显"延误工序"字段值
		Ext.getCmp('node_case_idx').setDisplayValue(r.get('nodeCaseIdx'), r.get('nodeName'));
		// 回显"延误时间"字段值
		VNodeCaseDelay.setDelayTime(parseInt(r.get('delayTime')));
	});
	
	// 回显“延误时间”字段值
	VNodeCaseDelay.setDelayTime = function(delayTime) {
		Ext.getCmp('ratedPeriod_H').setValue("");
		Ext.getCmp('ratedPeriod_M').setValue("");
		if (delayTime / 60 >= 1) {
			Ext.getCmp('ratedPeriod_H').setValue(Math.floor(delayTime / 60));
		}
		if (delayTime % 60 > 0) {
			Ext.getCmp('ratedPeriod_M').setValue(delayTime % 60 );
		}
	}
	
	// 工序延误编辑窗口定义
	VNodeCaseDelay.editWin = new Ext.Window({
		title:"工序延误",
		modal: true,
		closeAction: 'hide',
		width:900, height:450,
		layout:"border",
		items:[{
			id:'base_form',
			xtype:'form', region:'north', height:60, padding:"10px",
			labelWidth: VNodeCaseDelay.labelWidth - 40,
			frame:true, layout:"column",
			defaults: {
				layout:"form",
				columnWidth:0.25,
				defaults:{
					xtype: 'textfield', anchor:'100%', readOnly:true, 
					style:'background:none;border:none;font-weight:bold;'
				}
			},
			items: [{
				items:[{
					name:'trainTypeShortName', fieldLabel: '车型'
				}]
			}, {
				items:[{
					name:'trainNo', fieldLabel: '车号'
				}]
			}, {
				items:[{
					name:'repairClassName', fieldLabel: '修程'
				}]
			}, {
				items:[{
					name:'repairtimeName', fieldLabel: '修次'
				}]
			}]
		},{
			id: 'edit_form',
			xtype:"form", region:"south", height:160, padding:"10px",
			labelWidth: VNodeCaseDelay.labelWidth,
			frame:true,
			defaults: {
				xtype:"container",
				autoEl:"div",
				layout:"column",
				defaults: {
					xtype:"container",
					autoEl:"div",
					layout:"form",
					columnWidth:0.33,
					defaults: {
						xtype:'textfield', width:VNodeCaseDelay.fieldWidth, disabled: true
					}
				}
			},
			items:[{
				items:[{
					items:[{
						fieldLabel:"延误工序",
						disabled:false, allowBlank:false,
						id: 'node_case_idx',
						xtype: 'Base_comboTree', hiddenName: 'nodeCaseIdx',
						returnField:[{
							widgetId:"real_begin_time",propertyName:"realBeginTime"
						},{
							widgetId:"plan_begin_time",propertyName:"planBeginTime"
						},{
							widgetId:"plan_end_time",propertyName:"planEndTime"
						},{
							widgetId:"node_idx",propertyName:"nodeIDX"
						},{
							widgetId:"work_plan_idx",propertyName:"workPlanIDX"
						}],
						selectNodeModel:'all', business: 'vJobProcessNode', rootText: '延误工序',
						listeners: {
							render: function(me) {
								me.setDisplayValue(VNodeCaseDelay.idx, VNodeCaseDelay.nodeName);
							}
						}
					}, {
						name:'planBeginTime', id: 'plan_begin_time',
						fieldLabel:"计划开始时间", xtype:'my97date', format:'Y-m-d H:i'
					}]
				}, {
					items:[{
						name:'delayType', fieldLabel:"延误类型", 
						disabled:false, allowBlank:false,
						xtype: 'EosDictEntry_combo',
        				hiddenName: 'delayType',
        				dicttypeid:'JXGC_WORK_SEQ_DELAY',			
        				displayField:'dictname',valueField:'dictid',
        				editable:true,
        				maxLength:50,hasEmpty:false
					}, {
						name:'planEndTime', id: 'plan_end_time',
						fieldLabel:"计划结束时间", xtype:'my97date', format:'Y-m-d H:i'
					}]
				}, {
					items:[{
						xtype: 'compositefield', fieldLabel: '延误时间', combineErrors: false, allowBlank:false, disabled:false,
						items: [{
							xtype: 'numberfield', id: 'ratedPeriod_H', name: 'ratedPeriod_h', width: 50, allowBlank:false,allowNegative:false,
							decimalPrecision : 0,
							 validator: function(value) {
								if (Ext.isEmpty(value)) {
									return null;
								}
								if (isNaN(parseInt(value)) || parseInt(value) < 0) {
									return "请输入正整数";
								}
								var mValue = Ext.getCmp('ratedPeriod_M').getValue();
								if (Ext.isEmpty(value) && Ext.isEmpty(mValue)) {
									return '工期不能为空'
								} else {
									if (value.length > 4) {
										return "该输入项最大长度为4";
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
							xtype: 'numberfield', id: 'ratedPeriod_M', name: 'ratedPeriod_m', width: 50, 
							decimalPrecision : 0,
							allowBlank:false,allowNegative:false, validator: function(value) {
								if (Ext.isEmpty(value)) {
									return null;
								}
								//判断传入的是不是数字
								if (isNaN(parseInt(value)) || parseInt(value) < 0) {
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
					}, {
						name:'realBeginTime', id: 'real_begin_time',
						fieldLabel:"实际开始时间", xtype:'my97date', format:'Y-m-d H:i'
					}]
				}, {
					columnWidth:1,
					items:[{
						name:'delayReason', xtype:"textarea", fieldLabel:"延误原因", allowBlank:false, disabled:false,
						anchor:'80%',
						width:417, maxLength:200
					}]
				}]
			}, {
				xtype:'hidden', fieldLabel:'节点主键', name: 'nodeIDX', id:'node_idx'
			}, {
				xtype:'hidden', fieldLabel:'计划主键', name: 'rdpIDX', id:'work_plan_idx'
			}, {
				xtype:'hidden', fieldLabel:'idx主键', name: 'idx'
			}]
		}, {
			region:"center",
			layout: 'fit',
			items: [VNodeCaseDelay.grid]
		}],
		listeners: {
			show: function(me, eOpts) {
				var form = Ext.getCmp('base_form');
				var trainInfo = VNodeCaseDelay.trainInfo.split('_');
				form.find('name', 'trainTypeShortName')[0].setValue(trainInfo[0]);
				form.find('name', 'trainNo')[0].setValue(trainInfo[1]);
				form.find('name', 'repairClassName')[0].setValue(trainInfo[2]);
				form.find('name', 'repairtimeName')[0].setValue(trainInfo[3]);
				// 用于标识是否执行了“延误原因”的编辑
				VNodeCaseDelay.isEdited = false;
			},
			hide: function(me, eOpts) {
				var form = Ext.getCmp('edit_form').getForm();
				form.reset();
				Ext.getCmp('ratedPeriod_H').setValue('');
				Ext.getCmp('ratedPeriod_M').setValue('');
				// 重新加载Vis视图
				// Modified by hetao on 2016-03-29 如果没有执行“延误原因”的编辑，则不刷新主页面数据源
				if (VTrainWorkPlan.store && VNodeCaseDelay.isEdited) VTrainWorkPlan.store.reload();
			}
		},
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: function() {
				var form = Ext.getCmp('edit_form').getForm();
				// 验证表单输入是否合法
				if (!form.isValid()) {
					return;
				}
				//
				Ext.getCmp('real_begin_time').enable();
				Ext.getCmp('plan_begin_time').enable();
				Ext.getCmp('plan_end_time').enable();
				var values = form.getValues();
				//
				Ext.getCmp('real_begin_time').disable();
				Ext.getCmp('plan_begin_time').disable();
				Ext.getCmp('plan_end_time').disable();
				// 延误时间的特殊处理
				delete values.ratedPeriod_h;
				delete values.ratedPeriod_m;
				var delayTime = 0;
				var ratedPeriod_H = Ext.getCmp('ratedPeriod_H').getValue();
				var ratedPeriod_M = Ext.getCmp('ratedPeriod_M').getValue();
				if (!Ext.isEmpty(ratedPeriod_H)) {
					delayTime += parseInt(ratedPeriod_H) * 60;
				}
				if (!Ext.isEmpty(ratedPeriod_M)) {
					delayTime += parseInt(ratedPeriod_M);
				}
				if (delayTime <= 0) {
					MyExt.Msg.alert('延误时间不能为空！');
					return;
					Ext.getCmp('ratedPeriod_M').validateValue();
				}
				values.delayTime = delayTime;
				// Ajax请求
				VNodeCaseDelay.loadMask.show();
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
					url: ctx + '/nodeCaseDelay!saveOrUpdate.action',
					jsonData: Ext.encode(values),
					//请求成功后的回调函数
				    success: function(response, options){
				        if(VNodeCaseDelay.loadMask)    VNodeCaseDelay.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功   
				        	// 回显idx主键字段
//					        	var entity = result.entity;
//					        	Ext.getCmp('edit_form').find('name', 'idx')[0].setValue(entity.idx);
				        	// 重新加载表格数据
				        	VNodeCaseDelay.grid.store.load();
				        	VNodeCaseDelay.isEdited = true;
				            alertSuccess();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    }
				}));
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})
	// 显示工序延误编辑窗口
	VNodeCaseDelay.showEditWin = function(idx, nodeName, workPlanIDX) {
		VNodeCaseDelay.idx = idx;
		VNodeCaseDelay.nodeName = nodeName;
		VNodeCaseDelay.workPlanIDX = workPlanIDX;
		VNodeCaseDelay.trainInfo = arguments[6];
		// 加载工序延误记录表格
		VNodeCaseDelay.grid.store.load();
		// 设置“延误工序”字段值
		Ext.getCmp('node_case_idx').setDisplayValue(idx, nodeName);
		Ext.getCmp('node_case_idx').rootId = idx;
		Ext.getCmp('node_case_idx').rootText = nodeName;
		// Modified by hetao at 2015-09-16 15:00 修改选择子节点填写延误原因后，不能选择根节点填写的缺陷
		var root = Ext.getCmp('node_case_idx').tree.getRootNode();
		root.attributes.realBeginTime = parseInt(arguments[3]);
		root.attributes.planBeginTime = parseInt(arguments[4]);
		root.attributes.planEndTime = parseInt(arguments[5]);
		root.attributes.nodeIDX = arguments[7];
		root.attributes.workPlanIDX = workPlanIDX;
		
		Ext.getCmp('node_case_idx').queryParams = {
			workPlanIDX: workPlanIDX
		};
		// 初始化值设置
		var form = Ext.getCmp('edit_form');
		form.find('name', 'realBeginTime')[0].setValue(new Date(parseInt(arguments[3])).format('Y-m-d H:i'));
		form.find('name', 'planBeginTime')[0].setValue(new Date(parseInt(arguments[4])).format('Y-m-d H:i'));
		form.find('name', 'planEndTime')[0].setValue(new Date(parseInt(arguments[5])).format('Y-m-d H:i'));
		form.find('name', 'nodeIDX')[0].setValue(arguments[7]);
		form.find('name', 'rdpIDX')[0].setValue(arguments[8]);
		// 默认计算延误时间(算法：以当前时间 - 计划结束时间)
		var now = new Date().getTime();
		if (now > parseInt(arguments[5]) && arguments[9] == 'yyq') {
			VNodeCaseDelay.setDelayTime(Math.ceil((now - parseInt(arguments[5]))/(1000 * 60)));
		}
		VNodeCaseDelay.editWin.show();
	}
	
})