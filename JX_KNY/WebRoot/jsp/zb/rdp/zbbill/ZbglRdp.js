/**
 * 机车整备单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglRdp');                       //定义命名空间
ZbglRdp.searchLabelWidth = 90;
ZbglRdp.searchAnchor = '95%';
ZbglRdp.searchFieldWidth = 270;
/*** 碎修查询表单 start ***/
ZbglRdp.SXSearchParam = {};
ZbglRdp.SXSearchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: ZbglRdp.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdp.searchFieldWidth, labelWidth: ZbglRdp.searchLabelWidth, defaults:{anchor:ZbglRdp.searchAnchor},
			items:[{ 
				fieldLabel: "车型",
				hiddenName: "trainTypeShortName",
				displayField: "shortName", valueField: "shortName",
				pageSize: 0, minListWidth: 200,
				editable:true,
				forceSelection: true,
				xtype: "Base_combo",
	        	business: 'trainType',													
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = ZbglRdp.SXSearchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdp.searchFieldWidth, labelWidth: ZbglRdp.searchLabelWidth, defaults:{anchor:ZbglRdp.searchAnchor},
			items:[{
				id: "trainNo_comb_search",
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				maxLength : 4,
				vtype: "numberInt",
				anchor: "95%", 				
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
				{name:"leaveDate", type:"date", dateFormat: 'time'},
				"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
				"bId","dId","bName","dName","bShortName","dShortName"],
				queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
				isAll: 'yes',
				editable:true
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.34,
			fieldWidth: ZbglRdp.searchFieldWidth, labelWidth: ZbglRdp.searchLabelWidth, defaults:{anchor:ZbglRdp.searchAnchor},
			items:[{
				xtype: "textfield", name: 'dname', fieldLabel: '配属段'
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglRdp.SXSearchForm.getForm();
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				ZbglRdp.SXGrid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglRdp.SXSearchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
                var trainNo_comb = ZbglRdp.SXSearchForm.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeShortName;
                trainNo_comb.cascadeStore();
            	searchParam = {};
            	ZbglRdp.SXGrid.searchFn(searchParam);
            }
		}]
});
/*** 碎修查询表单 end ***/
/*** 碎修列表 start ***/
ZbglRdp.SXGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglRdpTempRepair!findSXRdpList.action',                 //装载列表数据的请求URL
    viewConfig:null,
    singleSelect: true,
    tbar:[{
    	text:"转临修", iconCls:"addIcon", handler: function() {
    		var grid = ZbglRdp.SXGrid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var data = grid.selModel.getSelections();
    		if (data.length > 1) {
    			MyExt.Msg.alert("只能选择一条记录");
	        	return;
    		}
    		TempRepairWin.showWin(data[0].data);
    	}
    },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'配属段', dataIndex:'dname', editor:{  maxLength:50 }
	},{
		header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{ xtype:'hidden' },
		searcher:{disabled: true}, width: 150
	},{
		header:'入段去向', dataIndex:'toGo', width: 80,
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		}
	},{
		header:'整备开始时间', dataIndex:'rdpStartTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width: 150
	},{
		header:'检查任务', dataIndex:'rdpCount', width: 80, sortable: false
	},{
		header:'提票任务', dataIndex:'tpCount', width: 80, sortable: false
	},{
		header:'普查整治任务', dataIndex:'pczzCount', width: 80, sortable: false
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50 }
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglRdp.SXSearchParam = searchParam;
    	this.store.load();
    }
});
ZbglRdp.SXGrid.store.on('beforeload', function() {
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglRdp.SXSearchParam);
});
/*** 碎修列表 end ***/
/*** 临修查询表单 start ***/
ZbglRdp.LXSearchParam = {};
ZbglRdp.LXSearchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: ZbglRdp.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdp.searchFieldWidth, labelWidth: ZbglRdp.searchLabelWidth, defaults:{anchor:ZbglRdp.searchAnchor},
			items:[{ 
				fieldLabel: "车型",
				hiddenName: "trainTypeShortName",
				displayField: "shortName", valueField: "shortName",
				pageSize: 0, minListWidth: 200,
				editable:true,
				forceSelection: true,
				xtype: "Base_combo",
	        	business: 'trainType',													
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = ZbglRdp.LXSearchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdp.searchFieldWidth, labelWidth: ZbglRdp.searchLabelWidth, defaults:{anchor:ZbglRdp.searchAnchor},
			items:[{
				id: "trainNo_comb_search1",
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				maxLength : 4,
				vtype: "numberInt",
				anchor: "95%", 				
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
				{name:"leaveDate", type:"date", dateFormat: 'time'},
				"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
				"bId","dId","bName","dName","bShortName","dShortName"],
				queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
				isAll: 'yes',
				editable:true
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.34,
			fieldWidth: ZbglRdp.searchFieldWidth, labelWidth: ZbglRdp.searchLabelWidth, defaults:{anchor:ZbglRdp.searchAnchor},
			items:[{
				xtype: "textfield", name: 'dname', fieldLabel: '配属段'
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglRdp.LXSearchForm.getForm();
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search1").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search1").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				ZbglRdp.LXGrid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglRdp.LXSearchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
                var trainNo_comb = ZbglRdp.LXSearchForm.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeShortName;
                trainNo_comb.cascadeStore();
            	searchParam = {};
            	ZbglRdp.LXGrid.searchFn(searchParam);
            }
		}]
});
/*** 临修查询表单 end ***/
/*** 临修列表 start ***/
ZbglRdp.LXGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglRdpTempRepair!findLXRdpList.action',                 //装载列表数据的请求URL
    viewConfig:null,
    tbar:['refresh'],
    singleSelect: true,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'配属段', dataIndex:'dname', editor:{  maxLength:50 }
	},{
		header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{ xtype:'hidden' },
		searcher:{disabled: true}, width: 150
	},{
		header:'入段去向', dataIndex:'toGo', width: 80,
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		}
	},{
		header:'整备开始时间', dataIndex:'rdpStartTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width: 150
	},{
		header:'检查任务', dataIndex:'rdpCount', width: 80, sortable: false
	},{
		header:'提票任务', dataIndex:'tpCount', width: 80, sortable: false
	},{
		header:'普查整治任务', dataIndex:'pczzCount', width: 80, sortable: false
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50 }
	},{
		header:'转临修人', dataIndex:'handlePersonName', width: 80, sortable: false
	},{
		header:'转临修时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width: 150, sortable: false
	},{
		header:'转临修原因', dataIndex:'handleReason', width: 180, sortable: false
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglRdp.LXSearchParam = searchParam;
    	this.store.load();
    }
});
ZbglRdp.LXGrid.store.on('beforeload', function() {
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglRdp.LXSearchParam);
});
/*** 临修列表 end ***/
});