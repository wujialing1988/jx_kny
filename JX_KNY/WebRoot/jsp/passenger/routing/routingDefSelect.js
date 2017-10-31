//机车生产计划明细选择控件，目前供配件生产计划使用
Ext.ns('jx.jxgc.routingDefSelect');
jx.jxgc.routingDefSelect = Ext.extend(Ext.form.TriggerField, {
    triggerClass: 'x-form-search-trigger', //一个额外的CSS类，用来定义触发按钮样式
    initComponent: function(config) {
        jx.jxgc.routingDefSelect.superclass.initComponent.call(this);
        Ext.apply(this, config);
        jx.jxgc.routingDefSelect.returnFn = this.returnFn;
    },
    onTriggerClick: function() {
        if(this.disabled)   return;
//        var trainTypeIdx =  jx.jxgc.JobProcessDefSelect.trainTypeIDX;
//		if(Ext.isEmpty(trainTypeIdx)){
//			MyExt.Msg.alert("请先选择车型！");
//			return;
//		}
//		var rcIDX = jx.jxgc.JobProcessDefSelect.rcIDX;
//		if(Ext.isEmpty(rcIDX)){
//			MyExt.Msg.alert("请先选择修程！");
//			return;
//		}
        if(jx.jxgc.routingDefSelect.win == null)   jx.jxgc.routingDefSelect.createWin();        
        jx.jxgc.routingDefSelect.grid.store.load();
        jx.jxgc.routingDefSelect.win.show();
    },
    returnFn:function(grid, rowIndex, e){    //选择确定后触发函数，用于处理返回记录
        //var record = grid.store.getAt(rowIndex);
    }
});
jx.jxgc.routingDefSelect.win = null;
jx.jxgc.routingDefSelect.grid = null;

//定义查询变量
jx.jxgc.routingDefSelect.trainTypeIDX = '';
jx.jxgc.routingDefSelect.rcIDX = '';
jx.jxgc.routingDefSelect.searchParams = {};
//创建弹出窗口
jx.jxgc.routingDefSelect.createWin = function(){
    if(jx.jxgc.routingDefSelect.grid == null)  jx.jxgc.routingDefSelect.createGrid();
    if(jx.jxgc.routingDefSelect.win == null){
        jx.jxgc.routingDefSelect.win = new Ext.Window({
            title:'交路选择', closeAction:"hide", width:650, height:400, layout:"fit", resizable:false, modal:true, 
            items:jx.jxgc.routingDefSelect.grid
        });
    }
}
jx.jxgc.routingDefSelect.rowIndex = 0;
//创建选择表格
jx.jxgc.routingDefSelect.createGrid = function(){
	if(jx.jxgc.routingDefSelect.grid)  return;
	jx.jxgc.routingDefSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/routing!pageQuery.action',  //装载列表数据的请求URL
	    storeAutoLoad:false,    //设置grid的store为手动加载(不设置false会引起参数排序失效)
	    searchFormColNum:2, singleSelect: true, 
	    tbar: [{
				text : "确定",
				iconCls : "addIcon",
				handler : function(){
					var grid = jx.jxgc.routingDefSelect.grid;
					if (!$yd.isSelectedRecord(grid)) return;
				    var data = grid.selModel.getSelections();
				 	jx.jxgc.routingDefSelect.returnFn(grid, jx.jxgc.routingDefSelect.rowIndex);
                   jx.jxgc.routingDefSelect.win.hide();
				},
				scope: this
			},{
				text : "取消",
				iconCls : "closeIcon",
				handler : function(){
					 jx.jxgc.routingDefSelect.win.hide();
				},
				scope: this
			}],
	    fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'编号', dataIndex:'routingCode', width: 60
		},{
			header:'出发地', dataIndex:'startingStation', width: 60
		},{
			header:'前往地', dataIndex:'leaveOffStation', width: 60
		},{
			header:'历时', dataIndex:'duration', width: 60,
			renderer: function(value){
				if (!Ext.isEmpty(value)) {
					return formatTimeForHours(value, 'm');
				}
			}
		},{
			header:'出发时间', dataIndex:'departureTime',width: 60,
			searcher:{disabled: true}
		},{
			header:'到达时间', dataIndex:'arrivalTime', width: 60
		},{
			header:'返程发车时间', dataIndex:'departureBackTime',
			searcher:{disabled: true}
		},{
			header:'返程到达时间', dataIndex:'arrivalBackTime' 
			
		},{
			header:'行程（KM）', dataIndex:'kilometers', editor:{ maxLength:10,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:'备注', dataIndex:'remark',  width: 260 , hidden:true, editor:{ xtype:'textarea', maxLength:1000 },
			searcher:{ disabled:true }
	    }],
        searchOrder:['trainTypeShortName','trainNo'],
        toEditFn:function(grid, rowIndex, e){
            jx.jxgc.routingDefSelect.returnFn(grid, rowIndex, e);
            jx.jxgc.routingDefSelect.win.hide();
        }
	});
	jx.jxgc.routingDefSelect.grid.on('rowclick', function( grid, rowIndex, e ) {
		jx.jxgc.routingDefSelect.rowIndex = rowIndex;
	});
	jx.jxgc.routingDefSelect.grid.store.on('beforeload', function(){
//		jx.jxgc.routingDefSelect.searchParams.trainTypeIDX = jx.jxgc.routingDefSelect.trainTypeIDX;
//		jx.jxgc.routingDefSelect.searchParams.rcIDX = jx.jxgc.routingDefSelect.rcIDX;
		var searchParam = jx.jxgc.routingDefSelect.searchParams;
		var whereList = [] ;
		for (prop in searchParam) {			
			if(prop == 'arrivalTime' || prop == 'departureTime'){
				whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
			}else{
	        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
			}
		}
		
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	//表格数据排序
    jx.jxgc.routingDefSelect.grid.store.setDefaultSort('routingCode', 'ASC');
}
//注册为Ext容器组件
Ext.reg('routingDefSelect', jx.jxgc.routingDefSelect);