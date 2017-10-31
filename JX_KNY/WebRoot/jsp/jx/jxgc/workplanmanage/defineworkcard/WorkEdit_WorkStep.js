/**
 * 工步（作业项） 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkStep');                       //定义命名空间
	WorkStep.searchParam = {} ;                    //定义查询条件
	WorkStep.OperStatus = "add" ;     //定义操作状态
	WorkStep.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workTask!pageQuery.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/workTask!logicDelete.action',
	    saveURL: ctx + '/workTask!saveOrUpdate.action',             //保存数据的请求URL
	    saveFormColNum:2,
	    labelWidth: 120,
	    storeAutoLoad: false,
	    fieldWidth: 240,
	    tbar:[{
	    	text:"新增",iconCls:"addIcon" ,handler: function(){
		    	WorkStep.addFn();
		    	WorkStep.saveWin.setTitle("新增");
		    	WorkStep.saveWin.show();
	    	}
	    },'delete',{
	    	text:"关闭",iconCls:"closeIcon" ,handler: function(){
	    		TrainWork.mainWin.hide();
	    	}
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { }
		},{
			header:'工序卡主键', dataIndex:'workCardIDX',hidden:true, editor:{xtype:'hidden',  maxLength:50 }
		},{
			header:'作业项编码', dataIndex:'workTaskCode', hidden:true, editor:{ xtype:'hidden'}
		},{
			header:'检测/检修项目', dataIndex:'workTaskName', editor:{ allowBlank:false,maxLength:50 }
		},{
			header:'技术要求或标准规定', dataIndex:'repairStandard', editor:{xtype:'textarea', maxLength:500 , width: 330,height:35, allowBlank:false},searcher: {disabled: true}
		},{
			header:'作业顺序', dataIndex:'workTaskSeq'
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
		    	
		        WorkStep.saveWin.show();
		        WorkStep.saveWin.setTitle("编辑");
		        WorkStep.stepForm.getForm().reset();
		        WorkStep.stepForm.getForm().loadRecord(record);
		        DetectItem.grid.enable();
		    	WorkStepEdit.clearQC();//清空质量选择项
	        	WorkStepEdit.SetWorkSeqQC(record.get("idx"), WorkStep.grid);//设置质量控制选择项
	        	WorkStep.saveForm.buttons[2].setVisible(false);
	        }
	    },
	    /**
	     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
	     * @param {Object} data 要保存的数据记录，json格式
	     * @return {Boolean} 如果返回fasle将不保存记录
	     */
	    beforeSaveFn: function(data){ 
	    	data.workCardIDX = TrainWork.idx;
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
		    Ext.getCmp("workTask_Id").setValue(result.entity.idx) ;
		    if(WorkStep.saveWin.title == "新增") WorkStep.saveForm.buttons[2].setVisible(true);
		    if(WorkStep.saveWin.title == "编辑") WorkStep.saveForm.buttons[2].setVisible(false);
	    }
	});
	WorkStep.stepForm = new Ext.FormPanel({
		layout: "tableform",     border: false,      style: "padding:0px 15px 0px 20px", defaults: { bodyStyle:'padding:20px'},
	    
	    baseCls: "x-plain", align: "center", layoutConfig: {columns: 2}, labelWidth: 100,
	    items: [{ name: "workTaskName",  labelWidth:120, fieldLabel: "检测/检修项目",   xtype: "textfield",  width: 240, allowBlank:false, maxLength:50},
				{ name: "repairStandard", labelWidth:120, fieldLabel: "&nbsp;&nbsp;&nbsp;&nbsp;技术要求或标准规定", xtype:'textarea', maxLength:500 , width: 330,height:50, allowBlank:false, rowspan:2 },
				{ name: "workTaskSeq", id: "workStepSeq_Id",  labelWidth:120, fieldLabel: "作业顺序",   xtype: "numberfield",  width: 240,  maxLength:2},
				{ name: "idx", id:"workTask_Id", xtype:'hidden'}
		]
	});
	
	
	WorkStepEdit.createSaveForm();
	WorkStep.saveForm = new Ext.Panel({
		layout: "form",     border: false,      style: "padding:0px",      
	    baseCls: "x-plain", align: "center",    buttonAlign: "center",
	    items: [{
				items: WorkStep.stepForm
				
			},{
				layout: "form",
				anchor: "40%",
				items: WorkStepEdit.saveForm
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
			title: '检测/检修项目及质量检查'
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
	
	
	WorkStep.addFn = function(){
		WorkStep.stepForm.getForm().reset();
		WorkStepEdit.idx = '';
		WorkStep.getMaxSeq(TrainWork.idx);
		
		DetectItem.grid.store.removeAll();
		DetectItem.grid.disable();
		WorkStepEdit.clearQC();//清空质量选择项
		WorkStep.saveForm.buttons[2].setVisible(false);
	}
	
	//添加过滤默认过滤信息
	WorkStep.grid.store.on('beforeload',function(){
		var searchParam = WorkStep.searchParam;
		searchParam.workCardIDX = TrainWork.idx ; //检修工序卡主键
		var whereList = [] ;
		for (prop in searchParam) {		
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		
	});
	
	WorkStep.getMaxSeq = function(workCardIDX){

		var cfg = {
            url: ctx + "/workTask!getMaxSeq.action", 
			params: {workCardIDX: workCardIDX},
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

});