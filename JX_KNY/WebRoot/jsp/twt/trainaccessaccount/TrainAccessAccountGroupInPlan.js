Ext.onReady(function(){

	if(typeof(TrainAccessAccountGroupInPlan) === 'undefined')
	Ext.ns("TrainAccessAccountGroupInPlan");
	
	TrainAccessAccountGroupInPlan.searchLabelWidth = 90;
	TrainAccessAccountGroupInPlan.searchAnchor = '70%';
	TrainAccessAccountGroupInPlan.searchFieldWidth = 270;
	TrainAccessAccountGroupInPlan.searchParam = {};
	
	TrainAccessAccountGroupInPlan.grid = new Ext.yunda.Grid({
		loadURL: ctx + "/jczlTrain!pageQuery.action",
		storeAutoLoad: true,
		isEdit: false,
		tbar: [{	
				xtype:'textfield', id:'query_train_no', enableKeyEvents:true, emptyText:'输入车型车号快速检索!', listeners: {
		    		keyup: function(filed, e) {
		    			if (TrainAccessAccountGroupInPlan.queryTimeout) {
		    				clearTimeout(TrainAccessAccountGroupInPlan.queryTimeout);
		    			}
		    			TrainAccessAccountGroupInPlan.queryTimeout = setTimeout(function(){
		    				TrainAccessAccountGroupInPlan.grid.store.load();
		    			}, 1000);					
		    		}
				}	
			},'-',{
			text: "添加明细",
			iconCls: "addIcon",
			handler:function(){
				var grid = TrainAccessAccountGroupInPlan.grid;
				var editGrid = TrainAccessAccountGroupInPlan.editGrid;
	    		if(!$yd.isSelectedRecord(grid)) return;//未选择记录，直接返回
	        	var data = grid.selModel.getSelections();
	        	for(var i = 0; i < data.length; i++){
	        		var d = data[i]; 
	        		grid.store.remove(d);
	        		
	        		//判断下面的编辑grid中是否有相同的数据，如果有提示
	        		//获取总条数
    				var recordCount = editGrid.store.getCount();
    				if(recordCount > 0){
	    				for(var i = 0;i<recordCount;i++){
	    					//获取每一行对象
	    					var recordV = editGrid.store.getAt(i);
	    					//比较唯一标示funccode
	    					if(recordV.data.trainNo == d.data.trainNo){
		    					MyExt.Msg.alert("表格中已经有相同的数据！");
								return;
	    					}
	    				}
    				}
	        		d.set("inTime", new Date()); 
	        		d.set("toGo",null);
		        	editGrid.store.add(d);
	        	}
			}
		}],
		fields: [{
			header: "车型", dataIndex: "trainTypeIDX", hidden: true
		},{
			header: "车型编码", dataIndex: "trainTypeShortName", width:150
		},{
			header: "车号", dataIndex: "trainNo", width:150
		}],
		searchFn: function(searchParam){
   			TrainAccessAccountGroupInPlan.searchParam = searchParam;
   			this.store.load();
  			}
	});
	
	// 取消表格双击进行编辑的事件监听
	TrainAccessAccountGroupInPlan.grid.un('rowdblclick', TrainAccessAccountGroupInPlan.grid.toEditFn, TrainAccessAccountGroupInPlan.grid);
	
		
	TrainAccessAccountGroupInPlan.grid.store.on("beforeload", function(){
		var searchParam = TrainAccessAccountGroupInPlan.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		var sqlStr = " concat(TRAIN_TYPE_IDX,TRAIN_NO) not in (select concat(TRAIN_TYPE_IDX,TRAIN_NO) from twt_train_access_account where in_time is not null and out_time is null and record_status = 0 and (train_type_idx is not null and train_no is not null)) " ;		
		whereList.push({sql: sqlStr, compare: Condition.SQL});
		// 模糊查询
		var queryKey = Ext.getCmp('query_train_no').getValue();
		if (!Ext.isEmpty(queryKey)) {
			var sql = " (TRAIN_TYPE_SHORTNAME LIKE '%" + queryKey + "%'"
				+ " OR TRAIN_NO LIKE '%" + queryKey + "%')";
			whereList.push({ sql: sql, compare: Condition.SQL});	
		}
		whereList.push({ propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ, stringLike: false});	
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	TrainAccessAccountGroupInPlan.editGrid = new Ext.yunda.EditGrid({
		storeAutoLoad: false,
		autoSubmit: false,
		isEdit: false,
		defaultTar: false,
		page: false,
		tbar: [{
			text:"删除明细",
			iconCls: "deleteIcon",
			handler: function(){
				var grid = TrainAccessAccountGroupInPlan.grid;
				var editGrid = TrainAccessAccountGroupInPlan.editGrid;
	    		if(!$yd.isSelectedRecord(editGrid)) return;//未选择记录，直接返回
	        	var data = editGrid.selModel.getSelections();
	        	for(var i = 0; i < data.length; i++){
	        		var d = data[i];
					//var record = new Ext.data.Record();
					//for(var i in d){
					//	if(!Ext.isEmpty(d[i]))
					//		record.set(i,d[i]);
					//} 
	        		editGrid.store.remove(d);  
	        		
	        		grid.store.add(d);
	        	}
			}
		}],
		fields: [{
			dataIndex: "idx", hidden: true
		},{
			header: "车型", dataIndex: "trainTypeShortName", width:150
		},{
			header: "车号", dataIndex: "trainNo", width:150
		},{
			header: "入段去向", dataIndex: "toGo",editor:{
			xtype: 'Base_comboTree',allowBlank: false,treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
			queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},hiddenName: 'toGo',
			//valueField : 'text',
			selectNodeModel: 'leaf',fieldLabel: '入段去向'},width:250,
			renderer : function(comboTreeID) {
				//通过字典表反写页面显示数据
				if(comboTreeID){				
					return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",comboTreeID);
				}else{
					return '';
				}
			}
			
		},{
			header: "入段时间", dataIndex: "inTime", editor:{
				xtype:"my97date", my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i", allowBlank: false,initNow:true
			}, xtype: "datecolumn", width: 150
		},{
			header: "计划出段时间", dataIndex: "planOutTime", editor:{
				xtype:"my97date", my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i", allowBlank: false
			}, xtype: "datecolumn", width: 150
		},{
			header:'客货类型', dataIndex:'vehicleType', hidden: true,editor:{ xtype:'hidden',value:vehicleType }
		}]
	});

	TrainAccessAccountGroupInPlan.showWin = function(_grid){
		win = new Ext.Window({
		   	title: "车辆批量入段",width:900, height:500, layout: "fit", 
			closeAction: "hide", modal: true , buttonAlign:"center",
			border: false,
			//resizable: false,
			items: [{
				xtype: "panel",
				layout: "border",
				items:[{
			        layout: "fit",
			        region : 'center',
			        items: TrainAccessAccountGroupInPlan.grid
				 },{
			        layout: "fit",
			        region : 'south',
			        frame: true,//如果没有这个属性，那么除开center的其他地方都不会显示比如south
			        height: 180,
			        items: TrainAccessAccountGroupInPlan.editGrid
				 }]
			}],
			buttons:[{
				text: "确认入段", iconCls: "saveIcon",
				handler: function(){
					var count = TrainAccessAccountGroupInPlan.editGrid.store.getCount();
					if(count == 0){
						MyExt.Msg.alert("尚未添加明细");
						return;
					}
					submit.call(this, count);
				}
			}, {
				text: "关闭", iconCls: "closeIcon", 
				handler: function(){
					win.hide();
					TrainAccessAccountGroupInPlan.editGrid.store.removeAll();
		        }
		    }]
		});
		
		win.show();
	};
	
	function submit(count){
		//判断提交数据是否有空值
		var submitFlag = true;
		var datas = [];
		for(var i = 0; i < count; i++){
			var r = MyJson.clone(TrainAccessAccountGroupInPlan.editGrid.store.getAt(i).data);
			
			//判断入段去向是否为空
			if(!r.toGo){
				MyExt.Msg.alert("入段车号为" + r.trainNo + "的入段去向未输入，请重新输入");
				return;
			}
			
			//判断入段时间是否为空
			if(r.inTime){
				r.inTime = r.inTime.format("Y-m-d H:i");
			}else{
				MyExt.Msg.alert("入段车号为" + r.trainNo + "的入段时间输入，请重新选择");
				return;
			}
			
			// 手动添加客货类型
			if(Ext.isEmpty(r.vehicleType)){
				r.vehicleType = vehicleType ;
			}
			
			datas.push(r);
		}
		var me = this;
		me.disable();
		Ext.Ajax.request({
			url: ctx + "/trainAccessAccount!saveOrUpdateListIn.action",
			jsonData: datas,
			success: function(r){
				var retn = Ext.util.JSON.decode(r.responseText);
				if(retn.success){
					alertSuccess();
					win.hide();
					self.location.reload();
				}else{
					alertFail(retn.errMsg);
				}
				me.enable();
			},
			failure: function(){
				alertFail("请求超时！");
				me.enable();
			}
		});
	}
	
});