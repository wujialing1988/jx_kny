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
	Ext.getCmp("comboTree_bid").setDisabled(arg);
	Ext.getCmp("comboTree_did").setDisabled(arg);
	Ext.getCmp("planStartDate_detailId").setDisabled(arg);
	Ext.getCmp("planEndDate_detailId").setDisabled(arg);
	Ext.getCmp("workNumber").setDisabled(arg);
	Ext.getCmp("remarks_id").setDisabled(arg);
	Ext.getCmp("comboTree_usedDId").setDisabled(arg);
}

TrainEnforcePlanDetail.uploadWin = new Ext.Window({            //定义上传窗口
	 title:"上传", width:400, height:120, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:false, modal: true,
		items: uploadFrom = new Ext.form.FormPanel({
			layout:"form", border:false, style:"padding:10px" , fileUpload:true,
			align:'center',baseCls: "x-plain", defaultType:'textfield',defaults:{anchor:"95%"},
			items:[{
				xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
				items:[{
					align:'center',	defaultType:'textfield', border:false, layout:'form',
					labelWidth: 60,	columnWidth:1,	defaults:{anchor:"100%"}, baseCls: "x-plain",
					buttonAlign:'center',
					items:[{
						fileUpload:true,
						fieldLabel:'选择',
						id: 'image',
						name:'image',
						labelWidth:60,
						xtype : "fileuploadfield",
						allowBlank:false,
						buttonText: '浏览文件...'/*,
			            buttonCfg: {
			                iconCls: 'upload-icon'
			            }*/
					}],
					buttons:[{
						text: "导入", iconCls: "saveIcon", scope: this, handler: function(){
							var form = uploadFrom.getForm();
							var imagePath = Ext.getCmp("image").getValue();
							if(imagePath==null||imagePath==""){
								alertFail("尚未选择excel文件！");
								return;
							}
							if(imagePath.indexOf(".xls")==-1||imagePath.indexOf(".xlsx")!=-1){
								alertFail("请导入excel2003格式文件！");
								return;
							}
							form.submit({  
	                        	url: ctx+'/trainEnforcePlanDetailUpload.action',  
	                       	 	waitMsg: '正在上传Excel文件请稍候...', 
	                       	 	method: 'POST',
	                       	 	params:{
	                       	 		  tpdIDX: TrainEnforcePlan.idx
	                       	 	        },
	                       	 	enctype: 'multipart/form-data',
	                        	success: function(response, options) { 
	                            	if(TrainEnforcePlanDetail.grid.loadMask)   TrainEnforcePlanDetail.grid.loadMask.hide();
					                var result = Ext.util.JSON.decode(options.response.responseText);
					                if(result.success==true){
					                	form.getEl().dom.reset();
					                	TrainEnforcePlanDetail.uploadWin.hide();
					                	if(result.resultMsg.errMsg == null){ //数据导入成功
					                		TrainEnforcePlanDetail.grid.afterSaveSuccessFn(result, response, options);
					                	} else {
					                		TrainEnforcePlanDetail.grid.afterSaveFailFn(result.resultMsg, response, options);
					                	}
					                } else {
					                	Ext.Msg.alert('上传失败!',"没有选择文件或文件体积过大！");
					                }
	                        	}
	                    	}); 
						}
					}]
				}]
			}]
		})
  });

