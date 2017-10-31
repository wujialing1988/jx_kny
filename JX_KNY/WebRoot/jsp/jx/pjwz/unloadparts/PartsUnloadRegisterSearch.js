/**
 * 下车配件登记单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsUnloadRegister');                       //定义命名空间
PartsUnloadRegister.fieldWidth = 160;
PartsUnloadRegister.labelWidth = 90;
PartsUnloadRegister.searchParam = {};
//规格型号选择控件赋值函数
PartsUnloadRegister.callReturnFn=function(node,e){
  PartsUnloadRegister.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
  PartsUnloadRegister.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
}
PartsUnloadRegister.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsUnloadRegister.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsUnloadRegister.labelWidth,
	            columnWidth: 0.25, 
	            items: [
	            {
                     xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					  	hiddenName: 'specificationModel', editable:false,width:PartsUnloadRegister.fieldWidth,
					  	returnFn: PartsUnloadRegister.callReturnFn
                  },
                { id:"partsTypeIDX", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true },
	            { id:"takeOver_select",xtype:'OmEmployee_SelectWin',editable:false,fieldLabel : "接收人",hiddenName:'takeOverEmpId',
					returnField:[{widgetId:"PartsUnloadRegister_takeOverEmp",propertyName:"empname"}],width:PartsUnloadRegister.fieldWidth},
					{id:"PartsUnloadRegister_takeOverEmp",xtype:"hidden", name:"takeOverEmp"}
                    
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsUnloadRegister.labelWidth,
	            columnWidth: 0.25, 
	            items: [
	            	{ id:"partsName", name:"partsName", fieldLabel:"配件名称" },
					{
			        	id:"OmOrganizationCustom_comboTree_Id",
			        	xtype: "Base_combo",
			        	fieldLabel: "接收部门",
						fields:['orgid','orgSeq','orgName'],
						hiddenName: "takeOverDeptId",
						business: 'orgDicItem',
						queryParams: {dictTypeId: 'accountorg'},
					  	idProperty: 'orgid',
						displayField: "orgName", 
						valueField: "orgid",
						pageSize: 20, 
						minListWidth: 200, 
						editable:false,
						isAll:true
		            }
					
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsUnloadRegister.labelWidth,
	            columnWidth: 0.25, 
	            items: [
	            	{   
	            		id:"trainType_comb",
						fieldLabel: "下车车型",
						xtype: "Base_combo",
						hiddenName: "unloadTrainTypeIdx",
					    business: 'trainVehicleType',
					    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
		                fields:['idx','typeName','typeCode','vehicleType'],
		                returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"typeCode"}],
		                queryParams: {},// 表示客货类型
		    		    displayField: "typeCode", valueField: "idx",
		                pageSize: 20, minListWidth: 200,
		                editable:false,
						listeners : {   
				        	"select" : function(combo, record, index) {   
				            	//重新加载车号下拉数据
				                var trainNo_comb = Ext.getCmp("trainNo_comb");   
				                trainNo_comb.reset();  
				                trainNo_comb.clearValue(); 
				                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
				                trainNo_comb.cascadeStore();
				        	}   
				    	}
		            },
	                { id:"PartsUnloadRegister_unloadTrainType",xtype:"hidden", name:"unloadTrainType"},
	            	{ id:"partsNo", name:"partsNo", fieldLabel:"配件编号" }
                   
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsUnloadRegister.labelWidth,
	            columnWidth: 0.25, 
	            items: [
	            	{   id:"trainNo_comb",
						fieldLabel: "下车车号",
	    				xtype: "Base_combo",
	    				name:'trainNo',
	    				hiddenName: "unloadTrainNo",
	    			    business: 'jczlTrain',
	    			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
	                    fields:['trainTypeIDX','trainNo','trainTypeShortName'],
	                    queryParams: {},// 表示客货类型
	        		    displayField: "trainNo", valueField: "trainNo",
	                    pageSize: 20, minListWidth: 200,
	                    disabled:false,
	                    editable:true,		
					    listeners : {
	    					"beforequery" : function(){
	    						
	                		}
	    				}
					  }
	            ]
	        }
	        ]
	    }],
	     buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	PartsUnloadRegister.searchParam = PartsUnloadRegister.searchForm.getForm().getValues();
			    PartsUnloadRegister.searchParam = MyJson.deleteBlankProp(PartsUnloadRegister.searchParam);
			    PartsUnloadRegister.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	PartsUnloadRegister.searchForm.getForm().reset();
//            	Ext.getCmp("PartsTypeTreeSelect_select").clearValue();
            	Ext.getCmp("takeOver_select").clearValue();
            	Ext.getCmp("OmOrganizationCustom_comboTree_Id").clearValue();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("trainNo_comb").clearValue();
            	PartsUnloadRegister.searchParam = {};
			    PartsUnloadRegister.grid.store.load();
            }
        }]
	});
PartsUnloadRegister.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsUnloadRegister!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsUnloadRegister!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsUnloadRegister!logicDelete.action',            //删除数据的请求URL
    viewConfig : [],
    tbar: ['refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'单据编号', dataIndex:'billNo', hidden:true, editor:{ maxLength:50 }
	},{
		header:'规格型号', dataIndex:'specificationModel', width:150,editor:{  maxLength:100 }
	},{
		header:'配件名称', dataIndex:'partsName', editor:{  maxLength:100 }
	},{
		header:'配件编号', dataIndex:'partsNo', width:120,editor:{  maxLength:50 }
	},{
		header:'走行公里', dataIndex:'runingKM',width:100, editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'下车车型', dataIndex:'unloadTrainType', width:80,editor:{  maxLength:50 }
	},{
		header:'下车车型编码', dataIndex:'unloadTrainTypeIdx',hidden:true, editor:{  maxLength:8 }
	},{
		header:'下车车号', dataIndex:'unloadTrainNo', width:80,editor:{  maxLength:50 }
	},{
		header:'下车修程编码', dataIndex:'unloadRepairClassIdx', hidden:true,editor:{  maxLength:8 }
	},{
		header:'下车修程', dataIndex:'unloadRepairClass',width:80, editor:{  maxLength:50 }
	},{
		header:'下车修次编码', dataIndex:'unloadRepairTimeIdx',hidden:true, editor:{  maxLength:8 }
	},{
		header:'下车修次', dataIndex:'unloadRepairTime', width:80,editor:{  maxLength:50 }
	},{
		header:'接收部门主键', dataIndex:'takeOverDeptId',hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'接收部门', dataIndex:'takeOverDept', width:150,editor:{  maxLength:100 }
	},{
		header:'接收部门序列', dataIndex:'takeOverDeptOrgseq',hidden:true, editor:{  maxLength:512 }
	},{
		header:'接收人主键', dataIndex:'takeOverEmpId', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接收人', dataIndex:'takeOverEmp', width:80,width:80,editor:{  maxLength:50 }
	},{
		header:'交件人主键', dataIndex:'handOverEmpId',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'交件人', dataIndex:'handOverEmp', width:80,width:80,editor:{  maxLength:25 }
	},{
		header:'存放地点', dataIndex:'location',width:150, editor:{  maxLength:100 }
	},{
		header:'下车原因', dataIndex:'unloadReason',width:150, editor:{  maxLength:100 }
	},{
		header:'下车位置', dataIndex:'unloadPlace',width:150, editor:{  maxLength:100 }
	},{
		header:'下车日期', dataIndex:'unloadDate', width:150,xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'出厂日期', dataIndex:'factoryDate',width:150, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'接收部门类型', dataIndex:'takeOverDeptType',hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'配件规格型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'生产厂家主键', dataIndex:'madeFactoryIdx',hidden:true, editor:{  maxLength:5 }
	},{
		header:'生产厂家', dataIndex:'madeFactoryName', width:150,editor:{  maxLength:50 }
	},{
		header:'详细配置', dataIndex:'configDetail', width:150,editor:{  maxLength:200 }
	},{
		header:'单据状态', dataIndex:'status',hidden:true, editor:{  maxLength:20 }
	},{
		header:'创建人名称', dataIndex:'creatorName', hidden:true,editor:{  maxLength:50 }
	}]
});
//移除监听
PartsUnloadRegister.grid.un('rowdblclick', PartsUnloadRegister.grid.toEditFn, PartsUnloadRegister.grid);
PartsUnloadRegister.grid.store.setDefaultSort('unloadDate', 'DESC');//设置默认排序
//查询前添加过滤条件
PartsUnloadRegister.grid.store.on('beforeload',function(){
	PartsUnloadRegister.searchParam = PartsUnloadRegister.searchForm.getForm().getValues();
	PartsUnloadRegister.searchParam = MyJson.deleteBlankProp(PartsUnloadRegister.searchParam);
	var searchParam = PartsUnloadRegister.searchParam;
	delete searchParam.PartsTypeTreeSelect_select ;
	var whereList = []; 
	for(prop in searchParam){
		 	whereList.push({propName:prop,propValue:searchParam[prop]});
	}
    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
var viewport=new Ext.Viewport({layout:'fit',items:[{
	    layout:'border',frame:true,
	    items:[{
	       region:'north',
	       collapsible :true,
	       title:'查询',
		   height:140,
	       frame:true,
	       items:[PartsUnloadRegister.searchForm]
	    },{
	      region:'center',
	      frame:true,
	      layout:'fit',
	      items:[PartsUnloadRegister.grid]
	    }]
		
	}]});
});