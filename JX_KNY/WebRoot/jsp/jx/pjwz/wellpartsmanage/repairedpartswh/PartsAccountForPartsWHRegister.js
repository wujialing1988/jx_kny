Ext.onReady(function(){
    Ext.namespace("PartsAccount");
    PartsAccount.searchParam={};
    PartsAccount.idx="-1111";
    PartsAccount.fieldWidth = 200;
	PartsAccount.labelWidth = 70;
	PartsAccount.specificationModel = "";//规格型号
	
    PartsAccount.batchForm=new Ext.form.FormPanel({
	  layout:'column',
	  baeCls:'x-plain',
	  frame:true,
	  labelWidth: PartsAccount.labelWidth,
	  defaults:{
	     columnWidth:.33,
	     layout:'form',
	     baseCls:'x-plain'
	  },
	  items:[{
	     items:[{ 
	     		id:"trainType_comb",xtype: "TrainType_combo",
				fieldLabel: "下车车型",
				xtype: "Base_combo",
				hiddenName: "unloadTrainTypeIdx",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode','vehicleType'],
                queryParams: {},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                width:120,
                editable:false,	
				listeners : {   
				    "select" : function(combo, record, index) {   
			                //重新加载修程下拉数据
			                var vehicleType = record.data.vehicleType ;
			                var rc_comb = Ext.getCmp("rc_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                rc_comb.getStore().removeAll();
			                rc_comb.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
			                rc_comb.cascadeStore();
				        	}   
			    	 }
	         },{
	         	xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"
         	}]
        },{
	     items:[{
		     	xtype:'textfield',fieldLabel:'下车车号',name:"unloadTrainNo"
		      }]
 		},{
	     items:[{
					id:"rc_comb",
					xtype: "Base_combo",
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					fieldLabel: "下车修程",
					hiddenName: "unloadRepairClassIdx", 
					displayField: "xcName",
					valueField: "xcID",
					pageSize: 20, minListWidth: 200,
					queryHql: 'from UndertakeRc',
					width: 140,
					editable:false
				},{
					layout:'column',frame:true,baseCls:'x-plain',
					items:[{
					   columnWidth:.3,
					   frame:true,baseCls:'x-plain',
					   items:[
					   {xtype:'button',text:'查询',iconCls:'searchIcon',handler:function(){
						   	PartsAccount.searchParam=PartsAccount.batchForm.getForm().getValues();
					     	PartsAccount.grid.searchFn(PartsAccount.searchParam);
				     }}]
				},{
					   columnWidth:.7,
					   frame:true,baseCls:'x-plain',
					   items:[
					     {xtype:'button',text:'重置',iconCls:'resetIcon',handler:function(){
					        var searchParam={};
					         Ext.getCmp("trainType_comb").clearValue();
					         Ext.getCmp("rc_comb").clearValue();
					         PartsAccount.specificationModel = "";
					         Ext.getCmp("partsAcId").setValue("");
					         PartsAccount.batchForm.getForm().reset();
					     	 PartsAccount.grid.searchFn(searchParam);
					     }}
					   ]
					}]
				}]
	  		}]
	});
    PartsAccount.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
		storeAutoLoad : false,
		viewConfig:{},		// 设置显示grid组件的滚动条
	    tbar:['配件名称(规格型号)：',{xtype:'textfield',width:150,id:'partsAcId',disabled:true},'&nbsp;&nbsp;',
	              {text:'确定',iconCls:'yesIcon',handler:function(){
	                   var records=PartsAccount.grid.selModel.getSelections();
	                   if(records.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                    var record_v;
	                    var count=PartsWHRegisterDetail.grid.store.getCount();
                		for(var i=0;i<records.length;i++){
                			var entity = records[i].data;
                			record_v = new Ext.data.Record();
                			//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
           	          		if(count!=0){
           	          			for(var i=0;i<count;i++){
           	          				var record=PartsWHRegisterDetail.grid.store.getAt(i);
           	          				if(entity.partsNo==record.get('oldPartsNo')){
           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('oldPartsNo')+"】</font>已存在列表中，请不要重复添加");
           	          					return ;
           	          				}
           	          			}
           	          		}
							record_v.set("partsAccountIdx",entity["idx"]);
							record_v.set("partsTypeIDX",entity["partsTypeIDX"]);
							record_v.set("partsName",entity["partsName"]);
							record_v.set("specificationModel",entity["specificationModel"]);
							record_v.set("partsNo",entity["partsNo"]);
							record_v.set("configDetail",entity["configDetail"]);
							record_v.set("locationName",entity["location"]);
							record_v.set("oldPartsNo",entity["partsNo"]);
							record_v.set("identificationCode",entity["identificationCode"]);
							record_v.set("billType",BILLTYPE_SELF);
                			PartsWHRegisterDetail.grid.store.insert(0, record_v); 
					        PartsWHRegisterDetail.grid.getView().refresh(); 
					        PartsWHRegisterDetail.grid.getSelectionModel().selectRow(0);
                		}
	                   PartsWHRegisterDetail.batchWin.hide();
	                   
	              }},'&nbsp;&nbsp;',
	              {text:'取消',iconCls:'deleteIcon',handler:function(){
	              	    PartsWHRegisterDetail.batchWin.hide();
	              }}
	    ],
	    fields: [{
	             	sortable:false,header:'idx主键',dataIndex:'idx',hidden:true,editor:{}
	           },{
	             	sortable:false,width:100,header:'配件编号',dataIndex:'partsNo',editor:{}
	           },{
	             	sortable:false,width:100,header:'配件识别码',dataIndex:'identificationCode',editor:{}
	           },{
	         		sortable:false,width:150,header:'配件名称',dataIndex:'partsName',editor:{}
	           },{
	         	 	sortable:false,width:200,header:'规格型号',dataIndex:'specificationModel',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车车型',dataIndex:'unloadTrainType',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车车号',dataIndex:'unloadTrainNo',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车修程',dataIndex:'unloadRepairClass',editor:{}
	           },{
	         	 	sortable:false,width:60,align:'center',header:'下车修次',dataIndex:'unloadRepairTime',editor:{}
	           },{
	         		sortable:false,width:60,header:'是否新品',dataIndex:'isNewParts',hidden:true,editor:{}
	           },{
	         	 	sortable:false,width:110,header:'存放地点',dataIndex:'location',editor:{}
	           },{
	          		sortable:false,width:250,header:'详细配置',dataIndex:'configDetail',editor:{}
	           },{
	         	 	sortable:false,width:60,header:'配件状态',dataIndex:'partsStatusName',editor:{}
	           },{
	           		/************ 以下字段为隐藏字段 ************/
	         		sortable:false,header:'配件规格型号主键',dataIndex:'partsTypeIDX',hidden:true,editor:{}
	           },{
	         	 	sortable:false,width:60,header:'配件状态',dataIndex:'partsStatus',hidden:true,editor:{}
	           },{
	          		sortable:false,width:60,align:'center',header:'计量单位',dataIndex:'unit',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'生产厂家主键',dataIndex:'madeFactoryIdx',hidden:true,editor:{}
	           },{
	          		sortable:false,header:'生产厂家',dataIndex:'madeFactoryName',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'出厂日期',dataIndex:'factoryDate',hidden:true,editor:{}
	           }],
	           searchFn: function(searchParam){
	                searchParam.specificationModel=PartsAccount.specificationModel;
	                PartsAccount.searchParam=searchParam;
	           		this.store.load();	
               }
	});
	//树形面板选择按钮
   jx.pjwz.partbase.component.returnFn = function(node){
     PartsAccount.searchParam=PartsAccount.batchForm.getForm().getValues();
     if(node.id == 'ROOT_0'){
   		 Ext.getCmp("partsAcId").setValue("");
	     PartsAccount.searchParam.specificationModel="";
	     PartsAccount.specificationModel = "";
   	}else {
   		Ext.getCmp("partsAcId").setValue(node.text);
        PartsAccount.searchParam.specificationModel=node.attributes.specificationModel;
        PartsAccount.specificationModel = node.attributes.specificationModel;
   	}
     PartsAccount.searchParam=MyJson.deleteBlankProp(PartsAccount.searchParam);
	 PartsAccount.grid.searchFn(PartsAccount.searchParam);
   }
	PartsAccount.grid.un("rowdblclick",PartsAccount.grid.toEditFn,PartsAccount.grid);
	PartsAccount.grid.store.on('beforeload',function(){
//	 	PartsAccount.searchParam = PartsAccount.batchForm.getForm().getValues();
	     var searchParam=PartsAccount.searchParam;
	     searchParam=MyJson.deleteBlankProp(searchParam);
	      //查询【除在库以外的在册】状态的周转信息
//	     var sqlStr = " parts_status  not like '" + PARTS_STATUS_ZK + "%' and parts_status not like '" + PARTS_STATUS_FZC + "%' ";
	     //查询【良好不在库、委外、修竣】状态的周转信息
	     //因有了委外回段模块，所以委外配件不能再修竣入库
	     //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好；责任部门为【机构】就是不在库
	     var sqlStr = " ((parts_status  like '" + PARTS_STATUS_LH + "%' and MANAGE_DEPT_Type=" + MANAGE_DEPT_TYPE_ORG + ") or parts_status like '" + PARTS_STATUS_XJ + "%' )";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} 
					]
//		 var whereList = [];
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
//	     whereList.push({propName:"partsStatus",propValue:PARTS_STATUS_ZX,compare:Condition.LIKE});
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
});