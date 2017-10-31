Ext.onReady(function(){
	Ext.namespace('TrainRdp');
	TrainRdp.searchParam = {};	
	TrainRdp.grid = new Ext.yunda.Grid({
		//TODO V3.2.1代码重构
	    loadURL: ctx + '/trainEnforcePlanRdp!pageQuery.action',                 //装载列表数据的请求URL
	    saveFormColNum:2,  searchFormColNum:2, storeAutoLoad:true,
	    singleSelect: true,
	    tbar:['search',{
	    	text:"添加材料消耗", scope:this, iconCls:"addIcon",
	    	handler:function(){
	    		//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(TrainRdp.grid)) return;
        		var data = TrainRdp.grid.selModel.getSelections();
        		TrainComm.toEditFn(data[0]);
	    	}    	
	    },{
	    	text:"材料消耗汇总",iconCls:"printerIcon" ,handler: function(){
	    		var ids = $yd.getSelectedIdx(TrainRdp.grid);
	    	 	if(!$yd.isSelectedRecord(TrainRdp.grid) ) return;
	    	 	if(ids.length > 1){
	    	 		MyExt.Msg.alert("只能选择一条记录！");
	    	 		return;
	    	 	}
	    	 	var record = TrainRdp.grid.store.getById(ids[0]);
	    	 	//var trainInfo = record.get("trainTypeShortName")+record.get("trainNo")+record.get("repairClassName")+record.get("repairtimeName");
	    	 	var url = "/jxgc/matConsumeInfo/matConsumeInfo.cpt&idx=" + ids[0]+"&teamOrgId=" + teamOrgId;
	    	 	window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(cjkEncode(url))+"&title=" + encodeURI("物料消耗汇总"));
	    	}
	    	
	    },'refresh','-',
	    {
    	    xtype:'label', text: '状态：'
	    },
		  {   
		    xtype:'checkbox', name:'status', id: 'status_handling', boxLabel:'在修&nbsp;&nbsp;&nbsp;&nbsp;', 
		    	inputValue:rdp_status_handling, checked:true,
			    handler: function(){
			    	TrainRdp.checkQuery(rdp_status_handling);
			    }
		  },{   
		    xtype:'checkbox', name:'status', id: 'status_handled', boxLabel:'修竣&nbsp;&nbsp;&nbsp;&nbsp;', 
		    	inputValue:rdp_status_handled,checked:true,
			    handler: function(){
			    	TrainRdp.checkQuery(rdp_status_handled);
			    }
		  }],	 
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'施修计划明细主键', dataIndex:'enforcePlanDetailIDX', hidden:true, editor:{  xtype:'hidden' }
		},{
			header:'组成主键', dataIndex:'buildUpTypeIDX', hidden:true, editor:{  xtype:'hidden' }
		},{
			header:'组成编码', dataIndex:'buildUpTypeCode', hidden:true, editor:{  xtype:'hidden' }
		},{
			header:'兑现单编码', dataIndex:'rdpBillCode', hidden:true,editor:{  maxLength:50, xtype:"hidden" },
			searcher: {disabled: true}		
		},{
			header:'车型', dataIndex:'trainTypeIDX', hidden: true, 
			editor:{}	    
		},{
			header:'车型', dataIndex:'trainTypeShortName', editor:{xtype: 'hidden' }, width: 60,
			searcher:{xtype: 'textfield'}
		},{
			header:'车号', dataIndex:'trainNo', width: 60,
			editor:{},
			searcher:{xtype: 'textfield'}
		},{
			header:'修程', dataIndex:'repairClassIDX', hidden: true,
			editor:{}
		},{
			header:'修程', dataIndex:'repairClassName', editor:{  xtype:'hidden' }, width: 60,
			searcher: {xtype: 'textfield'}
			
		},{
			header:'修次主键', dataIndex:'repairtimeIDX', hidden:true,
			editor:{}
		},{
			header:'修次', dataIndex:'repairtimeName', editor:{}, width: 60
		},{
			header:'承修段', dataIndex:'undertakeOversea', hidden: true, 
			editor:{ xtype:'hidden'}
		},{
			header:'承修段', dataIndex:'undertakeOverseaName', hidden: true, editor:{  disabled: true }
		},{
			header:'承修段序列', dataIndex:'undertakeOverseaSeq', hidden: true, editor:{  xtype:'hidden' }
		},{
			header:'承修部门', dataIndex:'undertakeDep', hidden: true, editor:{}
		},{
			header:'承修部门', dataIndex:'undertakeDepName', editor:{ },
			searcher: {disabled: true}
		},{
			header:'承修部门序列', dataIndex:'undertakeDepSeq',  hidden: true, editor:{ xtype: 'hidden' }
		},{
			header:'检修开始时间', dataIndex:'transInTime', xtype:'datecolumn', format: 'Y-m-d H:i', width: 120,
			editor:{ xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm'}, initNow: false },
			searcher: {disabled: true}
		},{
			header:'检修结束时间', dataIndex:'transOutTime', xtype:'datecolumn', format: 'Y-m-d H:i',  hidden:true,
			editor:{ xtype:'hidden' }
		},{
			header:'检修结束时间', dataIndex:'planTrainTime', xtype:'datecolumn', format: 'Y-m-d H:i', width: 120,
			editor:{ xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm'}, initNow: false },
			searcher: {disabled: true}
		},{
			header:'兑现时间', dataIndex:'rdpTime', xtype:'datecolumn', format: 'Y-m-d H:i', editor:{ xtype:'hidden' },width: 120
		},{
			header:'实际交车时间', dataIndex:'realTrainTime', xtype:'datecolumn', format: 'Y-m-d H:i', hidden:true, editor:{ xtype:'hidden' }
		},{
			header:'是否延迟交车', dataIndex:'isDelayTrain', hidden: true, editor:{ xtype:'hidden' }
		},{
			header:'检修流水线', dataIndex:'repairLineIDX', hidden: true, 
			editor:{}
		},{
			header:'检修流水线名称', dataIndex:'repairLineName', hidden: true, editor:{ xtype: 'hidden' }
		},{
			header:'检修股道', dataIndex:'trackCode', hidden: true, editor:{  xtype: 'hidden' }
		},{
			header:'检修股道', dataIndex:'trackName', hidden: true, editor:{  maxLength:100 }
		},{
			header:'工艺流程', dataIndex:'processIDX', hidden:true, 
			editor:{},
			searcher: {disabled: true}
		},{
			header:'工艺流程', dataIndex:'processName', editor:{ xtype:'hidden' }, width: 120
		},{
			header:'状态', dataIndex:'billStatus', editor:{ xtype: 'hidden' },
			renderer : function(v){
				if(v == rdp_status_new)return "新兑现";
				else if(v == rdp_status_handling)return "在修";
				else if(v == rdp_status_handled)return "修竣";
			}
		},{
			header:'检修台位', dataIndex:'deskCode', hidden: true, editor:{  xtype: 'hidden' }
		},{
			header:'检修台位', dataIndex:'deskName', hidden: true, editor:{  maxLength:100 }
		},{
			header:'备注', dataIndex:'remarks', hidden: true, editor:{ xtype:'textarea', maxLength:1000 }
		}],
		//查询
		searchFn:function(s){
			TrainRdp.searchParam = s;
			this.store.load();
		}
	});
	TrainRdp.grid.un('rowdblclick', TrainRdp.grid.toEditFn, TrainRdp.grid);
	TrainRdp.grid.createSearchWin();
	TrainRdp.grid.searchWin.modal = true;
	
	TrainRdp.status = rdp_status_handling + "," + rdp_status_handled + ",";
	//状态多选按钮
	TrainRdp.checkQuery = function(status){
		TrainRdp.status = "-1";
		if(Ext.getCmp("status_handling").checked){
			TrainRdp.status = TrainRdp.status + "," + rdp_status_handling;
		} 
		if(Ext.getCmp("status_handled").checked){
			TrainRdp.status = TrainRdp.status + "," + rdp_status_handled;
		}
		TrainRdp.grid.store.load();
	}
	TrainRdp.grid.store.on("beforeload", function(){	
		var searchParam = TrainRdp.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		whereList.push({propName:'billStatus', propValues: TrainRdp.status.split(",") ,compare : Condition.IN}) ;
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	//根据isQuery判断是维护页面还是统计页面
	TrainRdp.grid.on("beforerender", function(grid){
		if(!Ext.isEmpty(isQuery) && isQuery == 'true'){
			grid.getTopToolbar().get(1).setVisible(false);//屏蔽【添加材料消耗】按钮
			grid.getTopToolbar().get(2).setVisible(true);//屏蔽【材料消耗汇总】按钮
		}else if (!Ext.isEmpty(isQuery) && isQuery == 'false'){
			grid.getTopToolbar().get(1).setVisible(true);
			grid.getTopToolbar().get(2).setVisible(false);
		}
		return true;
	})
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainRdp.grid });
});