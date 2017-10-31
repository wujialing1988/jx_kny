/**
 * 临碎修提票form页面js
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
Ext.namespace('ZbglTpWin');                       //定义命名空间
ZbglTpWin.fieldWidth = 180;
ZbglTpWin.labelWidth = 80;
ZbglTpWin.buildUpPlaceIdx = "" ; //组成位置主键
ZbglTpWin.anchor = '95%';
ZbglTpWin.warningIDX = '';//机车检测预警IDX
//数据容器
ZbglTpWin.faultStore = new Ext.data.JsonStore({
		id : "faultName",
		//root : "root",
		//totalProperty : "totalProperty",
		autoLoad : true,
		//sortInfo : {field:'faultId',direction:'ASC'},
		url : ctx + "/jcgxBuildQuery!getFlbmFault.action",
		fields : [ 'faultId' , 'faultName']
});
//特殊select扩展控件（故障现象列表）
ZbglTpWin.multiSelect = new Ext.ux.Multiselect({
    fieldLabel: '常见故障',
    name: 'faultID',
    width: '100%',
    anchor: '98%',
    height: 120,
    autoScroll: true,
    autoHeight: true,
    //allowBlank:false,
    valueField : "faultId",
	displayField : "faultName",
    store: ZbglTpWin.faultStore,
    //value: OTHERID,
    tbar: new Ext.Toolbar({
    	width: 650,
    	items: [{
	    	 text: '选择故障字典', iconCls: 'addIcon',
	    	 handler: function(){
	    	   if(Ext.isEmpty(ZbglTpWin.buildUpPlaceIdx)){
					MyExt.Msg.alert("请先选择组成！");    	
					return ;
	    	   }
	           ZbglTpWin.win.show();
	           ZbglTpWin.faultSelectGrid.store.load({
			        params: { fixPlaceIdx: ZbglTpWin.buildUpPlaceIdx }       
			    });
	        }
    	    	}, '->',{   xtype:"label", text:"输入常见故障： "},{id: "customFaultName", xtype: "textfield", maxLenth: 50, width: 300},{
	    	 text: '保存', iconCls: 'saveIcon',
	    	 handler: function(){
	    	 	var dataAry = new Array();        		
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
        		data.flbm = ZbglTpWin.buildUpPlaceIdx;
        		//暂时写死，未发现为什么不能调用原因
        		data.faultId = '-1111';
        		data.faultName = customFaultName;
        		dataAry.push(data);
        		Ext.Ajax.request({
	                url: ctx + "/jcxtflFault!saveFlbmFault.action",
	                params: {entityJson : Ext.util.JSON.encode(dataAry)},
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        ZbglTpWin.faultSelectGrid.getStore().reload();
	                        //点击位置后刷新故障现象列表
							var paramObj = {};
	                        paramObj.flbm = ZbglTpWin.buildUpPlaceIdx;
							ZbglTpWin.faultStore.load({
						        params: { entityJson : Ext.util.JSON.encode(paramObj)},
						        callback: function(records){
						        	if (records && records.length > 0) {
							            ZbglTpWin.multiSelect.setValue(records[0].data.faultId);
							            ZbglTpWin.editForm.find("name","faultName")[0].setValue(records[0].data.faultName);
						        	}
						        }
						    });
	                        ZbglTpWin.win.hide();
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
    		ZbglTpWin.editForm.find("name","faultName")[0].setValue(faultName);
        },//双击才设置故障描述的值
        'dblclick':function(vw, index, node, e){
        	// var vs = this.getRawValue() ;
    		var faultName = e.target.innerText;
    		/*
    		if(vs.length > 1){
    			ZbglTpWin.multiSelect.setValue(vs[vs.length-1]); //如果多行选择时，选择最后一行
    			faultName = this.store.query('faultId',vs[vs.length-1]).items[0].data.faultName ;
    		}else{
	    		faultName = this.store.query('faultId',this.getValue()).items[0].data.faultName ;
    		}
    		*/
    		ZbglTpWin.editForm.find("name","faultName")[0].setValue(faultName);
    		// 选择多个常见故障时，需要叠加显示
    		var faultDesc = ZbglTpWin.editForm.find("name","faultDesc")[0].getValue();
    		ZbglTpWin.editForm.find("name","faultDesc")[0].setValue(faultDesc+faultName);
        }
    },
    ddReorder: true
});

