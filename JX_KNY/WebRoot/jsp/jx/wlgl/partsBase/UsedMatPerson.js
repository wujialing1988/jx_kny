/**
 * 常用物料清单使用人 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('UsedMatPerson');                       //定义命名空间
	
	/** ****************** 定义全局变量开始 ****************** */
	UsedMatPerson.usedMatIdx = "###";
	/** ****************** 定义全局变量结束 ****************** */
	
	/** ****************** 定义主体表格开始 ****************** */
	UsedMatPerson.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/usedMatPerson!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/usedMatPerson!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/usedMatPerson!logicDelete.action',            //删除数据的请求URL
	    tbar:['add', 'delete'],
	    storeAutoLoad:false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'清单主键', dataIndex:'usedMatIdx', hidden:true
		},{
			header:'人员ID', dataIndex:'empId', hidden: true
		},{
			header:'人员编码', dataIndex:'empCode'
		},{
			header:'人员姓名', dataIndex:'empName'
		}],
		addButtonFn: function() {
			PersonSelect.win.show();
		}
	});
	UsedMatPerson.grid.store.on('beforeload', function(){
		var searchParams = {usedMatIdx: UsedMatPerson.usedMatIdx};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	// 取消表格行“双击”进行编辑的事件监听
	UsedMatPerson.grid.un('rowdblclick', UsedMatPerson.grid.toEditFn, UsedMatPerson.grid);
	/** ****************** 定义主体表格结束 ****************** */
	
	UsedMatPerson.win = new Ext.Window({
		title:"适用人群",
		closeAction:"hide",
		width:650,
		height:400,
		layout:"fit",
		items: UsedMatPerson.grid
	})
	
	// 自定义人员选择控件【保存】按钮触发的函数处理
	PersonSelect.saveFn = function() {
		var records = Ext.getCmp('PersonSelect_Grid').getValues();
		if (null == records) {
			return;
		}
		var datas = [];
		for (var i = 0; i < records.length; i++) {
			var data = {};
			data.usedMatIdx = UsedMatPerson.usedMatIdx;
			data.empId = records[i].get('empid');
			data.empCode = records[i].get('empcode');
			data.empName = records[i].get('empname');
			datas.push(data);
		}
		PersonSelect.loadMask.show();
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/usedMatPerson!save.action',
            jsonData: datas,
            success: function(response, options){
              	PersonSelect.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    UsedMatPerson.grid.store.reload();
                    PersonSelect.win.hide();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
	}
	
});