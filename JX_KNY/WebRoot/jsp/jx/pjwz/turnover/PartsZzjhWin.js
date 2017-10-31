Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("PartsZzjhWin");
	//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
	PartsZzjhWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	//表单组件高宽等设置
	PartsZzjhWin.labelWidth = 180;
	PartsZzjhWin.fieldWidth = 260;
	PartsZzjhWin.addlabelWidth = 100;
	PartsZzjhWin.addfieldWidth = 180;
	PartsZzjhWin.currentNodeId = "";
	PartsZzjhWin.workPlanIdx = '';
	PartsZzjhWin.trainTypeAndNo = '';
	PartsZzjhWin.repairClassAndTime = '';
	
	PartsZzjhWin.isHomePage = false;
	
	//设置tbal为不显示
	PartsZzjhWin.setButtonDisable = function(){
//			Ext.getCmp("SC_button").setVisible(false);
//			Ext.getCmp("updatePlanTime_button").setVisible(false);
//			Ext.getCmp("updateRealTime_button").setVisible(false);
//			Ext.getCmp("delete_button").setVisible(false);
//			Ext.getCmp("zxjc").setVisible(true);
//			Ext.getCmp("trainTypeShortName").setVisible(true);   
			PartsZzjhWin.grid.store.load();
	}

	//数据容器
	PartsZzjhWin.store = new Ext.data.JsonStore({
		autoLoad : true,
		root : "root",
		remoteSort : true,
		totalProperty : "totalProperty",
		fields : [ "idx","partsName", "trainno","repairclass","repairtime","wzmc","wzdm","xcpjbh","jhxcsj","sjxcsj","xcPartsAccountIdx","jhscrq","scPartsAccountIdx","workPlanIdx",
					"sjscrq","jhddrq","sjddrq","scpjbm","jhscsj","sjscsj","ywyy","ywsc","partsidx","traintype","offPartsListIdx","updateTime","updatorName","partsIdx"],
	    url: ctx + "/partsZzjh!pageList.action"
	});

	//新增表单
	PartsZzjhWin.addForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:5px",		labelWidth: 200	,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 1, 
	            items: [
					{ xtype:"hidden", id:"workPlanIDXs", name:"workPlanIdx", fieldLabel:"兑现单ID"},
					{ name:"partsName", fieldLabel:"配件名称",maxLength: 50, allowBlank:false,width:PartsZzjhWin.addfieldWidth},
					{ name:"traintype", fieldLabel:"车型",maxLength: 100, allowBlank:false,width:PartsZzjhWin.addfieldWidth },
					{ name:"trainno", fieldLabel:"机车号",maxLength: 100, allowBlank:false,width:PartsZzjhWin.addfieldWidth },
					{ name:"repairclass", fieldLabel:"修程",maxLength: 100, allowBlank:false,width:PartsZzjhWin.addfieldWidth },
					{ name:"repairtime", fieldLabel:"修次",maxLength: 100, allowBlank:false,width:PartsZzjhWin.addfieldWidth },
					{ name:"wzmc", fieldLabel:"位置",maxLength: 100, allowBlank:false,width:PartsZzjhWin.addfieldWidth }
	            ]
	        }]
	    }]
	}),
	
   
	/*编辑表单*/
	PartsZzjhWin.editForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:5px",		labelWidth: 200	,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:120,
	            columnWidth: 3, 
	            items: [
	            	{ name:"idx",xtype:"hidden"},
					{ name:"partsName",xtype:"hidden"},
					{ name:"trainno",xtype:"hidden"},
					{ name:"repairclass",xtype:"hidden"},
					{ name:"repairtime",xtype:"hidden"},
					{ name:"wzmc",xtype:"hidden"},
					{ name:"wzdm",xtype:"hidden"},
					{ name:"traintype",xtype:"hidden"},
					{ name:"xcPartsAccountIdx",xtype:"hidden"},
					{ name:"scPartsAccountIdx",xtype:"hidden"},
					{ name:"offPartsListIdx",xtype:"hidden"},
					{ name:"workPlanIdx",xtype:"hidden"},
					{ name:"partsIdx",xtype:"hidden"},
					{ name:"xcpjbh", fieldLabel:"下车编号",maxLength: 50, width:PartsZzjhWin.addfieldWidth},
					{ name:"scpjbm", fieldLabel:"上车编号",maxLength: 50, width:PartsZzjhWin.addfieldWidth },
					{ name:"jhxcsj", fieldLabel:"下车计划",maxLength: 100, width:PartsZzjhWin.addfieldWidth,xtype: "my97date"},
					{ name:"sjxcsj", fieldLabel:"下车实际",maxLength: 100, width:PartsZzjhWin.addfieldWidth,xtype: "my97date" },
					{ name:"jhscsj", fieldLabel:"上车计划",maxLength: 100, width:PartsZzjhWin.addfieldWidth,xtype: "my97date" },
					{ name:"sjscsj", fieldLabel:"上车实际",maxLength: 100, width:PartsZzjhWin.addfieldWidth,xtype: "my97date" },				
					{ name:"jhscrq", fieldLabel:"送出计划",maxLength: 100, width:PartsZzjhWin.addfieldWidth,xtype: "my97date" },
					{ name:"sjscrq", fieldLabel:"送出实际",maxLength: 100, width:PartsZzjhWin.addfieldWidth ,xtype: "my97date"},
					{ name:"jhddrq", fieldLabel:"到段计划",maxLength: 100, width:PartsZzjhWin.addfieldWidth ,xtype: "my97date"},
					{ name:"sjddrq", fieldLabel:"到段实际",maxLength: 100, width:PartsZzjhWin.addfieldWidth ,xtype: "my97date"},
					{ name:"ywsc", fieldLabel:"延误时长（分钟）",maxLength: 10,xtype:'numberfield', maxLength:8, vtype:'positiveInt', width:PartsZzjhWin.addfieldWidth },
					{ name:"ywyy", xtype:'textarea' ,fieldLabel:"延误原因",maxLength: 50, width:PartsZzjhWin.addfieldWidth }
	            ]
	        }
	        ]
	    }]
	});
	//修改面板
	PartsZzjhWin.editwin = new Ext.Window({
	    title:"修改", width: 350, height: 410, plain: true, closeAction: "hide",align: "center",
	    items: PartsZzjhWin.editForm,buttonAlign: "center",
	    buttons: [{
	        id: "searchBtn", text: "保存", iconCls: "saveIcon",
	        handler: function(){  
	      		var form = PartsZzjhWin.editForm.getForm();
	            if (!form.isValid()) return;
	            var data = form.getValues();
	            var url = ctx + "/partsZzjh!saveOrUpdate.action";
	        	PartsZzjhWin.loadMask.show();
	           	Ext.Ajax.request({
	               url: url,
	               jsonData: data,
	               success: function(response, options){
	               		PartsZzjhWin.loadMask.hide();
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                    	alertSuccess();
	                    	PartsZzjhWin.editwin.hide();
	                    	PartsZzjhWin.grid.store.reload();
	                   	} else {
	                       alertFail(result.errMsg);
	                   	}
	               	},
	               	failure: function(response, options){
	                   PartsZzjhWin.loadMask.hide();
	                   MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	               }
	           });       
	        }
	    }, {
	        text:"关闭", iconCls:"closeIcon",
	        handler:function(){	PartsZzjhWin.editwin.hide()}
	    }]
	});


	//新增面板
	PartsZzjhWin.addwin = new Ext.Window({
	    title:"新增", width: 300, height: 250, plain: true, closeAction: "hide",
	    items: PartsZzjhWin.addForm, buttonAlign: "center",
	    buttons: [{
	        id: "searchBtn", text: "保存", iconCls: "saveIcon",
	        handler: function(){  
	      		var form = PartsZzjhWin.addForm.getForm();
	            if (!form.isValid()) return;
	            var data = form.getValues();
	            var url = ctx + "/partsZzjh!saveOrUpdate.action";
	        	PartsZzjhWin.loadMask.show();
	           	Ext.Ajax.request({
	               url: url,
	               jsonData : data,
	               success : function(response, options){
	               		PartsZzjhWin.loadMask.hide();
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                    	alertSuccess();
	                    	PartsZzjhWin.addwin.hide();
	                    	PartsZzjhWin.grid.store.reload();
	                   	} else {
	                       alertFail(result.errMsg);
	                   	}
	               	},
	               	failure: function(response, options){
	                   PartsZzjhWin.loadMask.hide();
	                   MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	               }
	           });       
	        }
	    }, {
	        text:"关闭", iconCls:"closeIcon",
	        handler:function(){	PartsZzjhWin.addwin.hide()}
	    }]
	});

	//选择模式，勾选框可多选
	PartsZzjhWin.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//分页工具
	PartsZzjhWin.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsZzjhWin.store});
	//Grid
	PartsZzjhWin.grid = new Ext.grid.GridPanel({
	    border: false, layout:'fit',loadMask:true,
	//    viewConfig: {forceFit: true},
//	    height: document.documentElement.scrollHeight,
	    enableColumnMove: true,
	    loadMask: {msg:"正在加载表格数据，请稍等..."},
	    stripeRows: false,
	   	selModel: PartsZzjhWin.sm,
	    colModel: new Ext.grid.ColumnModel([
	     	PartsZzjhWin.sm,
	        new Ext.grid.RowNumberer(),
	        {  header:"ID", dataIndex:"idx",hidden:true },			
	        {  header:"配件名称", dataIndex:"partsName",width:120},			
	        {  header:"机车号", dataIndex:"trainno" ,width:90,hidden:true,
	        renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
					return record.data.traintype + " " + record.data.trainno;
			}},			
			{  header:"修程修次", width:90,hidden:true,
				renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
					return record.data.repairclass + " " + record.data.repairtime;
			}},
	        {  header:"位置", dataIndex:"wzmc" ,width:80},			
	        {  header:"下车编号", dataIndex:"xcpjbh",width:80,
	        	renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
		     		var html = "";	 
		  			html = "<span><a href='#' onclick='PartsAccountOrRdp.showPartsRdpOrDetail(\""+ value + "\",\""+ record.data.xcPartsAccountIdx +"\")'>"+value+"</a></span>";
			      	return value != null? html:"";
		    }},
	        { header:"上车编号", dataIndex:"scpjbm",width:80,
	       		renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
			     		var html = "";	 
			  			html = "<span><a href='#' onclick='PartsAccountOrRdp.showPartsRdpOrDetail(\""+ value + "\",\""+ record.data.scPartsAccountIdx +"\")'>"+value+"</a></span>";
			      		if(value != null){
				      		return html;
			      		}else{
			      			return null;
			      		}}
			      		},
	        { header:"下车计划", dataIndex:"jhxcsj",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"下车实际", dataIndex:"sjxcsj",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"上车计划", dataIndex:"jhscsj",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"上车实际", dataIndex:"sjscsj",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"送出计划", dataIndex:"jhscrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"送出实际", dataIndex:"sjscrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"到段计划", dataIndex:"jhddrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"到段实际", dataIndex:"sjddrq",xtype: "datecolumn",format: "Y-m-d",width:90},
	        { header:"延误时长", dataIndex:"ywsc",width:90,
	        renderer: function(value, metaData, record, rowIndex, colIndex, store){
					metaData.attr = 'style="white-space:normal;"';
					return value;
				}
			},
	        { header:"延误原因", dataIndex:"ywyy",width:120,renderer: function(value, metaData, record, rowIndex, colIndex, store){
					metaData.attr = 'style="white-space:normal;"';
					return value;
				}
			},
	        { header:"修改人", dataIndex:"updatorName",width:90},
	        { header:"修改时间", dataIndex:"updateTime",xtype: "datecolumn",format: "Y-m-d",width:90}
	    ]),
	    store: PartsZzjhWin.store,
	    //工具栏
	    tbar: [{
	        	id:"zxjc", xtype:"label", text:"  在修机车：" ,hidden:true
	    	},{
	        	xtype:"hidden", id:"trainTypeIDX" 
	    	},{
	        	xtype:"hidden", id:"trainNo" 
	    	},{
	        	xtype:"hidden", id:"repairClassName" 
	    	},{
	        	xtype:"hidden", id:"repairtimeName" 
	    	},{
	        	xtype:"hidden", id:"workPlanIDX" 
	    	},{			
	            id: 'trainTypeShortName',
				xtype:"Base_combo",
				hidden:true, 
				entity:"com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan",		
				business:"partsZzjh",
				queryHql:"from TrainWorkPlan where workPlanStatus = 'ONGOING'",
				fields:['trainTypeShortName',"trainNo","trainTypeIDX","repairClassName","repairtimeName","idx"],
				returnField:[{widgetId:"trainTypeIDX",propertyName:"trainTypeIDX"},
				             {widgetId:"trainNo",propertyName:"trainNo"},
				             {widgetId:"workPlanIDX",propertyName:"idx"},
				             {widgetId:"repairClassName",propertyName:"repairClassName"},
				             {widgetId:"repairtimeName",propertyName:"repairtimeName"},
				             {widgetId:"trainTypeShortName",propertyName:"trainTypeShortName"}],
				displayField:"trainTypeShortName",
				valueField: "trainTypeShortName",
				isAll: 'yes',
				listeners : {
					"select" : function() {
//						PartsZzjhsc.traintypeIdx = Ext.getCmp("trainTypeIDX").getValue();//车型主键
//						PartsZzjhsc.trainno = Ext.getCmp("trainNo").getValue();//车号
//						PartsZzjhsc.trainshortname = Ext.getCmp("trainTypeShortName").getValue();//车型名称
//						PartsZzjhsc.repairClassName = Ext.getCmp("repairClassName").getValue();//修程
//						PartsZzjhsc.repairtimeName = Ext.getCmp("repairtimeName").getValue();//修次
//						PartsZzjhsc.workPlanIdx = Ext.getCmp("workPlanIDX").getValue();
//						var type =Ext.getCmp("trainTypeShortName").getValue();
					    var searchParam = {workPlanIdx:Ext.getCmp("workPlanIDX").getValue()};
					    PartsZzjhWin.store.load({
					        params: {entityJson: Ext.util.JSON.encode(searchParam)}
					    });
					    Ext.getCmp("add_button").disabled = false;
					    Ext.getCmp("SC_button").disabled = false;
					}
				}
			},'-',{
	        	xtype:"label", text:"  配件名称：" 
	    	},{			
	            xtype:"JobProcessDefSelect",
	            id:"search_pjmc",width:200,
				name: 'processName', editable:false, allowBlank: false,
				onTriggerClick:function(){
					PartsWin.win.show();
				}
			},'-',{
	        text:"查询", iconCls:"searchIcon", handler: function(){   
	        	var type =Ext.getCmp("trainTypeShortName").getValue();
	        	var planIdx ;
	        	if(zajcIdx != "" && zajcIdx != "null"){
	        		planIdx = zajcIdx;
	        	}else{
	        		planIdx = Ext.getCmp("workPlanIDX").getValue();
	        	}
			    var searchParam = {workPlanIdx:planIdx,partsName:Ext.getCmp("search_pjmc").getValue(),traintype:type.substring(0,type.indexOf("|"))};
			    PartsZzjhWin.store.load({
			        params: {entityJson: Ext.util.JSON.encode(searchParam)}
			    });
		
	       	}
	    },'-',
	    {
	        text:"新增", iconCls:"addIcon",id:"add_button", hidden:true, handler: function(){
	        	Ext.getCmp("workPlanIDXs").setValue(zajcIdx);
	        	PartsZzjhWin.addwin.show();
	        }
	    },{
	        text:"生成周转计划", id:"SC_button", hidden:true,iconCls:"acceptIcon",
	        handler: function(){
	        	PartsZzjhsc.toEditFn();
	        }
	    },{
	        text:"更新计划时间", iconCls:"acceptIcon",id:"updatePlanTime_button",hidden:true,
	        handler: function(){
	        	 if (!$yd.isSelectedRecord(PartsZzjhWin.grid)) return;
	        	 var s = $yd.getSelectedIdx( PartsZzjhWin.grid, PartsZzjhWin.grid.storeId);
	//        	 if(s.length > 1 ){
	//        	 	MyExt.Msg.alert("请选择一条数据进行更新操作！");
	//        	 }else{
					Ext.Msg.confirm("提示  ", "确定更新计划时间，是否继续？  ", function(btn) {
						if (btn != 'yes') return;
						if (self.loadMask) self.loadMask.show();
						Ext.Ajax.request({
							url: ctx + "/partsZzjh!updatePlanTime.action",
							params: {
								ids: $yd.getSelectedIdx( PartsZzjhWin.grid, PartsZzjhWin.grid.storeId)
							},
							success: function(response, options) {
								if (self.loadMask) self.loadMask.hide();
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.success == true) {
									PartsZzjhWin.grid.store.reload();
									MyExt.Msg.alert("操作成功！");
								} else {
									alertFail(result.errMsg);
								}
							},
							failure: function(response, options) {
								if (self.loadMask) self.loadMask.hide();
								MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
							}
						});
					});
	//			}
	        }
	    },{
	        text:"更新实际时间", iconCls:"acceptIcon",id:"updateRealTime_button",hidden:true,
	        handler: function(){
	        	if (!$yd.isSelectedRecord(PartsZzjhWin.grid)) return;
	        	var s = $yd.getSelectedIdx( PartsZzjhWin.grid, PartsZzjhWin.grid.storeId);
	//        	 if(s.length > 1 ){
	//        	 	MyExt.Msg.alert("请选择一条数据进行更新操作！");
	//        	 }else{
					Ext.Msg.confirm("提示  ", "确定更新实际时间，是否继续？  ", function(btn) {
						if (btn != 'yes') return;
						if (self.loadMask) self.loadMask.show();
						Ext.Ajax.request({
							url: ctx + "/partsZzjh!updateRealTime.action",
							params: {
								ids: $yd.getSelectedIdx( PartsZzjhWin.grid, PartsZzjhWin.grid.storeId)
							},
							success: function(response, options) {
								if (self.loadMask) self.loadMask.hide();
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.success == true) {
									PartsZzjhWin.grid.store.reload();
									MyExt.Msg.alert("操作成功！");
								} else {
									alertFail(result.errMsg);
								}
							},
							failure: function(response, options) {
								if (self.loadMask) self.loadMask.hide();
								MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
							}
						});
					});
	//			}
	        }
	    },{
	        text:"删除", iconCls:"deleteIcon",id:"delete_button",hidden:true,
	        handler: function(){
	            if (!$yd.isSelectedRecord(PartsZzjhWin.grid)) return;
					Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn) {
						if (btn != 'yes') return;
						if (self.loadMask) self.loadMask.show();
						Ext.Ajax.request({
							url: ctx + "/partsZzjh!delete.action",
							params: {
								ids: $yd.getSelectedIdx( PartsZzjhWin.grid, PartsZzjhWin.grid.storeId)
							},
							success: function(response, options) {
								if (self.loadMask) self.loadMask.hide();
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.success == true) {
									PartsZzjhWin.grid.store.reload();
									MyExt.Msg.alert("删除操作成功！");
								} else {
									alertFail(result.errMsg);
								}
							},
							failure: function(response, options) {
								if (self.loadMask) self.loadMask.hide();
								MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
							}
						});
					});
	        }
	    },'->',{
	        	id:'label_train',xtype:"label", text:"",hidden:false
	    	}],
	    bbar: PartsZzjhWin.pagingToolbar,
	    listeners: {
	        "rowdblclick": {
				fn: function(grid, idx, e){
					if(PartsZzjhWin.workPlanIdx != "" &&　PartsZzjhWin.workPlanIdx　!= null && PartsZzjhWin.workPlanIdx != "null"){
						PartsZzjhWin.editwin.show();
						var r = grid.store.getAt(idx);
						PartsZzjhWin.editForm.getForm().reset();
			            PartsZzjhWin.editForm.getForm().loadRecord(r);
					}
				}  
	         }
	      }
	});
	
	PartsZzjhWin.grid.store.on('beforeload', function(node){
		var searchParams = {};
		searchParams.workPlanIdx = PartsZzjhWin.workPlanIdx; 
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
});