//树点击事件的方法
TrainBuild.clickFn = function(node,e){
	var form = ZbglTpWin.editForm ;
	ZbglTpWin.buildUpPlaceIdx = node.attributes["flbm"] ; //组成位置主键
    //清空并设置专业类型数据 
    Ext.getCmp('professionalType_').clear('professionalType_');
    Ext.getCmp('professionalTypeName_').setValue("");
    if(node.attributes["zylxID"]){
	    Ext.getCmp('professionalType_').setDisplayValue(node.attributes["zylxID"],node.attributes["zylx"]);
        Ext.getCmp('professionalTypeName_').setValue(node.attributes["zylx"]);
    }
//	form.find("name",'faultFixPlaceIDX')[0].setValue(ZbglTpWin.buildUpPlaceIdx); //故障位置主键
	form.find("name",'faultFixFullCode')[0].setValue(ZbglTpWin.buildUpPlaceIdx);//node.getPath("wzdm").substring(1)
	form.find("name",'faultFixFullName')[0].setValue(node.getPath("text").substring(1));
	form.find("name","faultDesc")[0].setValue("");
	form.find("name","faultDesc")[0].clearInvalid();
	//点击位置后刷新故障现象列表，首先清空
	form.find("name","faultName")[0].setValue("");
	var paramObj = {};
    paramObj.flbm = ZbglTpWin.buildUpPlaceIdx;
	ZbglTpWin.faultStore.load({
        params: { entityJson : Ext.util.JSON.encode(paramObj)},
        callback: function(records){
        	if (records && records.length > 0) {
	            ZbglTpWin.multiSelect.setValue(records[0].data.faultId);
	            form.find("name","faultName")[0].setValue(records[0].data.faultName);
        	}
        }
    });
};

ZbglTpWin.addToTempGrid = function(data){
	var record = new Ext.data.Record();
	for(var i in data){
		if(i== "faultOccurDate"){
			var d = new Date(data[i].replace(/-/g,"/"));
			record.set(i, d);
			continue;
		}
		record.set(i, data[i]);
	}
	ZbglTpWin.storeFaultCheck(record);
};

ZbglTpWin.storeFaultCheck = function(record){
	var pathName = record.get('faultFixFullName');
	var res = ZbglTpWin.faultTempGrid.store.query('faultFixFullName',pathName);
	//Store中已存在一条或一条以上
	if(res.length>0 && !ZbglTpWin.hasReminded){
		Ext.Msg.confirm("提示" ,"<font style='color:red;'>列表中已存在相同位置上的提票信息！" +
    			"</br>   是否继续添加提票信息！</font>" ,function(btn){ 
    		if(btn=="yes"){
    			ZbglTpWin.faultTempGrid.store.add(record);
    		}
    	});
	} else {
		ZbglTpWin.faultTempGrid.store.add(record);
	}
	ZbglTpWin.hasReminded = false;
	hidetip();
};
	
ZbglTpWin.del = function del(){
	var record = ZbglTpWin.faultTempGrid.getSelectionModel().getSelected();
	ZbglTpWin.faultTempGrid.store.remove(record);
};

// 刷新故障选择数据
ZbglTpWin.freshFaultDataFun ;

