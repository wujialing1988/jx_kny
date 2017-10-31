/**
 * 机车施修计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainEnforcePlanTaskTodo');                       //定义命名空间
TrainEnforcePlanTaskTodo.idx = '';   //主键全局变量
TrainEnforcePlanTaskTodo.planStartDate = '';
TrainEnforcePlanTaskTodo.planEndDate = '';
TrainEnforcePlanTaskTodo.searchParams = {} //查询全局参数集


//机车施修计划列表
TrainEnforcePlanTaskTodo.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlan!getProcessWork.action',                 //装载列表数据的请求URL
    searchFormColNum: 2,
    saveFormColNum: 2,
    tbar: [{   
            text:"审批", iconCls:"checkIcon", handler: function(){
                var grid = TrainEnforcePlanTaskTodo.grid;
	    		if(!$yd.isSelectedRecord(grid)) return;
	    		var data = grid.selModel.getSelections();
	    		if (data.length > 1) {
	    			MyExt.Msg.alert("只能选择一条记录");
		        	return;
	    		}
	    		TrainEnforcePlanTaskTodo.grid.saveForm.getForm().reset();
	    		TrainEnforcePlanTaskTodo.grid.saveForm.getForm().loadRecord(data[0]);
	    		TrainEnforcePlanTaskTodo.grid.saveWin.setTitle("审批 - "+data[0].get("planName"));
				
				//设置“机车施修计划主键”全局变量
				TrainEnforcePlanTaskTodo.idx = data[0].get("idx");
				//设置“计划开始日期”和“计划结束日期”
				TrainEnforcePlanTaskTodo.planStartDate = new Date(data[0].get("planStartDate")).format('Y-m-d');
				TrainEnforcePlanTaskTodo.planEndDate = new Date(data[0].get("planEndDate")).format('Y-m-d');
				TrainEnforcePlanDetailTodo.grid.store.load();
				SnakerApprovalRecord.processInstID = data[0].get("processInstId");
				SnakerApprovalRecord.processId = data[0].get("processId");
				SnakerApprovalRecord.grid.store.load();
	    		
	    		TrainEnforcePlanTaskTodo.grid.saveWin.show();
            } 
        },'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { id:'idx', xtype:'hidden' }
	},{
		header:'流程模板ID', dataIndex:'processId', hidden:true, editor: { id:'processId', xtype:'hidden' }
	},{
		header:'流程实例ID', dataIndex:'processInstId', hidden:true, editor: { id:'processInstId', xtype:'hidden' }
	},	{
		header:'计划名称', dataIndex:'planName',id:'planName', editor:{  maxLength:100, allowBlank: false ,disabled:true }, searcher: {anchor:'98%'}
	},{
		header:'编制日期', dataIndex:'planTime', xtype:'datecolumn',format: "Y-m-d", editor:{ xtype:'my97date',initNow:true, allowBlank: false,disabled:true },
		searcher:{disabled: true }
	},{
		header:'计划入段日期', dataIndex:'planStartDate', xtype:'datecolumn', 
		editor:{ id: 'planStartDate_Id', xtype:'my97date',my97cfg: {dateFmt:'yyyy-MM-dd'}, 
				allowBlank: false ,disabled:true },
		searcher:{id: 'planStartDate_SearchId', xtype:'my97date', anchor:'98%',
			listeners : {
				//渲染查询面板时,动态绑定方法
				"render":function(){
					//给查询面板绑定beforeshow方法,在面板显示前,设置各个常用查询条件,减少录入步骤
					TrainEnforcePlanTaskTodo.grid.searchWin.on('beforeshow',function(){
						
					});
				}
			}	
		}
	},{
		header:'计划离段日期', dataIndex:'planEndDate', xtype:'datecolumn', 
		editor:{ id: 'planEndDate_Id', xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd'}, 
				 allowBlank: false,disabled:true },
				searcher:{id: 'planEndDate_SearchId', xtype:'my97date', anchor:'98%', initNow:false}
	},{
		header:'编制人', dataIndex:'planPersonName', editor:{ id:"planPersonName_Id", maxLength:25,disabled:true }
	},{
		header:'编制单位', dataIndex:'planOrgName', editor:{ id: "planOrgName_Id" ,disabled:true}
	},{
		header:'计划状态', dataIndex:'planStatus', hidden: false,editor:{ xtype:'hidden' },renderer:function(v){
			var vreturn = "";
			if(v == '10'){
				vreturn = "编制完成" ;
			}else if(v == '20'){
				vreturn = "审核中" ;
			}else if(v == '30'){
				vreturn = "审核完成" ;
			}else if(v == '40'){
				vreturn = "已经兑现" ;
			}else if(v == '50'){
				vreturn = "计划完成" ;
			}
			return vreturn ;
		}
	},{
		header:'任务ID', dataIndex:'workId', hidden: true, editor:{ id: "workId_Id", xtype: "hidden" }
	},{
		header:'任务名称', dataIndex:'workName', editor:{ id: "workName_Id", xtype: "hidden" }
	}],
	searchOrder: ['planName','planPersonName',
	 //计划开始时间(起)	计划结束日期(止)
	 'planStartDate',{id:'toPlanStartDate',fieldLabel:"到  ",xtype:'my97date',  anchor:'98%', my97cfg: {dateFmt:"yyyy-MM-dd"},initNow: false}
	 //计划结束时间(起)	计划结束时间(止)
	 ,'planEndDate', {id:'toplanEndDate',  fieldLabel:"到  ",xtype:'my97date',  anchor:'98%', my97cfg: {dateFmt:"yyyy-MM-dd"},initNow: false}
	],
	/**
     * 显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
    afterShowSaveWin: function(){
    	var data = TrainEnforcePlanTaskTodo.grid.selModel.getSelections();//获取当前用户选择的行
		TrainEnforcePlanTaskTodo.grid.saveWin.setTitle("审批 - "+data[0].get("planName"));
		//设置“机车施修计划主键”全局变量
		TrainEnforcePlanTaskTodo.idx = record.get("idx");
		//设置“计划开始日期”和“计划结束日期”
		TrainEnforcePlanTaskTodo.planStartDate = new Date(record.get("planStartDate")).format('Y-m-d');
		TrainEnforcePlanTaskTodo.planEndDate = new Date(record.get("planEndDate")).format('Y-m-d');
		TrainEnforcePlanDetailTodo.grid.store.load();
		SnakerApprovalRecord.processInstID = record.get("processInstId");
		SnakerApprovalRecord.processId = record.get("processId");
		SnakerApprovalRecord.grid.store.load();
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
	    TrainEnforcePlanDetail.grid.store.load();
    },
     /**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
	afterShowEditWin:function(record, rowIndex){
		var data = TrainEnforcePlanTaskTodo.grid.selModel.getSelections();//获取当前用户选择的行
		TrainEnforcePlanTaskTodo.grid.saveWin.setTitle("审批 - "+data[0].get("planName"));
		//设置“机车施修计划主键”全局变量
		TrainEnforcePlanTaskTodo.idx = record.get("idx");
		//设置“计划开始日期”和“计划结束日期”
		TrainEnforcePlanTaskTodo.planStartDate = new Date(record.get("planStartDate")).format('Y-m-d');
		TrainEnforcePlanTaskTodo.planEndDate = new Date(record.get("planEndDate")).format('Y-m-d');
		TrainEnforcePlanDetailTodo.grid.store.load();
		SnakerApprovalRecord.processInstID = record.get("processInstId");
		SnakerApprovalRecord.processId = record.get("processId");
		SnakerApprovalRecord.grid.store.load();
		return true;
	},
	/**
     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
     * @param searchParam 查询表单的Json对象
     * @return {} 返回的Json数据格式对象,
     */
    searchFn: function(searchParam){
    	TrainEnforcePlanTaskTodo.searchParams = searchParam;
    	this.store.load({
    		params: {
    			entityJson: Ext.util.JSON.encode(TrainEnforcePlanTaskTodo.searchParams)
    		}
    	});
    }
});

