/**
 * 配件信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsAccount');                       //定义命名空间
	PartsAccount.width = 80;
	/** **************** 定义全局函数开始 **************** */
	/** **************** 定义查询表单开始 **************** */
	PartsAccount.searchForm = new Ext.form.FormPanel({
		padding: 10, labelWidth: PartsAccount.labelWidth,
		layout: 'column', defaults: {
			columnWidth: .25, layout: 'form',
			defaults: {
				width: PartsAccount.fieldWidth
			}
		},
		items: [{
			items: [{
				id:"trainType_comb_id", 
				xtype: "Base_combo",
				fieldLabel:'下车车型',
				hiddenName: "unloadTrainTypeIdx",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
			    returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"typeCode"}],
                fields:['idx','typeName','typeCode','vehicleType'],
                queryParams: {},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                editable:false
			 }]
		},{                    
			items: [{
	    		xtype:'textfield', fieldLabel:'下车车号', name:"unloadTrainNo"
				}]						
		},{	
			items: [{
	    		xtype:'textfield', fieldLabel:'配件编号', name:"partsNo"
				}]	
		}],
		
		buttonAlign: 'center',
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				// 查询功能
				PartsAccount.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
//				var formPanel = this.findParentByType('form_acount');
//				formPanel.getForm().reset();
				PartsAccount.searchForm.getForm().reset();
				Ext.getCmp('trainType_comb_id').clearValue();
				// 重新查询
				PartsAccount.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义配件信息列表开始 **************** */
	PartsAccount.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
