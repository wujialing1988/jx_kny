/**
 * 机车检修项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RepairProject');                       //定义命名空间

// 按钮【定制打印模块】触发的函数处理
RepairProject.definePrinterFn = function() {
	var sm = RepairProject.grid.getSelectionModel();
	if (sm.getCount() <= 0) {
		MyExt.Msg.alert("尚未选择任何记录！");
		return;
	}
	// 设置报表业务关联 - 业务主键
	ReportMgr.businessIDX = sm.getSelections()[0].get('idx');
	// 设置报表打印模板 - 报表部署目录
	ReportMgr.deployCatalog = "pjjx.repairProject";
	// 显示打印模板定制窗口
	ReportMgr.win.show();
}

RepairProject.searchParam = {} ;                    //定义查询条件
var loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
RepairProject.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/repairProject!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/repairProject!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/repairProject!deleteRepairProjectAndWorkSeq.action',            //由于不使用用工单维护功能，该处删除会删除作业工单（级联删除）
    tbar:['search',{
    	text:"新增",iconCls:"addIcon" ,handler: function(){
            RepairProjectEdit.addFn();//新增按钮方法
    	}
    },'delete','refresh',{
		text: '定制打印模板', iconCls: 'page_edit_1Icon', handler: RepairProject.definePrinterFn
	}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"entityID", xtype:'hidden' }
	},{
		header:'编码', dataIndex:'repairProjectCode', editor:{id:"repairProjectCodeId", maxLength:20 }
	},{
		header:'机组型号主键', dataIndex:'pTrainTypeIdx',hidden:true, editor:{  
			id:"pTrainTypeIdx_comb",
			fieldLabel: "机组型号",
	    	xtype: "Base_combo",
			hiddenName: "pTrainTypeIdx",
		    business: 'trainVehicleType',
		    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
            fields:['idx','typeName','typeCode'],
            queryParams: {'vehicleType':vehicleType},// 表示客货类型
		    displayField: "typeCode", valueField: "idx",
            pageSize: 20, minListWidth: 200,
            returnField: [{widgetId:"pTrainTypeShortname",propertyName:"typeCode"}],
            allowBlank:false,
            editable:false	
		}
	},{
		header:'额定工时(分)', dataIndex:'ratedWorkHours', editor:{  maxLength:2 ,vtype: "positiveInt"},searcher: {disabled: true}
	},{
		header:'机组型号', dataIndex:'pTrainTypeShortname',editor:{ xtype:'hidden', id:'pTrainTypeShortname', maxLength:50 },searcher: {disabled: false}
	},{
		header:'名称', dataIndex:'repairProjectName', editor:{ allowBlank:false, maxLength:50 }
	},{
		header:'备注', dataIndex:'remark', editor:{ xtype:'textarea',height:80,  maxLength:1000 },searcher: {disabled: true}
	},{
		header:'客货类型', dataIndex:'vehicleType', hidden: true,editor:{ xtype:'hidden',value:vehicleType }
	}],
    searchFn: function(searchParam){ 
		RepairProject.searchParam = searchParam ;
        this.store.load();
	},
	toEditFn: function(grid, rowIndex, e){
        RepairProjectEdit.toEditFn(grid, rowIndex, e);
    }
});
//初始化表单
RepairProject.grid.createSaveForm(); 

//编辑选项卡列表
RepairProject.editTabs = new Ext.TabPanel({
    activeTab: 0, enableTabScroll:true, border:false,
    items:[{
            title: "基本信息", layout: "fit", frame: true, border: false, items: {
            	bodyStyle: 'padding:30,50;',
            	layout: "fit",
            	buttonAlign: "center",
            	items: [ RepairProject.grid.saveForm ],
            	buttons: [{
		    		id:"trainProjectSaveId", text: "保存", iconCls:"saveIcon", handler:function(){
						RepairProjectEdit.saveFn(RepairProject.grid) ; //调用新增方法（RepairProjectEdit.js）中            			
		    		}
		    	},{
					text: "关闭", iconCls:"closeIcon", handler:function(){RepairProject.saveWin.hide();}
				}]
            }
        },{
            title: "检修记录卡", layout: "fit", border: false, items: [ TrainQR.grid ]
        }
        ]
});

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items: RepairProject.grid });
//定义工序卡编辑窗口
RepairProject.saveWin = new Ext.Window({
	title: "检修记录单编辑", maximizable:true, width:750, height:415, layout: "fit", 
	closeAction: "hide", modal: true, buttonAlign:"center",
	items: [RepairProject.editTabs] 
});

//添加过滤默认过滤信息
RepairProject.grid.store.on('beforeload',function(){
	var searchParam = RepairProject.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {
        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
	}
	whereList.push({propName:'vehicleType',propValue:vehicleType,compare:Condition.EQ,stringLike: false});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	
});
});