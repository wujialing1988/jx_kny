Ext.onReady(function(){		
	Ext.namespace('Gzpg');	
	
	
	Gzpg.formHeight = 260;
	Gzpg.height = jQuery("body").height()-Gzpg.formHeight - 20;
	
	Gzpg.multiStore = new Ext.data.JsonStore({
			root : "root",
			totalProperty : "totalProperty",
			autoLoad : true,
			url : ctx + "/faultNotice!getEmpsByOrgId.action?orgId="+teamOrgId,
			fields : [ 'empid' , 'empname']
	});
	
	Gzpg.selector = new Ext.ux.ItemSelector({
		id : 'fuck',
	    xtype: 'itemselector',
	    name: 'itemselector',
	    fieldLabel: 'ItemSelector',
	    imagePath: ctx+'/frame/resources/images/toolbar/',
	    multiselects: [{
	    	id : 'mts',
	        width: 250,
	        height: Gzpg.height,
	        store: Gzpg.multiStore,
	        displayField: 'empname',
	        valueField: 'empid',
	        legend: '未派工'
	    },{
	    	id : 'its',
	        width: 250,
	        height: Gzpg.height,
	        store: {},
	        displayField: 'empname',
	        valueField: 'empid',
	        legend: '已派工'
	    }]
	});
	
	Gzpg.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding : 10px',
		defaults : { anchor : '98%'},
		labelWidth:80,
		buttonAlign : 'center', 
		items: [
			{
				xtype:'fieldset',
				title: '基本信息',
				collapsible: false,
				autoHeight:true,
				frame : true,
				items :[
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .3,
								items : [
									{
										name : 'workItemName', 
										fieldLabel : '工作项名称', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .3,
								items : [
									{
										name : 'processInstName', 
										fieldLabel : '流程名称', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .3,
								items : [
									{
										name : 'processInstID', 
										fieldLabel : '流程实例', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .3,
								items : [
									{
										name : 'trainType', 
										fieldLabel : '车型车号', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									},
									{
										name : 'trainTypeIdx', 
										xtype : 'hidden'
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .3,
								items : [
									{
										name : 'repairClassName', 
										fieldLabel : '修程修次', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					}
				]
			},
			{
				xtype:'fieldset',
				title: '提票信息',
				collapsible: false,
				autoHeight:true,
				frame : true,
				items :[					
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
						       {
						    	   align : 'center',
						    	   layout : 'form',
						    	   defaultType : 'textfield',
						    	   baseCls : 'x-plain',
						    	   columnWidth : .3,
						    	   items : [
						    	            {
						    	            	name : 'partsName', 
						    	            	fieldLabel : '配件名称', 
						    	            	width : "99%",
						    	            	style:"border:none;background:none;",
						    	            	readOnly:true
						    	            }
						    	            ]
						       },{
									align : 'center',
									layout : 'form',
									defaultType : 'textfield',
									baseCls : 'x-plain',
									columnWidth : .3,
									items : [
										{
											name : 'specificationModel', 
											fieldLabel : '配件型号', 
											width : "99%",
											style:"border:none;background:none;",
											readOnly:true
										}
									]
								},{
									align : 'center',
									layout : 'form',
									defaultType : 'textfield',
									baseCls : 'x-plain',
									columnWidth : .3,
									items : [
										{
											name : 'nameplateNo', 
											fieldLabel : '配件编号', 
											width : "99%",
											style:"border:none;background:none;",
											readOnly:true
										}
									]
								}
						  ]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .3,
								items : [
									{
										name : 'faultName', 
										fieldLabel : '故障现象', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .7,
								items : [
									{
										name : 'faultDesc', 
										fieldLabel : '故障描述', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
								{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .3,
								items : [
									{
										name : 'faultOccurDate', 
										fieldLabel : '故障发生日期', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},{
									align : 'center',
									layout : 'form',
									defaultType : 'textfield',
									baseCls : 'x-plain',
									columnWidth : .7,
									items : [
										{
											name : 'fixPlaceFullName', 
											fieldLabel : '故障位置', 
											width : "99%",
											style:"border:none;background:none;",
											readOnly:true
										}
									]
								}
								]
					}		
				]
			}
		]/*,
		buttons : [
			{
				text : '确认',
				iconCls:"checkIcon",
				handler : function(){
					var itsStoreCount = Ext.getCmp('its').view.store.getCount();	//获取右边的值的个数
	            	var selValue = Gzpg.isForm.getForm().findField('itemselector').getValue();	//获取右边的值
	                jQuery.ajax({
	                	//这个action只是测试从request中取值.. items
						url: ctx + "/faultNotice!getEmpsByOrgId.action",
						//data:{items : Gzpg.isForm.getForm().getValues().itemselector},
						data:{items : selValue},
						dataType:"json",
						type:"post",
						success:function(data){
							alert(selValue);
						}
					});
				}
			},{
				text : '取消',
				iconCls:'closeIcon',
				handler : function(){}
			}
		]*/
	});
	
	
	function loadAjax(workitemId, faultIdx){
		
		//获取流程基本信息
		jQuery.ajax({
			url: ctx + "/processTask!getBeseInfo.action",
			data:{workitemid: workitemId},
			dataType:"json",
			type:"post",
			success:function(data){
				/*if(data.xcIdx != sxIdx){
					Ext.getCmp("btnApplyToLx").setVisible(false);
					tpType = jt28;
					tpTypeName = dict[jt28];
				} else {
					tpType = jt6;
					tpTypeName = dict[jt6];
				}*/
				var record=new Ext.data.Record();
				for(var i in data){
					record.set(i,data[i]);
				}
				Gzpg.baseForm.getForm().loadRecord(record);
				Ext.getCmp('_workItemName').setValue(data.workItemName);
			}
		});
		//获取提票信息
		jQuery.ajax({
			url: ctx + "/faultNotice!getFaultNoticeBaseInfo.action",
			data:{sourceIdx: faultIdx},
			dataType:"json",
			type:"post",
			success:function(data){
				var record=new Ext.data.Record();	
				var json = data.entity;
				for(var i in json){
					if(i=='faultOccurDate'){
						record.set(i,new Date(json[i]).format('Y-m-d H:m'));
					}else{
						record.set(i,json[i]);
					}
				}
				Gzpg.baseForm.getForm().loadRecord(record);
			}
		});
	}


	Gzpg.confirm = function(){
		 var data = Gzpg.baseForm.getForm().getValues();
		 //删除多余的字段
		 delete data.repairClassName;
		 delete data.processInstName;
		 delete data.trainType;
		 
		 if(!Ext.getCmp('its').view.store.getCount()>0){
		 	Ext.Msg.alert("错误!","未获取到派工人员信息,无法提交派工数据!");
		 	return false;
		 }
		 
		 Ext.Msg.confirm("提示","确认提交",function(btn){
			 if(btn == "yes"){
				 parent.showtip();
		 		 var values = Gzpg.selector.getValue();
		         var records = [];
		         var store = Gzpg.grid.store;
		         for(var i = 0; i < store.getCount(); i++){
		        	 var r = store.getAt(i);
		        	 records.push({key:values, processInstID: r.get("processInstID"),workItemID : r.get("workItemID"), sourceIdx : r.get("sourceIdx")});
		         }
		 		 
				 Ext.Ajax.request({
			        url: ctx +"/faultHandle!batchGZPG.action",
			        params: {entityJson:Ext.util.JSON.encode(records)},
			        success: function(response, options){			            
			        	parent.hidetip();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                parent.hide();	                
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			        	parent.hidetip();
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			        }
			 	});
			 }
		 });
	}
	
	parent.confirm = Gzpg.confirm;
	
	Gzpg.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/processTask!findProcessTask.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/processTask!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/processTask!logicDelete.action',            //删除数据的请求URL
	    saveFormColNum:3, searchFormColNum:2, viewConfig:null,
	    pagingToolbar:false, storeAutoLoad:false,
	    tbar:[{
	    	text:"取消批量",
	    	iconCls:"deleteIcon",
	    	handler:function(){
	    		if(!$yd.isSelectedRecord(Gzpg.grid,true)) return;
	    		var r = Gzpg.grid.selModel.getSelections();
	    		for(var i=0;i<r.length;i++){
	    			Gzpg.grid.store.remove(r[i]);
	    		}
	    		if(Gzpg.grid.store.getCount() == 0){
	    			parent.ProcessTask.handlerWin.hide();
	    		}
	    	}
	    }],
	    fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'车型', dataIndex:'trainType', width:50, editor: { }
		},{
			header:'车号', dataIndex:'trainNo', width:50, editor: { }
		},{
			header:'修程', dataIndex:'repairClassName', width:50, editor: { }
		},{
			header:'工作项名称', dataIndex:'workItemName', editor: { }
		},{
			header:'任务描述', dataIndex:'taskDepict', editor: { }
		},{
			header:'sourceIdx', dataIndex:'sourceIdx', hidden:true, editor: { }
		},{
			header:'配件名称', dataIndex:'partsName', editor: { }		
		},{
			header:'配件型号', dataIndex:'specificationModel', editor: { }
		},{
			header:'序列号', dataIndex:'nameplateNo', editor: { }	
		},{
			header:'ActionURL', dataIndex:'actionUrl', hidden:true, editor: { }		
		},{
			header:'WORKITEMID', dataIndex:'workItemID', hidden:true, editor: { }
		},{
			header:'processInstID', dataIndex:'processInstID', hidden:true, editor: { }
		},{
			header:'key', dataIndex:'key', hidden:true, editor: { }
		},{
			header:'activityInstID', dataIndex:'activityInstID', editor:{ }, hidden:true
		},{
			header:'rdpIdx', dataIndex:'rdpIdx', editor:{ }, hidden:true
		}],
		toEditFn:function(grid,rowIndex){
			var record = this.store.getAt(rowIndex);
			loadAjax(record.get("workItemID"),record.get("sourceIdx"));
		}
	});
	Gzpg.grid.un('rowdblclick', Gzpg.grid.toEditFn);
	Gzpg.grid.on("rowclick", Gzpg.grid.toEditFn);
	var viewport = new Ext.Viewport({
		xtype: "panel", 
		layout: "border",
		items:[			
		    {
				region: 'north', layout: "border", height : Gzpg.formHeight, bodyBorder: false,
				items:[
				  {
					  region:"west",width:"30%", layout:"fit", items: Gzpg.grid
				  },{
					   region:"center", layout:"fit",frame:true, items:[Gzpg.baseForm]
				  }]
		    },{
		        region : 'center', frame:true, layout : {type: 'hbox',align: 'middle ',pack: 'center'}, 
		        bodyBorder: false, items : [Gzpg.selector]
		    }
		    
		]
	});
	
	var r = parent.ProcessTask.waitingHandleGrid.selModel.getSelections();
	for(var i = 0; i < r.length; i++){
		var record = new Ext.data.Record();
		var data = r[i].data;
		for(var j in data){
			record.set(j,data[j]);
		}
		Gzpg.grid.store.add(record);		
	}
	
});