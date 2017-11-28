//机车生产计划明细选择控件，目前供配件生产计划使用
Ext.ns('jx.jxgc.JobProcessDefSelect');
jx.jxgc.JobProcessDefSelect = Ext.extend(Ext.form.TriggerField, {
    triggerClass: 'x-form-search-trigger', //一个额外的CSS类，用来定义触发按钮样式
    initComponent: function(config) {
        jx.jxgc.JobProcessDefSelect.superclass.initComponent.call(this);
        Ext.apply(this, config);
        jx.jxgc.JobProcessDefSelect.returnFn = this.returnFn;
    },
    onTriggerClick: function() {
        if(this.disabled)   return;
        var trainTypeIdx =  jx.jxgc.JobProcessDefSelect.trainTypeIDX;
		if(Ext.isEmpty(trainTypeIdx)){
			MyExt.Msg.alert("请先选择型号！");
			return;
		}
		var rcIDX = jx.jxgc.JobProcessDefSelect.rcIDX;
		if(Ext.isEmpty(rcIDX)){
			MyExt.Msg.alert("请先选择修程！");
			return;
		}
        if(jx.jxgc.JobProcessDefSelect.win == null)   jx.jxgc.JobProcessDefSelect.createWin();        
        jx.jxgc.JobProcessDefSelect.grid.store.load();
        jx.jxgc.JobProcessDefSelect.win.show();
    },
    returnFn:function(grid, rowIndex, e){    //选择确定后触发函数，用于处理返回记录
        //var record = grid.store.getAt(rowIndex);
    }
});
jx.jxgc.JobProcessDefSelect.win = null;
jx.jxgc.JobProcessDefSelect.grid = null;

//定义查询变量
jx.jxgc.JobProcessDefSelect.trainTypeIDX = '';
jx.jxgc.JobProcessDefSelect.rcIDX = '';
jx.jxgc.JobProcessDefSelect.searchParams = {};
//创建弹出窗口
jx.jxgc.JobProcessDefSelect.createWin = function(){
    if(jx.jxgc.JobProcessDefSelect.grid == null)  jx.jxgc.JobProcessDefSelect.createGrid();
    if(jx.jxgc.JobProcessDefSelect.win == null){
        jx.jxgc.JobProcessDefSelect.win = new Ext.Window({
            title:'作业流程选择', closeAction:"hide", width:650, height:400, layout:"fit", resizable:false, modal:true, 
            items:jx.jxgc.JobProcessDefSelect.grid
        });
    }
}
jx.jxgc.JobProcessDefSelect.rowIndex = 0;
//创建选择表格
jx.jxgc.JobProcessDefSelect.createGrid = function(){
	if(jx.jxgc.JobProcessDefSelect.grid)  return;
	jx.jxgc.JobProcessDefSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jobProcessDef!pageQuery.action',  //装载列表数据的请求URL
	    storeAutoLoad:false,    //设置grid的store为手动加载(不设置false会引起参数排序失效)
	    searchFormColNum:2, singleSelect: true, 
	    tbar: [{
				text : "确定",
				iconCls : "addIcon",
				handler : function(){
					var grid = jx.jxgc.JobProcessDefSelect.grid;
					if (!$yd.isSelectedRecord(grid)) return;
				    var data = grid.selModel.getSelections();
				  jx.jxgc.JobProcessDefSelect.returnFn(grid, jx.jxgc.JobProcessDefSelect.rowIndex);
                  jx.jxgc.JobProcessDefSelect.win.hide();
				},
				scope: this
			},{
				text : "取消",
				iconCls : "closeIcon",
				handler : function(){
					 jx.jxgc.JobProcessDefSelect.win.hide();
				},
				scope: this
			}],
	    fields: [{
	        header:'idx主键', dataIndex:'idx', hidden:true
	    },{ 
	        header:'流程编码', dataIndex:'processCode'
	    },{ 
	        header:'流程名称', dataIndex:'processName'
	    },{
	        header:'流程描述', dataIndex:'description'
	    },{
	        header:'车型主键', dataIndex:'trainTypeIDX', hidden: true
	    },{
	        header:'车型', dataIndex:'trainTypeShortName', hidden: true
	    },{
	        header:'车号', dataIndex:'trainNo', hidden: true
	    },{
	        header:'车型名称', dataIndex:'trainTypeName', hidden: true
	    },{
	        header:'修程主键', dataIndex:'rcIDX', hidden: true
	    },{
	        header:'修程名称', dataIndex:'rcName', hidden: true
	    },{
	        header:'额定工期（小时）', dataIndex:'ratedWorkDay', hidden: true
	    }],
        searchOrder:['trainTypeShortName','trainNo'],
        toEditFn:function(grid, rowIndex, e){
            jx.jxgc.JobProcessDefSelect.returnFn(grid, rowIndex, e);
            jx.jxgc.JobProcessDefSelect.win.hide();
        }
	});
	jx.jxgc.JobProcessDefSelect.grid.on('rowclick', function( grid, rowIndex, e ) {
		jx.jxgc.JobProcessDefSelect.rowIndex = rowIndex;
	});
	jx.jxgc.JobProcessDefSelect.grid.store.on('beforeload', function(){
		jx.jxgc.JobProcessDefSelect.searchParams.trainTypeIDX = jx.jxgc.JobProcessDefSelect.trainTypeIDX;
		jx.jxgc.JobProcessDefSelect.searchParams.rcIDX = jx.jxgc.JobProcessDefSelect.rcIDX;
		var searchParam = jx.jxgc.JobProcessDefSelect.searchParams;
		searchParam.status = 1;//启用状态
		var whereList = [] ;
		for (prop in searchParam) {			
			if(prop == 'trainTypeIDX' || prop == 'rcIDX'){
				whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
			}else{
	        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
			}
		}
		
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	//表格数据排序
    jx.jxgc.JobProcessDefSelect.grid.store.setDefaultSort('processCode', 'ASC');
}
//注册为Ext容器组件
Ext.reg('JobProcessDefSelect', jx.jxgc.JobProcessDefSelect);