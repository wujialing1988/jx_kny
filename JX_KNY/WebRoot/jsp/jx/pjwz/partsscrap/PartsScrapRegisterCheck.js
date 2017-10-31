 Ext.onReady(function(){
	Ext.namespace('PartsScrapRegisterSearch');//定义命名空间
	PartsScrapRegisterSearch.fieldWidth = 160;
	PartsScrapRegisterSearch.labelWidth = 80;
	PartsScrapRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	PartsScrapRegisterSearch.callReturnFn=function(node,e){
	PartsScrapRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	PartsScrapRegisterSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
		//获取当前日期
	PartsScrapRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	
	PartsScrapRegisterSearch.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain",
    align: "center",
    defaultType: "textfield",
    defaults: { anchor: "98%" },
    layout: "form",
    border: false,
    style: "padding:10px",
    labelWidth: PartsScrapRegisterSearch.labelWidth,
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
	      	 labelWidth:PartsScrapRegisterSearch.labelWidth,
	         columnWidth: 0.33
      	}
    },
    items: [{
        items: [{
        	items:[{
                 xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
				 hiddenName: 'specificationModel', editable:false,
				 returnFn: PartsScrapRegisterSearch.callReturnFn
        	},{
              	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
        	},{
        		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
        	}]
        },{
        	items:[{
        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
        	},{
				xtype: 'compositefield', fieldLabel : '报废日期', combineErrors: false,
		        items: [{
					xtype:'my97date', name: 'scrapDate', id: 'startDate_d', format:'Y-m-d', value: PartsScrapRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
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
					xtype:'my97date', name: 'scrapDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
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
        	PartsScrapRegisterSearch.searchParam = PartsScrapRegisterSearch.searchForm.getForm().getValues();
		    PartsScrapRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsScrapRegisterSearch.searchParam);
		    PartsScrapRegisterSearch.grid.store.load();
        }
      },{
        text: "重置", iconCls: "resetIcon", handler: function(){
            PartsScrapRegisterSearch.searchForm.getForm().reset();
        	PartsScrapRegisterSearch.searchParam = {};
		    PartsScrapRegisterSearch.grid.store.load();
        }
      }]
	});
	
	
	PartsScrapRegisterSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsScrapRegister!pageQuery.action',                 //装载列表数据的请求URL
	    tbar:[{ text:'确认', iconCls:'checkIcon', handler:function(){
	    		var sm = PartsScrapRegisterSearch.grid.getSelectionModel();
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
			        url: ctx + "/partsScrapRegister!updatePartsScrapRegisterForCheck.action",
			        params: {ids: ids},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                alertSuccess();
			                PartsScrapRegisterSearch.grid.store.reload(); 
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
				  handler: function(){PartsScrapRegisterSearch.checkQuery();}
				},{
			      xtype:'checkbox', name:'status', id: 'status_ed', boxLabel:'已登帐&nbsp;&nbsp;&nbsp;&nbsp;', 
				  inputValue:status_ed,checked:true,
				  handler: function(){PartsScrapRegisterSearch.checkQuery();}
				},{
				   xtype:'checkbox', name:'status', id: 'status_checked', boxLabel:'已确认&nbsp;&nbsp;&nbsp;&nbsp;', 
			       inputValue:status_checked, //checked:true,
				   handler: function(){PartsScrapRegisterSearch.checkQuery();}
				}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'报废人主键', dataIndex:'scrapEmpId', hidden:true
		},{
			header:'报废人', dataIndex:'scrapEmp'
		},{
			header:'报废部门主键', dataIndex:'scrapOrgId', hidden:true
		},{
			header:'报废部门', dataIndex:'scrapOrg'
		},{
			header:'报废日期', dataIndex:'scrapDate', xtype:'datecolumn'
		},{
			header:'报废理由', dataIndex:'scrapReason'
		},{
			header:'配件信息主键', dataIndex:'partsAccountIdx', hidden:true
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
			header:'单据状态', dataIndex:'status',
			renderer:function(v){
			 	if(v == status_wait)  return '未登帐';
	            if(v == status_ed) return '已登帐';
	            if(v == status_checked) return '已确认';
	         }
		}]
	});
	
	//移除监听
	PartsScrapRegisterSearch.grid.un('rowdblclick',PartsScrapRegisterSearch.grid.toEditFn,PartsScrapRegisterSearch.grid);
	PartsScrapRegisterSearch.grid.store.setDefaultSort('scrapDate', 'DESC');//设置默认排序
	//公用checkbox查询方法
	PartsScrapRegisterSearch.status = "10,20";
	//状态多选按钮
	PartsScrapRegisterSearch.checkQuery = function(){
		    PartsScrapRegisterSearch.status = "-1";
		if(Ext.getCmp("status_ed").checked){
			PartsScrapRegisterSearch.status = PartsScrapRegisterSearch.status + "," + status_ed;
		}
		if(Ext.getCmp("status_checked").checked){
			PartsScrapRegisterSearch.status = PartsScrapRegisterSearch.status + "," + status_checked;
		}
		PartsScrapRegisterSearch.grid.store.load();
	}
	//查询前添加过滤条件
	PartsScrapRegisterSearch.grid.store.on('beforeload',function(){
		PartsScrapRegisterSearch.searchParam = PartsScrapRegisterSearch.searchForm.getForm().getValues();
		PartsScrapRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsScrapRegisterSearch.searchParam);
		var searchParam = PartsScrapRegisterSearch.searchParam;
		searchParam.status = PartsScrapRegisterSearch.status;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('scrapDate' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'scrapDate',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'scrapDate',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'scrapDate',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'scrapDate',propValue:whTimeVal[0]+' 23:59:59',compare:6});
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
		       	items:[PartsScrapRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[PartsScrapRegisterSearch.grid]
		    }]
		 }]
	 });
});