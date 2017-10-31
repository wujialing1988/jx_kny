/**
 * 机车施修计划明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainEnforcePlanDetail');                       //定义命名空间
TrainEnforcePlanDetail.searchParams = {};						//全局查询参数集
var results = [];
//*********************************************  
/** 获取当前月份的第一天和最后一天*/
TrainEnforcePlanDetail.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var MonthFirstDay=new Date(currentYear,currentMonth,1);
	var MonthNextFirstDay=new Date(currentYear,currentMonth+1,1);
 	var MonthLastDay=new Date(MonthNextFirstDay-86400000);

	if(arg == 'begin'){
		return MonthFirstDay.format('Y-m-d');
	}
	else if (arg == 'end'){
		return MonthLastDay.format('Y-m-d');
	}
} 

/**
 * 验证日期
 * 1. 验证计划修车日期与主计划起始和结束日期的关系
 * 2. 验证计划修车日期与出厂日期的先后
 */
TrainEnforcePlanDetail.checkDate = function(startDate, endDate, Trigger){
	var dateOne = new Date(Ext.getCmp(startDate).getValue()); //计划修车日期
	var dateTwo = new Date(Ext.getCmp(endDate).getValue());   //计划交车日期
	var mainDateBegin = new Date(TrainEnforcePlan.planStartDate.replace(/-/g,'/')); //主计划开始日期
	var mainDateEnd   = new Date(TrainEnforcePlan.planEndDate.replace(/-/g,'/'));   //主计划结束日期
	var message = ""; //错误提示信息
	if( dateOne != 'undefined' && !isNaN(dateOne) && dateOne != null && dateOne != ""
		&& dateTwo != 'undefined' && !isNaN(dateTwo) && dateTwo != null && dateTwo != "" ){
		if( dateOne < mainDateBegin || dateOne > mainDateEnd){
			/** 如果计划修车日期不在主计划开始与结束日期之间,则返回错误信息,并重置计划修车日期为主计划开始日期 */
			message = "计划修车日期需在("+TrainEnforcePlan.planStartDate+"~"+TrainEnforcePlan.planEndDate+")之间";
			Ext.getCmp(startDate).setValue(TrainEnforcePlan.planStartDate);
		} else if (Trigger) {
			if ( dateOne > dateTwo ){
				/** 如果计划修车日期晚于计划交车日期,给予提示,并重置计划交车日期 */
				message ="计划交车日期早于计划修车日期";
				Ext.getCmp(endDate).setValue(TrainEnforcePlan.planEndDate);
			}
		}
	} else {
		if( dateOne != 'undefined' || !isNaN(dateOne) ||dateOne == null || dateOne == "" ){
			message ="请正确填报计划交车日期";
			Ext.getCmp(startDate).setValue(mainDateBegin);
		}
		
	}
	if(message != ""){
		MyExt.Msg.alert(message);
	}
	return;
}

//预设表单元素序列, 根据参数对这些元素设置setDisabled属性,以实现其批量禁用或启用
TrainEnforcePlanDetail.disabledTargetEl = function(arg){
	Ext.getCmp("trainType_comb").setDisabled(arg);
	Ext.getCmp("trainNo_comb").setDisabled(arg);
	Ext.getCmp("rc_comb").setDisabled(arg);
	Ext.getCmp("rt_comb").setDisabled(arg);
	Ext.getCmp("planStartDate_detailId").setDisabled(arg);
	Ext.getCmp("planEndDate_detailId").setDisabled(arg);
	Ext.getCmp("remarks_id").setDisabled(arg);
}

