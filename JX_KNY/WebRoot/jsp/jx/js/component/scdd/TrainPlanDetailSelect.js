//机车生产计划明细选择控件，目前供配件生产计划使用
Ext.ns('jx.scdd.TrainPlanDetailSelect');
jx.scdd.TrainPlanDetailSelect = Ext.extend(Ext.form.TriggerField, {
    triggerClass: 'x-form-search-trigger', //一个额外的CSS类，用来定义触发按钮样式
    initComponent: function(config) {
        jx.scdd.TrainPlanDetailSelect.superclass.initComponent.call(this);
        Ext.apply(this, config);
        jx.scdd.TrainPlanDetailSelect.returnFn = this.returnFn;
    },
    onTriggerClick: function() {
        if(this.disabled)   return;
        if(jx.scdd.TrainPlanDetailSelect.win == null)   jx.scdd.TrainPlanDetailSelect.createWin();
        jx.scdd.TrainPlanDetailSelect.grid.store.load();
        jx.scdd.TrainPlanDetailSelect.win.show();
    },
    returnFn:function(grid, rowIndex, e){    //选择确定后触发函数，用于处理返回记录
        var record = grid.store.getAt(rowIndex);
    }
});
jx.scdd.TrainPlanDetailSelect.win = null;
jx.scdd.TrainPlanDetailSelect.grid = null;
//创建弹出窗口
jx.scdd.TrainPlanDetailSelect.createWin = function(){
    if(jx.scdd.TrainPlanDetailSelect.grid == null)  jx.scdd.TrainPlanDetailSelect.createGrid();
    if(jx.scdd.TrainPlanDetailSelect.win == null){
        jx.scdd.TrainPlanDetailSelect.win = new Ext.Window({
            title:'选择生产计划明细', closeAction:"hide", width:650, height:400, layout:"fit", resizable:false, modal:true, 
            items:jx.scdd.TrainPlanDetailSelect.grid
        });
    }
}
//创建选择表格
jx.scdd.TrainPlanDetailSelect.createGrid = function(){
    if(jx.scdd.TrainPlanDetailSelect.grid)  return;
    
	jx.scdd.TrainPlanDetailSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainEnforcePlanDetail!trainPlanDetailSelect.action',                 //装载列表数据的请求URL
	    viewConfig: null, storeAutoLoad:false,    //设置grid的store为手动加载(不设置false会引起参数排序失效)
	    searchFormColNum:2, singleSelect: true, 
	    tbar: ['search','-','状态： ',{
            xtype:"checkbox",id:"tpds$noStartChk", boxLabel:"新编制&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, inputValue:10,
            handler:function(){
                jx.scdd.TrainPlanDetailSelect.grid.store.load();                
        }},{   
            xtype:"checkbox",id:"tpds$redemptionChk", boxLabel:"已兑现&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, inputValue:20,
            handler:function(){
                jx.scdd.TrainPlanDetailSelect.grid.store.load();
        }},{   
            xtype:"checkbox",id:"tpds$completeChk", boxLabel:"已完成", inputValue:30,
            handler:function(){
                jx.scdd.TrainPlanDetailSelect.grid.store.load();
            }
        }],
	    fields: [{
	        header:'idx主键', dataIndex:'idx', hidden:true
	    },{ 
	        header:'配属局名称', dataIndex:'bName', hidden:true
	    },{ 
	        header:'配属段名称', dataIndex:'dNAME', hidden:true
	    },{
	        header:'机车施修计划主键', dataIndex:'trainEnforcePlanIDX', hidden: true
	    },{
	        header:'计划状态', dataIndex:'planStatus', width:65, renderer : function(v){
	            if(v == 10)    return "新编制";
	            if(v == 20)    return "<font color='orange' style='font-weight:bold;'>已兑现</font>";
	            if(v == 30)    return "<font color='green'  style='font-weight:bold;'>已完成</font>";
	            return v;
	        }
        },{ 
            header:'工作号', dataIndex:'workNumber'            
	    },{
	        header:'车型', dataIndex:'trainTypeIDX', hidden: true
	    },{
	        header:'车型', dataIndex:'trainTypeShortName'
	    },{
	        header:'车号', dataIndex:'trainNo'
	    },{
	        header:'配属局ID', dataIndex:'bid', hidden: true
	    },{
	        header:'配属段ID', dataIndex:'did', hidden: true
	    },{
	        header:'配属局', dataIndex:'bShortName'
	    },{
	        header:'配属段', dataIndex:'dShortName'
	    },{
	        header:'支配单位', dataIndex:'usedDShortName', hidden:true
	    },{
	        header:'承修部门', dataIndex:'undertakeOrgName'
	    },{
	        header:'修程', dataIndex:'repairClassIDX', hidden:true
	    },{
	        header:'修程', dataIndex:'repairClassName'
	    },{
	        header:'修次主键', dataIndex:'repairtimeIDX', hidden:true
	    },{
	        header:'修次', dataIndex:'repairtimeName'
	    },{
	        header:'计划进厂日期', dataIndex:'planStartDate', width:90, xtype:'datecolumn',hidden:true
	    },{
	        header:'计划交车日期', dataIndex:'planEndDate', width:90,xtype:'datecolumn', hidden:true
	    },{
	        header:'承修部门', dataIndex:'undertakeOrgId', hidden: true
	    },{
	        header:'承修部门序列', dataIndex:'undertakeOrgSeq', hidden:true
	    },{   
	        header:'支配段ID', dataIndex:'usedDId', hidden: true
	    },{
	        header:'支配段名称', dataIndex:'usedDName', hidden:true
	    },{
	        header:'备注', dataIndex:'remarks', hidden:true
	    }],
        searchOrder:['trainTypeShortName','trainNo','bShortName','dShortName','workNumber'],
        toEditFn:function(grid, rowIndex, e){
            jx.scdd.TrainPlanDetailSelect.returnFn(grid, rowIndex, e);
            jx.scdd.TrainPlanDetailSelect.win.hide();
        }
	});
    jx.scdd.TrainPlanDetailSelect.grid.store.on('beforeload', function(){
	    var whereList = [];
	//    计划状态 checkbox的组装
	    var status = [];
	    if(Ext.getCmp('tpds$noStartChk').checked)    status.push(Ext.getCmp('tpds$noStartChk').inputValue);
	    if(Ext.getCmp('tpds$redemptionChk').checked)    status.push(Ext.getCmp('tpds$redemptionChk').inputValue);
        if(Ext.getCmp('tpds$completeChk').checked)    status.push(Ext.getCmp('tpds$completeChk').inputValue);
	    if(status.length < 1)   status.push(-1);
	    whereList.push({propName:"planStatus", compare:Condition.IN, propValues:status});
	//    查询表单中的参数的组装
	    if(jx.scdd.TrainPlanDetailSelect.grid.searchForm != null){
	        var searchParam = jx.scdd.TrainPlanDetailSelect.grid.searchForm.getForm().getValues();
	        searchParam = MyJson.deleteBlankProp(searchParam);
	        for(prop in searchParam){
	            whereList.push({propName:prop, compare:Condition.EQ, propValue:searchParam[ prop ]});
	        }
	    }
	    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);        
    });
    jx.scdd.TrainPlanDetailSelect.grid.store.setDefaultSort('trainNo', 'ASC');
}

//注册为Ext容器组件
Ext.reg('TrainPlanDetailSelect', jx.scdd.TrainPlanDetailSelect);