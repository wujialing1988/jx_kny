/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('WSGroupItem');
	
	/** **************** 定义全局变量开始 **************** */
	WSGroupItem.labelWidth = 100;
	WSGroupItem.fieldWidth = 140;
	WSGroupItem.wsGroupIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 手动排序 
    WSGroupItem.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/wSGroupItem!moveOrder.action',
			params: {idx: idx, orderType: orderType},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    }
    
    // 删除（批量删除）函数
	WSGroupItem.deleteFn = function(idx) {
		var wSGroupItemIdxs = null;
		if (idx instanceof Array) {
			wSGroupItemIdxs = idx;
		} else {
			wSGroupItemIdxs = [idx];
		}
		
		 //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
            scope: WSGroupItem.grid, 
            url: ctx + '/wSGroupItem!delete.action',
			params: {
				ids: wSGroupItemIdxs
			}
        });
	}
	/** **************** 定义全局函数结束 **************** */
	
	WSGroupItem.grid = new Ext.yunda.Grid({
		title:'工位组明细',
	    loadURL: ctx + '/wSGroupItem!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/wSGroupItem!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wSGroupItem!delete.action',            //删除数据的请求URL
	    tbar:[{
	    	text:'选择作业工位', iconCls: 'addIcon', handler: function(){
	        	WorkStationSearcher.wsGroupIDX = WSGroupItem.wsGroupIDX;
	        	// 获取已选择的工位主键，用于过滤工位树
	        	var store = WSGroupItem.grid.store;
	        	var selectedIds = [];
	        	store.each(function(record){
	        		this.push(record.get('wsIDX'));
	        	}, selectedIds);
	        	WorkStationSearcher.selectedIds = selectedIds;
	    		WorkStationSearcher.win.show()
	    	}
	    }, 'delete', '->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				WSGroupItem.moveOrder(WSGroupItem.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				WSGroupItem.moveOrder(WSGroupItem.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				WSGroupItem.moveOrder(WSGroupItem.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				WSGroupItem.moveOrder(WSGroupItem.grid, ORDER_TYPE_BOT);
			}
		}],
	    storeAutoLoad: false,
		fields: [{
			header:'工位主键', dataIndex:'wsIDX', hidden:true
		},{
			header:'工位组主键', dataIndex:'wsGroupIDX', hidden:true
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:false
		},{
			header:'工位编码', dataIndex:'workStationCode'
		},{
			header:'工位名称', dataIndex:'workStationName'
		},{
			header:'流水线主键', dataIndex:'repairLineIdx', hidden:true
		},{
			header:'流水线名称', dataIndex:'repairLineName'
		},{
			header:'操作', dataIndex:'idx', align:'center', width: 60, renderer: function(value){
				return "<img src='" + deleteIcon + "' alt='删除' style='cursor:pointer' onclick='WSGroupItem.deleteFn(\""+ value +"\")'/>";
			}
		}],
		deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        WSGroupItem.deleteFn($yd.getSelectedIdx(this));
	    }
	})
	
	// 取消表格行双击事件
	WSGroupItem.grid.un('rowdblclick', WSGroupItem.grid.toEditFn, WSGroupItem.grid);
	
	WSGroupItem.grid.store.on('beforeload', function(){
		var searchParams = {};
		searchParams.wsGroupIDX = WSGroupItem.wsGroupIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
});