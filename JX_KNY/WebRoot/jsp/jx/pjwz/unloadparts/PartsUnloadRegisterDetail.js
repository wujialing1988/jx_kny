/**
 * 下车配件登记明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	//配件选择窗口,数据容器
	Ext.namespace("PartsUnloadRegisterDetail");
	PartsUnloadRegisterDetail.fieldWidth = 150;
	PartsUnloadRegisterDetail.labelWidth = 100;
	PartsUnloadRegisterDetail.rowIndex = "";
	
	//规格型号选择控件赋值函数
	PartsUnloadRegisterDetail.callReturnFn=function(node,e){
	  PartsUnloadRegisterDetail.form.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	  PartsUnloadRegisterDetail.form.find("name","partsName")[0].setValue(node.attributes["partsName"]);
	  PartsUnloadRegisterDetail.form.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	  if(node.attributes["unit"]!=null){
	 	 Ext.getDom("spUnit").innerHTML=node.attributes["unit"];
	  }else{
	  	 Ext.getDom("spUnit").innerHTML="";
	  }
	  Ext.Ajax.request({
	        url: ctx + "/trainTypeToParts!findListByTrainTypeIDX.action",
	        params: {trainTypeIDX : PartsUnloadRegister.unloadTrainTypeIdx ,trainNo : PartsUnloadRegister.unloadTrainNo ,repairClassId : PartsUnloadRegister.unloadRepairClassIdx ,partsTypeIDX : node.attributes["id"] },
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            Ext.getCmp("checkQty").setValue("");
	            Ext.getCmp("unCheckQty").setValue("");
	            if('undefined' != result.root[0] && null != result.root[0]){
	            	Ext.getCmp("checkQty").setValue(result.root[0].checkQty);
	            	if(result.root[0].unCheckQty > 0) Ext.getCmp("unCheckQty").setValue(result.root[0].unCheckQty);
	            	else Ext.getCmp("unCheckQty").setValue("0");
	            	
	            }
	            
	        }
		});
	}
	//添加明细
	PartsUnloadRegisterDetail.insertRow = function(addnumber){
		var specificationModel = Ext.getCmp("PartsTypeTreeSelect_select").getValue();
		var partsName = Ext.getCmp("partsName").getValue();
		var unit = Ext.getDom("spUnit").innerHTML;
		var partsTypeIDX = Ext.getCmp("partsTypeIDX").getValue();
		//PartsUnloadRegisterDetail.grid.store.removeAll();
		for(var i=0;i<addnumber;i++){
			var defaultData = {specificationModel:specificationModel,partsName:partsName,partsTypeIDX:partsTypeIDX};
			var initData = Ext.apply({}, defaultData); 
			var record = new Ext.data.Record(defaultData);
	        PartsUnloadRegisterDetail.grid.store.insert(0, record); 
	        PartsUnloadRegisterDetail.grid.getView().refresh(); 
	        PartsUnloadRegisterDetail.grid.getSelectionModel().selectRow(0);
		}
	}
	PartsUnloadRegisterDetail.form = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsUnloadRegisterDetail.labelWidth,
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
	            items:[{
                     xtype:"PartsTypeTreeSelect",
                     fieldLabel: '配件规格型号',
                     id:'PartsTypeTreeSelect_select',
                     allowBlank:false,
				  	 hiddenName: 'specificationModel', 
				  	 editable:false,
				  	 onTriggerClick: function() {
					        if(this.disabled)  return;
					        //选择配件规格型号前先选车型
	                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择下车车型！");
	    						return false;
	    					}
					        jx.pjwz.PartsTypeSelect.returnFn = this.returnFn;
					        jx.pjwz.PartsTypeSelect.init();
					        if(jx.pjwz.PartsTypeSelect.win == null)  
					        jx.pjwz.PartsTypeSelect.createWin();
					        jx.pjwz.PartsTypeSelect.win.show();
					    },
					  	returnFn: PartsUnloadRegisterDetail.callReturnFn
	                  }]
	        },{
	            items: [{ 
	            		id:"partsName", name:"partsName", fieldLabel:"配件名称",maxLength:100 ,disabled:true, allowBlank:false
	            	},{ 
	            		id:"partsTypeIDX", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true 
	            	}]
	        },{
	            items: [{ 
	            		id:"checkQty", name:"checkQty", fieldLabel:"已登记数量",maxLength:100 ,disabled:true, width:70 
	            	}]
	        },{
	            items: [{ 
	            		id:"unCheckQty", name:"unCheckQty", fieldLabel:"未登记数量",maxLength:100 ,disabled:true, width:70 
	            	}]
	         	}]
	    	}]
	});
	
	PartsUnloadRegisterDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	PartsUnloadRegisterDetail.store = new Ext.data.JsonStore({
	    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:false, pruneModifiedRecords: true,
	    url: ctx + "/partsUnloadRegister!pageList.action",
	    fields: [ "idx","partsTypeIDX","partsNo" ]
	});
	//材料规格型号，选择模式，勾选框可多选
	PartsUnloadRegisterDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//材料规格型号，分页工具
	PartsUnloadRegisterDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsUnloadRegisterDetail.store});
	PartsUnloadRegisterDetail.grid = new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: true, stripeRows: true, selModel: PartsUnloadRegisterDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	    colModel: new Ext.grid.ColumnModel([
	        { sortable:false, header:'idx', dataIndex:'idx',hidden: true},
	    	{ sortable:false, header:'配件规格型号id', dataIndex:'partsTypeIDX',hidden: true},
	    	{ sortable:false, header: "配件编号",  dataIndex: "partsNo",width:100,
		    	editor: new  Ext.form.TextField({
		    		id:"aaaa",
		    		maxLength:25,
	                allowBlank: false
	         	})},
	         { sortable:false, header: "配件识别码",  dataIndex: "identificationCode",width:100,
		    	editor: new  Ext.form.TextField({
		    		maxLength:25
	         	})},
	        { sortable:false, header: "走行公里数",  dataIndex: "runingKM",
	    		editor:{maxLength:8,vtype : "positiveInt"}
				},
         	{ sortable:false, header: "存放位置",  dataIndex: "location",maxLength:100,
	    		editor:{xtype: 'Base_combo',hiddenName: "location",fieldLabel:"存放位置",
		 				 entity:"com.yunda.jxpz.phrasedic.entity.PhraseDicItem",
		 				 queryParams: {'dictTypeId':'locationcode'}, 
		 				 displayField:"dictItemDesc",valueField:"dictItemDesc",fields:["dictItemDesc"],			 
		 				 width: PartsUnloadRegisterDetail.fieldWidth,maxLength:100}
				},
	    	
	    	{ sortable:false, header: "下车原因",  dataIndex: "unloadReason",
	    		editor:{id:"unloadReason_v",xtype: 'Base_combo',hiddenName: "unloadReason",fieldLabel:"下车原因",
		 				 entity:"com.yunda.jxpz.phrasedic.entity.PhraseDicItem",
		 				 queryParams: {'dictTypeId':'reasoncode'}, 
		 				 displayField:"dictItemDesc",valueField:"dictItemDesc",fields:["dictItemDesc"],			 
		 				 width: PartsUnloadRegisterDetail.fieldWidth},maxLength:50},
	    	{ sortable:false, header: "下车位置",  dataIndex: "unloadPlace",
	    		editor:{maxLength:50}
				},
	    	{ sortable:false, header: "出厂日期",  dataIndex: "factoryDate",xtype: "datecolumn",format: "Y-m-d",editor:{xtype:'my97date', format: "Y-m-d"}},
	    	{ sortable:false, header: "生产厂家",  dataIndex: "madeFactoryIdx",hidden:false,
	    		editor:{id:"madeFactoryIdx_id"}
	    		},
	    	{ sortable:false, header: "生产厂家",  dataIndex: "madeFactoryName",width:100,
	    	    editor: { id:"factory_comb", editable:true,forceSelection:false,selectOnFocus:false,typeAhead:false,
	    	    		 xtype: 'Base_combo',hiddenName: "madeFactoryName",fieldLabel:"生产厂家",
		 				 entity:"com.yunda.jx.pjwz.partsBase.madefactory.entity.PartsMadeFactory",
		 				 displayField:"madeFactoryName",valueField:"madeFactoryName",fields:["id","madeFactoryName"],			 
		 				 returnField:[{widgetId:"madeFactoryIdx_id",propertyName:"id"}], 
		 				 width: PartsUnloadRegisterDetail.fieldWidth,allowBlank: false,
		 				 listeners:{
		 				 	'collapse' : function(record, index){
		 				 		var record_v = PartsUnloadRegisterDetail.store.getAt(PartsUnloadRegisterDetail.rowIndex);
	 				 			var value = document.getElementById("factory_comb").value;
	 				 			record_v.data.madeFactoryName = value;
	 				 			record_v.data.madeFactoryIdx = Ext.getCmp("madeFactoryIdx_id").getValue();
								PartsUnloadRegisterDetail.grid.getView().refresh();
		 				 	}
		 				 }}
		 				 
		 		},
	    	{ sortable:false, header: "详细配置",  dataIndex: "configDetail",maxLength:200,
	    		editor: new  Ext.form.TextField({
		    		maxLength:200
	         	})
	    		},
	    	{ header:'partsAccountIDX', dataIndex:'partsAccountIDX',hidden: true},
	    	{ header:'partsTypeIDX', dataIndex:'partsTypeIDX',hidden: true},
	        { sortable:false, header: '配件名称',  dataIndex: 'partsName',width:100},
	    	{ sortable:false, header: "规格型号",  dataIndex: "specificationModel",width:100}
	    ]),
		listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex); 
				if(fieldName == "madeFactoryName"){
					PartsUnloadRegisterDetail.rowIndex = rowIndex;
				}
				if (fieldName == "extendNo") {
					var partsExtendNo_Select = grid.colModel.getCellEditor(columnIndex, rowIndex).field;

					// 修改扩展编号控件的partsTypeIDX字段数据来源于对于数据的表格行的“partsTypeIDX”字段
					partsExtendNo_Select.partsTypeIDX = grid.store.getAt(rowIndex).get("partsTypeIDX");
					var extendNoJson = grid.store.getAt(rowIndex).get("extendNoJson");
					if (!Ext.isEmpty(extendNoJson)) {
						partsExtendNo_Select.fields = Ext.util.JSON.decode(extendNoJson);
					} else {
						partsExtendNo_Select.fields = null;
					}
				}
				
			}
		},
	    store: PartsUnloadRegisterDetail.store,
	    tbar: ['增加数量：',
	    	 {xtype:'textfield',id:"addnumber", name:"addnumber",maxLength:2 ,vtype: "positiveInt", allowBlank:false,width:PartsUnloadRegisterDetail.fieldWidth},
	    	 '&nbsp;&nbsp;<span id="spUnit"><span>&nbsp;&nbsp;',
	    	{ text:'添加明细', iconCls:'addIcon', handler:function(){
	    		
				//表单验证是否通过
		        var whForm = PartsUnloadRegister.form.getForm(); 
		        if (!whForm.isValid()) return;
		        var whDetailForm = PartsUnloadRegisterDetail.form.getForm(); 
		        if (!whDetailForm.isValid()) return;
				var addnumber = Ext.getCmp("addnumber").getValue();
				var unCheckQty = Ext.getCmp("unCheckQty").getValue();
				var count = PartsUnloadRegisterDetail.grid.store.getCount();
	    		var canAddNum = unCheckQty - count ;
				if(addnumber==""){
				   MyExt.Msg.alert("请输入添加数量");
				   return ;
				}
				if(addnumber - canAddNum >0){
					Ext.Msg.confirm('提示','增加数量不能超过未登记数量，是否继续？', function(btn){
						if(btn == "yes"){
							PartsUnloadRegisterDetail.insertRow(addnumber);
						}else return ;
					});
				}else{
					PartsUnloadRegisterDetail.insertRow(addnumber);
				}
	        }},
	        	{ text:'删除明细', iconCls:'deleteIcon', handler:function(){
		        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	//	        if(!this.beforeDeleteFn()) return;
		        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
		        var data = PartsUnloadRegisterDetail.grid.selModel.getSelections();
		        if(data.length == 0 ){
		        	MyExt.Msg.alert("尚未选择一条记录！");
		            return;
		        }
		        var storeAt = PartsUnloadRegisterDetail.grid.store.indexOf(data[0]);
		        var records = PartsUnloadRegisterDetail.store.getModifiedRecords();
		        var count = records.length; 
		        var j = storeAt + 1;
		        if(count-1 == storeAt){
		        	j = storeAt-1;
		        }
			    PartsUnloadRegisterDetail.grid.getSelectionModel().selectRow(j);
			    for (var i = 0; i < data.length; i++){
			        PartsUnloadRegisterDetail.grid.store.remove(data[i]);
			    }
			    
		        
        }},{
	    	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
	    }]
	});
	PartsUnloadRegisterDetail.store.on("beforeload", function(){
		var beforeloadParam = {partsTypeIDX: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	
	PartsUnloadRegisterDetail.trainTypeToPartsGrid = new Ext.yunda.Grid({ 
	    loadURL: ctx + '/trainTypeToParts!findListByTrainTypeIDX.action',                 //装载列表数据的请求URL
	    storeAutoLoad: false, tbar: [],
		fields: [{
			header:'主键', dataIndex:'idx', hidden:true, editor:{  maxLength:8 } 
		},{
			header:"配件名称", dataIndex:"partsName",width:80,editor:{ } 
		},{
			header:"配件规格型号", dataIndex:"specificationModel",width:80,editor:{ } 
		},{
			header:"应登记数量",dataIndex:"standardQty",editor:{ } 
		},{
			header:"已登记数量",dataIndex:"checkQty",width:100,editor:{ } 
		},{
			header:"未登记数量", dataIndex:"unCheckQty",editor:{ },
			renderer:function(v){
			 	if(v < 0)  return '0';
			 	else return v ;
	         }
		}]
	});
	//移除监听
	PartsUnloadRegisterDetail.trainTypeToPartsGrid.un('rowdblclick', PartsUnloadRegisterDetail.trainTypeToPartsGrid.toEditFn, PartsUnloadRegisterDetail.trainTypeToPartsGrid);
	PartsUnloadRegisterDetail.trainTypeToPartsGrid.store.on("beforeload", function(){
		this.baseParams.trainTypeIDX = PartsUnloadRegister.unloadTrainTypeIdx ;  
		this.baseParams.trainNo = PartsUnloadRegister.unloadTrainNo ;  
		this.baseParams.repairClassId = PartsUnloadRegister.unloadRepairClassIdx ;  
	});
	//登帐并新增
	PartsUnloadRegisterDetail.saveFun = function(){
		var form = PartsUnloadRegister.form.getForm();
	    if (!form.isValid()) return;
	    var regData = form.getValues();
		var record = PartsUnloadRegisterDetail.store.getModifiedRecords();
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
				if(data.partsNo == data_v.partsNo && data.partsTypeIDX == data_v.partsTypeIDX){
					MyExt.Msg.alert("配件编号不能重复！");
					return ;
				}
			}
			for (var j = i+1; j < record.length; j++) {
				
				var data_c = {} ;
				data_c = record[j].data;
				if(data.identificationCode != undefined && data_c.identificationCode != undefined && data.identificationCode == data_c.identificationCode){
					MyExt.Msg.alert("配件识别码不能重复！");
					return ;
				}
			}
			datas.push(data);
		}
		PartsUnloadRegisterDetail.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/partsUnloadRegister!saveUnloadRegister.action',
            jsonData: datas,
            params : {regData : Ext.util.JSON.encode(regData)},
            success: function(response, options){
              	PartsUnloadRegisterDetail.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    PartsUnloadRegisterDetail.grid.store.removeAll();
                    PartsUnloadRegisterDetail.trainTypeToPartsGrid.store.load();
                    var partsTypeIDX = PartsUnloadRegisterDetail.form.find("name","partsTypeIDX")[0].getValue();
                    Ext.Ajax.request({
				        url: ctx + "/trainTypeToParts!findListByTrainTypeIDX.action",
				        params: {trainTypeIDX : PartsUnloadRegister.unloadTrainTypeIdx ,trainNo : PartsUnloadRegister.unloadTrainNo ,repairClassId : PartsUnloadRegister.unloadRepairClassIdx ,partsTypeIDX : partsTypeIDX },
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            Ext.getCmp("checkQty").setValue("");
				            Ext.getCmp("unCheckQty").setValue("");
				            if('undefined' != result.root[0] && null != result.root[0]){
				            	Ext.getCmp("checkQty").setValue(result.root[0].checkQty);
				            	Ext.getCmp("unCheckQty").setValue(result.root[0].unCheckQty);
				            }
				        }
					});
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                PartsUnloadRegisterDetail.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	PartsUnloadRegisterDetail.fieldset = {
			bodyStyle: 'padding-left:10px;',
			frame: true,
			autoScroll: true,
			//xtype:'fieldset',
			title: " 交旧明细",
			items:[{
				layout: "fit",
				height: 100,
				items: PartsUnloadRegisterDetail.form				
			},{
				layout: "fit",
				items: [PartsUnloadRegisterDetail.grid]
				
			}]
	};
});