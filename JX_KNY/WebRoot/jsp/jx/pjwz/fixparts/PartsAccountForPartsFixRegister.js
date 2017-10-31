Ext.onReady(function(){
    Ext.namespace("PartsAccount");
    PartsAccount.searchParam={};
    PartsAccount.manageDeptId = "";//库房id
    PartsAccount.fieldWidth = 200;
	PartsAccount.labelWidth = 70;
	PartsAccount.specificationModel = "";//规格型号
	
	// 显示扩展编号详情的函数
	PartsAccount.searchExtendNo = function(rowIndex, formColNum) {
		var extendNoJson = PartsAccount.grid.store.getAt(rowIndex).get("extendNoJson");
		jx.pjwz.partbase.component.PartsExtendNoWin.createWin(extendNoJson, formColNum);
	}
	
    PartsAccount.batchForm=new Ext.form.FormPanel({
	  layout:'column',baeCls:'x-plain',labelWidth: PartsAccount.labelWidth,
	  items:[{
	     columnWidth:.33,labelWidth: PartsAccount.labelWidth,
	     layout:'form',
	     baseCls:'x-plain',
	     items:[
	     	  { id:"trainType_comb_s",	 
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
	                    },{xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"}
//	             {id:"unloadTrainType",xtype:"hidden", name:"unloadTrainType"}
	        ]},{
		     columnWidth:.33,
		     layout:'form',labelWidth: PartsAccount.labelWidth,
		     baseCls:'x-plain',
		     items:[
		     		{xtype:'textfield',fieldLabel:'下车车号',name:"unloadTrainNo"},
		   	 	{
						xtype: 'compositefield', fieldLabel : '是否新品', combineErrors: false,
			        	items: [
				           {   
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
					}
		   	 	
			]},{
		     columnWidth:.33,
		     layout:'form',labelWidth: PartsAccount.labelWidth,
		     baseCls:'x-plain',
		     items:[
		         {
					id:"rc_comb_s",
					xtype: "Base_combo",
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					fieldLabel: "下车修程",
					hiddenName: "unloadRepairClassIdx", 
//					returnField: [{widgetId:"unloadRepairClass",propertyName:"xcName"}],
					displayField: "xcName",
					valueField: "xcID",
					pageSize: 20, minListWidth: 200,
					queryHql: 'from UndertakeRc',
					width: 140,
					editable:false
//					listeners : {  
//						"beforequery" : function(){
//							//选择修次前先选车型
//		            		var trainTypeId =  Ext.getCmp("trainType_comb_s").getValue();
//							if(trainTypeId == "" || trainTypeId == null){
//								MyExt.Msg.alert("请先选择下车车型！");
//								return false;
//							}
//		            	}
//		            }
				},
				{
			   	 	xtype:'button',text:'查询',handler:function(){
			        PartsAccount.searchParam = PartsAccount.batchForm.getForm().getValues();
			     	PartsAccount.grid.searchFn(PartsAccount.searchParam);
		     }}
//					{id:"unloadRepairClass",xtype:"hidden", name:"unloadRepairClass"},
		     ]
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
		                var count=PartsFixRegisterDetail.grid.store.getCount();
                		for(var i=0;i<records.length;i++){
                			var entity = records[i].data;
                			record_v = new Ext.data.Record();
                			//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
           	          		if(count!=0){
           	          			for(var i=0;i<count;i++){
           	          				var record=PartsFixRegisterDetail.grid.store.getAt(i);
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
							record_v.set("aboardDate",new Date());
                			PartsFixRegisterDetail.grid.store.insert(0, record_v); 
					        PartsFixRegisterDetail.grid.getView().refresh(); 
					        PartsFixRegisterDetail.grid.getSelectionModel().selectRow(0);
                		}
	                   PartsFixRegisterDetail.batchWin.hide();
	                   
	              }},'&nbsp;&nbsp;',
	              {text:'取消',iconCls:'deleteIcon',handler:function(){
	              	    PartsFixRegisterDetail.batchWin.hide();
	              }}
	    ],
	    fields: [  {
	             	sortable:false,header:'idx主键',dataIndex:'idx',hidden:true,editor:{}
	           },{
	             	sortable:false,width:100,header:'配件编号',dataIndex:'partsNo',editor:{}
	           },{
	             	sortable:false,width:100,header:'配件识别码',dataIndex:'identificationCode',editor:{}
	           },{
	           		/** 页面扩展编号显示方式一 */
					header:'扩展编号', dataIndex:'extendNoJson', searcher : { disabled : true }, width: 150, 
					renderer: function(v,metadata, record, rowIndex, colIndex, store) {
						var extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(v);		
						if (Ext.isEmpty(extendNo)) {
							return "";
						}
						var html = "";
			    		html = "<span><a href='#' onclick='PartsAccount.searchExtendNo(" + rowIndex + ", 1)'>"+extendNo+"</a></span>";
			            return html;
					}
			   
	          	 	/** 页面扩展编号显示方式二 */
	            	/*
					header:'扩展编号', dataIndex:'extendNo', renderer: function(v,metadata, record, rowIndex, colIndex, store) {
						v = store.getAt(rowIndex).get('extendNoJson');
						if (!Ext.isEmpty(v)) {
							var jsonData = eval(v);
							var extendNo = '';
							if (jsonData.length > 0) {
								extendNo += jsonData[0].value;
							}
							for (var i = 1; i < jsonData.length; i++) {
								var value = jsonData[i].value;
								if (Ext.isEmpty(value) || 'null' == value || null == value || undefined == value) {
									continue;
								}
								extendNo += "|";
								extendNo += value;
							}
							return extendNo;
						}
					}
			   },{
					header:'详情', dataIndex:'extendNoJson', align:'center',
					searcher : { disabled : true }, width: 100,
					renderer: function(v,metadata, record, rowIndex, colIndex, store) {
						if (Ext.isEmpty(v)) {
							return "";
						}
						return "<img src='" + img + "' alt='扩展编号详情' style='cursor:pointer' onclick='PartsAccount.searchExtendNo("
										+ rowIndex + ",1)'/>"
					}
			   */
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
//	 	PartsAccount.searchParam = PartsAccount.batchForm.getForm().getValues();
	     var searchParam=PartsAccount.searchParam;
	     searchParam.isNewParts = PartsAccount.isNewParts;
	     searchParam=MyJson.deleteBlankProp(searchParam);
	     //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好；责任部门为【机构】就是不在库
	     var sqlStr = " (parts_status  like '" + PARTS_STATUS_LH + "%' and MANAGE_DEPT_Type=" + MANAGE_DEPT_TYPE_ORG + ")";
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
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
});