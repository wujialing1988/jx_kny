/**
 * 配件检修结果查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace("PartsIntegrateQuery");				// 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	PartsIntegrateQuery.labelWidth = 100;
	PartsIntegrateQuery.fieldWidth = 200;
	PartsIntegrateQuery.gridWidth = 80;
	PartsIntegrateQuery.qty = 0;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 规格型号选择控件赋值函数
	 * @param node 配件规格型号树节点
	 * @param e
	 */
	PartsIntegrateQuery.callReturnFn = function(node, e){
		// 规格型号显示字段赋值
	  	PartsRdpSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
		// 规格型号主键隐藏字段赋值
	  	PartsRdpSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	/**
	 *显示配件清单 
	 */
	PartsIntegrateQuery.show = function(id,status,matCode) {
		PartsAccount.partsTypeIdx = id;
		PartsAccount.partsStatus = status;
		PartsAccount.matCode = matCode;
		PartsAccount.win.show();
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	PartsIntegrateQuery.searchForm = new Ext.form.FormPanel({
		padding: 10, labelWidth: PartsIntegrateQuery.labelWidth,
		layout: 'column', defaults: {
			columnWidth: .25, layout: 'form', 
			defaults: {
				width: PartsIntegrateQuery.fieldWidth
			}
		},
		items: [{
			items: [{
				fieldLabel: '配件名称', name: 'partsName',  id:"partsName_comb",
				xtype:"Base_combo", anchor:'90%',
				editable: true, typeAhead: false,
				entity:"com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType",
				idProperty: "partsName",
				fields:["partsName"],
				displayField:"partsName",valueField:"partsName",
				listeners : {  
					 beforequery: function( queryEvent ) {
	        			 var value = this.getEl().dom.value;
	        			 this.queryName = "partsName";
	        			 this.cascadeStore();
	        		 }   
				}
			}]
		},{
			items: [{
				id:"trainType_comb",  
				fieldLabel: "车型",
				xtype: "Base_combo",
				hiddenName: "unloadTrainTypeIdx",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode','vehicleType'],
                returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"typeCode"}],
                queryParams: {},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                editable:false			  
			}]
		},{
			items: [{
		    	xtype:"checkbox",hidden:true, name:"byQty",id:"status", boxLabel:"不显示保有量为0的记录&nbsp;&nbsp;&nbsp;&nbsp;", checked: true,
		    	handler:function(){
		    		// 清空数据集			
					if(Ext.getCmp("status").checked){
						PartsIntegrateQuery.qty = 1;
					} 
					else {
						PartsIntegrateQuery.qty = 0;		
					}
		    		PartsIntegrateQuery.grid.store.load();
		    	}
			}]

		}],
		
		buttonAlign: 'center',
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				// 查询功能
				PartsIntegrateQuery.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				var formPanel = this.findParentByType('form');
				formPanel.getForm().reset();
				Ext.getCmp('trainType_comb').clearValue();
				Ext.getCmp('partsName_comb').clearValue();
				// 重新查询
				PartsIntegrateQuery.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义配件检修作业查询结果列表开始 **************** */
	PartsIntegrateQuery.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsIntegrateQuery!pageList.action',                //装载列表数据的请求URL
		tbar: null,
		viewConfig: {forceFit: true},
		fields: [{
			header:'配件idx主键', dataIndex:'idx', hidden: true
		},{
			header:'配件名称', dataIndex:'partsName', width: 160
		},{
			header:'规格型号', dataIndex:'specificationModel', width: 160
		},{
			header:'物料编码', dataIndex:'matCode',hidden:true
		},{
			header:'计量单位', dataIndex:'unit', width: PartsIntegrateQuery.gridWidth
		},{
			header:'局配属量', dataIndex:'limitQty', width: PartsIntegrateQuery.gridWidth
		},{
			header:'可修车台数', dataIndex:'standardQty', width: PartsIntegrateQuery.gridWidth
		},{
			header:'保有量', dataIndex:'byQty', width: PartsIntegrateQuery.gridWidth,hidden:true,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	     		var id = record.id;
	     		var matCode = record.get('matCode');
	  			html = "<span><a href='#' onclick='PartsIntegrateQuery.show(\""+ id + "\",\""+ PARTS_STATUS_ZC +"\",\""+matCode+"\")'>"+value+"</a></span>";
	      		return (0==value)?value:html;
			}
		},{
			header:'良好', dataIndex:'lhQty', width: PartsIntegrateQuery.gridWidth,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			var id = record.id; var matCode = record.get('matCode');
	  			html = "<span><a href='#' onclick='PartsIntegrateQuery.show(\""+ id + "\",\""+ PARTS_STATUS_LH +"\",\""+matCode+"\")'>"+value+"</a></span>";
	  			return (0==value)?value:html;
			}
		},{
			header:'待修', dataIndex:'dxQty', width: PartsIntegrateQuery.gridWidth,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			var id = record.id; var matCode = record.get('matCode');
	  			html = "<span><a href='#' onclick='PartsIntegrateQuery.show(\""+ id + "\",\""+ PARTS_STATUS_DX +"\",\""+matCode+"\")'>"+value+"</a></span>";
	  			return (0==value)?value:html;
			}
		},{
			header:'自修', dataIndex:'zxQty', width: PartsIntegrateQuery.gridWidth	,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			var id = record.id; var matCode = record.get('matCode');
	  			html = "<span><a href='#' onclick='PartsIntegrateQuery.show(\""+ id + "\",\""+ PARTS_STATUS_ZX +"\",\""+matCode+"\")'>"+value+"</a></span>";
				return (0==value)?value:html;
			}		
		},{
			header:'委外修', dataIndex:'wwxQty', width: PartsIntegrateQuery.gridWidth,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			var id = record.id; var matCode = record.get('matCode');
	  			html = "<span><a href='#' onclick='PartsIntegrateQuery.show(\""+ id + "\",\""+ PARTS_STATUS_WWX +"\",\""+matCode +"\")'>"+value+"</a></span>";
			    return (0==value)?value:html;
			}
		},{
			header:'待校验', dataIndex:'djyQty', width: PartsIntegrateQuery.gridWidth,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			var id = record.id; var matCode = record.get('matCode');
	  			html = "<span><a href='#' onclick='PartsIntegrateQuery.show(\""+ id + "\",\""+ PARTS_STATUS_DJY +"\",\""+matCode +"\")'>"+value+"</a></span>";
	  			return (0==value)?value:html;
	  		}
			
		},{
			header:'待报废', dataIndex:'dbfQty', width: PartsIntegrateQuery.gridWidth,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
  				var id = record.id;var matCode = record.get('matCode');
	  			html = "<span><a href='#' onclick='PartsIntegrateQuery.show(\""+ id + "\",\""+ PARTS_STATUS_DBF +"\",\""+matCode +"\")'>"+value+"</a></span>";
				return (0==value)?value:html;
			}
		},{
			header:'良好率', dataIndex:'lhl', width: PartsIntegrateQuery.gridWidth,
			renderer: function(value,  metaData, record, rowIndex, colIndex, store){ 
				var byQty = record.get('byQty');
				return (0==byQty)? "0":(value.toFixed(2)+"%");
			}
		}],
		
		toEditFn: Ext.emptyFn
	});
	
	// 默认按识别码正序排序
	PartsIntegrateQuery.grid.store.setDefaultSort('partsName', "ASC");
	
	// 列表数据源加载前的查询条件过滤
	PartsIntegrateQuery.grid.store.on('beforeload', function(){

	    var form = PartsIntegrateQuery.searchForm.getForm();
		var values = form.getValues();
		var searchParams = MyJson.deleteBlankProp(values);
		searchParams.byQty = PartsIntegrateQuery.qty;
		PartsIntegrateQuery.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	    
	});
	
	/** **************** 定义配件检修作业查询结果列表结束 **************** */
	
	//页面自适应布局
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			title: '查询',
			region: 'north', height: 140,
			collapsible: true, frame: true,
			layout: 'fit',
			items: PartsIntegrateQuery.searchForm
		}, {
			region: 'center',
			layout: 'fit',
			items: PartsIntegrateQuery.grid
		}]
	});
	
});
