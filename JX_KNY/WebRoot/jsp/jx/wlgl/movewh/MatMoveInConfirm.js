/**
 * 
 * 确认入库
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatMoveInConfirm');
	/** ************ 定义全局变量开始 ************ */
	MatMoveInConfirm.fieldWidth = 70;
	MatMoveInConfirm.matTypeItems = [];
	/** ************ 定义全局变量线束 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	// 自动生成“物料类型”radiogroup内容
	MatMoveInConfirm.createMatTypeItemsFn = function() {
		for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  			// 定义检查项
	        editor.name = "matType"; 	
	        editor.boxLabel = field.dictname;
	        editor.inputValue = field.dictname;
	        editor.width = 80;
	        MatMoveInConfirm.matTypeItems.push(editor);
		}
	}();
	
	
	//查询库房信息
	MatMoveInConfirm.getMatInfo = function(matType,whIdx, matCode){
		if(null != matCode && "" != matCode && null != matType && "" != matType && null != whIdx && "" != whIdx){
			Ext.Ajax.request({
				url: ctx + "/matTypeList!getMatInfo.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("totalQty_k").setValue(result.matStock.qty);										
	    			} else Ext.getCmp("totalQty_k").setValue(0);	
					if (null != result.whMatQuota) {
						 Ext.getCmp("mixQty_k").setValue(result.whMatQuota.minQty);
	    				 Ext.getCmp("maxQty_k").setValue(result.whMatQuota.maxQty);
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		}
	}
	//查询库位信息
	MatMoveInConfirm.getModelStockByLocation = function(whIdx, matType, matCode,locationName){
		if(null != matCode && "" != matCode && null != matType && "" != matType){
    		Ext.Ajax.request({
				url: ctx + "/matStockQuery!getModelStockByLocation.action",
				params: {whIdx: whIdx , matCode: matCode , matType : matType, locationName : locationName},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (null != result.matStock) {
						 Ext.getCmp("LocationQty_k").setValue(result.matStock.qty);
					} else  Ext.getCmp("LocationQty_k").setValue(0);
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
    	}
	}
	//退回原库
	MatMoveInConfirm.backOriginal = function(){
		var row = MatMoveInConfirm.grid.getSelectionModel();
		if (row.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择一条记录！');
			return;
		}
//		if (row.getCount() > 1) {
//			MyExt.Msg.alert('请只选择一条记录');
//			return;
//		}
		var record = row.getSelections();	
		ids=[];
		for(i=0; i<row.getCount();i++){
			ids.push(record[i].get('moveIdx'));
		}
		Ext.Msg.confirm("提示  ", "是否确认退回原库？  ", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request({
		            url: ctx + '/matMoveWH!backOriginal.action',
		            params:{ids:ids},
		            success: function(response, options){
		                if(self.loadMask)   self.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {	             // 操作成功
							alertSuccess("退回原库成功");
							MatMoveInConfirm.grid.store.load();
		                } else {
		                    alertFail(result.errMsg);
		                }
		            }
		        });
	        }
		});		
	}
	
	
	/** ************ 定义全局函数结束 ************ */
	
	/** ************ 定义表单开始 ************ */

	MatMoveInConfirm.baseForm = new Ext.form.FormPanel({
		layout:"column",height: 400,
		padding:"10px", frame:true,
		defaults: {
	    	layout:"form",  defaultType:"textfield",labelWidth: 70
	    },
		items:[{
				xtype:"panel",
				columnWidth:1,
				items:[{
					id:"matType_k",	xtype:'radiogroup',disabled: true,
					fieldLabel:"领料类型", allowBlank: false, 
					items: MatMoveInConfirm.matTypeItems, anchor:'98%'
				}]
			},{
				xtype:"panel",
				columnWidth:0.35,  defaults:{width:MatMoveInConfirm.fieldWidth+70},
				items:[{
					id:"whName_k",xtype:"textfield", disabled: true,
		          	fieldLabel:"接收库房",name:"getWhName", allowBlank: false							
		        },{
		        	id:"whIdx_k", xtype:"hidden", name:"getWhIDX"
		        }]
     		},{
     			xtype:"panel",
				columnWidth:0.25, labelWidth:55,
				items:[{
					id:"totalQty_k", disabled: true,
					xtype:"textfield",  width:MatMoveInConfirm.fieldWidth,
					fieldLabel:"库存总量"
				}]
     		},{
     			columnWidth: 0.4,labelWidth: 40,
				 items: [{
					xtype: 'compositefield', fieldLabel:"低储", combineErrors: false,
					items:[{ 
						id:"mixQty_k", xtype:"textfield",  width: MatMoveInConfirm.fieldWidth-20, disabled: true
					},{	xtype: 'label', text: '高储:', style: 'height:23px; line-height:23px;'	
					},{ id:"maxQty_k", xtype:"textfield",  width:MatMoveInConfirm.fieldWidth-20, disabled: true
					}]
			  	}]
     		},{
				columnWidth:0.6,
				items:[{
                	id:"matDesc_k",
                	xtype:"Base_combo", allowBlank: false,
					name:"matDesc", disabled: true,
					fieldLabel:"物料描述", width: MatMoveInConfirm.fieldWidth + 230,
					entity:"com.yunda.jx.pjwz.partsBase.entity.MatTypeList",						  
					fields:["matCode","matDesc","unit"],
					displayField:"matDesc",valueField:"matDesc",
					returnField:[{widgetId:"matCode_k",propertyName:"matCode"},
					             {widgetId:"unit_k",propertyName:"unit"}]
					             
				},{
			        id:"matCode_k",xtype:"hidden", name:"matCode"
				}]
            },{ 
				columnWidth:0.4,labelWidth: 60,
				items:[{
					id:"unit_k",
					xtype:"textfield", width:MatMoveInConfirm.fieldWidth,
					name:"unit", disabled: true,
					fieldLabel:"计量单位"
				}]
			},{
				columnWidth:0.6, 
				items:{
					xtype: 'compositefield', fieldLabel:'库位', combineErrors: false,
						items:[{					
	                	id:"locationName_comb",
	                	fieldLabel:"库位",hiddenName:"getLocationName",
						xtype:"Base_combo", allowBlank: false, width: MatMoveInConfirm.fieldWidth + 100,
						entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",	
						fields:["idx","locationName","status"],
						displayField:"locationName", valueField:"locationName",
						returnField:[{widgetId:"ywwIdx_k", propertyName:"idx"},{widgetId:"status_k",propertyName:"status"}],
						listeners : {
	                		"select" : function() {   
	    	                	var matCode = Ext.getCmp("matCode_k").getValue();
	    	                	var whIdx = Ext.getCmp("whIdx_k").getValue();
	    	                	var matType = "" ;
								//查询库存信息
				                if(null != Ext.getCmp("matType_k").getValue()){
	    	                	   matType = Ext.getCmp("matType_k").getValue().inputValue;
	    	                	}
	    	                	MatMoveInConfirm.getModelStockByLocation(whIdx, matType, matCode, this.getValue());
						     }   
		    			}
		    		},{
		    			id:"ywwIdx_k",name:"getLocationIDX", xtype:"hidden"
		    		},{
		            	xtype: 'radiogroup',
		            	id:"status_k",
		                fieldLabel: '',
		                width:150,
		                items: [
		                    { boxLabel: '未满', name: 'status', inputValue: '未满', checked : true },
		                    { boxLabel: '已满', name: 'status', inputValue: '已满' }
		                ] 
		    		}]
	    		}
	    	},{
				xtype:"panel",labelWidth: 60,
				columnWidth:0.4,
				items:[{
					id:"LocationQty_k",disabled: true, width:MatMoveInConfirm.fieldWidth,
					xtype:"textfield",  
					fieldLabel:"库存数量"
				}]		
            },{
                xtype:"panel",
				columnWidth:0.3,
				items:[{
                	id:"qty_k",disabled: true, width:MatMoveInConfirm.fieldWidth,
                	xtype:"textfield", 
					name:"qty", 
					fieldLabel:"入库数量",
					allowBlank: false		
				},{		
					id:"idx_k",name:"idx", xtype:"hidden"					
				}]   
			}]		
	});		
	/** ************ 定义表单结束 ************ */

	
	//定义移库单列表
	MatMoveInConfirm.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matOutWH!findMoveInList.action',         //装载列表数据的请求URL
		tbar:[ {
		    	text : '确认入库',iconCls : 'addIcon', handler : function() {
		    		MatMoveInConfirm.showMoveInWin(0);
		     }},'-',  {
		    	text : '退回原库', iconCls : 'addIcon', handler : function() {
		    		MatMoveInConfirm.backOriginal();
		    }}, '-','refresh' ],
		fields:[{
		   dataIndex: 'moveIdx', hidden: true
		},{
		   header: '接收库房', dataIndex: 'getWhName',width: MatMoveInConfirm.fieldWidth
		},{
		   header: '接收库房', dataIndex: 'getWhIDX', hidden: true
		},{	
		   header: '物料类型', dataIndex: 'matType',width: MatMoveInConfirm.fieldWidth
		},{
		   header: '物料编码', dataIndex: 'matCode',width: MatMoveInConfirm.fieldWidth 
		},{
		   header: '物料描述', dataIndex: 'matDesc',width: MatMoveInConfirm.fieldWidth
		},{
		   header: '计量单位', dataIndex: 'unit',width: MatMoveInConfirm.fieldWidth-20
		},{
		   header: '出库数量', dataIndex: 'qty',width: MatMoveInConfirm.fieldWidth-20
		},{
		   header: '出库库房', dataIndex: 'whName',width: MatMoveInConfirm.fieldWidth-20
		},{
	  	   header: '出库库房id', dataIndex: 'whIDX', hidden: true
		},{
		   header: '出库库位', dataIndex: 'locationName',width: MatMoveInConfirm.fieldWidth	  
		},{	
		   header: '出库时间', dataIndex: 'whDate',width: MatMoveInConfirm.fieldWidth
		},{
		   header: '出库入', dataIndex: 'exWhEmp',width: MatMoveInConfirm.fieldWidth
		},{
		   header: '状态', dataIndex: 'moveStatus',width: MatMoveInConfirm.fieldWidth
	    },{
		   header: '操作',dataIndex:"",renderer: function(value, record, rowIndex, colIndex, storeId){
				var html = "";
		    		html = "<span><a href='#' onclick = 'MatMoveInConfirm.showMoveInWin("+1+")'>拆分入库</a></span>";
		            return html;  	
				return html;
    	   }	
	    }],
	    toEditFn: Ext.emptyFn //覆盖双击编辑事件
	});
	
	//确认入库窗口
	MatMoveInConfirm.moveInWin = new Ext.Window({
	    title:"确认入库", width: 700, anchor: "98%", height: 300,
	    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true,split:true, 
	    items: MatMoveInConfirm.baseForm, 
	    buttons: [{
	        text: "确定入库", iconCls: "saveIcon", handler: function() {        	    
				Ext.getCmp('matType_k').enable();
				Ext.getCmp('whName_k').enable();
				Ext.getCmp('matDesc_k').enable();
				Ext.getCmp('qty_k').enable();	
				Ext.getCmp('unit_k').enable();
				var form = MatMoveInConfirm.baseForm.getForm();		        	
				var values =  form.getValues();
				Ext.getCmp('matType_k').disable();
				Ext.getCmp('whName_k').disable();
				Ext.getCmp('matDesc_k').disable();
				Ext.getCmp('qty_k').disable();
				Ext.getCmp('unit_k').disable();
				if (!form.isValid()) { return; }
				if (values.qty <= 0 || isNaN(values.qty)) {
					MyExt.Msg.alert("请输入大于0的有效数字！");
					return;
				}
				Ext.Msg.confirm("提示  ", "是否确认入库？  ", function(btn){
					datas=null;
			        if(btn == 'yes') {
				        Ext.Ajax.request({
				            url: ctx + '/matMoveInConfirm!saveMoveInMat.action',
				            jsonData: Ext.util.JSON.encode(values),
				            params:{datas: Ext.util.JSON.encode(datas)},
				            success: function(response, options){
				                if(self.loadMask)   self.loadMask.hide();
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {	             // 操作成功
									alertSuccess("入库成功");
									MatMoveInConfirm.moveInWin.hide();
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
		     text: "取消", iconCls: "closeIcon", handler: function(){ MatMoveInConfirm.moveInWin.hide(); }
		}]
	});
	
	MatMoveInConfirm.showMoveInWin = function(flag) {
		var row = MatMoveInConfirm.grid.getSelectionModel();
		if (row.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择一条记录！');
			return;
		}
		if (row.getCount() > 1) {
			MyExt.Msg.alert('请只选择一条记录');
			return;
		}
		var record = row.getSelections()[0];
		//拆分入库
		if(1==flag) { 
			MatMoveInConfirm.record = record.json;
			SplinMoveInWin.shoWin.show();
		}
		//入库
	    if(0==flag){
			Ext.getCmp("idx_k").setValue(record.json.moveIdx);
			Ext.getCmp("whName_k").setValue(record.get('getWhName'));
			Ext.getCmp("whIdx_k").setValue(record.get('getWhIDX'));
			Ext.getCmp("matType_k").setValue(record.get('matType'));
			Ext.getCmp("matDesc_k").setValue(record.get('matDesc'));
			Ext.getCmp("qty_k").setValue(record.get('qty'));
			Ext.getCmp("unit_k").setValue(record.get('unit'));
			Ext.getCmp("matCode_k").setValue(record.get('matCode'));
			MatMoveInConfirm.getMatInfo(record.get('matType'),record.get('getWhIDX'),record.get('matCode'));
			//重新加载库位下拉数据
		    var locationName_comb = Ext.getCmp("locationName_comb");   
		    locationName_comb.reset();  
		    locationName_comb.clearValue();
		    locationName_comb.queryParams = {"wareHouseIDX":record.get('getWhIDX')};
		    locationName_comb.cascadeStore();	
			MatMoveInConfirm.moveInWin.show();	
	    }
	} 
	
	/** ************布局 ************ */
	MatMoveInConfirm.viewport = new Ext.Viewport({
		layout: 'fit',
		items:[{
			region: 'center',
			layout: 'fit',
			items: [MatMoveInConfirm.grid]
		}]
	});
	
	
	
});