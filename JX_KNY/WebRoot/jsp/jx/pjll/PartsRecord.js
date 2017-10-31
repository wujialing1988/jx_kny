/** 机车主要部件定义维护*/
Ext.onReady(function(){
	Ext.namespace('PartsRecord');                       //定义命名空间
	
	var loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	
	
		// 机车检修记录单结果双击显示界面
		PartsRecord.toEditFn = function(grid, rowIndex, e){		
			if(this.searchWin != null)  this.searchWin.hide();  
			var record = grid.store.getAt(rowIndex);
		    // 检修记录单界面显示信息
			TrainWorkPlanCommHis.setParams(record.json);
			TrainWorkPlanCommHis.saveWin.show();
		} 
		
		
	// 配件集合
	PartsRecord.trainTypeGrid = new Ext.yunda.Grid({
		loadURL: ctx + "/partsRecord!pageList.action",
		singleSelect: true, 
		tbar:[{
			xtype:'textfield', id:'parts_no', enableKeyEvents:true, emptyText:'请输入配件编码或名称', listeners: {
	    		keyup: function(filed, e) {
	    			
	    		}
			}
				
		},{
			text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
			handler : function(){
				var partsNo = Ext.getCmp("parts_no").getValue();
				var searchParam = {};
				searchParam.partsNo = partsNo ;
				PartsRecord.trainTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});
			}
		},{
			text : "重置",
			iconCls : "resetIcon",
			handler : function(){
				PartsRecord.trainTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode({});
				PartsRecord.trainTypeGrid.getStore().load({
					params:{
						partsNo : ""
					}																
				});
				//清空搜索输入框
				Ext.getCmp("parts_no").setValue("");
				//清空机车组成查询集合
				PartsRecord.searchTrainParams = {};
				PartsRecord.PartsRecordGrid.getStore().load();
			}
		}],
			fields: [{
				header:'配件编码', dataIndex:'partsNo', editor:{ }, sortable: true
			},{
				header:'配件名称', dataIndex:'partsName', editor:{ }
	        }]
	});
	
	// 添加加载结束事件
	PartsRecord.trainTypeGrid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			PartsRecord.trainTypeGrid.getSelectionModel().selectFirstRow();
			PartsRecord.PartsRecordGrid.getStore().load();
		}
	});
	
	//单击车型记录过滤机车和虚拟组成列表
	PartsRecord.trainTypeGrid.on("rowclick", function(grid, rowIndex, e){
				PartsRecord.PartsRecordGrid.getStore().load();
			});
	//移除侦听器
	PartsRecord.trainTypeGrid.un('rowdblclick', PartsRecord.trainTypeGrid.toEditFn, PartsRecord.trainTypeGrid);
	
	//列表
	PartsRecord.PartsRecordGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdp!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig: {
	    	forceFit:true
	    },
	    tbar:null,
		fields: [{
			header:'主键', dataIndex:'idx', hidden: true
		},{
			header:'配件idx主键', dataIndex:'partsAccountIDX', hidden: true
		},{
			header:'识别码', dataIndex:'identificationCode', width: 140 ,hidden: true
		},{
			header:'配件名称', dataIndex:'partsName', width: 200
		},{
			header:'配件编号', dataIndex:'partsNo', width: 140
		},{
			header:'配件铭牌号', dataIndex:'nameplateNo', width: 140 ,hidden: true
		},{
			header:'规格型号', dataIndex:'specificationModel', width: 180
		},{
			header:'物料编码', dataIndex:'matCode', width: 60 ,hidden: true
		},{
			header:'检修班组', dataIndex:'repairOrgName'
		},{
			header:'下车车型', dataIndex:'unloadTrainType', hidden: true
		},{
			header:'下车车号', dataIndex:'unloadTrainNo', width: 90,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				var unloadTrainType = record.get('unloadTrainType');
				var text = Ext.isEmpty(unloadTrainType) ? "" : unloadTrainType;
				return text + (Ext.isEmpty(value) ? "" : value);
			}
		},{
			header:'下车修程', dataIndex:'unloadRepairClass', hidden: true
		},{
			header:'下车修次', dataIndex:'unloadRepairTime', width: 60,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				var unloadRepairClass = record.get('unloadRepairClass');
				var text = Ext.isEmpty(unloadRepairClass) ? "" : unloadRepairClass;
				return text + (Ext.isEmpty(value) ? "" : value)
			}
		},{
			header:'检修开始时间', dataIndex:'realStartTime', xtype: "datecolumn", format:"Y-m-d"
		},{
			header:'检修结束时间', dataIndex:'realEndTime', xtype: "datecolumn", format:"Y-m-d"
		},{
			header:'检修状态', dataIndex:'status', width: 60,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				if (STATUS_WQD == value) return '未启动';
				if (STATUS_JXZ == value) return '检修中';
				if (STATUS_DYS == value) return '待验收';
				if (STATUS_YZZ == value) return '已终止';
				if (STATUS_WFXF == value) return '无法修复';
				if (STATUS_JXHG == value) return '检修合格';
				return '错误！未知的状态';
			}
		}, {
			header:'检修需求单', dataIndex: 'wpDesc', hidden: true
		},{
			header:'下车位置', dataIndex:'unloadPlace'
		}],
		toEditFn: function(grid, rowIndex, e){
			PartsRdpDetail.record = grid.store.getAt(rowIndex).data;
			PartsRdpDetail.win.show();
	    }
	});
	
	// 数据加载前
	PartsRecord.PartsRecordGrid.store.on('beforeload', function() {
		var sm = PartsRecord.trainTypeGrid.getSelectionModel();
		var searchParams = {};
		searchParams.status = '0401' ;
		if (sm.getCount() > 0) {
			var records = sm.getSelections();
			searchParams.partsNo = records[0].data.partsNo;
		}	
	
		var whereList = []; 
		for(prop in searchParams){
			if('partsNo' == prop){
				whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.eq, stringLike: false});
				continue;
			}
			if('status' == prop){
				whereList.push({propName:prop, propValue:searchParams[prop], compare:Condition.eq, stringLike: false});
				continue;
			}
			whereList.push({propName:prop, propValue:searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	
	//tab选项卡布局
	PartsRecord.tabs = new Ext.TabPanel({
	    activeTab: 0, frame:true, singleSelect: true,
	    items:[{  
	       id: "PartsRecordTab", title: '配件检修记录单', layout:'fit', items: [PartsRecord.PartsRecordGrid]
	    }]
	});
	//机车组成页面
	PartsRecord.PartsRecordPanel =  new Ext.Panel( {
	    layout : 'border',
	    items : [ {
	        title: '配件信息', width: 350, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
	        collapsible : true,
	        autoScroll: true, layout: 'fit', items : [ PartsRecord.trainTypeGrid ]
	    }, {
	        region : 'center', layout: 'fit', bodyBorder: false, items: [ PartsRecord.tabs ]
	    } ]
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:PartsRecord.PartsRecordPanel });
});