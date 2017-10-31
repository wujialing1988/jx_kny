Ext.onReady(function(){
    Ext.namespace("PartsAccountForOutSourcing");
    PartsAccountForOutSourcing.searchParam={};
    PartsAccountForOutSourcing.idx="-1111";
    PartsAccountForOutSourcing.specificationModel = "";//规格型号
    
    // 显示扩展编号详情的函数
	PartsAccountForOutSourcing.searchExtendNo = function(rowIndex, formColNum) {
		var extendNoJson = PartsAccountForOutSourcing.grid.store.getAt(rowIndex).get("extendNoJson");
		jx.pjwz.partbase.component.PartsExtendNoWin.createWin(extendNoJson, formColNum);
	}
    
    PartsAccountForOutSourcing.grid = new Ext.yunda.Grid({
	    viewConfig: {},	// 设置显示grid组件滚动体
		loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
	    tbar:['配件名称(规格型号)：',{xtype:'textfield',width:150,id:'partsAcId',disabled:true},'&nbsp;&nbsp;',
	              {text:'确定',iconCls:'yesIcon',handler:function(){
	                   var records=PartsAccountForOutSourcing.grid.selModel.getSelections();
	                   if(records.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                   for(var i=0;i<records.length;i++){
	               	        var record=records[i];
	                    	var idx=record.get('idx');//配件信息主键
	       	          		var partsNo=record.get('partsNo');//配件编号
	       	          		var extendNoJson=record.get('extendNoJson');//扩展编号
	       	          		var partsName=record.get('partsName');//配件名称
	       	          		var partsTypeIdx=record.get('partsTypeIDX');//配件规格型号主键
	       	          		var specificationModel=record.get('specificationModel');//规格型号
	       	          		var identificationCode=record.get('identificationCode');//识别码
	       	          		var unit=record.get('unit');//计量单位
	       	          		var count=partsOutsourcing.grid.store.getCount();
	           	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
	           	          		if(count!=0){
	           	          			for(var i=0;i<count;i++){
	           	          				var record1=partsOutsourcing.grid.store.getAt(i);
	           	          				if(partsNo==record1.get('partsOutNo')){
	           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record1.get('partsOutNo')+"】</font>已存在列表中，请不要重复添加");
	           	          					return ;
	           	          				}
	           	          			}
	           	          		}
	       	          		var defaultData = {partsAccountIDX:idx,partsOutNo:partsNo,extendNoJson:extendNoJson,partsName:partsName,specificationModel:specificationModel,identificationCode:identificationCode,unit:unit,partsTypeIdx:partsTypeIdx};
							var initData = Ext.apply({}, defaultData); 
							var recordData = new Ext.data.Record(defaultData);
		                   	partsOutsourcing.grid.store.insert(0, recordData); 
							partsOutsourcing.grid.getView().refresh(); 
							partsOutsourcing.grid.getSelectionModel().selectRow(0);
	                   }
	                   PartsAccountForOutSourcing.batchWin.hide();
	                   
	              }},'&nbsp;&nbsp;',
	              {text:'取消',iconCls:'deleteIcon',handler:function(){
	              	  PartsAccountForOutSourcing.batchForm.getForm().reset();
	                  PartsAccountForOutSourcing.searchParam={};
	                  PartsAccountForOutSourcing.grid.searchFn(PartsAccountForOutSourcing.searchParam);
	                  PartsAccountForOutSourcing.batchWin.hide();
	              }}
	    ],
	    fields: [	{
	   					sortable:false,header:'idx主键',dataIndex:'idx',hidden:true,editor:{}
					},{
						sortable:false,width:100,header:'配件编号',dataIndex:'partsNo',editor:{}
					},{
						sortable:false,width:100,header:'配件识别码',dataIndex:'identificationCode',editor:{}
					},{
						sortable:false,header:'扩展编号', dataIndex:'extendNoJson', searcher : { disabled : true }, width: 150, 
						renderer: function(v,metadata, record, rowIndex, colIndex, store) {
						var extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(v);		
						if (Ext.isEmpty(extendNo)) {
							return "";
						}
							var html = "";
				    		html = "<span><a href='#' onclick='PartsAccountForOutSourcing.searchExtendNo(" + rowIndex + ", 1)'>"+extendNo+"</a></span>";
				            return html;
						}
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
			        	sortable:false,width:80,header:'入库日期',dataIndex:'partsStatusUpdateDate',xtype:'datecolumn',editor:{}
			        },{
			        	sortable:false,width:110,header:'存放地点',dataIndex:'location',editor:{}
			        },{
			        	sortable:false,width:250,header:'详细配置',dataIndex:'configDetail',editor:{}
			        },{	
			        	/************ 以下字段为隐藏字段 *************/
			        	sortable:false,header:'配件状态',dataIndex:'partsStatusName',editor:{}
				        	
			        },{
			        	sortable:false,header:'是否新品',align:'center',dataIndex:'isNewParts',editor:{}
			        },{
			        	sortable:false,header:'配件规格型号主键',dataIndex:'partsTypeIDX',hidden:true,editor:{}
			        },{
			       		sortable:false,header:'计量单位',align:'center',dataIndex:'unit',hidden:true,editor:{}
			        },{
			        	sortable:false,header:'生产厂家主键',dataIndex:'madeFactoryIdx',hidden:true,editor:{}
			        },{
			       	 	sortable:false,header:'生产厂家',dataIndex:'madeFactoryName',hidden:true,editor:{}
			        },{
			        	sortable:false,header:'出厂日期',dataIndex:'factoryDate',hidden:true,editor:{}
			        }
	           ],
	           searchFn: function(searchParam){
	           	    searchParam.specificationModel=PartsAccountForOutSourcing.specificationModel;
	                PartsAccountForOutSourcing.searchParam=searchParam;
	           		this.store.load();	
               }
	});
	PartsAccountForOutSourcing.grid.un("rowdblclick",PartsAccountForOutSourcing.grid.toEditFn,PartsAccountForOutSourcing.grid);
	 PartsAccountForOutSourcing.grid.store.on('beforeload',function(){
	     var searchParam=PartsAccountForOutSourcing.searchParam;
	     searchParam=MyJson.deleteBlankProp(searchParam);
	     //查询【除在库以外的在册】状态的周转信息
//	     var sqlStr = " parts_status  not like '" + PARTS_STATUS_ZK + "%' and parts_status not like '" + PARTS_STATUS_FZC + "%' ";
	     //查询【待修】状态的周转信息
	     var sqlStr = " parts_status  like '" + PARTS_STATUS_DX + "%'";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} //通过回段日期过滤
					]
	     for(prop in searchParam){
	     	if(prop=='partsNo' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}
	     	if(prop=='partsStatus' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LLIKE});
	     		continue;
	     	}
	     	if(prop=='specificationModel' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}else{
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ,stringLike:false});
	     		continue;
	     	}
	     }
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
	//配件委外登记 批量添加明细窗口表单
