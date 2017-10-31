/**
 * 兑现单选择
 */
Ext.onReady(function(){
Ext.namespace('TrainEnforcePlanRdp');                       //定义命名空间
TrainEnforcePlanRdp.searchParams = {};						//全局查询参数集
//兑现单列表
TrainEnforcePlanRdp.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanRdp!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    searchFormColNum: 2,
    storeAutoLoad : false,
    tbar: ['search',{
    	text:'确定',iconCls:'saveIcon',handler:function(){
    		TrainEnforcePlanRdp.submit();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{ 
		header:'生产计划主键', dataIndex:'trainEnforcePlanIDX',hidden:true, editor:{  maxLength:50 }
	},{ 
//		header:'工作号', dataIndex:'workNumber', editor:{  maxLength:50 }
//	},{ 
		header:'配属局', dataIndex:'bshortName',editor:{ maxLength:50 }
	},{ 
		header:'配属段', dataIndex:'dshortName',editor:{ maxLength:50 }
	},{ 
		header:'委修单位', dataIndex:'delegateDShortName',editor:{ maxLength:50 }
	},{ 
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{ }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ }
	},{
		header:'车号', dataIndex:'trainNo',editor:{	}
	},{
		header:'承修部门', dataIndex:'undertakeDepName', editor:{ xtype: 'hidden' }
	},{
		header:'修程', dataIndex:'repairClassName', editor:{ }, searcher:{}
	},{
		header:'修次', dataIndex:'repairtimeName', editor:{  },	searcher:{}
	}],
	searchFn: function(searchParam){ 
		TrainEnforcePlanRdp.searchParams = searchParam ;
        TrainEnforcePlanRdp.grid.store.load();
        alert();
	}
});


TrainEnforcePlanRdp.selectWin = new Ext.Window({
	title:"选择生产计划", maximizable:true,width:700, height:325, closeAction:"hide", modal:true, layout:"fit", items:TrainEnforcePlanRdp.grid
});
//移除监听
TrainEnforcePlanRdp.grid.un('rowdblclick', TrainEnforcePlanRdp.grid.toEditFn, TrainEnforcePlanRdp.grid);
//确认提交方法，后面可覆盖此方法完成查询
TrainEnforcePlanRdp.submit = function(){alert("请覆盖，TrainEnforcePlanRdp.submit 方法完成自己操作业务！");};

});