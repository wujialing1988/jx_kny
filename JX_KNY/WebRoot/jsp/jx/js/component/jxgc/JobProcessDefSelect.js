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
			MyExt.Msg.alert(i18n.JobProcessDefSelect.choiceType);
			return;
		}
		var rcIDX = jx.jxgc.JobProcessDefSelect.rcIDX;
		if(Ext.isEmpty(rcIDX)){
			MyExt.Msg.alert(i18n.JobProcessDefSelect.choiceService);
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
            title:i18n.JobProcessDefSelect.choiceProcess, closeAction:"hide", width:650, height:400, layout:"fit", resizable:false, modal:true, 
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
				text : i18n.JobProcessDefSelect.confirm,
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
				text : i18n.JobProcessDefSelect.cancel,
				iconCls : "closeIcon",
				handler : function(){
					 jx.jxgc.JobProcessDefSelect.win.hide();
				},
				scope: this
			}],
	    fields: [{
	        header:i18n.JobProcessDefSelect.idx, dataIndex:'idx', hidden:true
	    },{ 
	        header:i18n.JobProcessDefSelect.processCode, dataIndex:'processCode'
	    },{ 
	        header:i18n.JobProcessDefSelect.processName, dataIndex:'processName'
	    },{
	        header:i18n.JobProcessDefSelect.description, dataIndex:'description'
	    },{
	        header:i18n.JobProcessDefSelect.trainTypeIDX, dataIndex:'trainTypeIDX', hidden: true
	    },{
	        header:i18n.JobProcessDefSelect.trainTypeShortName, dataIndex:'trainTypeShortName', hidden: true
	    },{
	        header:i18n.JobProcessDefSelect.trainNo, dataIndex:'trainNo', hidden: true
	    },{
	        header:i18n.JobProcessDefSelect.trainTypeName, dataIndex:'trainTypeName', hidden: true
	    },{
	        header:i18n.JobProcessDefSelect.rcIDX, dataIndex:'rcIDX', hidden: true
	    },{
	        header:i18n.JobProcessDefSelect.rcName, dataIndex:'rcName', hidden: true
	    },{
	        header:i18n.JobProcessDefSelect.ratedWorkDay, dataIndex:'ratedWorkDay', hidden: true
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