/**
 * 机车检修项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobNodeMatDef');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	JobNodeMatDef.nodeIDX = "";
	JobNodeMatDef.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 删除（批量删除）函数
	JobNodeMatDef.deleteFn = function(idx) {
		var jobNodeMatDefIdxs = null;
		if (idx instanceof Array) {
			jobNodeMatDefIdxs = idx;
		} else {
			jobNodeMatDefIdxs = [idx];
		}
		
		 //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
//        	jsonData: Ext.util.JSON.encode(jobNodeMatDefIdxs),
            scope: JobNodeMatDef.grid, url: ctx + '/jobNodeMatDef!delete.action', params: {
				ids: jobNodeMatDefIdxs
			}
        });
	}
	
	JobNodeMatDef.addFn = function(){
		var records = JobNodeMatDef.grid.getStore().getModifiedRecords();
		var datas = [];
		for(var a = 0;a<records.length;a++){
			var obj = {};		
			obj.idx = records[a].data.idx;
			obj.processIDX = records[a].json.processIDX;
			obj.nodeIDX = records[a].json.nodeIDX;
			obj.matCode = records[a].data.matCode;
			obj.matDesc = records[a].data.matDesc;
			obj.nodeName = records[a].data.nodeName;
			obj.parentNodeIDX = records[a].data.parentNodeIDX;
			obj.qty = records[a].data.qty;
			obj.unit = records[a].data.unit;
			
	//可以成功修改数量，但作业流程主键，作业节点主键没有取到，修改后这两个字段为空
		
			datas.push(obj);
		}
		
        Ext.Ajax.request({
        	url: ctx + '/jobNodeMatDef!Update.action',
        	jsonData: datas,
        	success: function(response,options){
        		JobNodeMatDef.loadMask.hide();
        		var result = Ext.util.JSON.decode(response.responseText);
        		if (result.errMsg == null) {
                    alertSuccess();
					// 重新加载表格数据
					JobNodeMatDef.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
        	},
        	failure: function(response,options){
        		JobNodeMatDef.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        	}
        });
	}
	
	MatInforList.submit = function(){
		var sm = MatInforList.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert("尚未选择一条记录");
			return;
		}
		// 已选择的待添加的物料信息
		var selectedRecrods = sm.getSelections();
		// 检验已选择的待添加的物料信息是否已经被添加
		var count = JobNodeMatDef.grid.store.getCount();
		if (count != 0) {
			for (var i = 0; i < count; i++) {
				var record = JobNodeMatDef.grid.store.getAt(i);
				for (var j = 0; j < selectedRecrods.length; j++) {
					if (record.get('matCode') == selectedRecrods[j].get('matCode')) {
						MyExt.Msg.alert("物料编码<font color='red'>【" + record.get('matCode') + "】</font>已存在列表中，请不要重复添加");
   	          			return ;
					}
				}
			}
		 }
		var datas = [];
		for (var k = 0; k < selectedRecrods.length; k++) {
			var data = {};
			data.nodeIDX = JobNodeMatDef.nodeIDX;		// 流程节点idx主键	
			data.matCode = selectedRecrods[k].get('matCode');		// 物料编码
			data.matDesc = selectedRecrods[k].get('matDesc');		// 物料描述
			data.unit = selectedRecrods[k].get('unit');				// 计量单位
			data.qty = 1;											// 数量默认为”1“
			datas.push(data);
		}
		JobNodeMatDef.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/jobNodeMatDef!saveNodeMats.action',
            jsonData: datas,
            success: function(response, options){
              	JobNodeMatDef.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
					// 添加成功后，隐藏【物料选择】 窗口
					 MatInforList.batchWin.hide();
					// 重新加载 【物料选择】 窗口表格数据
					MatInforList.grid.store.reload();
					// 重新加载表格数据
					JobNodeMatDef.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                JobNodeMatDef.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	/** **************** 定义全局函数结束 **************** */
	
	// 子节点表单数据容器
	JobNodeMatDef.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:true, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/jobNodeMatDef!pageQuery.action',
		fields:['idx','nodeName','qty','parentNodeIDX','matCode','matDesc','unit','price']
	});
	
	// 操作掩码
    JobNodeMatDef.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 行选择模式
    JobNodeMatDef.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    //材料规格型号，分页工具
	JobNodeMatDef.pagingToolbar = Ext.yunda.createPagingToolbar({store: JobNodeMatDef.store});
	JobNodeMatDef.grid = new Ext.grid.EditorGridPanel({  
	  	border: true, enableColumnMove: true, stripeRows: true, selModel: JobNodeMatDef.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: JobNodeMatDef.store,
		colModel:new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),{
			sortable:false, dataIndex:'idx', hidden:true, header:'idx'
		},{
			header:'物料编码', dataIndex:'matCode', width: 200, editor: { readOnly: true }
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200, editor: { readOnly: true }
		},{
			header:'单位', dataIndex:'unit', width: 100, editor: { readOnly: true }
		},{
			sortable:false, dataIndex:'qty', header:'数量', editor:{ 	
		        		maxLength: 6, 
		        		vtype: "nonNegativeInt", 
		        		allowBlank: false
					}
		},{
			header:'操作', dataIndex:'idx', align:'center', width: 60, renderer: function(value){
				return "<img src='" + deleteIcon + "' alt='删除' style='cursor:pointer' onclick='JobNodeMatDef.deleteFn(\""+ value +"\")'/>";
			}
		},{
			header:'流程idx', dataIndex:'processIDX', hidden:true, editor: { readOnly: true }
		},{
			header:'父节点idx', dataIndex:'parentNodeIDX', hidden:true, editor: { readOnly: true }
		},{
			header:'节点idx', dataIndex:'nodeIDX', hidden:true, editor: { readOnly: true }
		},{
			header:'节点名称', dataIndex:'nodeName', hidden:true, editor: { readOnly: true }
		}]),
		tbar:[{
	    	text:'选择物料', iconCls: 'addIcon', handler:function(){
	    		MatInforList.batchWin.show();
	    	}
	    }, {
	    	text:"保存", iconCls:'saveIcon',handler:JobNodeMatDef.addFn
	    }],
	    toobal:JobNodeMatDef.pagingToolbar,
	    afterDeleteFn:function(){
	    }
	});
	
	JobNodeMatDef.grid.store.on('beforeload', function(){
		var whereList = [];
		if (!Ext.isEmpty(JobNodeMatDef.nodeIDX)) {
			sql = "IDX IN (SELECT IDX FROM JXGC_JobNode_Mat_Def WHERE RECORD_STATUS = 0 AND NODE_IDX = '"+ JobNodeMatDef.nodeIDX +"')"
			whereList.push({sql: sql, compare:Condition.SQL});
		}		
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	//  查询表单容器
	JobNodeMatDef.gridQuery = new Ext.yunda.Grid({
	    loadURL: ctx + '/jobNodeMatDef!pageQuery.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/jobNodeMatDef!logicDelete.action',
	    tbar:[ '物料描述：', {
	    	xtype:'textfield', width:180, id:'matDesc', enableKeyEvents:true, emptyText:'输入物料描述快速检索', listeners: {
	    		keyup: function(filed, e) {
					// 如果敲下Enter（回车键），则触发添加按钮的函数处理
					if (e.getKey() == e.ENTER){
	    				JobNodeMatDef.gridQuery.store.load();
					}
				}
	    	}
	    	
	    }, {
	    	text:"查询", iconCls:'searchIcon', handler:function(){
	    		JobNodeMatDef.gridQuery.store.load();
	    	}
	    }, {
	    	text:"重置", iconCls:'resetIcon', handler:function(){
	    		Ext.getCmp('matDesc').reset();
	    		JobNodeMatDef.gridQuery.store.load();
	    	}
	    }],
	    storeAutoLoad:false,
		fields: [{
			header:'物料编码', dataIndex:'matCode', width: 120
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200		
		}],
	    afterDeleteFn:function(){
	    }
	});
	// 设置默认排序
	JobNodeMatDef.gridQuery.store.setDefaultSort('matCode', 'ASC');
	JobNodeMatDef.gridQuery.un('rowdblclick', JobNodeMatDef.grid.toEditFn, JobNodeMatDef.gridQuery);
	
	JobNodeMatDef.gridQuery.store.on('beforeload', function(){
		var whereList = [];
		if (!Ext.isEmpty(WorkStation.nodeIDX)) {
			sql = "IDX IN (SELECT IDX FROM JXGC_JobNode_Mat_Def WHERE RECORD_STATUS = 0 AND Parent_Node_IDX = '"+ JobProcessNodeDef.parentIDX +"')"
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		var matDesc = Ext.getCmp('matDesc').getValue();
		if (!Ext.isEmpty(matDesc)) {
			whereList.push({propName:'matDesc', propValue:matDesc, compare:Condition.EQ});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
});