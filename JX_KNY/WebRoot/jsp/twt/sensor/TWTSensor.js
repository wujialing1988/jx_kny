/**
 * 传感器注册 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TWTSensor');                       //定义命名空间
TWTSensor.labelWidth = 120;
TWTSensor.fieldWidth = 130;
TWTSensor.saveForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: TWTSensor.labelWidth,
	    buttonAlign: "center",
	    defaults: {
	    xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center",
	    defaults: {
	    	  baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield",columnWidth: 0.5
	    	}
	    },
	    items: [{
	    	baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield",columnWidth: 1,
	        items: [
	        		{
						fieldLabel:'集线盒编号',xtype:'checkboxgroup',id:'checkBox_boxCode', name:'boxCode',
				        items:[{   
					        id:"checkBox_boxCode1",xtype:'checkbox',  boxLabel:'1',inputValue:"1"
					    },{   
					        id:"checkBox_boxCode2",xtype:'checkbox',  boxLabel:'2',inputValue:"1"
					    },{   
					        id:"checkBox_boxCode3",xtype:'checkbox',  boxLabel:'3',inputValue:"1"
					    },{   
					        id:"checkBox_boxCode4",xtype:'checkbox', boxLabel:'4',inputValue:"1"
					    }]
					}]
	    },{
	    	baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield",columnWidth: 1,
	        items: [
	        		{
						fieldLabel:'传感器编号',xtype:'checkboxgroup',id:'checkBox_sensorCode', name:'sensorCode',
				        items:[{   
					        id:"checkBox_sensorCode1",xtype:'checkbox',  boxLabel:'1',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode2",xtype:'checkbox',  boxLabel:'2',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode3",xtype:'checkbox',  boxLabel:'3',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode4",xtype:'checkbox',  boxLabel:'4',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode5",xtype:'checkbox',  boxLabel:'5',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode6",xtype:'checkbox',  boxLabel:'6',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode7",xtype:'checkbox',  boxLabel:'7',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode8",xtype:'checkbox',  boxLabel:'8',inputValue:"1"
					    },{   
					        id:"checkBox_sensorCode9",xtype:'checkbox',  boxLabel:'9',inputValue:"1" ,disabled : true
					    },{   
					        id:"checkBox_sensorCode10",xtype:'checkbox', boxLabel:'10',inputValue:"1" ,disabled : true
					    }]
					}]
	    },{
	        items: [
	        {
	            items: [
	                {xtype:"hidden", name:"idx"},
	                {xtype:"hidden", name:"siteId"},
	                {xtype:"hidden", name:"seqNo"},
	                {xtype:"hidden", name:"stationCode"},
	                {xtype:"hidden", name:"stationName"},
					{ name:"minLimit", fieldLabel:"最小感应门限(mm)",maxLength:10 ,xtype:'numberfield',value: 100,allowBlank:false,width:TWTSensor.fieldWidth},
					{ name:"checkCycle", fieldLabel:"检测周期",maxLength:10 ,xtype:'numberfield',value: 3,allowBlank:false,width:TWTSensor.fieldWidth}
	            ]},{
	            items: [
					{ name:"maxLimit", fieldLabel:"最大感应门限(mm)",maxLength:10 ,xtype:'numberfield',value: 1000,allowBlank:false,width:TWTSensor.fieldWidth},
					{ name:"location", fieldLabel:"安装位置",maxLength:20 ,allowBlank:false,width:TWTSensor.fieldWidth 
					}]
	        }]
	    }],
	    buttons: [{
	        id:"submitBtn1", text:"保存", iconCls:"saveIcon",
	        handler:function(){
	            var form = TWTSensor.saveForm.getForm();
	            if (!form.isValid()) return;
	            var url = ctx + "/tWTSensor!saveOrUpdate.action";
	            var data = form.getValues();
	            var data_v = {};
	            //拼接集线盒编号【选中为1,不选为0】
	            if("1" == data.checkBox_boxCode1){
	            	data.boxCode = "1" ;
	            }else data.boxCode = "0" ;
	            if("1" == data.checkBox_boxCode2){
	            	data.boxCode = data.boxCode + "1" ;
	            }else data.boxCode =data.boxCode + "0" ;
	            if("1" == data.checkBox_boxCode3){
	            	data.boxCode = data.boxCode + "1" ;
	            }else data.boxCode =data.boxCode + "0" ;
	            if("1" == data.checkBox_boxCode4){
	            	data.boxCode = data.boxCode + "1" ;
	            }else data.boxCode =data.boxCode + "0" ;
	            //拼接传感器编号【选中为1,不选为0】
	            if("1" == data.checkBox_sensorCode1){
	            	data.sensorCode = "1" ;
	            }else data.sensorCode = "0" ;
	            if("1" == data.checkBox_sensorCode2){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode3){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode4){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode5){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode6){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode7){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode8){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode9){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            if("1" == data.checkBox_sensorCode10){
	            	data.sensorCode = data.sensorCode + "1" ;
	            }else data.sensorCode =data.sensorCode + "0" ;
	            
	            data_v.sensorCode = data.sensorCode ;
	            data_v.boxCode = data.boxCode ;
	            data_v.idx = data.idx ;
	            data_v.minLimit = data.minLimit ;
	            data_v.checkCycle = data.checkCycle ;
	            data_v.maxLimit = data.maxLimit ;
	            data_v.location = data.location ;
	            data_v.siteId = siteId ;
	            data_v.stationName = data.stationName ;
	            data_v.stationCode = data.stationCode ;
	            data_v.seqNo = data.seqNo ;
	            Ext.Ajax.request({
	                url: url,
	                jsonData: data_v,
	                success: function(response, options){
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        TWTSensor.grid.store.reload();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
	        }
	    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	TWTSensor.saveWin.hide();
    	 }
    }]
	});
	/** ***定义设置门限表单开始*** */
	TWTSensor.threshold = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: 150,
	    buttonAlign: "center",
	    defaults: {
	    	 xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center",
	    	 defaults: {
	    	 baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield",columnWidth: 1
	    	 }
	    },
	    items: [{
	        items: [{
	            items: [
	                {xtype:"hidden", name:"idx"},
					{ name:"minLimit", fieldLabel:"最小感应门限(mm)",vtype :"positiveInt",maxLength:10 ,allowBlank:false,width:TWTSensor.fieldWidth,value:100}
	            ]},{
	            items: [
					{ name:"maxLimit", fieldLabel:"最大感应门限(mm)",vtype :"positiveInt",maxLength:10 ,allowBlank:false,width:TWTSensor.fieldWidth,value:1000
					}]
	        }]
	    }],
    buttons: [{
        text:"确定", iconCls:"saveIcon",
        handler:function(){
            var form = TWTSensor.threshold.getForm();
            var data = form.getValues();
            var minLimit=data["minLimit"];
            var maxLimit=data["maxLimit"];
           if(minLimit > maxLimit){
        	  MyExt.Msg.alert('最小感应门限需小于最大感应门限');
        		return;
      	 	 }
            if (!form.isValid()) return;
            var url = ctx + "/tWTSensor!twtSensorUpdate.action";
            Ext.Ajax.request({
                url: url,
                params:{data: Ext.util.JSON.encode(data),ids: $yd.getSelectedIdx(TWTSensor.grid, TWTSensor.grid.storeId)},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        TWTSensor.grid.store.reload();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    },{
    	 text:"取消", iconCls:"closeIcon",handler:function(){
    	 	TWTSensor.addthresholdWin.hide();
    	 }
    }]
	});
	/** **定义设置门限结束** */
	
	/** ***定义设置检查周期的表单** */
	TWTSensor.addcycle = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: 100,
	    buttonAlign: "center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", 
	            columnWidth: 1, 
	            items: [
	                {xtype:"hidden", name:"idx"},
					{ name:"checkCycle", fieldLabel:"检测周期",maxLength:100 ,allowBlank:false,width:TWTSensor.fieldWidth,value:3}
	            ]
	        }]
	    }],
	    buttons: [{
	        text:"确定", iconCls:"saveIcon",
	        handler:function(){
	            var form = TWTSensor.addcycle.getForm();
	            if (!form.isValid()) return;
	            var url = ctx + "/tWTSensor!twtaddcycle.action";
	            var data = form.getValues();
	            Ext.Ajax.request({
	                url: url,
	              params:{data: Ext.util.JSON.encode(data),ids: $yd.getSelectedIdx(TWTSensor.grid, TWTSensor.grid.storeId)},
	                success: function(response, options){
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        TWTSensor.grid.store.reload();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
	        }
	    },{
    	 text:"取消", iconCls:"closeIcon",handler:function(){
    	 	TWTSensor.addcycleWin.hide();
    	 }
    }]
	});
	/** ***定义设置检查周期的表单结束** */
	
	/** ***定义修改集线盒号*** */
	TWTSensor.updatebox = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: 100,
	    buttonAlign: "center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", 
	            columnWidth: 1, 
	            items: [
	                {xtype:"hidden", name:"idx"},
					{ name:"boxCode", fieldLabel:"集线盒编号",maxLength:100 ,allowBlank:false,width:TWTSensor.fieldWidth}
	            ]
	        }]
	    }],
	    buttons: [{
	        text:"确定", iconCls:"saveIcon",
	        handler:function(){
	            var form = TWTSensor.updatebox.getForm();
	            if (!form.isValid()) return;
	            var url = ctx + "/tWTSensor!twtupdatebox.action";
	            var data = form.getValues();
	            Ext.Ajax.request({
	                url: url,
	              params:{data: Ext.util.JSON.encode(data),ids: $yd.getSelectedIdx(TWTSensor.grid, TWTSensor.grid.storeId)},
	                success: function(response, options){
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        TWTSensor.grid.store.reload();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
	        }
	    },{
    	 text:"取消", iconCls:"closeIcon",handler:function(){
    	 	TWTSensor.updateboxWin.hide();
    	 }
    }]
	});
	/** ********定义查询表单开始************** */
    TWTSensor.form=new Ext.form.FormPanel({
	
	  layout:'column',baeCls:'x-plain',frame:true,labelWidth: TWTSensor.labelWidth,
	 	 defaults: {
	  	 style: 'padding: 10px',
	  	 layout:'form', columnWidth:.25,
	  	 labelWidth: TWTSensor.labelWidth,
	  	 baseCls:'x-plain',
	  	 defaults: {
	  	 	xtype:'textfield',
	  	 	width:TWTSensor.fieldWidth
	  	 }
	  },
	  items:[{
	     items:[
	     	  {fieldLabel:'集线盒编号',name:"boxCode"}
	        ]},{
		     items:[
		     		{fieldLabel:'传感器编号',name:"sensorCode"}
			]},{
		     items:[
		     		{fieldLabel:'安装位置',name:"location"}
			]},{
		     items:[
		     		{fieldLabel:'绑定台位图名称',name:"stationName"}
			]}
	  		],	
	  		buttonAlign: 'center',
			buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){
				TWTSensor.grid.store.load();
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
						TWTSensor.form.getForm().reset();  
						// 重新加载表格
						TWTSensor.grid.store.load();
			}
		}] 
	});
	
	/** ********定义查询表单结束************** */	
	
	/** ****定义修改集线盒号结束***** */
	//设置门限的窗体
	TWTSensor.addthresholdWin=new Ext.Window({
		title: '设置门限',width: 340,height: 160,modal: true,
		layout: 'fit',closeAction: 'hide',
		items: TWTSensor.threshold
		
	});
	//设置检查周期
	TWTSensor.addcycleWin=new Ext.Window({
		title: '设置检查周期',width: 340,height: 120,modal: true,
		layout: 'fit',closeAction: 'hide',
		items: TWTSensor.addcycle
		
	});
	//修改集线盒窗体
	TWTSensor.updateboxWin=new Ext.Window({
		title: '修改集线盒',width: 340,height: 120,modal: true,
		layout: 'fit',closeAction: 'hide',
		items: TWTSensor.updatebox
		
	});
	
	
