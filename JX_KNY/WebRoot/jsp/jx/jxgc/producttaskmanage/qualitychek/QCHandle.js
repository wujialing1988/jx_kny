/**
 * 质量检查 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('QCHandle');
	
	QCHandle.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/qCResultQuery!getQCList.action',                 //装载列表数据的请求URL
	    saveFormColNum: 2,  fieldWidth:200,
	    tbar:['search','-', {
	    	text: "批量质检",
	    	iconCls: 'pjglIcon',
	    	handler: function(){
	    		Biz.QCBatchSubmit(QCHandle.grid);
	    	}
	    },'-','refresh'],
	    fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'车型', dataIndex:'trainType', width:100, editor: { }
		},{
			header:'车号', dataIndex:'trainNo', width:100, editor: { }
		},{
			header:'修程', dataIndex:'repairClassName', width:50, editor: { }
		},{
			header:'修次', dataIndex:'repairtimeName', width:50, editor: { }
		},{
			header:'质量检验项', dataIndex:'workItemName', width:100, editor: { }
		},{
			header:'作业工单名称', dataIndex:'workCardName', width:150, editor: { }
		},{
			header:'位置', dataIndex:'fixPlaceFullName', hidden: true, width:200, editor: { }
		},{
			header:'作业班组', dataIndex:'repairTeam', width:150, editor: { }
		},{
			header:'rdpIdx', dataIndex:'rdpIdx', editor: { }, hidden:true
		},{
			header:'sourceIdx', dataIndex:'sourceIdx', editor: { }, hidden:true
		},{
			header:'checkItemCode', dataIndex:'checkItemCode', editor: { }, hidden:true
		}],
		searchOrder:[{
            fieldLabel: '生产任务单',
            xtype: 'Base_combo',
            hiddenName: 'rdpIDX',           
        	fields: ["rdpText", "idx"],
        	minChars:50,
        	maxLength:100,
        	displayField:'rdpText',
        	valueField:'idx',
        	action: ctx + "/qCResultQuery!findRdp.action?mode=" + CONST_INT_CHECK_WAY_BJ
        },{
        	xtype: "textfield",
        	fieldLabel:"作业工单名称",
        	name: "workCardName"
        },{
        	xtype: "textfield",
        	fieldLabel:"质量检验项",
        	name: "workItemName"
        }],
        searchFn: function(sp){
			this.store.sp = sp;
			this.store.load();
		},
		toEditFn: function(grid, rowIndex, e){
			var record = this.store.getAt(rowIndex);			
			QCHandle.showHandle(record);
		}
	});
	
	QCHandle.grid.store.on("beforeload", function(){
		
		var sp = this.sp;
		if(!sp){
			sp = new Object();
		}
		sp.vehicleType = vehicleType ;
		if(sp){
			this.baseParams.query = Ext.util.JSON.encode(sp);
		}
		this.baseParams.mode = "2";
	});
	
	QCHandle.createHandlerWin = function(){
		
		QCHandle.handlerWin = new Ext.Window({
    		title:"质量检验处理", width:800, height:500,
    		plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    		modal:true ,maximized : true, items: Base.tabs,
		    buttons:[
		    	{
				text: "确认任务",
				iconCls:"checkIcon",
				handler:function(){
		    		Biz.QCSubmit();
		    	}
			},{
				text: "关闭",
				iconCls:"closeIcon",
				handler:function(){
					QCHandle.handlerWin.hide()
				}
			}]
    	});
	}
	
	QCHandle.showHandle = function(record){
		
		if(!QCHandle.handlerWin)
			QCHandle.createHandlerWin();
		QCHandle.handlerWin.show();
		QCHandle.currentRecord = record;
		Base.loadWorkCard(record.get("sourceIdx"), record.get("rdpIdx"));
		Base.loadBaseInfo(record);
		
	}
	
	
	QCHandle.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "必检", layout: "fit", border: false, items: [ QCHandle.grid ]
	        },{
	        	title: "抽检", layout: "fit", border: false, items:[ Async.grid ]	       
	        }]
	});	
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items: QCHandle.tabs });
});