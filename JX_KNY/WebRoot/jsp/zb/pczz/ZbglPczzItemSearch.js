/**
 * 普查整治项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglPczzItemSearch');     //定义命名空间

   /* ************* 定义全局变量开始 ************* */
  ZbglPczzItemSearch.searchParams = {};
  ZbglPczzItemSearch.zbglPczzItemIDX = "";
   /* ************* 定义全局变量结束 ************* */

  /**
   * 点击查询操作的方法
   */
ZbglPczzItemSearch.zbglPczzWIDetails = function(rowIndex){
  	var record = ZbglPczzItemSearch.grid.store.getAt(rowIndex);
	ZbglPczzWI.detailWin.show();
	ZbglPczzWI.form.getForm().reset();
	ZbglPczzWI.form.getForm().loadRecord(record);
	ZbglPczzItemSearch.zbglPczzItemIDX = record.get("idx");
	ZbglPczzWI.grid.store.load();
  }

ZbglPczzItemSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglPczzItem!pageList.action',                 //装载列表数据的请求URL
    saveFormColNum:1,
    tbar:[],
	fields: [{
		header:'执行情况', editor: { xtype:'textfield' },
		renderer:function(value, metaData, record, rowIndex, colIndex, store){			
			return "<img src='" + imgpathx + "' alt='执行情况' style='cursor:pointer' onclick='ZbglPczzItemSearch.zbglPczzWIDetails(\"" + rowIndex + "\")'/>";
		}, sortable:false
	},{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'普查整治单主键', dataIndex:'zbglPczzIDX',hidden:true,editor:{  xtype:'hidden'}
	},{
		header:'项目名称', dataIndex:'itemName', editor:{  maxLength:50 }
	},{
		header:'项目内容', dataIndex:'itemContent', editor:{  maxLength:300 },width: 350
	},{
		header:'车型', dataIndex:'trainTypeShortNameList', editor:{ xtype:'hidden'}
	},{
		header:'车型代码', dataIndex:'trainTypeList', hidden:true,editor:{  xtype:'hidden'}
	}]
 });
 // 表格数据加载前的参数设置
ZbglPczzItemSearch.grid.store.on('beforeload', function(){
		ZbglPczzItemSearch.searchParams.zbglPczzIDX = ZbglPczzSearch.zbglPczzIdx;
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczzItemSearch.searchParams);
   });
   
});