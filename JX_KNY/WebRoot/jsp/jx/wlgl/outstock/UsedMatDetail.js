/**
 * 常用物料清单明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('UsedMatDetail');                       //定义命名空间
	
	/** ****************** 定义全局变量开始 ****************** */
	UsedMatDetail.labelWidth = 100;
	UsedMatDetail.fieldWidth = 240;
	UsedMatDetail.usedMatIdx = "###",
	UsedMatDetail.whIdx = "";
	/** ****************** 定义全局变量结束 ****************** */
	
	/** ****************** 定义清单明细表格结束 ****************** */
	UsedMatDetail.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/usedMatSelect!pageList.action',                 //装载列表数据的请求URL
	    storeAutoLoad: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'清单主键', dataIndex:'usedMatIdx',hidden:true, editor:{  maxLength:50 }
		},{
			header:'物料编码', dataIndex:'matCode'
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200
		},{
			header:'库存数量', dataIndex:'qty', width: 60
		},{
			header:'计量单位', dataIndex:'unit', width: 60			
		},{
			header:'计划单价', dataIndex:'price', width: 60	
		}],
		tbar:null
	});
	// 默认已“物料编码”进行升序排序
	UsedMatDetail.grid.store.setDefaultSort('matCode','ASC');
	// 取消双击进行行编辑的函数监听
	UsedMatDetail.grid.un("rowdblclick", UsedMatDetail.grid.toEditFn, UsedMatDetail.grid);
	UsedMatDetail.grid.store.on("beforeload", function(){
		this.baseParams.entityJson = Ext.util.JSON.encode({
			usedMatIdx: UsedMatDetail.usedMatIdx,
			whIdx: UsedMatDetail.whIdx
		});
	})
	/** ****************** 定义清单明细表格开始 ****************** */
	
});