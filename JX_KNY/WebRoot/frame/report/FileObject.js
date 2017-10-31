/**
 * 报表文件对象 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('FileObject');                       //定义命名空间
	
	
	/** **************** 定义全局变量开始 **************** */
	FileObject.labelWidth = 100;
	FileObject.fieldWidth = 140;
	FileObject.searchParams = {};
	FileObject.printerModuleIDX = '';			// 报表打印模板主键
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 设置报表为当前标识为“启用”
	FileObject.setCurrentFlagFn = function(idx) {
		self.loadMask.show();
		// Ajax后台请求
		Ext.Ajax.request({
			url: ctx + '/fileObject!updateCurrentFlag.action',
			params: {idx : idx},
			scope: FileObject.grid,
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload(); 
					FileObject.grid.getTopToolbar().get(1).disable()
					FileObject.grid.getTopToolbar().get(2).disable()
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
	
	// 下载报表文件
	FileObject.downloadFn = function(idx) {
		window.location.href = ctx + '/fileObject!download.action?id=' + idx;
	}
	
	// 重新加载表格数据
	FileObject.reload = function(printerModuleIDX) {
		if(printerModuleIDX) {
			FileObject.printerModuleIDX = printerModuleIDX;
		} else {
			FileObject.printerModuleIDX = "";
		}
		if (Ext.isEmpty(FileObject.printerModuleIDX)) {
        	FileObject.grid.store.removeAll();
        } else {
	        FileObject.grid.store.load();
        }
	}
	
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义报表文件表格开始 **************** */
	FileObject.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/fileObject!pageQuery.action',                //装载列表数据的请求URL
	    saveURL: ctx + '/fileObject!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/fileObject!logicDelete.action',            //删除数据的请求URL
	    border:false,
	    page:false,
	    tbar:['add', 'delete', {
	    	text:'设为启用', iconCls:'startIcon', handler: function() {
	    		var sm = FileObject.grid.getSelectionModel();
	    		if (sm.getCount() <= 0) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    		var idx = sm.getSelections()[0].get('idx');
	    		FileObject.setCurrentFlagFn(idx);
	    	}
	    }],
	    storeAutoLoad: false, singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }/*, 
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				setTimeout(function(){
					if (CONST_STR_T == record.get('currentFlag')) {
						var row = FileObject.grid.getView().getRow(rowIndex);
						if(null != row){
							row.style.backgroundColor = '#98FB98';
						}
					}
				}, 50);
			}*/
		},{
			header:'报表打印模板主键', dataIndex:'printerModuleIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'文件名称', dataIndex:'fileName', width:30, editor:{  maxLength:50 }
		},{
			header:'文件描述', dataIndex:'fileDesc', width:35
		},{
			header:'文件大小', align:'center', dataIndex:'fileLength', width:10, editor:{  },
			renderer: function(value, metaData, record, rowIdx, colIndex, store) {
				return Math.round(value/1024) + "K"
			}
		},{
			header:'文件上传路径', dataIndex:'fileUploadPath', hidden:true, editor:{   }
		},{
			header:'版本号', align:'center', dataIndex:'version', width:10, editor:{  maxLength:50 }
		},{
			header:'当前标识', align:'center', dataIndex:'currentFlag', width:10, hidden:false,
			renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				if (value == CONST_STR_F) {
					// 如果是“不可编辑”记录，则字体已灰色显示
					var id = "cle"+Math.random()
					setTimeout(function(){
						var node = document.getElementById(id);
						if (null != node) {
							node.parentNode.parentNode.parentNode.style.color=UNEDITABLE_COLOR;
						}
					},50);
					return "<div id='"+id+"'>未启用</div>";
				}
				if (value == CONST_STR_T) return '启用';
				return '错误！未知状态';
			}
		}, {
		
			header:'下载', align:'center', width:10, sortable:false,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){			
				return "<img src='" + downloadImg + "' alt='下载' style='cursor:pointer' onclick='FileObject.downloadFn(\"" + record.get('idx') + "\")'/>";
				return record.get('fileLength')
			}
		}, {
			header:'创建时间', dataIndex:'createTime', xtype:'datecolumn', format:'Y-m-d H:i:s', hidden:true
		}], 
		
		// 新增时要验证是否已经保存了报表打印模板信息
		beforeAddButtonFn: function(){
			if (Ext.isEmpty(FileObject.printerModuleIDX)) {
				MyExt.Msg.alert('请先保存报表打印模板基本信息！');
				return false;
			}
	    	return true;
	    },
		
		// 重写【新增】按钮触发的函数操作
		addButtonFn: function() {
			if (!this.beforeAddButtonFn()) return;
			FileObject.addWin.show();
		},
		
		listeners: {
			rowclick: function() {
				var sm = FileObject.grid.getSelectionModel();
				var record = sm.getSelections()[0];
				if (CONST_STR_F == record.get('currentFlag')) {
					// 如果报表文件当前标识为非启用，则启用【删除】和【设为启用】按钮
					FileObject.grid.getTopToolbar().get(1).enable()
					FileObject.grid.getTopToolbar().get(2).enable()
					// 否则禁用【删除】和【设为启用】按钮
				} else {
					FileObject.grid.getTopToolbar().get(1).disable()
					FileObject.grid.getTopToolbar().get(2).disable()
				}
			}
		}
	});
	// 默认以版本号倒序排序
	FileObject.grid.store.setDefaultSort('currentFlag', "DESC");
	
	// 取消表格行双击事件
	FileObject.grid.un('rowdblclick', FileObject.grid.toEditFn, FileObject.grid);
	
	// 表格数据加载时的参数传递
	FileObject.grid.store.on('beforeload', function(){
		var searchParams = FileObject.searchParams;
		// 查询指定“报表打印模板”下的所有报表文件
		if (Ext.isEmpty(FileObject.printerModuleIDX)) {
			searchParams.printerModuleIDX = "###";
		} else {
			searchParams.printerModuleIDX = FileObject.printerModuleIDX;
		}
		searchParams = MyJson.deleteBlankProp(searchParams);
		var whereList = []; 
		for(prop in searchParams){
			if('printerModuleIDX' == prop){
				whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.EQ, stringLike: false});
				continue;
			}
			whereList.push({propName:prop, propValue:searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	/** **************** 定义报表文件表格结束 **************** */
	
	/** **************** 定义报表文件维护窗口开始 **************** */
	FileObject.window = new Ext.Window({
		title:'报表文件', layout:'border', modal:true,
		height:400, width:900,
		closeAction:'hide',
		defaults:{layout:'fit'},
		items:[{
			region:'north', height:60, baseCls:'x-plain',
			items:{
				xtype:'form', padding:'20px', baseCls:'x-plain', labelWidth: FileObject.labelWidth - 20, layout:'column',
				defaults:{
					columnWidth:.5, layout:'form', baseCls:'x-plain',
					defaults:{
						xtype:'textfield', style:'background:none; border:none', anchor:'100%', readOnly:true
					}
				},
				items:[{
					items:[{ name:'deployName', fieldLabel:'报表部署名称' }]
				}, {
					items:[{ name:'displayName', fieldLabel:'报表显示名称' }]
				}]
			}
		}, {
			region:'center',
			items:FileObject.grid
		}],
		buttonAlign:'center',
		
		// Modified by hetao on 2016-04-25在报表文件编辑窗口增加部署的功能
		buttons:[{
			text:'部署', iconCls:'deployIcon', handler: function() {
				/**
				 * 检测该报表打印模板下是否有启用的报表文件
				 */ 
				function hasValidFileObject() {
					var store = FileObject.grid.store;
					if (store.getCount() <= 0) {
						return false;
					}
					var record;
					for (var i = 0; i < store.getCount(); i++) {
						record = store.getAt(i);
						if (CONST_STR_T === record.get('currentFlag')) {
							return true;
						}
					}
				}
				
				if (!hasValidFileObject()) {
					MyExt.Msg.alert("请先添加报表文件再部署！");
					return;
				}
				
				// 确认部署报表打印模板的提示
				Ext.Msg.confirm('提示', '是否确认部署报表打印模板到服务器？', function(btn){
				if('yes' == btn) {
					self.loadMask.show();
					Ext.Ajax.request({
						url: ctx + '/printerModule!deployByIncrement.action',
						params:{ids: [FileObject.printerModuleIDX]},
					    //请求成功后的回调函数
					    success: function(response, options){
					        if(self.loadMask)    self.loadMask.hide();
					        var result = Ext.util.JSON.decode(response.responseText);
					        if (result.errMsg == null) {       //操作成功     
					            alertSuccess();
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
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			// 显示窗口时激活操作按钮【删除】和【设为启用】
			show: function(window) {
				FileObject.grid.getTopToolbar().get(1).enable();
				FileObject.grid.getTopToolbar().get(2).enable();
			},
			// 隐藏后重新加载打印模板表格数据
			hide: function() {
				PrinterModule.grid.store.reload();
			}
		}
	});
	/** **************** 定义报表文件维护窗口结束 **************** */
	
	/** **************** 定义报表文件新增窗口开始 **************** */
	FileObject.addWin = new Ext.Window({
		title:"上传", 
		width:450, height:160, 
		plain:true, maximizable:false, modal: true,
		closeAction:"hide",
		layout:'fit',
		items: [{
			xtype:"form", id:'form', border:false, style:"padding:10px" ,
			
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			fileUpload:true,															
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			
			baseCls: "x-plain", defaults:{anchor:"100%"},
			labelWidth:80,
			items:[{
				fieldLabel:'选择文件',
				name:'report',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: '浏览文件...'/*,
	            buttonCfg: {
	                iconCls: 'upload-icon'
	            }*/
			}, {
				xtype:'label',
				html:['提示：只能上传后缀名为<span style="color:red;font-weight:bold;">.cpt</span>的文件。'],
				style:'margin-bottom:10px;padding-left:85px;'
			}, {
				xtype:'textfield',
				fieldLabel:'文件描述',
				name:'fileDesc'
			}]
		}],
		listeners:{
			// 隐藏窗口时重置上传表单
			hide: function(window) {
				window.find('xtype', 'form')[0].getForm().getEl().dom.reset();
			}
		},
		buttonAlign:'center', 
		buttons:[{
			text: "导入", iconCls: "saveIcon", handler: function(){
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var fileUploadPath = window.find('name', 'report')[0].getValue();
				if(fileUploadPath==null||fileUploadPath==""){
					MyExt.Msg.alert('尚未选择报表(<span style="color:red;font-weight:bold;">*.cpt</span>)文件！');
					return;
				}
				if(fileUploadPath.indexOf(".cpt") < 0){
					MyExt.Msg.alert('只能上传后缀名为<span style="color:red;font-weight:bold;">.cpt</span>的文件。');
					return;
				}
				form.submit({  
                	url: ctx+'/fileObject!saveUpload.action',  
                	waitTitle:'请稍候',
               	 	waitMsg: '正在上传报表文件请稍候...', 
               	 	method: 'POST',
               	 	enctype: 'multipart/form-data',
               	 	params:{
               	 		printerModuleIDX: FileObject.printerModuleIDX,
               	 		fileUploadPath: fileUploadPath
           	 		},
                	// 请求成功后的回调函数
                	success: function(form, action) { 
                		var result = Ext.util.JSON.decode(action.response.responseText);
		                if(result.success){
							form.getEl().dom.reset();
							// 隐藏文件上传窗口
		                	window.hide();	
	                		FileObject.grid.store.reload();
							alertSuccess();
		                } else {
							alertFail(result.errMsg);
		                }
                	},
                	// 请求失败后的回调函数
				    failure: function(form, action){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + action.response.status + "\n" + action.response.responseText);
				    }
            	}); 
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})
	/** **************** 定义报表文件新增窗口结束 **************** */
	
});