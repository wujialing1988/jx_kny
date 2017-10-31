Ext.onReady(function(){
	 Ext.namespace("partsOutsourcingSearch");
	 partsOutsourcingSearch.searchParam={};
	 
partsOutsourcingSearch.grid=new Ext.yunda.Grid({
    loadURL: ctx + '/partsOutsourcing!pageQuery.action',
    tbar:[],
    viewConfig:{},
       fields: [
			{
				header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'配件委外编号', dataIndex:'partsOutNo',width:150, editor:{}
			},{
				header:'配件名称', dataIndex:'partsName',width:100, editor:{ }
			},{
	        	dataIndex:'identificationCode',header:'配件识别码'//,editor:{disabled:true}
	        },{
				header:'规格型号', dataIndex:'specificationModel',width:100, editor:{ }
			},{
				header:'详细配置', dataIndex:'configDetail',width:200, editor:{  }
			},{
				header:'委外检修原因', dataIndex:'outsourcingReasion',width:200, editor:{  }
			},{
				header:'委修厂家', dataIndex:'outsourcingFactory', width:100,editor:{  }
			},{
				header:'委修厂家主键', dataIndex:'outsourcingFactoryId',hidden:true, editor:{  }
			},{
				header:'车牌号', dataIndex:'carNo',width:70, editor:{  }
			},{
				header:'委外日期', dataIndex:'outsourcingDate', width:80,editor:{  }
			},{
				header:'经办人', dataIndex:'outEmp', width:70,editor:{  }
			},{
				header:'修理内容', dataIndex:'repairContent',width:300, editor:{  }
			},{
				header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true, editor:{ }
			},{
				header:'规格型号主键', dataIndex:'partsTypeIdx', hidden:true,editor:{  }
			}]
    
	 });
	 partsOutsourcingSearch.form=new Ext.form.FormPanel({
	    layout:'table',layoutConfig:{columns:5},margins:'10 0 5 20', 
	    defaults:{bodyStyle:'padding:4px'},buttonAlign:"center",
	    items:[{
	        layout:'form', labelWidth:60,baseCls:'x-plain',
	        items:[
	            {xtype:'textfield',id:'specificationModel',fieldLabel:'规格型号'}
	        ]
	     },{
	        layout:'form',labelWidth:80,baseCls:'x-plain',
	        items:[{xtype:'textfield',id:'partsOutNo',fieldLabel:'委外配件编号'}]
	     },{
	        layout:'form',labelWidth:60,baseCls:'x-plain',colspan:'2',
	        items:[{xtype:'textfield',id:'outsourcingFactory',fieldLabel:'委外厂家',hidden:true},
	               {xtype:'PartsOutSourcingFactory_combo',fieldLabel:'委外厂家',allowBlank:true,
          	        displayField:'factoryName',valueField:'id',
          	        hiddenName:'outsourcingFactoryId',editable:false,id:'PartsOutSourcingFactory_select',//outsourcingFactoryId
          	        returnField:[{widgetId:'outsourcingFactory',propertyName:'factoryName'}]}
	               ]
	     },{
	         layout:'form',labelWidth:60,baseCls:'x-plain',
	         items:[{xtype:'my97date',format:'Y-m-d',id:'outsourcingDate',name:'outsourcingDate',initNow:false,fieldLabel:'委外日期',width:125}]
	     },{
	        layout:'form',labelWidth:10,baseCls:'x-plain',
	        items:[{xtype:'my97date',format:'Y-m-d',id:'outsourcingDate_end',name:'outsourcingDate',initNow:false,fieldLabel:'至',width:125}]
	     },{
	        layout:'form',labelWidth:50,baseCls:'x-plain',
	        items:[{xtype:'textfield',id:'carNo',fieldLabel:'车牌号'}]
	     },{
	         layout:'form',labelWidth:50,baseCls:'x-plain',
	         items:[{xtype:'textfield',id:'outEmp',fieldLabel:'经办人',hidden:true},
	         	{id:"outEmp_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: true,
			        	         fieldLabel : "经办人",hiddenName:'outEmpId',
			        	         returnField:[{widgetId:"outEmp",propertyName:"empname"}]}]
	     }],
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	partsOutsourcingSearch.searchParam = partsOutsourcingSearch.form.getForm().getValues();
			    partsOutsourcingSearch.searchParam = MyJson.deleteBlankProp(partsOutsourcingSearch.searchParam);
			    partsOutsourcingSearch.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	partsOutsourcingSearch.form.getForm().reset();
            	Ext.getCmp("outsourcingFactory").setValue("");
				Ext.getCmp("PartsOutSourcingFactory_select").clearValue();
				Ext.getCmp("outEmp_select").clearValue();
            	partsOutsourcingSearch.searchParam = {};
			    partsOutsourcingSearch.grid.store.load();
            }
        }]
	 });
	
	partsOutsourcingSearch.grid.store.setDefaultSort('outsourcingDate', 'desc');
	//取消监听事件
	partsOutsourcingSearch.grid.un("rowdblclick",partsOutsourcingSearch.grid.toEditFn,partsOutsourcingSearch.grid);
	//页面加载
	partsOutsourcingSearch.grid.store.on('beforeload',function(){
		searchParam =partsOutsourcingSearch.form.getForm().getValues();
		partsOutsourcingSearch.searchParam=MyJson.deleteBlankProp(searchParam);
	   var searchParam=partsOutsourcingSearch.searchParam;
	   var whereList = []; 
//	//设置查询条件,只抓取在段状态为已入段的机车信息 propValue:10 已入场  20:已兑现 30:已出段
	for(prop in searchParam){
		 	//故障发生日期(起) 运算符为">="
		 	if('outsourcingDate' == prop){
			 	var whTimeVal_v = searchParam[prop];
			 	whTimeVal_v = whTimeVal_v.toString();
			 	var whTimeVal = whTimeVal_v.split(",");
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'outsourcingDate',propValue:whTimeVal[0],compare:4});
			 				whereList.push({propName:'outsourcingDate',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'outsourcingDate',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'outsourcingDate',propValue:whTimeVal[0]+' 23:59:59',compare:6});
			 		}
	                } 
		 		continue;
		 	}
		 	if('outEmpId'==prop){
		 		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ,stringLike:false});
		 		continue;
		 	}
		 	if('takeOverEmpId'==prop){
		 		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ,stringLike:false});
		 		continue;
		 	}
		 	whereList.push({propName:prop,propValue:searchParam[prop],compare:8});
	}
    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		
	});
	 var viewport=new Ext.Viewport({
	    layout:'fit',items:[{
	      layout:'border',
	       frame:true,
	       items:[{
		       region:'north',
		       margins:'10 0 10 0',
		       frame:true,
		       title:'查询',
		       collapsible:true,
		       height:180,
		       items:[partsOutsourcingSearch.form]
		   	},{
		        region:'center',
		        frame:true,
		        layout:'fit',
		        items:[partsOutsourcingSearch.grid]
		    }]
	  }]
	 });
});