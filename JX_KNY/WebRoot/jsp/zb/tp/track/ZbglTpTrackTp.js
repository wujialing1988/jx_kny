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
Ext.namespace('ZbglTpTrackTp');                       //定义命名空间
ZbglTpTrackTp.fieldWidth = 180;
ZbglTpTrackTp.labelWidth = 80;
ZbglTpTrackTp.buildUpPlaceIdx = "" ; //组成位置主键
ZbglTpTrackTp.anchor = '95%';
ZbglTpTrackTp.warningIDX = '';//机车检测预警IDX
//数据容器
ZbglTpTrackTp.faultStore = new Ext.data.JsonStore({
		id : "faultName",
		//root : "root",
		//totalProperty : "totalProperty",
		autoLoad : true,
		//sortInfo : {field:'faultId',direction:'ASC'},
		url : ctx + "/jcgxBuildQuery!getFlbmFault.action",
		fields : [ 'faultId' , 'faultName']
});
//特殊select扩展控件（故障现象列表）
ZbglTpTrackTp.multiSelect = new Ext.ux.Multiselect({
    fieldLabel: '故障现象',
    name: 'faultID',
    width: '100%',
    anchor: '95%',
    height: 160,
    autoScroll: true,
    autoHeight: true,
    allowBlank:false,
    valueField : "faultId",
	displayField : "faultName",
    store: ZbglTpTrackTp.faultStore,  //扩展控件使用的数据Store
    //value: OTHERID,
    tbar: new Ext.Toolbar({
    	width: 450,
    	items: [{
	    	 text: '选择故障字典', iconCls: 'addIcon',
	    	 handler: function(){
	    	   if(Ext.isEmpty(ZbglTpTrackTp.buildUpPlaceIdx)){
					MyExt.Msg.alert("请先选择组成！");    	
					return ;
	    	   }
	           ZbglTpTrackTp.win.show();
	           ZbglTpTrackTp.faultSelectGrid.store.load({
			        params: { fixPlaceIdx: ZbglTpTrackTp.buildUpPlaceIdx }       
			    });
	        }
    	    	}, '->',{   xtype:"label", text:"输入故障现象： "},{id: "customFaultName", xtype: "textfield", maxLenth: 50, width: 100},{
	    	 text: '保存', iconCls: 'saveIcon',
	    	 handler: function(){
	    	 	var dataAry = new Array();        		
        		var data = {};
        		var customFaultName = Ext.getCmp("customFaultName").getValue();
        		if (!customFaultName) {
        			MyExt.Msg.alert("请输入故障现象！");    	
					return ;
        		}
        		if (customFaultName.length > 50) {
        			MyExt.Msg.alert("故障现象不能超过50个字符！");    	
					return ;
        		}
        		data.flbm = ZbglTpTrackTp.buildUpPlaceIdx;
        		data.faultId = CUSTOMID;
        		data.faultName = customFaultName;
        		dataAry.push(data);
        		Ext.Ajax.request({
	                url: ctx + "/jcxtflFault!saveFlbmFault.action",
	                params: {entityJson : Ext.util.JSON.encode(dataAry)},
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        ZbglTpTrackTp.faultSelectGrid.getStore().reload();
	                        //点击位置后刷新故障现象列表
							var paramObj = {};
	                        paramObj.flbm = ZbglTpTrackTp.buildUpPlaceIdx;
							ZbglTpTrackTp.faultStore.load({
						        params: { entityJson : Ext.util.JSON.encode(paramObj)},
						        callback: function(records){
						        	if (records && records.length > 0) {
							            ZbglTpTrackTp.multiSelect.setValue(records[0].data.faultId);
							            ZbglTpTrackTp.editForm.find("name","faultName")[0].setValue(records[0].data.faultName);
						        	}
						        }
						    });
	                        ZbglTpTrackTp.win.hide();
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
    		ZbglTpTrackTp.editForm.find("name","faultName")[0].setValue(faultName);
        },//双击才设置故障描述的值
        'dblclick':function(vw, index, node, e){
    		var faultName = e.target.innerText;
    		ZbglTpTrackTp.editForm.find("name","faultName")[0].setValue(faultName);
    		ZbglTpTrackTp.editForm.find("name","faultDesc")[0].setValue(faultName);
        }
    },
    ddReorder: true
});

//树点击事件的方法
TrainBuild.clickFn = function(node,e){
	var form = ZbglTpTrackTp.editForm ;
	ZbglTpTrackTp.buildUpPlaceIdx = node.attributes["flbm"] ; //组成位置主键
    //清空并设置专业类型数据 
    Ext.getCmp('professionalType_').clear('professionalType_');
    Ext.getCmp('professionalTypeName_').setValue("");
    if(node.attributes["zylxID"]){
	    Ext.getCmp('professionalType_').setDisplayValue(node.attributes["zylxID"],node.attributes["zylx"]);
        Ext.getCmp('professionalTypeName_').setValue(node.attributes["zylx"]);
    }
//	form.find("name",'faultFixPlaceIDX')[0].setValue(ZbglTpTrackTp.buildUpPlaceIdx); //故障位置主键
	form.find("name",'faultFixFullCode')[0].setValue(ZbglTpTrackTp.buildUpPlaceIdx);//node.getPath("wzdm").substring(1)
	form.find("name",'faultFixFullName')[0].setValue(node.getPath("text").substring(1));
	
	//需求提出故障现象不应该清除 林欢 2016-8-2
//	form.find("name","faultDesc")[0].setValue("");
	form.find("name","faultDesc")[0].clearInvalid();
	//点击位置后刷新故障现象列表
	var paramObj = {};
    paramObj.flbm = ZbglTpTrackTp.buildUpPlaceIdx;
	ZbglTpTrackTp.faultStore.load({
        params: { entityJson : Ext.util.JSON.encode(paramObj)},
        callback: function(records){
        	if (records && records.length > 0) {
	            ZbglTpTrackTp.multiSelect.setValue(records[0].data.faultId);
	            form.find("name","faultName")[0].setValue(records[0].data.faultName);
        	}
        }
    });
}

ZbglTpTrackTp.addToTempGrid = function(data){
	var record = new Ext.data.Record();
	for(var i in data){
		if(i== "faultOccurDate"){
			var d = new Date(data[i].replace(/-/g,"/"));
			record.set(i, d);
			continue;
		}
		record.set(i, data[i]);
	}
	ZbglTpTrackTp.storeFaultCheck(record);
}

ZbglTpTrackTp.storeFaultCheck = function(record){
	var pathName = record.get('faultFixFullName');
	var res = ZbglTpTrackTp.faultTempGrid.store.query('faultFixFullName',pathName);
	//Store中已存在一条或一条以上
	if(res.length>0 && !ZbglTpTrackTp.hasReminded){
		Ext.Msg.confirm("提示" ,"<font style='color:red;'>列表中已存在相同位置上的提票信息！" +
    			"</br>   是否继续添加提票信息！</font>" ,function(btn){ 
    		if(btn=="yes"){
    			ZbglTpTrackTp.faultTempGrid.store.add(record);
    		}
    	});
	} else {
		ZbglTpTrackTp.faultTempGrid.store.add(record);
	}
	ZbglTpTrackTp.hasReminded = false;
	hidetip();
}
	
ZbglTpTrackTp.del = function del(){
	var record = ZbglTpTrackTp.faultTempGrid.getSelectionModel().getSelected();
	ZbglTpTrackTp.faultTempGrid.store.remove(record);
}


//故障选择
ZbglTpTrackTp.faultSelectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/equipFault!faultList.action',                 //装载列表数据的请求URL
    storeAutoLoad: false,
    tbar: [{
        	xtype:"label", text:"  故障名称：" 
    	},{			
            xtype: "textfield",    
            id: "fault_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var faultName = Ext.getCmp("fault_searchId").getValue();
				var searchParam = {};
				searchParam.FaultName = faultName;					
				ZbglTpTrackTp.faultSelectGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:ZbglTpTrackTp.buildUpPlaceIdx//位置主键
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(ZbglTpTrackTp.faultSelectGrid)) return;
        		var tempData = ZbglTpTrackTp.faultSelectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.flbm = ZbglTpTrackTp.buildUpPlaceIdx;
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
	                        ZbglTpTrackTp.faultSelectGrid.getStore().reload();
	                        //点击位置后刷新故障现象列表
							var paramObj = {};
	                        paramObj.flbm = ZbglTpTrackTp.buildUpPlaceIdx;
							ZbglTpTrackTp.faultStore.load({
						        params: { entityJson : Ext.util.JSON.encode(paramObj)},
						        callback: function(records){
						        	if (records && records.length > 0) {
							            ZbglTpTrackTp.multiSelect.setValue(records[0].data.faultId);
							            ZbglTpTrackTp.editForm.find("name","faultName")[0].setValue(records[0].data.faultName);
						        	}
						        }
						    });
	                        ZbglTpTrackTp.win.hide();
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
ZbglTpTrackTp.faultSelectGrid.un('rowdblclick', ZbglTpTrackTp.faultSelectGrid.toEditFn, ZbglTpTrackTp.faultSelectGrid);
//故障选择窗口
ZbglTpTrackTp.win = new Ext.Window({
    title: "故障现象选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [ZbglTpTrackTp.faultSelectGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglTpTrackTp.win.hide(); }
            }]
});

//故障提票编辑页面form
ZbglTpTrackTp.editForm = new Ext.form.FormPanel({
    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: ZbglTpTrackTp.labelWidth, 
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" }, buttonAlign: "center",
    buttonAlign: "center",autoScroll:true,  labelAlign : 'right',
    items: [{    	
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpTrackTp.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpTrackTp.anchor },
            items: [{
                	name: "trainTypeShortName", fieldLabel: "车型",  maxLength: 50, style:"color:gray", readOnly:true
                }]
       		},{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpTrackTp.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpTrackTp.anchor },
            items: [
				{ name: "trainNo", fieldLabel: "车号",  maxLength: 50, style:"color:gray", readOnly:true}
            ]
       }]    
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpTrackTp.labelWidth,      columnWidth: 1, 
            items: [{ name: "faultFixFullName", fieldLabel: "故障位置",allowBlank:false, style:"color:gray",  maxLength: 500, anchor: "98%" ,readOnly:true}]
       }]
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpTrackTp.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpTrackTp.anchor },
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
				},{
                	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", 
		        	items: [ZbglTpTrackTp.multiSelect], anchor: '100%'
                }]
       		},{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: ZbglTpTrackTp.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: ZbglTpTrackTp.anchor },
            items: [				
				{   name: "faultOccurDate", fieldLabel: "故障发现时间",xtype: "my97date",
					format: "Y-m-d H:i",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm",maxDate:'%y-%M-%d' },
					validator :function(v){
						if(v) return true;
						else return "不允许为空";
							
					}
				},{
					id:"discover",fieldLabel:"发现人名称", name: "discover",allowBlank: true
				},{ name: "faultDesc", fieldLabel: "故障描述", xtype:'textarea',height:182, maxLength: 500,
                        listeners : {
                            'change'  : function(field, newValue, oldValue ){
                                this.setValue(newValue.trim());
                            } 
                        }
            	}
            ]
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

 
 ZbglTpTrackTp.faultTempGrid = new Ext.yunda.Grid({
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
			return "<img src='"+imgpath+"/delete.png' alt='删除' onclick='ZbglTpTrackTp.del()' style='cursor:pointer'/>";
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
		header:'配属段', dataIndex:'dName', editor:{  maxLength:50 }, width : 80
	},{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
	},{
		header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
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
ZbglTpTrackTp.closeWin = function() {
	trackIDX = '';
	ZbglTpTrack.infoWin.hide();
}
ZbglTpTrackTp.panel = {
		xtype: "panel",
	    layout: "border",
    	items:[{
    		 region: 'west', layout: "fit", width: 200,minSize: 260, maxSize: 300,
    		 split: true, bodyBorder: false, items:[ TrainBuild.tree ]
    	},{
    		region: 'center', layout: "fit", bodyBorder: false,frame:true, 
    		items : [ ZbglTpTrackTp.editForm ],
    		buttonAlign:"center",
    		buttons: [{
        		 text: "添加提票信息", iconCls:"addIcon", handler:function(){
					  //表单验证是否通过
			        var form = ZbglTpTrackTp.editForm.getForm(); 
			        if (!form.isValid()){
			        	 MyExt.Msg.alert("请先添加完善故障信息！"); 
			        	 return;	
			        }
			        if (!Ext.isEmpty(ZbglTpTrackTp.warningIDX) && ZbglTpTrackTp.faultTempGrid.store.getCount() > 0) {
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
				            			ZbglTpTrackTp.addToTempGrid(data);
				            		}
				            	});
				            	ZbglTpTrackTp.hasReminded = true;//标识已经提醒了重复提票
				            } else {	
				            	ZbglTpTrackTp.addToTempGrid(data);
				            	ZbglTpTrackTp.hasReminded = false;
				            }
				        }
					});
        		}
        	},{
				text: "提交提票信息", iconCls:"saveIcon", handler:function(){
					if(ZbglTpTrackTp.faultTempGrid.store.getCount() > 0){
						Ext.Msg.confirm("提示","确认提交提票",function(btn){
							if(btn == "yes"){
								showtip();
								var tpArray = new Array();
								for(var i = 0; i < ZbglTpTrackTp.faultTempGrid.store.getCount(); i++){
									var tp = ZbglTpTrackTp.faultTempGrid.store.getAt(i).data;
									tp.warningIDX = ZbglTpTrackTp.warningIDX;
									delete tp.customFaultName;
									tpArray.push(tp);
							    }								
								var tpjson = Ext.util.JSON.encode(tpArray);
								jQuery.ajax({
									url: ctx + "/zbglTp!saveTpAndInstAndTrack.action",
									data:{entityJson: tpjson,trackIDX: trackIDX},
									dataType:"json",
									type:"post",
									success:function(data){
										hidetip();
					                    if (data.errMsg == null) {
					                        alertSuccess();
					                        ZbglTpTrackTp.faultTempGrid.store.removeAll();
					                        if (!Ext.isEmpty(ZbglTpTrackTp.warningIDX)) {
					                        	ZbglTpTrackTp.closeWin();
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
					if(ZbglTpTrackTp.faultTempGrid.store.getCount() > 0){
						Ext.Msg.confirm("提示","有未提交的提票，要取消吗？",function(btn){
							if(btn == "yes"){
								ZbglTpTrackTp.faultTempGrid.store.removeAll();
								ZbglTpTrackTp.closeWin();
							}
						});
					}else{
						ZbglTpTrackTp.closeWin();
					}
					
					ZbglTpTrackTp.buildUpPlaceIdx = '';
				}
			}]
    	},{
    		 region: 'south', layout: "fit", height: 220,
    		 split: true, bodyBorder: false, items:[ZbglTpTrackTp.faultTempGrid]
    	}]
};
	//初始化
ZbglTpTrackTp.initSaveWin = function(){
	ZbglTpTrackTp.initTree();
	ZbglTpTrackTp.initForm();
	ZbglTpTrackTp.initForm();
}
ZbglTpTrackTp.initTree = function(){
	TrainBuild.tree.root.setText(trainTypeShortName);
	TrainBuild.tree.root.attributes["fjdID"] = trainTypeShortName;//组成编码					
    TrainBuild.tree.root.reload();
	TrainBuild.tree.getRootNode().expand();
}
ZbglTpTrackTp.initForm = function(){
	var form = ZbglTpTrackTp.editForm.getForm();
	//form.reset();
	ZbglTpTrackTp.faultStore.removeAll();
	form.findField('trainTypeShortName').setValue(trainTypeShortName);
	form.findField('trainNo').setValue(trainNo);
	form.findField('trainTypeIDX').setValue(trainTypeIDX);
	form.findField('dID').setValue(dId);
	form.findField('dName').setValue(dName);
	form.findField('faultDesc').setValue(warningDesc);
	form.findField('professionalTypeIdx').clearValue();
	form.findField('professionalTypeName').setValue("");
	form.findField('professionalTypeSeq').setValue("");
}
//ZbglTpTrackTp.initSaveWin();
//页面自适应布局
//var viewport = new Ext.Viewport({ layout:'fit', items:ZbglTpTrackTp.panel });
});