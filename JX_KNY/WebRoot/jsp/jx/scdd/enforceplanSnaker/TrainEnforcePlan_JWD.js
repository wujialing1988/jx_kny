/**
 * 机车施修计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainEnforcePlan');                       //定义命名空间
TrainEnforcePlan.idx = '';   //主键全局变量
TrainEnforcePlan.planStartDate = '';
TrainEnforcePlan.planEndDate = '';

TrainEnforcePlan.planStatus = '' ;
TrainEnforcePlan.searchParams = {} //查询全局参数集
TrainEnforcePlan.serachParams


//年store data，从2008年到当前年
var yearData = [];
var now = new Date();
var currentYear = now.getFullYear();         //当前年
for (var i = 2008; i <= currentYear + 1; i++) {
    var year = [ i ];
    yearData.push(year);
}

//获取当前年度的开始和结束日期
TrainEnforcePlan.getCurrentYear = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var start = new Date(currentYear,0,1).format('Y-m-d');
	if(arg == 'begin'){
		return start;
	}
	else if (arg == 'end'){
		return new Date(currentYear,11,31).format('Y-m-d');
	}
}

//获取目标年度的开始和结束日期
TrainEnforcePlan.getTargetYear = function(arg,year){
	var start = new Date(year,0,1).format('Y-m-d');
	if(arg == 'begin'){
		return start;
	}
	else if (arg == 'end'){
		return new Date(year,11,31).format('Y-m-d');
	}
}

//验证计划开始结束日期
TrainEnforcePlan.checkStartDate = function(arg){
	var date1 = Ext.getCmp("planStartDate_Id").getValue() ;
	var date2 = Ext.getCmp("planEndDate_Id").getValue() ;

	var flag = true; 
	if(date1 != "" && date2 != ""){
		if(date1.format('Y-m-d') > date2.format('Y-m-d')){
			flag = false ;
			return "计划开始日期不能晚于计划结束日期！";
		}
	}	
	Ext.getCmp("planStartDate_Id").clearInvalid();
	Ext.getCmp("planEndDate_Id").clearInvalid();
	return flag;
}
//验证计划开始结束日期
TrainEnforcePlan.checkEndDate = function(arg){
	var date1 = Ext.getCmp("planStartDate_Id").getValue() ;
	var date2 = Ext.getCmp("planEndDate_Id").getValue() ;

	var flag = true; 
	if(date1 != "" && date2 != ""){
		if(date1.format('Y-m-d') > date2.format('Y-m-d')){
			flag = false ;
			return "计划结束日期不能早于计划开始日期！";
		}
	}	
	Ext.getCmp("planStartDate_Id").clearInvalid();
	Ext.getCmp("planEndDate_Id").clearInvalid();
	return flag;
}

TrainEnforcePlan.getCurrentMonth = function(arg){
	
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	//如果当前月度是12月, 则使用下一年度, 否则仍然是本年度
	var useYear = currentMonth == '12'?current+1:currentYear;
	var useMonth = currentMonth+1;
	var MonthFirstDay=new Date(useYear,useMonth,1);
	var MonthNextFirstDay=new Date(useYear,useMonth+1,1);
 	var MonthLastDay=new Date(MonthNextFirstDay-86400000);

	if(arg == 'begin')
		return MonthFirstDay;
	else if (arg == 'end'){
		return MonthLastDay;
	}
}

TrainEnforcePlan.checkQueryFn = function(){
	TrainEnforcePlan.status = [];
	if(Ext.getCmp("status_wtj").checked){
		TrainEnforcePlan.status.push(10) ;
		TrainEnforcePlan.status.push(60) ;
	} 
	if(Ext.getCmp("status_ytj").checked){
		TrainEnforcePlan.status.push(20) ;
		TrainEnforcePlan.status.push(30) ;
	} 
	TrainEnforcePlan.grid.store.load();
}


//机车施修计划列表
TrainEnforcePlan.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlan!findPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlan!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlan!logicDelete.action',            //删除数据的请求URL
    searchFormColNum: 2,
    saveFormColNum: 2,
    tbar: ['search','add','delete',
    {
    	text:"打印", iconCls:"printerIcon", handler: function(){
    		var sm = TrainEnforcePlan.grid.getSelectionModel();
	        if (sm.getCount() != 1) {
	            MyExt.Msg.alert("请选择一条记录！");
	            return;
	        }
	        var data = sm.getSelections();
    		var idx = data[0].get("idx") ;  //施修计划主键（生产计划）
    		var startDate = data[0].get("planStartDate") ;  //开始时间
    		var endDate = data[0].get("planEndDate") ;  //结束时间
    		var url = ctx + "/jsp/jx/scdd/enforceplan/ProducePlan_JWD.jsp?idx="+idx;
							window.open(encodeURI(url));
        } 
    },{
    	text:"提交", iconCls:"addIcon", handler: function(){
			var sm = TrainEnforcePlan.grid.getSelectionModel();
	        if (sm.getCount() != 1) {
	            MyExt.Msg.alert("请选择一条记录！");
	            return;
	        }
    		Ext.Msg.confirm("提示", "确认提交流程", function(btn){
				if(btn != 'yes'){
					return;
				}    
			        var data = sm.getSelections();
		    		var idx = data[0].get("idx") ;  //施修计划主键（生产计划）
		    				// Ajax请求
					Ext.Ajax.request({
						url:ctx + '/trainEnforcePlan!sendProcess.action',
						params: {
							idx: idx
						},
					    //请求成功后的回调函数
						success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
					        var result = Ext.util.JSON.decode(response.responseText);
					        if (result.errMsg == null) {       //操作成功     
					            alertSuccess();
					            TrainEnforcePlan.grid.store.load();
					        } else {                           //操作失败
					            alertFail(result.errMsg);
					        }
					    },
					    //请求失败后的回调函数
					    failure: function(response, options){
					        if(self.loadMask)    self.loadMask.hide();
					        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					    }
					});
				});
    		
    		
        } 
    },'refresh', '-' ,'状态：', {
	    	xtype:"checkbox", id:"status_wtj", boxLabel:"未提交&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: TrainEnforcePlan.checkQueryFn
	    }, {
	    	xtype:"checkbox", id:"status_ytj", boxLabel:"已提交&nbsp;&nbsp;&nbsp;&nbsp;", checked:false , handler: TrainEnforcePlan.checkQueryFn
	    }
    ],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { id:'idx', xtype:'hidden' }
	},{
		header:'流程模板ID', dataIndex:'processId', hidden:true, editor: { id:'processId', xtype:'hidden' }
	},{
		header:'流程实例ID', dataIndex:'processInstId', hidden:true, editor: { id:'processInstId', xtype:'hidden' }
	},{
		header:'计划名称', dataIndex:'planName',id:'planName', editor:{  maxLength:100, allowBlank: false }, searcher: {anchor:'98%'}
	},{
		header:'编制日期', dataIndex:'planTime', xtype:'datecolumn',format: "Y-m-d", editor:{ xtype:'my97date',initNow:true, allowBlank: false },
		searcher:{disabled: true }
	},{
		header:'计划开始日期', dataIndex:'planStartDate', xtype:'datecolumn', 
		editor:{ id: 'planStartDate_Id', xtype:'my97date',my97cfg: {dateFmt:'yyyy-MM-dd'}, 
				validator: TrainEnforcePlan.checkStartDate,
				allowBlank: false  },
		searcher:{id: 'planStartDate_SearchId', xtype:'my97date', anchor:'98%',
			listeners : {
				//渲染查询面板时,动态绑定方法
				"render":function(){
					//给查询面板绑定beforeshow方法,在面板显示前,设置各个常用查询条件,减少录入步骤
					TrainEnforcePlan.grid.searchWin.on('beforeshow',function(){
						Ext.getCmp("planStartDate_SearchId").setValue(TrainEnforcePlan.getCurrentYear("begin")); //本年1月1日
						Ext.getCmp("toPlanStartDate").setValue(TrainEnforcePlan.getCurrentYear("end"));	//本年12月31日
					});
				}
			}	
		}
	},{
		header:'计划结束日期', dataIndex:'planEndDate', xtype:'datecolumn', 
		editor:{ id: 'planEndDate_Id', xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd'}, 
				validator: TrainEnforcePlan.checkEndDate,
				 allowBlank: false },
		searcher:{id: 'planEndDate_SearchId', xtype:'my97date', anchor:'98%', initNow:false}
	},{
		header:'编制人', dataIndex:'planPerson', hidden: true, 
		editor:{ 
			id: "planPerson_Id",xtype: "OmEmployee_SelectWin", fieldLabel: "编制人",
			hiddenName: "planPerson", displayField:"empname", valueField: "empid",
			returnField :[{widgetId: "planPersonName_Id", propertyName: "empname"}],
			editable: false 
		}
	},{
		header:'编制人', dataIndex:'planPersonName', editor:{ id:"planPersonName_Id", xtype:"hidden", maxLength:25 },
		searcher:{xtype:'textfield', anchor:'98%'}
	},{
		header:'编制单位', dataIndex:'planOrgId', hidden: true,
		editor:{
			id: "planOrgId_Id", xtype: "OmOrganizationCustom_comboTree", 
			hiddenName: "planOrgId", fieldLabel: "编制单位", orgid: orgId, orgname: orgRootName, selectNodeModel: "all",
			returnField: [{widgetId:"planOrgSeq_Id",propertyName:"orgseq"},{widgetId:"planOrgName_Id",propertyName:"orgname"}],
			queryHql:"[degree]oversea"
		}
	},{
		header:'编制单位部门序列', dataIndex:'planOrgSeq', hidden: true, editor:{ id: "planOrgSeq_Id", xtype: "hidden" }
	},{
		header:'编制单位', dataIndex:'planOrgName', editor:{ id: "planOrgName_Id", xtype: "hidden" }
	},{
		header:'计划状态', dataIndex:'planStatus', hidden: false,editor:{ id:'planStatus', xtype:'hidden' },renderer:function(v){
			var vreturn = "";
			if(v == '10'){
				vreturn = "未处理" ;
			}else if(v == '20'){
				vreturn = "审核中" ;
			}else if(v == '30'){
				vreturn = "审核完成" ;
			}else if(v == '40'){
				vreturn = "已经兑现" ;
			}else if(v == '50'){
				vreturn = "计划完成" ;
			}else if(v == '60'){
				vreturn = "拒绝" ;
			}
			return vreturn ;
		}
	}],
	searchOrder: ['planName','planPersonName',
	 //计划开始时间(起)	计划结束日期(止)
	 'planStartDate',{id:'toPlanStartDate',fieldLabel:"到  ",xtype:'my97date',  anchor:'98%', my97cfg: {dateFmt:"yyyy-MM-dd"},initNow: false}
	 //计划结束时间(起)	计划结束时间(止)
	 ,'planEndDate', {id:'toplanEndDate',  fieldLabel:"到  ",xtype:'my97date',  anchor:'98%', my97cfg: {dateFmt:"yyyy-MM-dd"},initNow: false}
	],
	beforeShowSaveWin: function(){
		Ext.getCmp("planEndDate_Id").setValue(TrainEnforcePlan.getCurrentMonth("end"));
		Ext.getCmp("planStartDate_Id").setValue(TrainEnforcePlan.getCurrentMonth("begin"));//planEndDate_Id
	},
	 /**
     * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */
    beforeGetFormData: function(){
    	//打开修改编制人和其所属单位,让表单可以读取数据
		Ext.getCmp("planPerson_Id").setDisabled(false);
		Ext.getCmp("planOrgId_Id").setDisabled(false);
    },
    /**
     * 获取表单数据后触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */    
    afterGetFormData: function(){
    	//禁用修改编制人和其所属单位
		Ext.getCmp("planPerson_Id").setDisabled(true);
		Ext.getCmp("planOrgId_Id").setDisabled(true);
    },  
	/**
     * 显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
    afterShowSaveWin: function(){
    	Ext.getCmp("planEndDate_Id").setValue(TrainEnforcePlan.getCurrentMonth("end"));
    	Ext.getCmp("planStartDate_Id").setValue(TrainEnforcePlan.getCurrentMonth("begin"));
    	//设置【机车施修计划明细】不可用
    	TrainEnforcePlanDetail.grid.store.removeAll();
    	TrainEnforcePlanDetail.grid.disable();
    	//设置编制人默认为当前登录用户
    	Ext.getCmp("planPerson_Id").setDisplayValue(empId,empname);
    	Ext.getCmp("planPersonName_Id").setValue(empname);
    	//设置编制单位默认为当前用户所在的段级单位
    	Ext.getCmp("planOrgId_Id").clearValue();
    	Ext.getCmp("planOrgName_Id").setValue(orgName);
    	Ext.getCmp("planOrgId_Id").setDisplayValue(orgId,orgName);
		Ext.getCmp("planOrgSeq_Id").setValue(orgseq);
		//禁止修改编制人和其所属单位
		Ext.getCmp("planPerson_Id").setDisabled(true);
		Ext.getCmp("planOrgId_Id").setDisabled(true);
		// 新增可操作
		Ext.getCmp("saveParts").setVisible(true);
		TrainEnforcePlanDetail.grid.toolbars[0].setVisible(true) ;
		
		TrainEnforcePlan.tab.hideTabStripItem(1);
		TrainEnforcePlan.tab.hideTabStripItem(2);
		
		TrainEnforcePlan.tab.setActiveTab(0);
		
    },
	/**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * 设置“计划状态”为“编制完成”
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
	beforeSaveFn: function(data){
		// 如果状态没有改变，则不进行默认状态赋值
		if(Ext.isEmpty(data.planStatus)){
			data.planStatus = status_completePlan;
			TrainEnforcePlan.planStatus = status_completePlan ;
		}
		return true;
	},
	/**
     * 保存成功之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * 
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
	afterSaveSuccessFn: function(result, response, options){
        //TrainEnforcePlan.clearSearchParams(this);
        alertSuccess();
        var result = Ext.util.JSON.decode(response.responseText);  
        //获取保存成功后的“机车施修计划”主键并保存到全局变量
	    TrainEnforcePlan.idx = result.entity.idx;
	    TrainEnforcePlan.grid.saveForm.getForm().getValues().idx = result.entity.idx;
	    Ext.getCmp("idx").setValue(result.entity.idx);
	    //设置“计划开始日期”和“计划结束日期”
		TrainEnforcePlan.planStartDate = new Date(result.entity.planStartDate).format('Y-m-d');
		TrainEnforcePlan.planEndDate = new Date(result.entity.planEndDate).format('Y-m-d');
	    //设置【机车施修计划明细】可用
		TrainEnforcePlanDetail.grid.enable();
	    this.saveWin.setTitle('编辑');	    
	    TrainEnforcePlanDetail.grid.store.load();
	    TrainEnforcePlan.grid.store.load();
    },
     /**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
	afterShowEditWin:function(record, rowIndex){
		var data = TrainEnforcePlan.grid.selModel.getSelections();//获取当前用户选择的行
		TrainEnforcePlan.grid.saveWin.setTitle("编辑 - "+data[0].get("planName"));
		//设置"编制人""编制单位"控件回显值
		Ext.getCmp("planOrgName_Id").setValue(record.get("planOrgName"));
		Ext.getCmp("planOrgId_Id").setDisplayValue(record.get("planOrgId"),record.get("planOrgName"));
		Ext.getCmp("planOrgSeq_Id").setValue(record.get("planOrgSeq"));
		
		Ext.getCmp("planPerson_Id").setDisplayValue(record.get("planPerson"),record.get("planPersonName"));
		//设置“机车施修计划主键”全局变量
		TrainEnforcePlan.idx = record.get("idx");
		//设置“计划开始日期”和“计划结束日期”
		TrainEnforcePlan.planStartDate = new Date(record.get("planStartDate")).format('Y-m-d');
		TrainEnforcePlan.planEndDate = new Date(record.get("planEndDate")).format('Y-m-d');
		//设置【机车施修计划明细】可用
		TrainEnforcePlanDetail.grid.enable();
		TrainEnforcePlanDetail.grid.store.load();
		/*
		 * 因为计划开始日期验证的时候计划结束日期还没有赋值
		 * 导致获取到的计划结束日期为当前日期，导致验证失败会提示一个错误信息
		 * 此处在显示窗口之后再验证一次，就能恢复
		 */
		TrainEnforcePlan.grid.saveForm.getForm().isValid();
		//禁止修改编制人和其所属单位
		Ext.getCmp("planPerson_Id").setDisabled(true);
		Ext.getCmp("planOrgId_Id").setDisabled(true);
		
		SnakerApprovalRecord.processInstID = record.get("processInstId");
		SnakerApprovalRecord.processId = data[0].get("processId");
		SnakerApprovalRecord.grid.store.load();
		
		// 如果状态不是【新建】和【拒绝】，则不可修改
		var planStatus = record.get('planStatus');
		
		TrainEnforcePlan.planStatus = planStatus ;
		
		if(planStatus != '10' && planStatus != '60'){
			Ext.getCmp("saveParts").setVisible(false);
			TrainEnforcePlanDetail.grid.toolbars[0].setVisible(false) ;
		}else{
			Ext.getCmp("saveParts").setVisible(true);
			TrainEnforcePlanDetail.grid.toolbars[0].setVisible(true) ;
		}
		
		// tab页面显示
		if(planStatus == '10'){
			TrainEnforcePlan.tab.hideTabStripItem(1);
			TrainEnforcePlan.tab.hideTabStripItem(2);
		}else{
			TrainEnforcePlan.tab.unhideTabStripItem(1);
			TrainEnforcePlan.tab.unhideTabStripItem(2);
		}
		
		TrainEnforcePlan.tab.setActiveTab(0);
		return true;
	},
	/**
     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
     * @param searchParam 查询表单的Json对象
     * @return {} 返回的Json数据格式对象,
     */
    searchFn: function(searchParam){
    	TrainEnforcePlan.searchParams = searchParam;
    	this.store.load({
    		params: {
    			entityJson: Ext.util.JSON.encode(TrainEnforcePlan.searchParams)
    		}
    	});
    }
});
//创建表单
TrainEnforcePlan.grid.createSaveForm();
//创建完成表单后给表单添加操作按钮
TrainEnforcePlan.grid.saveForm.buttonAlign = "center" ;
TrainEnforcePlan.grid.saveForm.addButton({
	text:"保存", iconCls:"saveIcon",id:'saveParts',
	handler: function(){
		TrainEnforcePlan.grid.saveFn();
	}
});
TrainEnforcePlan.grid.saveForm.addButton({
	text:"关闭", iconCls:"closeIcon",handler:function(){
		TrainEnforcePlan.grid.saveWin.hide();
	}
});
TrainEnforcePlan.grid.saveForm.style = "padding:30px",

