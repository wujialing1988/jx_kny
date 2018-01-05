/**
 * 常用部门字典项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('OrgDicItem');                       //定义命名空间
OrgDicItem.searchParam = {};
OrgDicItem.dictTypeId = "" ;//字典类型编码
OrgDicItem.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/orgDicItem!pageListByTypeId.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/orgDicItem!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/orgDicItem!delete.action',            //删除数据的请求URL
    storeAutoLoad: false,
    storeId: "id.orgId",
    tbar: [{
	    	text: i18n.OrgDicItem.add,
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		TeamSelect.selectWin.show();
	    		TeamSelect.grid.store.load();
	    	}
	    },'delete','refresh'],
	fields: [{
		header:i18n.OrgDicItem.dicCode, dataIndex:'id.dictTypeId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:i18n.OrgDicItem.departmentID, dataIndex:'id.orgId', hidden:true, editor: { xtype:'hidden' }
	},{
		header:i18n.OrgDicItem.departmentSeq, dataIndex:'orgSeq', editor:{  maxLength:200 }
	},{
		header:i18n.OrgDicItem.departmentName, dataIndex:'orgName', editor:{  maxLength:100 }
	}],
	deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
        if(this.saveWin)    this.saveWin.hide();
        if(this.searchWin)  this.searchWin.hide();        
        //未选择记录，直接返回
        if(!$yd.isSelectedRecord(this)) return;
        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
        if(!this.beforeDeleteFn()) return;
        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
            scope: this, url: this.deleteURL, params: {ids: $yd.getSelectedIdx(this, this.storeId),dictTypeId : OrgDicItem.dictTypeId}
        });
    }
});
//移除事件
OrgDicItem.grid.un('rowdblclick',OrgDicItem.grid.toEditFn,OrgDicItem.grid);
OrgDicItem.grid.store.on('beforeload',function(){
	     var searchParam = OrgDicItem.searchParam;
	     searchParam = MyJson.deleteBlankProp(searchParam);
	     this.baseParams.dictTypeId = OrgDicItem.dictTypeId;
	     this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
});