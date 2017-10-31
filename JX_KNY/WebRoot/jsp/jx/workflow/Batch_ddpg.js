/**
 * 调度派工页面构造
 */
Ext.onReady(function(){	
	Ext.namespace('Scheduling');
	
	if(jQuery("body").height() > 550){
		Scheduling.formHeight = 290;
	}else{
		Scheduling.formHeight = 260;
	}
	Scheduling.height = jQuery("body").height()-Scheduling.formHeight - 20;	
	
	parent.confirm = function(){
		var node = Scheduling.tree.getSelectionModel().selNode;
		if(node == null){
			MyExt.Msg.alert("尚未选择一个班组");
			return;
		}else if(node == Scheduling.tree.root){
			MyExt.Msg.alert("不能选择根节点");
			return;
		}else if(node.leaf == false){
			MyExt.Msg.alert("请选择最后一级");
			return;
		}else{
			Scheduling.submit(node.id);
		}
	};
	Scheduling.submit = function(orgid){
		Ext.Msg.confirm("提示","确认提交",function(btn){
			if(btn == "yes"){
		    	parent.showtip();
		    	
		        var records = [];
		        var store = Scheduling.grid.store;
		        for(var i = 0; i < store.getCount(); i++){
		        	var r = store.getAt(i);
		        	records.push({key: orgid, processInstID: r.get("processInstID"),workItemID : r.get("workItemID"), sourceIdx : r.get("sourceIdx")});
		        }
		        Ext.Ajax.request({
				    url: ctx +"/faultHandle!batchSchedulingWork.action",
				    params: {entityJson:Ext.util.JSON.encode(records)},
				    success: function(response, options){
				    	parent.hidetip();
					    var result = Ext.util.JSON.decode(response.responseText);
					    if (result.errMsg == null) {
					    	parent.hide();//关闭窗口
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
	
	
	/**
	 * 基本表单显示
	 */
	Scheduling.baseForm = new Ext.form.FormPanel({
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
		]
	});
	
	function loadAjax(workitemId,sourceIdx){
		/*
		 * 查询流程基本信息
		 */
		jQuery.ajax({
			url: ctx + "/processTask!getBeseInfo.action",
			data:{workitemid: workitemId},
			dataType:"json",
			type:"post",
			success:function(data){
				var record=new Ext.data.Record();
				for(var i in data){
					record.set(i,data[i]);
				}
				Scheduling.baseForm.getForm().loadRecord(record);
			}
		});
		/*
		 * 查询提票基本信息
		 */
		jQuery.ajax({
			url: ctx + "/faultNotice!getFaultNoticeBaseInfo.action",
			data:{sourceIdx: sourceIdx},
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
				Scheduling.baseForm.getForm().loadRecord(record);
			}
		});
	}
	Scheduling.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/processTask!findProcessTask.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/processTask!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/processTask!logicDelete.action',            //删除数据的请求URL
	    saveFormColNum:3, searchFormColNum:2, viewConfig:null,
	    pagingToolbar:false, storeAutoLoad:false,
	    tbar:[{
	    	text:"取消批量",
	    	iconCls:"deleteIcon",
	    	handler:function(){
	    		if(!$yd.isSelectedRecord(Scheduling.grid,true)) return;
	    		var r = Scheduling.grid.selModel.getSelections();
	    		for(var i=0;i<r.length;i++){
	    			Scheduling.grid.store.remove(r[i]);
	    		}
	    		if(Scheduling.grid.store.getCount() == 0){
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
	Scheduling.grid.un("rowdblclick",Scheduling.grid.toEditFn);
	Scheduling.grid.on("rowclick", Scheduling.grid.toEditFn);
	
	
	Scheduling.tree = new Ext.tree.TreePanel( {
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/faultDispatcher!dispatcherTree.action"
	    }),
	    width:300,
	    height:Scheduling.height,
	    style:'background-color:white',
	    root: new Ext.tree.AsyncTreeNode({
	       	text:"调度派工机构树",
			id:"ROOT_0",
			isLastLevel: 0,
			classID : "",
			parentIDX : "",
			status : 0,
			leaf:false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : false,
	    useArrows : false,
	    border : false
	});
	
	Scheduling.tree.on('beforeload', function(node){
		Scheduling.tree.loader.dataUrl = ctx + '/faultDispatcher!dispatcherTree.action?parent=' + node.id;
	});
	Scheduling.tree.getRootNode().expand();
	
	
	/**
	 * 页面布局
	 */
	var viewport = new Ext.Viewport({
		xtype: "panel", 
		layout: "border",
		items:[
		       {
					region: 'north', layout: "border", height : Scheduling.formHeight, bodyBorder: false,
					items:[
					  {
						  region:"west",width:"30%", layout:"fit", items: Scheduling.grid
					  },{
						   region:"center", layout:"fit",frame:true, items:[Scheduling.baseForm]
					  }]
			    },{
			        region : 'center', autoScroll:true, frame:true, layout : {type: 'hbox',align: 'middle ',pack: 'center'}, bodyBorder: false,
			        //items : [Scheduling.multiSelect, Scheduling.selector,Scheduling.teamSelect]
			        items:[Scheduling.tree], layout : {type: 'hbox',align: 'middle ',pack: 'center'}
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
		Scheduling.grid.store.add(record);		
	}
});