//机车施修计划明细列表
TrainEnforcePlanDetail.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetail!findPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlanDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlanDetail!logicDelete.action',            //删除数据的请求URL
    viewConfig: null,
    storeAutoLoad:false,	//设置grid的store为手动加载(不设置false会引起参数排序失效)
    searchFormColNum: 2,
    saveFormColNum: 2, 
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	{
		header:'机车施修计划主键', dataIndex:'trainEnforcePlanIDX', hidden: true, editor:{  xtype: 'hidden' }
	},{
		header:'计划', dataIndex:'planStatus', width:65, renderer : function(v){
						if(v == status_detail_formation)return "未执行";
						else if(v == status_detail_redemption)return "<font color='orange' style='font-weight:bold;'>执行中</font>";
						else if(v == status_detail_complete) return "<font color='green'  style='font-weight:bold;'>已完成</font>";
						else return "";
					}
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden: true 
	},
	{
		header:'车型', dataIndex:'trainTypeShortName', width:65, editor:{
		xtype: 'hidden' },
		searcher:{xtype: 'textfield', anchor:'98%'}
	},{
		header:'车号', dataIndex:'trainNo', width:65,
		searcher:{xtype: 'textfield', anchor:'98%'}
	},{
		header:'修程', dataIndex:'repairClassIDX', hidden: true
	},{
		header:'修程', dataIndex:'repairClassName', width:65,
		searcher:{anchor:'98%'}
	},{
		header:'修次', dataIndex:'repairtimeName', width:65
	},{
		header:'修次主键', dataIndex:'repairtimeIDX', hidden:true	
	},{
		header:'上次修程', dataIndex:'lastRepairClassIDX', hidden: true
	},{
		header:'上次修程', dataIndex:'lastRepairClassName', width:65,
		searcher:{anchor:'98%'}
	},{
		header:'上次修次', dataIndex:'lastRepairtimeName', width:65
	},{		
		header:'上次修次主键', dataIndex:'lastRepairtimeIDX', hidden:true
	},{
		header:'计划入段日期', dataIndex:'planStartDate', width:90, xtype:'datecolumn', 
		searcher:{id:'planStartDate_searchId',disabled: true}
	},{
		header:'计划离段日期', dataIndex:'planEndDate', width:90,xtype:'datecolumn',
		searcher:{disabled: true}
	},{
		header:'不良状态预报', dataIndex:'remarks', 
		searcher:{disabled: true}
	}],
	searchOrder:[
		'trainTypeShortName','trainNo'
	],
	tbar:['search','add','delete',{
			text:"刷新", iconCls:"refreshIcon", handler: function(){
				TrainEnforcePlanDetail.grid.store.load();
		    } 
		}
	],
    /**
     * 进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
     * 该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
    beforeShowSaveWin: function(){ 
    	TrainEnforcePlanDetail.baseForm.getForm().reset();
    	/** 设置新增计划明细时的计划修车和出厂时间默认为计划主单的计划开始日期和结束日期 */
		Ext.getCmp("planStartDate_detailId").setValue(TrainEnforcePlan.planStartDate);
		Ext.getCmp("planEndDate_detailId").setValue(TrainEnforcePlan.planStartDate);
    },
	 /**
     * 显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
	afterShowSaveWin: function(){
		/** 设置新增计划明细时的计划修车和出厂时间默认为计划主单的计划开始日期和结束日期 */
		Ext.getCmp("planStartDate_detailId").setValue(TrainEnforcePlan.planStartDate);
		Ext.getCmp("planEndDate_detailId").setValue(TrainEnforcePlan.planEndDate);
		/** 页面控件的值重置 */
		Ext.getCmp("trainType_comb").clearValue();
		Ext.getCmp("trainNo_comb").clearValue();
		Ext.getCmp("rc_comb").clearValue();
		Ext.getCmp("rt_comb").clearValue();
		/** 设置车型默认值,并触发更新默认车型的车号*/
		Ext.getCmp("trainNo_comb").reset();  
	    Ext.getCmp("trainNo_comb").clearValue(); 
		Ext.getCmp("rt_comb").reset();
		Ext.getCmp("rt_comb").clearValue();
		TrainEnforcePlanDetail.disabledTargetEl(false); //表单控件设置可用
		this.saveWin.buttons[0].show();					//保存按钮可见
		return true;
	},
	/**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * 设置“计划状态”为“编制（初始状态）”
     * 设置"机车施修计划主键"
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
	beforeSaveFn: function(data){		
		data.planStatus = status_detail_formation;
		data.trainEnforcePlanIDX = TrainEnforcePlan.idx;
		data.vehicleType = vehicleType ;
		return true;
	},
	/**
     * 保存成功之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
    afterSaveSuccessFn: function(result, response, options){
        TrainEnforcePlanDetail.clearSearchParams(this);
        if(this.saveWin != null){
        	this.saveWin.hide();
        }
        alertSuccess();
    },
	/**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
	afterShowEditWin:function(record, rowIndex){
		TrainEnforcePlanDetail.baseForm.getForm().reset();
		TrainEnforcePlanDetail.baseForm.getForm().loadRecord(record);
//		//设置控件回显值
		Ext.getCmp("trainType_comb").setDisplayValue(record.get("trainTypeIDX"),record.get("trainTypeShortName"));		
		Ext.getCmp("trainNo_comb").queryParams = {"trainTypeIDX":record.get("trainTypeIDX"),"vehicleType":vehicleType};
	    Ext.getCmp("trainNo_comb").cascadeStore();
	    Ext.getCmp("trainNo_comb").setDisplayValue(record.get("trainNo"),record.get("trainNo"));
		Ext.getCmp("rc_comb").setDisplayValue(record.get("repairClassIDX"),record.get("repairClassName"));
		//过滤修程
		var rc_comb = Ext.getCmp("rc_comb");
        rc_comb.queryParams = {"TrainTypeIdx":record.get("trainTypeIDX"),"vehicleType":vehicleType};
        rc_comb.cascadeStore();
		//根据默认的车型,设置该默认车型的默认修程
		Ext.getCmp("rt_comb").reset();
		Ext.getCmp("rt_comb").clearValue();
		Ext.getCmp("rt_comb").setDisplayValue(record.get("repairtimeIDX"),record.get("repairtimeName"));
		if(!Ext.isEmpty(record.get("repairClassIDX"))){								
            Ext.getCmp("rt_comb").queryParams = {"rcIDX":record.get("repairClassIDX")};
            Ext.getCmp("rt_comb").cascadeStore(); 
		}
		if(record.get("planStatus")=='10'){
			TrainEnforcePlanDetail.disabledTargetEl(false); //表单控件设置可用
			TrainEnforcePlanDetail.grid.saveWin.setTitle("编辑");
			this.disableColumns(TrainEnforcePlanDetail.myDataIndexAry);
			TrainEnforcePlanDetail.grid.saveWin.buttons[0].show();					//保存按钮可见
		}else{
			if(record.get("planStatus")==status_detail_redemption)
				TrainEnforcePlanDetail.grid.saveWin.setTitle("计划已兑现!");
			if(record.get("planStatus")==status_detail_complete)
				TrainEnforcePlanDetail.grid.saveWin.setTitle("计划已完成!");
			TrainEnforcePlanDetail.disabledTargetEl(true);
			TrainEnforcePlanDetail.grid.saveWin.buttons[0].hide();
		}		
		return true;
	},
	/**
     * 新增编辑窗口保存按钮触发的函数，执行数据数据保存动作
     */
    saveFn: function(){
        //表单验证是否通过
        var form = TrainEnforcePlanDetail.baseForm.getForm(); 
        if (!form.isValid()) return;
        
        var data = form.getValues();
        
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!this.beforeSaveFn(data)) return;
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: this.saveURL, jsonData: data, params: {smjwd: '1'},
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                } else {
                    this.afterSaveFailFn(result, response, options);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    },
	/**
     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
     * @param searchParam 查询表单的Json对象
     * @return {} 返回的Json数据格式对象,
     */
    searchFn: function(searchParam){
    	//清空查询条件参数
    	TrainEnforcePlanDetail.searchParams = {};
    	this.store.baseParams.startDate = "";
    	this.store.baseParams.overDate = "";
    	//将查询表单数据设置到全局查询参数集
    	for(prop in searchParam){
	    	TrainEnforcePlanDetail.searchParams[prop] = searchParam[prop];
		}
		//将全局查询参数集设置到baseParams，解决分页控件刷新
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(TrainEnforcePlanDetail.searchParams);
    	/***************暂时去掉日期查询*******************/
    	//设置计划开始、结束日期传参
    	var startDate = "";
        var overDate = "";
        searchParam.trainEnforcePlanIDX = TrainEnforcePlan.idx;
		//访问后台，根据查询参数刷新列表
        this.store.load({
            params: {                    
                    entityJson: Ext.util.JSON.encode(searchParam),
                    startDate: startDate,
                    overDate: overDate                    
                }       
        });	
    }
});

