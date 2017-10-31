Ext.onReady(function(){
	
	Ext.namespace("TrainResume");
	
	TrainResume.searchParam={};
	
	TrainResume.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/trainResume!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/trainResume!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/trainResume!logicDelete.action',            //删除数据的请求URL
	    saveFormColNum:2, searchFormColNum:1,	 singleSelect:true,
	    tbar:["search","refresh"],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'事件名称', dataIndex:'eventName', editor:{ xtype:"textfield"}
		},{
			header:"事件时间", dataIndex:"eventTime", xtype:'datecolumn', editor:{ xtype:'my97date' }, searcher:{ initNow:false }
		},{
			header:"操作", dataIndex:"code", width:100, editor:{},
			renderer:function(){
				return "<a href=\"javascript:void(0)\" onclick=\"TrainResume.showEventInfo = true;\">详情</a>";
			},searcher:{ disabled:true }
		}],
		searchFn: function(searchParam){ 
			TrainResume.searchParam = searchParam ;
			TrainResume.grid.store.load();
		}
	});
	//查询前添加过滤条件
	TrainResume.grid.store.on('beforeload' , function(){		
		var searchParam = TrainResume.searchParam;
		searchParam.trainTypeIDX = JczlTrain.trainTypeIdx;
		searchParam.trainNo = JczlTrain.trainNo;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	TrainResume.showEventInfo = false;
	TrainResume.saveWindow = null;//保存前一个窗口，后一个窗口打开时隐藏它
	TrainResume.showWindow = function(grid,rowIndex,e){	
		if(TrainResume.showEventInfo){
			var eventRecord = grid.store.getAt(rowIndex);
			if(TrainResume.saveWindow){
				TrainResume.saveWindow.hide();
			}
			switch(eventRecord.get("code")){
				case "1"://破损
					var record = TrainDamagedAccount.grid.store.getById(eventRecord.get("idx"));
					if(!record){
						record = getRecord("trainDamagedAccount",eventRecord.get("idx"),
								TrainDamagedAccount.grid);
					}else{
						TrainResume.toEditFunc(TrainDamagedAccount.grid,record);
					}
					break;
				case "2"://检修
					var record = RepairAccount.grid.store.getById(eventRecord.get("idx"));
					if(!record){
						record = getRecord("repairAccount",eventRecord.get("idx"),
								RepairAccount.grid);
					}else{
						TrainResume.toEditFunc(RepairAccount.grid,record);
					}
					break;
				case "3"://配属
					var record = TrainTransferDetail.grid.store.getById(eventRecord.get("idx"));
					if(!record){
						record = getRecord("trainTransferDetail",eventRecord.get("idx"),
								TrainTransferDetail.grid);
					}else{
						TrainResume.toEditFunc(TrainTransferDetail.grid,record);
					}
					break;
			}
			TrainResume.showEventInfo = false;
		}
	}
	
	TrainResume.grid.un('rowdblclick', TrainResume.grid.toEditFn, TrainResume.grid);
	TrainResume.grid.on("rowclick", TrainResume.showWindow, TrainResume.grid);
	
	TrainResume.toEditFunc = function(grid,record){		
        //判断新增编辑窗体是否为null，如果为null则自动创建后显示
        if(grid.saveWin == null)  grid.createSaveWin();
        //是否显示编辑窗口，中止或继续编辑动作
        if(!grid.beforeShowEditWin(record, 0))     return;
        
	        grid.saveWin.show();
	        grid.saveForm.getForm().reset();
	        grid.saveForm.getForm().loadRecord(record);
         
        TrainResume.saveWindow = grid.saveWin;
        //显示编辑窗口后触发函数（可能需要对某些特殊控件赋值等操作）
        grid.afterShowEditWin(record, 0);        
    }
	function getRecord(action,idx,grid){
		var record = new Ext.data.Record();
		Ext.Ajax.request({
	        url: ctx + "/" + action + "!searchEntity.action?idx="+idx,
	        async:true,
	        success: function(response, options){
	        	var result = Ext.util.JSON.decode(response.responseText);
	            var entity = result.entity;
	            var ts = jsonField(entity);	            
	            for(var i = 0; i< ts.length;i++){	            	
	            	if(!isNaN(eval("entity."+ts[i])) && eval("entity."+ts[i]) != null){
	            		if(ts[i].toLowerCase().indexOf("time")!=-1 || ts[i].toLowerCase().indexOf("date")!=-1){
	            			record.set(ts[i],new Date(eval("entity."+ts[i])));
	            			continue;
	            		}
	            	}
	            	record.set(ts[i],eval("entity."+ts[i]));	            	
	            }
	            TrainResume.toEditFunc(grid,record);
	        }
	    });
	}
	
	function jsonField(o) {    
	    var arr = []; 
	    var fmt = function(s) { 
	        if (typeof s == 'object' && s != null) return jsonField(s); 
	        return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s; 
	    } 
	    for (var i in o) 
	         arr.push(i); 
	    return arr; 
	} 
});