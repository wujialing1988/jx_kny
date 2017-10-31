/**
 * 故障提票form页面js
 */
function showtip(){
	processTips = new Ext.LoadMask(document.body,{
		msg:"正在处理你的操作，请稍后...",
		removeMask:true
	});
	processTips.show();
}

function hidetip(){
	processTips.hide();
}

Ext.onReady(function(){
Ext.namespace('FaultTicketWin');                       //定义命名空间
FaultTicketWin.fieldWidth = 180;
FaultTicketWin.labelWidth = 80;
FaultTicketWin.buildUpPlaceIdx = "" ; // 分类编码
FaultTicketWin.anchor = '95%';

FaultTicketWin.vehicleType = null ; // 客货类型

//数据容器
FaultTicketWin.faultStore = new Ext.data.JsonStore({
		id : "faultName",
		//root : "root",
		//totalProperty : "totalProperty",
		autoLoad : true,
		//sortInfo : {field:'faultId',direction:'ASC'},
		url : ctx + "/jcgxBuildQuery!getFlbmFault.action",
		fields : [ 'faultId' , 'faultName']
});
//特殊select扩展控件（故障现象列表）
FaultTicketWin.multiSelect = new Ext.ux.Multiselect({
    fieldLabel: '常见现象',
    name: 'faultID',
    width: '100%',
    anchor: '95%',
    autoHeight: true,
    height: 120,
//    FaultTicketWin.multiSelect.scroll("t",200):ture,
//    layout:'anchor',
    autoScroll:true,
//    style: 'overflow-y:hidden;',
//    allowBlank:false,
    valueField : "faultId",
	displayField : "faultName",
    store: FaultTicketWin.faultStore,
    //value: OTHERID, 
    tbar: new Ext.Toolbar({
    	width: 450,
    	items: [{
	    	 text: '选择故障字典', iconCls: 'addIcon',
	    	 handler: function(){
	    	   if(Ext.isEmpty(FaultTicketWin.buildUpPlaceIdx)){
					MyExt.Msg.alert("请先选择组成！");    	
					return ;
	    	   }
	           FaultTicketWin.win.show();
	           FaultTicketWin.faultSelectGrid.store.load({
			        params: { fixPlaceIdx: FaultTicketWin.buildUpPlaceIdx }       
			    });
	        }
    	}, '->',{   xtype:"label", text:"输入常见故障： "},{id: "customFaultName", xtype: "textfield", maxLenth: 50, width: 100},{
	    	 text: '保存', iconCls: 'saveIcon',
	    	 handler: function(){
        		var data = {};
        		var customFaultName = Ext.getCmp("customFaultName").getValue();
        		if (!customFaultName) {
        			MyExt.Msg.alert("请输入常见故障！");
					return ;
        		}
        		if (customFaultName.length > 50) {
        			MyExt.Msg.alert("常见故障不能超过50个字符！");
					return ;
        		}
        		data.flbm = FaultTicketWin.buildUpPlaceIdx;
        		data.faultId = CUSTOMID;
        		data.faultName = customFaultName;

        		Ext.Ajax.request({
	                url: ctx + "/jcxtflFault!saveFlbmFaultCus.action",
	                jsonData: Ext.util.JSON.encode(data),
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FaultTicketWin.faultSelectGrid.getStore().reload();
	                        //点击位置后刷新故障现象列表
	                        var paramObj = {};
	                        paramObj.flbm = FaultTicketWin.buildUpPlaceIdx;
							FaultTicketWin.faultStore.load({
						        params: { entityJson : Ext.util.JSON.encode(paramObj)},
						        callback: function(records){
						        	if (records && records.length > 0) {
							            FaultTicketWin.multiSelect.setValue(records[0].data.faultId);
							            FaultTicketWin.editForm.find("name","faultName")[0].setValue(records[0].data.faultName);
						        	}
						        }
						    });
	                        FaultTicketWin.win.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
	    	 }
    	},{text: '清除', iconCls: 'cancelIcon',
	    	 handler: function(){
	    	 	Ext.getCmp("customFaultName").setValue("");
	    	 }
	    }]
    }),
    listeners:{
    	'click' : function(vw, e){
    		var faultName = e.target.innerText;
    		FaultTicketWin.editForm.find("name","faultName")[0].setValue(faultName);
        },//双击才设置故障描述的值
        'dblclick':function(vw, index, node, e){
        	//var vs = this.getRawValue();
    		var faultName = e.target.innerText;
    		/*
    		if(vs.length > 1){
    			FaultTicketWin.multiSelect.setValue(vs[vs.length-1]); //如果多行选择时，选择最后一行
    			faultName = this.store.query('faultId',vs[vs.length-1]).items[0].data.faultName ;
    		}else{
	    		faultName = this.store.query('faultId',this.getValue()).items[0].data.faultName ;
    		}
    		*/
    		FaultTicketWin.editForm.find("name","faultName")[0].setValue(faultName);
    		// 选择多个常见故障时，需要叠加显示
    		var faultDesc = FaultTicketWin.editForm.find("name","faultDesc")[0].getValue();
    		FaultTicketWin.editForm.find("name","faultDesc")[0].setValue(faultDesc+faultName);
        }
    },
    ddReorder: true
});

//树点击事件的方法
TrainBuild.clickFn = function(node,e){
	var form = FaultTicketWin.editForm ;
	FaultTicketWin.buildUpPlaceIdx = node.attributes["flbm"] ; // 分类编码
    //清空并设置专业类型数据 
    Ext.getCmp('professionalType_').clear('professionalType_');
    Ext.getCmp('professionalTypeName_').setValue("");
    if(node.attributes["zylxID"]){
	    Ext.getCmp('professionalType_').setDisplayValue(node.attributes["zylxID"],node.attributes["zylx"]);
        Ext.getCmp('professionalTypeName_').setValue(node.attributes["zylx"]);
    }
	form.find("name",'faultFixPlaceIDX')[0].setValue(FaultTicketWin.buildUpPlaceIdx); //故障位置主键
	form.find("name",'fixPlaceFullCode')[0].setValue(FaultTicketWin.buildUpPlaceIdx);//node.getPath("wzdm").substring(1)
	form.find("name",'fixPlaceFullName')[0].setValue(node.getPath("text").substring(1));
	form.find("name","faultDesc")[0].setValue("");
	form.find("name","faultDesc")[0].clearInvalid();
	//点击位置后刷新故障现象列表
	
	var paramObj = {};
    paramObj.flbm = FaultTicketWin.buildUpPlaceIdx;
	FaultTicketWin.faultStore.load({
        params: { entityJson : Ext.util.JSON.encode(paramObj)},
        callback: function(records){
        	if (records && records.length > 0) {
	            FaultTicketWin.multiSelect.setValue(records[0].data.faultId);
	            form.find("name","faultName")[0].setValue(records[0].data.faultName);
        	}
        }
    });
}

FaultTicketWin.addToTempGrid = function(data){
	var record = new Ext.data.Record();
	for(var i in data){
		if(i== "faultOccurDate"){
			var d = new Date(data[i].replace(/-/g,"/"));
			record.set(i, d);
			continue;
		}
		record.set(i, data[i]);
	}
	FaultTicketWin.storeFaultCheck(record);
}

FaultTicketWin.storeFaultCheck = function(record){
	var pathName = record.get('fixPlaceFullName');
	var res = FaultTicketWin.faultTempGrid.store.query('fixPlaceFullName',pathName);
	//Store中已存在一条或一条以上
	if(res.length>0 && !FaultTicketWin.hasReminded){
		Ext.Msg.confirm("提示" ,"<font style='color:red;'>列表中已存在相同位置上的提票信息！" +
    			"</br>   是否继续添加提票信息！</font>" ,function(btn){ 
    		if(btn=="yes"){
    			FaultTicketWin.faultTempGrid.store.add(record);
    		}
    	});
	} else {
		FaultTicketWin.faultTempGrid.store.add(record);
	}
	FaultTicketWin.hasReminded = false;
	hidetip();
}
	
FaultTicketWin.del = function del(){
	var record = FaultTicketWin.faultTempGrid.getSelectionModel().getSelected();
	FaultTicketWin.faultTempGrid.store.remove(record);
}

// 刷新故障选择数据
FaultTicketWin.freshFaultDataFun ;

//故障选择
FaultTicketWin.faultSelectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/equipFault!faultList.action',                 //装载列表数据的请求URL
    storeAutoLoad: false,
    tbar: [{
        	xtype:"label", text:"  故障名称：" 
    	},{			
            xtype: "textfield",    
            id: "fault_searchId",
            enableKeyEvents: true,
            listeners: {
            	"keyup":function(){
            		clearTimeout(FaultTicketWin.freshFaultDataFun);
            		FaultTicketWin.freshFaultDataFun = setTimeout(function(){
            			var faultName = Ext.getCmp("fault_searchId").getValue();
	            		var searchParam = {};
						searchParam.FaultName = faultName;					
						FaultTicketWin.faultSelectGrid.getStore().load({
							params:{
								entityJson:Ext.util.JSON.encode(searchParam),
								fixPlaceIdx:FaultTicketWin.buildUpPlaceIdx//位置主键
							}																
						});
            		},500);
            	}
            }
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var faultName = Ext.getCmp("fault_searchId").getValue();
				var searchParam = {};
				searchParam.FaultName = faultName;					
				FaultTicketWin.faultSelectGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:FaultTicketWin.buildUpPlaceIdx//位置主键
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(FaultTicketWin.faultSelectGrid)) return;
        		var tempData = FaultTicketWin.faultSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.flbm = FaultTicketWin.buildUpPlaceIdx;
        			data.faultId = tempData[ i ].get("FaultID");
        			data.faultName = tempData[ i ].get("FaultName");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/jcxtflFault!saveFlbmFault.action",
	                params: {entityJson : Ext.util.JSON.encode(dataAry)},
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        FaultTicketWin.faultSelectGrid.getStore().reload();
	                        //点击位置后刷新故障现象列表
	                        var paramObj = {};
	                        paramObj.flbm = FaultTicketWin.buildUpPlaceIdx;
							FaultTicketWin.faultStore.load({
						        params: { entityJson : Ext.util.JSON.encode(paramObj)},
						        callback: function(records){
						        	if (records && records.length > 0) {
							            FaultTicketWin.multiSelect.setValue(records[0].data.faultId);
							            FaultTicketWin.editForm.find("name","faultName")[0].setValue(records[0].data.faultName);
						        	}
						        }
						    });
	                        FaultTicketWin.win.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
			}
		}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'故障编号', dataIndex:'FaultID', editor:{   }
	},{
		header:'故障名称', dataIndex:'FaultName', editor:{   }
	},{
		header:'故障类别', dataIndex:'FaultTypeID', editor:{ xtype:'hidden'   }
	}]
});
//移除双击事件
FaultTicketWin.faultSelectGrid.un('rowdblclick', FaultTicketWin.faultSelectGrid.toEditFn, FaultTicketWin.faultSelectGrid);
//故障选择窗口
FaultTicketWin.win = new Ext.Window({
    title: "常见故障选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [FaultTicketWin.faultSelectGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ FaultTicketWin.win.hide(); }
            }]
});

