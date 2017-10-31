/**
 * 
 * 借出归还
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatMoveOutBack');
	/** ************ 定义全局变量开始 ************ */
	MatMoveOutBack.fieldWidth = 60;
	MatMoveOutBack.matTypeItems = [];
	/** ************ 定义全局变量线束 ************ */
	
	/** ************ 定义全局函数开始 ************ */
	
	//退回入库
	MatMoveOutBack.backInWh = function(){        	    
		var row = MatMoveOutBack.grid.getSelectionModel();
		if (row.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择一条记录！');
			return;
		}
		if (row.getCount() > 1) {
			MyExt.Msg.alert('请只选择一条记录');
			return;
		}
		var record = row.getSelections()[0];
		var id = record.json;
		Ext.Msg.confirm("提示  ", "是否确认退回原库入库？  ", function(btn){
		        if(btn == 'yes') {
			        Ext.Ajax.request({
			            url: ctx + '/matOutWH!saveMoveBackInMat.action',
			            jsonData: Ext.util.JSON.encode(record.json),
			            success: function(response, options){
			                if(self.loadMask)   self.loadMask.hide();
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {	             // 操作成功
								alertSuccess("入库成功");
								MatMoveOutBack.grid.store.load();
			                } else {
			                    alertFail(result.errMsg);
			                }
			            }
			        });
		        }
			});		
        }
	
	//退回
	MatMoveOutBack.back = function(){
		var row = MatMoveOutBack.grid.getSelectionModel();
		if (row.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择一条记录！');
			return;
		}
		if (row.getCount() > 1) {
			MyExt.Msg.alert('请只选择一条记录');
			return;
		}
		var record = row.getSelections();	
		ids=[];
		for(i=0; i<row.getCount();i++){
			ids.push(record[i].get('moveIdx'));
		}
		Ext.Msg.confirm("提示  ", "是否确认退回？  ", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request({
		            url: ctx + '/matMoveWH!back.action',
		            params:{ids:ids},
		            success: function(response, options){
		                if(self.loadMask)   self.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {	             // 操作成功
							alertSuccess("退回成功");
							MatMoveOutBack.grid.store.load();
		                } else {
		                    alertFail(result.errMsg);
		                }
		            }
		        });
	        }
		});		
	}
	
	/** ************ 定义全局函数结束 ************ */
	
	MatMoveOutBack.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matOutWH!findBackMoveInList.action',         //装载列表数据的请求URL
		tbar:[ {
		    	text : '退回入库',iconCls : 'addIcon', handler : function() {
		    		MatMoveOutBack.backInWh();
		     }},'-',  {
		    	text : '退回', iconCls : 'addIcon', handler : function() {
		    		MatMoveOutBack.back();
		    }}, '->','refresh' ],
		fields:[{
		   dataIndex: 'moveIdx', hidden: true
		},{
		   header: '接收库房', dataIndex: 'getWhName',width: MatMoveOutBack.fieldWidth
		},{
		   header: '接收库房', dataIndex: 'getWhIDX', hidden: true
		},{	
		   header: '物料类型', dataIndex: 'matType',width: MatMoveOutBack.fieldWidth
		},{
		   header: '物料编码', dataIndex: 'matCode',width: MatMoveOutBack.fieldWidth 
		},{
		   header: '物料描述', dataIndex: 'matDesc',width: MatMoveOutBack.fieldWidth
		},{
		   header: '计量单位', dataIndex: 'unit',width: MatMoveOutBack.fieldWidth-20
		},{
		   header: '出库数量', dataIndex: 'qty',width: MatMoveOutBack.fieldWidth-20
		},{
		   header: '出库库房', dataIndex: 'whName',width: MatMoveOutBack.fieldWidth-20
		},{
	  	   header: '出库库房id', dataIndex: 'whIDX', hidden: true
		},{
		   header: '出库库位', dataIndex: 'locationName',width: MatMoveOutBack.fieldWidth	  
		},{	
		   header: '出库时间', dataIndex: 'whDate',width: MatMoveOutBack.fieldWidth
		},{
		   header: '出库人', dataIndex: 'exWhEmp',width: MatMoveOutBack.fieldWidth
		},{
		   header: '状态', dataIndex: 'moveStatus',width: MatMoveOutBack.fieldWidth
	    }],
	    toEditFn: Ext.emptyFn //覆盖双击编辑事件
	});

	
	/** ************布局 ************ */
	MatMoveOutBack.viewport = new Ext.Viewport({
		layout: 'fit',
		items:[{
			region: 'center',
			layout: 'fit',
			items: [MatMoveOutBack.grid]
		}]
	});
	
	
	
});