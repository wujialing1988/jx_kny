/**
 * 记录单报表模板管理 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ReportTmplManage');                       //定义命名空间
ReportTmplManage.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/reportTmplManage!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/reportTmplManage!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/reportTmplManage!logicDelete.action',            //删除数据的请求URL
//    saveFormColNum:,	searchFormColNum:,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'记录单主键', dataIndex:'qrIDX', editor:{  maxLength:500 }
	},{
		header:'记录单报表模板编码', dataIndex:'qrReportTmplCode', editor:{  maxLength:50 }
	},{
		header:'记录单报表模板名称', dataIndex:'qrReportTmplName', editor:{  maxLength:100 }
	},{
		header:'记录单模板文件报表路径', dataIndex:'qrReportTmplReportPath', editor:{  maxLength:500 }
	},{
		header:'记录单模板文件保存名称', dataIndex:'qrReportTmplUploadName', editor:{  maxLength:100 }
	},{
		header:'记录单模板文件真实名称', dataIndex:'qrReportTmplRealName', editor:{  maxLength:100 }
	},{
		header:'记录单模板文件上传路径', dataIndex:'qrReportTmplUploadPath', editor:{  maxLength:500 }
	},{
		header:'记录单模板文件大小', dataIndex:'qrSize', editor:{ xtype:'numberfield', maxLength:12 }
	},{
		header:'记录单模板文件类型', dataIndex:'fileType', editor:{  maxLength:100 }
	},{
		header:'是否最新版本', dataIndex:'isCurrentVersion', editor:{  maxLength:1 }
	},{
		header:'版本号', dataIndex:'versionNo', editor:{  maxLength:50 }
	},{
		header:'上传人', dataIndex:'uploadPerson', editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'上传人名称', dataIndex:'uploadPersonName', editor:{  maxLength:25 }
	},{
		header:'上传时间', dataIndex:'uploadTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'是否已发布报表', dataIndex:'isPublish', editor:{  maxLength:10 }
	},{
		header:'报表文件对象', dataIndex:'reportFile', editor:{   }
	},{
		header:'模板类型', dataIndex:'type', editor:{  maxLength:10 }
	}]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ReportTmplManage.grid });
});