PartsAccountForOutSourcing.batchForm=new Ext.form.FormPanel({
	  layout:'column',baeCls:'x-plain',frame:true,labelWidth:60,
	  items:[{
	     columnWidth:.33,
	    // labelWidth:60,
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
	   	 	},{xtype: 'Base_comboTree', hiddenName:'partsStatus',id:'comboTree_select',isExpandAll: true,//hiddenName: 'partsStatusBill',
						  fieldLabel:'配件状态',returnField:[{widgetId:"textId",propertyName:"text"}],selectNodeModel:'exceptRoot',width:125,minListWidth:200,
						  treeUrl: ctx + '/partsOutsourcing!statusTree.action', rootText: '数据字典', queryParams: {'dicttypeid':'PJWZ_PARTS_ACCOUNT_STATUS'},
						  rootId: '01', rootText: '在册'},
			{stype:'textfield',id:'textId',hidden:true}
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
		 	{layout:'column',frame:true,baseCls:'x-plain',
		 	   items:[{
		 	      columnWidth:.3,
		 	      layout:'form',
		 	      baseCls:'x-plain',
		 	      items:[{xtype:'button',text:'查询',iconCls:'searchIcon',handler:function(){
		     	PartsAccountForOutSourcing.searchParam=PartsAccountForOutSourcing.batchForm.getForm().getValues();
		     	PartsAccountForOutSourcing.searchParam=MyJson.deleteBlankProp(PartsAccountForOutSourcing.searchParam);
		     	PartsAccountForOutSourcing.grid.searchFn(PartsAccountForOutSourcing.searchParam);
		     }}]
		  },{
		      columnWidth:.7,
		       baseCls:'x-plain',
		       items:[{xtype:'button',text:'重置',iconCls:'resetIcon',handler:function(){
		       PartsAccountForOutSourcing.batchForm.getForm().reset();
		        var searchParam={};
		        Ext.getCmp("trainType_comb").clearValue();
		        Ext.getCmp("trainNo_comb").clearValue();
		        Ext.getCmp("comboTree_select").clearValue();
		        Ext.getCmp("rc_comb").clearValue();
		        PartsAccountForOutSourcing.specificationModel = "" ;
		        Ext.getCmp("partsAcId").setValue("");
		        PartsAccountForOutSourcing.grid.searchFn(searchParam);
		     }}]
		  }]}
	     ]
  }]
});
 //树形面板选择按钮
   jx.pjwz.partbase.component.returnFn = function(node){
     PartsAccountForOutSourcing.searchParam=PartsAccountForOutSourcing.batchForm.getForm().getValues();
     if(node.id == 'ROOT_0'){
   		 Ext.getCmp("partsAcId").setValue("");
	     PartsAccountForOutSourcing.searchParam.specificationModel="";
	     PartsAccountForOutSourcing.specificationModel = "";
   	}else {
   		Ext.getCmp("partsAcId").setValue(node.text);
        PartsAccountForOutSourcing.searchParam.specificationModel=node.attributes.specificationModel;
        PartsAccountForOutSourcing.specificationModel = node.attributes.specificationModel;
   	}
     PartsAccountForOutSourcing.searchParam=MyJson.deleteBlankProp(PartsAccountForOutSourcing.searchParam);
	 PartsAccountForOutSourcing.grid.searchFn(PartsAccountForOutSourcing.searchParam);
   }

//配件委外登记 批量添加明细窗口
PartsAccountForOutSourcing.batchWin=new Ext.Window({
     title:'待委外配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
    	maximizable:false,  modal:true,
    	items:[{
    	   region:'west',
    	   width:280,
    	   frame:true,
    	   layout:'fit',
    	   items:[PartsOutSourcingTypeTreeSelect.panel]
    	},{
    	   region:'center',
    	   layout:'border',
    	   frame:true,
    	   items:[{
    	      region:'north',
    	      collapsible :true,
    	      height:100,
    	      title:'查询',
    	      items:[PartsAccountForOutSourcing.batchForm]
    	   },{
    	      region:'center',
    	      frame:true,
    	      layout:'fit',
    	      items:[PartsAccountForOutSourcing.grid]
    	   }]
    	}]
});
});