//机车施修计划明细列表
TrainEnforcePlanDetail.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetail!findPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlanDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlanDetail!logicDelete.action',            //删除数据的请求URL
    viewConfig: null,
    storeAutoLoad:false,	//设置grid的store为手动加载(不设置false会引起参数排序失效)
    searchFormColNum: 2,
    saveFormColNum: 2, 
    //tbar: ['search','-','add','-','delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	{ header:'配属局名称', dataIndex:'bShortName', hidden:true, editor:{maxLength:50,xtype:'hidden'}},
	{ header:'配属段名称', dataIndex:'dShortName', hidden:true, editor:{ maxLength:50,xtype:'hidden'}},
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
	{ header:'工作号', dataIndex:'workNumber',width:65, hidden : true, editor:{
		maxLength:50,allowBlank: false},
		searcher:{disabled: true}},	
	{
		header:'车型', dataIndex:'trainTypeShortName', width:65, editor:{
		xtype: 'hidden' },
		searcher:{xtype: 'textfield', anchor:'98%'}
	},{
		header:'车号', dataIndex:'trainNo', width:65,
		searcher:{xtype: 'textfield', anchor:'98%'}
	},{
		header:'配属局ID', dataIndex:'bid', hidden: true
		},{
		header:'配属段ID', dataIndex:'did', hidden: true 
	},{
		header:'配属局', dataIndex:'bName', width:100,
		searcher:{ xtype: "textfield"}
	},{
		header:'配属段', dataIndex:'dNAME', width:100,
		searcher:{ xtype: "textfield" }
	}/*,{
		header:'支配单位', dataIndex:'usedDName',width:100, 
		searcher:{ xtype: "textfield" }
	}*/,{
		header:'BPS流程定义', dataIndex:'bpsProcessDefName', hidden:true, editor:{ xtype:"hidden" }, searcher: {disabled: true}
	},{
		header:'检修流程名称', dataIndex:'processName', hidden: true, editor:{ xtype:'hidden' }
	},{
		header:'BPS流程名称', dataIndex:'bpsProcessChName', hidden: true, editor:{  xtype:'hidden' }
	},{
		header:'bpsTemplateIdx', dataIndex:'bpsTemplateIdx', hidden: true, editor:{  xtype:'hidden' }
	},{
		header:'工艺流程主键', dataIndex:'processIDX', hidden: true, editor:{ xtype:'hidden' }
	},{
		header:'修程', dataIndex:'repairClassIDX', hidden: true
	},{
		header:'修程', dataIndex:'repairClassName', width:65,
		searcher:{anchor:'98%'}
	},{
		header:'修次主键', dataIndex:'repairtimeIDX', hidden:true
	},{
		header:'修次', dataIndex:'repairtimeName', width:65
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
		header:'走行公里',dataIndex:'runningKM',maxLength:8
	},{
		header:'计划入段日期', dataIndex:'planStartDate', width:90, xtype:'datecolumn', 
		searcher:{id:'planStartDate_searchId',disabled: true}
	},{
		header:'计划离段日期', dataIndex:'planEndDate', width:90,xtype:'datecolumn',
		searcher:{disabled: true}
	},{
		header:'委修单位ID', dataIndex:'usedDId', hidden: true
	},{
		header:'委修单位', dataIndex:'usedDName'
	},{
		header:'不良状态预报', dataIndex:'remarks', 
		searcher:{disabled: true}
	}],
	searchOrder:[
		'trainTypeShortName','trainNo','bShortName','dShortName','usedDShortName','repairClassName'
	],
	tbar:['search','add',{
		text: "根据走行生成",
		iconCls: "checkIcon",
		handler: function(){
			RKM.showWin(function(selection, win, grid){
				this.disable();
				var me = this;
				var types = [];
				var nos = [];
				var type = {};
				var trains = {};
				var trainInfo = [];
				for(var i = 0; i < selection.length; i++){
					var train = selection[i].get("trainTypeIdx"); 
					if(type[train] == undefined){
						types.push(train);
						type[train] = true;
						trains[train] = {};
					}
					var trainNo = selection[i].get("trainNo")
					trains[train][trainNo] = selection[i].data;
					nos.push(trainNo);
				}
				
				Ext.Ajax.request({
					url: ctx + "/jczlTrain!findTrainByTrain.action",
					params: {types: types.join("]|["), nos: nos.join("]|[")},
					success: function(r){
						var list = Ext.util.JSON.decode(r.responseText).list;
						//aaabbb
						var data = [];
						var tmp;
						for(var i in trains){
							for(var j in trains[i]){
								trains[i][j].entity = {};
								Ext.apply(trains[i][j].entity, {
									'planStatus': status_detail_formation  ,
									'trainEnforcePlanIDX': TrainEnforcePlan.idx,
									'trainTypeIDX': i,
									'trainTypeShortName': trains[i][j].trainType,
									'trainNo': j,
									'runningKM': trains[i][j].recentlyRunningKm,
									"repairClassIDX": trains[i][j].repairClass,
									'repairClassName': trains[i][j].repairClassName,
									"repairtimeIDX": trains[i][j].repairOrder,
									'repairtimeName': trains[i][j].repairOrderName,
									'planStartDate': TrainEnforcePlan.planStartDate,
									'planEndDate': TrainEnforcePlan.planEndDate
								});								
							}
						}
						
						for(var i = 0; i < list.length; i++){
							tmp = list[i];
							var _data = trains[tmp.trainTypeIDX][tmp.trainNo];
							if(_data == undefined) continue;
							Ext.apply(_data.entity, {
								"bid": tmp.bId,
								'bName': tmp.bName,
								'bShortName': tmp.bShortName,
								"trainNo": tmp.trainNo, 
								"did": tmp.dId,
								'dNAME': tmp.dNAME,
								'dShortName': tmp.dShortName,
								"usedDId": tmp.dId,
								'usedDName': tmp.dNAME,
								'usedDShortName': tmp.dShortName
							});
						}
						
						for(var i in trains){
							for(var j in trains[i]){
								data.push(trains[i][j].entity);
							}
						}
						
						if(data.length > 0){
							Ext.Ajax.request({
								url: ctx + "/trainEnforcePlanDetail!batchSave.action",
								jsonData: data,
								success: function(r){
									var rlt = Ext.util.JSON.decode(r.responseText);
									if(rlt.success){
										TrainEnforcePlanDetail.grid.store.reload();
										alertSuccess();
										grid.store.reload();
									}
									else{
										alertFail(rlt.errMsg);
									}
									me.enable();
								},
								failure: function(){
									alertFail("连接超时！");
									me.enable();
								}
							});
						}else{
							MyExt.Msg.alert("未能查询到机车相关信息");
							me.enable();
						}
					},
					failure: function(){
						me.enable();
						alertFail("操作失败！");
					}
				});
			});
		}
	},'delete',	
		    { text:"下载模板", iconCls:"application-vnd-ms-excel", 
	            handler:function(){
            	   window.location.href = ctx + '/trainEnforcePlanDetail!download.action';
	            }
	        },{ text:"导入", iconCls:"page_excelIcon", 
              handler:function(){
              TrainEnforcePlanDetail.uploadWin.show();
                 }
	        },{
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
		Ext.getCmp("comboTree_usedDId").clearValue();
		Ext.getCmp("comboTree_bid").clearValue();
		Ext.getCmp("comboTree_did").clearValue();
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
		Ext.getCmp("trainNo_comb").queryParams = {"trainTypeIDX":record.get("trainTypeIDX")};
	    Ext.getCmp("trainNo_comb").cascadeStore();
	    Ext.getCmp("trainNo_comb").setDisplayValue(record.get("trainNo"),record.get("trainNo"));
		Ext.getCmp("rc_comb").setDisplayValue(record.get("repairClassIDX"),record.get("repairClassName"));
//		//配属局
		Ext.getCmp("comboTree_bid").setDisplayValue(record.get("bid"),record.get("bName"));
//		//配属段
		Ext.getCmp("comboTree_did").setDisplayValue(record.get("did"),record.get("dNAME"));
		Ext.getCmp("comboTree_did").orgid = record.get("bid"); //设置配属段的查询条件
		Ext.getCmp("comboTree_did").orgname = record.get("bShortName"); //设置配属段的查询条件
//		//支配单位
		Ext.getCmp("comboTree_usedDId").setDisplayValue(record.get("usedDId"),record.get("usedDName"));
		//过滤修程
		var rc_comb = Ext.getCmp("rc_comb");
        rc_comb.queryParams = {"TrainTypeIdx":record.get("trainTypeIDX")};
        rc_comb.cascadeStore();
		//根据默认的车型,设置该默认车型的默认修程
		Ext.getCmp("rt_comb").reset();
		Ext.getCmp("rt_comb").clearValue();
		Ext.getCmp("rt_comb").setDisplayValue(record.get("repairtimeIDX"),record.get("repairtimeName"));
		if(!Ext.isEmpty(record.get("repairClassIDX"))){								
            Ext.getCmp("rt_comb").queryParams = {"rcIDX":record.get("repairClassIDX")};
            Ext.getCmp("rt_comb").cascadeStore(); 
		}
		if(record.get("planStatus")=='10' && (TrainEnforcePlan.planStatus == '10' || TrainEnforcePlan.planStatus == '60')){
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
		/**车型		车号
		 * 配属局		配属段  */
		xtype: 'panel',border:false,layout:'column',align:'center',baseCls: "x-plain",
		items:[{
			align:'center',layout:'form',defaultType:'textfield',baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//车型IDX[控件]  dataIndex:'trainTypeIDX',
				{	fieldLabel:'车型',
					id:"trainType_comb",
					width:TrainEnforcePlanDetail.fieldWidth,
					xtype:'Base_combo',
					business: 'trainType',													
					fields:['typeID','shortName','repairType'],
					queryParams: {'isCx':'yes'},
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					hiddenName:'trainTypeIDX',
					returnField:[{widgetId:"trainTypeShortName",propertyName:"shortName"}],
					displayField: "shortName", 
					valueField: "typeID",
					pageSize: 20, 
					minListWidth: 200,
					disabled:false,
					editable:true,
					allowBlank: false,
					listeners : {
						"select" : function() { 
							//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
			                trainNo_comb.cascadeStore();
			                //重新加载修程下拉数据
			                var rc_comb = Ext.getCmp("rc_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                var filterParams = {"TrainTypeIdx":this.getValue()};
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
				},
				//配属局IDX[控件]  dataIndex:'bid'
				{
					fieldLabel:'配属局',
					id: "comboTree_bid",
					xtype: "BureauSelect_comboTree",
					hiddenName: "bid",
					width:TrainEnforcePlanDetail.fieldWidth,
					disabled:false,
					returnField: [{widgetId: "bName", propertyName: "text"}, //名称
			  			  		  {widgetId: "bShortName", propertyName: "orgname"}], //简称
		    		selectNodeModel: "leaf",
		    		allowBlank: true,
		    		listeners : {
			  			"select" : function() {
			  				Ext.getCmp("comboTree_did").reset();
	             		    Ext.getCmp("comboTree_did").clearValue();
			  				Ext.getCmp("comboTree_did").orgid = this.getValue();
			  				Ext.getCmp("comboTree_did").orgname = this.lastSelectionText;
			  			}
			  		}
				},
				//配属局全称[隐藏框] dataIndex:'bName'
				{
					fieldLabel:'配属局全称',
					id:"bName",
					name:'bName',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				},
				//配属局简称[隐藏框] dataIndex:'bShortName'
				{
					fieldLabel:'配属局简称',
					id:'bShortName',
					name:'bShortName',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				}
			]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//车号名称[控件] dataIndex:'trainNo'
				{
					fieldLabel:'车号',
					id:"trainNo_comb",
					name:'trainNo',
					width:TrainEnforcePlanDetail.fieldWidth,
					xtype: "Base_combo",
					disabled:false,
					hiddenName: "trainNo", 
					displayField: "trainNo", 
					valueField: "trainNo",
					pageSize: 20, 
					minListWidth: 200,
					minChars : 1,
					minLength : 4, 
					maxLength : 5,
					business: 'jczlTrain',										
					entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',			
					fields:["trainNo","holdOrgId","holdOrgName","holdOrgSeq",
					"makeFactoryIDX","makeFactoryName",{name:"leaveDate", type:"date", dateFormat: 'time'},	
					"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName","bId","dId","bName","dName","bShortName","dShortName"],
					queryParams:{'isCx':'yes','isIn':'false','isRemoveRun':'true'},
    				isAll: 'yes',
					//forceSelection:false,    //车号必须在Store中
					returnField: [
			              {widgetId:"comboTree_bid",propertyName:"bId"},//配属局ID
			              {widgetId:"bName",propertyName:"bName"},//配属局名称 -
			              {widgetId:"bShortName",propertyName:"bShortName"},//配属局简称--
			              {widgetId:"comboTree_did",propertyName:"dId"},//配属段ID
			              {widgetId:"dNAME",propertyName:"dName"},//配属段名称 -
			              {widgetId:"dShortName",propertyName:"dShortName"}],//配属段简称--
			        editable:true,
					allowBlank: false,
					listeners : {
						"beforequery" : function(){
					//选择车号前先选车型
					var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
					if(trainTypeIdx == "" || trainTypeIdx == null){
						MyExt.Msg.alert("请先选择车型！");
						return false;
					}
				},
				"blur": function(){
					/** 
					 *  当车号控件失去焦点时,触发获取当前用户所选或者填写的内容
					 *  如trainNo_comb的value与hiddenName的value相同,则可得知车号是由用户通过下拉进行选择的
					 *  如果两者不相同,或者hiddenName的value为空,则可认为车号是用户通过键盘录入的
					 */
					
					if(Ext.getCmp("trainNo_comb").getRawValue()!=null&&Ext.getCmp("trainNo_comb").getRawValue()!=""){
						if(Ext.getCmp("trainNo_comb").getRawValue()!=Ext.getCmp("trainNo_comb").getValue()){
							Ext.getCmp("trainNo_comb").setDisplayValue(Ext.getCmp("trainNo_comb").getRawValue(),Ext.getCmp("trainNo_comb").getRawValue());
						}
						//如果
						else {
							/**
							 * 当选择车号后,自动带出配属局和配属段的内容
							 */
							var record = Ext.getCmp("trainNo_comb").findRecord(Ext.getCmp("trainNo_comb").valueField, Ext.getCmp("trainNo_comb").value);
							if(typeof record != "undefined"){
								//1. 带出配属局的Id及简称回显
								var b_id = record.get("bId");//Ext.get("bid").dom.value;
								var b_shortname = record.get("bName");//values.bShortName;
								//2. 带出配属段的Id及简称回显
								var d_id = record.get("dId");//Ext.get("did").dom.value;
								var d_shortname = record.get("dName");//values.dShortName;
								Ext.getCmp("comboTree_bid").clearValue(); //清空配属局控件
								Ext.getCmp("comboTree_did").clearValue(); //清空配属段控件
								Ext.getCmp("comboTree_bid").setDisplayValue(b_id,b_shortname);
								Ext.getCmp("comboTree_did").setDisplayValue(d_id,d_shortname);
								Ext.getCmp("comboTree_usedDId").setDisplayValue(d_id,d_shortname);//给委修单位设值
								Ext.getCmp("usedDName").setValue(record.get("dName"));//Ext.get("dNAME").dom.value); //给委修单位全称设值
								Ext.getCmp("usedDShortName").setValue(record.get("dShortName"));//Ext.get("dShortName").dom.value);//给委修单位简称设置
								Ext.getCmp("comboTree_did").orgid = b_id; //设置配属段的查询条件
								Ext.getCmp("comboTree_did").orgname = b_shortname; //设置配属段的查询条件
								TrainEnforcePlanDetail.checkDate('planStartDate_detailId','planEndDate_detailId',false);
							}
						}
					}
				},
				"select" : function(){
					/**
					 * 当选择车号后,自动带出配属局和配属段的内容
					 */
					var record = Ext.getCmp("trainNo_comb").findRecord(Ext.getCmp("trainNo_comb").valueField, Ext.getCmp("trainNo_comb").value);
					if(typeof record != "undefined"){
					//1. 带出配属局的Id及简称回显
					var b_id = record.get("bId");//Ext.get("bid").dom.value;
					var b_shortname = record.get("bName");//values.bShortName;
					//2. 带出配属段的Id及简称回显
					var d_id = record.get("dId");//Ext.get("did").dom.value;
					var d_shortname = record.get("dName");//values.dShortName;
					Ext.getCmp("comboTree_bid").clearValue(); //清空配属局控件
					Ext.getCmp("comboTree_did").clearValue(); //清空配属段控件
					Ext.getCmp("comboTree_bid").setDisplayValue(b_id,b_shortname);
					Ext.getCmp("comboTree_did").setDisplayValue(d_id,d_shortname);
					Ext.getCmp("comboTree_usedDId").setDisplayValue(d_id,d_shortname);//给委修单位设值
					Ext.getCmp("usedDName").setValue(record.get("dName"));
					Ext.getCmp("bName").setValue(record.get("bName"));
					Ext.getCmp("dNAME").setValue(record.get("dName"));//Ext.get("dNAME").dom.value); //给委修单位全称设值
					Ext.getCmp("comboTree_did").orgid = b_id; //设置配属段的查询条件
					Ext.getCmp("comboTree_did").orgname = b_shortname; //设置配属段的查询条件
					}
				}
					}
				},
				//配属段IDX[控件]  dataIndex:'did'
				{
					fieldLabel:'配属段',
					id: "comboTree_did",
					width:TrainEnforcePlanDetail.fieldWidth,
					xtype: "DeportSelect_comboTree",
					hiddenName: "did",
					disabled:false,
					returnField: [{widgetId: "dNAME", propertyName: "text"}, //名称
			  			  {widgetId: "dShortName", propertyName: "orgname"},
			  			  {widgetId: "usedDName",propertyName:"text"}], //简称
		    		selectNodeModel: "leaf" ,
		    		allowBlank: true,
		    		listeners : {
		    			"beforequery" : function(){
							//选择段前先选局
							var comboTree_bid =  Ext.getCmp("comboTree_bid").getValue();
							if(comboTree_bid == "" || comboTree_bid == null){
								MyExt.Msg.alert("请先选择配属局！");
								return false;
							}
						},
						"select" : function(){
							//当用户选择配属段时,自动默认委修单位为当前配属段
							Ext.getCmp("comboTree_usedDId").setDisplayValue(this.getValue(),this.lastSelectionText); //ID
							//Ext.getCmp("usedDName").setValue(Ext.get("dNAME").dom.value); //全称
							Ext.getCmp("usedDShortName").setValue(this.lastSelectionText);//简称
						}
					}
				},
				//配属段全称[隐藏框] dataIndex:'dNAME'
				{
					fieldLabel:'配属段全称',
					id:"dNAME",
					name:'dNAME',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				},
				//配属段简称[隐藏框] dataIndex:'dShortName'
				{ 
					fieldLabel:'配属段简称',
					id:'dShortName',
					name:'dShortName',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				}
			]
		}]
	},{
		/** 支配单位 */
		xtype: 'panel',	border:false,	layout:'column',	align:'center',	baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield',	baseCls: "x-plain",
			labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
			items:[
				//支配单位IDX[控件] dataIndex:'usedDId'
				{
					fieldLabel:'委修单位',
					id: "comboTree_usedDId",
					width:TrainEnforcePlanDetail.fieldWidth,
					xtype: "DominateSection_comboTree",
					hiddenName: "usedDId",
                    readOnly : true,
					orgid: "0",
					orgname: orgRootName, 
					selectNodeModel: "leaf",
					disabled:false,
					returnField: [{widgetId: "usedDName", propertyName: "text"},
			  			  {widgetId: "usedDShortName", propertyName: "orgname"}]
				},
				//支配单位全称[隐藏框] dataIndex:'usedDName'
				{
					fieldLabel:'委修单位全称',
					id:"usedDName",
					name:'usedDName',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				},
				//支配单位简称[隐藏框] dataIndex:'usedDShortName'
				{
					fieldLabel:'委修单位',
					id:"usedDShortName",
					name:'usedDShortName',
					width:TrainEnforcePlanDetail.fieldWidth,
					hidden:true
				}
			]
		},{
		   align:'center',	layout:'form', defaultType:'textfield',	baseCls: "x-plain",
		   labelWidth: TrainEnforcePlanDetail.labelWidth,	columnWidth:0.5,	defaults:{anchor:TrainEnforcePlanDetail.anchor},
		   items:[{
		         fieldLabel:'走行公里',
		         name:'runningKM',
		         id:'runningKM',
		         maxLength:8,
		         vtype:'nonNegativeNumber',
		         width:TrainEnforcePlanDetail.fieldWidth
		   }]
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
				},
				//工作号[文本框]  dataIndex:'workNumber'
				{
					fieldLabel:'工作号',
					id:'workNumber',
					width:TrainEnforcePlanDetail.fieldWidth,
					maxLength:50,
                    xtype : 'hidden'
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
            text: "保存", iconCls: "saveIcon",id:'trainEnforcePlanDetailw_saveBtn', scope: this, handler: function(){
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