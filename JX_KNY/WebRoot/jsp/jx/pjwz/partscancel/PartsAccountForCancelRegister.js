Ext.onReady(function(){
    Ext.namespace("PartsAccountForCancelRegister");
    PartsAccountForCancelRegister.searchParam={};
    //PartsAccountForCancelRegister.idx="-1111";
    PartsAccountForCancelRegister.specificationModel = "";//规格型号
    
    PartsAccountForCancelRegister.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
		viewConfig:{forceFit: false},		// 设置显示grid组件的滚动条
	    tbar:['配件名称(规格型号)：',{xtype:'textfield',width:150,id:'partsAcId',disabled:true},'&nbsp;&nbsp;',
	              {text:'确定',iconCls:'yesIcon',handler:function(){
	                   var records=PartsAccountForCancelRegister.grid.selModel.getSelections();
	                   if(records.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                   for(var i=0;i<records.length;i++){
	                   	var defaultData=records[i].data;
	                   	var count=PartsCancelRegisterDetail.grid.store.getCount();
       	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
       	          		if(count!=0){
       	          			for(var i=0;i<count;i++){
       	          				var record1=PartsCancelRegisterDetail.grid.store.getAt(i);
       	          				if(defaultData.partsNo==record1.get('partsNo')){
       	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record1.get('partsNo')+"】</font>已存在列表中，请不要重复添加");
       	          					return ;
       	          				}
       	          			}
       	          		}
						var record = new Ext.data.Record(defaultData);
	                   	PartsCancelRegisterDetail.grid.store.insert(0, record); 
						PartsCancelRegisterDetail.grid.getView().refresh(); 
						PartsCancelRegisterDetail.grid.getSelectionModel().selectRow(0);
	                   }
	                   PartsAccountForCancelRegister.batchWin.hide();
	                   
	              }},'&nbsp;&nbsp;',
	              {text:'取消',iconCls:'deleteIcon',handler:function(){
	              	    var searchParam={};
				        PartsAccountForCancelRegister.batchForm.getForm().reset();
				     	PartsAccountForCancelRegister.grid.searchFn(searchParam);
				     	PartsAccountForCancelRegister.batchWin.hide();
	              }}
	    ],
	    fields: [{
	             	sortable:false,header:'idx主键',dataIndex:'idx',hidden:true,editor:{}
	           },{
	             	sortable:false,width:100,header:'配件编号',dataIndex:'partsNo',editor:{}
	           },{
	         		sortable:false,width:150,header:'配件名称',dataIndex:'partsName',editor:{}
	           },{
	         	 	sortable:false,width:200,header:'规格型号',dataIndex:'specificationModel',editor:{}
	           },{
	           		sortable:false,width:200,header:'配件识别码',dataIndex:'identificationCode',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车车型',dataIndex:'unloadTrainType',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车车号',dataIndex:'unloadTrainNo',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车修程',dataIndex:'unloadRepairClass',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车修次',dataIndex:'unloadRepairTime',editor:{}
	           },{
	         	 	sortable:false,align:'center',header:'配件状态',dataIndex:'partsStatusName',hidden:false,editor:{}
	           },{
	         	 	sortable:false,width:60,header:'配件状态',dataIndex:'partsStatus',hidden:true,editor:{}
	           },{
	         		sortable:false,width:60,align:'center',header:'是否新品',dataIndex:'isNewParts',editor:{}
	           },{
	         	 	sortable:false,width:110,header:'存放地点',dataIndex:'location',editor:{}
	           },{
	          		sortable:false,width:250,header:'详细配置',dataIndex:'configDetail',editor:{}
	           },{
	         		sortable:false,header:'配件规格型号主键',dataIndex:'partsTypeIDX',hidden:true,editor:{}
	           },{
	          		sortable:false,header:'计量单位',dataIndex:'unit',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'生产厂家主键',dataIndex:'madeFactoryIdx',hidden:true,editor:{}
	           },{
	          		sortable:false,header:'生产厂家',dataIndex:'madeFactoryName',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'出厂日期',dataIndex:'factoryDate',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'责任部门id',dataIndex:'manageDeptId',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'责任部门',dataIndex:'manageDept',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'责任部门类型',dataIndex:'manageDeptType',hidden:true,editor:{}
	           }],
	           searchFn: function(searchParam){
	           	    searchParam.specificationModel=PartsAccountForCancelRegister.specificationModel;
	                PartsAccountForCancelRegister.searchParam=searchParam;
	           		this.store.load();	
               }
	});
	PartsAccountForCancelRegister.batchForm=new Ext.form.FormPanel({
	  layout:'column',baeCls:'x-plain',frame:true,labelWidth:60,
	 	 items:[{
				     columnWidth:.33,
				     layout:'form',
				     baseCls:'x-plain',
		    		 items:[
		     			  { id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "下车车型",
						  hiddenName: "unloadTrainTypeIdx",
						  returnField: [{widgetId:"unloadTrainType",propertyName:"shortName"}],
						  displayField: "shortName", valueField: "typeID",width:125,
						  pageSize: 20, minListWidth: 200,   //isCx:'no',
						  editable:false  ,allowBlank: true,
							listeners : {   
					        	"select" : function() {   
					            	//重新加载车号下拉数据
				                var trainNo_comb = Ext.getCmp("trainNo_comb");   
				                trainNo_comb.reset();  
				                trainNo_comb.clearValue(); 
				                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
				                trainNo_comb.cascadeStore();
				                //重新加载修程下拉数据
				                var rc_comb = Ext.getCmp("rc_comb");
				                rc_comb.reset();
				                rc_comb.clearValue();
				                rc_comb.getStore().removeAll();
				                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
				                rc_comb.cascadeStore();
				        	}   
				    	 }
	                    },{xtype:'textfield',fieldLabel:'配件编号',id:'partsNo'},
						  {stype:'textfield',id:'textId',hidden:true},
	             {id:"unloadTrainType",xtype:"hidden", name:"unloadTrainType"}
	        ]},{
		     columnWidth:.33,
		     layout:'form',
		     baseCls:'x-plain',
		     items:[
	     			{ id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "下车车号",
						  hiddenName: "unloadTrainNo",isAll:'no',width:125,
						  displayField: "trainNo", valueField: "trainNo",
						  pageSize: 20, minListWidth: 200, 
						  editable:false,allowBlank: true,
						  listeners : {
		    				"beforequery" : function(){
		    					//选择修次前先选车型
		                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
		    					if(trainTypeId == "" || trainTypeId == null){
		    						MyExt.Msg.alert("请先选择下车车型！");
		    						return false;
		    					}
		                	}
		    			}
		   	 	}
			]},{
		     columnWidth:.33,
		     layout:'form',
		     baseCls:'x-plain',
		     items:[
		         {
					id:"rc_comb",
					xtype: "Base_combo",
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					fieldLabel: "下车修程",
					hiddenName: "unloadRepairClassIdx", 
					returnField: [{widgetId:"unloadRepairClass",propertyName:"xcName"}],
					displayField: "xcName",
					valueField: "xcID",
					pageSize: 20, minListWidth: 200,
					queryHql: 'from UndertakeRc',
					width: 125,
					allowBlank: true,
					editable:false,
					listeners : {  
						"beforequery" : function(){
							//选择修次前先选车型
		            		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
							if(trainTypeId == "" || trainTypeId == null){
								MyExt.Msg.alert("请先选择下车车型！");
								return false;
							}
		            	}
		            }
				},{id:"unloadRepairClass",xtype:"hidden", name:"unloadRepairClass"},
				{
					layout:'column',frame:true,baseCls:'x-plain',
					items:[{
					   columnWidth:.3,
					   frame:true,baseCls:'x-plain',
					   items:[
					   {xtype:'button',text:'查询',iconCls:'searchIcon',handler:function(){
						   	PartsAccountForCancelRegister.searchParam=PartsAccountForCancelRegister.batchForm.getForm().getValues();
					     	PartsAccountForCancelRegister.grid.searchFn(PartsAccountForCancelRegister.searchParam);
				     }}]
				},{
					   columnWidth:.7,
					   frame:true,baseCls:'x-plain',
					   items:[
					     {xtype:'button',text:'重置',iconCls:'resetIcon',handler:function(){
					        var searchParam={};
					         Ext.getCmp("trainType_comb").clearValue();
					         Ext.getCmp("trainNo_comb").clearValue();
					         Ext.getCmp("rc_comb").clearValue();
					         PartsAccountForCancelRegister.specificationModel = "";
					         Ext.getCmp("partsAcId").setValue("");
					         PartsAccountForCancelRegister.batchForm.getForm().reset();
					     	 PartsAccountForCancelRegister.grid.searchFn(searchParam);
					     }}
					   ]
					}]
				}]
	  		}]
		});
	PartsAccountForCancelRegister.batchWin=new Ext.Window({
     title:'待报废配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
    	maximizable:false,  modal:true,
    	items:[{
    	   region:'west',
    	   width:280,
    	   frame:true,
    	   layout:'fit',
    	   items:[PartsTypeTreeSelect.panel]
    	},{
    	   region:'center',
    	   layout:'border',
    	   frame:true,
    	   items:[{
    	      region:'north',
    	      collapsible :true,
    	      height:100,
    	      title:'查询',
    	      items:[PartsAccountForCancelRegister.batchForm]
    	   },{
    	      region:'center',
    	      frame:true,
    	      layout:'fit',
    	      items:[PartsAccountForCancelRegister.grid]
    	   }]
    	}]
	});
   //树形面板选择按钮
   jx.pjwz.partbase.component.returnFn = function(node){
     PartsAccountForCancelRegister.searchParam=PartsAccountForCancelRegister.batchForm.getForm().getValues();
     if(node.id == 'ROOT_0'){
   		 Ext.getCmp("partsAcId").setValue("");
	     PartsAccountForCancelRegister.searchParam.specificationModel="";
	     PartsAccountForCancelRegister.specificationModel = "";
   	}else {
   		Ext.getCmp("partsAcId").setValue(node.text);
        PartsAccountForCancelRegister.searchParam.specificationModel=node.attributes.specificationModel;
        PartsAccountForCancelRegister.specificationModel = node.attributes.specificationModel;
   	}
     PartsAccountForCancelRegister.searchParam=MyJson.deleteBlankProp(PartsAccountForCancelRegister.searchParam);
	 PartsAccountForCancelRegister.grid.searchFn(PartsAccountForCancelRegister.searchParam);
   }
   //禁用监听事件
	PartsAccountForCancelRegister.grid.un("rowdblclick",PartsAccountForCancelRegister.grid.toEditFn,PartsAccountForCancelRegister.grid);
	 PartsAccountForCancelRegister.grid.store.on('beforeload',function(){
	     var searchParam=PartsAccountForCancelRegister.searchParam;
	     searchParam=MyJson.deleteBlankProp(searchParam);
		 //查询【待修、良好】状态的周转信息
	     var sqlStr = " (parts_status  like '" + PARTS_STATUS_DX + "%' or parts_status like '" + PARTS_STATUS_LH + "%' )";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} 
					]
	     for(prop in searchParam){
	     	if(prop=='partsNo' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}
	     	if(prop=='specificationModel' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}else{
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ});
	     		continue;
	     	}
	     }
//	     whereList.push({propName:"partsStatus",propValue:PARTS_STATUS_ZC,compare:Condition.LLIKE});
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
});