// tab页面
TrainEnforcePlan.tab = new Ext.TabPanel({
	activeTab: 0,
	items: [{
            title: "基本信息", layout: "fit",  border: false, 
            items: {
				xtype: "panel",frame:true, autoScroll:true,layout: "border",buttonAlign:"center",
				items:[{
		            region: 'north',
		            layout: "fit",frame:true,collapsible:true, 
		            id:"tab_basicInfo",
		            height: 225, bodyBorder: false,
		            items:[TrainEnforcePlan.grid.saveForm]
		        },{
		            region : 'center',autoScroll:true,title: '计划明细',frame:true, layout : 'fit', bodyBorder: false, items : [ TrainEnforcePlanDetail.grid ]
		        }]
            }
        },{
		title: "审批意见",
		layout:"fit",
		border: false, 
		items: {
				xtype: "panel",frame:true, autoScroll:true,layout: "border",buttonAlign:"center",
				items:[{
		            region : 'center',autoScroll:true,title: '审批记录',frame:true, layout : 'fit', bodyBorder: false, items : [ SnakerApprovalRecord.grid ]
		        }]
            }
		},{
		title: "流程图",
		id:"flowDiagramTab",
		layout:"fit",
		border: false, 
		items: [{
				xtype: "panel",
				layout: "fit",
				border:false,
				html:'<iframe id="flowDiagramIframe" frameborder="0" scrolling="no" src="" width="100%" height="100%" style="margin: 0;padding: 0"></iframe>'
            }]
		}],
	listeners: {
		tabchange: function(tab, panel){
			panel.doLayout();
			
			var iframe = Ext.getDom('flowDiagramIframe');
			if ('flowDiagramTab' == panel.id && iframe) {
			    iframe.src = ctx +'/snaker/diagram.jsp?processId=' + SnakerApprovalRecord.processId
				    + '&orderId=' + SnakerApprovalRecord.processInstID;
			}
		}
	}
});

//重构新增编辑窗口
TrainEnforcePlan.grid.saveWin  = new Ext.Window({
    title: "新增", maximizable: true, width: 800, height: 550, 
    layout: "fit", plain: true, 
    closeAction: "hide", modal: true,
    items: [TrainEnforcePlan.tab]
});


TrainEnforcePlan.grid.store.on('beforeload', function(){
	var searchParams = TrainEnforcePlan.searchParams;
	if (undefined == TrainEnforcePlan.status) {
		searchParams.status = [10,60];
	} else {
		searchParams.status = TrainEnforcePlan.status;
	}	
	TrainEnforcePlan.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
});

//清空查询参数集并刷新列表
TrainEnforcePlan.clearSearchParams = function(_grid){
	_grid.store.load({
        	params:{
        		entityJson: Ext.util.JSON.encode({})
        	}    
        });
        _grid.store.baseParams.entityJson = Ext.util.JSON.encode({});
};
TrainEnforcePlan.grid.store.load({
	params:{
		entityJson: Ext.util.JSON.encode({planStartDate:TrainEnforcePlan.getCurrentYear("begin"),
		toPlanStartDate:TrainEnforcePlan.getCurrentYear("end")
		}
		)
	}
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainEnforcePlan.grid });
});