TWTSensor.saveWin = new Ext.Window({
    title:"新增", maximizable:true, width:580, height:220,
    plain:true,  layout:"fit", closeAction:"hide",
    items: TWTSensor.saveForm
});
TWTSensor.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/tWTSensor!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/tWTSensor!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/tWTSensor!logicDelete.action',            //删除数据的请求URL
    defaultData: {minLimit:100,maxLimit:1000,checkCycle:3},   
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'集线盒编号', dataIndex:'boxCode', editor:{  maxLength:20 }
	},{
		header:'传感器编号', dataIndex:'sensorCode', editor:{  maxLength:20 }
	},{
		header:'最小感应门限(mm)', dataIndex:'minLimit', editor:{ xtype:'numberfield', maxLength:10}
	},{
		header:'最大感应门限(mm)', dataIndex:'maxLimit', editor:{ xtype:'numberfield', maxLength:10}
	},{
		header:'检测周期', dataIndex:'checkCycle', editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'安装位置', dataIndex:'location', editor:{  maxLength:50 }
	},{
		header:'绑定序列号', dataIndex:'seqNo', editor:{ disabled: true }
	},{
		header:'绑定台位号', dataIndex:'stationCode', editor:{  disabled: true }
	},{
		header:'绑定台位名称', dataIndex:'stationName', editor:{  disabled: true }
	},{
		header:'站场', dataIndex:'siteId', editor:{  disabled: true }
	}],
	tbar: [{text: '注册',iconCls: 'addIcon',handler: function (){
				TWTSensor.saveWin.show();
           	    TWTSensor.saveForm.getForm().reset();
	}},{ 
        text:"注销", iconCls:"deleteIcon", handler: function(){
        	var records=TWTSensor.grid.selModel.getSelections();
            if(records.length<1){
           	   MyExt.Msg.alert("尚未选择一条记录！");
           	   return ;
            }
            Ext.Msg.confirm('提示',"该操作将不能恢复，是否继续？", function(btn){ 	
	            if(btn == "yes"){
			        Ext.Ajax.request({
					      url:ctx+'/tWTSensor!logicDelete.action',
					      params:{ids: $yd.getSelectedIdx(TWTSensor.grid, TWTSensor.grid.storeId)},
					      success:function(response,options){
			    	  	  var result=Ext.util.JSON.decode(response.responseText);
			    	  	  if(result.errMsg==null){
			    	  	      alertSuccess();
			                  TWTSensor.grid.store.reload();
			              } else {
			                  alertFail(result.errMsg);
			              }
			    	    },
			    	    failure: function(response, options){
			               MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			            }
				     });
		     }
          });
        }
    },{
    	text: '设置门限',iconCls:'chart_attributeConfigIcon',handler: function(){
    		 	var com=TWTSensor.grid.getSelectionModel().getCount();
	        	if(com>0){
	        	TWTSensor.addthresholdWin.show();
	        	}else{
	        		MyExt.Msg.alert("尚未选择一条记录！");
	        	}
    	}
    
    },{
    	text : '设子检查周期',iconCls:'chart_attributeConfigIcon',handler : function() {
				var com = TWTSensor.grid.getSelectionModel().getCount();
				if (com > 0) {
					TWTSensor.addcycleWin.show();
				} else {
					MyExt.Msg.alert("尚未选择一条记录！");
				}
			}
    
    },{
    	text: '修改集线盒号',iconCls:'chart_attributeConfigIcon',handler: function (){
    		var com = TWTSensor.grid.getSelectionModel().getCount();
    		if(com>0){
    			TWTSensor.updateboxWin.show();
    		}else{
    			MyExt.Msg.alert('尚未选择一条记录！');
    		}
    	}
    },{
	text: '解除绑定',iconCls:"deleteIcon",handler: function(){
    	var records=TWTSensor.grid.selModel.getSelections();
            if(records.length<1){
           	   MyExt.Msg.alert("尚未选择一条记录！");
           	   return ;
            }
         Ext.Msg.confirm('提示',"解除绑定后不能恢复，是否继续？", function(btn){
         	if(btn == "yes"){
			    Ext.Ajax.request({
			      url:ctx+'/tWTSensor!twtRelieveBin.action',
			      params:{ids: $yd.getSelectedIdx(TWTSensor.grid, TWTSensor.grid.storeId)},
			      success:function(response,options){
			  	  var result=Ext.util.JSON.decode(response.responseText);
			  	  if(result.errMsg==null){
			  	      alertSuccess();
			          TWTSensor.grid.store.reload();
			      } else {
			          alertFail(result.errMsg);
			      }
			    },
			   	 failure: function(response, options){
			       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    }
			 });
         	}
         }); 	
	}
 },{
//    	text: '推送到电子图',handler: function (){
//    		MyExt.Msg.alert("待开发！");
//    		var records=TWTSensor.grid.selModel.getSelections();
//            if(records.length<1){
//           	   MyExt.Msg.alert("尚未选择一条记录！");
//           	   return ;
//            }
//            var datas = new Array();
//    		for (var i = 0; i < records.length; i++) {
//				var data = {} ;
//				data = records[i].data;
//				datas.push(data);
//			}
//	        Ext.Ajax.request({
//	            url: ctx + '/tWTSensor!savePushSensor.action',
//	            jsonData: datas,
//                params : {sensorDatas : Ext.util.JSON.encode(datas)},
//	            success: function(response, options){
//	                var result = Ext.util.JSON.decode(response.responseText);
//	                if (result.errMsg == null) {
//	                    alertSuccess();
//	                    TWTSensor.grid.store.load();
//	                } else {
//	                    alertFail(result.errMsg);
//	                }
//	            },
//	            failure: function(response, options){
//	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
//	            }
//	        });
//	    	}
//	    
//	    },{
	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
    }],
     toEditFn: function(grid, rowIndex, e){
     		TWTSensor.saveWin.show();
           	TWTSensor.saveForm.getForm().reset();
           	var record = this.store.getAt(rowIndex);
           	TWTSensor.saveForm.getForm().loadRecord(record);
           	//集线盒编号赋值【1为选中，0为不选】
           	var boxCode = record.get("boxCode");
           	for(var i = 1;i <= boxCode.length;i++){
           		var boxCodeId = "checkBox_boxCode"  + i ;
           		if("1" == boxCode.substring(i-1,i)){
           			document.getElementById(boxCodeId).checked = true ;
           		}else document.getElementById(boxCodeId).checked = false ;
           	}
           	//传感器编号赋值【1为选中，0为不选】
           	var sensorCode = record.get("sensorCode");
           	for(var i = 1;i <= sensorCode.length;i++){
           		var sensorCodeId = "checkBox_sensorCode"  + i ;
           		if("1" == sensorCode.substring(i-1,i)){
           			document.getElementById(sensorCodeId).checked = true ;
           		}else document.getElementById(sensorCodeId).checked = false ;
           	}
     }
});

	// 设置默认排序
	TWTSensor.grid.store.setDefaultSort('seqNo', 'ASC');
	// 组装数据  将时间传入后台做筛选
	TWTSensor.grid.store.on(
		'beforeload', function() {
			var searchParams = TWTSensor.form.getForm().getValues() ;
			var searchParams = MyJson.deleteBlankProp(searchParams);
			this.baseParams.entityJson = Ext.encode(searchParams);
			});
//页面自适应布局
	var viewport = new Ext.Viewport({
			layout: 'border',
				defaults: {
				layout : 'fit'
				},
	items : [{
				region : 'north',
				height : 110,
				title : '查询	',
				collapsible : true,
				items : TWTSensor.form
			}, {
				region : 'center',
				items : TWTSensor.grid

				}]
			});
});