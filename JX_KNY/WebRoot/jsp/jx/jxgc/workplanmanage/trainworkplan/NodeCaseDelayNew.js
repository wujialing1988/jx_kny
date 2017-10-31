/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('NodeCaseDelayNew');
	
	/** **************** 定义全局变量开始 **************** */
	NodeCaseDelayNew.labelWidth = 100;
	NodeCaseDelayNew.fieldWidth = 140;
	
	NodeCaseDelayNew.idx = null;				// 机车检修作业流程节点主键
	NodeCaseDelayNew.nodeName = null;			// 机车检修作业流程节点名称
	NodeCaseDelayNew.workPlanIDX = null;		// 机车检修作业流程计划主键
	NodeCaseDelayNew.trainInfo = null;		// 机车检修作业流程计划车型信息，如：SS4B_0006_辅修_一次 
	NodeCaseDelayNew.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."});
	//获取数据字典列表
	var saveFields = [];
	    for (var i = 0; i < delayList.length; i++) {
	         var field = delayList[i];
	        var editor = {};  			// 定义检查项
	        editor.id = "delayType"+i;
	        editor.name = "delayTypeName"; 	
	        editor.xtype = "checkbox";
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.id.dictid;
	        editor.autoWidth = true;
	        saveFields.push(editor);	        
	    }
	/** **************** 定义全局变量结束 **************** */
	    
	// 上方表格【工序延误记录表格】定义
	NodeCaseDelayNew.grid = new Ext.yunda.Grid({
		toEditFn: Ext.emptyFn,
		storeAutoLoad: false,
		loadURL: ctx + '/nodeCaseDelay!findChildrenNew.action',
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
			dataIndex: 'delayTypeName', header: '延误类型', width: 100,renderer: function(v){
				var result = v ;
				if(Ext.isEmpty(v)){
					result = "未填写！" ;
				}
				return result;
			}
		}, {
			dataIndex: 'delayTime', header: '预计延误时长', width: 100, renderer: function(v){
				return formatTime(v, 'm');
			}
		}, {
			dataIndex: 'delayReason', header: '延误原因', width: 200
		}, {
			dataIndex: 'orgname', header: '所在班组', width: 160
		}, {
			dataIndex: 'empname', header: '填写人', width: 100
		}, {
			dataIndex: 'createTime', header: '填写时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}]
	});
	
	// Grid数据加载前的查询条件传递 
	NodeCaseDelayNew.grid.store.on('beforeload', function(){
		this.baseParams.nodeCaseIdx = NodeCaseDelayNew.idx;
		this.baseParams.workPlanIDX = NodeCaseDelayNew.workPlanIDX;
	});
	NodeCaseDelayNew.grid.store.on('load', function(me, records, options){
		// TODO
	});
	
	// Grid行选择事件监听，选择一条记录后，加载该工序延误记录进行编辑
	NodeCaseDelayNew.grid.getSelectionModel().on('rowselect', function(sm, rowIndex, r){
		Ext.getCmp('edit_form').getForm().reset();
		// 加载工序延误记录
		Ext.getCmp('edit_form').getForm().loadRecord(r);
		// 回显"延误工序"字段值
		Ext.getCmp('node_case_idx').setDisplayValue(r.get('nodeCaseIdx'), r.get('nodeName'));
		// 回显"延误时间"字段值
		NodeCaseDelayNew.setDelayTime(parseInt(r.get('delayTime')));
		// 回显"延误类型"字段值
		NodeCaseDelayNew.setDelayType((r.get('delayType')));
		
	});
	
	// 回显“延误时间”字段值
	NodeCaseDelayNew.setDelayTime = function(delayTime) {
		Ext.getCmp('ratedPeriod_H').setValue("");
		if (delayTime / 60 >= 1) {
			Ext.getCmp('ratedPeriod_H').setValue(Math.floor(delayTime / 60));
		}		
	}
	// “延误类型”字段拆分
	NodeCaseDelayNew.setDelayType = function(delayType) {
		if(Ext.isEmpty(delayType)){
			return ;
		}
		var delayTypeArray= delayType.split(",");
		var delayTypeGroup = Ext.getCmp('delayTypeGroup');
		for(var index in delayTypeArray){
			var id = delayTypeArray[index];
			NodeCaseDelayNew.checkedGroup(delayTypeGroup,id); 
		}
	}
	// 回显“延误类型”字段值
	NodeCaseDelayNew.checkedGroup = function(delayTypeGroup,id){
		if(delayTypeGroup.items && delayTypeGroup.items.items){
			var items = delayTypeGroup.items.items ;
			for(var i=0 ; i<items.length;i++){
				if(items[i].inputValue == id){
					delayTypeGroup.items.items[i].setValue(true) ;
				}
			}
		}
	}
	
	// 工序延误编辑窗口定义
	NodeCaseDelayNew.editWin = new Ext.Window({
		title:"工序延误",
		modal: true,
		closeAction: 'hide',
		width:900, height:450,
		layout:"border",
		items:[{
			id:'base_form',
			xtype:'form', region:'north', height:60, padding:"10px",
			labelWidth: NodeCaseDelayNew.labelWidth - 40,
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
					name:'repairClassTimeName', fieldLabel: '修程修次'
				}]
			}]
		},{
			id: 'edit_form',
			xtype:"form", region:"south", height:160, padding:"10px",
			labelWidth: NodeCaseDelayNew.labelWidth,
			frame:true,
			defaults: {
				xtype:"container",
				autoEl:"div",
				layout:"column",
				defaults: {
					xtype:"container",
					autoEl:"div",
					layout:"form",
					columnWidth:0.5,
					defaults: {
						xtype:'textfield', width:NodeCaseDelayNew.fieldWidth, disabled: true
					}
				}
			},
			items:[{
				items:[{
					items:[{
						fieldLabel:"延误工序",
						disabled:false, allowBlank:false, width: 200,
						id: 'node_case_idx',
						xtype: 'Base_comboTree', hiddenName: 'nodeCaseIdx',
						returnField:[{
				
							widgetId:"node_idx",propertyName:"nodeIDX"
						},{
							widgetId:"work_plan_idx",propertyName:"workPlanIDX"
						}],
						selectNodeModel:'all', business: 'vJobProcessNode', rootText: '延误工序',
						listeners: {
							render: function(me) {
								me.setDisplayValue(NodeCaseDelayNew.idx, NodeCaseDelayNew.nodeName);
							}
						}
//					}, {
//						name:'planBeginTime', id: 'plan_begin_time',
//						fieldLabel:"计划开始时间", xtype:'my97date', format:'Y-m-d H:i'
					}]
			
				}, {
					items:[{
						xtype: 'compositefield', fieldLabel: '预计延误时长', combineErrors: false, allowBlank:false, disabled:false,
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
//								var mValue = Ext.getCmp('ratedPeriod_M').getValue();
								if (Ext.isEmpty(value) && Ext.isEmpty(mValue)) {
									return '工期不能为空'
								} else {
									if (value.length > 4) {
										return "该输入项最大长度为4";
//									} else if (Ext.isEmpty(mValue) || parseInt(mValue) < 60){
//										Ext.getCmp('ratedPeriod_H').clearInvalid();
//										Ext.getCmp('ratedPeriod_M').clearInvalid();
									}
								}
							}
						}, {
							xtype: 'label',
							text: '小时',
							style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示					
						
						}]
					}]
				}, {
					columnWidth:1,
					items:[{
						xtype:'checkboxgroup', id :'delayTypeGroup', name: "delayTypeName", fieldLabel: '延误类型', anchor: "50%",
						disabled:false, allowBlank:false, items: saveFields
			
					}]
				}, {
					columnWidth:1,
					items:[{
						name:'delayReason', xtype:"textarea", fieldLabel:"延误原因",  disabled:false,
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
			items: [NodeCaseDelayNew.grid]
		}],
		listeners: {
			show: function(me, eOpts) {
				var form = Ext.getCmp('base_form');
				var trainInfo = NodeCaseDelayNew.trainInfo.split('_');
				form.find('name', 'trainTypeShortName')[0].setValue(trainInfo[0]);
				form.find('name', 'trainNo')[0].setValue(trainInfo[1]);
				form.find('name', 'repairClassTimeName')[0].setValue(trainInfo[2]+"|"+trainInfo[3]);
				// 用于标识是否执行了“延误原因”的编辑
				NodeCaseDelayNew.isEdited = false;
			},
			hide: function(me, eOpts) {
				var form = Ext.getCmp('edit_form').getForm();
				form.reset();
				Ext.getCmp('ratedPeriod_H').setValue('');
				// 重新加载Vis视图
				// Modified by hetao on 2016-03-29 如果没有执行“延误原因”的编辑，则不刷新主页面数据源
				if("undefined" != typeof(TrainWorkPlanEdit)&& (TrainWorkPlanEdit.store && NodeCaseDelayNew.isEdited)) TrainWorkPlanEdit.store.reload();
				if("undefined" != typeof(VTrainWorkPlan)&& (VTrainWorkPlan.store && NodeCaseDelayNew.isEdited)) VTrainWorkPlan.store.reload();
				if("undefined" != typeof(VTrainWorkPlanSearch)&& (VTrainWorkPlanSearch.store && NodeCaseDelayNew.isEdited)) VTrainWorkPlanSearch.store.reload();
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
				var values = form.getValues();
				var delayType="";
				var delayTypeName = new Array(values.delayTypeName);
				for(var i=0; i < delayTypeName.length;i++){
					delayType+= delayTypeName[i]+",";
				}
				delayType = delayType.substring(0,delayType.length-1);
				values.delayType = delayType;
				delete values.delayTypeName;
				// 延误时间的特殊处理
				delete values.ratedPeriod_h;
				var delayTime = 0;
				var ratedPeriod_H = Ext.getCmp('ratedPeriod_H').getValue();
				if (!Ext.isEmpty(ratedPeriod_H)) {
					delayTime += parseInt(ratedPeriod_H) * 60;
				}
				values.delayTime = delayTime;
				// Ajax请求
				NodeCaseDelayNew.loadMask.show();
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
					url: ctx + '/nodeCaseDelay!saveOrUpdate.action',
					jsonData: Ext.encode(values),
					//请求成功后的回调函数
				    success: function(response, options){
				        if(NodeCaseDelayNew.loadMask)    NodeCaseDelayNew.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功   
				        	// 回显idx主键字段
//					        	var entity = result.entity;
//					        	Ext.getCmp('edit_form').find('name', 'idx')[0].setValue(entity.idx);
				        	// 重新加载表格数据
				        	NodeCaseDelayNew.grid.store.load();
				        	NodeCaseDelayNew.isEdited = true;
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
	NodeCaseDelayNew.showEditWin = function(idx, nodeName, workPlanIDX) {
		NodeCaseDelayNew.idx = idx;
		NodeCaseDelayNew.nodeName = nodeName;
		NodeCaseDelayNew.workPlanIDX = workPlanIDX;
		NodeCaseDelayNew.trainInfo = arguments[6];
		// 加载工序延误记录表格
		NodeCaseDelayNew.grid.store.load();
		// 设置“延误工序”字段值
		Ext.getCmp('node_case_idx').setDisplayValue(idx, nodeName);
		Ext.getCmp('node_case_idx').rootId = idx;
		Ext.getCmp('node_case_idx').rootText = nodeName;
		// Modified by hetao at 2015-09-16 15:00 修改选择子节点填写延误原因后，不能选择根节点填写的缺陷
		var root = Ext.getCmp('node_case_idx').tree.getRootNode();
		root.attributes.nodeIDX = arguments[7];
		root.attributes.workPlanIDX = workPlanIDX;
		
		Ext.getCmp('node_case_idx').queryParams = {
			workPlanIDX: workPlanIDX
		};
		// 初始化值设置
		var form = Ext.getCmp('edit_form');
		form.find('name', 'nodeIDX')[0].setValue(arguments[7]);
		form.find('name', 'rdpIDX')[0].setValue(arguments[8]);
		// 默认计算延误时间(算法：以当前时间 - 计划结束时间)
		var now = new Date().getTime();
		if (now > parseInt(arguments[5]) && arguments[9] == 'yyq') {
			NodeCaseDelayNew.setDelayTime(Math.ceil((now - parseInt(arguments[5]))/(1000 * 60)));
		}
		NodeCaseDelayNew.editWin.show();
	}
	
})