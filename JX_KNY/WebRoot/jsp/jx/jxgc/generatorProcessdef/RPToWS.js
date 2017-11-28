/**
 * 检修项目对应作业工单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainQR');                       //定义命名空间
	
	TrainQR.nodeIDX = "";
	
	/** **************** 定义全局变量开始 **************** */
	TrainQR.repairProjectIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	// 删除（批量删除）函数
	TrainQR.deleteFn = function(idx) {
		var projectIdxs = null;
		if (idx instanceof Array) {
			projectIdxs = idx;
		} else {
			projectIdxs = [idx];
		}
		
		 //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
        	jsonData: Ext.util.JSON.encode(projectIdxs),
            scope: TrainQR.grid, url: ctx + '/jobNodeUnionWorkSeq!deleteJobNodeUnionWorkSeqByRecordCardIDX.action', params: {
				nodeIDX: TrainQR.nodeIDX
			}
        });
	}
	
	TrainQR.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workSeq!findWorkSeqByNodeIDX.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workSeq!saveWorkSeqForQrKey.action',             //保存数据的请求URL
	    deleteURL: '/jobNodeUnionWorkSeq!deleteJobNodeUnionWorkSeqByRecordCardIDX.action',
	    saveFormColNum: 2 ,fieldWidth: 180,
	    searchFormColNum: 2,
	    tbar:[{
	    	text:'选择机车检修记录卡', iconCls: 'addIcon', handler: function(){
	        	WorkSeqSearcher.nodeIDX = TrainQR.nodeIDX;	
	    		WorkSeqSearcher.win.show();
	    	}
	    }, 'delete', '->', '机车检修记录卡名称：', {
	    	xtype:'textfield', width:180, id:'workSeqName', enableKeyEvents:true, emptyText:'输入机车检修记录卡名称快速检索', listeners: {
	    		keyup: function(filed, e) {
					// 如果敲下Enter（回车键），则触发添加按钮的函数处理
					if (e.getKey() == e.ENTER){
	    				TrainQR.grid.store.load();
					}
				}
	    	}
	    	
	    }, {
	    	text:"查询", iconCls:'searchIcon', handler:function(){
	    		TrainQR.grid.store.load();
	    	}
	    }, {
	    	text:"重置", iconCls:'resetIcon', handler:function(){
	    		Ext.getCmp('workSeqName').reset();
	    		TrainQR.grid.store.load();
	    	}
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"entityID", xtype:'hidden' }
		},{
			header:'编号', dataIndex:'workSeqCode',width:120, editor:{ id:"workSeqCodeId",  maxLength:50 , allowBlank: false }
		},{
			header:'检修车型', dataIndex:'pTrainTypeIDX', hidden: true, 
			editor:{  
				id:"trainType_comb",xtype: "Base_combo",	fieldLabel: "检修车型",
				business: 'trainType',													
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				forceSelection: true,
				hiddenName: "pTrainTypeIDX", 
				returnField: [{widgetId:"trainTypeShortName_Id",propertyName:"shortName"}],
				displayField: "shortName", valueField: "typeID",
				pageSize: 0, minListWidth: 200,
				editable:true,
				allowBlank: false,
				listeners : {   
		        	"select" : function() {   
	                	Ext.getCmp("chartNo_Id").setValue("");
	                	Ext.getCmp("isVirtual_Id").setValue("");
		        	}   
		    	}}	    
		},{
			header:'车型', dataIndex:'pTrainTypeShortName',hidden: true,  editor:{id: 'trainTypeShortName_Id', xtype: 'hidden' },
			searcher:{xtype: 'textfield'}, width: 60
		},{
			header:'机车检修记录卡名称', dataIndex:'workSeqName',width:200, editor:{ allowBlank:false, maxLength:50 }
		},{
			header:'记录卡序号', dataIndex:'seq', editor:{  maxLength:8 ,vtype: "positiveInt"},searcher: {disabled: true}
		},{
			header:'描述', dataIndex:'repairScope',hidden: false,  width:200, editor:{xtype:'textarea',  maxLength:1000 },searcher: {disabled: true}
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
		},{
			header:'isVirtual', dataIndex:'isVirtual',hidden:true, editor:{ xtype:'hidden', id:"isVirtual_Id" }
		},{
			header:'recordIDX', dataIndex:'recordIDX',hidden:true, editor:{ xtype:'hidden', id:"recordIDX_Id" }
		}],
	    searchFn: function(searchParam){ 
			TrainQR.searchParam = searchParam ;
	        this.store.load();
		},
		toEditFn: function(grid, rowIndex, e){
            var record = this.store.getAt(rowIndex);
            QREdit.clearQC();//清空质量选择项
            QREdit.SetWorkSeqQC(record.get("idx"), TrainQR.grid);//设置质量控制选择项
	        QREdit.toEditFn(grid, rowIndex, e, TrainQR.saveWin, TrainQR.editTabs);
	    },
	    deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        TrainQR.deleteFn($yd.getSelectedIdx(this));
	    },
	    editOrder: ['workSeqCode','workSeqName','pTrainTypeIDX','ratedWorkHours','buildUpTypeCode','buildUpTypeName','repairScope','safeAnnouncements']
	});
	
	//初始化表单
	TrainQR.grid.createSaveForm(); 
	QREdit.createSaveForm();
	//定义'fieldset'列表
	TrainQR.fieldset = [{
		bodyStyle: 'padding-left:10px;',
		items:{
			xtype: "fieldset",
			title: "基本信息编辑",
			autoHeight: true,
			width: 720,
			items: [
                TrainQR.grid.saveForm
            ]
		}
	},{
		bodyStyle: 'padding-left:10px;',
		items:{
			xtype: "fieldset",
			title: "质量检查项",
			width: 720,  //宽度设置
			items: QREdit.saveForm
		}
	}];
	//编辑选项卡列表
	TrainQR.editTabs = new Ext.TabPanel({
	    activeTab: 0, frame:true,
	    items:[{
	            title: "基本信息编辑", layout: "fit", frame: true, border: false, items: {
	                buttonAlign:"center",
	                autoScroll: true, //滚动条
	                defaults: {
	                    border: false
	                },            
	                items: TrainQR.fieldset,
	            	buttons: [{
	            		id:"trainWorkSeqSaveId", text: "保存", iconCls:"saveIcon", handler:function(){
							QREdit.saveFn(TrainQR.grid, TrainQR.editTabs) ; //调用新增方法（QREdit.js）中            			
	            		}
	            	},{
						text: "关闭", iconCls:"closeIcon", handler:function(){TrainQR.saveWin.hide();}
					}]
	            }
	        },{
	            title: "检测/检修项目", layout: "fit", border: false, items: [ WorkStep.grid ]
	        }]
	});
	//作业工单编辑窗口
	TrainQR.saveWin = new Ext.Window({
		title: "机车检修记录单编辑", maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
		items: [TrainQR.editTabs] 
	});
	//查询前添加过滤条件
	TrainQR.grid.store.on('beforeload',function(){
		var searchParam = {} ;
		searchParam.nodeIDX = TrainQR.nodeIDX; 		//检修节点idx
		searchParam.workSeqName = Ext.getCmp('workSeqName').getValue(); 		//检修节点idx
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
});