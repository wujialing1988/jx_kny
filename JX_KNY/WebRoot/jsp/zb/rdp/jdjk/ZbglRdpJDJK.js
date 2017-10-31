/**
 * 机车整备合格交验 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglRdpJDJK');                       //定义命名空间
ZbglRdpJDJK.searchParam = {};
ZbglRdpJDJK.searchLabelWidth = 90;
ZbglRdpJDJK.searchAnchor = '95%';
ZbglRdpJDJK.searchFieldWidth = 270;
/*** 机车整备合格交验查询表单 start ***/
ZbglRdpJDJK.searchParam = {};
ZbglRdpJDJK.rdpStatus = STATUS_HANDLING;
ZbglRdpJDJK.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:5px" ,
	labelWidth: ZbglRdpJDJK.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdpJDJK.searchFieldWidth, labelWidth: ZbglRdpJDJK.searchLabelWidth, defaults:{anchor:ZbglRdpJDJK.searchAnchor},
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
		                var trainNo_comb = ZbglRdpJDJK.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: ZbglRdpJDJK.searchFieldWidth, labelWidth: ZbglRdpJDJK.searchLabelWidth, defaults:{anchor:ZbglRdpJDJK.searchAnchor},
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
			fieldWidth: ZbglRdpJDJK.searchFieldWidth, labelWidth: ZbglRdpJDJK.searchLabelWidth, defaults:{anchor:ZbglRdpJDJK.searchAnchor},
			items:[{
				xtype: "textfield", name: 'dName', fieldLabel: '配属段'
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = ZbglRdpJDJK.searchForm.getForm();
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				ZbglRdpJDJK.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = ZbglRdpJDJK.searchForm;
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
                var trainNo_comb = form.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeShortName;
                trainNo_comb.cascadeStore();
            	searchParam = {};
            	ZbglRdpJDJK.grid.searchFn(searchParam);
            }
		}]
});
/*** 机车整备合格交验查询表单 end ***/
/*** 机车整备合格交验列表 start ***/
ZbglRdpJDJK.setColor = function(handledCount, allCount, value) {
	var value = handledCount + '/' + allCount;
	if (handledCount < allCount)
		return '<font color=blue></font><span style="color:red">' + value + '</span>';
	return value;	 
}
ZbglRdpJDJK.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglRdpJY!findRdpListForJY.action',                 //装载列表数据的请求URL
    viewConfig:null,
    singleSelect: true,
    tbar:[{
    	text:"查看", iconCls:"addIcon", handler: function() {
    		var grid = ZbglRdpJDJK.grid;
    		if(!$yd.isSelectedRecord(grid)) return;
    		var data = grid.selModel.getSelections();
    		if (data.length > 1) {
    			MyExt.Msg.alert("只能选择一条记录");
	        	return;	        	
    		}
    		if (data[0].get("rdpStatus") == STATUS_HANDLING){
    			ZbglRdpJDJKWin.editWin(data[0].data);   
    			}
    		else if (data[0].get("rdpStatus") == STATUS_HANDLED)
    			ZbglRdpJDJKWin.searchWin(data[0].data);  
    	}
    },'refresh','-',
	{   
    	xtype:"checkbox",name:"isJY", 
    	boxLabel:'未交验'+"&nbsp;&nbsp;&nbsp;&nbsp;",
    	id: 'STATUS_HANDLING',inputValue:STATUS_HANDLING,
    	checked:true, 
	    handler: function(){
	    	ZbglRdpJDJK.checkQuery();
//	    	ZbglRdpJDJK.grid.getTopToolbar().get(0).setText("交验");
    		ZbglRdpJDJK.grid.store.load();
	    }
    },
    {   
    	xtype:"checkbox",name:"isJY", boxLabel:'已交验', 
    	id: 'STATUS_HANDLED',inputValue:STATUS_HANDLED,
    	handler: function(){
	    	ZbglRdpJDJK.checkQuery();
//	    	ZbglRdpJDJK.grid.getTopToolbar().get(0).setText("查看");
    		ZbglRdpJDJK.grid.store.load();
	    }
	}],
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
		header:'检修类型', dataIndex:'repairClass', width: 80,
        renderer: function(v){
            switch(v){
            	case REPAIRCLASS_SX:
                    return "碎修";
                case REPAIRCLASS_LX:
                    return "临修";
                default:
                    return v;
            }
        }
	},{
		header:'检查任务', dataIndex:'rdpCount', width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledRdpCount");
			var allCount = record.get("allRdpCount");
			return ZbglRdpJDJK.setColor(handledCount, allCount);
		}, sortable: false
	},{
		header:'提票任务', dataIndex:'tpCount', width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledTpCount");
			var allCount = record.get("allTpCount");
			return ZbglRdpJDJK.setColor(handledCount, allCount);
		}, sortable: false
	},{
		header:'普查整治任务', dataIndex:'pczzCount', width: 80,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
			var handledCount = record.get("handledPczzCount");
			var allCount = record.get("allPczzCount");
			return ZbglRdpJDJK.setColor(handledCount, allCount);
		}, sortable: false
	},{
		header:'整备后去向', dataIndex:'afterToGo', width: 80, sortable: false, menuDisabled : false
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50 }
	},{
		header:'trainAccessAccountIDX', dataIndex:'trainAccessAccountIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'rdpStatus', dataIndex:'rdpStatus', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'fromPersonId', dataIndex:'fromPersonId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'fromPersonName', dataIndex:'fromPersonName', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'toPersonId', dataIndex:'toPersonId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'toPersonName', dataIndex:'toPersonName', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'outOrder', dataIndex:'outOrder', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'remarks', dataIndex:'remarks', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'rdpEndTime', dataIndex:'rdpEndTime', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'handledRdpCount', dataIndex:'handledRdpCount', hidden:true
	},{
		header:'allRdpCount', dataIndex:'allRdpCount', hidden:true
	},{
		header:'handledTpCount', dataIndex:'handledTpCount', hidden:true
	},{
		header:'allTpCount', dataIndex:'allTpCount', hidden:true
	},{
		header:'handledPczzCount', dataIndex:'handledPczzCount', hidden:true
	},{
		header:'allPczzCount', dataIndex:'allPczzCount', hidden:true
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglRdpJDJK.searchParam = searchParam;
    	this.store.load();
    }
});
//公用checkbox查询方法
ZbglRdpJDJK.rdpStatus = STATUS_HANDLING;
//状态多选按钮
ZbglRdpJDJK.checkQuery = function(){
	ZbglRdpJDJK.rdpStatus = "-1";
	if(Ext.getCmp("STATUS_HANDLING").checked){
		ZbglRdpJDJK.rdpStatus = ZbglRdpJDJK.rdpStatus + "," + STATUS_HANDLING;
	} 
	if(Ext.getCmp("STATUS_HANDLED").checked){
		ZbglRdpJDJK.rdpStatus = ZbglRdpJDJK.rdpStatus + "," + STATUS_HANDLED;
	} 
	ZbglRdpJDJK.grid.store.load();
}
ZbglRdpJDJK.grid.store.on('beforeload', function() {
	ZbglRdpJDJK.searchParam.rdpStatus = ZbglRdpJDJK.rdpStatus;
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglRdpJDJK.searchParam);
});
/*** 机车整备合格交验列表 end ***/
/*** 界面布局 start ***/
ZbglRdpJDJK.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:5px;',
        collapsible:true, height: 130, bodyBorder: false,
        items:[ZbglRdpJDJK.searchForm], frame: true, title: "查询"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ ZbglRdpJDJK.grid ]
    }, {
		region: 'south', height: 165,
		items: VTrainAccessAccount.panel
	}]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ZbglRdpJDJK.panel });
});