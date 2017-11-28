/**
 * 客车 以走行公里计算
 */
Ext.onReady(function(){
	
	if(typeof(RepairStandard) === 'undefined')
		Ext.ns("RepairStandard");
	
	RepairStandard.trainTypeIdx;
	
	var grid = new Ext.yunda.Grid({
		loadURL: ctx + "/repairStandard!pageQuery.action",
		saveURL: ctx + "/repairStandard!saveOrUpdate.action",
		deleteURL: ctx + "/repairStandard!delete.action",
		storeAutoLoad: false,
		viewConfig: null,
		tbar:[{
			xtype: "Base_combo",
			id: "cb_xc_search_id",
			entity:'com.yunda.jx.base.jcgy.entity.XC',
			fields:['xcID','xcName'],
			hiddenName: "repairClass", 
			width: 100,
			returnField: [{widgetId:"xc_id",propertyName:"xcName"}],
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
				Ext.getCmp("cb_xc_search_id").clearValue();
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
				id: "cb_xc_id",
				entity:'com.yunda.jx.base.jcgy.entity.XC',
				fields:['xcID','xcName'],
				hiddenName: "repairClass", 
				returnField: [{widgetId:"xc_id",propertyName:"xcName"}],
				displayField: "xcName",
				valueField: "xcID",
				allowBlank:false,
				pageSize: 20, minListWidth: 200,
				queryParams: {xcType: "NULL"},
				editable:false
			}
		},{
			header: "修程", dataIndex: "repairClassName", editor: {xtype: "hidden", id:"xc_id"}
		},{
			header: "修次", dataIndex: "repairOrder", editor: {
				xtype: "Base_combo",
				id: "cb_xci_id",
				entity:'com.yunda.jx.base.jcgy.entity.RT',
				fields:['rtID','rtName'],
				hiddenName: "repairOrder", 
				allowBlank:false,
				returnField: [{widgetId:"xci_id",propertyName:"rtName"}],
				displayField: "rtName",
				valueField: "rtID",
				pageSize: 20, minListWidth: 200,
				editable:false
			}, hidden:true
		},{
			header: "修次", dataIndex: "repairOrderName", editor: {xtype: "hidden", id:"xci_id"}
		},{
			header: "最小运行小时", dataIndex: "minRunningKm", editor: {
				xtype: "numberfield",
				allowBlank:false,
				id:"min_id",
				validator: function(){
					var val = Ext.getCmp("max_id").getValue();
					if(val){
						if(this.getValue() > val){
							return  "不能大于最大运行小时";
						}
					}
					
					if(/^((\d+)|(\d+\.?\d+))$/.test(this.getValue()) == false){
						return "不合法的输入";
					}
					val = String(this.getValue());
					var index = val.indexOf(".")
					if(index == -1 && val.length > 7){
						return "输入长度最大为7位";
					}else if(index > 7){
						return "小数点前最长为7位";
					}
				}
			}
		},{
			header: "最大运行小时", dataIndex: "maxRunningKm", editor: {
				xtype: "numberfield",
				allowBlank:false,
				id:"max_id",
				validator: function(){
					var val = Ext.getCmp("min_id").getValue();
					if(val){
						if(this.getValue() < val){
							return  "不能小于最小运行小时";
						}
					}
					if(/^((\d+)|(\d+\.?\d+))$/.test(this.getValue()) == false){
						return "不合法的输入";
					}
					val = String(this.getValue());
					var index = val.indexOf(".")
					if(index == -1 && val.length > 7){
						return "输入长度最大为7位";
					}else if(index > 7){
						return "小数点前最长为7位";
					}
				}
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
	        grid.saveForm.find("name", "trainTypeIdx")[0].setValue(RepairStandard.trainTypeIdx);
	    },
	    afterShowSaveWin: function(){
	    	this.saveForm.find("name", "trainTypeIdx")[0].setValue(RepairStandard.trainTypeIdx);
	    },
	    afterShowEditWin: function(r){
	    	Ext.getCmp("cb_xc_id").setDisplayValue(r.get("repairClass"), r.get("repairClassName"));
	    	Ext.getCmp("cb_xci_id").setDisplayValue(r.get("repairOrder"), r.get("repairOrderName"));
	    }
	});
	grid.store.setDefaultSort("minRunningKm", "asc");
	grid.store.on("beforeload", function(){
		if(!RepairStandard.trainTypeIdx){
			return false;
		}
		var whereList = [];
		var xc = Ext.getCmp("cb_xc_search_id").getValue();
		if(xc){
			whereList.push({propName: "repairClass", propValue:xc, stringLike: false});
		}
		
		whereList.push({propName: "trainTypeIdx", propValue:RepairStandard.trainTypeIdx, stringLike: false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	grid.on("render", function(){
		this.topToolbar.disable();
	});
	
	function initXc(){
		var cmp = Ext.getCmp("cb_xc_id");
		var searchCmp = Ext.getCmp("cb_xc_search_id");
		searchCmp.queryParams.vehicleType = vehicleType;
		searchCmp.cascadeStore();
		searchCmp.clearValue();
    	cmp.queryParams.vehicleType = vehicleType;
    	cmp.cascadeStore();
	}
	RepairStandard.grid = grid;
	RepairStandard.resetXC = initXc;
});