/**
 * 选择检修需求单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WP');                       //定义命名空间
// 对工时字段（分钟）按#天#小时#分钟进行单位显示
WP.formatTime = function(ratedPeriod) {
	if (Ext.isEmpty(ratedPeriod) || ratedPeriod == 0) {
		return "";
	}
	var ratedPeriod_d = Math.floor(ratedPeriod/(24*60));			// 天
	var ratedPeriod_h = Math.floor((ratedPeriod%(24*60))/60);		// 时
	var ratedPeriod_m = ratedPeriod%60;								// 分
	var displayValue = "";
	if (ratedPeriod_d != 0) {
		displayValue += ratedPeriod_d + "天";
		if (ratedPeriod_h != 0) {
			displayValue += ratedPeriod_h + "小时";
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分";
			}
		} else {
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分";
			}
		}
	} else {
		if (ratedPeriod_h != 0) {
			displayValue += ratedPeriod_h + "小时";
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分";
			}
		} else {
			if (ratedPeriod_m != 0) {
				displayValue += ratedPeriod_m + "分";
			}
		}
	}
	return displayValue;
};
WP.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wP!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/wP!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wP!logicDelete.action',            //删除数据的请求URL
	    singleSelect : true,storeAutoLoad: false,
	    tbar:null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, width: 15, editor: { xtype:'hidden' }
		},{
			header:'编号', dataIndex:'wPNo', width: 10
		},{
			header:'描述', dataIndex:'wPDesc', width: 30
		},{
			header:'额定工期', dataIndex:'ratedPeriod', width: 10, renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				return WP.formatTime(value);
			}
		},{
			header:'额定工时', dataIndex:'ratedWorkTime', width: 10
		},{
			header:'备注', dataIndex:'remarks', width: 20
		}]
});
WP.grid.store.on('beforeload',function(){
	     //查询其适用配件与所选规格型号匹配的检修需求单
	     var sqlStr = " idx in (select WP_IDX from PJJX_WP_Union_Parts where Record_Status=0 and Parts_Type_IDX = '"+PartsRdp.partsTypeIdx+"')";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} //通过回段日期过滤
					]
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
//默认选中第一条
WP.grid.store.on("load", function(){
		WP.grid.selModel.selectRow(0);
	});
// 取消表格双击进行编辑的事件监听
WP.grid.un('rowdblclick', WP.grid.toEditFn, WP.grid);
});