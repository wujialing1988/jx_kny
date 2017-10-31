/**
 * 作业工单选择（可根据组成的过滤不同组成类型的作业工单）
 */
Ext.onReady(function(){
Ext.namespace('WorkSeqSelect');                       //定义命名空间
WorkSeqSelect.searchParams = {};						//全局查询参数集
	
	WorkSeqSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workSeq!pageQuery.action',                 //装载列表数据的请求URL
	    tbar:[
	    	{
			xtype:"combo", id:"queryWorkSeq_Id", hiddenName:"queryType", displayField:"type",
	        width: 100, valueField:"type", value:"编码", mode:"local",triggerAction: "all",
			store: new Ext.data.SimpleStore({
				fields: ["type"],
				data: [ ["编码"], ["名称"] ]
			})
		},{	            
	        xtype:"textfield",  id:"workSeqName_Id", width: 130
		},{
			text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
			handler : function(){
				var typeName = Ext.getCmp("workSeqName_Id").getValue();
				var querytype = Ext.getCmp("queryWorkSeq_Id").getValue();
				if(querytype == '编码'){
					delete WorkSeqSelect.searchParams.workSeqName;
					WorkSeqSelect.searchParams.workSeqCode = typeName;
				}else if(querytype == '名称'){
					delete WorkSeqSelect.searchParams.workSeqCode;
					WorkSeqSelect.searchParams.workSeqName = typeName;
				}
				WorkSeqSelect.grid.getStore().load();
			}
		},{
			text : "重置",iconCls : "resetIcon",
			handler : function(){
				//清空搜索输入框
				Ext.getCmp("workSeqName_Id").setValue("");
				Ext.getCmp("queryWorkSeq_Id").setValue("编码");
				//清空机车组成查询集合
				WorkSeqSelect.searchParams = {};
				WorkSeqSelect.grid.getStore().load();
			}
		},{
			text : "确定",iconCls : "saveIcon", handler: function(){
				WorkSeqSelect.submit(); 
			}
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'编码', dataIndex:'workSeqCode',width:50,  editor:{ }
		},{
			header:'名称', dataIndex:'workSeqName', editor:{  }
		},{
			header:'额定工时(分)', dataIndex:'ratedWorkHours',width:45, editor:{  }
		}]
	});


//移除事件
WorkSeqSelect.grid.un('rowdblclick',WorkSeqSelect.grid.toEditFn,WorkSeqSelect.grid);
//store载入前查询，为了分页查询时不至于出差错
WorkSeqSelect.grid.store.on("beforeload", function(){
	var whereLis;
	//	调用的JSp页面需要加入后面的代码 var WORKSEQ_STATUS_USE = <%=com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq.STATUS_USE%>
	var sqlStr = " idx not in (select nvl(t.work_seq_idx,'-11111') from JXGC_RP_To_WS t " +
				 " where t.record_status=0 and t.repair_project_idx='"+RepairProjectEdit.idx+"')" ;
	WorkSeqSelect.searchParams = MyJson.deleteBlankProp(WorkSeqSelect.searchParams);
	whereList = [
        {propName: "pTrainTypeIDX", propValue: RepairProjectEdit.pTrainTypeIdx, compare: Condition.EQ, stringLike: false}, //车型id
		{sql: sqlStr, compare: Condition.SQL} //过滤检修项目已经对应的的作业工单
		] ;
	for(prop in WorkSeqSelect.searchParams){
		whereList.push({propName: prop, propValue: WorkSeqSelect.searchParams[prop]});
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
//定义点击确定按钮的操作
WorkSeqSelect.submit = function(){
	alert("请覆盖方法（WorkSeqSelect.submit）！");
}
//定义选择窗口
WorkSeqSelect.selectWin = new Ext.Window({
	title:"选择检修工单",maximizable:true, width:550, height:360, closeAction:"hide", modal:true, layout:"fit", items:[ WorkSeqSelect.grid ]
});

});