//故障提票编辑页面form
FaultTicketWin.editForm = new Ext.form.FormPanel({
    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: FaultTicketWin.labelWidth, 
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" }, buttonAlign: "center",
    buttonAlign: "center",autoScroll:true,  labelAlign : 'right',
    items: [{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: FaultTicketWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: FaultTicketWin.anchor },
            items: [{
                	name: "trainTypeShortName", fieldLabel: "车型",  maxLength: 50, style:"color:gray", readOnly:true
                }]
       		},{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: FaultTicketWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: FaultTicketWin.anchor },
            items: [
				{ name: "trainNo", fieldLabel: "车号",  maxLength: 50, style:"color:gray", readOnly:true}
            ]
       }]    
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: FaultTicketWin.labelWidth,      columnWidth: 1, 
            items: [{ name: "fixPlaceFullName", fieldLabel: "故障位置",allowBlank:false, style:"color:gray",  maxLength: 500, anchor: "98%" ,readOnly:true}]
       }]
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: FaultTicketWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: FaultTicketWin.anchor },
            items: [
            	{ name: "type", fieldLabel: "提票类型",  maxLength: 50, style:"color:gray", readOnly:true},
            	{ 
                	name: "faultReason", fieldLabel: "故障原因"
                },{
                id:'discoverer_Name', name: "discover", fieldLabel: "发现人", xtype:"hidden" ,value:empName},
            	{id:"discoverer_Id", fieldLabel:"发现人", xtype: 'OmEmployee_SelectWin', hiddenName: 'discoverID',
		        	returnField:[{widgetId:"discoverer_Name",propertyName:"empname"}], displayField:'empname', valueField:'empid', editable:false }
		        ]
       		},{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: FaultTicketWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: FaultTicketWin.anchor },
            items: [
            	{
                	id: "professionalType_", 
		            xtype: "ProfessionalType_comboTree", 
		            fieldLabel: "专业类型",
		            hiddenName: "professionalTypeIdx", 
		            returnField: [
		            	{widgetId : "professionalTypeName_", propertyName : "text"}		            	
		            ], 
		            selectNodeModel: "all"
                },{ 
                	id : 'professionalTypeName_', name: "professionalTypeName", fieldLabel: "专业类型名称",  maxLength: 50, style:"color:gray", hidden :true
                },{  
                	 xtype: 'Base_multyComboTree',hiddenName: 'reasonAnalysisID', isExpandAll: false,
					 fieldLabel:'原因分析',returnField:[{widgetId:"reasonAnalysis_id",propertyName:"text"}],
					 selectNodeModel:'leaf',
					 treeUrl: ctx + '/eosDictEntrySelect!queryChildTree.action', rootText: '原因类型', 
					 queryParams: {'dicttypeid':'JXGC_Fault_Ticket_YYFX'}
            	},{ name: "faultOccurDate", fieldLabel: "故障发现时间",xtype: "my97date",
					format: "Y-m-d H:i",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm",maxDate:'%y-%M-%d' },
					validator :function(v){
						if(v) return true;
						else return "不允许为空";
							
					}
				}
            ]
       }]
    },
    {
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: FaultTicketWin.labelWidth,columnWidth: 1, anchor: "100%",
            items: [FaultTicketWin.multiSelect]
        }]
    },
    {
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: FaultTicketWin.labelWidth,      columnWidth: 1, 
            items: [{
            	 name: "faultDesc", fieldLabel: "故障现象",allowBlank:false, xtype:'textarea',height:45, maxLength: 500,anchor: "98%",
                        listeners : {
                            'change'  : function(field, newValue, oldValue ){
                                this.setValue(newValue.trim());
                            } 
                        }
       		}]
        }]
    },
    {xtype:"hidden", name:"reasonAnalysis",id:"reasonAnalysis_id"},
    {xtype:"hidden", name:"ticketCode"},
    {xtype:"hidden", name:"trainTypeIDX"},
	{xtype:"hidden", name:"faultName" ,value:'其它'},
	{xtype:"hidden", name:"fixPlaceFullCode"},
	{xtype:"hidden", name:"faultFixPlaceIDX"}
    ]  
});

 
 FaultTicketWin.faultTempGrid = new Ext.yunda.Grid({
    loadURL: "",                 //装载列表数据的请求URL
    storeAutoLoad: false,
    viewConfig: null,
    tbar:[],
    singleSelect: true,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	{
		dataIndex:"" ,header:"操作", width:40, sortable:false,
		renderer:function(v,x,r,i){
			return "<img src='"+imgpath+"/delete.png' alt='删除' onclick='FaultTicketWin.del()' style='cursor:pointer'/>";
		}
	},
	{
		header:'提票单号', dataIndex:'ticketCode', hidden:true, editor:{  maxLength:50 }
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55, sortable:false
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50, sortable:false
	},{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }, sortable:false
	},{
		header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }, sortable:false
	},{
		header:'故障位置', dataIndex:'fixPlaceFullName', editor:{  maxLength:500 }, width : 300, sortable:false
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', editor:{ xtype:'my97date' }, width : 120, sortable:false
	},{
		header:'故障位置ID', dataIndex:'faultFixPlaceIDX', hidden:true, editor:{  maxLength:200 }, sortable:false
	},{
		header:'故障部件编码', dataIndex:'fixPlaceFullCode', hidden:true, editor:{  maxLength:200 }, sortable:false
	},{
		header:'故障ID', dataIndex:'faultID', hidden:true, editor:{  maxLength:8 }, sortable:false
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }, sortable:false
	},{
		header:'专业类型', dataIndex:'professionalTypeName', editor:{  maxLength:100 }, sortable:false
	},{
		header:'原因分析ID', dataIndex:'reasonAnalysisID', hidden:true, editor:{ xtype:'hidden', maxLength:100 }, sortable:false
	},{
		header:'原因分析', dataIndex:'reasonAnalysis', editor:{  maxLength:200 }, width : 300, sortable:false
	},{
		header:'发现人', dataIndex:'discover', editor:{  maxLength:100 }, sortable:false
	},{
		header:'提票人编码', dataIndex:'ticketEmpId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }, sortable:false
	},{
		header:'提票人名称', dataIndex:'ticketEmp', hidden:true, editor:{  maxLength:25 }, sortable:false
	},{
		header:'提票时间', dataIndex:'ticketTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }, sortable:false
	},{
		header:'票活状态', dataIndex:'status', hidden:true, editor:{  maxLength:20 }, sortable:false
	},{
		header:'提票类型', dataIndex:'type', hidden:true, editor:{  maxLength:20 }, sortable:false
	}],
	toEditFn: function(grid, rowIndex, e) {},
	page: false 
});
FaultTicketWin.closeWin = function() {
	//调用js实现此方法
}
FaultTicketWin.panel = {
		xtype: "panel",
	    layout: "border",
    	items:[{
    		 region: 'west', layout: "fit", width: 200,minSize: 260, maxSize: 300,
    		 split: true, bodyBorder: false, items:[ TrainBuild.tree ]
    	},{
    		region: 'center', layout: "fit", bodyBorder: false,frame:true, 
    		items : [ FaultTicketWin.editForm ],
    		buttonAlign:"center",
    		buttons: [{
        		 text: "添加提票信息", iconCls:"addIcon", handler:function(){
					  //表单验证是否通过
			        var form = FaultTicketWin.editForm.getForm();
			        if (!form.isValid()){
			        	 MyExt.Msg.alert("请先添加完善故障信息！");
			        	 return;
			        }
			        
			        showtip();
			        var data = form.getValues();
					delete data.customFaultName;
					
			        Ext.Ajax.request({
				        url: ctx + "/faultTicket!checkData.action",
				        jsonData: data,
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.success == true) {
				            	hidetip();
				            	Ext.Msg.confirm("提示" ,"存在相同位置上的提票还未处理！" +
				            			"</br>   是否继续提票！" ,function(btn){ 
				            		if(btn=="yes"){
				            			showtip();
				            			FaultTicketWin.addToTempGrid(data);
				            		}
				            	});
				            	FaultTicketWin.hasReminded = true;//标识已经提醒了重复提票
				            } else {	
				            	FaultTicketWin.addToTempGrid(data);
				            	FaultTicketWin.hasReminded = false;
				            }
				        }
					});
        		}
        	},{
				text: "提交提票信息", iconCls:"saveIcon", handler:function(){
					if(FaultTicketWin.faultTempGrid.store.getCount() > 0){
						Ext.Msg.confirm("提示","确认提交提票",function(btn){
							if(btn == "yes"){
								showtip();
								var tpArray = new Array();
								for(var i = 0; i < FaultTicketWin.faultTempGrid.store.getCount(); i++){
									var tp = FaultTicketWin.faultTempGrid.store.getAt(i).data;
									if(Ext.isEmpty(tp.vehicleType)){
										tp.vehicleType = FaultTicketWin.vehicleType ;
									}
									delete tp.customFaultName;
									tpArray.push(tp);
							    }								
								var tpjson = Ext.util.JSON.encode(tpArray);
								jQuery.ajax({
									url: ctx + "/faultTicket!saveTpAndInst.action",
									data:{entityJson: tpjson},
									dataType:"json",
									type:"post",
									success:function(data){
										hidetip();
					                    if (data.errMsg == null) {
					                        alertSuccess();
					                        FaultTicketWin.faultTempGrid.store.removeAll();
					                        
					                    } else {
					                        alertFail(data.errMsg);
					                    }
									}
								});
							}
						});
					}else{
						MyExt.Msg.alert("请先添加提票信息");
					}
				}
			},{
				text: "取消", iconCls:"closeIcon", handler:function(){
					if(FaultTicketWin.faultTempGrid.store.getCount() > 0){
						Ext.Msg.confirm("提示","有未提交的提票，要取消吗？",function(btn){
							if(btn == "yes"){
								FaultTicketWin.faultTempGrid.store.removeAll();
								FaultTicketWin.closeWin();
							}
						});
					}else{
						FaultTicketWin.closeWin();
					}
					
					FaultTicketWin.buildUpPlaceIdx = '';
				}
			}]
    	},{
    		 region: 'south', layout: "fit", height: 220,
    		 split: true, bodyBorder: false, items:[FaultTicketWin.faultTempGrid]
    	}]
};
	//初始化
FaultTicketWin.initSaveWin = function(){
	FaultTicketWin.initTree();   
	FaultTicketWin.initForm();
}
FaultTicketWin.initTree = function(){
	TrainBuild.tree.root.setText(trainTypeShortName);
	TrainBuild.tree.root.attributes["fjdID"] = trainTypeShortName;
    TrainBuild.tree.root.reload();
	TrainBuild.tree.getRootNode().expand();
}
FaultTicketWin.initForm = function(){
	var form = FaultTicketWin.editForm.getForm();
	form.reset();
	if (FaultTicketWin.faultStore) {
	    FaultTicketWin.faultStore.removeAll();
	}
	form.findField('trainTypeShortName').setValue(trainTypeShortName);
	form.findField('trainNo').setValue(trainNo);
	form.findField('trainTypeIDX').setValue(trainTypeIDX);
	form.findField('type').setValue(type);
	form.findField('professionalTypeIdx').clearValue();	
	form.findField('professionalTypeName').setValue("");
	form.findField('discoverID').clearValue();
	form.findField('discover').setValue("");
}
});