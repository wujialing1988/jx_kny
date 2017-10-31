/**
 * 良好配件登记 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	//配件选择窗口,数据容器
	Ext.namespace("WellPartsRegister");
	WellPartsRegister.fieldWidth = 200;
	WellPartsRegister.labelWidth = 100;
	WellPartsRegister.rowIndex = "";
	//规格型号选择控件赋值函数
	WellPartsRegister.callReturnFn=function(node,e){
	  //alert(node);
	  NewPartsWH.form.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	  NewPartsWH.form.find("name","partsName")[0].setValue(node.attributes["partsName"]);
	  NewPartsWH.form.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	WellPartsRegister.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	WellPartsRegister.store = new Ext.data.JsonStore({
	    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true, pruneModifiedRecords: true,
	    url: ctx + "/newPartsWHDetail!pageList.action",
	    fields: [ "idx","partsTypeIDX","partsNo" ]
	});
	//材料规格型号，选择模式，勾选框可多选
	WellPartsRegister.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//材料规格型号，分页工具
	WellPartsRegister.pagingToolbar = Ext.yunda.createPagingToolbar({store: WellPartsRegister.store});
	WellPartsRegister.grid = new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: true, stripeRows: true, selModel: WellPartsRegister.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	    colModel: new Ext.grid.ColumnModel([
	         new Ext.grid.RowNumberer(),
	        { sortable:false, header:'idx', dataIndex:'idx',hidden: true},
	        { sortable:false, header: '配件名称',  dataIndex: 'partsName',width:100},
	    	{ sortable:false, header: "规格型号",  dataIndex: "specificationModel",width:100},	        
	    	{ sortable:false, header:'配件规格型号id', dataIndex:'partsTypeIdx',hidden: true},
	    	{ sortable:false, header: "配件编号",  dataIndex: "partsNo",width:100,
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	    	
		    	editor: new  Ext.form.TextField({
		    		id:"aaaa",
		    		maxLength:25,
	                allowBlank: false
	         	})},
			{ sortable:false, header: "配件识别码",  dataIndex: "identificationCode",width:100,
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},			
		    	editor: new  Ext.form.TextField({
		    		maxLength:25
	         	})},
         	{ sortable:false, header: "存放库位",  dataIndex: "locationName",width:100,
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},         	
	    	    editor: { id:"location_comb", editable:true,selectOnFocus:false,forceSelection:false,typeAhead:false,
	    	    		 xtype: 'Base_combo',hiddenName: "locationName",fieldLabel:"存放库位",
		 				 entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",displayField:"locationName",valueField:"locationName",fields:["idx","locationName"],			 
		 				 width: WellPartsRegister.fieldWidth,allowBlank: false,
		 				 listeners:{
		 				 	'collapse' : function(record, index){
		 				 		var record_v = WellPartsRegister.store.getAt(WellPartsRegister.rowIndex);
	 				 			var value = document.getElementById("location_comb").value;
	 				 			record_v.data.locationName = value;
								WellPartsRegister.grid.getView().refresh();
		 				 	}
		 				 }}
		 				 
		 		},
	    	
	    	{ sortable:false, header: "出厂日期",  dataIndex: "factoryDate",/*xtype: "datecolumn",format: "Y-m-d",*/
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	var result = "" ;
     	   	   	 	if(v){
     	   	   	 		result = new Date(v).format("Y-m-d");
     	   	   	 	}
     	   	   	 	return result;
     	   	   	},	    		
	    		editor:{xtype:'my97date', format: "Y-m-d"}},
	    	{ sortable:false, header: "生产厂家",  dataIndex: "madeFactoryIdx",hidden:true,
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	    		
	    		editor:{id:"madeFactoryIdx_id"}
	    		},
	    	{ sortable:false, header: "生产厂家",  dataIndex: "madeFactoryName",width:100,
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	    	    
	    	    editor: { id:"factory_comb", editable:true,forceSelection:false,selectOnFocus:false,typeAhead:false,
	    	    		 xtype: 'Base_combo',hiddenName: "madeFactoryName",fieldLabel:"生产厂家",
		 				 entity:"com.yunda.jx.pjwz.partsBase.madefactory.entity.PartsMadeFactory",displayField:"madeFactoryName",valueField:"madeFactoryName",fields:["id","madeFactoryName"],			 
		 				 returnField:[{widgetId:"madeFactoryIdx_id",propertyName:"id"}], 
		 				 width: WellPartsRegister.fieldWidth,allowBlank: false,
		 				 listeners:{
		 				 	'collapse' : function(record, index){
		 				 		var record_v = WellPartsRegister.store.getAt(WellPartsRegister.rowIndex);
	 				 			var value = document.getElementById("factory_comb").value;
	 				 			record_v.data.madeFactoryName = value;
	 				 			record_v.data.madeFactoryIdx = Ext.getCmp("madeFactoryIdx_id").getValue();
								WellPartsRegister.grid.getView().refresh();
		 				 	}
		 				 }}
		 				 
		 		},
	    	{ sortable:false, header: "详细配置",  dataIndex: "configDetail",
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	    		
	    		editor: new  Ext.form.TextField({
		    		maxLength:100
	         	})
	    		},
	    	{
	        	sortable:false,dataIndex:'source',header:'单据类型',hidden:true,
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	        	
	        	editor:{
	        			id:"source",
						xtype:'combo',
			            fieldLabel: '单据类型',
			            store:new Ext.data.SimpleStore({
			               fields: ['value', 'text'],
                            data : [
                            	['新购', '新购'],
                            	['调入', '调入']
                            ] 
			            }),
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            allowBlank: false,
			            mode:'local'
				}
	        },
	    	{
	        	sortable:false,dataIndex:'partsStatusName',header:'配件状态',
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	        	
	        	editor:{
	        			id:"partsStatusName",
						xtype:'combo',
			            fieldLabel: '配件状态',
			            store:new Ext.data.SimpleStore({
			               fields: ['value', 'text'],
                            data : [
                            	['0103', '良好'],
                            	['0104', '待校验']
                            ] 
			            }),
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            allowBlank: false,
			            editable:false,
			            mode:'local',
			            listeners:{
			            	'select' : function(combo,record, index){
			            		var record_v = WellPartsRegister.store.getAt(WellPartsRegister.rowIndex);
			            		record_v.data.partsStatus = record.data.value;
	 				 			record_v.data.partsStatusName = record.data.text;
								WellPartsRegister.grid.getView().refresh();
			            	}
			            }
				}
	        },	        
        	{ sortable:false, header: "配件状态名称",  dataIndex: "partsStatus",hidden:true,
    			editor:{id:"partsStatus_id"}
    		},
	    	{ header:'partsAccountIDX', dataIndex:'partsAccountIDX',hidden: true}
	    ]),
		listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
			    var whIdx = Ext.getCmp("NewPartsWH_whIdx").getValue();
			    var location_comb =  Ext.getCmp("location_comb");
                location_comb.reset();
