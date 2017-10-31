/**
 * 报表打印模板 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
var now = new Date();
now.setMonth(now.getMonth() - 1);
var lastMonth = now.format('Y-m-d');
Ext.onReady(function(){
	Ext.namespace('PrinterModule');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	PrinterModule.labelWidth = 100;
	PrinterModule.fieldWidth = 140;
	PrinterModule.searchParams = {};
	PrinterModule.deployCatalog = '';			// 报表部署目录
	PrinterModule.isSaveAndAdd = false;			// 是否是保存并新增
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	PrinterModule.searchForm = new Ext.form.FormPanel({
		layout:'column',
		style:'padding:10px 10px 0 10px;',
		labelWidth:PrinterModule.labelWidth,
		defaults:{
			layout:'form', columnWidth:.33, defaults: {
				xtype:'textfield',
				width:PrinterModule.fieldWidth
			}
		},
		items:[{
			items: {
				fieldLabel:'报表标识码', name:'identifier'
			}
		}, {
			items: {
				fieldLabel:'报表部署名称', name:'deployName'
			}
		}, {
			items: {
				xtype: 'compositefield', fieldLabel: '报表部署日期', combineErrors: false, anchor:'100%',
				items: [{
					xtype:'my97date', id: 'startDate', format:'Y-m-d', value:lastMonth, width: 100,
					// 日期校验器
					vtype:'dateRange',
					dateRange:{startDate: 'startDate', endDate: 'endDate'}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {							
					xtype:'my97date', id: 'endDate', format:'Y-m-d', width: 100,
					// 日期校验器
					vtype:'dateRange',
					dateRange:{startDate: 'startDate', endDate: 'endDate'}
				}]
			}
		}],
		buttonAlign: 'center',
		buttons: [{
			text:'查询', iconCls:'searchIcon', handler: function() {
				var form = this.findParentByType('form').getForm();
				if (!form.isValid()) {
					return;
				}
				PrinterModule.grid.store.load();
			}
		}, {
			text:'重置', iconCls:'resetIcon', handler: function() {
				PrinterModule.searchForm.getForm().reset();
				PrinterModule.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	PrinterModule.saveForm = new Ext.form.FormPanel({
		border:false, baseCls:'x-plain',
		labelWidth:PrinterModule.labelWidth,
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
				vtype:'fineReport',
				allowBlank: false,
				name:"deployName",
				anchor:"90%"
			}, {
				fieldLabel:"报表显示名称", maxLength:40,
				allowBlank: false,
				name:"displayName",
				anchor:"90%"
			}]
		}, {
			items:[{
				fieldLabel:"报表部署目录", maxLength:50,
				allowBlank: false,
				anchor:"90%",
				maxLength:50, xtype: 'Base_comboTree', hiddenName: 'deployCatalog',
				displayField: 'folderFullPath', valueField:'folderFullPath',
				fieldLabel:'报表部署目录',
				selectNodeModel:"exceptRoot",
				business: 'fileCatalog', rootText: '报表模板目录'
			}, {
				fieldLabel:"报表标识码",
				maxLength:40,
				allowBlank: false,
				name:'identifier',
				anchor:"90%"
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
				name:"editable", value:CONST_STR_T, fieldLabel:"是否可编辑"
			}, {
				name:"latestDeployTime", fieldLabel:"最近部署时间", hidden:true, xtype:"my97date", format:'Y-m-d H:i:s', initNow:false
			}]
		}]
	});
	/** **************** 定义保存表单结束 **************** */
	
	/** **************** 定义保存全局函数开始 **************** */
	// 自动设置【报表标识】字段值
	PrinterModule.setIdentifierFn = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "REPORT_IDENTIFIER_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					PrinterModule.saveForm.find('name', 'identifier')[0].setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	// 操作按钮【维护报表】触发的函数处理
	PrinterModule.configReportFn = function () {
		var sm = PrinterModule.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('请先选择一条记录！');
			return;
		}
		if (sm.getCount() > 1) {
			MyExt.Msg.alert('请选择一条记录进行报表维护！');
			return;
		}
		var record = sm.getSelections()[0];
		
		// 验证报表是否可编辑
		if (CONST_STR_F == record.get('editable')) {
			MyExt.Msg.alert('您选中的报表不能进行报表维护！');
			return;
		}
		
		FileObject.window.find('name', 'deployName')[0].setValue(record.get('deployName'));
		FileObject.window.find('name', 'displayName')[0].setValue(record.get('displayName'));
		
		// 设置【报表文件对象】的“报表打印模板主键”
		FileObject.printerModuleIDX = record.get('idx');
		FileObject.grid.store.load();
		FileObject.window.show();
	}
	
	// 操作按钮【增量部署】触发的函数处理
	PrinterModule.deployByIncrementFn = function() {
		if (!$yd.isSelectedRecord(PrinterModule.grid)) {
			return;
		}
		Ext.Msg.confirm('提示', '是否确认部署您已选择的报表模板到服务器？', function(btn){
			if('yes' == btn) {
				self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/printerModule!deployByIncrement.action',
					params:{ids: $yd.getSelectedIdx(PrinterModule.grid)},
					scope:PrinterModule.grid,
				    //请求成功后的回调函数
				    success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            this.store.reload(); 
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
		});
	}
	
	// 操作按钮【全量部署】触发的函数处理
	PrinterModule.deployAll = function() {
		Ext.Msg.show({
			title:'提示',
			msg:'是否清空服务器上的所有报表模板？',
			buttons:Ext.Msg.YESNOCANCEL,
			icon:Ext.MessageBox.INFO,
			fn: function(btn) {
				if ('cancel' == btn) {
					return;
				}
				var deleteDir = false;
				if ('yes' == btn) {
					deleteDir = true;
				}
				self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/printerModule!deployAll.action',
					scope:PrinterModule.grid,
					params:{deleteDir: deleteDir},
				    //请求成功后的回调函数
				    success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            this.store.reload(); 
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
		});
	}
	
	// 【初始化】按钮触发的函数处理
	PrinterModule.insertDeployedReportFn = function() {
		Ext.Msg.confirm('警告', '是否确认导入已部署的报表到数据库？', function(btn){
			if ('yes' == btn) {
				self.loadMask.show();
				Ext.Ajax.request({
					url: ctx + '/printerModule!insertDeployedReport.action',
					scope:PrinterModule.grid,
				    //请求成功后的回调函数
				    success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            this.store.reload(); 
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
		});
	}
	
	// 打印预览
	PrinterModule.printerPreviewFn = function(idx) {
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
	/** **************** 定义保存全局函数结束 **************** */
	
	/** **************** 定义模板表格开始 **************** */
	PrinterModule.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/printerModule!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/printerModule!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/printerModule!logicDelete.action',            //删除数据的请求URL
//	    singleSelect: true,
	    viewConfig: {forceFit: false, markDirty: false},
	    tbar:['add', 'delete', {
	    	text:'报表维护', id:'btnConfigReport', iconCls:'pjglIcon', hidden:true, handler: PrinterModule.configReportFn
	    }, {
	    	text:'增量部署', iconCls:'deployIcon', handler: PrinterModule.deployByIncrementFn
	    }, 'refresh', '->', {
	    	text:'<span style="color:#888888;">全量部署</span>', iconCls:'deployIconGhost', handler: PrinterModule.deployAll
	    }, {
			text: '<span style="color:#888888;">初始化</span>', handler: PrinterModule.insertDeployedReportFn
					    	
	    }, {
			xtype:'checkbox', id:'checkbox_A', boxLabel:'只显示未部署的报表', handler: function() {
				PrinterModule.grid.store.load();
			}
	    }],
	    saveForm: PrinterModule.saveForm,
		fields: [{
			dataIndex:'idx', header:'报表维护', hidden:false, width:70, editor: { xtype:'hidden' }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
				return "<img src='" + img + "' alt='报表维护' style='cursor:pointer' onclick='PrinterModule.configReportFn()'/>";
			}
		},{
			header:'报表标识码', dataIndex:'identifier', width: 160, editor:{  maxLength:50 }
		},{
			header:'上级报表主键', dataIndex:'parentIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'报表部署名称（点击预览）', dataIndex:'deployName', width: 220, editor:{  maxLength:50 }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='PrinterModule.printerPreviewFn(\""+ record.get('idx') + "\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'是否可编辑', dataIndex:'editable', hidden:true, editor:{ maxLength:1 }, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == CONST_STR_F) {
					// 如果是“不可编辑”记录，则字体已灰色显示
					var id = "clr"+Math.random()
					setTimeout(function(){
						var node = document.getElementById(id);
						if (null != node) {
							node.parentNode.parentNode.parentNode.style.color=UNEDITABLE_COLOR;
						}
					},50);
					return "<div id='"+id+"'>否</div>";
				}
				if (value == CONST_STR_T) return '是';
				return '错误！未知状态';
			}
		},{
			header:'报表显示名称', dataIndex:'displayName', width: 250, editor:{  maxLength:50 }
		},{
			header:'报表部署目录', dataIndex:'deployCatalog', width: 220, editor:{  maxLength:50 }
		},{
			header:'最近部署时间', width:145, dataIndex:'latestDeployTime', xtype:'datecolumn', format:'Y-m-d H:i:s', editor:{ xtype:'hidden' }
		},{
			header:'最近更新时间', width:145, dataIndex:'latestUpdateTime', xtype:'datecolumn', format:'Y-m-d H:i:s', editor:{ xtype:'hidden' }
		},{
			header:'报表描述', dataIndex:'moduleDesc', width: 350, editor:{  maxLength:200 }
		}],
		
		// 新增窗口显示后的函数处理
		afterShowSaveWin: function() {
			// 如果选择了【报表模板目录树】节点，则默认设置“报表部署目录”为当前选择的目录
			this.saveForm.find('hiddenName', 'deployCatalog')[0].setDisplayValue(PrinterModule.deployCatalog, PrinterModule.deployCatalog);
			// 自动设置报表标识码
			PrinterModule.setIdentifierFn();
		},
		
		// 编辑时验证该报表打印模板是否可编辑，如果不可编辑则不弹出编辑窗口
		beforeShowEditWin: function(record, rowIndex){  
			var editable = record.get('editable');
			if (CONST_STR_F == editable) {
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
				if (CONST_STR_F == editable) {
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
//			// 验证报表部署名称是否是以.cpt结尾
//			var deployName = data.deployName;
//			if (deployName.indexOf('.cpt') <= 0) {
//				this.saveForm.find('name', 'deployName')[0].markInvalid("报表部署名称必须以.cpt结尾！");
//				return false;
//			}
			return true; 
		},
		
		// 重写创建保存窗口的函数
		createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        this.saveWin = new Ext.Window({
	            title:"新增", width:650, height:210, layout:'fit', plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.saveForm, 
	            buttons: [{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: function () {
	                	PrinterModule.isSaveAndAdd = false;
	                	this.saveFn();
	                }
	            }, {
	                text: "保存并新增", iconCls: "addIcon", scope: this, handler: function () {
	                	PrinterModule.isSaveAndAdd = true;
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
		            scope: PrinterModule.grid, url: PrinterModule.grid.deleteURL, params: {ids: ids}
		        }
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		    });   
	    },
	    
	    // 保存成功后的函数处理
	    afterSaveSuccessFn: function(result, response, options) {
	        this.store.reload();
	        alertSuccess();
	    	if (!PrinterModule.isSaveAndAdd) {
	    		this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	    		this.saveForm.find('name', 'deployName')[0].setValue(result.entity.deployName);
	    	} else {
	    		this.saveForm.getForm().reset();
				// 自动设置报表标识码
				PrinterModule.setIdentifierFn();
	    	}
	    }
	});
	
	// 默认以报表部署名称升序排序
	PrinterModule.grid.store.setDefaultSort('deployCatalog', "ASC");
	
	// 表格数据加载时的参数传递
	PrinterModule.grid.store.on('beforeload', function(){
		var searchParams = PrinterModule.searchForm.getForm().getValues();
		// 查询指定“报表部署目录”下的所有报表
		searchParams.deployCatalog = PrinterModule.deployCatalog;
		searchParams = MyJson.deleteBlankProp(searchParams);
		var whereList = []; 
		var sql;
		for(prop in searchParams){
			if('startDate' == prop) {
//				var value = searchParams[prop] + " 00:00:00";
//				sql = "LATEST_DEPLOY_TIME >= TO_DATE('"+ value +"', 'yyyy-MM-dd HH24:mi:ss')";
//				whereList.push({sql: sql, compare:Condition.SQL});
			} else if('endDate' == prop) {
//				var value = searchParams[prop] + " 23:59:59";
//				sql = "LATEST_DEPLOY_TIME <= TO_DATE('"+ value +"', 'yyyy-MM-dd HH24:mi:ss')";
//				whereList.push({sql: sql, compare:Condition.SQL});
			} else if('deployCatalog' == prop){
				whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.EQ, stringLike: false});
			} else {
				whereList.push({propName:prop, propValue:searchParams[prop]});
			}
		}
		// 针对日期范围查询的特殊处理，否则不能查询出新增、但还未部署的报表打印模板记录
		var startDate = null;
		var endDate = null;
		if (searchParams.startDate) {
			startDate = searchParams.startDate + " 00:00:00";
		}
		if (searchParams.endDate) {
			endDate = searchParams.endDate + " 23:59:59";
		}
		if (startDate && endDate) {
			sql = "((LATEST_DEPLOY_TIME >= TO_DATE('"+ startDate +"', 'yyyy-MM-dd HH24:mi:ss') AND LATEST_DEPLOY_TIME <= TO_DATE('"+ endDate +"', 'yyyy-MM-dd HH24:mi:ss')) OR LATEST_DEPLOY_TIME IS NULL)";
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		if (startDate && !endDate) {
			sql = "(LATEST_DEPLOY_TIME >= TO_DATE('"+ startDate +"', 'yyyy-MM-dd HH24:mi:ss') OR LATEST_DEPLOY_TIME IS NULL)";
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		if (!startDate && endDate) {
			sql = "(LATEST_DEPLOY_TIME <= TO_DATE('"+ endDate +"', 'yyyy-MM-dd HH24:mi:ss') OR LATEST_DEPLOY_TIME IS NULL)";
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		if (Ext.getCmp('checkbox_A').checked) {
			sql = "(LATEST_DEPLOY_TIME IS NULL OR LATEST_DEPLOY_TIME < LATEST_UPDATE_TIME)"
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	/** **************** 定义模板表格结束 **************** */
	
});