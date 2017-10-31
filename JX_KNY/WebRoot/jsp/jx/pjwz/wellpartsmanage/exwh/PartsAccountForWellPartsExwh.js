Ext.onReady(function(){
    Ext.namespace("PartsAccount");
    PartsAccount.searchParam={};
    PartsAccount.manageDeptId = "";//库房id
    PartsAccount.fieldWidth = 200;
	PartsAccount.labelWidth = 70;
	PartsAccount.specificationModel = "";//规格型号
	
    PartsAccount.batchForm=new Ext.form.FormPanel({
	  layout:'column',
	  baeCls:'x-plain',
	  labelWidth: PartsAccount.labelWidth,
	  defaults:{
  		 columnWidth:.33,
	     layout:'form',
	     baseCls:'x-plain'
	  },
	  items:[{
	     items:[{ 
	     		id:"trainType_comb_s",
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
				listeners:{   
			      "select" : function(combo, record, index) {   
		                //重新加载修程下拉数据
		                var vehicleType = record.data.vehicleType ;
		                var rc_comb_s = Ext.getCmp("rc_comb_s");
		                rc_comb_s.reset();
		                rc_comb_s.clearValue();
		                rc_comb_s.getStore().removeAll();
		                rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
		                rc_comb_s.cascadeStore();
			        	}   
			    	 }
             },{
            	xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"
             }]
   		},{
		     items:[{
		     		xtype:'textfield',fieldLabel:'下车车号',name:"unloadTrainNo"
	     	   	},{
					xtype: 'compositefield', fieldLabel : '是否新品', combineErrors: false,
		        	items: [{   
						    xtype:'checkbox', name:'isNewParts', id: 'isNewParts_new', boxLabel:'新&nbsp;&nbsp;&nbsp;&nbsp;', 
						    	inputValue:'新', checked:true,
							    handler: function(){
							    	PartsAccount.checkQuery();
							    }
						  },{   
						    xtype:'checkbox', name:'isNewParts', id: 'isNewParts_old', boxLabel:'旧&nbsp;&nbsp;&nbsp;&nbsp;', 
						    	inputValue:'旧',checked:true,
							    handler: function(){
							    	PartsAccount.checkQuery();
							    }
						  }]
					}]
		},{
		     items:[{
					id:"rc_comb_s",
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
					   items:[{
					   		xtype:'button',text:'查询',iconCls:'searchIcon',handler:function(){
						   	PartsAccount.searchParam=PartsAccount.batchForm.getForm().getValues();
					     	PartsAccount.grid.searchFn(PartsAccount.searchParam);
				     		}
			     		}]
					},{
					   columnWidth:.7,
					   frame:true,baseCls:'x-plain',
					   items:[{
			   				xtype:'button',text:'重置',iconCls:'resetIcon',handler:function(){
			        		var searchParam={};
					         Ext.getCmp("trainType_comb_s").clearValue();
					         Ext.getCmp("rc_comb_s").clearValue();
					         PartsAccount.specificationModel = "";
					         Ext.getCmp("partsAcId").setValue("");
					         PartsAccount.batchForm.getForm().reset();
					     	 PartsAccount.grid.searchFn(searchParam);
					     	}
					     }]
					}]
		     }]
  		}]
	});
    PartsAccount.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
		storeAutoLoad : false,
		viewConfig:{},		// 显示grid组件滚动体
	    tbar:['配件名称(规格型号)：',{xtype:'textfield',width:150,id:'partsAcId',disabled:true},'&nbsp;&nbsp;',
	              {text:'确定',iconCls:'yesIcon',handler:function(){
	                   var records=PartsAccount.grid.selModel.getSelections();
	                   if(records.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                    var record_v;
		                var count=WellPartsExwhDetail.grid.store.getCount();
                		for(var i=0;i<records.length;i++){
                			var entity = records[i].data;
                			record_v = new Ext.data.Record();
                			//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
           	          		if(count!=0){
           	          			for(var i=0;i<count;i++){
           	          				var record=WellPartsExwhDetail.grid.store.getAt(i);
           	          				if(entity.partsNo==record.get('partsNo')){
           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('partsNo')+"】</font>已存在列表中，请不要重复添加");
           	          					return ;
           	          				}
           	          			}
           	          		}
							record_v.set("partsAccountIDX",entity["idx"]);
							record_v.set("partsTypeIDX",entity["partsTypeIDX"]);
							record_v.set("partsName",entity["partsName"]);
							record_v.set("specificationModel",entity["specificationModel"]);
							record_v.set("partsNo",entity["partsNo"]);
							record_v.set("identificationCode",entity["identificationCode"]);
							record_v.set("locationName",entity["location"]);
							record_v.set("isDeliver","否");
							record_v.set("toGo","上车");
                			WellPartsExwhDetail.grid.store.insert(0, record_v); 
					        WellPartsExwhDetail.grid.getView().refresh(); 
					        WellPartsExwhDetail.grid.getSelectionModel().selectRow(0);
                		}
	                   WellPartsExwhDetail.batchWin.hide();
	                   Ext.getCmp("whName_comb").disable();
	                   
	              }},'&nbsp;&nbsp;',
	              {text:'取消',iconCls:'deleteIcon',handler:function(){
	              	    WellPartsExwhDetail.batchWin.hide();
	              }}
	    ],
	    fields: [  {
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
	                sortable:false,width:60,header:'配件状态',dataIndex:'partsStatusName',editor:{}
	           },{
	         	 	sortable:false,width:60,header:'配件状态',dataIndex:'partsStatus',editor:{},hidden:true
	           },{
	         		sortable:false,width:60,align:'center',header:'是否新品',dataIndex:'isNewParts',editor:{}
	           },{
	         	 	sortable:false,width:110,header:'存放地点',dataIndex:'location',editor:{}
	           },{
	          		sortable:false,width:250,header:'详细配置',dataIndex:'configDetail',editor:{}
	           },{
	         		sortable:false,header:'配件规格型号主键',dataIndex:'partsTypeIDX',hidden:true,editor:{}
	           },{
		           /************** 以下字段为隐藏字段 ***************/
	          		sortable:false,header:'计量单位',dataIndex:'unit',hidden:true,editor:{}
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
   //公用checkbox查询方法
	PartsAccount.isNewParts = "新,旧";
	//状态多选按钮
	PartsAccount.checkQuery = function(){
		PartsAccount.isNewParts = "-1";
		if(Ext.getCmp("isNewParts_new").checked){
			PartsAccount.isNewParts = PartsAccount.isNewParts + ",新";
		} 
		if(Ext.getCmp("isNewParts_old").checked){
			PartsAccount.isNewParts = PartsAccount.isNewParts + ",旧";
		} 
		PartsAccount.grid.store.load();
	}
	PartsAccount.grid.un("rowdblclick",PartsAccount.grid.toEditFn,PartsAccount.grid);
	PartsAccount.grid.store.on('beforeload',function(){
	     var searchParam=PartsAccount.searchParam;
	     searchParam.isNewParts = PartsAccount.isNewParts;
	     searchParam=MyJson.deleteBlankProp(searchParam);
		 var whereList = [];
	     for(prop in searchParam){
	     	if(prop=='partsNo' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}
	     	if(prop=='specificationModel' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}
	     	if('isNewParts' == prop){
				var val = searchParam[prop];
				val = val.toString();
				val = val.split(",");
                if(val instanceof Array){
                    whereList.push({propName:'isNewParts', propValues:val, compare:Condition.IN });
                } else {
                    var valAry = [];
                    valAry.push(val);
                    whereList.push({propName:'isNewParts', propValues:valAry, compare:Condition.IN });
                }
                continue;
		 	}else{
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ});
	     		continue;
	     	}
	     }
	     //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好；责任部门为【库房】就是在库
	     whereList.push({propName:"partsStatus",propValue:PARTS_STATUS_LH,compare:Condition.LLIKE});
	     whereList.push({propName:"manageDeptId",propValue:PartsAccount.manageDeptId,compare:Condition.EQ});//库房id
	     whereList.push({propName:"manageDeptType",propValue:MANAGE_DEPT_TYPE_WH,compare:Condition.EQ});//责任部门类型---库房
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
});