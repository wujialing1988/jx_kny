Ext.onReady(function(){	
	var workStationTips;
	
	/*
	 * 显示处理遮罩
	 */
	function showtip(msg){
		workStationTips = new Ext.LoadMask(TecnodeUnionStation.selectWin.getEl(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		workStationTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	function hidetip(){
		workStationTips.hide();
	}
	Ext.namespace('TecProcessNode'); 
	TecProcessNode.idx = "";
	
	Ext.namespace('TecnodeUnionStation');                       //定义命名空间
	TecnodeUnionStation.searchParams = {};						//全局查询参数集
	TecnodeUnionStation.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workStation!findSelectPageList.action',                 //装载列表数据的请求URL
	    storeAutoLoad: false,
	    singleSelect: true,    
	    tbar:[{
			xtype:"combo", hiddenName:"queryType", displayField:"type",
	        width: 80, valueField:"type", value:"工位编码", mode:"local",triggerAction: "all",
			store: new Ext.data.SimpleStore({
				fields: ["type"],
				data: [ ["工位编码"], ["工位名称"]]
			})
		},{	            
	        xtype:"textfield", width: 100
		},{
			text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
			handler : function(){
				var grid = TecnodeUnionStation.grid;
				var typeName = grid.getTopToolbar().get(1).getValue();
				var querytype = grid.getTopToolbar().get(0).getValue();
				if(querytype == '工位编码'){
					delete TecnodeUnionStation.searchParams["workStationName"];
					TecnodeUnionStation.searchParams.workStationCode = typeName;
				}else if(querytype == '工位名称'){
					delete TecnodeUnionStation.searchParams["workStationCode"];
					TecnodeUnionStation.searchParams.workStationName = typeName;
				}
				TecnodeUnionStation.grid.getStore().load();
			}
		},{
			text : "重置",iconCls : "resetIcon",
			handler : function(){
				//清空搜索输入框
				var grid = TecnodeUnionStation.grid;
				var stationName = grid.getTopToolbar().get(1);
				stationName.setValue("");
				TecnodeUnionStation.searchParams.workStationName = "";
				TecnodeUnionStation.searchParams.workStationCode = "";
				TecnodeUnionStation.grid.getStore().load();
			}
		},{
	        text:"选择其他工位", iconCls:"addIcon",handler: function(){
	        	WorkStationSearcher1.nodeIDX = nodeIDX;	
	        	RepairLine1.nodeIDX = nodeIDX;
	        	// 获取已选择的工位主键，用于过滤工位树
	        	var store = TecnodeUnionStation.grid.store;
	        	var selectedIds = [];
	        	store.each(function(record){
	        		this.push(record.get('repairLineIdx'));
	        	}, selectedIds);
	        	WorkStationSearcher1.selectedIds = selectedIds;
	    		WorkStationSearcher1.win.show();
	    	
//	        	TecnodeUnionStation.win.show();
//	        	WorkStation.grid.store.on("beforeload", function(){
//	        		var searchParam = WorkStation.searchParam ;
//	        		if(typeof(repairLineType) != "undefined"){
//	        			searchParam.lineType = repairLineType;
//	        		}
//	        		searchParam.nodeIDX = nodeIDX;
//        			this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
//	        		TecProcessNode.idx = nodeIDX;	    		
//				});
//                WorkStation.grid.store.load();
	        }        
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'工艺节点主键', dataIndex:'tecProcessNodeIdx', hidden:true, editor:{ }
		},{
			header:'工位编码', dataIndex:'workStationCode', width: 100, editor:{  }
		},{
			header:'工位名称', dataIndex:'workStationName', width: 100, editor:{ }
		},{
			header:'流水线主键', dataIndex:'repairLineIdx', hidden:true
		},{
			header:'流水线名称', dataIndex:'repairLineName', width: 100, editor:{ xtype:'hidden', maxLength:100 }
		},{
			header:'工作班组', dataIndex:'teamOrgName', width: 100, editor:{ }
		},{
			header:'工作班组ID', dataIndex:'teamOrgId', hidden:true, editor:{ }
		},{
			header:'工作班组序列', dataIndex:'teamOrgSeq', hidden:true, editor:{ }
		}]
	});
	//移除事件
	TecnodeUnionStation.grid.un('rowdblclick',TecnodeUnionStation.grid.toEditFn,TecnodeUnionStation.grid);
	TecnodeUnionStation.grid.store.on("beforeload", function(){
		var searchParam = TecnodeUnionStation.searchParams;
		searchParam.nodeIDX = nodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	//定义点击确定按钮的操作
	TecnodeUnionStation.submit = function(){
		var grid = TecnodeUnionStation.grid;
		if(!$yd.isSelectedRecord(grid)) return;
		var r = grid.selModel.getSelections();
		var workStationIdx = r[0].get("idx");
		dataParam.workStationIDX = r[0].get("idx");
		dataParam.workStationName = r[0].get("workStationName");
		dataParam.workStationCode = r[0].get("workStationCode");
		dataParam.repairLineIDX = r[0].get("repairLineIdx");
		dataParam.workStationBelongTeam = r[0].get("teamOrgId");
		dataParam.workStationBelongTeamName = r[0].get("teamOrgName");
		dataParam.workStationBelongTeamSeq = r[0].get("teamOrgSeq");
		var cfg = {				
			url: ctx + "/jobProcessNode!dispatchNode.action",
			jsonData: dataParam,
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(workStationTips)  hidetip();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null && result.success == true) {       //操作成功     
		            alertSuccess();
		            WorkPlanGantt.loadFn("expanded", dataParam.nodeCaseIDX);
		            TecnodeUnionStation.selectWin.hide();
		            TecnodeUnionStation.selectWin.buttons[0].setVisible(true);
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		            TecnodeUnionStation.selectWin.buttons[0].setVisible(true);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(workStationTips)  hidetip();
		    	TecnodeUnionStation.selectWin.buttons[0].setVisible(true);
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		};
		Ext.Msg.confirm("提示  ", "是否对该工艺节点的作业工单派工？  ", function(btn){
	        if(btn != 'yes')   {
	        	return;		        	
	        }
        	TecnodeUnionStation.selectWin.buttons[0].setVisible(false);
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });    
	}
	//选择工位窗口
	TecnodeUnionStation.win = new Ext.Window({
		title: "其他工位选择",width: 500, height: 360, maximizable:false, layout: "fit", modal: true,
		closeAction: "hide", modal: true, maximized: false , buttonAlign:"center",
		items: [TecnodeUnionStation.grid],
	    buttons: [{
	                text: "关闭", iconCls: "closeIcon", handler: function(){ TecnodeUnionStation.win.hide(); }
	            }]
	});
	//定义选择窗口
	TecnodeUnionStation.selectWin = new Ext.Window({
		title:"选择工位", width:550, height:360, closeAction:"hide", modal:true, layout:"fit", buttonAlign:"center",
		items:[TecnodeUnionStation.grid],
		buttons:[{
			text: "确定", iconCls:"saveIcon",
			handler:function(){
				TecnodeUnionStation.submit();
	    	}
		},{
			text: "关闭", iconCls:"closeIcon",
			handler:function(){
				TecnodeUnionStation.selectWin.hide();
			}
		}]
	});
});