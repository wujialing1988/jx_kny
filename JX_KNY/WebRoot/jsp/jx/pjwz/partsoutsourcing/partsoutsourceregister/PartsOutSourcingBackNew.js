Ext.onReady(function(){
    Ext.namespace("PartsOutSourcingBackNew");
    PartsOutSourcingBackNew.searchParams = {};
    PartsOutSourcingBackNew.searchParams_back = {};
    PartsOutSourcingBackNew.labelWidth = 60;
	PartsOutSourcingBackNew.fieldWidth = 150;
  	// 设置已登记 未登记数量
	PartsOutSourcingBackNew.setPartsCounts = function(){
		var length = PartsOutSourcingBackNew.store.getCount();
		var record = PartsOutSourcingBackNew.store.getRange(0,length);
		if(record == null || record.length == 0){
			Ext.getCmp("checkQty").setValue("0");
			Ext.getCmp("unCheckQty").setValue("0");
			return ;
		}
		var count = 0 ;
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			if(!Ext.isEmpty(data.idx) && back==data.status){
				count++;
			}
		}
		Ext.getCmp("checkQty").setValue(count+"");
		Ext.getCmp("unCheckQty").setValue((record.length-count)+"");
	};
	
	// 回段登记
	PartsOutSourcingBackNew.saveFun = function(){
        //表单验证是否通过
        var form = PartsOutSourcingBackNew.form.getForm(); 
        if (!form.isValid()) return;
        var length = PartsOutSourcingBackNew.store.getCount();
		var record = PartsOutSourcingBackNew.store.getRange(0,length);
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
				if(!Ext.isEmpty(data.partsBackNo) && !Ext.isEmpty(data_v.partsBackNo) && data.partsBackNo == data_v.partsBackNo){
					MyExt.Msg.alert("配件编号不能重复！");
					return ;
				}
			}
			for (var j = i+1; j < record.length; j++) {
				var data_c = {} ;
				data_c = record[j].data;
				if(!Ext.isEmpty(data.identificationCodeBack) && !Ext.isEmpty(data_c.identificationCodeBack) && data.identificationCodeBack == data_c.identificationCodeBack){
					MyExt.Msg.alert("配件识别码不能重复！");
					return ;
				}
			}
			  // 设置人员组织为当前登录人
		    data.takeOverOrgId = teamOrgId ;
		    data.takeOverOrg = teamOrgName ;
		    data.takeOverOrgSeq = teamOrgSeq ;
		    data.takeOverEmpId = empId ;
		    data.takeOverEmp = empName ;
			data.rdpIdx = PartsOutSourcingBackNew.rdpIdx;
			data.backDate = new Date();
			data.takeOverType = takeOverType;
			// 只保存配件不为空的数据
			if(!Ext.isEmpty(data.partsBackNo) && !Ext.isEmpty(data.idx) && back != data.status){
				datas.push(data);
			}
		}
		if(datas == null || datas.length == 0){
			MyExt.Msg.alert("没有可保存的数据，请查看配件编号和识别码是否填写，或所选数据已经回段登记！");
			PartsOutSourcingBackNew.grid.store.load();
			return ;
		}
        var cfg = {
	        scope: this, url: ctx + '/partsOutsourcing!updatePartsOutsourcingForBackNew.action',
	      	jsonData: datas,
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
	            	alertSuccess();
	                PartsOutSourcingBackNew.grid.store.load();
	            }else {
	            	 PartsOutSourcingBackNew.grid.store.load();
                     alertFail(result.errMsg);
                    }
	         	 }
	     	 };
	    	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
   	 };
   		 
   // 定义form 
	PartsOutSourcingBackNew.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", 
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsOutSourcingBackNew.labelWidth,
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
	    	 	 columnWidth: 0.25
	    	 }
	    },
	    items: [{
	        items: [{
	            items: [{
					xtype: 'compositefield', fieldLabel : '下车车型', combineErrors: false,
			        items: [{ 
			        	id:"trainType_comb",
			        	hidden:true,
			        	xtype: "Base_combo",
			        	fieldLabel: "下车车型",
						entity:'com.yunda.jx.jczl.undertakemanage.entity.UndertakeTrainType',
						fields:['trainTypeIDX','trainTypeShortName'],
						hiddenName: "unloadTrainTypeIdx",
						returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"trainTypeShortName"}],
						displayField: "trainTypeShortName", 
						valueField: "trainTypeIDX",
						width:100,
						pageSize: 20, 
						minListWidth: 200, 
						queryHql: 'from UndertakeTrainType where recordStatus=0',
						editable:false,
						readOnly: true,
						allowBlank:false
                    },{
                     	id:"PartsUnloadRegister_unloadTrainType",xtype:"hidden", name:"unloadTrainType"
                    },{
	               		xtype: 'button',
	               		text: '在修机车',
	              		width: 45,
	              		handler: function(){
	               	    	jx.jxgc.TrainTypeTreeSelect.createWin();
	               			jx.jxgc.TrainTypeTreeSelect.win.show();
	           	   		}
	           		}]
				}]
	        },{
	            items: [{ 
						id:"trainNo_comb",
						hidden:true,
						xtype: "TrainNo_combo",
						fieldLabel: "下车车号",
					  	hiddenName: "unloadTrainNo",
					  	displayField: "trainNo",
					  	valueField: "trainNo",
					  	pageSize: 20,
					  	minListWidth: 200, 
					 	editable:true,
					 	readOnly: true
				 }]
	        },{
	            items: [{
	        			id:"rc_comb",
	        			hidden:true,
	        			xtype: "Base_combo",
	        			business: 'trainRC',
						entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
						fields:['xcID','xcName'],
	        			fieldLabel: "下车修程",
	        			hiddenName: "unloadRepairClassIdx", 
	        			returnField: [{widgetId:"PartsUnloadRegister_unloadRepairClass",propertyName:"xcName"}],
	        			displayField: "xcName",
	        			valueField: "xcID",
	        			pageSize: 20, minListWidth: 200,
	        			queryHql: 'from UndertakeRc',
	        			readOnly: true
	        		},{
	        			id:"PartsUnloadRegister_unloadRepairClass",xtype:"hidden", name:"unloadRepairClass"
	        		}]
	        },{
	            items: [{
		    			id:"rt_comb",
		    			hidden:true,
		    			xtype: "Base_combo",
		    			fieldLabel: "下车修次",
		    			hiddenName: "unloadRepairTimeIdx", 
		    			returnField: [{widgetId:"PartsUnloadRegister_unloadRepairTime",propertyName:"repairtimeName"}],
		    			displayField: "repairtimeName",
		    			valueField: "repairtimeIDX",
		    			pageSize: 0,
		    			minListWidth: 200,
		    			fields: ["repairtimeIDX","repairtimeName"],
	    				business: 'rcRt',
	    				readOnly: true
		    		},{
		    			id:"PartsUnloadRegister_unloadRepairTime",xtype:"hidden", name:"unloadRepairTime"
		    		}]
	        	},{
	            items: [{ 
						id:"display_field",
						xtype: "displayfield",
						//fieldLabel: "已选车",
						style: {
				            "font-weight":"bold"
				        }
				 }]
	        },{
	        			id:"PartsUnloadRegister_rdpIdx",xtype:"hidden", name:"rdpIdx"
	        }]
	    }]
	});
	jx.jxgc.TrainTypeTreeSelect.returnFn = function(node, e){    //选择确定后触发函数，用于处理返回记录
			Ext.getCmp("trainType_comb").setDisplayValue(node.attributes["trainTypeIDX"],node.attributes["trainTypeShortName"]);
			Ext.getCmp("PartsUnloadRegister_unloadTrainType").setValue(node.attributes["trainTypeShortName"]);
			Ext.getCmp("trainNo_comb").setDisplayValue(node.attributes["trainNo"],node.attributes["trainNo"]);
			Ext.getCmp("rc_comb").setDisplayValue(node.attributes["repairClassIDX"],node.attributes["repairClassName"]);
			Ext.getCmp("PartsUnloadRegister_unloadRepairClass").setValue(node.attributes["repairClassName"]);
			Ext.getCmp("rt_comb").setDisplayValue(node.attributes["repairTimeIDX"],node.attributes["repairTimeName"]);
			Ext.getCmp("PartsUnloadRegister_unloadRepairTime").setValue(node.attributes["repairTimeName"]);
			Ext.getCmp("PartsUnloadRegister_rdpIdx").setValue(node.id);//机车兑现单id
			//重新加载车号下拉数据
	        var trainNo_comb = Ext.getCmp("trainNo_comb");   
	        trainNo_comb.queryParams = {"trainTypeIDX":node.attributes["trainTypeIDX"]};
	        trainNo_comb.cascadeStore();
	        //重新加载修程下拉数据
	        var rc_comb = Ext.getCmp("rc_comb");
	        rc_comb.queryParams = {"TrainTypeIdx":node.attributes["trainTypeIDX"]};
	        rc_comb.cascadeStore();
			//重新加载修次数据
	    	var rt_comb = Ext.getCmp("rt_comb");
	     	rt_comb.queryParams = {"rcIDX":node.attributes["repairClassIDX"]};
	        rt_comb.cascadeStore(); 
	        PartsOutSourcingBackNew.unloadTrainTypeIdx = node.attributes["trainTypeIDX"] ;
	        PartsOutSourcingBackNew.unloadTrainNo = node.attributes["trainNo"] ;
	        PartsOutSourcingBackNew.unloadRepairClassIdx = node.attributes["repairClassIDX"] ;
	        
	        // 设置显示值
	        Ext.getCmp("display_field").setValue(node.attributes["trainTypeShortName"]+""+node.attributes["trainNo"]+"  "+node.attributes["repairClassName"]+""+node.attributes["repairTimeName"]);
	        PartsOutSourcingBackNew.rdpIdx = node.id ;
	        PartsOutSourcingBackNew.store.load();	        
	    };


	
	PartsOutSourcingBackNew.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	PartsOutSourcingBackNew.store = new Ext.data.JsonStore({
	    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:false, pruneModifiedRecords: true,        
	    url: ctx + "/partsOutsourcing!findpageQuery.action",
	    fields: [ "idx","partsTypeIdx","partsName","partsOutNo","specificationModel","identificationCode","partsBackNo","identificationCodeBack",'backDate','backPartsStatus','backPartsStatusName'
	    	,"partsAccountIDX","outsourcingReasion","outsourcingFactory","outsourcingFactoryId","outsourcingDate","outEmp","repairContent","takeOverOrg","takeOverOrgId","status"
	    ]
	});
	// 选择模式，勾选框可多选
	PartsOutSourcingBackNew.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
   // grid
	PartsOutSourcingBackNew.grid = new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: true, stripeRows: true, 
	    loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	    selModel: PartsOutSourcingBackNew.sm,
	    colModel: new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(), PartsOutSourcingBackNew.sm,
	        {	header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	        },{
				header:'配件名称', dataIndex:'partsName',width:100,renderer: function(v, metaData, record) {
 				if (!Ext.isEmpty(record.get('idx'))&& back == record.get('status')) {
	 				return "<span style='color:green'>" + v + "</span>";
 				}else{
 					return v ;
 				}}
					
	        },{
				header:'规格型号', dataIndex:'specificationModel',width:100
			},{
				header:'委外配件编号', dataIndex:'partsOutNo',width:150
			},{
	        	dataIndex:'identificationCode',width:150, header:'委外配件识别码'
    		},{
				header:'委修厂家', dataIndex:'outsourcingFactory', width:100
			},{
				header:'委外日期', dataIndex:'outsourcingDate', width:80
			},{
				header:'返回配件编号 *',cls:'checkQty_css', dataIndex:'partsBackNo',width:150, editor: new  Ext.form.TextField({
		    		maxLength:25
	         	}),
     	   	   renderer: function(v,m){
     	   	   	 m.css = 'x-grid-col';
     	   	   	 return v;
     	   	   }	
	        },{
	        	header:'返回配件识别码 *',dataIndex:'identificationCodeBack', width:150, editor: new  Ext.form.TextField({
		    		maxLength:25
	         	}),
     	   	   renderer: function(v,m){
     	   	   	 m.css = 'x-grid-col';
     	   	   	 return v;
     	   	   }    	   	   
			},{
				header:'回段日期', dataIndex:'backDate', width:80,xtype: "datecolumn",format: "Y-m-d"    //, editor:{xtype:'my97date', format: "Y-m-d"}
			},{
				header:'配件状态', dataIndex:'backPartsStatus',  editor:{id:"backPartsStatusId"}
			},{
				header:'配件状态', dataIndex:'backPartsStatusName',  width:80,editor:{
						name:"backPartsStatusName", 
						xtype: 'combo',	
				        store:new Ext.data.ArrayStore({
						    fields: ['K','V'],
							data : [['0103','良好'],['0101','待修'],['0104','待校验']]
						}),
						returnField:[{widgetId:"backPartsStatusId",propertyName:'K'}],
						valueField:'V',
						displayField:'V',
						triggerAction:'all',
						value: "良好",
						mode:'local',		
						listeners: {
						  'select':function(me,record,idx){
						  	 var record_v = PartsOutSourcingBackNew.store.getAt(PartsOutSourcingBackNew.rowIndex);
	 				 		 record_v.data.backPartsStatus = record.data.K;
						   }
						}
				},
     	   	   renderer: function(v,m){
     	   	   	 m.css = 'x-grid-col';
     	   	   	 return v;
     	   	   }    	
         	},{
				header:'接收班组 *', dataIndex:'takeOverOrg', allowbland:false, width:80, hidden:true,editor:{ 
					id:"whName_comb", fieldLabel:"接收库房",
		        	hiddenName:"takeOverOrg",width:150,
					xtype:"Base_combo",allowBlank:false,
					entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName",valueField:"wareHouseName",
					returnField:[{widgetId:"takeOverOrgId",propertyName:"idx"}]   }
		
			},{
				header:'委外检修原因', dataIndex:'outsourcingReasion',width:200
		
			},{
				header:'委修厂家主键', dataIndex:'outsourcingFactoryId',hidden:true
			
			},{
				header:'经办人', dataIndex:'outEmp',hidden:true,  width:70
			},{
				header:'修理内容', dataIndex:'repairContent',width:300				
			},{
				header:'接收库房', dataIndex:'takeOverOrgId',hidden:true, editor:{ id:"takeOverOrgId",xtype:'hidden'}
  	        },{
				header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true
			},{
				header:'规格型号主键', dataIndex:'partsTypeIdx', hidden:true
			},{
				header:'回段状态', dataIndex:'status', hidden:true
			}]),
		listeners: {		
			cellclick:function(grid, rowIndex, columnIndex, e) {
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex); 
				if(fieldName == "backPartsStatusName"){
					PartsOutSourcingBackNew.rowIndex = rowIndex;
				}
			},
			afteredit:function(e){
//				var d =Ext.getCmp("backPartsStatusId").getValue();
//				if(e.field == "backPartsStatusName"){
//					e.record.data.backPartsStatus =  Ext.getCmp("backPartsStatusId").getValue();
//				}
			}				
		},					
	      store: PartsOutSourcingBackNew.store,
          tbar:[{text:'复制委外信息', iconCls:'addIcon', handler:function(){
		        var recordselect = PartsOutSourcingBackNew.grid.selModel.getSelections();
		        if(recordselect.length == 0 ){
		        	MyExt.Msg.alert("尚未选择一条记录！");
		            return;
		        }		
			    for (var i = 0; i < recordselect.length; i++){			
					 recordselect[i].set("partsBackNo",recordselect[i].data.partsOutNo);
					 recordselect[i].set("identificationCodeBack",recordselect[i].data.identificationCode);
				     recordselect[i].set("backPartsStatus",'0103');
				 	 recordselect[i].set("backPartsStatusName",'良好');	
					PartsOutSourcingBackNew.grid.getView().refresh();
			    } }
        },{
	    	text: '撤销', iconCls: 'deleteIcon', handler: function() {
		        var data = PartsOutSourcingBackNew.grid.selModel.getSelections();
		     	if(data.length<1){
		       	  MyExt.Msg.alert("尚未选择一条记录！");
		       	  return ;
		       }
		       var ids = new Array();
			   for(var i=0;i<data.length;i++){
			      if(back == data[i].get('status')){
			 		 ids.push(data[i].get('idx'));
			      }
			    }
			    if(ids.length == 0){
					MyExt.Msg.alert("没有要撤销的数据！");
					return ;
				}
			 	Ext.Msg.confirm('提示', "是否确认撤销？", function(btn){
					if (btn == 'yes') {
					 	Ext.Ajax.request({
					        url: ctx + "/partsOutsourcing!updateForCancelBackBatch.action",
					        params: {ids : ids},
					        success: function(response, options){
					            var result = Ext.util.JSON.decode(response.responseText);
					            if (result.errMsg == null) {
					                alertSuccess();
					                PartsOutSourcingBackNew.grid.store.reload(); 
					                
					            } else {
					                alertFail(result.errMsg);
					            }
					        },
					        failure: function(response, options){
					            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					        }
						});
					}
			 	});
		 	
	    	}
	    },{
	    	text: "刷新", iconCls: "refreshIcon", handler: function(){PartsOutSourcingBackNew.store.reload();}
        },'-','*配件名称颜色为<span style="color:green">绿色</span>表示已回段数据','->','已回段数量：',
    	{xtype:'displayfield',id:'checkQty',cls:'checkQty_css'},
        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','-','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
        '未回段数量：',
		{xtype:'displayfield',id:'unCheckQty',cls:'unCheckQty_css'},
        	'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;']
    
	 });
	

   // 页面加载和数据模糊查询
   PartsOutSourcingBackNew.store.on('beforeload', function() {		
		var whereList = [] ;	
	    whereList.push({propName:'rdpIdx', propValue: PartsOutSourcingBackNew.rdpIdx});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	PartsOutSourcingBackNew.store.on("load", function(){
		PartsOutSourcingBackNew.setPartsCounts();
	});
	

	//页面自适应布局
	var viewport = new Ext.Viewport({
		 layout:'border',frame:true,
		 items:[{
			region:"north",
			layout:"fit",
			height:60,
			split:true,
			maxSize:70,
			minSize:70,
			frame: true,bodyBorder: false, 
			items:[PartsOutSourcingBackNew.form]
		},{
			title:"委外明细",region : 'center', layout : 'border', bodyBorder: false,xtype:'fieldset',
			margins:'5 10 0 10', 
			items:[{
					layout: "fit",
					region : 'center',
			        bodyBorder: false,
			        autoScroll : true,
			        id: "nGridId",
			        items : [ PartsOutSourcingBackNew.grid ]
				},{
					layout: "fit",
					region : 'south',
			        bodyBorder: false,
			        height:40,
			        buttonAlign:"center",
				    buttons:[{text: "回段登记", iconCls:'saveIcon',  handler: function(){PartsOutSourcingBackNew.saveFun()}}] 
				}]
		}]
	});
});