/**
 * 配件检测项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordDI');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpRecordDI.labelWidth = 100;
	PartsRdpRecordDI.fieldWidth = 140;
	PartsRdpRecordDI.rdpRecordRIIDX = "###";				// 记录卡实例主键
	PartsRdpRecordDI.searchParams = {};						// 查询实体
	//PartsRdpRecordDI.partsIDValue = '';
	//PartsRdpRecordDI.num;
	/** ************** 定义全局变量结束 ************** */
	
	
	// 数据容器
	PartsRdpRecordDI.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:false, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/partsRdpRecordDI!findListForDI.action',
		fields:['idx','rdpRecordRIIDX','recordDIIDX','dataItemNo','dataItemName','isBlank','seqNo', 'dataItemResult','dataSource','partID','checkTime','checkEndTime']
	});
	PartsRdpRecordDI.store.setDefaultSort('seqNo', 'ASC');
	// 行选择模式
    PartsRdpRecordDI.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
    // 单据明细表格
    PartsRdpRecordDI.grid = new Ext.grid.EditorGridPanel({
		border: false, enableColumnMove: true, stripeRows: true, selModel: PartsRdpRecordDI.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: PartsRdpRecordDI.store,
		colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
			{
	        	sortable:false, dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	sortable:false, dataIndex:'rdpRecordRIIDX', hidden:true, header:'检修检测项实例主键'
	        },{
	        	sortable:false, dataIndex:'recordDIIDX', hidden:true, hidden:true, header:'数据项主键'
	        },{	
	        	sortable:false, dataIndex:'dataItemNo', header:'数据项编号', hidden:true
	        },{	
	        	sortable:false, dataIndex:'seqNo', header:'顺序号', width: 15
	        },{	
	        	sortable:false, dataIndex:'dataItemName', header:'数据项名称', width: 50
	        },{	
	        	sortable:false, dataIndex:'checkID', header:'检测项编码', hidden:true
	        },{	
	        	sortable:false, dataIndex:'partID', header:'配件二维码', hidden:true
	        },{	
	        	sortable:false, dataIndex:'checkTime', header:'检测开始时间', hidden:true
	        },{	
	        	sortable:false, dataIndex:'checkEndTime', header:'检测结束时间', hidden:true
	        },{	
	        	sortable:false, dataIndex:'dataItemResult', header:'检测结果', width: 30, editor: {
	        		xtype: 'textfield',
	        		maxLength: 30
	        	}
	        },/*{
	        	header:'检测结果', dataIndex:'dataItemResult', width: 25,
	        	editor: {
	        		id:"check_Base_combo",
	        		xtype:'PartsCheckItemData_SelectWin',
	        		editable: true,
	        		valueField : 'checkValue',
					displayField : 'checkValue',
	        		allowBlank: false,
	        		fieldLabel : "检测项结果",
	        		hiddenName:'dataItemResult',
	        		returnField: [{ widgetId: "check_Base_combo", propertyName: "checkValue" }],
					returnFn : function(grid, rowIndex, e) {
						var record = grid.store.getAt(rowIndex);
						var newRecord = PartsRdpRecordDI.grid.getStore().getAt(PartsRdpRecordDI.num);
						newRecord.data.dataItemResult = record.json.checkValue;
						//可视化系统选择
						newRecord.data.dataSource = VISUALINPUT;
						PartsRdpRecordDI.grid.getView().refreshRow(newRecord);
						//调用暂存方法
						PartsRdpRecordRI.submitFn(true)
					}
				}
	        },*/{
		        	sortable:false, dataIndex:'isBlank', header:'是否必填', width: 15, renderer: function(v){
					if (v == IS_BLANK_YES) return '是';
					if (v == IS_BLANK_NO) return '否';
				}
	        },{
		        	sortable:false, dataIndex:'dataSource', header:'数据来源', width: 15, 
		        	renderer: function(v){
						if (v == HANDINPUT) return '手工录入';
						if (v == VISUALINPUT) return '自动采集';
					}
	        },{	
	        	sortable:false, dataIndex:'aa', header:'监控视频',
	        	renderer: function(v){
					return "<a href='#' onclick='openWin();'>点击预览</a>"
	        	}
	        }]),
	       listeners: {
			afteredit:function(e){
				if(e.field == "dataItemResult") {
					/*//获取组件值
					//alert(Ext.getCmp("check_Base_combo").getValue()); 
					//获取组件dom值
					//alert(Ext.get("check_Base_combo").dom.value); 
					e.record.data.dataItemResult = Ext.get("check_Base_combo").dom.value;}*/
					//手工录入
					delete e.record.data.partID;
					delete e.record.data.checkTime;
					delete e.record.data.checkEndTime;
					e.record.data.dataSource = HANDINPUT;
					//调用暂存方法
					PartsRdpRecordRI.submitFn(true)
				}
			}
			/*,
			cellclick:function(grid, rowIndex, columnIndex, e) {
				if (columnIndex == 8) {
					PartsRdpRecordDI.num = rowIndex;
				}
			}*/
		}
    });
	PartsRdpRecordDI.grid.store.on("beforeload", function(){
		var searchParams = {rdpRecordRIIDX: PartsRdpRecordDI.rdpRecordRIIDX};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);  
		this.baseParams.rdpRecordRIIDX = PartsRdpRecordDI.rdpRecordRIIDX;  
	});
	/*
	PartsRdpRecordDI.grid.on("rowclick", function(a,b,c){
		var recordV = this.store.getAt(b);
		var checkIDValue = recordV.json.checkID;
		Ext.getCmp('check_Base_combo').setCheckIDValue(checkIDValue);
		Ext.getCmp('check_Base_combo').setPartsIDValue(PartsRdpRecordDI.partsIDValue);
		Ext.getCmp('check_Base_combo').setRdpIDXValue(PartsRdpRecordCard.rdpIDX);
	});*/
	
});

function openWin(){
	var URL = "http://127.0.0.1:8083/previewAction!gotoVedioPlaybackPage.action?startTime=2016-7-13 17:10:10&endTime=2016-7-30 17:10:10";
	var obj = new Object(); 
	obj.str="51js"; 
	window.showModalDialog(URL,obj,"dialogWidth=1000px;dialogHeight=700px;center=yes");
}