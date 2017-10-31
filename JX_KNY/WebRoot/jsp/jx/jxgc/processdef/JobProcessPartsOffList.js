
Ext.onReady(function() {
	Ext.namespace('JobProcessPartsOffList');
	/** ************ 定义全局变量开始 ************ */
	 JobProcessPartsOffList.fieldWidth = 70;
	 JobProcessPartsOffList.processIdx = "###";
	 JobProcessPartsOffList.flag = false; 
	/** ************ 定义全局变量线束 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	 
	//新增一行
    JobProcessPartsOffList.addFn = function(){
    	var record_v = null;
		record_v = new Ext.data.Record();		
        JobProcessPartsOffList.grid.store.insert(0, record_v); 
        JobProcessPartsOffList.grid.getView().refresh(); 
        JobProcessPartsOffList.grid.getSelectionModel().selectRow(0);
    }
    
    //批量设置上下车节点
	JobProcessPartsOffList.setNode  = function(data,flag){
	    var dataAry = new Array();
		for (var i = 0; i < data.length; i++) {
			dataAry.push(data[i].data);
		}
		if (dataAry.length <= 0) {
			MyExt.Msg.alert("请选择一条记录");
			return;
		}
		JobProcessNodeDefWin.processIdx = JobProcessPartsOffList.processIdx;
		JobProcessNodeDefWin.dataAry = dataAry;
		if(flag == 'on'){
			JobProcessNodeDefWin.flag = "on";
		}else if(flag == 'off'){
			JobProcessNodeDefWin.flag = "off";
		}else return;
		JobProcessNodeDefWin.win.show();
	}
	
	// 批量设置节点提交
	JobProcessNodeDefWin.submit = function(){
		var sm = JobProcessNodeDefWin.grid.getSelectionModel();
        if (sm.getCount() < 1 || sm.getCount()>1) {
            MyExt.Msg.alert("请选择一条记录！");
            return;
        }
        var data = sm.getSelections()[0].json;    
		Ext.Msg.confirm('提示',"确认设置此流程节点", function(btn) {
			if (btn != 'yes') return;
			if (self.loadMask) self.loadMask.show();
			Ext.Ajax.request({
				url : ctx + "/jobProcessPartsOffList!saveNodeList.action",
				jsonData : JobProcessNodeDefWin.dataAry,
				params : {
					nodeIdx : data.idx,
					nodeName : data.nodeName,
					flag: JobProcessNodeDefWin.flag
				},
				success : function(response, options) {
	        		if (self.loadMask) self.loadMask.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.errMsg == null) {
						JobProcessPartsOffList.grid.store.load();
				   		JobProcessNodeDefWin.win.hide();
						alertSuccess("批量设置成功！");
					} else {
						alertFail(result.errMsg);
					}
				},
				failure : function(response, options) {
	        		if (self.loadMask) self.loadMask.hide();
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		});
	}
	
	//选择零部件名称之后执行确认的操作（生成一条记录）
	JcpjzdBuildWin.submit = function(){	
    	var store = JcpjzdBuildWin.store;
     	if(store.getCount() == 0){
			MyExt.Msg.alert("请添加配件信息！");
			return ;
		}
      	var datas = new Array();
		for (var i = 0; i < store.getCount(); i++) {
			var data = store.getAt(i).data;
			for (var j = i+1; j < store.getCount(); j++) {
				var data_v = {} ;
				data_v = store.getAt(j).data;
				if(data.partsId == data_v.partsId && data.wzmc == data_v.wzmc){
					MyExt.Msg.alert("配件<font color='red'>【" + data.partsName + "】位置【" + data.wzmc + "】</font>添加重复,请重新选择！");
					return ;
				}
			}
			data.processIdx = JobProcessPartsOffList.processIdx
			datas.push(data);
		} 
        Ext.Ajax.request({
            url: ctx + "/jobProcessPartsOffList!savePartsOffList.action",
            jsonData: datas,
            params: {processIdx : JobProcessPartsOffList.processIdx},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
				    JobProcessPartsOffList.grid.store.load();
				    JcpjzdBuildWin.win.hide();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });    
	};	
	/** ************ 定义全局函数结束 ************ */
	
	/** ************ 定义导入窗口开始 ************ */
	JobProcessPartsOffList.importWin = new Ext.Window({
		title:"导入下车配件",
		width:450, height:120,
		plain:true, maximizable:false, modal: true,
		closeAction:"hide",
		layout:'fit',
		items: [{
			xtype:"form", id:'formid', border:false, style:"padding:10px" ,
			
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			fileUpload:true,															
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			
			baseCls: "x-plain", defaults:{anchor:"100%"},
			labelWidth:80,
			items:[{
				fieldLabel:'选择文件',
				name:'parts',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: '浏览文件...'
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
				var filePath = window.find('name', 'parts')[0].getValue();
				var hzm = filePath.substring(filePath.lastIndexOf("."));
				if(hzm !== ".xls"){
					MyExt.Msg.alert('该功能仅支持<span style="color:red;"> Excel2003（*.xls） </span>版本文件！');
					return;
				}
				form.submit({
                	url: ctx+'/jobProcessPartsOffList!saveImport.action?processIDX=' + JobProcessPartsOffList.processIdx,
                	waitTitle:'请稍候',
               	 	waitMsg: '正在导入数据请稍候...', 
               	 	method: 'POST',
               	 	enctype: 'multipart/form-data',
                	// 请求成功后的回调函数
                	success: function(form,  action) {
                		var result = Ext.util.JSON.decode(action.response.responseText);
		                if(result.success == true){
							form.getEl().dom.reset();
							// 隐藏文件上传窗口
		                	window.hide();
							if(result.errMsg == null || result.errMsg.length == 0){ //数据导入成功
		                		alertSuccess();
		                	} else {
		                		alertFail(result.errMsg);
		                	}
							JobProcessPartsOffList.grid.store.reload();
		                } else {
							alertFail(result.errMsg);
		                }
                	},
                	// 请求失败后的回调函数
				    failure: function(form, action){
                		var result = Ext.util.JSON.decode(action.response.responseText);
                		if (!Ext.isEmpty(result.errMsg)) {
 							alertFail(result.errMsg);
                		} else {
				       	 	Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + action.response.status + "\n" + action.response.responseText);
                		}
				    }
				});
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	/** ************ 定义导入窗口结束 ************ */
	
	/** ************ 定义列表开始 ************ */
	JobProcessPartsOffList.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + "/jobProcessPartsOffList!pageList.action",                      //装载列表数据的请求URL
	    saveURL: ctx + "/jobProcessPartsOffList!saveOrUpdate.action",                  //保存数据的请求URL
	    deleteURL: ctx + "/jobProcessPartsOffList!logicDelete.action",                      //删除数据的请求URL
	    storeAutoLoad: false,	
		beforeSaveFn: function(rowEditor, changes, record, rowIndex){
 			var value = document.getElementById("wzmc_id").value;
 			if(Ext.isEmpty(value))Ext.getCmp("wzdm_id").setValue('');
 			record.data.wzmc = value;
 			record.data.wzdm = Ext.getCmp("wzdm_id").getValue();
	        return true;
	    },
	    tbar:[{
			text:'新增', iconCls:'addIcon', handler:function(){
				if(Ext.isEmpty(JobProcessPartsOffList.processIdx) || "###" ==JobProcessPartsOffList.processIdx ){
					MyExt.Msg.alert('请先添加并保存作业流程信息！');
				}else{ 
					JcpjzdBuildWin.win.show();			
				}
			}	
		}, {
			text:'批量设置下车节点', iconCls:'pjglIcon',handler:function(){
	    		var data = JobProcessPartsOffList.grid.selModel.getSelections();
	    		JobProcessPartsOffList.setNode(data,"off");
	    	}
		}, {
			text:'批量设置上车节点', iconCls:'pjglIcon', handler:function(){
				var data = JobProcessPartsOffList.grid.selModel.getSelections();
	    		JobProcessPartsOffList.setNode(data,"on");
	    	}
		},'delete',{
			text:'关闭', iconCls:'closeIcon', scope:this, handler: function(){ 
//				window.parent.document.getElementById("topwin").close(); 
				Ext.getCmp("topwin").hide();
		}}, '-',{
		     text:"下载模板", iconCls:"application-vnd-ms-excel", 
	            handler:function(){
            	   window.location.href = ctx + '/jobProcessPartsOffList!download.action';
	            }
			}, '-',{
			text: '导入', iconCls:"page_excelIcon", handler: function() {
				JobProcessPartsOffList.importWin.show();
			}
	    }],
		fields:[{
	        	header: '主键',dataIndex:'idx', hidden:true
			},{
	        	dataIndex:'processIdx', header:'流程idx',hidden:true,width: 100, editor: { width: 100,readOnly: true }
			},{
	        	dataIndex:'partsId',id:'partsId', header:'配件id',hidden:true,width: 100, editor: { width: 100,readOnly: true }
			},{
	        	dataIndex:'partsName', header:'配件名称',width: 100,editor: { readOnly: true }
	        }, {
				header: '位置', dataIndex: 'wzmc', width: 100,
				editor: {
					id: "wzmc_id",
					xtype: "Base_combo",
					fieldLabel: "位置",
					hiddenName: "wzmc",
					returnField: [{ widgetId: "wzdm_id", propertyName: "partId" }],
					displayField: "partName", valueField: "partName",
					entity: "com.yunda.jx.component.entity.EquipPart",
					fields: ["partId", "partName"],
					pageSize: 20,
					minListWidth: 200,
					editable: true,
	 				listeners:{
						"beforequery" : function(){
							Ext.getCmp("wzdm_id").setValue(""); // 清除之前的记录						
						}
//	 				 	'collapse' : function(record, index){
//	 				 		Ext.getCmp("wzdm_id").setValue("");
//							JobProcessPartsOffList.grid.getView().refresh();
//	 				 	}
	 				 }
				}
			}, {
				header: '位置名称编码', dataIndex: 'wzdm', hidden: true,
				editor: { id: "wzdm_id" }    	  
	        },{
	        	 header:'下车节点', dataIndex:'offNodeName', width: 120,editor:{     		
					fieldLabel:'流程节点名称',
					id: "tecprocess_node_off",
					xtype: "Base_comboTree",
					hiddenName: "offNodeName",					
					business: 'jobProcessNodeDef',
					rootText: '流程节点名称',
					width:80,
//					disabled:false,
				    returnField: [{widgetId: "offNodeIdx", propertyName: "id"}], //id
//		    		editable: false,
		    		displayField: "text", valueField: "text",
		    		selectNodeModel: "all",
		    		listeners : {
		    			"beforequery" : function(){
		    				Ext.getCmp("tecprocess_node_off").queryParams = {'processIdx':JobProcessPartsOffList.processIdx};
						}
		    		}	    		
	        	}
	     	},{	
	        	sortable:false,  dataIndex:'offNodeIdx',header:'下车节点id', hidden:true, editor:{
	        		id:"offNodeIdx"              
			    }
	        },{	
	        	sortable:false,  dataIndex:'onNodeName',header:'上车节点', editor:{   
	                fieldLabel:'流程节点名称',
					id: "tecprocess_node_on",
					xtype: "Base_comboTree",
					hiddenName: "onNodeName",					
					business: 'jobProcessNodeDef',
					rootText: '流程节点名称',
					width:80,
					disabled:false,
					returnField: [{widgetId: "onNodeIdx", propertyName: "id"}], //id
		    		editable: true, displayField: "nodeName", valueField: "nodeName",
		    		selectNodeModel: "all",
		    		listeners : {
		    			"beforequery" : function(){
		    				Ext.getCmp("tecprocess_node_on").queryParams = {'processIdx':JobProcessPartsOffList.processIdx};
						}
		    		}	    	
			    }
	        },{	
	        	 dataIndex:'onNodeIdx',header:'上车节点id', hidden:true, editor:{
	        		id:"onNodeIdx"            
			    }
	   	 },{
	        dataIndex:'seqNo', header:'排序号',hidden:false, editor:{maxLength:8,vtype : "positiveInt"}   
	   	 },{
	        dataIndex:'trainTypeIDX', header:'车型idx',hidden:true, editor: { readOnly: true }	   
	   	 },{
	        dataIndex:'trainTypeShortName', header:'车型简称',hidden:true, editor: { readOnly: true }
        }]
	});
	JobProcessPartsOffList.grid.store.on('beforeload', function(){
		var searchParams = {};
		searchParams.processIdx = JobProcessPartsOffList.processIdx;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
   // 以下代码用于重置“节点”字段
	JobProcessPartsOffList.grid.rowEditor.on('beforeedit', function(me, rowIndex) {
		Ext.getCmp('wzmc_id').clearValue();
		Ext.getCmp('tecprocess_node_on').clearValue();
		Ext.getCmp('tecprocess_node_off').clearValue();
		var record = JobProcessPartsOffList.grid.store.getAt(rowIndex);
		// 目前发现一个问题，在第一次触发行编辑时，不能正常的回显控件值，暂时未找到解决方案
		Ext.getCmp('tecprocess_node_on').setDisplayValue(record.get('onNodeName'),record.get('onNodeName'));
		Ext.getCmp('tecprocess_node_off').setDisplayValue(record.get('offNodeName'),record.get('offNodeName'));
	});
	
	JobProcessPartsOffList.grid.rowEditor.on('afteredit', function(me, rowIndex) {
		Ext.getCmp('wzmc_id').clearValue();
		Ext.getCmp('tecprocess_node_on').clearValue();
		Ext.getCmp('tecprocess_node_off').clearValue();
		
	});
	JobProcessPartsOffList.grid.rowEditor.on('canceledit', function(me, rowIndex) {
		Ext.getCmp('wzmc_id').clearValue();
		Ext.getCmp('tecprocess_node_on').clearValue();
		Ext.getCmp('tecprocess_node_off').clearValue();
	});
});