/**
 * 下车配件登记确认 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
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
	            	{ id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "下车车型",
						  hiddenName: "unloadTrainTypeIdx",
						  returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"shortName"}],
						  displayField: "shortName", valueField: "typeID",width:100,
						  pageSize: 20, minListWidth: 200,   //isCx:'no',
						  editable:false  ,
							listeners : {   
					        	"select" : function() {   
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
	            	{ id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "下车车号",
					  hiddenName: "unloadTrainNo",width:PartsUnloadRegister.fieldWidth,
					  displayField: "trainNo", valueField: "trainNo",
					  pageSize: 20, minListWidth: 200, 
					  editable:true,
					  listeners : {
	    				"beforequery" : function(){
	    					//选择修次前先选车型
	                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择下车车型！");
	    						return false;
	    					}
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
    tbar: [{ text:'确认', iconCls:'checkIcon', handler:function(){
	    		var sm = PartsUnloadRegister.grid.getSelectionModel();
			    if (sm.getCount() < 1) {
			        MyExt.Msg.alert("尚未选择一条记录！");
			        return;
			    }
			    var data = sm.getSelections();
			    var ids = new Array();
			    for (var i = 0; i < data.length; i++){
			    	if(data[ i ].get("status") != status_ed){
			    		MyExt.Msg.alert("只有状态为【已登帐】的数据才能进行确认操作！");
			        	return;
			    	}
		    		ids.push(data[ i ].get("idx"));
			    }
			    Ext.Ajax.request({
			        url: ctx + "/partsUnloadRegister!updatePartsUnloadRegisterForCheck.action",
			        params: {ids: ids},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                alertSuccess();
			                PartsUnloadRegister.grid.store.reload(); 
			            } else {
			                alertFail(result.errMsg);
			            }
			        },
			        failure: function(response, options){
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			        }
			    });
	        }},'refresh',{
			      xtype:'checkbox', name:'status', id: 'status_wait', boxLabel:'未登帐&nbsp;&nbsp;&nbsp;&nbsp;', 
				  inputValue:status_wait,checked:true,
				  handler: function(){PartsUnloadRegister.checkQuery();}
				},{
			      xtype:'checkbox', name:'status', id: 'status_ed', boxLabel:'已登帐&nbsp;&nbsp;&nbsp;&nbsp;', 
				  inputValue:status_ed,checked:true,
				  handler: function(){PartsUnloadRegister.checkQuery();}
				},{
				   xtype:'checkbox', name:'status', id: 'status_checked', boxLabel:'已确认&nbsp;&nbsp;&nbsp;&nbsp;', 
			       inputValue:status_checked, //checked:true,
				   handler: function(){PartsUnloadRegister.checkQuery();}
				}],
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
		header:'收件日期', dataIndex:'takeOverTime', width:150,xtype:'datecolumn', editor:{ xtype:'my97date' }
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
		header:'单据状态', dataIndex:'status',
		renderer:function(v){
		 	if(v == status_wait)  return '未登帐';
            if(v == status_ed) return '已登帐';
            if(v == status_checked) return '已确认';
         }
	},{
		header:'创建人名称', dataIndex:'creatorName', hidden:true,editor:{  maxLength:50 }
	}]
});
//移除监听
PartsUnloadRegister.grid.un('rowdblclick', PartsUnloadRegister.grid.toEditFn, PartsUnloadRegister.grid);
PartsUnloadRegister.grid.store.setDefaultSort('unloadDate', 'DESC');//设置默认排序
 //公用checkbox查询方法
PartsUnloadRegister.status = "10,20";
//状态多选按钮
PartsUnloadRegister.checkQuery = function(){
	    PartsUnloadRegister.status = "-1";
	if(Ext.getCmp("status_ed").checked){
		PartsUnloadRegister.status = PartsUnloadRegister.status + "," + status_ed;
	}
	if(Ext.getCmp("status_checked").checked){
		PartsUnloadRegister.status = PartsUnloadRegister.status + "," + status_checked;
	}
	PartsUnloadRegister.grid.store.load();
}
//查询前添加过滤条件
PartsUnloadRegister.grid.store.on('beforeload',function(){
	PartsUnloadRegister.searchParam = PartsUnloadRegister.searchForm.getForm().getValues();
	PartsUnloadRegister.searchParam = MyJson.deleteBlankProp(PartsUnloadRegister.searchParam);
	var searchParam = PartsUnloadRegister.searchParam;
	searchParam.status = PartsUnloadRegister.status;
	delete searchParam.PartsTypeTreeSelect_select ;
	var whereList = []; 
	for(prop in searchParam){
			if('status' == prop){
				var val = searchParam[prop];
				val = val.toString();
				val = val.split(",");
                if(val instanceof Array){
                    whereList.push({propName:'status', propValues:val, compare:Condition.IN });
                } else {
                    var valAry = [];
                    valAry.push(val);
                    whereList.push({propName:'status', propValues:valAry, compare:Condition.IN });
                }
                continue;
		 	}
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