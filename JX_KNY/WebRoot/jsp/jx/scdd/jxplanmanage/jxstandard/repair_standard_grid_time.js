/**
 * 客车 以走行公里计算
 */
Ext.onReady(function(){
	
	if(typeof(RepairStandardTime) === 'undefined')
		Ext.ns("RepairStandardTime");
	
	RepairStandardTime.trainTypeIdx;
	
	var grid = new Ext.yunda.Grid({
		loadURL: ctx + "/repairStandardTime!pageQuery.action",
		saveURL: ctx + "/repairStandardTime!saveOrUpdate.action",
		deleteURL: ctx + "/repairStandardTime!delete.action",
		storeAutoLoad: false,
		viewConfig: null,
		tbar:[{
			xtype: "Base_combo",
			id: "cb_xc_search_time_id",
			entity:'com.yunda.jx.base.jcgy.entity.XC',
			fields:['xcID','xcName'],
			hiddenName: "repairClass", 
			width: 100,
			returnField: [{widgetId:"xc_time_id",propertyName:"xcName"}],
			displayField: "xcName",
			valueField: "xcID",
			pageSize: 20, minListWidth: 200,
			queryParams: {vehicleType: "NULL"},
			editable:false,
			listeners:{
				"select":function(){
					grid.store.reload();
				}
			}
		},{
			text: "重置",
			iconCls: "resetIcon",
			handler: function(){
				Ext.getCmp("cb_xc_search_time_id").clearValue();
				grid.store.reload();
			}
		}, "add", "delete", "refresh"],
		fields: [{
			dataIndex: "idx", hidden:true, editor: {xtype: "hidden"}
		},{
			dataIndex: "trainTypeIdx", hidden:true, editor: {xtype: "hidden"}
		},{
			header: "修程", dataIndex: "repairClass", hidden:true, editor: {
				xtype: "Base_combo",
				id: "cb_xc_time_id",
				entity:'com.yunda.jx.base.jcgy.entity.XC',
				fields:['xcID','xcName'],
				hiddenName: "repairClass", 
				returnField: [{widgetId:"xc_time_id",propertyName:"xcName"}],
				displayField: "xcName",
				valueField: "xcID",
				allowBlank:false,
				pageSize: 20, minListWidth: 200,
				queryParams: {xcType: "NULL"},
				editable:false
			}
		},{
			header: "修程", dataIndex: "repairClassName", editor: {xtype: "hidden", id:"xc_time_id"}
		},{
			header: "对比修程", dataIndex: "repairClassCompare", hidden:true, editor: {
				xtype: "Base_combo",
				id: "cb_xc_compare_id",
				entity:'com.yunda.jx.base.jcgy.entity.XC',
				fields:['xcID','xcName'],
				hiddenName: "repairClassCompare", 
				returnField: [{widgetId:"xc_compare_id",propertyName:"xcName"}],
				displayField: "xcName",
				valueField: "xcID",
				allowBlank:false,
				pageSize: 20, minListWidth: 200,
				queryParams: {xcType: "NULL"},
				editable:false
			}
		},{
			header: "对比修程", dataIndex: "repairClassCompareName", editor: {xtype: "hidden", id:"xc_compare_id"}
		},{
			header: "对比天数", dataIndex: "compareDay", editor: {
				xtype: "numberfield",
				allowBlank:false
			}
		}],
		createSaveWin: function(){
	        if(this.saveForm == null){
	        	this.createSaveForm();
	        }
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
	            title:"新增", width:this.saveWinWidth, height:this.saveWinHeight, plain:true, closeAction:"hide",
	            buttonAlign:'center', maximizable:true, items:this.saveForm, autoHeight:true, modal: true,
	            buttons: [{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: this.saveFn
	            }, {
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
	            }]
	        });
	        grid.saveForm.find("name", "trainTypeIdx")[0].setValue(RepairStandardTime.trainTypeIdx);
	    },
	    afterShowSaveWin: function(){
	    	this.saveForm.find("name", "trainTypeIdx")[0].setValue(RepairStandardTime.trainTypeIdx);
	    },
	    afterShowEditWin: function(r){
	    	Ext.getCmp("cb_xc_time_id").setDisplayValue(r.get("repairClass"), r.get("repairClassName"));
	    	Ext.getCmp("cb_xc_compare_id").setDisplayValue(r.get("repairClassCompare"), r.get("repairClassCompareName"));
	    }
	});
	grid.store.on("beforeload", function(){
		if(!RepairStandardTime.trainTypeIdx){
			return false;
		}
		var whereList = [];
		var xc = Ext.getCmp("cb_xc_search_time_id").getValue();
		if(xc){
			whereList.push({propName: "repairClass", propValue:xc, stringLike: false});
		}
		
		whereList.push({propName: "trainTypeIdx", propValue:RepairStandardTime.trainTypeIdx, stringLike: false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	grid.on("render", function(){
		this.topToolbar.disable();
	});
	
	function initXc(){
		var cmp = Ext.getCmp("cb_xc_time_id");
		var Compare = Ext.getCmp("cb_xc_compare_id");
		var searchCmp = Ext.getCmp("cb_xc_search_time_id");
		searchCmp.queryParams.vehicleType = vehicleType;
		searchCmp.cascadeStore();
		searchCmp.clearValue();
    	cmp.queryParams.vehicleType = vehicleType;
    	cmp.cascadeStore();
    	Compare.queryParams.vehicleType = vehicleType;
    	Compare.cascadeStore();
	}
	RepairStandardTime.grid = grid;
	RepairStandardTime.resetXC = initXc;
});