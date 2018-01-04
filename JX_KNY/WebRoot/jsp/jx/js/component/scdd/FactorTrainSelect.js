Ext.ns('jx.scdd.FactorTrainSelect');

jx.scdd.FactorTrainSelect = Ext.extend(Ext.form.TriggerField, {
    triggerClass: 'x-form-search-trigger', //一个额外的CSS类，用来定义触发按钮样式
    initComponent: function(config) {
        jx.scdd.FactorTrainSelect.superclass.initComponent.call(this);
        Ext.apply(this, config);
        jx.scdd.FactorTrainSelect.returnFn = this.returnFn;
    },
    onTriggerClick: function() {
        if(this.disabled)   return;
        jx.scdd.FactorTrainSelect.init();
        if(jx.scdd.FactorTrainSelect.win == null)   jx.scdd.FactorTrainSelect.createWin();
        jx.scdd.FactorTrainSelect.trainList.store.load();
        jx.scdd.FactorTrainSelect.grid.store.removeAll();
        jx.scdd.FactorTrainSelect.win.show();
    },
    returnFn:function(grid, rowIndex, e){    //选择确定后触发函数，用于处理返回记录
        var record = grid.store.getAt(rowIndex);
    }
});
jx.scdd.FactorTrainSelect.init = function(){
	jx.scdd.FactorTrainSelect.win = null;
	jx.scdd.FactorTrainSelect.grid = null;
	jx.scdd.FactorTrainSelect.trainList = null;
	jx.scdd.FactorTrainSelect.panel = null;
	jx.scdd.FactorTrainSelect.searchRdpParam = {};
	jx.scdd.FactorTrainSelect.searchParam = {};
	jx.scdd.FactorTrainSelect.rdpIdx = '';
	jx.scdd.FactorTrainSelect.status = node_status_handling;
}