//故障选择
ZbglTpWin.faultSelectGrid = new Ext.yunda.Grid({
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
            		clearTimeout(ZbglTpWin.freshFaultDataFun);
            		ZbglTpWin.freshFaultDataFun = setTimeout(function(){
            			var faultName = Ext.getCmp("fault_searchId").getValue();
	            		var searchParam = {};
						searchParam.FaultName = faultName;					
						ZbglTpWin.faultSelectGrid.getStore().load({
							params:{
								entityJson:Ext.util.JSON.encode(searchParam),
								fixPlaceIdx:ZbglTpWin.buildUpPlaceIdx//位置主键
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
				ZbglTpWin.faultSelectGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:ZbglTpWin.buildUpPlaceIdx//位置主键
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(ZbglTpWin.faultSelectGrid)) return;
        		var tempData = ZbglTpWin.faultSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.flbm = ZbglTpWin.buildUpPlaceIdx;
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
	                        ZbglTpWin.faultSelectGrid.getStore().reload();
	                        //点击位置后刷新故障现象列表
							var paramObj = {};
	                        paramObj.flbm = ZbglTpWin.buildUpPlaceIdx;
							ZbglTpWin.faultStore.load({
						        params: { entityJson : Ext.util.JSON.encode(paramObj)},
						        callback: function(records){
						        	if (records && records.length > 0) {
							            ZbglTpWin.multiSelect.setValue(records[0].data.faultId);
							            ZbglTpWin.editForm.find("name","faultName")[0].setValue(records[0].data.faultName);
						        	}
						        }
						    });
	                        ZbglTpWin.win.hide();
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
ZbglTpWin.faultSelectGrid.un('rowdblclick', ZbglTpWin.faultSelectGrid.toEditFn, ZbglTpWin.faultSelectGrid);
//故障选择窗口
ZbglTpWin.win = new Ext.Window({
    title: "常见故障选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [ZbglTpWin.faultSelectGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglTpWin.win.hide(); }
            }]
});

//故障提票编辑页面form
ZbglTpWin.editForm = new Ext.form.FormPanel({
    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: ZbglTpWin.labelWidth, 
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" }, buttonAlign: "center",
    buttonAlign: "center",autoScroll:true,  labelAlign : 'right',
    items: [{    	
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpWin.anchor },
            items: [{
                	name: "trainTypeShortName", fieldLabel: "车型",  maxLength: 50, style:"color:gray", readOnly:true
                }]
       		},{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpWin.anchor },
            items: [
				{ name: "trainNo", fieldLabel: "车号",  maxLength: 50, style:"color:gray", readOnly:true}
            ]
       }]    
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpWin.labelWidth,      columnWidth: 1, 
            items: [{ name: "faultFixFullName", fieldLabel: "故障位置",allowBlank:false, style:"color:gray",  maxLength: 500, anchor: "98%" ,readOnly:true}]
       }]
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpWin.anchor },
            items: [
                {
                	id: "professionalType_", 
		            xtype: "ProfessionalType_comboTree", 
		            fieldLabel: "专业类型",
		            hiddenName: "professionalTypeIdx", 
		            returnField: [
		            	{widgetId : "professionalTypeName_", propertyName : "text"},
		            	{widgetId : "professionalTypeSeq_", propertyName : "proSeq"}
		            	
		            ], 
		            selectNodeModel: "all"
                },{ 
                	id : 'professionalTypeName_', name: "professionalTypeName", fieldLabel: "专业类型名称",  maxLength: 50, style:"color:gray", hidden :true
                },{ 
                	id : 'professionalTypeSeq_', name: "professionalTypeSeq", xtype: 'hidden'
                },{
					id:"discoverID",fieldLabel:"发现人工号", name: "discoverID",vtype: "numberInt",allowBlank: true,
					enableKeyEvents:true,
					listeners : {
						keyup : function(a,b){
							//通过ajax到后台查询，精确查询
							var pid = a.getValue();//交车司机ID，输入框中的值
							Ext.Ajax.request({
								url: ctx + "/omEmployeeSelect!findEmployeeByCardID.action",
								params: {fromPersonId : pid},
								success: function(r){
									var retn = Ext.util.JSON.decode(r.responseText);
									if(retn.success){
										Ext.getCmp("discover").setValue(retn.fromPersonName);
									}else{
										Ext.getCmp("discover").setValue("");
									}
								},
								failure: function(){
									alertFail("请求超时！");
								}
							});
						}
					}
				}/*,{
                	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", 
		        	items: [ZbglTpWin.multiSelect], anchor: '100%'
                }*/]
       		},{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpWin.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpWin.anchor },
            items: [				
				{   name: "faultOccurDate", fieldLabel: "故障发现时间",xtype: "my97date",
					format: "Y-m-d H:i",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm",maxDate:'%y-%M-%d' },
					validator :function(v){
						if(v) return true;
						else return "不允许为空";
							
					}
				},{
					id:"discover",fieldLabel:"发现人名称", name: "discover",allowBlank: true
				}/*,{ name: "faultDesc", fieldLabel: "故障描述", xtype:'textarea',height:182, maxLength: 500,
                        listeners : {
                            'change'  : function(field, newValue, oldValue ){
                                this.setValue(newValue.trim());
                            } 
                        }
            	}*/
            ]
       }]
    },
    {
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpWin.labelWidth,columnWidth: 1, anchor: "100%",
            items: [ZbglTpWin.multiSelect]
        }]
    },
        {
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpWin.labelWidth,      columnWidth: 1, 
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
    {xtype:"hidden", name:"faultNoticeCode"},
    {xtype:"hidden", name:"trainTypeIDX"},
	{xtype:"hidden", name:"faultName" ,value:'其它'},
	{xtype:"hidden", name:"faultFixFullCode"},
	{xtype:"hidden", name:"dID"},
	{xtype:"hidden", name:"dName"}
    ]  
});

 
 ZbglTpWin.faultTempGrid = new Ext.yunda.Grid({
    loadURL: "",                 //装载列表数据的请求URL
    storeAutoLoad: false,
    viewConfig: null,
    tbar:[],
    singleSelect: true,
    page: false,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	{
		dataIndex:"" ,header:"操作", width:40,
		renderer:function(v,x,r,i){
			return "<img src='"+imgpath+"/delete.png' alt='删除' onclick='ZbglTpWin.del()' style='cursor:pointer'/>";
		}
	},
	{
		header:'提票单号', dataIndex:'faultNoticeCode', hidden:true, editor:{  maxLength:50 }
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
	},{
		header:'配属段编码', dataIndex:'dID', hidden:true, editor:{  maxLength:20 }
	},{
		header:'配属段', dataIndex:'dName', hidden:true, editor:{  maxLength:50 }, width : 80
	},{
		header:'常见故障', dataIndex:'faultName', editor:{  maxLength:50 }
	},{
		header:'故障现象', dataIndex:'faultDesc', editor:{  maxLength:500 }
	},{
		header:'故障位置', dataIndex:'faultFixFullName', editor:{  maxLength:500 }, width : 300
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', editor:{ xtype:'my97date' }, width : 120
	},{
		header:'故障位置编码', dataIndex:'faultFixFullCode', hidden:true, editor:{  maxLength:200 }
	},{
		header:'故障ID', dataIndex:'faultID', hidden:true, editor:{  maxLength:8 }
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'专业类型', dataIndex:'professionalTypeName', editor:{  maxLength:100 }
	},{
		header:'专业类型序列', dataIndex:'professionalTypeSeq', hidden:true,editor:{  maxLength:100 }
	},{
		header:'提票来源', dataIndex:'noticeSource', hidden:true, editor:{  maxLength:50 }
	},{
		header:'提票人编码', dataIndex:'noticePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'提票人名称', dataIndex:'noticePersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'提票时间', dataIndex:'noticeTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'提票站场名称', dataIndex:'siteName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组编码', dataIndex:'revOrgID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'处理班组名称', dataIndex:'revOrgName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组序列', dataIndex:'revOrgSeq', hidden:true, editor:{  maxLength:300 }
	},{
		header:'接票人编码', dataIndex:'revPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接票人名称', dataIndex:'revPersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'施修方法', dataIndex:'methodDesc', hidden:true, editor:{  maxLength:200 }
	},{
		header:'处理结果', dataIndex:'repairResult', hidden:true, editor:{ xtype:'numberfield', maxLength:2 }
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'销票人名称', dataIndex:'handlePersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'销票时间', dataIndex:'handleTime', hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'销票站场', dataIndex:'handleSiteID', hidden:true, editor:{  maxLength:50 }
	},{
		header:'验收人编码', dataIndex:'accPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'验收人名称', dataIndex:'accPersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'验收时间', dataIndex:'accTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'票活状态', dataIndex:'faultNoticeStatus', hidden:true, editor:{  maxLength:20 }
	},{
		header:'检修类型', dataIndex:'repairClass', hidden:true, editor:{  maxLength:20 }
	}],
	toEditFn: function(grid, rowIndex, e) {}

});
ZbglTpWin.closeWin = function() {
	//调用js实现此方法
};
ZbglTpWin.panel = {
		xtype: "panel",
	    layout: "border",
    	items:[{
    		 region: 'west', layout: "fit", width: 200,minSize: 260, maxSize: 300,
    		 split: true, bodyBorder: false, items:[ TrainBuild.tree ]
    	},{
    		region: 'center', layout: "fit", bodyBorder: false,frame:true, 
    		items : [ ZbglTpWin.editForm ],
    		buttonAlign:"center",
    		buttons: [{
        		 text: "添加提票信息", iconCls:"addIcon", handler:function(){
					  //表单验证是否通过
			        var form = ZbglTpWin.editForm.getForm(); 
			        if (!form.isValid()){
			        	 MyExt.Msg.alert("请先添加完善故障信息！"); 
			        	 return;	
			        }
			        if (!Ext.isEmpty(ZbglTpWin.warningIDX) && ZbglTpWin.faultTempGrid.store.getCount() > 0) {
			        	MyExt.Msg.alert("机车检测预警活转提票只能提一张票！"); 
			        	return;	
			        }
			        
			        showtip();
			        var data = form.getValues();
					delete data.customFaultName;
			        Ext.Ajax.request({
				        url: ctx + "/zbglTp!checkData.action",
				        jsonData: data,
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.success) {
				            	hidetip();
				            	Ext.Msg.confirm("提示" ,"存在相同位置上的提票还未处理！" +
				            			"</br>   是否继续提票！" ,function(btn){ 
				            		if(btn=="yes"){
				            			showtip();
				            			ZbglTpWin.addToTempGrid(data);
				            		}
				            	});
				            	ZbglTpWin.hasReminded = true;//标识已经提醒了重复提票
				            } else {	
				            	ZbglTpWin.addToTempGrid(data);
				            	ZbglTpWin.hasReminded = false;
				            }
				        }
					});
        		}
        	},{
				text: "提交提票信息", iconCls:"saveIcon", handler:function(){
					if(ZbglTpWin.faultTempGrid.store.getCount() > 0){
						Ext.Msg.confirm("提示","确认提交提票",function(btn){
							if(btn == "yes"){
								showtip();
								var tpArray = new Array();
								for(var i = 0; i < ZbglTpWin.faultTempGrid.store.getCount(); i++){
									var tp = ZbglTpWin.faultTempGrid.store.getAt(i).data;
									tp.warningIDX = ZbglTpWin.warningIDX;
									delete tp.customFaultName;
									tpArray.push(tp);
							    }								
								var tpjson = Ext.util.JSON.encode(tpArray);
								jQuery.ajax({
									url: ctx + "/zbglTp!saveTpAndInst.action",
									data:{entityJson: tpjson},
									dataType:"json",
									type:"post",
									success:function(data){
										hidetip();
					                    if (data.errMsg == null) {
					                        alertSuccess();
					                        ZbglTpWin.faultTempGrid.store.removeAll();
					                        if (!Ext.isEmpty(ZbglTpWin.warningIDX)) {
					                        	ZbglTpWin.closeWin();
					                        }
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
					if(ZbglTpWin.faultTempGrid.store.getCount() > 0){
						Ext.Msg.confirm("提示","有未提交的提票，要取消吗？",function(btn){
							if(btn == "yes"){
								ZbglTpWin.faultTempGrid.store.removeAll();
								ZbglTpWin.closeWin();
							}
						});
					}else{
						ZbglTpWin.closeWin();
					}
					
					ZbglTpWin.buildUpPlaceIdx = '';
				}
			}]
    	},{
    		 region: 'south', layout: "fit", height: 220,
    		 split: true, bodyBorder: false, items:[ZbglTpWin.faultTempGrid]
    	}]
};
	//初始化
ZbglTpWin.initSaveWin = function(){
	ZbglTpWin.initTree();
	ZbglTpWin.initForm();
};
ZbglTpWin.initTree = function(){
	TrainBuild.tree.root.setText(trainTypeShortName);
	TrainBuild.tree.root.attributes["fjdID"] = trainTypeShortName;//组成编码					
    TrainBuild.tree.root.reload();
	TrainBuild.tree.getRootNode().expand();
};
ZbglTpWin.initForm = function(){
	var form = ZbglTpWin.editForm.getForm();
	form.reset();
	ZbglTpWin.faultStore.removeAll();
	form.findField('trainTypeShortName').setValue(trainTypeShortName);
	form.findField('trainNo').setValue(trainNo);
	form.findField('trainTypeIDX').setValue(trainTypeIDX);
	form.findField('dID').setValue(dId);
	form.findField('dName').setValue(dName);
	form.findField('faultDesc').setValue(warningDesc);
	form.findField('professionalTypeIdx').clearValue();
	form.findField('professionalTypeName').setValue("");
	form.findField('professionalTypeSeq').setValue("");
};
//ZbglTpWin.initSaveWin();
//页面自适应布局
//var viewport = new Ext.Viewport({ layout:'fit', items:ZbglTpWin.panel });
});