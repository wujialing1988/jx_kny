Ext.onReady(function(){
	 Ext.namespace('WellPartsWhBackSearch');
	 WellPartsWhBackSearch.fieldWidth = 170;
	 WellPartsWhBackSearch.labelWidth = 80;
	 WellPartsWhBackSearch.searchParam = {};
	 
	 //规格型号选择控件赋值函数
	WellPartsWhBackSearch.callReturnFn=function(node,e){
		WellPartsWhBackSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
		WellPartsWhBackSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	 }
	 //获取当前日期
	 WellPartsWhBackSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay = new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	 
	 WellPartsWhBackSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: WellPartsWhBackSearch.labelWidth,
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
		      	 labelWidth:WellPartsWhBackSearch.labelWidth,
		         columnWidth: 0.33
	      	}
	    },
	    items: [{
	        items: [{
	        	items:[{
	                 xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					 hiddenName: 'specificationModel', editable:false,
					 returnFn: WellPartsWhBackSearch.callReturnFn
	        	},{
	              	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
	        	},{
	        		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
	        	}]
	        },{
	        	items:[{
	        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
	        	},{
					xtype: 'compositefield', fieldLabel : '退库日期', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'backTime', id: 'startDate_d', format:'Y-m-d', value: WellPartsWhBackSearch.getCurrentMonth(), width: 100, allowBlank: false,
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
						xtype:'my97date', name: 'backTime', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
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
	        		},{
		            	id:"whName_Base_combo", fieldLabel:"库房",
		            	hiddenName:"whName",xtype:"Base_combo",
		                entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",
		                queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",
		                fields:["wareHouseID","wareHouseName","idx"],
						displayField:"wareHouseName",valueField:"wareHouseName",
						returnField:[{widgetId:"wellPartsWH_whIdx",propertyName:"idx"}] 
					}, {
						id:"wellPartsWH_whIdx",xtype:"hidden", name:"warehouseIdx"
				}]
	        }]
	    }],
	     buttons: [{
	        text: "查询", iconCls: "searchIcon", handler: function(){
	        	WellPartsWhBackSearch.searchParam = WellPartsWhBackSearch.searchForm.getForm().getValues();
			    WellPartsWhBackSearch.searchParam = MyJson.deleteBlankProp(WellPartsWhBackSearch.searchParam);
			    WellPartsWhBackSearch.grid.store.load();
	        }
	      },{
	        text: "重置", iconCls: "resetIcon", handler: function(){
	                WellPartsWhBackSearch.searchForm.getForm().reset();
	            	WellPartsWhBackSearch.searchParam = {};
				    WellPartsWhBackSearch.grid.store.load();
	            }
	        }]
	  });
	 //配件退库列表
	 WellPartsWhBackSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wellPartsWhBack!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig:{},
	    tbar:[{ text:'确认', iconCls:'checkIcon', handler:function(){
	    		var sm = WellPartsWhBackSearch.grid.getSelectionModel();
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
			        url: ctx + "/wellPartsWhBack!updateWellPartsWhBackForCheck.action",
			        params: {ids: ids},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                alertSuccess();
			                WellPartsWhBackSearch.grid.store.reload(); 
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
				  handler: function(){WellPartsWhBackSearch.checkQuery();}
				},{
			      xtype:'checkbox', name:'status', id: 'status_ed', boxLabel:'已登帐&nbsp;&nbsp;&nbsp;&nbsp;', 
				  inputValue:status_ed,checked:true,
				  handler: function(){WellPartsWhBackSearch.checkQuery();}
				},{
				   xtype:'checkbox', name:'status', id: 'status_checked', boxLabel:'已确认&nbsp;&nbsp;&nbsp;&nbsp;', 
			       inputValue:status_checked, //checked:true,
				   handler: function(){WellPartsWhBackSearch.checkQuery();}
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
			header:'库房名称', dataIndex:'whName'
		},{
			header:'库管员', dataIndex:'warehouseEmp'
		},{
			header:'交件人', dataIndex:'handoverEmp'
		},{
			header:'交件部门', dataIndex:'handoverOrg'
		},{
			header:'交件人', dataIndex:'handoverEmp'
		},{
			header:'创建人', dataIndex:'creatorName', hidden:true
		},{
			header:'退库日期', dataIndex:'backTime', xtype:'datecolumn'
		},{
			header:'单据状态', id:"c", dataIndex:'status',
			renderer:function(v){
			 	if(v == status_wait)  return '未登帐';
	            if(v == status_ed) return '已登帐';
	            if(v == status_checked) return '已确认';
	         }
		 },{
			header:'warehouseIdx', dataIndex:'warehouseIdx', hidden:true
		 },{
			header:'warehouseEmpId', dataIndex:'warehouseEmpId', hidden:true
		 },{
			header:'handoverEmpId', dataIndex:'handoverEmpId', hidden:true
		 }]
	   });
	   
	//移除事件
	WellPartsWhBackSearch.grid.un('rowdblclick',WellPartsWhBackSearch.grid.toEditFn,WellPartsWhBackSearch.grid);
	 //公用checkbox查询方法
	WellPartsWhBackSearch.status = "10,20";
	//状态多选按钮
	WellPartsWhBackSearch.checkQuery = function(){
		    WellPartsWhBackSearch.status = "-1";
		if(Ext.getCmp("status_ed").checked){
			WellPartsWhBackSearch.status = WellPartsWhBackSearch.status + "," + status_ed;
		}
		if(Ext.getCmp("status_checked").checked){
			WellPartsWhBackSearch.status = WellPartsWhBackSearch.status + "," + status_checked;
		}
		WellPartsWhBackSearch.grid.store.load();
	}
	//查询前添加过滤条件
	WellPartsWhBackSearch.grid.store.on('beforeload',function(){
	WellPartsWhBackSearch.searchParam = WellPartsWhBackSearch.searchForm.getForm().getValues();
	WellPartsWhBackSearch.searchParam = MyJson.deleteBlankProp(WellPartsWhBackSearch.searchParam);
	var searchParam = WellPartsWhBackSearch.searchParam;
	searchParam.status = WellPartsWhBackSearch.status;
	delete searchParam.PartsTypeTreeSelect_select ;
	var whereList = []; 
	for(prop in searchParam){
		 	if('backTime' == prop){
			 	var whTimeVal_v = searchParam[prop];
			 	whTimeVal_v = whTimeVal_v.toString();
			 	var whTimeVal = whTimeVal_v.split(",");
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'backTime',propValue:whTimeVal[0],compare:4});
			 				whereList.push({propName:'backTime',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'backTime',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'backTime',propValue:whTimeVal[0]+' 23:59:59',compare:6});
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
		 	whereList.push({propName:prop,propValue:searchParam[prop],compare:8});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	  	//页面自适应布局
	var viewport=new Ext.Viewport({layout:'fit',items:[{
		 layout:'border',frame:true,
		 items:[{
		       region:'north',
		       collapsible :true,
		       title:'查询',
			   height:140,
		       frame:true,
		       items:[WellPartsWhBackSearch.searchForm]
		    },{
		      region:'center',
		      frame:true,
		      layout:'fit',
		      items:[WellPartsWhBackSearch.grid]
		    }]	
		}]
	});
});