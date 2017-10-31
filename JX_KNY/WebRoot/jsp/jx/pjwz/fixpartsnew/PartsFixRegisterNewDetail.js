/**
 * 下车配件登记明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	//配件选择窗口,数据容器
	Ext.namespace("PartsFixRegisterNewDetail");
	PartsFixRegisterNewDetail.fieldWidth = 150;
	PartsFixRegisterNewDetail.labelWidth = 100;
	PartsFixRegisterNewDetail.rowIndex = "";
	PartsFixRegisterNewDetail.rdpIdx = "";
	PartsFixRegisterNewDetail.specificationModel ='';
	
	//规格型号选择控件赋值函数
	PartsFixRegisterNewDetail.callReturnFn=function(node,e){
	  var defaultData = {specificationModel:node.attributes["specificationModel"],partsName:node.attributes["partsName"],partsTypeIDX:node.attributes["id"]};
  	  var initData = Ext.apply({}, defaultData); 
	  var record = new Ext.data.Record(defaultData);
  	  PartsFixRegisterNewDetail.grid.store.insert(0, record); 
      PartsFixRegisterNewDetail.grid.getView().refresh(); 
      PartsFixRegisterNewDetail.grid.getSelectionModel().selectRow(0);
      PartsFixRegisterNewDetail.setPartsCounts();
	};
	
	// 设置已登记 未登记数量
	PartsFixRegisterNewDetail.setPartsCounts = function(){
		var length = PartsFixRegisterNewDetail.store.getCount();
		var record = PartsFixRegisterNewDetail.store.getRange(0,length);
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
	
	PartsFixRegisterNewDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	PartsFixRegisterNewDetail.store = new Ext.data.JsonStore({
	    id:"unloadIdx", root:"root", totalProperty:"totalProperty", autoLoad:false, pruneModifiedRecords: true, limit:1000,
	    url: ctx + "/partsFixRegister!findPartsAboardRegisterAll.action",
	    fields: [ "idx","partsTypeIDX","partsName","aboardPlace","specificationModel","partsTypeIDX","partsNo","identificationCode"
	    	,"partsAccountIDX","isInRange","jcpjbm","unloadIdx","unloadPartsAccountIDX","unloadPartsTypeIDX","unloadSpecificationModel","unloadPartsNo","unloadIdentificationCode","unloadPlace","aboardDate"
	    ]
	});
	//材料规格型号，选择模式，勾选框可多选
	PartsFixRegisterNewDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	// 分页工具
	PartsFixRegisterNewDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsFixRegisterNewDetail.store});
	// grid
	PartsFixRegisterNewDetail.grid = new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: true, stripeRows: true, 
	    loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true
//	    , templates:{header: new Ext.Template()}
	    },
	    selModel: PartsFixRegisterNewDetail.sm,css:'color:red',
	    colModel: new Ext.grid.ColumnModel([
	      new Ext.grid.RowNumberer(), PartsFixRegisterNewDetail.sm,
	        { sortable:false, header:'上车配件idx', dataIndex:'idx',hidden: true},
	        { sortable:false, header: '配件名称',  dataIndex: 'partsName',width:100,renderer: function(v, metaData, record) {
 				if (!Ext.isEmpty(record.get('idx'))) {
	 				return "<span style='color:green'>" + v + "</span>";
 				}else{
 					return v ;
 				}}},
	        { sortable:false, header: "下车位置",  dataIndex: "unloadPlace"
			},{ sortable:false, header: "下车规格型号",  dataIndex: "unloadSpecificationModel",width:100	    	  
		   
		   },{ sortable:false, header:'下车配件规格型号id', dataIndex:'unloadPartsTypeIDX',hidden: true
		   },{ sortable:false, header: "下车配件编号",  dataIndex: "unloadPartsNo",width:100
	        },{ sortable:false, header: "下配件识别码",  dataIndex: "unloadIdentificationCode",width:100		    	
     	   },{ sortable:false, header:'上车配件规格型号id', dataIndex:'partsTypeIDX', hidden: true, editor:{id:"partsTypeIDX_id"}
		   	},{ sortable:false, header: "上车配件规格型号",  dataIndex: "specificationModel",width:100,
     	   	   renderer: function(v,m){
     	   	   	 m.css = 'x-grid-col';
     	   	   	 return v;
     	   	   },
	    	    editor: {  id:"specificationModel_comb", 
	    	    		 xtype: 'Base_combo',hiddenName: "specificationModel",fieldLabel:"规格型号",
		 				 entity:"com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType",
		 				 displayField:"specificationModel",valueField:"specificationModel",fields:["idx","partsName","specificationModel","specificationModelCode"],			 
		 				 returnField:[{widgetId:"partsTypeIDX_id",propertyName:"idx"}], 
		 				 width: PartsFixRegisterNewDetail.fieldWidth,allowBlank: false,
		 				 listeners:{
		 				 	'beforeshow': function(me){
		 				 		var record_v = PartsFixRegisterNewDetail.store.getAt(PartsFixRegisterNewDetail.rowIndex);
		 				 		var rc_comb = Ext.getCmp("specificationModel_comb");
				                rc_comb.reset();
				                rc_comb.clearValue();
				                rc_comb.getStore().removeAll();
				                rc_comb.queryParams = {"jcpjbm":record_v.json.jcpjbm,"status":"1"};
				                rc_comb.cascadeStore();
		 				 	},
		 				 	"select" : function(me,record,idx) { 
			            		PartsFixRegisterNewDetail.specificationModel = this.getValue();
			              		var record_v = PartsFixRegisterNewDetail.store.getAt(PartsFixRegisterNewDetail.rowIndex);
	 				 		 	record_v.data.partsTypeIDX = Ext.getCmp("partsTypeIDX_id").getValue();              
		 				 	}				 	
		 				 }}		   
		   },{ sortable:false, header: "上车配件编号",  dataIndex: "partsNo",width:100,
     	   	   renderer: function(v,m){
     	   	   	 m.css = 'x-grid-col';
     	   	   	 return v;
     	   	   },
		    	editor: new  Ext.form.TextField({
		    		id:"partsNo",
		    		maxLength:25
	         	}),
	    	    editor: {  id:"parts_comb", forceSelection:false, editable:true,
    	    		 xtype: 'Base_combo',hiddenName: "partsNo",fieldLabel:"上车配件编号",
//	 				 entity:"com.yunda.jx.pjwz.partsmanage.entity.PartsAccount",
//	 				 queryHql:"from PartsAccount where recordStatus = 0 and partsStatus in('0101','0103') and  specificationModel='"+PartsFixRegisterNewDetail.specificationModels+"'",
	 				 displayField:"partsNo",valueField:"partsNo",fields:["idx","partsNo"],
	 				 width: PartsFixRegisterNewDetail.fieldWidth,allowBlank: false,
	 				 listeners:{
	 				 	'beforeshow': function(me){
	 				 	
	 				 		if(Ext.isEmpty(PartsFixRegisterNewDetail.specificationModel)){
	 				 			 MyExt.Msg.alert("请先选择规格型号！");
	 				 			 return;
 				 			}				 			
	 				 		var rc_comb = Ext.getCmp("parts_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                rc_comb.getStore().removeAll();	
			                rc_comb.queryHql="from PartsAccount where recordStatus = 0 and partsStatus in('0101','0103') and  specificationModel='"+PartsFixRegisterNewDetail.specificationModel+"'",
			                rc_comb.cascadeStore();
	 				 	}
	 				 }
	    	    }
	        },{ sortable:false, header: "上车配件识别码",  dataIndex: "identificationCode",width:100,
     	   	   renderer: function(v,m){
     	   	   	 m.css = 'x-grid-col';
     	   	   	 return v;
     	   	   },
		    	editor: new  Ext.form.TextField({
		    		maxLength:25
	         	})        
	       },{ 
				sortable:false, header: "上车日期",  dataIndex: "aboardDate",xtype: "datecolumn",format: "Y-m-d",hidden: true,editor:{xtype:'my97date', format: "Y-m-d"}
			},{ 
				sortable:false, header: "上车位置",  dataIndex: "aboardPlace",maxLength: 100,hidden: true   			
	    	},{ header:'partsAccountIDX', dataIndex:'partsAccountIDX',hidden: true},
	    	{ header:'isInRange', dataIndex:'isInRange',hidden: true},
	    	{ header:'unloadPartsAccountIDX', dataIndex:'unloadPartsAccountIDX',hidden: true},
    		  // 设置人员组织为当前登录人	  
	    	{ header:'fixEmpId', dataIndex:'fixEmpId',value:empId ,hidden: true},
	    	{ header:'fixEmp', dataIndex:'fixEmp',value: empName,hidden: true}
	    ]),
		listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex); 
				if(fieldName == "specificationModel"){
					PartsFixRegisterNewDetail.rowIndex = rowIndex;					
				}
				if(fieldName == "partsNo"){
					PartsFixRegisterNewDetail.specificationModel = PartsFixRegisterNewDetail.store.getAt(rowIndex).data.specificationModel;

				}
			},
       	 	beforeedit:function(e){
       	 		if(e.record.data.idx){
       	 			return false ;
       	 		}else{
       	 			return true ;
       	 		}
				// 规格型号超范围不能编
				if(e.field == 'specificationModel'){
					if(e.record.data.isInRange == '否'){
						return false ;
					}else{
						return true ;
					}
				}
        	}	
		},
		
	    store: PartsFixRegisterNewDetail.store,
	    tbar: [{ text:'复制下车信息', iconCls:'addIcon', handler:function(){
		        var records = PartsFixRegisterNewDetail.grid.selModel.getSelections();
		        if(records.length == 0 ){
		        	MyExt.Msg.alert("尚未选择一条记录！");
		            return;
		        }		
			    for (var i = 0; i < records.length; i++){			
					 records[i].set("aboardPlace",records[i].data.unloadPlace);
					 records[i].set("partsNo",records[i].data.unloadPartsNo);
					 records[i].set("identificationCode",records[i].data.unloadIdentificationCode);
					 records[i].set("specificationModel",records[i].data.unloadSpecificationModel);
					 records[i].set("partsTypeIDX",records[i].data.unloadPartsTypeIDX);		
					 PartsFixRegisterNewDetail.grid.getView().refresh();
			    } }
        },{
	   		text:'撤销', iconCls:'deleteIcon', handler:function(){
		        var records = PartsFixRegisterNewDetail.grid.selModel.getSelections();
		        if(records.length == 0 || records.length >1 ){
		        	MyExt.Msg.alert("请选择一条记录！");
		            return;
		        }
		        if(null == records[0].data.idx){
		       		MyExt.Msg.alert("只能撤销已经登记数据！");
		       		PartsFixRegisterNewDetail.store.load();
		            return;
	   			}
				Ext.Ajax.request({
					url: ctx + "/partsFixRegister!updateFixRegisterForCancel.action",
					params: {
						id:records[0].data.idx
					},
					success: function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						var result = Ext.util.JSON.decode(response.responseText);
						if (result.success == true) {
							PartsFixRegisterNewDetail.store.load();
							MyExt.Msg.alert("撤销操作成功！");
						} else {
							alertFail(result.errMsg);
							PartsFixRegisterNewDetail.store.load();
						}
					},
					failure: function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
		   	 } 
	    },{
	    	text: "刷新", iconCls: "refreshIcon", handler: function(){PartsFixRegisterNewDetail.store.reload();}
        },'->','已登记数量：',
    	{xtype:'displayfield',id:'checkQty',cls:'checkQty_css'},
        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','-','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
        '未登记数量：',
		{xtype:'displayfield',id:'unCheckQty',cls:'unCheckQty_css'},
        	'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
        ]
	});
	
	PartsFixRegisterNewDetail.store.on("beforeload", function(){
		this.baseParams.workPlanId = PartsFixRegisterNewDetail.rdpIdx ;
	});
	
	
	PartsFixRegisterNewDetail.store.on("load", function(){
		PartsFixRegisterNewDetail.setPartsCounts();
	});
	
	//登帐并新增
	PartsFixRegisterNewDetail.saveFun = function(){
		var form = PartsFixRegisterNew.form.getForm();
		if (!form.isValid()) return;
	    var regData = form.getValues();
	    
	    var length = PartsFixRegisterNewDetail.store.getCount();
		var record = PartsFixRegisterNewDetail.store.getRange(0,length);
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("没有保存的数据！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			for (var j = i+1; j < record.length; j++) {
				var data_v = {} ;
				data_v = record[j].data;
				if(!Ext.isEmpty(data.partsNo) && !Ext.isEmpty(data_v.partsNo) && data.partsNo == data_v.partsNo && data.partsTypeIDX == data_v.partsTypeIDX){
					MyExt.Msg.alert("配件编号不能重复！");
					return ;
				}
			}
			for (var j = i+1; j < record.length; j++) {
				var data_c = {} ;
				data_c = record[j].data;
				if(!Ext.isEmpty(data.identificationCode) && !Ext.isEmpty(data_c.identificationCode) && data.identificationCode == data_c.identificationCode){
					MyExt.Msg.alert("配件识别码不能重复！");
					return ;
				}
			}
			data.rdpIdx = PartsFixRegisterNewDetail.rdpIdx;
			data.aboardTrainType = regData.aboardTrainType;
			data.aboardTrainTypeIdx = regData.aboardTrainTypeIdx;
			data.aboardTrainNo = regData.aboardTrainNo;
			data.aboardRepairClassIdx = regData.aboardRepairClassIdx;
			data.aboardRepairClass = regData.aboardRepairClass;
			data.aboardRepairTimeIdx = regData.aboardRepairTimeIdx;
			data.aboardRepairTime = regData.aboardRepairTime;
			data.aboardPlace = data.unloadPlace;
			delete data.jcpjbm;
			delete data.unloadIdx;
			delete data.unloadPartsAccountIDX;
			delete data.unloadPlace;
			delete data.unloadSpecificationModel;
			delete data.unloadPartsTypeIDX;
			delete data.unloadPartsNo;
			delete data.unloadPlace;
			delete data.unloadIdentificationCode;
			// 只保存配件不为空的数据
			if(!Ext.isEmpty(data.partsNo) && !Ext.isEmpty(data.partsTypeIDX)&& Ext.isEmpty(data.idx)){
				datas.push(data);
			}
		}
		
		if(datas == null || datas.length == 0){
			MyExt.Msg.alert("没有可保存的数据，请查看配件编号和规格型号是否填写！");
			PartsFixRegisterNewDetail.store.reload();
			return ;
		}
		PartsFixRegisterNewDetail.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/partsFixRegister!saveFixRegisterNew.action',
            jsonData: datas,
            success: function(response, options){
              	PartsFixRegisterNewDetail.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	alertSuccess();
                	PartsFixRegisterNewDetail.store.load();
                } else {
                	PartsFixRegisterNewDetail.store.reload();
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                PartsFixRegisterNewDetail.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	};
});