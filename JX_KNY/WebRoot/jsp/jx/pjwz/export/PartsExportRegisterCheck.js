/**
 * 配件调出登记 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsExportRegisterSearch'); //定义命名空间
	
    PartsExportRegisterSearch.fieldWidth = 160;
	PartsExportRegisterSearch.labelWidth = 80;
	PartsExportRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	PartsExportRegisterSearch.callReturnFn=function(node,e){
	PartsExportRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	PartsExportRegisterSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
		//获取当前日期
	PartsExportRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	
	PartsExportRegisterSearch.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain",
    align: "center",
    defaultType: "textfield",
    defaults: { anchor: "98%" },
    layout: "form",
    border: false,
    style: "padding:10px",
    labelWidth: PartsExportRegisterSearch.labelWidth,
    buttonAlign:"center",
    defaults:{
      xtype: "panel",
      border: false,
      baseCls: "x-plain",
      layout: "column",
      align: "center", 
      	defaults:{
	      	 baseCls:"x-plain", 
	      	 align:"center",
	      	 layout:"form",
	      	 defaultType:"textfield", 
	      	 labelWidth:PartsExportRegisterSearch.labelWidth,
	         columnWidth: 0.33
      	}
    },
    items: [{
        items: [{
        	items:[{
                 xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
				 hiddenName: 'specificationModel', editable:false,
				 returnFn: PartsExportRegisterSearch.callReturnFn
        	},{
              	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
        	},{
        		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
        	}]
        },{
        	items:[{
        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
        	},{
				xtype: 'compositefield', fieldLabel : '调出日期', combineErrors: false,
		        items: [{
					xtype:'my97date', name: 'exportDate', id: 'startDate_d', format:'Y-m-d', value: PartsExportRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
					// 日期校验器
					validator: function() {
						var startDate =new Date(Ext.getCmp('startDate_d').getValue());
						var endDate = new Date(Ext.getCmp('endDate_d').getValue());
						if (startDate > endDate) {
							return "开始日期不能大于结束日期";
						}
					}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height: 23px;line-height:23px;'
				}, {
					xtype:'my97date', name: 'exportDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
					// 日期校验器
					validator: function() {
						var startDate =new Date(Ext.getCmp('startDate_d').getValue());
						var endDate = new Date(Ext.getCmp('endDate_d').getValue());
						if (startDate > endDate) {
							return "结束日期不能小于开始日期";
						}
					}
				}]
        	}]
        },{
        	items:[{
        		id:"partsNo", name:"partsNo", fieldLabel:"配件编号"
        	}]
        }]
     }],
     buttons: [{
        text: "查询", iconCls: "searchIcon", handler: function(){
        	PartsExportRegisterSearch.searchParam = PartsExportRegisterSearch.searchForm.getForm().getValues();
		    PartsExportRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsExportRegisterSearch.searchParam);
		    PartsExportRegisterSearch.grid.store.load();
        }
      },{
        text: "重置", iconCls: "resetIcon", handler: function(){
            PartsExportRegisterSearch.searchForm.getForm().reset();
        	PartsExportRegisterSearch.searchParam = {};
		    PartsExportRegisterSearch.grid.store.load();
        }
     }]
	});
	
	PartsExportRegisterSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsExportRegister!pageQuery.action',                 //装载列表数据的请求URL
	    tbar:[{text:'撤销', iconCls:'chart_attributeConfigIcon',
	     handler: function(){
	     	var data = PartsExportRegisterSearch.grid.selModel.getSelections();
	     	if(data.length<1){
	       	  MyExt.Msg.alert("尚未选择一条记录！");
	       	  return ;
	       }
	       var ids = new Array();
		   for(var i=0;i<data.length;i++){
		 		ids.push(data[i].get('idx'));
		    }
		 	Ext.Msg.confirm('提示', "是否确认撤销？", function(btn){
				if (btn == 'yes') {
				 	Ext.Ajax.request({
				        url: ctx + "/partsExportRegister!updatePartsExportCancelWeb.action",
				        params: {ids : ids},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null) {
				                alertSuccess();
				                PartsExportRegisterSearch.grid.store.reload(); 
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
	     }},{ text:'确认', iconCls:'checkIcon', handler:function(){
	    		var sm = PartsExportRegisterSearch.grid.getSelectionModel();
			    if (sm.getCount() < 1) {
			        MyExt.Msg.alert("尚未选择一条记录！");
			        return;
			    }
			    var data = sm.getSelections();
			    var ids = new Array();
			    for (var i = 0; i < data.length; i++){
			    	if(data[ i ].get("status") != status_ed){
			    		MyExt.Msg.alert("只有状态为【已登帐】的数据才能进行确认操作！");
			        	return;
			    	}
		    		ids.push(data[ i ].get("idx"));
			    }
			    Ext.Ajax.request({
			        url: ctx + "/partsExportRegister!updatePartsExportRegisterForCheck.action",
			        params: {ids: ids},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                alertSuccess();
			                PartsExportRegisterSearch.grid.store.reload(); 
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			        }
			    });
	        }},'refresh',{
			      xtype:'checkbox', name:'status', id: 'status_wait', boxLabel:'未登帐&nbsp;&nbsp;&nbsp;&nbsp;', 
				  inputValue:status_wait,checked:true,
				  handler: function(){PartsExportRegisterSearch.checkQuery();}
				},{
			      xtype:'checkbox', name:'status', id: 'status_ed', boxLabel:'已登帐&nbsp;&nbsp;&nbsp;&nbsp;', 
				  inputValue:status_ed,checked:true,
				  handler: function(){PartsExportRegisterSearch.checkQuery();}
				},{
				   xtype:'checkbox', name:'status', id: 'status_checked', boxLabel:'已确认&nbsp;&nbsp;&nbsp;&nbsp;', 
			       inputValue:status_checked, //checked:true,
				   handler: function(){PartsExportRegisterSearch.checkQuery();}
				}],
		fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'配件信息主键', dataIndex:'partsAccountIDX', hidden:true
			},{
				header:'配件规格型号主键', dataIndex:'partsTypeIDX', hidden:true
			},{
				header:'配件名称', dataIndex:'partsName'
			},{
				header:'规格型号', dataIndex:'specificationModel'
			},{
				header:'配件编号', dataIndex:'partsNo'
			},{
				header:'配件识别码', dataIndex:'identificationCode'
			},{
				header:'下车车型编码', dataIndex:'unloadTrainTypeIdx', hidden:true
			},{
				header:'下车车型', dataIndex:'unloadTrainType'
			},{
				header:'下车车号', dataIndex:'unloadTrainNo'
			},{
				header:'下车修程', dataIndex:'unloadRepairClass'
			},{
				header:'下车修程编码', dataIndex:'unloadRepairClassIdx', hidden:true
			},{
				header:'下车修次', dataIndex:'unloadRepairTime'
			},{
				header:'下车修次编码', dataIndex:'unloadRepairTimeIdx',hidden:true
			},{
				header:'调出日期', dataIndex:'exportDate', xtype:'datecolumn'
			},{
				header:'调拨命令', dataIndex:'exportOrder'
			},{
				header:'接收单位编码', dataIndex:'acceptDepCode',hidden:true
			},{
				header:'接收单位', dataIndex:'acceptDep'
			},{
				header:'单据状态', dataIndex:'status',
				renderer:function(v){
				 	if(v == status_wait)  return '未登帐';
		            if(v == status_ed) return '已登帐';
		            if(v == status_checked) return '已确认';
		         }
			},{
				header:'创建人名称', dataIndex:'creatorName', hidden:true
		 }]
	});
	
		//移除监听
	PartsExportRegisterSearch.grid.un('rowdblclick', PartsExportRegisterSearch.grid.toEditFn, PartsExportRegisterSearch.grid);
	 //公用checkbox查询方法
	PartsExportRegisterSearch.status = "10,20";
	//状态多选按钮
	PartsExportRegisterSearch.checkQuery = function(){
		    PartsExportRegisterSearch.status = "-1";
		if(Ext.getCmp("status_ed").checked){
			PartsExportRegisterSearch.status = PartsExportRegisterSearch.status + "," + status_ed;
		}
		if(Ext.getCmp("status_checked").checked){
			PartsExportRegisterSearch.status = PartsExportRegisterSearch.status + "," + status_checked;
		}
		PartsExportRegisterSearch.grid.store.load();
	}
	//查询前添加过滤条件
	PartsExportRegisterSearch.grid.store.on('beforeload',function(){
		PartsExportRegisterSearch.searchParam = PartsExportRegisterSearch.searchForm.getForm().getValues();
		PartsExportRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsExportRegisterSearch.searchParam);
		var searchParam = PartsExportRegisterSearch.searchParam;
		searchParam.status = PartsExportRegisterSearch.status;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('exportDate' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'exportDate',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'exportDate',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'exportDate',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'exportDate',propValue:whTimeVal[0]+' 23:59:59',compare:6});
			 			}
	              } 
		 		continue;
		 	}
		 	if('status' == prop){
				var val = searchParam[prop];
				val = val.toString();
				val = val.split(",");
                if(val instanceof Array){
                    whereList.push({propName:'status', propValues:val, compare:Condition.IN });
                } else {
                    var valAry = [];
                    valAry.push(val);
                    whereList.push({propName:'status', propValues:valAry, compare:Condition.IN });
                }
                continue;
		 	}
			whereList.push({propName:prop,propValue:searchParam[prop]});
		}
	    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	var viewport=new Ext.Viewport({
	        layout:'fit',
	        items:[{
		    layout:'border',frame:true,
		    items:[{
		       	region:'north',
		       	collapsible :true,
		       	title:'查询',
			   	height:150,
		       	frame:true,
		       	items:[PartsExportRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[PartsExportRegisterSearch.grid]
		    }]
		 }]
	 });
});