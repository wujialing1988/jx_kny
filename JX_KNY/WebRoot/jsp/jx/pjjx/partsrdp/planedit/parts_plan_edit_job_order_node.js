Ext.onReady(function(){
	
	if(typeof(PartsPlanEdit) === 'undefined')
		Ext.ns("PartsPlanEdit");
	
	var recordGrid, jobOrderGrid, stationGrid, rdpNodeIdx, tab;
	
	var loaded;
	
	function initLoad(){
		if(loaded){
			loaded.t1 = false;
			loaded.t2 = false;
			loaded.t3 = false;
		} else
			loaded = {t1: false, t2: false, t3: false};
	}
	
	
	function createRecordGrid(){
		recordGrid = new Ext.yunda.Grid({
			loadURL: ctx + "/partsRdpRecordCard!pageQuery.action",
			storeAutoLoad: false,
			tbar:[],
			fields: [{
				dataIndex: "idx", hidden:true
			},{
				header: "记录卡编号", dataIndex: "recordCardNo"
			},{
				header: "工单描述", dataIndex: "recordCardDesc"
			},{
				header: "状态", dataIndex: "status"
			},{
				header: "作业人员", dataIndex: "workEmpName"
			},{
				header: "质量检验人员", dataIndex: "handleEmpName"
			}],
			toEditFn: notEidt
		});
		recordGrid.store.on("beforeload", function(){
			var whereList = [{propName: "rdpNodeIDX", propValue: rdpNodeIdx, stringLike: false}];
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
		
		recordGrid.on("render", function(){
			this.tbar.dom.parentNode.removeChild(this.tbar.dom);
			this.doLayout();
		});
	}
	
	function createJobOrderGrid(){
		jobOrderGrid = new Ext.yunda.Grid({
			loadURL: ctx + "/partsRdpTecCard!pageQuery.action",
			storeAutoLoad: false,
			tbar:[],
			fields: [{
				dataIndex: "idx", hidden:true
			},{
				header: "工艺卡编号", dataIndex: "tecCardNo"
			},{
				header: "工单名称", dataIndex: "tecCardDesc"
			},{
				header: "状态", dataIndex: "status"
			},{
				header: "作业人员", dataIndex: "workEmpName"
			}],
			toEditFn: notEidt
		});
		jobOrderGrid.store.on("beforeload", function(){
			var whereList = [{propName: "rdpNodeIDX", propValue: rdpNodeIdx, stringLike: false}];
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
		jobOrderGrid.on("render", function(){
			this.tbar.dom.parentNode.removeChild(this.tbar.dom);
			this.doLayout();
		});
	}
	
	function createStationGrid(){
		stationGrid = new Ext.yunda.Grid({
			loadURL: ctx + "/partsRdpNodeStation!pageQuery.action",
			storeAutoLoad: false,
			tbar: [{
				text: "选择工位",
				iconCls: "addIcon",
				handler: function(){
					PartsWorkStation.showSelect(rdpNodeIdx, request);
				}
			}, "delete"],
			fields: [{
				dataIndex: "idx", hidden:true
			},{
				header: "工位编码", dataIndex: "workStationCode"
			},{
				header: "工位名称", dataIndex: "workStationName"
			},{
				header: "流水线名称", dataIndex: "repairLineName"
			}],
			toEditFn: notEidt
		});
		stationGrid.store.on("beforeload", function(){
			var whereList = [{propName: "rdpNodeIdx", propValue: rdpNodeIdx, stringLike: false}];
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
	}
	
	function request(datas, grid){
		Ext.Ajax.request({
			url: ctx + "/partsRdpNodeStation!batchSave.action",
			jsonData: datas,
			success: function(r){
				var e = Ext.util.JSON.decode(r.responseText);
				if(e.success){
					alertSuccess();
					grid.store.reload();
					stationGrid.store.reload();
				}else{
					alertFail(e.errMsg);
				}
			},
			failure: function(){
				alertFail("请求超时！");
			}
		});
	}
	
	function createTab(){
		createStationGrid();
		createJobOrderGrid();
		createRecordGrid();
		initLoad();
		tab = new Ext.TabPanel({
			activeTab: 0,
			items: [{
				title: "检修记录工单",
				sgrid: "t1",
				layout:"fit",
				items: recordGrid
			},{
				title: "检修作业工单",
				sgrid: "t2",
				layout:"fit",			
				items: []
			},{
				title: "作业工位",
				sgrid: "t3",
				layout:"fit",			
				items: []
			}],
			listeners: {
				tabchange: function(tab, panel){
					if(panel.items.length == 0){
						if(panel.title.charAt(0) === '检'){
							panel.add(jobOrderGrid);
						}else{
							panel.add(stationGrid);
						}
						panel.doLayout();
					}
					if(loaded[panel.sgrid] == false){
						panel.items.items[0].store.load();
						loaded[panel.sgrid] = true;
					}
					panel.doLayout();
				}
			}
		});
		return tab;
	}
	
	PartsPlanEdit.createNodeTabs = createTab;
	
	PartsPlanEdit.loadNodeGrid = function(_rdpNodeIdx, reload){
		rdpNodeIdx = _rdpNodeIdx;
		if(reload){
			tp = tab.getActiveTab();
			tp.items.items[0].store.load();
			initLoad();
			loaded[tp.sgrid] = true;
		}
	}
});