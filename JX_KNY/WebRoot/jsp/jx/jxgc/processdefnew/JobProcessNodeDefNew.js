/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobProcessNodeDef');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	JobProcessNodeDef.labelWidth = 100;
	JobProcessNodeDef.fieldWidth = 140;
	
	JobProcessNodeDef.isRender = false;
	JobProcessNodeDef.treePath = "";
	
	JobProcessNodeDef.processIDX = "";
	JobProcessNodeDef.parentIDX = "ROOT_0";
	
	JobProcessNodeDef.isSaveAndAdd = false;			// 是否是保存并新增的标识
	JobProcessNodeDef.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请稍候..."});
	// ['圆形','circle'],['圆柱形','database'],['椭圆形','llipse'],
	var showFlowData =[['正方形','square'],['凌形','diamond'],['圆点形','dot'],['星形','star'],['三角形','triangle'],['下三角形','triangleDown'],
						['盒形','box'],['文本框','text']];
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 重新加载【作业节点树】
	JobProcessNodeDef.reloadTree = function(path) {
        JobProcessNodeDef.tree.root.reload(function() {
        	if (!path) {
				JobProcessNodeDef.tree.getSelectionModel().select(JobProcessNodeDef.tree.root);
        	}
    	});
        if (path == undefined || path == "" || path == "###") {
			JobProcessNodeDef.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        JobProcessNodeDef.tree.expandPath(path);
	        JobProcessNodeDef.tree.selectPath(path);
        }
	}
	// 新增函数
	JobProcessNodeDef.addFn =  function(isLeaf) {
		var jobProcessNodeDef = {};
		jobProcessNodeDef.processIDX = JobProcessNodeDef.processIDX;
		jobProcessNodeDef.parentIDX = JobProcessNodeDef.parentIDX;
		jobProcessNodeDef.nodeName = "<新节点>";
		jobProcessNodeDef.isLeaf = isLeaf;
		
		// 指定插入位置的新增功能,如果选择了一条（或多条）记录，则在已选择记录的第一条（从上往下）之前新增记录
		var sm = JobProcessNodeDef.grid.getSelectionModel();
		if (sm.getCount() > 0) {
			var firstSlectedRecord = sm.getSelections()[0];
			jobProcessNodeDef.seqNo = firstSlectedRecord.get('seqNo');
		}
		
		// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/jobProcessNodeDef!saveOrUpdate.action',
            jsonData: jobProcessNodeDef,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载【作业节点表格】
                    JobProcessNodeDef.grid.store.reload();
                	// 重新加载【作业节点树】
                    JobProcessNodeDef.reloadTree(JobProcessNodeDef.treePath);
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
	}
	// 手动排序 
    JobProcessNodeDef.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/jobProcessNodeDef!moveOrder.action',
			params: {idx: idx, orderType: orderType},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload({
		            	callback: function(){
				            // 重新加载树
		            		/** Modified by hetao at 2015-08-24 【来源20150812检修培训】作业流程维护，任务节点移动，目前移动节点会刷新整个任务树。建议上移或者下移任务节点，只在“下级作业节点”页签上动态更新顺序，不刷新整个维护页面。  */
				            JobProcessNodeDef.reloadTree(JobProcessNodeDef.treePath);
		            	}
		            });
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    }
    // 自动设置【流程编码】字段值
	JobProcessNodeDef.setProcessNodeCodeFn = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: CODE_RULE_JOB_PROCESS_NODE_CODE},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					JobProcessNodeDef.saveForm.find('name', 'nodeCode')[0].setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	/** **************** 定义全局函数结束 **************** */
    
    /** ************** 定义节点编辑表单开始 ************** */
	JobProcessNodeDef.saveForm = new Ext.form.FormPanel({
		padding: "10px", frame: true, labelWidth: JobProcessNodeDef.labelWidth,
		layout:"column",
		defaults: {
			layout:"form",
			columnWidth:0.5
		},
		items:[{
			defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				name:"nodeName", fieldLabel:"节点名称"
			},{
				allowBlank: true,
				xtype: 'Base_combo',hiddenName: 'workCalendarIDX', fieldLabel: '日历',
				entity:'com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo',
				fields: ["idx","calendarName"],displayField:'calendarName',valueField:'idx',
		    	queryHql: 'from WorkCalendarInfo where recordStatus = 0'
			}, {
				xtype: 'compositefield', id:'ratedWorkMinutes_id',fieldLabel: '工期', combineErrors: false, 
				items: [{
					xtype: 'numberfield', id: 'ratedPeriod_H', name: 'ratedPeriod_h',  width: 60, validator: function(value) {
						if (Ext.isEmpty(value)) {
							return null;
						}
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
						if (Ext.isEmpty(value)) {
							return null;
						}
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
			}, {
				xtype: 'compositefield', id:'nodeStartTime_id',fieldLabel: '开始时间', combineErrors: false, 
				items: [{
					xtype: 'label',
					text: '第',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {
					xtype: 'numberfield', id: 'startDay', name: 'startDay', value:1, width: 45, validator: function(value) {
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
			defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				name:"nodeCode", fieldLabel:"节点编码"
			},{    			
		            	xtype: 'radiogroup',
			            fieldLabel: '启动模式',
			            name: 'planMode',
			            items: [
			                { boxLabel: '定时',inputValue: PLANMODE_TIMER, name: 'planMode', checked: true
			                },
			                { boxLabel: '手动',inputValue: PLANMODE_MUNAUL, name: 'planMode'
//			                },  
//			                { boxLabel: '自动',inputValue: PLANMODE_AUTO, name: 'planMode'
			                }  
			            ]
				}, {
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
		}, {
			columnWidth:0.5,
			items:[{
				xtype:"my97date", id: 'relayTime_id',  name:"relayTime", fieldLabel:"超时时间",width: 80, format: "H:i",
				        	my97cfg: {dateFmt:"HH:mm"}		
			}]
		}, {
			layout:"form",
			columnWidth:0.5,
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
		}, {
			columnWidth:1,
			items:[{
				xtype:"textarea", name:"nodeDesc", fieldLabel:"节点描述", maxLength:500, anchor:"97%", height: 55
			}]
		}, {
			// 【作业节点】保存表单的隐藏字段
			columnWidth:1,
			defaultType:'hidden',
			items:[
				{ fieldLabel:"idx主键", name:"idx" },
				{ fieldLabel:"顺序号", name:"seqNo" },
				{ fieldLabel:"是否子节点", name:"isLeaf", value: IS_LEAF_YES },
				{ fieldLabel:"作业流程主键", name:"processIDX" },
				{ fieldLabel:"上级作业节点主键", name:"parentIDX" }
			]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: function() {
				JobProcessNodeDef.isSaveAndAdd = false;
				JobProcessNodeDef.grid.saveFn();
			}
		}, {
			text: '保存并新增', iconCls: 'addIcon', handler: function() {
				JobProcessNodeDef.isSaveAndAdd = true;
				JobProcessNodeDef.grid.saveFn();
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	/** ************** 定义节点编辑表单结束 ************** */
	
	JobProcessNodeDef.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jobProcessNodeDef!pageList.action',             //装载列表数据的请求URL
	    saveURL: ctx + '/jobProcessNodeDef!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/jobProcessNodeDef!logicDelete.action',            //删除数据的请求URL
	    saveForm: JobProcessNodeDef.saveForm,
	    saveWinWidth: 650,        
    	saveWinHeight: 500,    
    	storeAutoLoad: false,
    	viewConfig:null,
	    tbar:[/*{
	    	text:"新增父节点", iconCls:"addIcon", handler: function () {JobProcessNodeDef.addFn(IS_LEAF_NO)}
	    },{
	    	text:"新增子节点", iconCls:"addIcon", handler: function () {JobProcessNodeDef.addFn(IS_LEAF_YES)}
	    }, */'delete', '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				JobProcessNodeDef.moveOrder(JobProcessNodeDef.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				JobProcessNodeDef.moveOrder(JobProcessNodeDef.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				JobProcessNodeDef.moveOrder(JobProcessNodeDef.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				JobProcessNodeDef.moveOrder(JobProcessNodeDef.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'流程主键', dataIndex:'processIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'上级主键', dataIndex:'parentIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'顺序号', dataIndex:'seqNo', width: 60, hidden:false, editor:{ xtype:'numberfield', maxLength:8 }
		},{
			header:'节点编码', dataIndex:'nodeCode', width: 120, editor:{  maxLength:50 }
		},{
			header:'节点名称', dataIndex:'nodeName', width: 200, editor:{  maxLength:100 }
		},{
			header:'额定工期', dataIndex:'ratedWorkMinutes', width: 120, editor:{ xtype:'numberfield', maxLength:12 }, 
			renderer: function(value){
				if (!Ext.isEmpty(value)) {
					return formatTimeForHours(value, 'm');
				}
			}
		},{
			header:'开始时间', dataIndex:'startTime', hidden:true, width: 120,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				if (!Ext.isEmpty(value)) {
					return "第" + record.get('startDay')+ "天 " + value;
				}
			}
		},{
			header:'结束时间', dataIndex:'endTime',  hidden:true, width: 120,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				if (!Ext.isEmpty(value)) {
					return "第" + record.get('endDay')+ "天 " + value;
				}
			}
		},{
			header:'开始天数', dataIndex:'startDay', width: 120,hidden:true, editor:{ xtype:'numberfield', maxLength:12 }			
		},{
			header:'结束天数', dataIndex:'endDay', width: 120,hidden:true, editor:{ xtype:'numberfield', maxLength:12 }
		},{
			header:'超时时间', dataIndex:'relayTime', hidden:true, editor:{  maxLength:50 }
		},{
			header:'日历', dataIndex:'workCalendarIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'计划模式', dataIndex:'planMode', hidden:true, editor:{  maxLength:50 }
		},{
			header:'节点描述', dataIndex:'nodeDesc', width: 460, editor:{  maxLength:1000 }
		},{
			header:'是否叶子节点', dataIndex:'isLeaf', hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
		},{
			header:'前置节点主键', dataIndex:'preNodeIDX', hidden:true, editor:{ disabled: true }
		},{
			header:'前置节点(名称)', dataIndex:'preNodeName', width:70, hidden:true,  editor:{ disabled: true }
		},{
			header:'前置节点(序号)', dataIndex:'preNodeSeqNo', width:125, hidden:false,  editor:{ disabled: true }
		},{
			header:'显示图标样式', dataIndex:'showFlag', width: 120,hidden:true, editor:{ xtype:'numberfield', maxLength:12 }
		}],
		
		// 删除成功后的函数处理
		afterDeleteFn: function(){ 
        	// 重新加载【作业节点树】
            JobProcessNodeDef.reloadTree(JobProcessNodeDef.treePath);
		},
		
		// 保存成功后的函数处理
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 重新加载【作业节点树】
            JobProcessNodeDef.reloadTree(JobProcessNodeDef.treePath);
            // 回显字段值
            var entity = result.entity;
            
            // 启用前后置节点关系表格工具栏
            JobProcessNodeRelDef.grid.getTopToolbar().enable();
	    	JobProcessNodeRelDef.nodeIDX = entity.idx;				// 设置作业节点主键
	    	
	    	// 显示扩展配置选项卡
	    	this.saveWin.find('xtype', 'tabpanel')[0].unhideTabStripItem(1);
	    	// 设置扩展配置的节点主键
	    	JobNodeExtConfigDef.nodeIDX = entity.idx;
	    	if (JobProcessNodeDef.isSaveAndAdd) {
			    this.saveForm.getForm().reset();
		    	this.afterShowSaveWin();
	    	} else {
	            this.saveForm.find('name', 'idx')[0].setValue(entity.idx);
	            this.saveForm.find('name', 'seqNo')[0].setValue(entity.seqNo);
	    	}
	    },
	    
	    afterShowEditWin: function(record, rowIndex){
	    	
	    	
	    	// 设置“工期”字段
	    	var ratedWorkMinutes = record.get('ratedWorkMinutes');
	    	if (!Ext.isEmpty(ratedWorkMinutes) && ratedWorkMinutes > 0) {
				var ratedPeriod_h = Math.floor(ratedWorkMinutes/60);
				var ratedPeriod_m = ratedWorkMinutes%60;
				// 设置 额定工期_时
				if (ratedPeriod_h > 0)
					Ext.getCmp("ratedPeriod_H").setValue(ratedPeriod_h);
				// 设置 额定工期_分
				if (ratedPeriod_m > 0)
					Ext.getCmp("ratedPeriod_M").setValue(ratedPeriod_m);
	    	}
	    	
	    	/** Modified by hetao at 2015-08-21，取消如果是父节点，“额定工期”字段不能编辑的限制 */
	    	// 如果是子节点，则不能对“额定工期”字段进行编辑
	    	if (record.get('parentIDX') == "ROOT_0") { 
	    		this.saveWin.setTitle("编辑节点");
	    		Ext.getCmp('nodeStartTime_id').hide();
	    		Ext.getCmp('nodeEndTime_id').hide();
	    		Ext.getCmp('relayTime_id').hide();
    			Ext.getCmp('ratedWorkMinutes_id').show();  
    		 			// 显示扩展配置选项卡
	    		this.saveWin.find('xtype', 'tabpanel')[0].unhideTabStripItem(0);
    			this.saveWin.find('xtype', 'tabpanel')[0].setActiveTab(0);
	    	} else {
	    		this.saveWin.setTitle("编辑子节点");
	    		var startDay = record.get('startDay');
	    		var endDay = record.get('endDay');
//	    		if(Ext.isEmpty(startDay) || startDay <= 0){
//	    			Ext.getCmp('startDay').setValue(1);
//	    		}
//	    		if(Ext.isEmpty(endDay) || endDay <= 0){
//	    			Ext.getCmp('endDay').setValue(1);
//	    		}
	    		Ext.getCmp('nodeStartTime_id').show();
	    		Ext.getCmp('nodeEndTime_id').setValue(1);
	    		Ext.getCmp('nodeEndTime_id').show();
	    		Ext.getCmp('relayTime_id').show();
    			Ext.getCmp('ratedWorkMinutes_id').hide(); 
 				// 隐藏前后置关系
	    		this.saveWin.find('xtype', 'tabpanel')[0].hideTabStripItem(0);
	    		this.saveWin.find('xtype', 'tabpanel')[0].setActiveTab(1);		
	    	}
	    	
	    	// 如果之前没有保存流程编码字段，则在保存窗口显示后自动设置
	    	if (Ext.isEmpty(this.saveForm.find('name', 'nodeCode')[0].getValue())) {
	    		JobProcessNodeDef.setProcessNodeCodeFn();
	    	}
	    	
	    	// 初始化前置节点关系表格数据
	    	// 重新加载【节点编辑】节点前后置关系表格数据
	    	JobProcessNodeRelDef.processIDX = record.get('processIDX');						// 设置作业流程主键
	    	JobProcessNodeRelDef.nodeIDX = record.get('idx');								// 设置作业节点主键
	    	JobProcessNodeRelDef.parentIDX = record.get('parentIDX');						// 设置上级作业节点主键
	    	JobProcessNodeRelDef.grid.store.load();
	    	
	    	// 启用前后置节点关系表格工具栏
	    	JobProcessNodeRelDef.grid.getTopToolbar().enable();
	    	
	    	// 显示扩展配置选项卡
	    	this.saveWin.find('xtype', 'tabpanel')[0].unhideTabStripItem(1);
	    	// 设置扩展配置的节点主键
	    	JobNodeExtConfigDef.nodeIDX = record.get('idx');
	    	JobNodeExtConfigDef.loadFn(record.get('idx'));
	    	
	    	if (!Ext.isEmpty(record.get('workCalendarIDX'))) {			    
	                var cfg = {
				        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
	        			params: {infoIdx: record.get('workCalendarIDX')},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.defInfo != null) {
				                this.saveForm.getForm().findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
				            }
				        }
				    };
	    			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
				}
	    },
	    
	    // 重新保存方法，完善对“额定工期（分钟）”字段保存时的特殊处理
		beforeSaveFn: function(data){ 
			var ratedPeriod_h = data.ratedPeriod_h;
			var ratedPeriod_m = data.ratedPeriod_m;
			if(Ext.isEmpty(ratedPeriod_h) && Ext.isEmpty(ratedPeriod_m) && data.isLeaf == IS_LEAF_NO){
				MyExt.Msg.alert('【节点工期】必须为正整数！');
    			return false;
			}
			if(data.isLeaf == IS_LEAF_YES && (data.parentIDX != "ROOT_0"|| Ext.isEmpty(data.parentIDX)) && (Ext.isEmpty(data.startDay) || Ext.isEmpty(data.endDay)) ){
				MyExt.Msg.alert('【开始时间】或【结束时间】天数必须为正整数！');
    			return false;
			}
			data.ratedWorkMinutes = parseInt(ratedPeriod_h * 60);
			if (!Ext.isEmpty(ratedPeriod_m)) {
				 data.ratedWorkMinutes += parseInt(ratedPeriod_m);
			}
			delete data.ratedPeriod_h;
			delete data.ratedPeriod_m;
			// 删除扩展配置信息
			delete data.ext_train_status;
			delete data.ext_check_control;
			delete data.ext_check_ticket;
			return true; 
		},
		
//	    beforeGetFormData: function(){
//	    	if (this.saveForm.find('name', 'isLeaf')[0].getValue() == IS_LEAF_NO) {
//		    	Ext.getCmp('ratedPeriod_H').enable();
//	    		Ext.getCmp('ratedPeriod_M').enable();
//	    	}
//	    },
//	    
//	    afterGetFormData: function(){    
//	    	if (this.saveForm.find('name', 'isLeaf')[0].getValue() == IS_LEAF_NO) {
//		    	Ext.getCmp('ratedPeriod_H').disable();
//	    		Ext.getCmp('ratedPeriod_M').disable();
//	    	}
//	    },
//	    
	    createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
				width: 650, height: 500,
				closeAction: 'hide',
				layout: 'border',
				modal: false,
				defaults: {layout: 'fit', border: false},
				items: [{
					region: 'north', height: 260,defaults: {layout:'fit'},
					items: [{
						items: [{
							title: '基本信息',
							items: [JobProcessNodeDef.saveForm]
						}]
					}]
//					
				}, {
					region: 'center',				
					items: [{
						xtype: 'tabpanel', activeTab:0, 
						defaults: {layout:'fit'},
						items: [{
							title: '前置节点明细',
							items: [JobProcessNodeRelDef.grid]
						}, {
							title: '扩展配置',
							items: [JobNodeExtConfigDef.form]
						}]
					}]
				}],
				listeners: {
					hide: function() {
						// 隐藏前后置节点关系编辑窗口
						if (JobProcessNodeRelDef.grid.saveWin) JobProcessNodeRelDef.grid.saveWin.hide();
					}
				}
	        });
	    },
	    
	    afterShowSaveWin: function() {
	    	// 清空前后置节点关系表格数据
	    	if (JobProcessNodeRelDef.grid.store.getCount() > 0) {
	    		JobProcessNodeRelDef.grid.store.removeAll();
	    	}
	    	// 禁用前后置节点关系表格工具栏
	    	JobProcessNodeRelDef.grid.getTopToolbar().disable();
	    	
	    	// 自动设置流程作业节点编码
	    	JobProcessNodeDef.setProcessNodeCodeFn();
	    	// 设置保存表单默认值
	    	this.saveForm.find('name', 'parentIDX')[0].setValue(JobProcessNodeRelDef.parentIDX);
	    	this.saveForm.find('name', 'processIDX')[0].setValue(JobProcessNodeRelDef.processIDX);
	 
	    	// 重置扩展配置表单
	    	JobNodeExtConfigDef.form.getForm().reset();
	    	if(JobProcessNodeRelDef.parentIDX == "ROOT_0"){
	    		this.saveWin.setTitle("新增一级节点");
				Ext.getCmp('nodeStartTime_id').hide();
	    		Ext.getCmp('nodeEndTime_id').hide();
	    		Ext.getCmp('relayTime_id').hide();
				Ext.getCmp('ratedPeriod_H').setValue(8);  
    			Ext.getCmp('ratedWorkMinutes_id').show(); 
    			  	// 隐藏前后置关系
	    		this.saveWin.find('xtype', 'tabpanel')[0].unhideTabStripItem(0);
			   	// 隐藏扩展配置选项卡	 
	    		this.saveWin.find('xtype', 'tabpanel')[0].setActiveTab(0);
	    	} else {
	    		this.saveWin.setTitle("新增下级节点");
    			Ext.getCmp('startDay').setValue(1);
    			Ext.getCmp('endDay').setValue(1);
    			Ext.getCmp('nodeStartTime_id').show();
	    		Ext.getCmp('nodeEndTime_id').show();
	    		Ext.getCmp('relayTime_id').show();
    			Ext.getCmp('ratedWorkMinutes_id').hide();   
			   	// 隐藏前后置关系
	    		this.saveWin.find('xtype', 'tabpanel')[0].hideTabStripItem(0);
	    		this.saveWin.find('xtype', 'tabpanel')[0].setActiveTab(1);		
	    	}
    		// 隐藏前后置节点关系的保存窗口
    		if(JobProcessNodeRelDef.grid.saveWin && JobProcessNodeRelDef.parentIDX != "ROOT_0") JobProcessNodeRelDef.grid.saveWin.hide();
	    	// 重置扩展配置保存表单
	    	JobNodeExtConfigDef.resetFn();
	    	// 如果选择了一条记录，则在这条记录之前新增节点
	    	var sm = JobProcessNodeDef.grid.getSelectionModel();
	    	if (sm.getCount() > 0) {
	    		var firstRecord = sm.getSelections()[0];
	    		this.saveForm.find('name', 'seqNo')[0].setValue(firstRecord.get('seqNo'));
	    	}
	    	/** Modified by hetao at 2015-08-21 根据最新需求“日历字段需要设置为非必填，且默认为空” */
//	    	 自动设置日历
//	    	var cfg = {
//		        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
//		        success: function(response, options){
//		            var result = Ext.util.JSON.decode(response.responseText);
//		            if (result.defInfo != null) {
//		                this.saveForm.getForm().findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
//		            }
//		        }
//		    };
//		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    },
	    
	    beforeAddButtonFn: function() {
	    	var treeNode = JobProcessNodeDef.tree.getSelectionModel().getSelectedNode();
	    	if (treeNode.leaf) {
	    		if (TrainQR.grid.store.getCount() > 0) {
	    			MyExt.Msg.alert('该作业节点已经关联了检修记录卡，不能继续添加下级节点！');
	    			return false;
	    		}
	    		if (WorkStation.grid.store.getCount() > 0) {
	    			MyExt.Msg.alert('该作业节点已经关联了作业工位，不能继续添加下级节点！');
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    
	});
	// 设置默认排序字段为“顺序号（升序）”
	JobProcessNodeDef.grid.store.setDefaultSort("seqNo", "ASC");
	
	JobProcessNodeDef.grid.store.on('beforeload', function(){
		var searchParams = {};
		searchParams.processIDX = JobProcessNodeDef.processIDX;
		searchParams.parentIDX = JobProcessNodeDef.parentIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	/** **************** 定义作业流程节点树开始 **************** */
	// 选择一个流程节点时的初始化函数处理
	JobProcessNodeDef.initFn = function(node) {
		if (node == null || node.id == null) {
			return;
		}
    	// 获取当前节点的路径信息
    	JobProcessNodeDef.treePath = node.getPath();	
    	JobProcessNodeDef.parentIDX = node.id;
    	
    	JobProcessNodeRelDef.parentIDX = node.id;		// 设置上级作业节点主键
		var colModels = JobProcessNodeDef.grid.colModel;
    	if(JobProcessNodeRelDef.parentIDX == "ROOT_0"){
    		colModels.setHidden(8,false);
			colModels.setHidden(9,true);
			colModels.setHidden(10,true);
			colModels.setHidden(13,true);
    	}else{
    		colModels.setHidden(8,true);
			colModels.setHidden(9,false);
			colModels.setHidden(10,false);
			colModels.setHidden(13,false);
    	}
		// 如果是叶子节点，则仅能进行检修工艺卡和检修记录卡,检修用料定义的设置
    	if (node.leaf) {
			Ext.getCmp('tabpanel').hideTabStripItem(0);
			Ext.getCmp('tabpanel').unhideTabStripItem(1);
			Ext.getCmp('tabpanel').unhideTabStripItem(2);
			Ext.getCmp('tabpanel').unhideTabStripItem(3);
			Ext.getCmp('tabpanel').hideTabStripItem(4);
    		// 获取当前活动的Tab选项卡页
    		var activeTab = Ext.getCmp('tabpanel').getActiveTab();
    		// 如果当前活动的Tab选项卡页是“下级作业节点”页，则设置当前活动的Tab选项卡页为“检修工艺卡”页
    		if (activeTab.getId() == 'tabpanel_station') {
        		Ext.getCmp('tabpanel').setActiveTab(2);
    		} else {
        		Ext.getCmp('tabpanel').setActiveTab(1);
    		}	        		
    		// 设置【作业项目-作业工单】基础信息
    		TrainQR.nodeIDX = node.id;							// 作业流程节点主键
			TrainQR.grid.store.load();
	        		
    		// 设置【作业工位】基础信息
    		WorkStation.nodeIDX = node.id;							// 作业流程节点主键
			WorkStation.grid.store.load();

    		// 设置【配件用料信息】基础信息
    		JobNodeMatDef.nodeIDX = node.id;						// 作业流程节点主键
			JobNodeMatDef.grid.store.load();
			
    		// 重命名Tab - 只取序列号
			Ext.getCmp('tabpanel_station').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 作业工位");
			Ext.getCmp('tabpanel_project').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 机车检修记录卡");
			
		// 如果是树干节点，则仅能编辑下级作业节点信息,查询用料信息
    	} else if (!node.leaf) {
			Ext.getCmp('tabpanel').unhideTabStripItem(0);
			Ext.getCmp('tabpanel').hideTabStripItem(1);
			Ext.getCmp('tabpanel').hideTabStripItem(2);
			Ext.getCmp('tabpanel').hideTabStripItem(3);
			Ext.getCmp('tabpanel').unhideTabStripItem(4);
			Ext.getCmp('tabpanel').setActiveTab(0);
			
			// 重新加载作业节点表格数据
    		JobProcessNodeDef.grid.store.load();
    		JobNodeMatDef.gridQuery.store.load();
    		// 重命名Tab - 只取序列号
    		if (node.text.lastIndexOf(".") <= 0) {
        		Ext.getCmp('tabpanel_node').setTitle(node.text + " - 作业节点")
    		} else {
        		Ext.getCmp('tabpanel_node').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 下级作业节点")
    		}
    	}
	}
	
	// 机车检修作业流程节点树型列表
	JobProcessNodeDef.tree =  new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/jobProcessNodeDef!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    		JobProcessNodeDef.isRender = true;	// 增加一个数已经被渲染的标示字段，用以规避数还未渲染，就执行reload()和expand()方法导致的错误
	    		JobProcessNodeDef.reloadTree();
	    	},
	        click: function(node, e) {
//	        	JobProcessNodeDef.initFn(node);
	        },
	        beforeload:  function(node){
			    JobProcessNodeDef.tree.loader.dataUrl = ctx + '/jobProcessNodeDef!tree.action?parentIDX=' + node.id + '&processIDX=' + JobProcessNodeDef.processIDX;
			},
			load: function(node) {
//				JobProcessNodeDef.initFn(node);
			},
			movenode: function( tree, node, oldParent, newParent, index ) {
				// Ajax请求
				JobProcessNodeDef.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/jobProcessNodeDef!moveNode.action',
					params: {
						node: node.id,
						oldParent: oldParent.id,
						newParent: newParent.id,
						index: index
					},
					success: function(response, options){
						JobProcessNodeDef.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            // 重新加载树
//				            JobProcessNodeDef.reloadTree();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				   	failure: function(response, options){
				        JobProcessNodeDef.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
	    },
	    enableDD:true
	});
	
	// 选中的树节点变化时的事件监听函数
	JobProcessNodeDef.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
    	JobProcessNodeDef.initFn(node);
	});
	/** **************** 定义作业流程节点树结束 **************** */
	
	/** **************** 定义作业流程编辑窗口开始 **************** */
	JobProcessNodeDef.win = new Ext.Window({
		title:"作业流程编辑", maximized:true,
		layout:"border", closeAction:"hide",
		items:[{
			title : '<span style="font-weight:normal">作业节点树</span>',
			tbar: [{
				text: '新增下级', iconCls: 'addIcon', handler: function() {
					if(JobProcessNodeDef.grid.beforeAddButtonFn() == false)   return;
			        //判断新增删除窗体是否为null，如果为null则自动创建后显示
			        if(JobProcessNodeDef.grid.saveWin == null)  JobProcessNodeDef.grid.createSaveWin();
			        if(JobProcessNodeDef.grid.searchWin)  JobProcessNodeDef.grid.searchWin.hide();
			        if(JobProcessNodeDef.grid.saveWin.isVisible())    JobProcessNodeDef.grid.saveWin.hide();
			        if(JobProcessNodeDef.grid.beforeShowSaveWin() == false)   return;
			        
			        JobProcessNodeDef.grid.saveWin.setTitle('新增');
			        JobProcessNodeDef.grid.saveWin.show();
			        JobProcessNodeDef.grid.saveForm.getForm().reset();
			        JobProcessNodeDef.grid.saveForm.getForm().setValues(this.defaultData);
			        
			        JobProcessNodeDef.grid.afterShowSaveWin();
				}
			}, {
				text: '删除', iconCls: 'deleteIcon', handler: function() {
					var treeNode = JobProcessNodeDef.tree.getSelectionModel().getSelectedNode();
					if (null == treeNode) {
						MyExt.Msg.alert('尚未选一个作业流程节点！');
						return;
					}
					Ext.Msg.confirm('提示', '该操作将不能恢复，是否继续？', function(btn){
						if ('yes' == btn) {
							Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
								url: ctx + '/jobProcessNodeDef!logicDelete.action',
								params: {ids: [treeNode.id]},
								success: function(response, options){
							        if(self.loadMask)    self.loadMask.hide();
							        var result = Ext.util.JSON.decode(response.responseText);
							        if (result.errMsg == null) {       //操作成功     
							            alertSuccess();
							            JobProcessNodeDef.reloadTree();
							        } else {                           //操作失败
							            alertFail(result.errMsg);
							        }
							    }
							}));
						}
					});
				}
			}],
	        iconCls : 'icon-expand-all',
	        tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	JobProcessNodeDef.reloadTree(JobProcessNodeDef.treePath);
	            }
	        } ],
			region:"west", collapsible: true,
			width:279,
			layout:"fit",
			items:[JobProcessNodeDef.tree]
		}, {
			xtype:"tabpanel",
			id:"tabpanel",
			activeTab:0,
			region:"center",
			items:[{
				id:"tabpanel_node",
				title:"流程节点",
				layout:"fit",
				items:[JobProcessNodeDef.grid]
			}, {
				id:"tabpanel_project",
				title:"作业工单",
				layout:"border",
				defaults: {layout: 'fit', border: false},
				items:[{
					region: 'center',
					items: [TrainQR.grid]    // 对应的页面：RPToWS.js
				}]
			}, {
				id:"tabpanel_station",
				title:"作业工位",
				layout:"fit",
				items:[WorkStation.grid]
			}, {
				id:"tabpanel_mat",
				title:"检修用料",
				layout:"fit",
				items:[JobNodeMatDef.grid]
			}, {
				id:"tabpanel_matQuery",
				title:"检修用料查询",
				layout:"fit",
				items:[JobNodeMatDef.gridQuery]
			}],
			listeners : {
				render : function(){
					this.unhideTabStripItem(0);
					this.hideTabStripItem(1);
					this.hideTabStripItem(2);
					this.hideTabStripItem(3);
					this.hideTabStripItem(4);
					this.setActiveTab(0);
				}
			}
		}],
		buttonAlign:'center',
		buttons:[{
			text:'甘特图查看', iconCls: 'queryIcon', handler:function(){
				JobProcessGantt.processIDX = JobProcessNodeDef.processIDX;
	    		JobProcessGantt.win.show();
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}], 
		listeners : {
			show: function(window) {
				JobProcessNodeDef.parentIDX = "ROOT_0";
				JobProcessNodeDef.grid.store.load();
				JobProcessNodeDef.reloadTree();
				JobProcessNodeRelDef.processIDX = JobProcessNodeDef.processIDX;
				// 选择激活的panel
				Ext.getCmp('tabpanel').unhideTabStripItem(0);
				Ext.getCmp('tabpanel').hideTabStripItem(1);
				Ext.getCmp('tabpanel').hideTabStripItem(2);
				Ext.getCmp('tabpanel').hideTabStripItem(3);
				Ext.getCmp('tabpanel').hideTabStripItem(4);
				Ext.getCmp('tabpanel').setActiveTab(0);
			},
			hide: function() {
				// 隐藏机车检修作业流程节点编辑窗口
				if (JobProcessNodeDef.grid.saveWin) JobProcessNodeDef.grid.saveWin.hide();
			}
		}
	});
	/** **************** 定义作业流程编辑窗口结束 **************** */
	
});