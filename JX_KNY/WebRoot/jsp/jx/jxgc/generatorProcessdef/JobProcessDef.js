/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobProcessDef');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	JobProcessDef.labelWidth = 100;
	JobProcessDef.fieldWidth = 250;
	JobProcessDef.serachParams = {};
	JobProcessDef.trainTypeIDX = "";
	JobProcessDef.isSaveAndAdd = false;
	
	JobProcessDef.trainShortName = "";			// 车型简称
	JobProcessDef.rcName = "";					// 修程名称
	
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 复选框多选查询函数处理
	JobProcessDef.checkQueryFn = function() {
		JobProcessDef.status = [-1];
		if(Ext.getCmp("status_xz").checked){
			JobProcessDef.status.push(STATUS_XZ);
		} 
		if(Ext.getCmp("status_qy").checked){
			JobProcessDef.status.push(STATUS_QY);
		} 
		if(Ext.getCmp("status_zf").checked){
			JobProcessDef.status.push(STATUS_ZF);
		} 
		JobProcessDef.grid.store.load();
	}
	
	// 更新记录状态的函数处理
	JobProcessDef.updateStatusFn = function(status) {
		var sm = JobProcessDef.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择一条记录！');
			return;
		}
		var records = sm.getSelections();
		var ids = [];
		for (var i = 0; i < records.length; i++) {
			if (status == STATUS_QY && records[i].get('status') != STATUS_XZ) {
				MyExt.Msg.alert('只可以启用状态为新增的作业流程，请重新选择！');
				return;
			}
			if (status == STATUS_ZF && records[i].get('status') != STATUS_QY) {
				MyExt.Msg.alert('只可以作废状态为启用的作业流程，请重新选择！');
				return;
			}
			ids.push(records[i].get('idx'));
		}
		var tip = null;
		if (status == STATUS_ZF) {
			tip = "作废后将不可以恢复，是否继续？";
		} else {
			tip = "启用后将不可以编辑，是否继续？";
		}
		Ext.Msg.confirm('提示', tip, function(btn){
			if ('yes' == btn) {
				// Ajax请求
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
					scope: JobProcessDef.grid,
					url: ctx + '/jobProcessDef!updateStatus.action', 
					params: {ids: ids, status: status}
				}));
			}
		});
	}
	
	// 配置流程节点
	JobProcessDef.configNodeFn = function() {
		var sm = JobProcessDef.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择一条记录！');
			return;
		}
		if (sm.getCount() > 1) {
			MyExt.Msg.alert('请只选择一条记录进行流程节点设置');
			return;
		}
		var record = sm.getSelections()[0];
		if (record.get('status') == STATUS_ZF) {
			MyExt.Msg.alert('已作废的作业流程不可以进行流程节点设置');
			return;
		}
		JobProcessNodeDef.processIDX = record.get('idx');
		JobProcessNodeDef.processName = record.get('processName');
		JobProcessNodeDef.tree.root.setText(record.get('processName'));
		Ext.getCmp('tabpanel_node').setTitle(record.get('processName') + " - 作业节点")
		
		WorkSeqSearcher.pTrainTypeIDX = record.get('trainTypeIDX');
		WorkSeqSearcher.processIDX = record.get('idx');
		
		WorkStationSearcher.processIDX = record.get('idx');
		
		JobProcessNodeDef.win.show();
	}
	// 自动设置【流程编码】字段值
	JobProcessDef.setProcessCodeFn = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: CODE_RULE_JOB_PROCESS_CODE},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					JobProcessDef.saveForm.find('name', 'processCode')[0].setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义编辑表单开始 **************** */
	JobProcessDef.saveForm = new Ext.form.FormPanel({
		labelWidth: JobProcessDef.labelWidth,
		layout:"column", padding:"10px", frame: true,
		defaults: {
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
			defaults:{
				xtype: 'textfield', width: JobProcessDef.fieldWidth, maxLength: 50, allowBlank: false
			}
		},
		items:[{
			items:[{
				fieldLabel: "机组型号",
				id:"trainType_combJ",
		    	xtype: "Base_combo",
				hiddenName: "trainTypeIDX",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
		        fields:['idx','typeName','typeCode'],
		        queryParams: {'vehicleType':vehicleType},// 表示客货类型
			    displayField: "typeCode", valueField: "idx",
		        pageSize: 20, minListWidth: 200,
		        returnField: [{widgetId:"trainTypeShortName",propertyName:"typeCode"},{widgetId:"trainTypeName",propertyName:"typeName"}],
		        allowBlank:false,
		        editable:false,
				listeners : {
		        	"select" : function(combo, record, index) {   
		                //重新加载修程下拉数据
		                var rc_comb = Ext.getCmp("rc_comb");
		                rc_comb.reset();
		                rc_comb.clearValue();
		                rc_comb.getStore().removeAll();
		                rc_comb.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
		                rc_comb.cascadeStore();
	                 	//重置“修次”名称字段
	                 	Ext.getCmp('rcName').reset();
	                 	
	                 	// 选择【修程】后自动设置【流程名称】
	                 	JobProcessDef.trainShortName = record.get('shortName');			// 车型简称
						JobProcessDef.rcName = "";
		        	}
		    	}
			}, {
				xtype:"textfield",
				fieldLabel:"流程编码",
				name:"processCode"
			}, {
				xtype:"numberfield",
				fieldLabel:"额定工期(小时)",
				labelWidth: JobProcessDef.labelWidth + 30,
				name:"ratedWorkDay",
				vtype:"positiveInt",
				value:1,
				maxLength: 6
			}]
		}, {
			items:[{
				fieldLabel:'修程',
				id:"rc_comb",
				xtype: "Base_combo",
				hiddenName: "rcIDX",
				displayField: "xcName",
				valueField: "xcID",
				business: 'trainRC',
				queryParams: {'vehicleType':vehicleType},// 表示客货类型
				entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
				fields:['xcID','xcName'],
				emptyText:"- 请选择 -",
				pageSize: 0, 
				minListWidth: 200,
				allowBlank: false,
				returnField: [{widgetId:"rcName",propertyName:"xcName"}],    	
    			listeners: {
    				"beforequery" : function(){
    					//选择修次前先选车型
                		var trainTypeId =  Ext.getCmp("trainType_combJ").getValue();
    					if(trainTypeId == "" || trainTypeId == null){
    						MyExt.Msg.alert("请先选择上车车型！");
    						return false;
    					}
    					// 修程应该显示的是对应车型的承修修程（承修车型维护页面维护的）
    					this.queryParams = {TrainTypeIdx: Ext.getCmp("trainType_combJ").getValue(),"vehicleType":vehicleType}
                	},
                	select : function(combo, record, index) {
                		JobProcessDef.rcName = record.get('xcName');
                		var processNameField = this.findParentByType('form').find('name', 'processName')[0];
                		if (!Ext.isEmpty(JobProcessDef.rcName) && !Ext.isEmpty(JobProcessDef.trainShortName)) {
                			processNameField.setValue(JobProcessDef.trainShortName + JobProcessDef.rcName + '作业流程')
                		}
                	}
                }
	        		
			}, {
				xtype:"textfield",
				fieldLabel:"流程名称",
				name:"processName"
			}, {
				xtype: 'Base_combo',hiddenName: 'workCalendarIDX', fieldLabel: '日历',
				  entity:'com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo', allowBlank: false,
				  fields: ["idx","calendarName"],displayField:'calendarName',valueField:'idx',
				  queryHql: 'from WorkCalendarInfo where recordStatus = 0'
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"textarea",
				fieldLabel:"描述",
				name:"description",
				anchor:"95%",
				allowBlank: true,
				maxLength: 1000
			}]
		}, {
			columnWidth:1,
			defaults: { xtype: 'hidden' },
			items:[{
				fieldLabel:"idx主键", name:"idx"
			}, {
				fieldLabel:"车型名称", name:"trainTypeName"
			}, {
				fieldLabel:"车型简称", id:"trainTypeShortName", name:"trainTypeShortName"
			}, {
				fieldLabel:"修程名称", id:"rcName", name:"rcName"
			}, {
				fieldLabel:"流程状态", name:"status", value: STATUS_XZ
			}, {
				fieldLabel:"客货类型", name:"vehicleType", value: vehicleType
			}]
		}]
	})
	/** **************** 定义编辑表单结束 **************** */
	
	JobProcessDef.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jobProcessDef!pageQuery.action',                // 装载列表数据的请求URL
	    saveURL: ctx + '/jobProcessDef!saveJobProcessDefInfo.action',             // 保存数据的请求URL
	    deleteURL: ctx + '/jobProcessDef!logicDelete.action',            // 删除数据的请求URL
	    saveForm: JobProcessDef.saveForm,
	    tbar: ['add', 'delete', '-', {
	    	text: '启用', iconCls: '', handler: function() {
	    		JobProcessDef.updateStatusFn(STATUS_QY);
	    	}
	    }, {
	    	text: '作废', iconCls: '', handler: function() {
	    		JobProcessDef.updateStatusFn(STATUS_ZF);
	    	}
	    }, '-', {
	    	text: '设置流程节点', iconCls: 'configIcon', handler: JobProcessDef.configNodeFn
	    }, {
	    	text: '甘特图查看', iconCls: 'queryIcon', handler: function() {
//	    		MyExt.Msg.alert('功能完善中！请稍候');
	    		var sm = JobProcessDef.grid.getSelectionModel();
	    		if (sm.getCount() <= 0) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    		if (sm.getCount() > 1) {
	    			MyExt.Msg.alert('请只选择一条记录进行甘特图查看！');
	    			return;
	    		}
	    		JobProcessGantt.processIDX = sm.getSelections()[0].get('idx');
//	    		JobProcessGantt.win.setTitle(sm.getSelections()[0].get('processName'));
	    		JobProcessGantt.win.show();
	    	}
	    }, {
	    	text: '复制', iconCls: 'wrenchIcon', handler: function() {
	    		var sm = JobProcessDef.grid.getSelectionModel();
	    		if (sm.getCount() <= 0) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    		if (sm.getCount() > 1) {
	    			MyExt.Msg.alert('请只选择一条记录进行复制！');
	    			return;
	    		}
	    		var cfg = {
		        scope: this, url: ctx + '/jobProcessDefCopy!copyJobProcessDef.action',
		        params: {idx: sm.getSelections()[0].get('idx')},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.errMsg == null) {
		            	alertSuccess();
		                JobProcessDef.grid.store.load();
		            }else {
	                        alertFail(result.errMsg);
	                    }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    	}
	    }, '-' ,'状态：', {
	    	xtype:"checkbox", id:"status_xz", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: JobProcessDef.checkQueryFn
	    }, {
	    	xtype:"checkbox", id:"status_qy", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: JobProcessDef.checkQueryFn
	    }, {
	    	xtype:"checkbox", id:"status_zf", boxLabel:"作废&nbsp;&nbsp;&nbsp;&nbsp;", checked:false , handler: JobProcessDef.checkQueryFn
	    }, 'refresh', '->', '机组型号：', {
			id: "trainType_combo_s",
			emptyText: "选择车型快速检索",
			width: 180,
			xtype: "Base_combo",
		    business: 'trainVehicleType',
		    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
	        fields:['idx','typeName','typeCode'],
	        queryParams: {'vehicleType':vehicleType},// 表示客货类型
		    displayField: "typeCode", valueField: "idx",
	        pageSize: 20, minListWidth: 200,
	        disabled:false,
	        editable:true,
			listeners : {   
	        	"select" : function() {
	        		JobProcessDef.trainTypeIDX = this.getValue();
	        		// 重新加载表格数据
	        		JobProcessDef.grid.store.load();
	        	}   
	    	}
	    },'&nbsp;', {
	    	text: '重置', iconCls: 'resetIcon', handler: function() {
        		Ext.getCmp('trainType_combo_s').clearValue();
        		// 重新加载表格数据
        		JobProcessDef.trainTypeIDX = "";
        		JobProcessDef.grid.store.load();
	    	}
	    }],
	    viewConfig:null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'流程编码', dataIndex:'processCode', width: 140, editor:{  maxLength:50 }, renderer: function(value, metaData, record){
				var html = "";
	  			html = "<span><a href='#' onclick='JobProcessDef.configNodeFn()'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'流程名称', dataIndex:'processName', width: 180, editor:{  maxLength:50 }
		},{
			header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'机组型号', dataIndex:'trainTypeShortName', editor:{  maxLength:50 }
		},{
			header:'机组型号名称', dataIndex:'trainTypeName', hidden:true, editor:{  maxLength:50 }
		},{
			header:'修程主键', dataIndex:'rcIDX', hidden:true, editor:{  maxLength:8 }
		},{
			header:'修程名称', dataIndex:'rcName', editor:{  maxLength:50 }
		},{
			header:'额定工期（小时）', dataIndex:'ratedWorkDay', width: 140,  editor:{ xtype:'numberfield', maxLength:12 },
			renderer: function(value) {
				if (!Ext.isEmpty(value)) {
					return formatTime(value, 'h');
				}
			}
		},{
			header:'日历', dataIndex:'workCalendarIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'状态', dataIndex:'status', editor:{ xtype:'numberfield', maxLength:2 }, 
			renderer: function(value) {
				if (STATUS_XZ == value) return '新增';
				if (STATUS_QY == value) return '启用';
				if (STATUS_ZF == value) return '作废';
				return '错误！未知状态';
			}
		},{
			header:'流程描述', dataIndex:'description', width: 450,  editor:{  maxLength:1000, xtype:'textarea' }
		}],
		
		// 只可删除状态为新增的作业流程记录
		beforeDeleteFn: function(){  
	    	var sm = this.getSelectionModel();
	    	var records = sm.getSelections();
	    	for (var i = 0; i < records.length; i++) {
	    		if (STATUS_XZ != records[i].get('status')) {
	    			MyExt.Msg.alert('只可删除状态为新增的作业流程，请重新选择！');
	    			return false;
	    		}
	    	}
			return true;
		},
		
		// 自定义保存窗口
		createSaveWin: function(){
	        //计算查询窗体宽度
	        this.saveWin = new Ext.Window({
	        	title:"作业流程编辑",layout: 'fit',id:"topwin",maximized: true,
//	            width:600, height:340, 
	            plain:false, closeAction:"hide",  maximizable:true,
	            items: {
	                xtype:"tabpanel",activeTab:0, bodyBorder: false, 
	     	        items:[{
		            title:"基本信息", items:this.saveForm, frame: true,
		            buttonAlign:'center',
		            buttons: [{
		                text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
		                	JobProcessDef.isSaveAndAdd = false;
		                	this.saveFn();
		                }
		            }, {
		                text: "保存并新增", iconCls: "addIcon", scope: this, handler: function(){
		                	JobProcessDef.isSaveAndAdd = true;
		                	this.saveFn();
		                }
		            }, {
		                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
		            }]
	             }/*,{
	             	title:"下车配件清单",
					layout:"fit",
					items:[JobProcessPartsOffList.grid]
	             }*/]
              }
	      });
	   },    
	    // 新增时的初始化设置
	    afterShowSaveWin: function(){
	    	JobProcessPartsOffList.processIdx = "###";    //流程id
	    	JobProcessPartsOffList.grid.store.load();
	    	// 自动设置作业流程编码
	    	JobProcessDef.setProcessCodeFn();
	    	var cfg = {
		        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.defInfo != null) {
		                JobProcessDef.saveForm.getForm().findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    },
	    
	    // 编辑时的字段回显
	    afterShowEditWin: function(record, rowIndex) {
	    	JobProcessPartsOffList.processIdx = record.get('idx');
	    	JobProcessPartsOffList.grid.store.load();
	    	JobProcessPartsOffList.trainTypeIDX =record.get('trainTypeIDX');
	    	JobProcessPartsOffList.trainTypeShortName =record.get('trainTypeShortName');   	
	    	// 回显车型
	    	Ext.getCmp('trainType_combJ').setDisplayValue(record.get('trainTypeIDX'), record.get('trainTypeShortName'));
	    	// 回显修程
	    	Ext.getCmp('rc_comb').setDisplayValue(record.get('rcIDX'), record.get('rcName'));
	    	// 如果之前没有保存流程编码字段，则在保存窗口显示后自动设置
	    	if (Ext.isEmpty(this.saveForm.find('name', 'processCode')[0].getValue())) {
	    		JobProcessDef.setProcessCodeFn();
	    	}
	    	if (!Ext.isEmpty(record.get('workCalendarIDX'))) {			    
	                var cfg = {
				        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
	        			params: {infoIdx: record.get('workCalendarIDX')},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.defInfo != null) {
				                JobProcessDef.saveForm.getForm().findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
				            }
				        }
				    };
	    			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
				}
	    },
	    beforeSaveFn: function(data){ 
	    	data.trainTypeIDX = Ext.getCmp('trainType_combJ').value;
	    	return true; 
	    },
	    
	    // 针对【保存】和【保存并新增】操作后的特殊处理
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 【保存并新增】
	        if (JobProcessDef.isSaveAndAdd) {
	        	// 重置表单
	        	this.saveForm.getForm().reset();
	        	JobProcessDef.setProcessCodeFn();
	        	// 初始化状态-新增
	        	this.saveForm.find('name', 'status')[0].setValue(STATUS_XZ);
		    	// 重置车型
		    	Ext.getCmp('trainType_combJ').clearValue();
		    	// 重置修程
		    	Ext.getCmp('rc_comb').clearValue();
		    	JobProcessPartsOffList.processIdx = "###";
	    		JobProcessPartsOffList.grid.store.load();
	        // 【保存】
	        } else {
	        	JobProcessPartsOffList.processIdx = result.entity.idx;
	    		JobProcessPartsOffList.grid.store.load();
	        	this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	        }
   		}
	});
	
	JobProcessDef.grid.store.setDefaultSort('status', 'ASC'),
	JobProcessDef.grid.store.on('beforeload', function(){
		var searchParams = JobProcessDef.serachParams;
		searchParams.trainTypeIDX = JobProcessDef.trainTypeIDX;
		// 初始化只查询【启用】的检修作业流程
		if (undefined == JobProcessDef.status) {
			searchParams.status = [STATUS_XZ, STATUS_QY];
		} else {
			searchParams.status = JobProcessDef.status;
		}		
		// 删除空属性
	    searchParams = MyJson.deleteBlankProp(searchParams);		
		var whereList = [];
		for(prop in searchParams){
			if ('status' == prop) {
				whereList.push({propName:'status', propValues:searchParams[prop], compare:Condition.IN });
	     		continue;
			}
     		whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.EQ, stringLike: false});
		}
		whereList.push({propName:'vehicleType',propValue:vehicleType,compare:Condition.EQ,stringLike: false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	//页面适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:JobProcessDef.grid });
});