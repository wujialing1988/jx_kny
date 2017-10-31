

Ext.onReady(function() {
	Ext.namespace('WPMat');
	
	/** ************* 定义全局变量开始 ************* */
	WPMat.searchParam = {};
	WPMat.wPIDX = "###";										// 作业流程主键
	WPMat.wPNodeIDX = "###";									// 作业流程节点主键
	WPMat.wPNodeName = "###";									// 作业流程节点名称
	WPMat.parentWPNodeIDX = "###";                              // 上级作业节点
	WPMat.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 删除（批量删除）函数
	WPMat.deleteFn = function(idx) {
		var wPMatIdxs = null;
		if (idx instanceof Array) {
			wPMatIdxs = idx;
		} else {
			wPMatIdxs = [idx];
		}
		
		 //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
            scope: WPMat.grid, url: ctx + '/wPMat!delete.action', params: {
				ids: wPMatIdxs
			}
			
        });
	}
	
	WPMat.addFn = function(){
		var records = WPMat.grid.getStore().getModifiedRecords();
		var datas = [];

		for(var a = 0;a<records.length;a++){
			var obj = {};
			
			obj.idx = records[a].data.idx;
			obj.wpIDX = records[a].json.wpIDX;
			obj.wpNodeIDX = records[a].json.wpNodeIDX;
			obj.matCode = records[a].data.matCode;
			obj.matDesc = records[a].data.matDesc;
			obj.wpNodeName = records[a].json.wpNodeName;
			obj.parentWPNodeIDX = records[a].json.parentWPNodeIDX;
			obj.qty = records[a].data.qty;
			obj.unit = records[a].data.unit;
		
			datas.push(obj);
		}
		
        Ext.Ajax.request({
        	url: ctx + '/wPMat!matUpdate.action',
        	jsonData: datas,
        	success: function(response,options){
        		WPMat.loadMask.hide();
        		var result = Ext.util.JSON.decode(response.responseText);
        		if (result.errMsg == null) {
                    alertSuccess();
					// 重新加载表格数据
					WPMat.grid.store.reload();
					//重新加载查询页面
					WPMatSearch.grid.getStore().reload();
                } else {
                    alertFail(result.errMsg);
                }
        	},
        	failure: function(response,options){
        		WPMat.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        	}
        });
	}
	
	//覆盖MatInforList.submit()方法，添加配件检修用料信息
	MatInforList.submit = function(){
		var sm = MatInforList.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert("尚未选择一条记录");
			return;
		}
		// 已选择的待添加的物料信息
		var selectedRecrods = sm.getSelections();
		// 检验已选择的待添加的物料信息是否已经被添加
		var count = WPMat.grid.store.getCount();
		if (count != 0) {
			for (var i = 0; i < count; i++) {
				var record = WPMat.grid.store.getAt(i);
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
			data.wpNodeIDX = WPMat.wPNodeIDX		// 作业节点idx主键
			data.wpIDX = WPMat.wPIDX;		// 作业流程主键
			data.parentWPNodeIDX = WPMat.parentWPNodeIDX;		// 上级作业节点主键
			data.wpNodeName = WPMat.wPNodeName;
			data.matCode = selectedRecrods[k].get('matCode');		// 物料编码
			data.matDesc = selectedRecrods[k].get('matDesc');		// 物料描述
			data.unit = selectedRecrods[k].get('unit');				// 计量单位
			data.qty = 1;											// 数量默认为”1“
			
			datas.push(data);
		}
		WPMat.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/wPMat!saveMatInfor.action',
            jsonData: datas,
            success: function(response, options){
              	WPMat.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
					// 添加成功后，隐藏【物料选择】 窗口
					MatInforList.batchWin.hide();
					// 重新加载 【物料选择】 窗口表格数据
					MatInforList.grid.store.reload();
					// 重新加载表格数据
					WPMat.grid.store.reload();
					//重新加载查询页面
					WPMatSearch.grid.getStore().reload();					
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                WPMat.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	
	
	/** ************* 定义全局函数结束 ************* */
	// 子节点表单数据容器
	WPMat.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:true, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/wPMat!pageQuery.action',
		fields:['idx','nodeName','qty','parentNodeIDX','matCode','matDesc','unit','price']
	});
	// 行选择模式
    WPMat.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    
	WPMat.grid = new Ext.grid.EditorGridPanel({ 
	  	border: true, enableColumnMove: true, stripeRows: true, selModel: WPMat.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: WPMat.store,
		colModel:new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: {hidden: true}
		},{
			header:'作业节点idx主键', dataIndex:'wpNodeIDX', hidden:true, editor: {hidden: true}
		},{
			header:'作业流程主键', dataIndex:'wpIDX', hidden:true, editor: {hidden: true}
		},{
			header:'上级作业节点主键', dataIndex:'parentWPNodeIDX', hidden:true, editor: {hidden: true}
		},{
			header:'节点名称', dataIndex:'wpNodeName', hidden:true, editor: {hidden: true}
		},{
			header:'物料编码', dataIndex:'matCode', editor:{readOnly: true}
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{readOnly: true}, width: 240
		},{
			header:'数量', dataIndex:'qty', editor:{ maxLength:4, allowBlank:false, vtype:'positiveInt' }, searcher: {hidden: true}
		},{
			header:'计量单位', dataIndex:'unit', editor:{readOnly: true}, searcher: {hidden: true}
		},{
			header:'操作', dataIndex:'idx', align:'center',  width: 60, renderer: function(value){
				return "<img src='" + deleteIcon + "' alt='删除' style='cursor:pointer' onclick='WPMat.deleteFn(\""+ value +"\")'/>";
			}
		}]),
		tbar:[{
	    	text:'选择物料', iconCls: 'addIcon', handler:function(){
	    		MatInforList.batchWin.show();
	    	}
	    }, {
	    	text:"保存", iconCls:'saveIcon',handler:WPMat.addFn
	    }],
		
		afterShowSaveWin: function() {
	    	WPMat.grid.store.load();
	    },
	   
	    afterDeleteFn:function(){
	    }
	    
	});
	
	
	//查询前添加过滤条件
	
	WPMat.grid.store.on('beforeload' , function(){
		var whereList = [];
	//	var sql = "WP_IDX = '" + WPMat.wPIDX + "' AND WP_NODE_IDX = '" + WPMat.wPNodeIDX +"'";
	//	whereList.push({sql: sql, compare:Condition.SQL});
		//for (var prop in WPMat.searchParam) {
			//  whereList.push({propName:prop, compare:Condition.LIKE, propValue:WPMat.searchParam[ prop ]});
			  whereList.push({propName:"wpIDX", compare:Condition.EQ, propValue:WPMat.wPIDX});
			  whereList.push({propName:"wpNodeIDX", compare:Condition.EQ, propValue:WPMat.wPNodeIDX});
	//	}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
});