// 审批意见
TrainEnforcePlanTaskTodo.opinionsForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 1, 
            items: [{ 
            	fieldLabel:"审批意见", allowBlank:false,name:"opinions",id:"opinions", xtype: "textarea", maxLength:100,width:"100%"
            }]
        }, {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 1, 
            items: [{
	            		xtype: 'radiogroup',
			            fieldLabel: '审批结果',
			            name: 'opinionsResult',
			            id:'opinionsResult',
			            allowBlank: false,
			            items: [
			                { boxLabel: '同意',inputValue: '01', name: 'opinionsResult' ,checked:true },
			                { boxLabel: '不同意',inputValue: '02', name: 'opinionsResult' }
			                /*{ boxLabel: '驳回上一级',inputValue: '03', name: 'opinionsResult' }*/
			            ]

	            	}]
        }]
    }]
});


//创建表单
TrainEnforcePlanTaskTodo.grid.createSaveForm();
//创建完成表单后给表单添加操作按钮
TrainEnforcePlanTaskTodo.grid.saveForm.style = "padding:30px",

TrainEnforcePlanTaskTodo.tab = new Ext.TabPanel({
	activeTab: 0,
	items: [{
            title: "基本信息", layout: "fit",  border: false, 
            items: {
				xtype: "panel",frame:true, autoScroll:true,layout: "border",buttonAlign:"center",
				items:[{
		            region: 'north',
		            layout: "fit",frame:true,collapsible:true, 
		            id:"tab_basicInfo",
		            height: 195, bodyBorder: false,
		            items:[TrainEnforcePlanTaskTodo.grid.saveForm]
		        },{
		            region : 'center',autoScroll:true,title: '计划明细',frame:true, layout : 'fit', bodyBorder: false, items : [ TrainEnforcePlanDetailTodo.grid ]
		        }]
            }
        },{
		title: "审批意见",
		layout:"fit",
		border: false, 
		items: {
				xtype: "panel",frame:true, autoScroll:true,layout: "border",buttonAlign:"center",
				items:[{
		            region: 'north',
		            layout: "fit",frame:true,collapsible:true, 
		            id:"tab_opinions",
		            height: 225, bodyBorder: false,
		            title:'审批意见',
		            items:[TrainEnforcePlanTaskTodo.opinionsForm]
		        },{
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
TrainEnforcePlanTaskTodo.grid.saveWin  = new Ext.Window({
    title: "审批", maximizable: true, width: 800, height: 550, 
    layout: "fit", plain: true, 
    closeAction: "hide", modal: true,buttonAlign:"center",
    items: [TrainEnforcePlanTaskTodo.tab],
	buttons: [{
    		text:"确定",
    		iconCls:"checkIcon",
    		handler:function(){
	    		var data = TrainEnforcePlanTaskTodo.grid.saveForm.form.getValues();
	    		var opinions =  Ext.getCmp('opinions').getValue();
	    		if(Ext.isEmpty(opinions)){
	    			MyExt.Msg.alert("请填写审批意见！");
	            	return;
	    		}
	    		var opinionsResult =  Ext.getCmp('opinionsResult').getValue().inputValue;
	    		Ext.Ajax.request({
					url:ctx + '/trainEnforcePlan!approvalProcess.action',
					params: {
						idx: data.idx ,
						processInstID:data.processInstId,
						workId:data.workId,
						opinions:opinions,
						opinionsResult:opinionsResult
					},
				    //请求成功后的回调函数
					success: function(response, options){
			        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				            alertSuccess();
				            TrainEnforcePlanTaskTodo.grid.saveWin.hide();
				            TrainEnforcePlanTaskTodo.grid.store.load();
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
    		}
    	},{
			text: "关闭",
			iconCls: "closeIcon",
			handler: function(){
				TrainEnforcePlanTaskTodo.grid.saveWin.hide();
			}
		}]
});


//清空查询参数集并刷新列表
TrainEnforcePlanTaskTodo.clearSearchParams = function(_grid){
	_grid.store.load({
        	params:{
        		entityJson: Ext.util.JSON.encode({})
        	}    
        });
        _grid.store.baseParams.entityJson = Ext.util.JSON.encode({});
};

TrainEnforcePlanTaskTodo.grid.store.load();
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainEnforcePlanTaskTodo.grid });
});