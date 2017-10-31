/**
 * 工步（作业项） 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkStepSearch');                       //定义命名空间
	WorkStepSearch.searchParam = {} ;                    //定义查询条件
	WorkStepSearch.OperStatus = "add" ;     //定义操作状态
	WorkStepSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workTask!pageQuery.action',                 //装载列表数据的请求URL
	    saveFormColNum:2,
	    labelWidth: 120,
	    storeAutoLoad: false,
	    singleSelect:true,
	    fieldWidth: 240,
	    tbar:[{
	    	text:"关闭",iconCls:"closeIcon" ,handler: function(){
	    		TrainWorkSearch.mainWin.hide();
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
			WorkStepSearch.searchParam = searchParam ;
	        this.store.load();
		},
		toEditFn: function(grid, rowIndex, e){
	        if(this.searchWin != null)  this.searchWin.hide();  
	        var record = this.store.getAt(rowIndex);
	        WorkStepSearch.OperStatus = "edit";
	        WorkStepEditSearch.idx = record.get("idx") ;
	        //显示编辑窗口
	        if(WorkStepSearch.grid.isEdit){
		    	DetectItemSearch.grid.store.load(); //刷新检测项
		    	
		        WorkStepSearch.saveWin.show();
		        WorkStepSearch.saveWin.setTitle("查看");
		        WorkStepSearch.stepForm.getForm().reset();
		        WorkStepSearch.stepForm.getForm().loadRecord(record);
		        DetectItemSearch.grid.enable();		    	
	        }
	    }
	});
	
	WorkStepSearch.stepForm = new Ext.FormPanel({
		layout: "tableform",     border: false,      style: "padding:0px 15px 0px 20px", defaults: { bodyStyle:'padding:20px'},
	    
	    baseCls: "x-plain", align: "center", layoutConfig: {columns: 2}, labelWidth: 100,
	    items: [{ name: "workTaskName",  labelWidth:120, fieldLabel: "检测/检修项目",   xtype: "textfield",  width: 240, allowBlank:false, maxLength:50, disabled: true},
				{ name: "repairStandard", labelWidth:120, fieldLabel: "&nbsp;&nbsp;&nbsp;&nbsp;技术要求或标准规定", xtype:'textarea', maxLength:1500 , width: 330,height:50, allowBlank:false, rowspan:2, disabled: true},
				{ name: "workTaskSeq", id: "workStepSeq_Id",  labelWidth:120, fieldLabel: "作业顺序",   xtype: "numberfield",  width: 240,  maxLength:2, disabled: true},
				{ name: "idx", id:"workTask_Id", xtype:'hidden', disabled: true}
		]
	});
	
	
	WorkStepEditSearch.createSaveForm();
	WorkStepSearch.saveForm = new Ext.Panel({
		layout: "form",     border: false,      style: "padding:0px",      
	    baseCls: "x-plain", align: "center",    buttonAlign: "center",
	    items: [{
				items: WorkStepSearch.stepForm
			}
		],
	    buttons: [{
			text:"关闭", iconCls:"closeIcon",handler:function(){
				WorkStepSearch.saveWin.hide();
			}
		}]
	})
	//编辑选项卡列表
	WorkStepSearch.editTabs = new Ext.Panel({  
	   layout:'border',//表格布局
	   buttonAlign: 'center',
	   frame: true,
	   items: [{   		
		    region: 'north',            
		    //height: 80,
		    autoHeight: true,
		    style: "padding:0px",	    
		    xtype: "fieldset",
		    items: WorkStepSearch.saveForm,
		    split: false,
			bodyBorder: false,
			title: '检测/检修项目'
		   },{
		    region: "center",
			style: "padding:5px",
			xtype: "fieldset",
			items: [DetectItemSearch.grid],
			layout: 'fit',
			title: '检测项'
		 }] 
	}); 
	
	
	//定义作业项编辑窗口
	WorkStepSearch.saveWin = new Ext.Window({
		title: "查看", width:700,height:380,layout: "fit", modal: true, plain: true,
		closeAction: "hide", maximized: true ,
		items: [WorkStepSearch.editTabs] 
	});
	
	//添加过滤默认过滤信息
	WorkStepSearch.grid.store.on('beforeload',function(){
		var searchParam = WorkStepSearch.searchParam;
		searchParam.workCardIDX = TrainWorkSearch.idx ; //检修工序卡主键
		var whereList = [] ;
		for (prop in searchParam) {	
			if(prop == 'workCardIDX'){
				whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
			}else{
	        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
			}
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		
	});
	
});