//                location_comb.clearValue();
                location_comb.getStore().removeAll();
                location_comb.queryParams = {"wareHouseIDX":whIdx};
                location_comb.cascadeStore();
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex); 
				if(fieldName == "locationName" || fieldName == "madeFactoryName" || fieldName == "partsStatusName"){
					WellPartsRegister.rowIndex = rowIndex;
				}
			}
		},
	    store: WellPartsRegister.store,
	    tbar: ['增加数量：',
	    	 	{xtype:'textfield',id:"addnumber", name:"addnumber",maxLength:2 ,vtype: "positiveInt", allowBlank:false,width:WellPartsRegister.fieldWidth},
	    	 	{ text:'添加明细', iconCls:'addIcon', handler:function(){
				//表单验证是否通过
		        var whForm = NewPartsWH.form.getForm(); 
		        if (!whForm.isValid()) return;
		        var whDetailForm = NewPartsWH.form.getForm(); 
		        if (!whDetailForm.isValid()) return;
				var addnumber = Ext.getCmp("addnumber").getValue();
				if(addnumber==""){
				   MyExt.Msg.alert("请输入添加数量");
				   return ;
				}
				if(addnumber>30){
				   MyExt.Msg.alert("数量不能超过30，请重新输入");
				   return ;
				}
				var specificationModel = Ext.getCmp("PartsTypeTreeSelect_select").getValue();
				var partsName = Ext.getCmp("partsName").getValue();
				var partsTypeIDX = Ext.getCmp("partsTypeIDX").getValue();
				for(var i=0;i<addnumber;i++){
					var defaultData = {specificationModel:specificationModel,partsName:partsName,partsTypeIdx:partsTypeIDX,source:SOURCE_NEW,partsStatus:"0103",partsStatusName:"良好"};
					var initData = Ext.apply({}, defaultData); 
					var record = new Ext.data.Record(defaultData);
			        WellPartsRegister.grid.store.insert(0, record); 
			        WellPartsRegister.grid.getView().refresh(); 
			        WellPartsRegister.grid.getSelectionModel().selectRow(0);
				}
				
	        }},
	        	{ text:'删除明细', iconCls:'deleteIcon', handler:function(){
		        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	//	        if(!this.beforeDeleteFn()) return;
		        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
		        var data = WellPartsRegister.grid.selModel.getSelections();
		        if(data.length == 0 ){
		        	MyExt.Msg.alert("尚未选择一条记录！");
		            return;
		        }
		        var storeAt = WellPartsRegister.grid.store.indexOf(data[0]);
		        var records = WellPartsRegister.store.getModifiedRecords();
		        var count = records.length; 
		        var j = storeAt + 1;
		        if(count-1 == storeAt){
		        	j = storeAt-1;
		        }
			    WellPartsRegister.grid.getSelectionModel().selectRow(j);
			    for (var i = 0; i < data.length; i++){
			        WellPartsRegister.grid.store.remove(data[i]);
			    }
        }},{
	    	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
	    }],
	    bbar:[]//WellPartsRegister.pagingToolbar
	});
	WellPartsRegister.store.on("beforeload", function(){
		var beforeloadParam = {newPartsWhIDX: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	//登帐并新增
	WellPartsRegister.saveFun = function(){
		var form = NewPartsWH.form.getForm();
	    if (!form.isValid()) return;
	    var whData = form.getValues();
		var record = WellPartsRegister.store.getModifiedRecords();
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("请添加明细！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			if(data.partsNo == undefined || data.partsNo == ''){
				MyExt.Msg.alert("配件编号不能为空！");
				return ;
			}
			for (var j = i+1; j < record.length; j++) {
				var data_v = {} ;
				data_v = record[j].data;
				if(data.partsNo == data_v.partsNo){
					MyExt.Msg.alert("配件编号不能重复！");
					return ;
				}
			}
//			if(data.identificationCode == undefined || data.identificationCode == ''){
//				MyExt.Msg.alert("配件识别码不能为空！");
//				return ;
//			}
			for (var j = i+1; j < record.length; j++) {
				var data_c = {} ;
				data_c = record[j].data;
				if(data.identificationCode != undefined && data_c.identificationCode != undefined && data.identificationCode == data_c.identificationCode){
					MyExt.Msg.alert("配件识别码不能重复！");
					return ;
				}
			}
			data.acceptDeptId = whData.whIdx;
			data.acceptDept = whData.whName;
			data.takeOverEmpId = whData.takeOverEmpId;
			data.takeOverEmp = whData.takeOverEmp;
			data.acceptDeptType = ACCEPT_DEPT_TYPE_WH;
			datas.push(data);			
		}
		
		WellPartsRegister.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/wellPartsRegister!saveBatchWellParts.action',
            params : {registerDatas : Ext.util.JSON.encode(datas)},
            success: function(response, options){
              	WellPartsRegister.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    WellPartsRegister.grid.store.removeAll();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                WellPartsRegister.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	Ext.namespace('NewPartsWH');                       //定义命名空间
	NewPartsWH.fieldWidth = 150;
	NewPartsWH.labelWidth = 90;
	NewPartsWH.form = new Ext.form.FormPanel({
		    baseCls: "x-plain",
		    align: "center",
		    defaultType: "textfield",
		    defaults:{anchor: "98%"},
		    layout: "form",
		    border: false,
		    style: "padding:10px",
		    labelWidth: NewPartsWH.labelWidth,
		    defaults:{
		    	 xtype: "panel",border: false,baseCls: "x-plain",layout: "column",align: "center", 
		    	 defaults:{
		    	 	baseCls:"x-plain", 
		    	 	align:"center", 
		    	 	layout:"form", 
		    	 	defaultType:"textfield", 
		    	 	width:NewPartsWH.fieldWidth,
		            columnWidth: 0.25
		    	 }
		    },
		    items: [{
		        items: [{
		            items:[{
				        	id:"whName_comb", fieldLabel:"接收库房",
				        	hiddenName:"whName",width:150,
							xtype:"Base_combo",allowBlank: false,
							entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"NewPartsWH_whIdx",propertyName:"idx"}]        
				        },{
				        	id:"NewPartsWH_whIdx",xtype:"hidden", name:"whIdx"}]
		        },{
		            items: [
		            	{ id:"takeOver_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "接收人",hiddenName:'takeOverEmpId',
						returnField:[{widgetId:"NewPartsWH_takeOverEmp",propertyName:"empname"}]},
						{id:"NewPartsWH_takeOverEmp",xtype:"hidden", name:"takeOverEmp"}
		            ]
		        },{
		            items: [
		            	{ xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',allowBlank:false,
						  	hiddenName: 'specificationModel', editable:false,
						  	returnFn: WellPartsRegister.callReturnFn}
		            ]
		        },{
		            items: [
		            	{ id:"partsName", name:"partsName", fieldLabel:"配件名称",maxLength:100 ,disabled:true, allowBlank:false},
						{ id:"partsTypeIDX", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true }]
		        }]
		    }]
		});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		 layout: "border",
		 items:[{
						region:"north",
						layout:"fit",
						height:70,
						split:true,
						maxSize:70,
						minSize:70,
						frame: true,bodyBorder: false, 
						items:[NewPartsWH.form]
					},{
						title:"良好配件登记明细",region : 'center', layout : 'border', bodyBorder: false, 
						xtype:'fieldset',margins:'5 10 5 10',
						items:[{
									layout: "fit",
									region : 'center',
							        bodyBorder: false,
							        autoScroll : true,
							        id: "nGridId",
							        items : [ WellPartsRegister.grid ]
							}]
					},{
						region : 'south', layout:"fit",baseCls:'x-plain',
						height:40, bodyBorder: false, buttonAlign:"center",
						buttons:[{text: "登帐并新增",  handler: function(){WellPartsRegister.saveFun()}}]
					}
		]
	});
	NewPartsWH.init = function(){
		Ext.getCmp("takeOver_select").setDisplayValue(empId,empName);
		Ext.getCmp("NewPartsWH_takeOverEmp").setValue(empName);
		var whName_comb =  Ext.getCmp("whName_comb");
		whName_comb.getStore().on("load",function(store, records){ 
			if(records.length > 0){
		    	whName_comb.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
		    	Ext.getCmp("NewPartsWH_whIdx").setValue(records[0].get('idx'));
			}
		});
	};
	NewPartsWH.init();
});