TrainEnforcePlanDetail.grid.store.on("beforeload", function(){
	TrainEnforcePlanDetail.searchParams.trainEnforcePlanIDX = TrainEnforcePlan.idx;	
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainEnforcePlanDetail.searchParams);
});

//清空查询参数集并刷新列表
TrainEnforcePlanDetail.clearSearchParams = function(_grid){
	_grid.store.load({
        	params:{
        		entityJson: Ext.util.JSON.encode({trainEnforcePlanIDX : TrainEnforcePlan.idx}),
        		startDate: "",
	            overDate: ""
        	}    
        });
        _grid.store.baseParams.entityJson = Ext.util.JSON.encode({trainEnforcePlanIDX : TrainEnforcePlan.idx});
        _grid.store.baseParams.startDate = "";
        _grid.store.baseParams.overDate = "";
        TrainEnforcePlanDetail.searchParams = {};
};
TrainEnforcePlanDetail.labelWidth = 90;
TrainEnforcePlanDetail.anchor = '95%';
TrainEnforcePlanDetail.fieldWidth = 130;
TrainEnforcePlanDetail.baseForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: TrainEnforcePlanDetail.labelWidth, align:'center',baseCls: "x-plain",
	defaultType:'textfield',defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	defaultType:'textfield', border:false,
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//计划明细IDX主键[隐藏框] dataIndex:'idx'
				{
					fieldLabel:'计划明细IDX',
					name:'idx',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				},
				//计划执行情况[隐藏框] dataIndex:'planStatus'
				{
					fieldLabel:'计划执行情况',
					name:'planStatus',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true,
					value:status_detail_formation
				},
				//客货类型
				{
					fieldLabel:'客货类型',
					name:'vehicleType',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true,
					value:vehicleType
				}				
			]
		},{
			align:'center',	layout:'form',	defaultType:'textfield', baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//计划主表IDX[隐藏框]  dataIndex:'trainEnforcePlanIDX'
				{
					fieldLabel:'计划主表IDX',
					name:'trainEnforcePlanIDX',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				}
			]
		}]
	},{
		/**车型 车号 */
		xtype: 'panel',border:false,layout:'column',align:'center',baseCls: "x-plain",
		items:[{
			align:'center',layout:'form',defaultType:'textfield',baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//车型IDX[控件]  dataIndex:'trainTypeIDX',
				{	
					id:"trainType_comb",	
    				fieldLabel: "车型",
    				width:TrainEnforcePlanDetail.fieldWidth,
    				xtype: "Base_combo",
    				hiddenName: "trainTypeIDX",
    			    business: 'trainVehicleType',
    			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                    fields:['idx','typeName','typeCode'],
                    returnField:[{widgetId:"trainTypeShortName",propertyName:"typeCode"}],
                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
        		    displayField: "typeCode", valueField: "idx",
                    pageSize: 20, minListWidth: 200,
                    allowBlank: false,
                    disabled:false,
                    editable:false,	
					listeners : {
						"select" : function() { 
							//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue(),"vehicleType":vehicleType};
			                trainNo_comb.cascadeStore();
			                //重新加载修程下拉数据
			                var rc_comb = Ext.getCmp("rc_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                var filterParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
			                if(arguments[1].data.repairType){
				                //有修程类型，加上过滤
			                	filterParams.xcType = arguments[1].data.repairType;
			                }
			                rc_comb.queryParams = filterParams;
			                rc_comb.cascadeStore();
			                Ext.getCmp("rt_comb").reset();
			                Ext.getCmp("rt_comb").clearValue();
						}
					}
				},
				//车型简称[隐藏框] dataIndex:'trainTypeShortName'
				{
					fieldLabel:'车型简称',
					name:'trainTypeShortName',
					width:TrainEnforcePlanDetail.fieldWidth,
					id: 'trainTypeShortName',
					hidden:true
				}
			]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//车号名称[控件] dataIndex:'trainNo'
				{
					id:"trainNo_comb",	
    				fieldLabel: "车号",
    				width:TrainEnforcePlanDetail.fieldWidth,
    				xtype: "Base_combo",
    				name:'trainNo',
    				hiddenName: "trainNo",
    			    business: 'jczlTrain',
    			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                    fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
        		    displayField: "trainNo", valueField: "trainNo",
                    pageSize: 20, minListWidth: 200,
                    allowBlank: false,
                    disabled:false,
                    editable:true,
					listeners : {
						"beforequery" : function(){
							//选择车号前先选车型
							var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
							if(trainTypeIdx == "" || trainTypeIdx == null){
								MyExt.Msg.alert("请先选择车型！");
								return false;
							}
						}
					}
				}
			]
		}]
	},{
		/** 修程			修次 */
		xtype: 'panel',	border:false,	layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//修程IDX[控件] dataIndex:'repairClassIDX'
				{
					fieldLabel:'修程',
					id:"rc_comb",
					width:TrainEnforcePlanDetail.fieldWidth,
					disabled:false,
					xtype: "Base_combo",
					hiddenName: "repairClassIDX",
					displayField: "xcName",
					valueField: "xcID",
					queryParams: {'vehicleType':vehicleType},// 表示客货类型
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					emptyText:"- 请选择 -",
					pageSize: 0, 
					minListWidth: 200,
					allowBlank: false,
					returnField: [{widgetId:"repairClassName",propertyName:"xcName"}],
					listeners : {  
						"select" : function() {   
		                	var rt_comb = Ext.getCmp("rt_comb");
		                	rt_comb.clearValue();
		                 	rt_comb.reset();
		                    rt_comb.queryParams = {"rcIDX":this.getValue()};
		                    rt_comb.cascadeStore(); 
		                    TrainEnforcePlanDetail.checkDate('planStartDate_detailId','planEndDate_detailId',false);
                            var rcIDX = this.getValue();
		            	},
		            	"beforequery" : function(){
		            		var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
							if(trainTypeIdx == "" || trainTypeIdx == null){
								MyExt.Msg.alert("请先选择车型！");
								return false;
							}
		            	}
					}
				},
				//修程名称[隐藏框] dataIndex:'repairClassName'
				{
					fieldLabel:'修程',
					id:'repairClassName',
					name:'repairClassName',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true}
			]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//修次IDX[控件] dataIndex:'repairtimeIDX'
				{
					fieldLabel:'修次',
					id:"rt_comb",
					width:TrainEnforcePlanDetail.fieldWidth,
					disabled:false,
					xtype: "Base_combo",
					allowBlank: false,
					fields: ["repairtimeIDX","repairtimeName"],
    				business: 'rcRt',
					hiddenName: "repairtimeIDX",
					displayField: "repairtimeName",
					valueField: "repairtimeIDX",
					pageSize: 0,
					minListWidth: 200,
					emptyText:"- 请选择 -",
					returnField: [{widgetId:"repairtimeName",propertyName:"repairtimeName"}],
					listeners : {
						"beforequery" : function(){
							//选择修次前先选修程
		            		var rcIdx =  Ext.getCmp("rc_comb").getValue();
							if(rcIdx == "" || rcIdx == null){
								MyExt.Msg.alert("请先选择修程！");
								return false;
							}
		            	}
					}
				},
				//修次名称[隐藏框] dataIndex:'repairtimeName'
				{
					fieldLabel:'修次名称',
					id:'repairtimeName',
					name:'repairtimeName',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				}
				]
			}]
		},
		{
			/**  计划修车日期	计划交车日期
			 *  承修部门		工作号		*/
			xtype: 'panel',	border:false,	layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
				labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
				items:[
				//计划修车日期[日历控件] dataIndex:'planStartDate'
				{
					fieldLabel:'计划入段日期',
					id: 'planStartDate_detailId',
					name:'planStartDate',
					width:TrainEnforcePlanDetail.fieldWidth,
					disabled:false,
					xtype:'my97date',
					format: 'Y-m-d',
//					allowBlank: false,
				 	initNow:false,
				 	listeners : {
				 		"blur":function(v){
				 			TrainEnforcePlanDetail.checkDate('planStartDate_detailId','planEndDate_detailId',false);
				 	 	}
				 	}
				}
				
			]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				
				//计划交车日期[日历控件] dataIndex:'planEndDate'
				{
					fieldLabel:'计划离段日期',
					id: 'planEndDate_detailId',
					name:'planEndDate',
					width:TrainEnforcePlanDetail.fieldWidth,
					disabled:false,
					xtype:'my97date', 
					format: 'Y-m-d',
					initNow:false,
					listeners : {
						"blur":function(v){
				 			TrainEnforcePlanDetail.checkDate('planStartDate_detailId','planEndDate_detailId',true);
				 	 	}
					}
				}
			]
		}]
	},{
		/** 不良状态预报 */
		xtype: 'panel',	border:false,	layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textarea',  baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:1.025,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//备注[文本域] dataIndex:'remarks'
				{
					fieldLabel:'不良状态预报',
					id:'remarks_id',
					name: 'remarks',
					maxLength:1000,
					width:TrainEnforcePlanDetail.fieldWidth,
					disabled:false
				}
			]
		}]
	}]
});


//覆盖创建的窗口方法
TrainEnforcePlanDetail.grid.createSaveWin = function(){
    if(TrainEnforcePlanDetail.grid.saveForm == null) TrainEnforcePlanDetail.grid.createSaveForm();
	TrainEnforcePlanDetail.grid.saveWin = new Ext.Window({
		title: "编辑", layout: "fit", height:'335',width:'550', plain:true,
		closeAction: "hide", modal: true, buttonAlign: "center",
		items : [ TrainEnforcePlanDetail.baseForm],
		buttons: [{
            text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
            	/* 
            	 * 当选择了计划修车日期之后, 该控件失去焦点时将会Ajax异步调用后台逻辑计算出计划交车日期,这里有一定的时间开销,
            	 * 为防止用户选择了计划修车日期后立刻点击保存,此时计划交车日期尚未计算完成并填充数据至表单文本框中,此处加0.5秒的延迟提交
            	 */
            	setTimeout("TrainEnforcePlanDetail.grid.saveFn()",500);
            }
        }, {
            text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ TrainEnforcePlanDetail.grid.saveWin.hide(); }
        }]
	});
}

});