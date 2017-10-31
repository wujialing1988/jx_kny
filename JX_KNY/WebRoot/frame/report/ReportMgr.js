

Ext.onReady(function() {
		
	Ext.namespace('ReportMgr');								// 定义名称空间
	
	/** **************** 定义全局变量开始 **************** */
	ReportMgr.labelWidth = 100;
	ReportMgr.fieldWidth = 140;
	
	ReportMgr.businessIDX = "";								// 报表业务关联 - 业务主键
	ReportMgr.deployCatalog = "";							// 报表打印模板 - 报表部署目录
	ReportMgr.parentIDX = "";								// 钻取模板的上级报表标主键
	ReportMgr.isSaveAndAdd = false;
	ReportMgr.isMainTab = true;								// 标识当前选项卡是在【主模板】tab页
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数结束 **************** */// 自动设置【报表标识】字段值
	ReportMgr.setIdentifierFn = function(formPanel) {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "REPORT_IDENTIFIER_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					formPanel.find('name', 'identifier')[0].setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	// 主模板基本信息表单【重置】按钮触发的函数处理
	ReportMgr.resetFn = function() {
		ReportMgr.mainForm.getForm().reset();
		// 设置“报表部署目录”
		ReportMgr.mainForm.find('name', 'deployCatalog')[0].setValue(ReportMgr.deployCatalog);
		
	}
	
	// 重新加载钻取模板表格
	ReportMgr.reloadSubGrid = function(parentIDX) {
		if (parentIDX) {
			ReportMgr.parentIDX = parentIDX;
		} else {
			parentIDX = "";
		}
		if (Ext.isEmpty(ReportMgr.parentIDX)) {
			ReportMgr.subGrid.store.removeAll();
			FileObject.grid.store.removeAll();
		} else {
			ReportMgr.subGrid.store.load();
		}
	}
	// 打印预览
	ReportMgr.printerPreviewFn = function(idx) {
		// Ajax请求
		Ext.Ajax.request({
			url: ctx + '/printerModule!getModelForPreview.action',
			params:{idx: idx},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	var entity = result.entity
		        	var deployCatalog = entity.deployCatalog;		// 报表部署目录
					var displayName = entity.displayName;			// 报表显示名称
					var deployName = entity.deployName;				// 报表部署名称
					while(deployCatalog.indexOf('.') >= 0) {
						deployCatalog = deployCatalog.replace('.', '/');
					}
					var reportUrl = "/" + deployCatalog + "/" + deployName;
					
					var url = reportUrl + "?ctx=" + ctx.substring(1);
					window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(url)+"&title=" + encodeURI(displayName));
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	/** **************** 定义全局函数开始 **************** */
	
	/** **************** 定义主模板表单开始 **************** */
	ReportMgr.mainForm = new Ext.form.FormPanel({
		style:'padding: 10px 10px 0 10px;',
		
		labelWidth:ReportMgr.labelWidth, border:false,
		labelAlign:"left",
		layout:"column",
		defaults:{
			xtype:"panel", layout:"form", columnWidth:0.5,
			defaults:{
				anchor:"90%", xtype:"textfield", allowBlank:false, maxLength:40
			}
		},
		items:[{
			items:[{
				name:'idx',
				fieldLabel:"IDX主键", hidden:true,
				allowBlank:true
			}, {
				name:'deployName',
				fieldLabel:"报表部署名称",
				vtype:'fineReport'
			}, {
				name:'displayName',
				fieldLabel:"报表显示名称"
			}]
		}, {
			items:[{
				name:'parentIDX',
				fieldLabel:"上级报表主键", hidden:true,
				allowBlank:true
			}, {
				name:'deployCatalog',
				fieldLabel:"报表部署目录", hidden:true, disabled:true
			}, {
				name:'identifier',
				fieldLabel:"报表标识码"
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"textarea",
				name:'moduleDesc',
				fieldLabel:"报表描述",
				maxLength:200,
				allowBlank:true
			}, {
				name:'editable',
				fieldLabel:"是否可编辑", hidden:true,
				value:'F'
			}]
		}, {
			items:[{
				name:'latestUpdateTime',
				xtype:'my97date', format:'Y-m-d H:i:s', initNow:false,
				fieldLabel:"最近更新时间", allowBlank:true,
				disabled:true
			}]
		}, {
			items:[{
				name:'latestDeployTime',
				xtype:'my97date', format:'Y-m-d H:i:s',initNow:false,
				fieldLabel:"最近部署时间", allowBlank:true,
				disabled:true
			}]
		}],
		buttonAlign:'center',
		buttons:[{
			text:'保存', iconCls:'saveIcon', handler: function() {
				// 获取表单面板
				var formPanel = this.findParentByType('form');
				var form = formPanel.getForm();
				if (!form.isValid()) {
					return;
				}
				// 获取数据前启用“最近更新时间”、“最近部署时间”、“报表部署目录”字段
				formPanel.find('name', 'latestUpdateTime')[0].enable();
				formPanel.find('name', 'latestDeployTime')[0].enable();
				formPanel.find('name', 'deployCatalog')[0].enable();
				// 获取表单数据
				var data = form.getValues();
				// 获取数据前禁用“最近更新时间”、“最近部署时间”、“报表部署目录”字段
				formPanel.find('name', 'latestUpdateTime')[0].disable();
				formPanel.find('name', 'latestDeployTime')[0].disable();
				formPanel.find('name', 'deployCatalog')[0].disable();
				// Ajax请求
				self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/printerModule!saveOrUpdate.action',
					jsonData: data,
					params: {businessIDX: ReportMgr.businessIDX},
					scope: formPanel,
					//请求成功后的回调函数
				    success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            var entity = result.entity;
				            
				            // 回显idx主键字段
				            this.find('name', 'idx')[0].setValue(entity.idx);
				            // 设置报表文件表格的打印模板主键
				            FileObject.printerModuleIDX = entity.idx;
				            // 设置钻取模板的上级模板主键
				            ReportMgr.parentIDX = entity.idx;
				            
				            // 回显主模板的“报表部署名称”字段
				            this.find('name', 'deployName')[0].setValue(entity.deployName)
				            // 回显主模板的“最近更新日期”字段
				            this.find('name', 'latestUpdateTime')[0].setValue(new Date(entity.latestUpdateTime).format('Y-m-d H:i:s'))
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
		}, {
			text:'重置', iconCls:'resetIcon', hidden:true, handler: ReportMgr.resetFn
		}]
						
	});
	/** **************** 定义主模板表单结束 **************** */
	
	/** **************** 定义钻取模板保存表单开始 **************** */
	ReportMgr.saveForm = new Ext.form.FormPanel({
		border:false, baseCls:'x-plain',
		labelWidth:ReportMgr.labelWidth,
		layout:'column',
		padding:'10px',
		defaults:{
			xtype:"container",
			autoEl:"div",
			layout:"form",
			columnWidth:0.5,
			defaultType:"textfield",
			defaults:{
			}
		},
		items:[{
			items:[{
				fieldLabel:"报表部署名称", maxLength:40,
				allowBlank: false,
				name:"deployName",
				anchor:"90%",
				vtype:'fineReport'
			}, {
				fieldLabel:"报表显示名称", maxLength:40,
				allowBlank: false,
				name:"displayName",
				anchor:"90%"
			}]
		}, {
			items:[{
				fieldLabel:"报表标识码",	
				allowBlank: false,
				maxLength:40,
				name:'identifier',
				anchor:"90%"
			}, {
				fieldLabel:"报表部署目录", maxLength:50,
				allowBlank: false,
				anchor:"90%",
				maxLength:40, xtype: 'Base_comboTree', hiddenName: 'deployCatalog',
				displayField: 'folderFullPath', valueField:'folderFullPath',
				fieldLabel:'报表部署目录',
				selectNodeModel:"exceptRoot",
				business: 'fileCatalog', rootText: '报表模板目录'
			}]
		}, {
			columnWidth:1,
			items:[{
				fieldLabel:"报表描述", maxLength:200,
				xtype:"textarea",
				name:"moduleDesc",
				anchor:"95%"
			}]
		}, {
			columnWidth:1,
			defaultType:'hidden',
			items:[{
				name:"idx", fieldLabel:"idx主键"
			}, {
				name:"parentIDX", fieldLabel:"上级报表标主键"
			}, {
				name:"editable", value:"T", fieldLabel:"是否可编辑"
			}, {
				name:"latestDeployTime", fieldLabel:"最近部署时间", hidden:true, xtype:"my97date", format:'Y-m-d H:i:s', initNow:false
			}]
		}]
	});
	/** **************** 定义钻取模板保存表单结束 **************** */
	
	/** **************** 定义钻取模板表格开始 **************** */
	ReportMgr.subGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/printerModule!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/printerModule!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/printerModule!logicDelete.action',            //删除数据的请求URL
	    singleSelect: true,
	    storeAutoLoad: false,
	    tbar:['add', 'delete', '->', {
			xtype:'checkbox', id:'checkbox_A', boxLabel:'只显示未部署的报表', handler: function() {
				ReportMgr.subGrid.store.load();
			}
	    }],
	    viewConfig: {forceFit: false, markDirty: false},
	    saveForm: ReportMgr.saveForm,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'报表标识码', dataIndex:'identifier', width: 160, editor:{  maxLength:50 }
		},{
			header:'上级报表主键', dataIndex:'parentIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'报表部署名称', dataIndex:'deployName', width: 220, editor:{  maxLength:50 }
		},{
			header:'是否可编辑', dataIndex:'editable', hidden:true, editor:{ maxLength:1 }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if ("T" == value) return "是";
				if ("F" == value) return "否";
				return "错误！未知状态";
			}
		},{
			header:'报表显示名称', dataIndex:'displayName', width: 250, editor:{  maxLength:50 }
		},{
			header:'报表部署目录', dataIndex:'deployCatalog', width: 220, editor:{  maxLength:50 }
		},{
			header:'最近部署时间', dataIndex:'latestDeployTime', width:145, xtype:'datecolumn', format:'Y-m-d H:i:s', editor:{ xtype:'hidden' }
		},{
			header:'最近更新时间', dataIndex:'latestUpdateTime', width:145, xtype:'datecolumn', format:'Y-m-d H:i:s', editor:{ xtype:'hidden' }
		},{
			header:'报表描述', dataIndex:'moduleDesc', width: 350, editor:{  maxLength:200 }
		}],
		
		// 新增时要验证是否已经保存了主模板信息
		beforeAddButtonFn: function(){
			if (Ext.isEmpty(ReportMgr.parentIDX)) {
				MyExt.Msg.alert('请先保存主模板基本信息！');
				return false;
			}
	    	return true;
	    },
	    
		// 新增窗口显示后的函数处理
		afterShowSaveWin: function() {
			// 如果选择了【报表模板目录树】节点，则默认设置“报表部署目录”为当前选择的目录
			this.saveForm.find('hiddenName', 'deployCatalog')[0].setDisplayValue(ReportMgr.deployCatalog, ReportMgr.deployCatalog);
			this.saveForm.find('name', 'parentIDX')[0].setValue(ReportMgr.parentIDX);
			ReportMgr.setIdentifierFn(this.saveForm);
		},
		
		// 编辑时验证该报表打印模板是否可编辑，如果不可编辑则不弹出编辑窗口
		beforeShowEditWin: function(record, rowIndex){  
			var editable = record.get('editable');
			if ("F" == editable) {
				MyExt.Msg.alert('您选中的报表不能进行编辑！');
				return false;
			}
			
	    	//清空自定义组件的值
	        var componentArray = ["TrainType_combo","Base_combo","TrainNo_combo","ProfessionalType_comboTree","BureauSelect_comboTree","DeportSelect_comboTree",
	        		"PartsClass_comboTree","MatClass_comboTree","OmOrganization_comboTree","OmOrganizationCustom_comboTree",
	        		"EosDictEntry_combo","OmEmployee_SelectWin","GyjcFactory_SelectWin","PartsAccount_SelectWin","PartsStock_SelectWin",
	        		"PartsTypeAndQuota_SelectWin","XC_combo","BuildUpType_comboTree","RCRT_combo","TrainRC_combo","RT_SelectWin"];
	        for(var j = 0; j < componentArray.length; j++){
	        	var component = this.saveWin.findByType(componentArray[j]);
	        	if(Ext.isEmpty(component) || !Ext.isArray(component)){
	        	}else{
	        		for(var i = 0; i < component.length; i++){
	                    component[ i ].clearValue();
	                }
	        	}	                    
	        }
	    	return true; 
	    },    
	    
		// 删除时验证该报表打印模板是否可编辑，如果不可编辑则不允许删除
		beforeDeleteFn: function(){
			var sm = this.getSelectionModel();
			var records = sm.getSelections();
			for (var i = 0; i < records.length; i++) {
				var editable = records[i].get('editable');
				if ("F" == editable) {
					MyExt.Msg.alert('您选中的报表中包含不能进行删除的项目，请重新选择！');
					return false;
				}
			}
				
	        return true;
	    },
		
		// 保存窗口显示后的函数处理
		afterShowEditWin: function(record, rowIndex) {
			// 回显“报表部署目录”字段
			this.saveForm.find('hiddenName', 'deployCatalog')[0].setDisplayValue(record.get('deployCatalog'), record.get('deployCatalog'));
		},
		
		// 保存方法执行前，删除空字段
		beforeSaveFn: function(data){
			data = MyJson.deleteBlankProp(data);
			return true; 
		},
		
		// 重写创建保存窗口的函数
		createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        this.saveWin = new Ext.Window({
	            title:"新增", width:650, height:210, layout:'fit', plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.saveForm, 
	            modal:true,
	            buttons: [{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: function () {
	                	ReportMgr.isSaveAndAdd = false;
	                	this.saveFn();
	                }
	            }, {
	                text: "保存并新增", iconCls: "addIcon", scope: this, handler: function () {
	                	ReportMgr.isSaveAndAdd = true;
	                	this.saveFn();
	                }
	            }, {
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
	            }]
	        });
    	},
	      
    	// 重写删除方法
    	deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        if(this.saveWin)    this.saveWin.hide();
	        if(this.searchWin)  this.searchWin.hide();        
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        // 获取选择记录的idx数组
	        var ids = $yd.getSelectedIdx(this);
	        
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        Ext.Msg.confirm("提示  ", "共删除记录<span style='color:red;font-weight:bold;'>&nbsp;"+ ids.length +"&nbsp;</span>条，该操作将不能恢复，是否继续？  ", function(btn){
		        if(btn != 'yes')    return;
		        var cfg = {
		            scope: ReportMgr.subGrid, url: ReportMgr.subGrid.deleteURL, params: {ids: ids}
		        }
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		    });   
	    },
	    
	    // 保存成功后的函数处理
	    afterSaveSuccessFn: function(result, response, options) {
	        this.store.reload();
	        alertSuccess();
	    	if (!ReportMgr.isSaveAndAdd) {
	    		this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	    		this.saveForm.find('name', 'deployName')[0].setValue(result.entity.deployName);
	    	} else {
	    		this.saveForm.getForm().reset();
	    		this.saveForm.find('name', 'parentIDX')[0].setValue(ReportMgr.parentIDX);
	    	}
	    },
	    
	    listeners: {
	    	rowclick: function(grid, rowIndex, e) {
	    		var record = grid.store.getAt(rowIndex);
	    		FileObject.reload(record.get('idx'));
	    	}
	    }
	});
	
	// 默认以报表部署名称升序排序
	ReportMgr.subGrid.store.setDefaultSort('deployCatalog', "ASC");
	
	// 表格数据加载时的参数传递
	ReportMgr.subGrid.store.on('beforeload', function(){
		var searchParams = {};
		if (Ext.isEmpty(ReportMgr.parentIDX)) {
			searchParams.parentIDX = "###";
		} else {
			searchParams.parentIDX = ReportMgr.parentIDX
		}
		// 查询指定“报表部署目录”下的所有报表
		searchParams = MyJson.deleteBlankProp(searchParams);
		var whereList = []; 
		for(prop in searchParams){
			whereList.push({propName:prop, propValue:searchParams[prop], stringLike: false});
		}
		if (Ext.getCmp('checkbox_A').checked) {
			var sql = "LATEST_DEPLOY_TIME IS NULL OR LATEST_DEPLOY_TIME < LATEST_UPDATE_TIME"
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	// 表格数据加载成功后的函数处理
	ReportMgr.subGrid.store.on('load', function(){
		if (ReportMgr.isMainTab) {
			return;
		}
		if (this.getCount() <= 0) {
			FileObject.reload();
			return;
		}
		var sm = ReportMgr.subGrid.getSelectionModel();
		sm.selectFirstRow();						// 选择一条记录时会触发【rowselect】事件
	});
	
	// 选择变化时的事件监听
	ReportMgr.subGrid.getSelectionModel().on('rowselect', function(sm, rowIndex, record){
		FileObject.reload(record.get('idx'));
	});
	/** **************** 定义钻取模板表格结束 **************** */
	
	ReportMgr.win = new Ext.Window({
		title:"报表模板编辑",
		width:800, height:500,
		modal: true,
		layout:"border",
		closeAction:'hide',
		items:[{
			// 【主(钻取)模板报表文件】		
			xtype:"panel",
			title:"<span style='font-weight:normal;'>主模板报表文件</span>",
			region:"center",
			layout:"fit",
			items:[FileObject.grid]
		}, {
			xtype:"tabpanel",
			activeTab:0,
			region:"north",
			height:235,
			items:[{
				xtype:"panel",
				title:"主模板",
				layout:"fit",
				frame:true,
				items:[ReportMgr.mainForm],
				listeners:{
					activate: function() {
				        ReportMgr.win.find('xtype', 'panel')[0].setTitle("<span style='font-weight:normal;'>主模板报表文件</span>");
						ReportMgr.isMainTab = true;
						// 加载报表文件表格
				        FileObject.reload(this.find('name', 'idx')[0].getValue());
					}
				}
			}, {
				xtype:"panel",
				title:"钻取模板",
				layout:"fit",
				height:161,
				items:[ReportMgr.subGrid],
				listeners:{
					activate: function() {
				        ReportMgr.win.find('xtype', 'panel')[0].setTitle("<span style='font-weight:normal;'>钻取模板报表文件</span>");
						ReportMgr.isMainTab = false;
						ReportMgr.reloadSubGrid();
					}
				}
			}]
		}],
		listeners: {
			beforeshow: function() {
				// 如果业务主键为空，则提示
				if (Ext.isEmpty(ReportMgr.businessIDX)) {
					MyExt.Msg.alert("未设置业务主键<span style='color:red; font-weight:bold;'>ReportMgr.businessIDX</span>，请检查！");
					return false;
				}
				return true;
			},
			show: function(window) {
				// 始终在窗口显示时，激活“主模板”编辑tab，否则会有加载问题
				var tabPanel = window.findByType('tabpanel')[0];
				tabPanel.setActiveTab(0);
				
				// 从数据库获取主报表模板的基本信息
				Ext.Ajax.request({
					url: ctx + "/printerModule!getModelByBusinessIDX.action",
					params: {businessIDX: ReportMgr.businessIDX},
					scope: ReportMgr.mainForm,
					//请求成功后的回调函数
				    success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        var entity = result.entity;
				        if (null == entity || 'null' == entity || undefined == entity) {
				        	ReportMgr.resetFn();
							ReportMgr.setIdentifierFn(this);
				        } else {
					        // 回显主模板基本信息表单
					        this.getForm().setValues(entity);
					        // 针对日期字段的特殊处理
					        var latestUpdateTime = entity.latestUpdateTime;
					        if (null != latestUpdateTime && 'null' != latestUpdateTime && '' != latestUpdateTime) {
					        	latestUpdateTime = new Date(latestUpdateTime).format('Y-m-d H:i:s');
				           		this.find('name', 'latestUpdateTime')[0].setValue(latestUpdateTime);
					        }
					        var latestDeployTime = entity.latestDeployTime;
					        if (null != latestDeployTime && 'null' != latestDeployTime && '' != latestDeployTime) {
					        	latestDeployTime = new Date(latestDeployTime).format('Y-m-d H:i:s');
				           		this.find('name', 'latestDeployTime')[0].setValue(latestDeployTime);
					        }
				        }
				        // 加载报表文件表格的【报表打印模板主键】
				        var printerModuleIDX = this.find('name', 'idx')[0].getValue();
				        ReportMgr.parentIDX = printerModuleIDX;
				        if (!ReportMgr.isMainTab) {
					        // 加载钻取报表模板
					        ReportMgr.reloadSubGrid(printerModuleIDX)
				        }
				        // 加载报表文件表格
				        FileObject.reload(printerModuleIDX);
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}
		},
		buttonAlign:'center',
		buttons:[{
			text:'部署', iconCls:'deployIcon', handler:function() {
				// 检测是否保存了报表打印模板信息
				if(Ext.isEmpty(ReportMgr.parentIDX)) {
					MyExt.Msg.alert("请先设置并保存报表打印模板信息！");
					return;
				}
				
				self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/printerModule!deploySingle.action',
					params:{id: ReportMgr.parentIDX},
					scope:ReportMgr.mainForm,
				    //请求成功后的回调函数
				    success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            // 回显主模板的“最近部署日期”字段
				            var entity = result.entity;
				            this.find('name', 'latestDeployTime')[0].setValue(new Date(entity.latestDeployTime).format('Y-m-d H:i:s'))
				            
				            // 重新加载钻取模板表格
				            ReportMgr.subGrid.store.reload();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
		
			}
		}, {
			text:'预览', iconCls:'queryIcon', handler: function() {
				if(Ext.isEmpty(ReportMgr.parentIDX)) {
					MyExt.Msg.alert("请先设置并保存报表打印模板信息！");
					return;
				}
				ReportMgr.printerPreviewFn(ReportMgr.parentIDX);
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler:function() {
				this.findParentByType('window').hide();	
			}
		}]
	})
	
});