//创建弹出窗口
jx.scdd.FactorTrainSelect.createWin = function(){
    if(jx.scdd.FactorTrainSelect.panel == null)  jx.scdd.FactorTrainSelect.createGrid();
    if(jx.scdd.FactorTrainSelect.win == null){
	    jx.scdd.FactorTrainSelect.win = new Ext.Window({
	        title:i18n.FactorTrainSelect.choiceNode, closeAction:"hide", width:600, height:300, layout:"fit", resizable:false, modal:true, 
            items:jx.scdd.FactorTrainSelect.panel
	    });
    }
}
//创建选择表格
jx.scdd.FactorTrainSelect.createGrid = function(){
    if(jx.scdd.FactorTrainSelect.panel)  return;
    
	jx.scdd.FactorTrainSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/factorTrainSelect!pageQuery.action',                 //装载列表数据的请求URL
        singleSelect: true, storeAutoLoad: false, 
        viewConfig: null,
        tbar:['search','-',
		    {
	    	    xtype:'label', text: i18n.FactorTrainSelect.status
		    },{   
			    xtype:'checkbox', name:'status', id: 'status_new', boxLabel:i18n.FactorTrainSelect.notProcessed+'&nbsp;&nbsp;&nbsp;&nbsp;', inputValue:node_status_new,
			    handler: function(){
			    	jx.scdd.FactorTrainSelect.checkQuery(node_status_new);
			    }
			  },{   
			    xtype:'checkbox', name:'status', id: 'status_handling', boxLabel:i18n.FactorTrainSelect.processing+'&nbsp;&nbsp;&nbsp;&nbsp;', inputValue:node_status_handling, checked:true,
			    handler: function(){
			    	jx.scdd.FactorTrainSelect.checkQuery(node_status_handling);
			    }
			  },{   
			    xtype:'checkbox', name:'status', id: 'status_handled', boxLabel:i18n.FactorTrainSelect.processed+'&nbsp;&nbsp;&nbsp;&nbsp;', inputValue:node_status_handled,
			    handler: function(){
			    	jx.scdd.FactorTrainSelect.checkQuery(node_status_handled);
			    }
			  }
		  ],
	    fields: [{
	        header:'i18n.FactorTrainSelect.idx', dataIndex:'idx', hidden:true
	    },{
	        header:i18n.FactorTrainSelect.nodeIDX, dataIndex:'nodeIDX', hidden:true
	    },{
	        header:i18n.FactorTrainSelect.rdpIDX, dataIndex:'rdpIDX', hidden:true
	    },{
	        header:i18n.FactorTrainSelect.nodeCaseName, dataIndex:'nodeCaseName', width: 150
	    },{
	        header:i18n.FactorTrainSelect.status, dataIndex:'status', searcher:{disabled:true},
	        renderer : function(v){return getNodeStatus(v);}, width: 100
	    },{
	        header:i18n.FactorTrainSelect.trainTypeShortName, dataIndex:'trainTypeShortName',searcher:{disabled:true}, hidden:true
	    },{
            header:i18n.FactorTrainSelect.trainNo, dataIndex:'trainNo',searcher:{disabled:true}, hidden:true
        },{
            header:i18n.FactorTrainSelect.trainTypeIdx, dataIndex:'trainTypeIdx',searcher:{disabled:true}, hidden:true
        },{
            header:i18n.FactorTrainSelect.repairClassIdx, dataIndex:'repairClassIdx',searcher:{disabled:true}, hidden:true
        },{
	        header:i18n.FactorTrainSelect.repairClassName, dataIndex:'repairClassName',searcher:{disabled:true}, hidden:true
	    },{
	        header:i18n.FactorTrainSelect.repairTimeName, dataIndex:'repairTimeName',searcher:{disabled:true}, hidden:true
	    }],
        toEditFn:function(grid, rowIndex, e){
            jx.scdd.FactorTrainSelect.returnFn(grid, rowIndex, e);
            jx.scdd.FactorTrainSelect.win.hide();
        },
        /**
	     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
	     * @param searchParam 查询表单的Json对象
	     * @return {} 返回的Json数据格式对象,
	     */
	    searchFn: function(searchParam){
	    	jx.scdd.FactorTrainSelect.searchParam = searchParam ;
	        this.store.load();
	    }
	});
	jx.scdd.FactorTrainSelect.trainList = new Ext.yunda.Grid({
		//TODO V3.2.1代码重构
	    loadURL: ctx + '/trainEnforcePlanRdp!pageQuery.action',                 //装载列表数据的请求URL
        singleSelect: true, storeAutoLoad: false, tbar:['search'],
        viewConfig: null,
	    fields: [{
	        header:i18n.FactorTrainSelect.idx, dataIndex:'idx', hidden:true
	    },{
	        header:i18n.FactorTrainSelect.tecProcessCaseIDX, dataIndex:'tecProcessCaseIDX', hidden:true 
	    },{
	        header:i18n.FactorTrainSelect.trainTypeShortName, dataIndex:'trainTypeShortName', width: 60
	    },{
	        header:i18n.FactorTrainSelect.trainNo, dataIndex:'trainNo', width: 60
	    },{
	        header:i18n.FactorTrainSelect.repairClassName, dataIndex:'repairClassName', width: 60
	    },{
	        header:i18n.FactorTrainSelect.repairTimeName, dataIndex:'repairtimeName',searcher:{disabled:true}, width: 60
	    }],
	    /**
	     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
	     * @param searchParam 查询表单的Json对象
	     * @return {} 返回的Json数据格式对象,
	     */
	    searchFn: function(searchParam){
	    	jx.scdd.FactorTrainSelect.searchRdpParam = searchParam ;
	        this.store.load();
	    }
	});
	jx.scdd.FactorTrainSelect.panel =  new Ext.Panel( {
	    layout : 'border',
	    items : [ {
	        title: i18n.FactorTrainSelect.maintainTrain, width: 290, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
	        layout: 'fit', items : [ jx.scdd.FactorTrainSelect.trainList ]
	    }, {
	        title: i18n.FactorTrainSelect.node, region : 'center', layout: 'fit', bodyBorder: false, items: [ jx.scdd.FactorTrainSelect.grid ]
	    } ]
	});
	jx.scdd.FactorTrainSelect.grid.store.on("beforeload", function(){
		if(Ext.isEmpty(jx.scdd.FactorTrainSelect.rdpIdx)){
			MyExt.Msg.alert(i18n.FactorTrainSelect.chooseMaintainTrain);
			return false;
		}
		var searchParam = jx.scdd.FactorTrainSelect.searchParam;
		searchParam.status = jx.scdd.FactorTrainSelect.status;
		if(!Ext.isEmpty(jx.scdd.FactorTrainSelect.rdpIdx)) searchParam.rdpIDX = jx.scdd.FactorTrainSelect.rdpIdx;
		var whereList = [] ;
		for (prop in searchParam) {	
			if('status' == prop){
				var val = searchParam[prop].split(",");
	                if(val instanceof Array){
	                    whereList.push({propName:'status', propValues:val, compare:Condition.IN });
	                } else {
	                    var valAry = [];
	                    valAry.push(val);
	                    whereList.push({propName:'status', propValues:valAry, compare:Condition.IN });
	                }
	                continue;
	        }
	        whereList.push({propName:prop, propValue: searchParam[prop]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	jx.scdd.FactorTrainSelect.trainList.store.on("beforeload", function(){
		var searchParam = jx.scdd.FactorTrainSelect.searchRdpParam;
		searchParam.billStatus = rdp_status_handling;
		var whereList = [] ;
		for (prop in searchParam) {		
	        whereList.push({propName:prop, propValue: searchParam[prop]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	jx.scdd.FactorTrainSelect.trainList.store.setDefaultSort('trainNo', 'ASC');//默认按车号排序
	jx.scdd.FactorTrainSelect.grid.store.setDefaultSort('status', 'ASC');//默认按idx排序
	//状态多选按钮
	jx.scdd.FactorTrainSelect.checkQuery = function(status){
		jx.scdd.FactorTrainSelect.status = "-1";
		if(Ext.getCmp("status_new").checked){
			jx.scdd.FactorTrainSelect.status = jx.scdd.FactorTrainSelect.status + "," + node_status_new;
		} 
		if(Ext.getCmp("status_handling").checked){
			jx.scdd.FactorTrainSelect.status = jx.scdd.FactorTrainSelect.status + "," + node_status_handling;
		} 
		if(Ext.getCmp("status_handled").checked){
			jx.scdd.FactorTrainSelect.status = jx.scdd.FactorTrainSelect.status + "," + node_status_handled;
		}		
		jx.scdd.FactorTrainSelect.grid.store.load();
	}
	jx.scdd.FactorTrainSelect.trainList.on("rowclick", function(grid, rowIndex, e){
			var record = grid.store.getAt(rowIndex);
			jx.scdd.FactorTrainSelect.rdpIdx = record.get("idx");
			jx.scdd.FactorTrainSelect.grid.store.load();
		});
	jx.scdd.FactorTrainSelect.trainList.createSearchWin();
	jx.scdd.FactorTrainSelect.trainList.searchWin.modal = true;
	jx.scdd.FactorTrainSelect.grid.createSearchWin();
	jx.scdd.FactorTrainSelect.grid.searchWin.modal = true;
	jx.scdd.FactorTrainSelect.trainList.un('rowdblclick', jx.scdd.FactorTrainSelect.trainList.toEditFn, jx.scdd.FactorTrainSelect.trainList);
}
//注册为Ext容器组件
Ext.reg('FactorTrainSelect', jx.scdd.FactorTrainSelect);