Ext.onReady(function(){
	Ext.namespace('WellPartsExwhSearch'); //定义命名空间
	WellPartsExwhSearch.fieldWidth = 160;
	WellPartsExwhSearch.labelWidth = 80;
	WellPartsExwhSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	WellPartsExwhSearch.callReturnFn=function(node,e){
	WellPartsExwhSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	WellPartsExwhSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
	//获取当前日期
	WellPartsExwhSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	 
	WellPartsExwhSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: WellPartsExwhSearch.labelWidth,
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
		      	 labelWidth:WellPartsExwhSearch.labelWidth,
		         columnWidth: 0.33
	      	}
	    },
	    items: [{
	        items: [{
	        	items:[{
                     xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					 hiddenName: 'specificationModel', editable:false,
					 returnFn: WellPartsExwhSearch.callReturnFn
	        	},{
                  	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
	        	},{
            		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
	        	}]
	        },{
	        	items:[{
	        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
	        	},{
					xtype: 'compositefield', fieldLabel : '领件日期', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'whTime', id: 'startDate_d', format:'Y-m-d', value: WellPartsExwhSearch.getCurrentMonth(), width: 100, allowBlank: false,
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
						xtype:'my97date', name: 'whTime', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
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
            	WellPartsExwhSearch.searchParam = WellPartsExwhSearch.searchForm.getForm().getValues();
			    WellPartsExwhSearch.searchParam = MyJson.deleteBlankProp(WellPartsExwhSearch.searchParam);
			    WellPartsExwhSearch.grid.store.load();
            }
          },{
            text: "重置", iconCls: "resetIcon", handler: function(){
                WellPartsExwhSearch.searchForm.getForm().reset();
            	WellPartsExwhSearch.searchParam = {};
			    WellPartsExwhSearch.grid.store.load();
            }
         }]
	});
	
	WellPartsExwhSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wellPartsExwh!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig : [],
	    tbar: [{ text:'确认', iconCls:'checkIcon', handler:function(){
	    		var sm = WellPartsExwhSearch.grid.getSelectionModel();
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
			        url: ctx + "/wellPartsExwh!updateWellPartsExwhForCheck.action",
			        params: {ids: ids},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                alertSuccess();
			                WellPartsExwhSearch.grid.store.reload(); 
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
				  handler: function(){WellPartsExwhSearch.checkQuery();}
				},{
			      xtype:'checkbox', name:'status', id: 'status_ed', boxLabel:'已登帐&nbsp;&nbsp;&nbsp;&nbsp;', 
				  inputValue:status_ed,checked:true,
				  handler: function(){WellPartsExwhSearch.checkQuery();}
				},{
				   xtype:'checkbox', name:'status', id: 'status_checked', boxLabel:'已确认&nbsp;&nbsp;&nbsp;&nbsp;', 
			       inputValue:status_checked, //checked:true,
				   handler: function(){WellPartsExwhSearch.checkQuery();}
				}],
		fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'接收库房主键', dataIndex:'whIdx', hidden:true
			},{
				header:'接收库房', dataIndex:'whName'
			},{
				header:'库位名称', dataIndex:'locationName'
			},{
				header:'接收人主键', dataIndex:'acceptEmpId', hidden:true
			},{
				header:'接收人', dataIndex:'acceptEmp'
			},{
				header:'配件识别码', dataIndex:'identificationCode'
			},{
				header:'规格型号', dataIndex:'specificationModel', width:150
			},{
				header:'配件名称', dataIndex:'partsName'
			},{
				header:'配件编号', dataIndex:'partsNo', width:120
			},{
				header:'配件信息主键', dataIndex:'partsAccountIdx',hidden:true
			},{
				header:'配件规格型号主键', dataIndex:'partsTypeIDX',hidden:true
			},{
				header:'领件日期', dataIndex:'whTime',xtype:'datecolumn'
			},{
				header:'接收部门', dataIndex:'acceptOrg'
			},{
				header:'接收部门主键', dataIndex:'acceptOrgID',hidden:true
			},{
				header:'交件人主键', dataIndex:'handOverEmpId', hidden:true
			},{
				header:'交件人', dataIndex:'handOverEmp'
			},{
				header:'是否配送', dataIndex:'isDeliver'
			},{
				header:'配送时间', dataIndex:'deliverTime',xtype:'datecolumn',format: "Y-m-d H:i"
			},{
				header:'配送地址', dataIndex:'deliverLocation'
			},{
				header:'单据状态', dataIndex:'status',
				renderer:function(v){
				 	if(v == status_wait)  return '未登帐';
		            if(v == status_ed) return '已登帐';
		            if(v == status_checked) return '已确认';
		         }
			}]
	 });
	//移除监听
	WellPartsExwhSearch.grid.un('rowdblclick', WellPartsExwhSearch.grid.toEditFn, WellPartsExwhSearch.grid);
	 //公用checkbox查询方法
	WellPartsExwhSearch.status = "10,20";
	//状态多选按钮
	WellPartsExwhSearch.checkQuery = function(){
		    WellPartsExwhSearch.status = "-1";
		if(Ext.getCmp("status_ed").checked){
			WellPartsExwhSearch.status = WellPartsExwhSearch.status + "," + status_ed;
		}
		if(Ext.getCmp("status_checked").checked){
			WellPartsExwhSearch.status = WellPartsExwhSearch.status + "," + status_checked;
		}
		WellPartsExwhSearch.grid.store.load();
	}
	//查询前添加过滤条件
	WellPartsExwhSearch.grid.store.on('beforeload',function(){
		WellPartsExwhSearch.searchParam = WellPartsExwhSearch.searchForm.getForm().getValues();
		WellPartsExwhSearch.searchParam = MyJson.deleteBlankProp(WellPartsExwhSearch.searchParam);
		var searchParam = WellPartsExwhSearch.searchParam;
		searchParam.status = WellPartsExwhSearch.status;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('whTime' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'whTime',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'whTime',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'whTime',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'whTime',propValue:whTimeVal[0]+' 23:59:59',compare:6});
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
		       	items:[WellPartsExwhSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[WellPartsExwhSearch.grid]
		    }]
		 }]
	 });

});