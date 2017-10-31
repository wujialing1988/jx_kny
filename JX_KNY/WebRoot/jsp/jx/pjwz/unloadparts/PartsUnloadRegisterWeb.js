/**
 * 下车配件登记单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainWorkPlanView');                       //定义命名空间
TrainWorkPlanView.fieldWidth = 160;
TrainWorkPlanView.labelWidth = 90;
TrainWorkPlanView.searchParam = {};
TrainWorkPlanView.rdpIdx = "" ;
//规格型号选择控件赋值函数
TrainWorkPlanView.callReturnFn=function(node,e){
  TrainWorkPlanView.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
  TrainWorkPlanView.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
}
TrainWorkPlanView.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: TrainWorkPlanView.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanView.labelWidth,
	            columnWidth: 0.33, 
	            items: [
	            	{ id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
						  hiddenName: "trainTypeIdx",
						  displayField: "shortName", valueField: "typeID",width:100,
						  pageSize: 20, minListWidth: 200,   //isCx:'no',
						  editable:false  ,
							listeners : {   
					        	"select" : function() {   
					            	//重新加载修程下拉数据
					                var rc_comb_s = Ext.getCmp("rc_comb_s");
					                rc_comb_s.reset();
					                rc_comb_s.clearValue();
					                rc_comb_s.getStore().removeAll();
					                rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue()};
					                rc_comb_s.cascadeStore();
					        	}   
					    	}
		                    }
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanView.labelWidth,
	            columnWidth: 0.33, 
	            items: [
					  {xtype:'textfield',fieldLabel:'车号',name:"trainNo"}
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanView.labelWidth,
	            columnWidth: 0.33, 
	            items: [
	            	{
						id:"rc_comb_s",
						xtype: "Base_combo",
						business: 'trainRC',
						entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
						fields:['xcID','xcName'],
						fieldLabel: "修程",
						hiddenName: "repairClassIdx", 
						displayField: "xcName",
						valueField: "xcID",
						pageSize: 20, minListWidth: 200,
						queryHql: 'from UndertakeRc',
						width: 140,
						editable:false
					}
	            ]
	        }
	        ]
	    }],
	     buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	TrainWorkPlanView.searchParam = TrainWorkPlanView.searchForm.getForm().getValues();
			    TrainWorkPlanView.searchParam = MyJson.deleteBlankProp(TrainWorkPlanView.searchParam);
			    TrainWorkPlanView.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	TrainWorkPlanView.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("rc_comb_s").clearValue();
            	TrainWorkPlanView.searchParam = {};
			    TrainWorkPlanView.grid.store.load();
            }
        }]
	});
TrainWorkPlanView.saveForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: TrainWorkPlanView.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        defaults:{
			layout:"form", defaults: {
					xtype:"textfield", anchor: '95%', readOnly: true, style: 'background:none; border: none;'
				}
			},
	        items: [{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanView.labelWidth,
	            columnWidth: 0.33, 
	            items: [
		                    {xtype:'textfield',fieldLabel:'车型',name:"trainTypeShortName"},
		                    {xtype:'textfield',fieldLabel:'车型id',name:"trainTypeIdx",hidden:true}
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanView.labelWidth,
	            columnWidth: 0.33, 
	            items: [
					  {xtype:'textfield',fieldLabel:'车号',name:"trainNo"}
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanView.labelWidth,
	            columnWidth: 0.33, 
	            items: [
					{xtype:'textfield',fieldLabel:'修程',name:"repairClassName"},
		            {xtype:'textfield',fieldLabel:'修程id',name:"repairClassIdx",hidden:true}
	            ]
	        },
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:TrainWorkPlanView.labelWidth,
	            columnWidth: 1, 
	            items: [
	            	{
						xtype: 'compositefield', fieldLabel : '检修日期', combineErrors: false,
			        	items: [
				           { id:"beginTime_v",name: "beginTime",  xtype: "textfield", readOnly: true, style: 'background:none; border: none;',//format: "Y-m-d",  
							width: 150},
							{
					    	    xtype:'label', text: '至：'
						    },
							{ id:"endTime_v", name: "endTime", xtype: "textfield", readOnly: true, style: 'background:none; border: none;',//format: "Y-m-d",  
							width: 150}]
					}
	            ]
	        }
	        ]
	    }]
	});
TrainWorkPlanView.saveWin = new Ext.Window({title:"下车配件登记情况",
				width:850, height:600,
				plain: true, maximized:true, modal:true,
				closeAction: 'hide', 
				layout:"border",
				items:[{
					region:"north", height: 100,
					baseCls:"x-plain", border:false,		
					items:[{
						baseCls:"x-plain", border:false,		
						items:TrainWorkPlanView.saveForm
					}]
				}, {
					// 【检修检测项】表格
					region:"center",
					layout:"fit",
					baseCls:"x-plain", border:false,	
					items:[{
				        xtype: "tabpanel", border: false, activeTab:0, enableTabScroll:true, items:[{
				            title: "未登记", layout: "fit", border: false, items: [PartsUnloadRegisterWait.grid]
				        },{
			            	title: "已登记", layout: "fit", border: false, items: [PartsUnloadRegisterEd.grid]
			        	}]
					}]
				}],
				buttonAlign: 'center',
	            buttons: [{
	                text: "关闭", iconCls: "closeIcon", scope: this, 
	                handler: function(){ 
	                	TrainWorkPlanView.saveWin.hide(); 
	                	TrainWorkPlanView.grid.store.reload();
	                }
	            }]
	})
TrainWorkPlanView.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsUnloadRegister!findWorkPlanListWeb.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsUnloadRegister!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsUnloadRegister!logicDelete.action',            //删除数据的请求URL
    viewConfig : [],
    singleSelect : true,
	saveForm: TrainWorkPlanView.saveForm,
	saveWin: TrainWorkPlanView.saveWin,
    tbar: [{
	    	text:"登记情况", iconCls:"configIcon", handler: function(){
	    		if(!$yd.isSelectedRecord(TrainWorkPlanView.grid)) {
					return;
				}
				var sm = TrainWorkPlanView.grid.getSelectionModel();
			    if (sm.getCount() < 1) {
			        MyExt.Msg.alert("尚未选择一条记录！");
			        return;
			    }
			    if (sm.getCount() > 1) {
			        MyExt.Msg.alert("只能选择一条记录！");
			        return;
			    }
			    var data = sm.getSelections();
				var record = data[0];
				TrainWorkPlanView.grid.saveWin.show();
				TrainWorkPlanView.saveForm.getForm().reset();
	       	    TrainWorkPlanView.saveForm.getForm().loadRecord(record);
	       	    TrainWorkPlanView.rdpIdx = record.get("idx");
				if(record.get("beginTime") != "" && record.get("beginTime") != null) Ext.getCmp("beginTime_v").setValue(record.get("beginTime").format("Y-m-d"));
				if(record.get("endTime") != "" && record.get("endTime") != null) Ext.getCmp("endTime_v").setValue(record.get("endTime").format("Y-m-d"));
		        PartsUnloadRegisterWait.grid.store.load();
		        PartsUnloadRegisterEd.grid.store.load();
	    	}
	    },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', width:80,editor:{  maxLength:50 },sortable:false
	},{
		header:'车号', dataIndex:'trainNo', width:80,editor:{  maxLength:50 },sortable:false
	},{
		header:'修程', dataIndex:'repairClassName',width:80, editor:{  maxLength:50 },sortable:false
	},{
		header:'检修日期', dataIndex:'beginTime', width:150,xtype:'datecolumn', editor:{ xtype:'my97date' },sortable:false
	},{
		header:'登记情况', dataIndex:'num', width:150,editor:{  maxLength:200 },sortable:false
	},{
		header:'状态', dataIndex:'workPlanStatus',editor:{  maxLength:20 },sortable:false,
		renderer : function(v){
				if(v == rdp_status_new) return "未启动";
				if(v == rdp_status_handling)return "处理中";
				if(v == rdp_status_handled) return "已处理";
				if(v == rdp_status_nullify)return "终止";
				return "错误！未知的工单状态：" + v;
			}
	},{
		header:'车型id', dataIndex:'trainTypeIdx', hidden:true,editor:{  maxLength:50 }
	},{
		header:'修程id', dataIndex:'repairClassIdx', hidden:true,editor:{  maxLength:50 }
	},{
		header:'修次id', dataIndex:'repairtimeIdx', hidden:true,editor:{  maxLength:50 }
	},{
		header:'修次', dataIndex:'repairtimeName', hidden:true,editor:{  maxLength:50 }
	},{
		header:'实际完成时间', dataIndex:'endTime',xtype:'datecolumn', hidden:true,editor:{  maxLength:50 }
	}],
	afterShowEditWin: function(record, rowIndex){
		TrainWorkPlanView.rdpIdx = record.get("idx");
		if(record.get("beginTime") != "" && record.get("beginTime") != null) Ext.getCmp("beginTime_v").setValue(record.get("beginTime").format("Y-m-d"));
		if(record.get("endTime") != "" && record.get("endTime") != null) Ext.getCmp("endTime_v").setValue(record.get("endTime").format("Y-m-d"));
        PartsUnloadRegisterWait.grid.store.load();
        PartsUnloadRegisterEd.grid.store.load();
	}
});
//查询前添加过滤条件
TrainWorkPlanView.grid.store.on('beforeload',function(){
	TrainWorkPlanView.searchParam = TrainWorkPlanView.searchForm.getForm().getValues();
	TrainWorkPlanView.searchParam = MyJson.deleteBlankProp(TrainWorkPlanView.searchParam);
	var searchParam = TrainWorkPlanView.searchParam;
    this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
var viewport=new Ext.Viewport({layout:'fit',items:[{
	    layout:'border',frame:true,
	    items:[{
	       region:'north',
	       collapsible :true,
	       title:'查询',
		   height:140,
	       frame:true,
	       items:[TrainWorkPlanView.searchForm]
	    },{
	      region:'center',
	      frame:true,
	      layout:'fit',
	      items:[TrainWorkPlanView.grid]
	    }]
		
	}]});
});