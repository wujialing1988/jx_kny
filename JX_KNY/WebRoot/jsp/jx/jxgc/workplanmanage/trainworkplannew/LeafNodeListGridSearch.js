/**
 * 机车检修作业编辑超链接中的下级节点grid
 */
Ext.onReady(function(){
	Ext.namespace('LeafNodeListGridSearch'); 
	LeafNodeListGridSearch.labelWidth = 90;
	LeafNodeListGridSearch.fieldWidth = 150;
	LeafNodeListGridSearch.searchParam = {}
	LeafNodeListGridSearch.nodeIDX = '';
	LeafNodeListGridSearch.ids = [];
	LeafNodeListGridSearch.workPlanStatus = '';
	LeafNodeListGridSearch.calendarName = '';
	LeafNodeListGridSearch.parentIDX =  '';
	LeafNodeListGridSearch.status =  '';
	LeafNodeListGridSearch.workPlanIDX =  '';
	LeafNodeListGridSearch.processIDX = '';
	//初始化状态和对应的中文名
	LeafNodeListGridSearch.stateName = {"NOTSTARTED":"未启动","RUNNING":"运行","COMPLETED":"完成","TERMINATED":"终止"};
	//初始化状态和对应的中文名
	LeafNodeListGridSearch.planMode = {"AUTO":"自动","MANUAL":"手动","TIMER":"定时"};
	
	var processTips;
	
	/*
	 * 显示处理遮罩
	 */
	function showtip(msg){
		processTips = new Ext.LoadMask(Ext.getBody(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		processTips.show();
	}
	
	/*
	 * 隐藏处理遮罩
	 */
	function hidetip(){
		processTips.hide();
	}
	
	LeafNodeListGridSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jobProcessNodeQuery!getAllChildNodeExceptThisList.action',                 
	    page : false,
	    isEdit : false,
	    singleSelect: true,
	    storeAutoLoad:false,
	    tbar: [{
			iconCls:"refreshIcon",
	    	text:"刷新",
	    	handler: function(){
	    		LeafNodeListGrid.grid.store.reload();
	    	}
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'名称', dataIndex:'nodeName',width: 130
		},{
			header:'工期', dataIndex:'ratedWorkMinutes',width: 50,
			renderer :function(v, r){
				if(v != null && v != '' && v != 'null'){
					var hours = (v/60).toFixed(0);
					var minutes = (v- hours*60).toFixed(0);
					if(hours > 0){
						if(minutes > 0){
			    			return hours + "小时" + minutes + "分钟";
						}else{
							return hours + "小时";
						}
					}else{
						return minutes + "分钟"	;	
					}
				}else{
					return "0小时";
				}
			}
		},{
			header:'前置节点', dataIndex:'lastChildNodeNames', hidden:true
		},{
			header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn',width: 100, format: "Y-m-d H:i"
		},{
			header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn',width: 100, format: "Y-m-d H:i"
		},{
			header:'作业工位IDX', dataIndex:'workStationIDX', hidden:true
		},{
			header:'作业工位', dataIndex:'workStationName',width: 80
		},{
			header:'作业班组', dataIndex:'workStationBelongTeamName',width: 80
		},{
			header:'实际开始时间', dataIndex:'realBeginTime', xtype:'datecolumn', format: "Y-m-d H:i",width: 100
		},{
			header:'实际完成时间', dataIndex:'realEndTime', xtype:'datecolumn', format: "Y-m-d H:i",width: 100
		},{
			header:'作业计划主键', dataIndex:'workPlanIDX', hidden:true
		},{
			header:'父节点idx', dataIndex:'parentIDX', hidden:true
		},{
			header:'是否是子节点', dataIndex:'isLeaf', hidden:true
		},{
			header:'定义节点id', dataIndex:'nodeIDX', hidden:true
		},{
			header:'作业流程主键', dataIndex:'processIDX', hidden:true
		},{
			header:'处理情况', dataIndex:'status',width: 80 ,
			renderer :function(a,b,c,d){
				return LeafNodeListGridSearch.stateName[a];
			}
		},{
			header:'计划模式', dataIndex:'planMode',width: 40,
			renderer :function(v){
				return LeafNodeListGridSearch.planMode[v];
			}
		},{
			header:'日历', dataIndex:'workCalendarIDX',width: 120,
			renderer :function(v){
				return WorkCalendarInfo.getCalendarName(v);
			}
		}],
		//不需要双击显示
		toEditFn : function(){}
	});
	LeafNodeListGridSearch.grid.store.on('beforeload', function() {
		this.baseParams.nodeIDX = LeafNodeListGridSearch.nodeIDX;
	});
	

	


});