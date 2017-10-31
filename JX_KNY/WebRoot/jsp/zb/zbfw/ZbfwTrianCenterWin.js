//设置适用机车
Ext.onReady(function(){
Ext.namespace('ZbfwTrianCenterWin'); 
ZbfwTrianCenterWin.zbFwIdx = '';//整备范围idx
ZbfwTrianCenterWin.ZbfwTrianCenterIDX = '';//整备范围和车型中间表idxidx
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
ZbfwTrianCenterWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
ZbfwTrianCenterWin.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbfwTrainCenter!findZbfwTrainInfo.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/zbfwTrainCenter!delete.action',                 //删除操作
    hideRowNumberer: true,
    tbar: [{
        xtype:"label", text:"  车号： " 
    },{
        id:"trainNo",xtype: "textfield" ,maxLength: 50,vtype:"validChar"
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
			ZbfwTrianCenterWin.grid.store.load();
    	}
    },'-',{
    	text: "重置", iconCls: "saveIcon", handler: function(){
			Ext.getCmp("trainNo").setValue("");
    		ZbfwTrianCenterWin.grid.store.load();
    	}
   	},'-','delete','-',
   	{
   		text:"添加机车", iconCls:"addIcon",
    	 handler: function(){
		    ZbfwTrianCenterWin.ZbfwTrianCenterIDX = $yd.getSelectedIdx(ZbfwTrianCenterWin.grid);
		    TrianInfoWin.ZbfwTrianCenterIDX = ZbfwTrianCenterWin.ZbfwTrianCenterIDX.length == 0 ? '' : ZbfwTrianCenterWin.ZbfwTrianCenterIDX[0];
		    
		    //设置车型显示值为选中的行
    		TrianInfoWin.selectWin.show();
    		TrianInfoWin.grid.store.load();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName' ,searcher: { hidden: true }
	},{
		header:'车号', dataIndex:'trainNo', editor:{   }
	},{
		header:'车型idx', dataIndex:'trainTypeIDX',hidden:true, editor:{   }
	},{
		header:'整备范围idx', dataIndex:'zbfwIDX',hidden:true, editor:{   }
	},{
		header:'整备范围名称', dataIndex:'fwName', editor:{   }
	},{
		header:'是否本段',hidden:true, dataIndex:'isThisSite', editor:{   }
	}]
});

// 表格数据加载前的参数设置
ZbfwTrianCenterWin.grid.store.on('beforeload', function(){
	var trainNo = Ext.getCmp("trainNo").getValue();
	var entityJson = {};
	entityJson.trainNo = trainNo;
	entityJson.zbfwIDX = ZbfwTrianCenterWin.zbFwIdx;
	this.baseParams.entityJson = Ext.util.JSON.encode(entityJson);
});	

ZbfwTrianCenterWin.selectWin = new Ext.Window({
	title:"选择车号", maximizable:true, width:600, height:280, 
    plain:true, closeAction:"hide", modal:true, layout:"fit",
    items:ZbfwTrianCenterWin.grid
});
//移除监听
ZbfwTrianCenterWin.grid.un('rowdblclick', ZbfwTrianCenterWin.grid.toEditFn, ZbfwTrianCenterWin.grid);
//确认提交方法，后面可覆盖此方法完成查询
ZbfwTrianCenterWin.submit = function(){alert("请覆盖，ZbfwTrianCenterWin.submit 方法完成自己操作业务！");};
});