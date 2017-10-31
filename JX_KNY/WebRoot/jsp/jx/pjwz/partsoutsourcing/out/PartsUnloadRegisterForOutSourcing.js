Ext.onReady(function(){
    Ext.namespace("PartsUnloadRegisterForOutSourcing");
    PartsUnloadRegisterForOutSourcing.searchParam={};
    PartsUnloadRegisterForOutSourcing.rdpIdx="-1111";
    PartsUnloadRegisterForOutSourcing.specificationModel = "";//规格型号
    
    PartsUnloadRegisterForOutSourcing.grid = new Ext.yunda.Grid({
	    viewConfig: {},	// 设置显示grid组件滚动体
		loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
	    tbar:[{text:'确定',iconCls:'yesIcon',handler:function(){
	                   var records=PartsUnloadRegisterForOutSourcing.grid.selModel.getSelections();
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
	       	          		var count=PartsoutSourcingDetail.grid.store.getCount();
	           	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
	           	          		if(count!=0){
	           	          			for(var j=0;j<count;j++){
	           	          				var record1=PartsoutSourcingDetail.grid.store.getAt(j);
	           	          				if(partsNo==record1.get('partsOutNo') && specificationModel==record1.get('specificationModel')){
	           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record1.get('partsOutNo')+"】</font>，规格型号为已存在列表中<font color='red'>【"+record1.get('specificationModel')+"】</font>，请不要重复添加");
	           	          					return ;
	           	          				}
	           	          			}
	           	          		}
	       	          		var defaultData = {
	       	          			partsAccountIDX:idx,
	       	          			partsOutNo:partsNo,
	       	          			extendNoJson:extendNoJson,
	       	          			partsName:partsName,
	       	          			specificationModel:specificationModel,
	       	          			identificationCode:identificationCode,
	       	          			unit:unit,
	       	          			partsTypeIdx:partsTypeIdx,
	       	          			outsourcingFactory:'',
	       	          			outsourcingFactoryId:'',
	       	          			outsourcingReasion:'超范围',
	       	          			repairContent:'',
	       	          			outsourcingDate:new Date(),
	       	          			isInRange:'否'
	       	          			};
							var initData = Ext.apply({}, defaultData); 
							var recordData = new Ext.data.Record(defaultData);
		                   	PartsoutSourcingDetail.grid.store.insert(0, recordData); 
							PartsoutSourcingDetail.grid.getView().refresh(); 
							PartsoutSourcingDetail.grid.getSelectionModel().selectRow(0);
							PartsoutSourcingDetail.setPartsCounts();
	                   }
	                   PartsUnloadRegisterForOutSourcing.batchWin.hide();
	                   
	              }},'&nbsp;&nbsp;',
	              {text:'取消',iconCls:'deleteIcon',handler:function(){
	                  PartsUnloadRegisterForOutSourcing.searchParam={};
	                  PartsUnloadRegisterForOutSourcing.grid.searchFn(PartsUnloadRegisterForOutSourcing.searchParam);
	                  PartsUnloadRegisterForOutSourcing.batchWin.hide();
	              }}
	    ],
	    fields: [	{
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
	           	    searchParam.specificationModel=PartsUnloadRegisterForOutSourcing.specificationModel;
	                PartsUnloadRegisterForOutSourcing.searchParam=searchParam;
	           		this.store.load();	
               }
	});
	
	PartsUnloadRegisterForOutSourcing.grid.un("rowdblclick",PartsUnloadRegisterForOutSourcing.grid.toEditFn,PartsUnloadRegisterForOutSourcing.grid);
	
	 PartsUnloadRegisterForOutSourcing.grid.store.on('beforeload',function(){
	     var searchParam=PartsUnloadRegisterForOutSourcing.searchParam;
	     searchParam=MyJson.deleteBlankProp(searchParam);
	     //查询【除在库以外的在册】状态的周转信息
//	     var sqlStr = " parts_status  not like '" + PARTS_STATUS_ZK + "%' and parts_status not like '" + PARTS_STATUS_FZC + "%' ";
	     //查询【待修】状态的周转信息
	     var sqlStr = " parts_status  like '" + PARTS_STATUS_DX + "%' and idx in (select parts_account_idx from PJWZ_Parts_Unload_Register where record_status = 0 and rdp_idx = '"+PartsUnloadRegisterForOutSourcing.rdpIdx+"')";
		 
	     var count=PartsoutSourcingDetail.grid.store.getCount();
	     var partsAccounts = '';
   	          		if(count!=0){
   	          			for(var i=0;i<count;i++){
   	          				var record1=PartsoutSourcingDetail.grid.store.getAt(i);
   	          				if(!Ext.isEmpty(record1.get('partsAccountIDX'))){
   	          					partsAccounts += "'" + record1.get('partsAccountIDX') + "'," ;
   	          				}
   	          			}
   	          		}
		 if(!Ext.isEmpty(partsAccounts)){
		 	sqlStr += " and idx not in (" + partsAccounts.substring(0,partsAccounts.length-1) + ")";
		 }
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

//配件委外登记 批量添加明细窗口
PartsUnloadRegisterForOutSourcing.batchWin=new Ext.Window({
     title:'待委外配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
    	maximizable:false,  modal:true,
    	items:[{
    	   region:'center',
    	   layout:'border',
    	   frame:true,
    	   items:[{
    	      region:'center',
    	      frame:true,
    	      layout:'fit',
    	      items:[PartsUnloadRegisterForOutSourcing.grid]
    	   }]
    	}]
});
});