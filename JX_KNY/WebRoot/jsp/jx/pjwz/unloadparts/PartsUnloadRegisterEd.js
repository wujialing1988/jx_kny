/**
 * 下车配件登记单--已登记 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsUnloadRegisterEd');                       //定义命名空间
PartsUnloadRegisterEd.fieldWidth = 160;
PartsUnloadRegisterEd.labelWidth = 90;
PartsUnloadRegisterEd.searchParam = {};
PartsUnloadRegisterEd.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsUnloadRegister!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsUnloadRegister!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsUnloadRegister!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad : false,
    viewConfig : [],
    tbar: [],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'单据编号', dataIndex:'billNo', hidden:true, editor:{ maxLength:50 }
	},{
		header:'规格型号', dataIndex:'specificationModel', width:150,editor:{  maxLength:100 }
	},{
		header:'配件名称', dataIndex:'partsName', editor:{  maxLength:100 }
	},{
		header:'配件编号', dataIndex:'partsNo', width:120,editor:{  maxLength:50 }
	},{
		header:'走行公里', dataIndex:'runingKM',width:100, editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'下车车型', dataIndex:'unloadTrainType', width:80,editor:{  maxLength:50 }
	},{
		header:'下车车型编码', dataIndex:'unloadTrainTypeIdx',hidden:true, editor:{  maxLength:8 }
	},{
		header:'下车车号', dataIndex:'unloadTrainNo', width:80,editor:{  maxLength:50 }
	},{
		header:'下车修程编码', dataIndex:'unloadRepairClassIdx', hidden:true,editor:{  maxLength:8 }
	},{
		header:'下车修程', dataIndex:'unloadRepairClass',width:80, editor:{  maxLength:50 }
	},{
		header:'下车修次编码', dataIndex:'unloadRepairTimeIdx',hidden:true, editor:{  maxLength:8 }
	},{
		header:'下车修次', dataIndex:'unloadRepairTime', width:80,editor:{  maxLength:50 }
	},{
		header:'接收部门主键', dataIndex:'takeOverDeptId',hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'接收部门', dataIndex:'takeOverDept', width:150,editor:{  maxLength:100 }
	},{
		header:'接收部门序列', dataIndex:'takeOverDeptOrgseq',hidden:true, editor:{  maxLength:512 }
	},{
		header:'接收人主键', dataIndex:'takeOverEmpId', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接收人', dataIndex:'takeOverEmp', width:80,width:80,editor:{  maxLength:50 }
	},{
		header:'交件人主键', dataIndex:'handOverEmpId',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'交件人', dataIndex:'handOverEmp', width:80,width:80,editor:{  maxLength:25 }
	},{
		header:'存放地点', dataIndex:'location',width:150, editor:{  maxLength:100 }
	},{
		header:'下车原因', dataIndex:'unloadReason',width:150, editor:{  maxLength:100 }
	},{
		header:'下车位置', dataIndex:'unloadPlace',width:150, editor:{  maxLength:100 }
	},{
		header:'下车日期', dataIndex:'unloadDate', width:150,xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'出厂日期', dataIndex:'factoryDate',width:150, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'接收部门类型', dataIndex:'takeOverDeptType',hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'配件规格型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'生产厂家主键', dataIndex:'madeFactoryIdx',hidden:true, editor:{  maxLength:5 }
	},{
		header:'生产厂家', dataIndex:'madeFactoryName', width:150,editor:{  maxLength:50 }
	},{
		header:'详细配置', dataIndex:'configDetail', width:150,editor:{  maxLength:200 }
	},{
		header:'单据状态', dataIndex:'status',hidden:true, editor:{  maxLength:20 }
	},{
		header:'创建人名称', dataIndex:'creatorName', hidden:true,editor:{  maxLength:50 }
	}]
});
//移除监听
PartsUnloadRegisterEd.grid.un('rowdblclick', PartsUnloadRegisterEd.grid.toEditFn, PartsUnloadRegisterEd.grid);
//查询前添加过滤条件
PartsUnloadRegisterEd.grid.store.on('beforeload',function(){
	var searchParam = PartsUnloadRegisterEd.searchParam;
	searchParam.status = STATUS_ED;  //状态--已登记
	searchParam.rdpIdx = TrainWorkPlanView.rdpIdx;  
    this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});