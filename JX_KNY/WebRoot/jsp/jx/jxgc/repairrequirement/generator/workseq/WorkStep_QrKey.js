/**
 * 检测/修项目（作业项-质量控制配置在作业工单上） 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkStep');                       //定义命名空间
WorkStep.searchParam = {} ;                    //定义查询条件
WorkStep.OperStatus = "add" ;     //定义操作状态
WorkStep.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workStep!pageQuery.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/workStep!logicDeleteWorkStep.action',
    saveURL: ctx + '/workStep!saveWorkStep_QrKey.action',             //保存数据的请求URL
    saveFormColNum:2,
    labelWidth: 120,
    fieldWidth: 240,
    tbar:[{
    	text:"新增",iconCls:"addIcon" ,handler: function(){ 
	    	WorkStep.addFn();
	    	WorkStep.saveWin.setTitle("新增");
	    	WorkStep.saveWin.show();
    	}
    },'delete',{
    	text:"关闭",iconCls:"closeIcon" ,handler: function(){
    			TrainQR.saveWin.hide();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { }
	},{
		header:'工序卡主键', dataIndex:'workSeqIDX',hidden:true, editor:{xtype:'hidden',  maxLength:50 }
	},{
		header:'作业项编码', dataIndex:'workStepCode',  editor:{ xtype:'hidden'}
	},{
		header:'检测/检修项目', dataIndex:'workStepName', editor:{ allowBlank:false,maxLength:50 }
	},{
		header:'技术要求或标准规定', dataIndex:'repairStandard', editor:{xtype:'textarea', maxLength:500 , width: 330,height:35, allowBlank:false},searcher: {disabled: true}		
	},{
		header:'作业顺序', dataIndex:'workStepSeq'
	}],
	searchFn: function(searchParam){ 
		WorkStep.searchParam = searchParam ;
        this.store.load();
	},
	toEditFn: function(grid, rowIndex, e){
        if(this.searchWin != null)  this.searchWin.hide();  
        var record = this.store.getAt(rowIndex);
        WorkStep.OperStatus = "edit";
        WorkStepEdit.idx = record.get("idx") ;
        
        //显示编辑窗口
        if(WorkStep.grid.isEdit){
	    	DetectItem.grid.store.load(); //刷新检测项
	    	WorkStepResult.grid.store.load();//刷新检测结果
	        WorkStep.saveWin.show();
	        WorkStep.saveWin.setTitle("编辑");
	        WorkStep.stepForm.getForm().reset();
	        WorkStep.stepForm.getForm().loadRecord(record);
	        DetectItem.grid.enable();
	    	WorkStepResult.grid.enable();
        	WorkStep.saveForm.buttons[2].setVisible(false);
        }
    },
    /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(data){ 
    	data.workSeqIDX = QREdit.idx;
    	return true; 
    },
    /**
     * 保存成功之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
		WorkStepEdit.idx = result.entity.idx ;
	    DetectItem.grid.enable();
	    WorkStepResult.grid.enable();
	    Ext.getCmp("WorkStepId").setValue(result.entity.idx) ;
	    if(WorkStep.saveWin.title == "新增") WorkStep.saveForm.buttons[2].setVisible(true);
	    if(WorkStep.saveWin.title == "编辑") WorkStep.saveForm.buttons[2].setVisible(false);
    }
});
//初始化表单
WorkStep.stepForm = new Ext.FormPanel({
	layout: "tableform",     border: false,      style: "padding:0px 15px 0px 20px", defaults: { bodyStyle:'padding:20px'},
    
    baseCls: "x-plain", align: "center", layoutConfig: {columns: 2}, labelWidth: 150,
    items: [{ name: "workStepName",  labelWidth:120, fieldLabel: "检测/检修项目",   xtype: "textfield",  width: 240, allowBlank:false, maxLength:50},
			{ name: "repairStandard", labelWidth:120, fieldLabel: "&nbsp;&nbsp;&nbsp;&nbsp;技术要求或标准规定", xtype:'textarea', maxLength:1500 , width: 330,height:50, allowBlank:false, rowspan:2 },
			{ name: "workStepSeq", id: "workStepSeq_Id",  labelWidth:120, fieldLabel: "作业顺序",   xtype: "numberfield",  width: 240,  maxLength:2},
			{ name: "workStepCode", id:"workStepCode",  labelWidth:120, xtype: "textfield", fieldLabel: "作业项编码 ",  maxLength:50},
			{ name: "idx", id:"WorkStepId", xtype:'hidden'},
			{ name: "workSeqIDX", xtype:'hidden'}
	]
});
WorkStep.saveForm = new Ext.Panel({
	layout: "form",     border: false,      style: "padding:0px",      
    baseCls: "x-plain", align: "center",    buttonAlign: "center",
    items: [{
			items: WorkStep.stepForm
			
		}
	],
    buttons: [{
		text:"保存", iconCls:"saveIcon",
		handler: function(){
			WorkStepEdit.saveFn();
		}
	},{
		text:"关闭", iconCls:"closeIcon",handler:function(){
			WorkStep.saveWin.hide();
			//取消行编辑
			WorkStepResult.grid.rowEditor.stopEditing(false);
			DetectItem.grid.rowEditor.stopEditing(false);
		}
	},{
		text:"继续新增", iconCls:"addIcon",handler:function(){
			WorkStep.addFn();
		}
	}]
})
//编辑选项卡列表
WorkStep.editTabs = new Ext.Panel({  
   layout:'border',//表格布局
   buttonAlign: 'center',
   frame: true,
   items: [{   		
	    region: 'north',            
	    //height: 80,
	    autoHeight: true,
	    style: "padding:0px",	    
	    xtype: "fieldset",
	    items: WorkStep.saveForm,
	    split: false,
		bodyBorder: false,
		title: '检测/检修项目'
	   },{ 
	    region:'west',
	    style: "padding:5px",
		width: 300,
		xtype: "fieldset",
		items: [WorkStepResult.grid],
		split: false,
		bodyBorder: false,
		layout: 'fit',
		title: '默认检测/检修结果'
	   },{
	    region: "center",
		style: "padding:5px",
		xtype: "fieldset",
		items: [DetectItem.grid],
		layout: 'fit',
		title: '检测项'
	 }] 
}); 


//定义作业项编辑窗口
WorkStep.saveWin = new Ext.Window({
	title: "编辑", width:700,height:380,layout: "fit", modal: true, plain: true,
	closeAction: "hide", maximized: true ,
	items: [WorkStep.editTabs] 
});
//根据作业工单主键获取其检测/修项目中的最大作业顺序
WorkStep.getMaxSeq = function(workSeqIDX){
	var cfg = {
            url: ctx + "/workStep!getMaxSeq.action", 
			params: {workSeqIDX: workSeqIDX},
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null && result.success == true) {
                    Ext.getCmp("workStepSeq_Id").setValue(result.seq);
                } 
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}

WorkStep.addFn = function(){
	WorkStep.stepForm.getForm().reset();
	WorkStepEdit.idx = '';
	WorkStep.getMaxSeq(QREdit.idx);
	WorkStepResult.grid.store.removeAll();
	DetectItem.grid.store.removeAll();
	DetectItem.grid.disable();
	WorkStepResult.grid.disable();
	WorkStep.saveForm.buttons[2].setVisible(false);
}

//添加过滤默认过滤信息
WorkStep.grid.store.on('beforeload',function(){
	var searchParam = WorkStep.searchParam;
	searchParam.workSeqIDX = QREdit.idx ; //检修工序卡主键
	var whereList = [] ;
	for (prop in searchParam) {
		if(prop == 'workSeqIDX'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
        	whereList.push({propName:prop, propValue: searchParam[prop], compare: Condition.IN}) ;
		}
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	
});

});