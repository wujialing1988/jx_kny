Ext.onReady(function(){
	
	if(typeof(Started) === 'undefined')
		Ext.ns('Started');
	
	var form = createSearchForm();
	
	var grid = new Ext.yunda.Grid({
		loadURL: ctx + "/partsRdp!pageQuery.action",
		viewConfig: null,
		tbar: [{
			text: "查询",
			iconCls: "searchIcon",
			handler: function(){
				grid.store.sp = form.getForm().getValues();
				grid.store.load();
			}
		},'refresh'/*,{
			text: "生产进度查看",
			iconCls: "see2Icon",
			handler: function(){
				
			}
		}*/],
		fields:[{
			header: "", dataIndex: "idx", hidden: true, editor: {xtype: "hidden"}
		},{
			header: "配件编号", dataIndex: "partsNo", width: 130
		},{
			header: "配件识别码", dataIndex: "identificationCode", width: 130
		},{
			header: "配件名称", dataIndex: "partsName", width: 130
		},{
			header: "规格型号", dataIndex: "specificationModel", width: 140
		},{
			header: "物料编码", dataIndex: "matCode", width: 130
		},{
			header: "下车车型", dataIndex: "unloadTrainType", width: 70
		},{
			header: "下车车号", dataIndex: "unloadTrainNo", width: 70
		},{
			header: "下车修程", dataIndex: "unloadRepairClass", width: 70
		},{
			header: "下车修次", dataIndex: "unloadRepairTime", width: 70
		},{
			header: "承修班组", dataIndex: "repairOrgName", width: 100
		},{
			header: "partsTypeIDX", dataIndex: "partsTypeIDX", hidden:true
		},{
			header: "wpDesc", dataIndex: "wpDesc", hidden:true
		},{
			header: "wpIdx", dataIndex: "wpIDX", hidden:true
		},{
			header: "calendarName", dataIndex: "calendarName", hidden:true
		},{
			header: "calendarIdx", dataIndex: "calendarIdx", hidden:true
		},{
			header: "计划开始时间", dataIndex: "planStartTime", xtype: "datecolumn",
			editor: {
				xtype: "my97date",
				my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"},
				format: "Y-m-d H:i"
			}, width: 120
		},{
			header: "计划完成时间", dataIndex: "planEndTime", xtype: "datecolumn",
			editor: {
				xtype: "my97date",
				my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"},
				format: "Y-m-d H:i"
			}, width: 120
		}],
		toEditFn: function(g, rowIndex){
//			var record = this.store.getAt(rowIndex);
//			PartsPlanEdit.showWin(record);
		}
	});
	
	grid.store.on("beforeload", function(){
		var sp = MyJson.clone(this.sp || {});
		sp.status = STARTED;
		var whereList = []; 
		for(prop in sp){
			whereList.push({propName: prop, propValue: sp[prop]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	Started.grid = grid;
	Started.form = form;
});