/**
 * 下车配件登记明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	//配件选择窗口,数据容器
	Ext.namespace("PartsoutSourcingDetail");
	PartsoutSourcingDetail.fieldWidth = 150;
	PartsoutSourcingDetail.labelWidth = 100;
	PartsoutSourcingDetail.rowIndex = "";
	PartsoutSourcingDetail.rdpIdx = "";
	
	//规格型号选择控件赋值函数
	PartsoutSourcingDetail.callReturnFn=function(node,e){
	  var defaultData = {specificationModel:node.attributes["specificationModel"],partsName:node.attributes["partsName"],partsTypeIDX:node.attributes["id"],isInRange:'否',unloadReason:'超范围'};
  	  var initData = Ext.apply({}, defaultData); 
	  var record = new Ext.data.Record(defaultData);
  	  PartsoutSourcingDetail.grid.store.insert(0, record); 
      PartsoutSourcingDetail.grid.getView().refresh(); 
      PartsoutSourcingDetail.grid.getSelectionModel().selectRow(0);
      PartsoutSourcingDetail.setPartsCounts();
	};
	
	// 设置已登记 未登记数量
	PartsoutSourcingDetail.setPartsCounts = function(){
		var length = PartsoutSourcingDetail.store.getCount();
		var record = PartsoutSourcingDetail.store.getRange(0,length);
		if(record == null || record.length == 0){
			Ext.getCmp("checkQty").setValue("0");
			Ext.getCmp("unCheckQty").setValue("0");
			return ;
		}
		var count = 0 ;
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			if(!Ext.isEmpty(data.idx)){
				count++;
			}
		}
		Ext.getCmp("checkQty").setValue(count+"");
		Ext.getCmp("unCheckQty").setValue((record.length-count)+"");
		
	};
	
	
	PartsoutSourcingDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	PartsoutSourcingDetail.store = new Ext.data.JsonStore({
	    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:false, pruneModifiedRecords: true,
	    url: ctx + "/partsOutsourcing!findPartsoutsourcingOutAll.action",
	    fields: [ "idx","rdpIdx","partsAccountIDX","partsTypeIdx","specificationModel","partsName","partsOutNo","identificationCode"
	    	,"outsourcingReasion","repairContent","outsourcingFactoryId","outsourcingFactory","outsourcingDate","status","isInRange"
	    ]
	});
	//材料规格型号，选择模式，勾选框可多选
	PartsoutSourcingDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//材料规格型号，分页工具
	PartsoutSourcingDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsoutSourcingDetail.store});
	PartsoutSourcingDetail.grid = new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: false, stripeRows: true, selModel: PartsoutSourcingDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	    selModel: PartsoutSourcingDetail.sm,
	    colModel: new Ext.grid.ColumnModel([
	    new Ext.grid.RowNumberer(), PartsoutSourcingDetail.sm,
	        { sortable:false, header:'idx', dataIndex:'idx',hidden: true},
	        { sortable:false, header: '配件名称',  dataIndex: 'partsName',width:100,renderer: function(v, metaData, record) {
 				if (!Ext.isEmpty(record.get('idx'))) {
	 				return "<span style='color:green'>" + v + "</span>";
 				}else{
 					return v ;
 				}
 			}},
 			{ sortable:false, header: "规格型号",  dataIndex: "specificationModel",width:100
		    },
	        { sortable:false, header: "配件编号",  dataIndex: "partsOutNo"
			},
			{ sortable:false, header: "配件识别码",  dataIndex: "identificationCode",width:100
         	},
		   { sortable:false, header: "委外厂家*",  dataIndex: "outsourcingFactory",width:100,renderer: function(v,m){
     	   	   	 m.css = 'x-grid-col';
     	   	   	 return v;
     	   	   },   
	    	    editor:{xtype:'PartsOutSourcingFactory_combo',fieldLabel:'委外厂家*',allowBlank:false,
          	        displayField:'factoryName',valueField:'factoryName',fields:["id","factoryName"],
          	        hiddenName:'outsourcingFactory',editable:false,id:'PartsOutSourcingFactory_select',//outsourcingFactoryId
          	        returnField:[{widgetId:'outsourcingFactoryId_id',propertyName:'id'}],
          	        listeners:{
		 				 	/*'collapse' : function(record, index){
		 				 		var record_v = PartsoutSourcingDetail.store.getAt(PartsoutSourcingDetail.rowIndex);
	 				 			var value = document.getElementById("PartsOutSourcingFactory_select").value;
	 				 			record_v.data.outsourcingFactory = value;
	 				 			record_v.data.outsourcingFactoryId = Ext.getCmp("outsourcingFactoryId_id").getValue();
								PartsoutSourcingDetail.grid.getView().refresh();
		 				 	},*/
		 				 	'beforeshow': function(me){
		 				 		var rc_comb = Ext.getCmp("PartsOutSourcingFactory_select");
				                rc_comb.reset();
				                rc_comb.clearValue();
				                rc_comb.getStore().removeAll();
				                rc_comb.cascadeStore();
		 				 	},
		 				 	'blur':function(me){
		 				 		var record_v = PartsoutSourcingDetail.store.getAt(PartsoutSourcingDetail.rowIndex);
		 				 		record_v.data.outsourcingFactoryId = Ext.getCmp("outsourcingFactoryId_id").getValue();
		 				 	}
		 				 }
          	     }
		   },
		   { sortable:false, header: "委外日期*",  dataIndex: "outsourcingDate",renderer: function(v, metaData, record){
     	   	   	 metaData.css = 'x-grid-col';
     	   	   	 var result = "" ;
     	   	   	 if(Ext.isEmpty(v)){
     	   	   	 	result = new Date().format('Y-m-d');
     	   	   	 	record.data.outsourcingDate = new Date();
     	   	   	 }else{
     	   	   	 	if(v instanceof Date){
     	   	   	 		result = v.format('Y-m-d');;
     	   	   	 	}else{
     	   	   	 		result = v;
     	   	   	 	}
     	   	   	 }
     	   	   	 return result;
     	   	   },editor:{xtype:'my97date', format: "Y-m-d"}},
		   { sortable:false, header: "委外检修原因",  dataIndex: "outsourcingReasion",renderer: function(v, metaData, record) {
 				if (Ext.isEmpty(v)) {
 					var nloadRepairClass = Ext.getCmp('PartsoutSourcing_unloadRepairClass').getValue();
 					record.data.outsourcingReasion = nloadRepairClass;
	 				return nloadRepairClass;
 				}
 				return v;
 			},width:100,
		  		 editor: new  Ext.form.TextField({
		    		maxLength:100
	         	})  
		   },
		  { sortable:false, header: "修理内容",  dataIndex: "repairContent",width:100,
		   		editor: new  Ext.form.TextField({
		    		maxLength:100
	         	})  
		   },
		   	{ sortable:false, header: "状态",  dataIndex: "status",width:100,renderer: function(v,m){
		   		 var result = '' ;
		   		 if('out' == v){
		   		 	result = '委外' ;
		   		 }else if('back' == v){
		   		 	result = '已回段' ;
		   		 }
     	   	   	 return result;
     	   	   }
		   },
		   { header:'outsourcingFactoryId', dataIndex:'outsourcingFactoryId',hidden: true,editor:{id:"outsourcingFactoryId_id"}},
	    	{ header:'partsAccountIDX', dataIndex:'partsAccountIDX',hidden: true},
	    	{ header:'isInRange', dataIndex:'isInRange',hidden: true},
	    	{ header:'partsTypeIdx', dataIndex:'partsTypeIdx',hidden: true}
	    ]),
		listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex); 
				if(fieldName == "outsourcingFactory"){
					PartsoutSourcingDetail.rowIndex = rowIndex;
				}
			},
			afteredit:function(e){
				
			},
			beforeedit:function(e){
				// 已登记数据不能编辑
				if(e.field == 'outsourcingDate' 
				|| e.field == 'outsourcingFactory'
				|| e.field == 'outsourcingReasion'
				|| e.field == 'repairContent'){
					if(!Ext.isEmpty(e.record.data.idx)){
						return false ;
					}else{
						return true ;
					}
				}
			}
		},
	    store: PartsoutSourcingDetail.store,
	    tbar: [
	        	{ text:'撤销', iconCls:'deleteIcon', handler:function(){
		        var data = PartsoutSourcingDetail.grid.selModel.getSelections();
		        if(data.length == 0){
		        	MyExt.Msg.alert("请选择一条记录！");
		            return;
		        }
			    // 后台删除
				PartsoutSourcingDetail.deleteFun(data);
        }},{
	    	text: "刷新", iconCls: "refreshIcon", handler: function(){
	    			PartsoutSourcingDetail.store.reload();
	    		}
	    },'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','*配件名称颜色为<span style="color:green">绿色</span>表示已登记数据，委外登记后不能修改'
	    ,'->','已登记数量：',{xtype:'displayfield',id:'checkQty',cls:'checkQty_css'},'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','-','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','未登记数量：',{xtype:'displayfield',id:'unCheckQty',cls:'unCheckQty_css'},'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;']
	});
	
	PartsoutSourcingDetail.store.on("beforeload", function(){
		this.baseParams.workPlanId = PartsoutSourcingDetail.rdpIdx ;
	});
	
	
	PartsoutSourcingDetail.store.on("load", function(){
		PartsoutSourcingDetail.setPartsCounts();
	});
	
	// 数据删除
	PartsoutSourcingDetail.deleteFun = function(data){
		
			var ids = [] ;
			
		    for (var i = 0; i < data.length; i++){
		    	
		    	if(!Ext.isEmpty(data[i].data.idx)){
		        	ids.push(data[i].data.idx);
		        }else if(!Ext.isEmpty(data[i].data.isInRange) && data[i].data.isInRange == '否'){
	    			PartsoutSourcingDetail.grid.store.remove(data[i]);
	    	    }
		    	
		    }
			if(ids != null && ids.length > 0){
		    	Ext.Ajax.request({
				url: ctx + "/partsOutsourcing!updateOutsourcingForCancelBatch.action",
				params: {
					ids:ids
				},
				success: function(response, options) {
					if (self.loadMask) self.loadMask.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.success == true) {
						// 调用本地删除
						PartsoutSourcingDetail.deletLocal(data);
						PartsoutSourcingDetail.setPartsCounts();
						MyExt.Msg.alert("撤销操作成功！");
					} else {
						alertFail(result.errMsg);
					}
				},
				failure: function(response, options) {
					if (self.loadMask) self.loadMask.hide();
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
	    }else{
	    	PartsoutSourcingDetail.setPartsCounts();
	    }	
		
	}
	
	// 本地删除
	PartsoutSourcingDetail.deletLocal = function(data){
		var storeAt = PartsoutSourcingDetail.grid.store.indexOf(data[0]);
        var records = PartsoutSourcingDetail.store.getModifiedRecords();
        var count = records.length; 
        var j = storeAt + 1;
        if(count-1 == storeAt){
        	j = storeAt-1;
        }
	    PartsoutSourcingDetail.grid.getSelectionModel().selectRow(j);
		for (var i = 0; i < data.length; i++){
	    	 // 超范围登记
	    	if(!Ext.isEmpty(data[i].data.isInRange) && data[i].data.isInRange == '否'){
	    		PartsoutSourcingDetail.grid.store.remove(data[i]);
	    	}else{
	    		data[i].set("idx",null);
	    		data[i].set("status",null);
	    		data[i].set("repairContent",null);
	    		PartsoutSourcingDetail.grid.getView().refresh();
	    	}
	    }
	}
	
	//登帐并新增
	PartsoutSourcingDetail.saveFun = function(){
	    var length = PartsoutSourcingDetail.store.getCount();
		var record = PartsoutSourcingDetail.store.getRange(0,length);
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("没有保存的数据！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = record[i].data;
			// 设置人员组织为当前登录人
		    data.outOrgId = teamOrgId ;
		    data.outOrg = teamOrgName ;
		    data.outEmpId = empId ;
		    data.outEmp = empName ;
		    // 检修任务单
		    data.rdpIdx = PartsoutSourcingDetail.rdpIdx;
		    
		    delete data.status ;
		    
		    // 委外日期及厂家必须填写，已登记的数据不保存
		    if(!Ext.isEmpty(data.outsourcingDate) 
		    	&& !Ext.isEmpty(data.outsourcingFactory)
		    	&& !Ext.isEmpty(data.outsourcingFactoryId)
		    	&& Ext.isEmpty(data.idx)){
		    	datas.push(data);
		    }
		}
		
		if(datas == null || datas.length == 0){
				MyExt.Msg.alert("没有可保存的数据，请查看委外日期或厂家是否填写！");
				return ;
		}
		PartsoutSourcingDetail.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/partsOutsourcing!saveBatchPartsOutsourcing.action',
            params : {registerDatas : Ext.util.JSON.encode(datas)},
            success: function(response, options){
              	PartsoutSourcingDetail.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	PartsoutSourcingDetail.store.load();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                PartsoutSourcingDetail.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	};
});