//	    border:false,
	    viewConfig: null,
	    tbar:null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'配件型号表主键', dataIndex:'partsTypeIDX', width: PartsAccount.width, hidden:true
		},{
			header:'配件名称', dataIndex:'partsName', width:160
		},{
			header:'规格型号', dataIndex:'specificationModel',width:160
		},{
			header:'物料编码', dataIndex:'', width:PartsAccount.width,
			renderer:function(v){
				if(null == PartsAccount.matCode || "undefined"==PartsAccount.matCode || "null" == PartsAccount.matCode) return "";
				return PartsAccount.matCode;
			}
		},{
			header:'配件编号', dataIndex:'partsNo',width:PartsAccount.width,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	     		var id = record.id;
	  			html = "<span><a href='#' onclick='	PartsAccountOrRdp.showPartsRdpOrDetail(\""+ value +"\",\""+ id +"\")'>"+value+"</a></span>";
	      		return null==value ? "":html;
			}
		},{
			header:'配件识别码', dataIndex:'identificationCode', width: PartsAccount.width,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	     		var id = record.id;
	  			html = "<span><a href='#' onclick='	PartsAccountOrRdp.showPartsRdpOrDetail(\""+ record.get('partsNo') +"\",\""+ id+ "\")'>"+value+"</a></span>";
	      		return null==value ? "":html;
			}		
		},{
			header:'状态修改日期', dataIndex:'partsStatusUpdateDate', xtype:'datecolumn', width: PartsAccount.width, hidden:true
		},{
			header:'计量单位', dataIndex:'unit', width: PartsAccount.width, hidden:true
		},{
			header:'生产厂家主键', dataIndex:'madeFactoryIdx', hidden:true
		},{
			header:'生产厂家', dataIndex:'madeFactoryName', width: PartsAccount.width,  hidden:true
		},{
			header:'出厂日期', dataIndex:'factoryDate', hidden:true, xtype:'datecolumn', width: PartsAccount.width, hidden:true
		},{
			header:'存放地点', dataIndex:'location', width: PartsAccount.width+20
		},{
			header:'详细配置', dataIndex:'configDetail', width: PartsAccount.width,  hidden:true
		},{
			header:'责任部门ID', dataIndex:'manageDeptId', width: PartsAccount.width, hidden:true
		},{
			header:'责任部门', dataIndex:'manageDept', width: PartsAccount.width+20
		},{
			header:'责任部门类型', dataIndex:'manageDeptType', width: PartsAccount.width, hidden:true
		},{
			header:'配件状态编码', dataIndex:'partsStatus', width: PartsAccount.width, hidden:true
		},{
			header:'是否新品', dataIndex:'isNewParts', width: PartsAccount.width, hidden:true
		},{
			header:'配件旧编号', dataIndex:'oldPartsNo', width: PartsAccount.width, hidden:true
		},{
			header:'下车车型', dataIndex:'unloadTrainType', width: PartsAccount.width
		},{
			header:'下车车型编码', dataIndex:'unloadTrainTypeIdx', width: PartsAccount.width, hidden:true
		},{
			header:'下车车号', dataIndex:'unloadTrainNo', width: PartsAccount.width
		},{
			header:'下车修程编码', dataIndex:'unloadRepairClassIdx', width: PartsAccount.width, hidden:true
		},{
			header:'下车修程', dataIndex:'unloadRepairClass', width: PartsAccount.width
		},{
			header:'下车修次编码', dataIndex:'unloadRepairTimeIdx', width: PartsAccount.width, hidden:true
		},{
			header:'下车修次', dataIndex:'unloadRepairTime', width: PartsAccount.width
		},{
			header:'下车日期', dataIndex:'unloadDate', xtype:'datecolumn', width: PartsAccount.width
		},{
			header:'下车位置', dataIndex:'unloadPlace', width: PartsAccount.width
		},{
			header:'下车原因', dataIndex:'unloadReason', width: PartsAccount.width, hidden:true
		},{
			header:'上车车型编码', dataIndex:'aboardTrainTypeIdx', width: PartsAccount.width, hidden:true
		},{
			header:'上车车型', dataIndex:'aboardTrainType', width: PartsAccount.width, hidden:true
		},{
			header:'上车车号', dataIndex:'aboardTrainNo', width: PartsAccount.width, hidden:true
		},{
			header:'上车修程编码', dataIndex:'aboardRepairClassIdx', width: PartsAccount.width, hidden:true
		},{
			header:'上车修程', dataIndex:'aboardRepairClass', width: PartsAccount.width, hidden:true
		},{
			header:'上车修次编码', dataIndex:'aboardRepairTimeIdx', width: PartsAccount.width, hidden:true
		},{
			header:'上车修次', dataIndex:'aboardRepairTime', width: PartsAccount.width, hidden:true
		},{
			header:'上车日期', dataIndex:'aboardDate', xtype:'datecolumn', width: PartsAccount.width, hidden:true
		},{
			header:'上车位置', dataIndex:'aboardPlace', hidden:true, width:PartsAccount.width
		},{
			header:'配件状态', dataIndex:'partsStatusName', width: PartsAccount.width
		}]
	});
	
	PartsAccount.grid.store.setDefaultSort('partsStatusUpdateDate', 'DESC');//设置默认排序
	PartsAccount.grid.un("rowdblclick",PartsAccount.grid.toEditFn,PartsAccount.grid);//取消双击操作
	
	PartsAccount.grid.store.on('beforeload',function(){
		var searchParam = {};//PartsAccount.searchForm.getForm().getValues();
		searchParam.partsTypeIDX = PartsAccount.partsTypeIdx;
		searchParam.partsStatus = PartsAccount.partsStatus;
		searchParam = MyJson.deleteBlankProp(searchParam);
		 var whereList = [];
	     for(prop in searchParam){
	     	if(prop=='partsStatus' && searchParam[prop]!=null){
	     		var value = searchParam[prop];
	     		whereList.push({propName:prop,propValue:value,compare:Condition.LLIKE});
	     		continue;
	     	}
	     	if(prop=='partsTypeIDX' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ,stringLike:false});
	     		continue;
	     	}  
	     	if(prop=='unloadTrainTypeIdx' && searchParam[prop]!=null){
	     		whereList.push({propName:prop, propValue: searchParam[prop],compare:Condition.EQ,stringLike:false}); 
	     		continue;
	     	}
	     	whereList.push({propName:prop, propValue: searchParam[prop]}) ; 
	     }
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
 	});
	/** **************** 定义配件信息列表结束 **************** */
 	
	/** **************** 定义配件信息显示窗口 **************** */
	PartsAccount.win = new Ext.Window({
		title: '配件清单',
		padding:"10px",
		maximized: true,
//		sautoScroll:true,
		closeAction: 'hide',
		layout: 'border',
		items: [{
			title: '查询',layout: 'fit',
			region: 'north', height: 120,
			collapsible: true, frame: true,
			layout: 'fit',
			items: PartsAccount.searchForm
		}, {
			layout: 'fit', region: 'center',
			items:  [PartsAccount.grid]	
		}],
		listeners: {
			show: function() {	
				PartsAccount.grid.store.load();
			}	
		}
	});

});