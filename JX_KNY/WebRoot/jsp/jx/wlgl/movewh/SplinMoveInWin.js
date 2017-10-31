/**
 * 
 * 拆分入库
 * 
 */

Ext.onReady(function() {
	Ext.namespace('SplinMoveInWin');
	/** ************ 定义全局变量开始 ************ */
	SplinMoveInWin.fieldWidth = 70;
	SplinMoveInWin.matTypeItems = [];
	SplinMoveInWin.locationQty = 0;
	/** ************ 定义全局变量线束 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	SplinMoveInWin.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        SplinMoveInWin.matTypeItems.push(editor);
		}
	}();
	
	//查询库房信息
	SplinMoveInWin.getMatInfo = function(matType,whIdx, matCode){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
			Ext.Ajax.request({
				url: ctx + "/matTypeList!getMatInfo.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("totalQty_s").setValue(result.matStock.qty);										
	    			} else Ext.getCmp("totalQty_s").setValue(0);	
					if (null != result.whMatQuota) {
						 Ext.getCmp("mixQty_s").setValue(result.whMatQuota.minQty);
	    				 Ext.getCmp("maxQty_s").setValue(result.whMatQuota.maxQty);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		}
	}
	//查询库位信息
	SplinMoveInWin.getModelStockByLocation = function(whIdx, matType, matCode,locationName){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
    		Ext.Ajax.request({
				url: ctx + "/matStockQuery!getModelStockByLocation.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType, locationName : locationName},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
			        	 Ext.getCmp("locationQty_s").setValue(result.matStock.qty);
//			        	 SplinMoveInWin.locationQty.setValue(result.matStock.qty);
			        	SplinMoveInWin.locationQty = result.matStock.qty;
					}
					else { Ext.getCmp("locationQty_s").setValue(0);}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
    	}
	}
	//新增一行
    SplinMoveInWin.addFn = function(){
    	var record_v = null;
		record_v = new Ext.data.Record();
		record_v.set("qty",1);
		record_v.set("locationQty",0);
		record_v.set("locationName","");
		record_v.set("status","未满");
        SplinMoveInWin.locationGrid.store.insert(0, record_v); 
        SplinMoveInWin.locationGrid.getView().refresh(); 
        SplinMoveInWin.locationGrid.getSelectionModel().selectRow(0);
    }
    //删除一行
	SplinMoveInWin.deleteFn  = function(){
		var data = SplinMoveInWin.locationGrid.selModel.getSelections();
        if(data.length == 0 ){
        	MyExt.Msg.alert("尚未选择一条记录！");
            return;
        }
        var storeAt = SplinMoveInWin.locationGrid.store.indexOf(data[0]);
        var records = SplinMoveInWin.store.getModifiedRecords();
        var count = records.length; 
        var j = storeAt + 1;
        if(count-1 == storeAt){
        	j = storeAt-1;
        }
	    SplinMoveInWin.locationGrid.getSelectionModel().selectRow(j);
	    for (var i = 0; i < data.length; i++){
	        SplinMoveInWin.locationGrid.store.remove(data[i]);
	    }
		SplinMoveInWin.locationGrid.getView().refresh();	    
	}
	/** ************ 定义全局函数结束 ************ */

	
	SplinMoveInWin.baseForm = new Ext.form.FormPanel({
		layout:"column",
		padding:"10px", frame:true, 
		defaults: {
	    	layout:"form",  defaultType:"textfield",labelWidth: 70
	    },
		items:[{
				xtype:"panel",
				columnWidth:1,
				items:[{
					id:"matType_s",	xtype:'radiogroup',disabled: true,
					fieldLabel:"领料类型", allowBlank: false, 
					items: SplinMoveInWin.matTypeItems, anchor:'100%'
				}]
			},{
				columnWidth:0.35, xtype:"panel", 
				items:[{
					id:"whName_s", xtype:"textfield", disabled: true, width:SplinMoveInWin.fieldWidth + 70, allowBlank: false,
		          	fieldLabel:"接收库房",xtype:"textfield", name:"getWhName"
		          	},{
			        	id:"whIdx_s", xtype:"hidden", name:"getWhIDX"
	                }]		
			 },{
			 	xtype:"panel",
				columnWidth:0.25,labelWidth: 55,
				items:[{
					id:"totalQty_s", disabled: true, width:SplinMoveInWin.fieldWidth,
					xtype:"textfield",  
					fieldLabel:"库存总量"
					}]	
			},{	
     			columnWidth: 0.4,labelWidth: 40,
				 items: [{
					xtype: 'compositefield', fieldLabel:"低储", combineErrors: false,
					items:[{ 
						id:"mixQty_s",
						xtype:"textfield",  
						fieldLabel:"低储",
						disabled: true,
						width:SplinMoveInWin.fieldWidth-20
					},{	xtype: 'label', text: '高储:', style: 'height:23px; line-height:23px;'	
					},{ id:"maxQty_s",
						xtype:"textfield",  
						fieldLabel:"高储",
						disabled: true,
						width:SplinMoveInWin.fieldWidth-20
					}]
			  	}]			
			},{
			    xtype:"panel",
				columnWidth:0.6,
				items:[{
                	id:"matDesc_s", width:SplinMoveInWin.fieldWidth+220,
					name:"matDesc", xtype:"textfield",  
					fieldLabel:"物料描述",disabled: true		
				},{
			        id:"matCode_s",xtype:"hidden", name:"matCode"           		
                }]
	       },{ 
	       		xtype:"panel",
				columnWidth:0.4,labelWidth: 60,
				items:{
		                id:"unit_s",xtype:"textfield", width:SplinMoveInWin.fieldWidth,
						name:"unit", disabled: true,
						fieldLabel:"计量单位"}
			},{
				xtype:"panel",
				columnWidth:0.3,
				items:{
					id:"qty_s", xtype:"textfield", 
					name:"qty", disabled: true, width:SplinMoveInWin.fieldWidth,
					fieldLabel:"待入库数量"	}
			},{		
				id:"idx_s",name:"idx", xtype:"hidden"					
			}]		
	});		
	/** ************ 定义表单结束 ************ */
	
	// 数据容器
	SplinMoveInWin.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:false, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/warehouseLocation!pageList.action',
		fields:['idx','locationName','wareHouseIDX','status']
	});
	
	// 操作掩码
    SplinMoveInWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 行选择模式
    SplinMoveInWin.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    
	SplinMoveInWin.locationGrid = new Ext.grid.EditorGridPanel({
		border: false, enableColumnMove: true, stripeRows: true, selModel: SplinMoveInWin.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: SplinMoveInWin.store,
		colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
			{
	        	sortable:false, dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	sortable:false, dataIndex:'locationName', header:'库位',editor:{
	        		id:"location_comb",
					xtype:"Base_combo", allowBlank: false, width:SplinMoveInWin.fieldWidth+30,
					entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",	
					fields:["idx","locationName","status"],
					displayField:"locationName", valueField:"locationName",
					returnField:[{widgetId:"ywwIdx_s", propertyName:"idx"},{widgetId:"status_s",propertyName:"status"}],
					listeners : {
                		"select" : function(combo,record) {		
    	                	var matCode = Ext.getCmp("matCode_s").getValue();
    	                	var whIdx = Ext.getCmp("whIdx_s").getValue();      
    	                	var matType = "" ;
							//查询库存信息
			                if(null != Ext.getCmp("matType_s").getValue()){
    	                	   matType = Ext.getCmp("matType_s").getValue().inputValue;
    	                	}
    	                	SplinMoveInWin.getModelStockByLocation(whIdx, matType, matCode, this.getValue());
					     }   
	    			}
	        	}
	        },{
	        	sortable:false, dataIndex:'locationIdx', hidden:true, header:'库位id',editor:{
	        		id:"ywwIdx_s", xtype:'hidden'
	        	}
	        },{
	        	sortable:false, dataIndex:'qty', header:'入库数量', editor:{ 	
		        		maxLength: 6, 
		        		vtype: "nonNegativeInt", 
		        		allowBlank: false
					}
	        },{	
	        	sortable:false,  dataIndex:'status',header:'入库状态', editor:{   
	                id:"status_s",
		        	xtype: 'combo',
		            fieldLabel: '入库状态',
		            hiddenName:'status',
		            store:new Ext.data.SimpleStore({
					    fields: ['v'],
						data : [['已满'],['未满']]
					}),
					valueField:'v',
					displayField:'v',
					mode:'local',
					editable: false,
					allowBlank: false
			    }
	       },{
	        	sortable:false, dataIndex:'locationQty', header:'库存数量', editor:{
	        		id:"locationQty_s", xtype:'hidden'
	        	}
	        }]),
		listeners: {
			afteredit:function(e){
				var status = Ext.getCmp("status_s").getValue();
				var locationIdx = Ext.getCmp("ywwIdx_s").getValue();
				var locationQty = Ext.getCmp("locationQty_s").getValue();
//				var locationQty = SplinMoveInWin.locationQty;
				if(e.field == "qty" || e.field == "status"){
					return;
				}else {					
					e.record.data.status = status;
					e.record.data.locationIdx = locationIdx;
					e.record.data.locationQty = locationQty;
					SplinMoveInWin.locationGrid.getView().refresh();
				}
			}
		},
		tbar:[{
			text:'添加',iconCls:'addIcon',handler:SplinMoveInWin.addFn
		}, '&nbsp;&nbsp;',{
			text:'删除', iconCls:'deleteIcon', handler:SplinMoveInWin.deleteFn
	    }]
	});
	
	//设置查询条件	
	SplinMoveInWin.store.on("beforeload", function(){
		var beforeloadParam = {wareHouseIDX: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	
	SplinMoveInWin.shoWin = new Ext.Window({
	    title:"拆分入库", height: 500,
		width: 700,closeAction: 'hide',
		layout: 'border',
		modal: true,
		defaults: {layout: 'fit'},
		items: [{
	 		height: 150,
			region: 'north',
			frame: true,							
			items: SplinMoveInWin.baseForm
		},{
			region: 'center',
    		collapsible: true,
            items: [SplinMoveInWin.locationGrid]
		}],
		listeners: {
			beforeshow: function(window) {
				var record = MatMoveInConfirm.record;
				Ext.getCmp("idx_s").setValue(record.moveIdx);
				Ext.getCmp("whName_s").setValue(record.getWhName);
				Ext.getCmp("whIdx_s").setValue(record.getWhIDX);
				Ext.getCmp("matType_s").setValue(record.matType);
				Ext.getCmp("matDesc_s").setValue(record.matDesc);
				Ext.getCmp("qty_s").setValue(record.qty);
				Ext.getCmp("unit_s").setValue(record.unit);
				Ext.getCmp("matCode_s").setValue(record.matCode);
				SplinMoveInWin.getMatInfo(record.matType,record.getWhIDX, record.matCode);
				//重新加载库位下拉数据
			    var location_comb = Ext.getCmp("location_comb");   
			    location_comb.reset();  
			    location_comb.clearValue();
			    location_comb.queryParams = {"wareHouseIDX":record.getWhIDX};
			    location_comb.cascadeStore();
			   
			},
			show: function() {
				SplinMoveInWin.locationGrid.store.load();
			}
		},
		buttonAlign:'center',
	    buttons: [{
	        text: "确定入库", iconCls: "saveIcon", handler: function() {        	    
				Ext.getCmp('unit_s').enable();
				Ext.getCmp('matType_s').enable();
				Ext.getCmp('whName_s').enable();
				Ext.getCmp('matDesc_s').enable();
				Ext.getCmp('qty_s').enable();
				var form = SplinMoveInWin.baseForm.getForm();
				var values =  form.getValues();
				Ext.getCmp('matType_s').disable();
				Ext.getCmp('whName_s').disable();
				Ext.getCmp('matDesc_s').disable();
				Ext.getCmp('qty_s').disable();
				Ext.getCmp('unit_s').disable();		
	        	if (!form.isValid()) { return;}		
				//获取列表行数
				var store = SplinMoveInWin.store;
				// 验证添加的明细数据是否为空
				if(store.getCount() == 0){
					MyExt.Msg.alert("请添加库位信息！");
					return ;
				}
				var datas = new Array();
				var sumQty = 0;
				for (var i = 0; i < store.getCount(); i++) {
					var data = store.getAt(i).data;
					if (data.qty == 0) {
						MyExt.Msg.alert("数量不能为0，请输入有效数字！");
						return;
					}
					if(null == data.locationName || ""== data.locationName){
						MyExt.Msg.alert("<font color='red'>库位名称为空！</font>");
						return; 
					}
					if(i>0 && data.locationName==(store.getAt(i-1).data).locationName){
						MyExt.Msg.alert("库位<font color='red'>【" + data.locationName + "】</font>添加重复,请重新选择！");
						return; 
					}
					sumQty = parseInt(sumQty) + parseInt(data.qty);		
					datas.push(data);
				} 
				if(parseInt(sumQty)!= parseInt(values.qty)){
					MyExt.Msg.alert("入库总量与待入库数量不符！");
				    return;
				}
				
				Ext.Msg.confirm("提示  ", "是否确认入库？  ", function(btn){
					for (var i = 0; i < store.getCount(); i++) {			
						delete datas[i].locationQty;
					}
			        if(btn == 'yes') {
				        Ext.Ajax.request({
				            url: ctx + '/matMoveInConfirm!saveMoveInMat.action',
				            jsonData: Ext.util.JSON.encode(values),
				            params : {datas: Ext.util.JSON.encode(datas)},
				            success: function(response, options){
				                if(self.loadMask)   self.loadMask.hide();
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {	             // 操作成功
									alertSuccess("入库成功");
									SplinMoveInWin.shoWin.hide();
									MatMoveInConfirm.grid.store.load();
				                } else {
				                    alertFail(result.errMsg);
				                }
				            }
				        });
			        }
				});		
	        }
		}, {
		     text: "取消", iconCls: "closeIcon", handler: function(){ SplinMoveInWin.shoWin.hide(); }
		}]
		
	});
	
	

});