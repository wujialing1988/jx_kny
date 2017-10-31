Ext.onReady(function(){
	
	if(typeof(NotStart) === 'undefined')
		Ext.ns('NotStart');
	
	function post(param, url){
		var me = this;
		this.disable();
		Ext.Ajax.request({
			url: ctx + url,
			params: param,
			success: function(r){
				var e = Ext.util.JSON.decode(r.responseText);
				if(e.success){
					alertSuccess();
					grid.store.reload();
					Started.grid.store.reload();
				}else{
					alertFail(e.errMsg);
				}
				me.enable();
			},
			failure: function(){
				alertFail("请求超时！");
				me.enable();
			}
		});
	}
	
	function beginOpen(idx){
		post.call(this, {idx: idx}, "/partsRdp!startPartsRdp.action");
	}
	
	function termination(idx){
		post.call(this, {rdpIdx: idx}, "/partsRdp!terminationPlan.action");
	}
	
	
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
		},{
			text: "新增作业计划",
			iconCls: "addIcon",
			handler: function(){
				NewPartsPlan.showWin(grid);
			}
		},{
			/*text: "作业计划编辑",
			iconCls: "editIcon",
			handler: function(){
				PartsPlanEdit.showWin();
			}
		},{*/
			text: "终止计划",
			iconCls: "deleteIcon",
			handler: function(){
				var record, me = this;
				var data = grid.selModel.getSelections();
				if (data.length == 0) {
					MyExt.Msg.alert("尚未选择一条记录！");
					return;
				}
				if (data.length > 1) {
					MyExt.Msg.alert("只能选择一条记录！");
					return;
				}
				record = data[0];
				Ext.Msg.confirm("提示", "确认终止计划", function(){
					termination.call(me, record.get("idx"));
				});
			}
		},{
			text: "启动生产",
			iconCls: "beginIcon",
			handler: function(){
				var record, me = this;
				var data = grid.selModel.getSelections();
				if (data.length == 0) {
					MyExt.Msg.alert("尚未选择一条记录！");
					return;
				}
				if (data.length > 1) {
					MyExt.Msg.alert("只能选择一条记录！");
					return;
				}
				record = data[0];
				Ext.Msg.confirm("提示", "确认启动生产", function(btn){
				if(btn != 'yes')    return;
					beginOpen.call(me, record.get("idx"));
				});
			}
		},'refresh'],
		fields:[{
			dataIndex: "idx", hidden: true
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
			header: "承修班组", dataIndex: "repairOrgID", hidden:true
		},{
			header: "承修班组", dataIndex: "repairOrgSeq", hidden:true
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
			var record = this.store.getAt(rowIndex);
			PartsPlanEdit.showWin(record);
		}
	});
	
	grid.store.on("beforeload", function(){
		var sp = MyJson.clone(this.sp || {});
		sp.status = NOT_START;
		var whereList = []; 
		for(prop in sp){
			if(prop == 'status'){
				whereList.push({propName:prop, propValue: sp[prop], stringLike: false}) ;
			}else{
	        	whereList.push({propName:prop, propValue: sp[prop]}) ;
			}
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	
	NotStart.grid = grid;
	
	NotStart.form = form;
});