/**
 * 机车生产计划明细查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PlanDetailSearchView');                       //定义命名空间
//年store data，从2008年到当前年
var yearData = [];
var now = new Date();
var currentYear = now.getFullYear();         //当前年
for (var i = 2008; i <= currentYear + 1; i++) {
    var year = [ i ];
    yearData.push(year);
}
var currentMonth = now.getMonth();      //当前月
PlanDetailSearchView.selMonth = currentMonth + 1;   //树节点选中月
var selYearMonth = currentYear + '年' + (currentMonth + 1) + '月';

PlanDetailSearchView.reportTotalStr = "" ;                   //报表统计信息
PlanDetailSearchView.searchParam = "" ;                   //全局查询条件
PlanDetailSearchView.startPlanDate = "" ;                   //生产计划开始时间
PlanDetailSearchView.endPlanDate = "" ;                     //生产计划结束时间

var COLOR_PLAN = '#FFEE33';
var COLOR_REDEMPTION = '#8FD1F5';
var COLOR_COMPLETE = '#0C6';
var colorDemo = "<div align='right' border='0' style='float:right'>" +
                    "<span style='width:15px;background-color:" + COLOR_PLAN + ";border:1px #FFF solid'>&nbsp;</span>&nbsp;<span style='vertical-align:top;'>未执行</span>&nbsp;"+
                    "<span style='width:15px;background-color:" + COLOR_REDEMPTION + ";border:1px #FFF solid'>&nbsp;</span>&nbsp;<span style='vertical-align:top;'>执行中</span>&nbsp;"+
                    "<span style='width:15px;background-color:" + COLOR_COMPLETE + ";border:1px #FFF solid'>&nbsp;</span>&nbsp;<span style='vertical-align:top;'>已完成</span>"+
                "</div>";
PlanDetailSearchView.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetail!searchPlanDetail.action',                 //装载列表数据的请求URL
    searchFormColNum:2, singleSelect:true, 
    viewConfig: {markDirty: false}, 
    storeAutoLoad:false, labelWidth:120,
    tbar: ['search',{
        	text:"打印", iconCls:"printerIcon", handler: function(){
        		var searchParam = PlanDetailSearchView.searchParam ; //查询表单
        		var trainType = searchParam.trainTypeShortName == undefined ? "" : searchParam.trainTypeShortName ;
        		var trainNo = searchParam.trainNo == undefined ? "" : searchParam.trainNo ;
        		var bShortName = searchParam.bShortName == undefined ? "" : searchParam.bShortName ;
        		var dShortName = searchParam.dShortName == undefined ? "" : searchParam.dShortName ;
        		var undertakeOrgName = searchParam.undertakeOrgName == undefined ? "" : searchParam.undertakeOrgName ;
        		var planStartDate = searchParam.planStartDate == undefined ? "" : searchParam.planStartDate ;
        		var planStartDate_End = searchParam.planStartDate_End == undefined ? "" : searchParam.planStartDate_End ;
        		var planEndDate = searchParam.planEndDate == undefined ? "" : searchParam.planEndDate ;
        		var planEndDate_End = searchParam.planEndDate_End == undefined ? "" : searchParam.planEndDate_End ;
        		var  startPlanDate= PlanDetailSearchView.startPlanDate; //生产计划开始日期 大于等于
        		var  endPlanDate= PlanDetailSearchView.endPlanDate;  //生产计划开始日期 小于等于
        		var planStatus = -1; //报表状态
			    if(Ext.getCmp('noStartChk').checked)    planStatus = planStatus +","+ Ext.getCmp('noStartChk').inputValue;
			    if(Ext.getCmp('redemptionChk').checked)    planStatus = planStatus +","+Ext.getCmp('redemptionChk').inputValue;
			    if(Ext.getCmp('completeChk').checked)    planStatus = planStatus +","+Ext.getCmp('completeChk').inputValue;
	    		var reportTotalStr = PlanDetailSearchView.reportTotalStr ;
		        var url = ctx + "/jsp/jx/scdd/enforceplan/ProducePlanGather.jsp?totalStr="+reportTotalStr
		        +"&planStatus="+planStatus+"&trainType="+trainType+"&trainNo="+trainNo+"&bShortName="+bShortName
		        +"&dShortName"+dShortName+"&undertakeOrgName="+undertakeOrgName
		        +"&planStartDate="+planStartDate+"&planStartDate_End="+planStartDate_End
		        +"&planEndDate="+planEndDate+"&planEndDate_End="+planEndDate_End
		        +"&startPlanDate="+startPlanDate+"&endPlanDate="+endPlanDate;
				window.open(url); //转码后的数据
        	} 
        },'refresh','-','状态： ',{
            xtype:"checkbox",id:"noStartChk", boxLabel:"未执行&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, inputValue:STATUS_PLAN,
            handler:function(){
                PlanDetailSearchView.grid.store.load();                
        }},{   
            xtype:"checkbox",id:"redemptionChk", boxLabel:"执行中&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, inputValue:STATUS_REDEMPTION,
            handler:function(){
                PlanDetailSearchView.grid.store.load();
        }},{   
            xtype:"checkbox",id:"completeChk", boxLabel:"已完成", inputValue:STATUS_COMPLETE,
            handler:function(){
                PlanDetailSearchView.grid.store.load();
        }},'->', colorDemo
    ],
    fields: [{
        header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
    },{
        header:'生产计划开始日期', dataIndex:'startPlanDate', xtype:'datecolumn',hidden:true       
    },{
        header:'机车施修计划主键', dataIndex:'trainEnforcePlanIDX', hidden:true, editor:{ xtype:'hidden',  maxLength:50 }
    },{
        header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{ xtype:'hidden',  maxLength:8 }
    },{
        header:'车型', dataIndex:'trainTypeShortName', width:60, editor:{  maxLength:8 },
        renderer: function(value, x, record){
            var planStatus = record.get('planStatus');
            if(planStatus == STATUS_PLAN){
                return "<div style='background-color:" + COLOR_PLAN + ";'>" + value + "</div>";
            } else if(planStatus == STATUS_REDEMPTION){
                return "<div style='background-color:" + COLOR_REDEMPTION + ";'>" + value + "</div>";
            } else if(planStatus == STATUS_COMPLETE){
                return "<div style='background-color:" + COLOR_COMPLETE + ";'>" + value + "</div>";
            }
        }        
    },{
        header:'车号', dataIndex:'trainNo', width:60, editor:{  maxLength:50 },
        renderer: function(value, x, record){
            var planStatus = record.get('planStatus');
            if(planStatus == STATUS_PLAN){
                return "<div style='background-color:" + COLOR_PLAN + ";'>" + value + "</div>";
            } else if(planStatus == STATUS_REDEMPTION){
                return "<div style='background-color:" + COLOR_REDEMPTION + ";'>" + value + "</div>";
            } else if(planStatus == STATUS_COMPLETE){
                return "<div style='background-color:" + COLOR_COMPLETE + ";'>" + value + "</div>";
            }
        }        
    },{
        header:'修程主键', dataIndex:'repairClassIDX', hidden:true, editor:{ xtype:'hidden',  maxLength:8 }
    },{
        header:'修程', dataIndex:'repairClassName', width:60, searcher:{disabled:true}
    },{
        header:'修次主键', dataIndex:'repairtimeIDX', hidden:true, editor:{ xtype:'hidden',  maxLength:8 }
    },{
        header:'修次', dataIndex:'repairtimeName', width:60, searcher:{disabled:true}   
    },{
        header:'配属局ID', dataIndex:'bid', hidden:true, editor:{ xtype:'hidden',  maxLength:10 }
    },{
        header:'配属局', dataIndex:'bName',editor:{  maxLength:50 }
    },{
        header:'配属局简称', dataIndex:'bShortName', hidden:true, width:150, editor:{  maxLength:50 }
    },{
        header:'配属段ID', dataIndex:'did', hidden:true, editor:{ xtype:'hidden',  maxLength:20 }
    },{
        header:'配属段', dataIndex:'dNAME', editor:{  maxLength:50 }
    },{
        header:'配属段简称', dataIndex:'dShortName', hidden:true, width:150, editor:{ maxLength:50 }
    },{
        header:'工作号', dataIndex:'workNumber', hidden:true, editor:{xtype:'hidden',maxLength:50 }
    },{
        header:'承修部门ID', dataIndex:'undertakeOrgId', hidden:true, editor:{ xtype:'hidden', xtype:'numberfield', maxLength:10 }
    },{
        header:'承修部门序列', dataIndex:'undertakeOrgSeq', hidden:true, editor:{ xtype:'hidden',  maxLength:512 }
    },{
        header:'承修部门', dataIndex:'undertakeOrgName', hidden:true, editor:{  maxLength:300 }
    },{
        header:'计划修车日期', dataIndex:'planStartDate', xtype:'datecolumn', searcher:{xtype:'my97date', initNow:false}
    },{
        header:'计划交车日期', dataIndex:'planEndDate', xtype:'datecolumn', searcher:{xtype:'my97date', initNow:false}
    },{
        header:'计划状态', dataIndex:'planStatus', hidden:true, editor:{ xtype:'hidden'}
    },{
        header:'备注', dataIndex:'remarks', hidden:true, editor:{xtype:'hidden'}, searcher:{disabled:true}
    }],
    searchOrder:[
        {name:'toStartDate', id:'toStartDate', xtype:'displayfield', fieldLabel:'计划开始日期', value:selYearMonth}, 'undertakeOrgName',
        {name:'planStartDate', fieldLabel:'计划进车日期(开始)', xtype:'my97date', initNow:false}, 
        {name:'planStartDate_End', fieldLabel:'计划进车日期(结束)', xtype:'my97date', initNow:false}, 
        {name:'planEndDate', fieldLabel:'计划交车日期(开始)', xtype:'my97date', initNow:false},
        {name:'planEndDate_End', fieldLabel:'计划交车日期(结束)', xtype:'my97date', initNow:false},    
        'trainTypeShortName', 'trainNo', 'bName', 'dNAME'
    ],
    toEditFn:function(){}
});
//月节点数组
var monthNode = [
    {text: "1月", leaf: true, month:1},
    {text: "2月", leaf: true, month:2},
    {text: "3月", leaf: true, month:3},
    {text: "4月", leaf: true, month:4},
    {text: "5月", leaf: true, month:5},
    {text: "6月", leaf: true, month:6},
    {text: "7月", leaf: true, month:7},
    {text: "8月", leaf: true, month:8},
    {text: "9月", leaf: true, month:9},
    {text: "10月", leaf: true, month:10},
    {text: "11月", leaf: true, month:11},
    {text: "12月", leaf: true, month:12}
];
//列表页面排序规则 1. 计划状态(未执行,已执行,已完成) 2.承修部门 3.机车车型 4.机车车号 
PlanDetailSearchView.grid.store.setDefaultSort('planStatus,undertakeOrgName,trainTypeShortName,trainNo,planStartDate', 'ASC');
//在store.load前，组装查询参数
PlanDetailSearchView.grid.store.on('beforeload', function(){
    var whereList = [];
//    计划开始时间 年 月的组装
    var yearCombo = Ext.getCmp('yearCombo');
    var year = yearCombo == null ? null : yearCombo.getValue();
    year = (year == null || year == '') ? currentYear : year;
    year = parseInt(year);
    this.baseParams.year = year;

    var month = PlanDetailSearchView.selMonth;
    month = month == null ? null : parseInt(month);
    this.baseParams.month = month;
    
    var startPlanDate_ge = null;
    var startPlanDate_lt = null;
    if(month == null || month == ''){
         startPlanDate_ge = year + '-01-01';
         startPlanDate_lt = (year + 1) + '-01-01';
    } else {
        if(month >= 12){
            startPlanDate_ge = year + '-' + month + '-01';
            startPlanDate_lt = (year + 1) + '-01-01';                  
        } else {
            startPlanDate_ge = year + '-' + month + '-01';
            startPlanDate_lt = year + '-' + (month + 1) + '-01';
        }
    }
    PlanDetailSearchView.startPlanDate = startPlanDate_ge ; //大于等于开始时间 报表使用
    PlanDetailSearchView.endPlanDate = startPlanDate_lt ;  //小于等于结束时间  报表使用
    whereList.push({propName:"startPlanDate", compare:Condition.GE, propValue:startPlanDate_ge});
    whereList.push({propName:"startPlanDate", compare:Condition.LT, propValue:startPlanDate_lt});
//    计划状态 checkbox的组装
    var planStatus = [];
    if(Ext.getCmp('noStartChk').checked)    planStatus.push(Ext.getCmp('noStartChk').inputValue);
    if(Ext.getCmp('redemptionChk').checked)    planStatus.push(Ext.getCmp('redemptionChk').inputValue);
    if(Ext.getCmp('completeChk').checked)    planStatus.push(Ext.getCmp('completeChk').inputValue);
    if(planStatus.length < 1)   planStatus.push(-1);
    whereList.push({propName:"planStatus", compare:Condition.IN, propValues:planStatus});
//    查询表单中的参数的组装
    if(PlanDetailSearchView.grid.searchForm != null){
        var searchParam = PlanDetailSearchView.grid.searchForm.getForm().getValues();
        delete searchParam.toStartDate;
        
        PlanDetailSearchView.searchParam = searchParam;
        searchParam = MyJson.deleteBlankProp(searchParam);
        if(searchParam.planStartDate){
            whereList.push({propName:'planStartDate', compare:Condition.GE, propValue:searchParam.planStartDate});
            delete searchParam.planStartDate;
        }
        if(searchParam.planStartDate_End){
            whereList.push({propName:'planStartDate', compare:Condition.LE, propValue:searchParam.planStartDate_End});
            delete searchParam.planStartDate_End;
        }
        if(searchParam.planEndDate){
            whereList.push({propName:'planEndDate', compare:Condition.GE, propValue:searchParam.planEndDate});
            delete searchParam.planEndDate;
        }
        if(searchParam.planEndDate_End){
            whereList.push({propName:'planEndDate', compare:Condition.LE, propValue:searchParam.planEndDate_End});
            delete searchParam.planEndDate_End;
        }
        for(prop in searchParam){
            whereList.push({propName:prop, compare:Condition.EQ, propValue:searchParam[ prop ]});
        }
    }
    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
    
//    避免重置查询动作修改计划开始日期
    var toStartDate = Ext.getCmp('toStartDate');
    if(toStartDate)   toStartDate.setValue(year + '年' + (month == null ? '' : month + '月') );
});

//页面自适应布局
var viewport = new Ext.Viewport({ 
    layout:"border", border: false,
    items:[{
        region:'west', layout: "fit", autoScroll:true, width:140, minSize:100, maxSize:200, split: true, bodyBorder:false, 
		tbar:{
	        xtype:"container", border:false,
	        items:[{
	            xtype:"toolbar",border:false,items:['计划开始日期']
	        },{
	            xtype:"toolbar",border:false,
	            items:[{
		            xtype:"combo", id:"yearCombo", hiddenName:"year", displayField:"year",
		            width:60, valueField:"year", value:currentYear, mode:"local",triggerAction: "all",
		            store:new Ext.data.SimpleStore({
		                fields: ["year"],
		                data: yearData
		            }),
		            listeners:{
		                'select': function(yearCombo){ 
		                    var selMonth = Ext.getCmp('monthTree').selModel.getSelectedNode().attributes.month;
		                    selMonth = selMonth == null ? '' : (selMonth + '月');
		                    var selYearMonth = yearCombo.getValue() + '年' + selMonth;
		                    Ext.getCmp('toStartDate').setValue(selYearMonth);
		                    PlanDetailSearchView.grid.store.load(); 
	                    }
		            }
	            },'年']
	        }]
		},
        items: {
	        xtype:'treepanel', id:'monthTree', autoScroll:true, split:true, border:false, rootVisible:true, loader:new Ext.tree.TreeLoader(),
	        root: new Ext.tree.AsyncTreeNode({
	            text:'全年计划', expanded:true, children:monthNode
	        }),
	        listeners: {
	            'click': function(node, e) {
                    PlanDetailSearchView.selMonth = node.attributes.month;
                    var selMonth = PlanDetailSearchView.selMonth;
                    selMonth = selMonth == null ? '' : (selMonth + '月');
                    var selYearMonth = Ext.getCmp('yearCombo').getValue() + '年' + selMonth;
                    if(Ext.getCmp('toStartDate'))   Ext.getCmp('toStartDate').setValue(selYearMonth);
                    PlanDetailSearchView.grid.store.load();
	            }
	        }            
        }
    },{
        region:'center', layout:'fit', bodyBorder: false, items:PlanDetailSearchView.grid
    }]
});

PlanDetailSearchView.grid.store.load();
Ext.getCmp('monthTree').root.childNodes[ currentMonth ].select();
PlanDetailSearchView.grid.createSearchWin();    //预先生成查询窗口及表单
PlanDetailSearchView.grid.searchWin.show();
PlanDetailSearchView.grid.searchWin.hide();

});
