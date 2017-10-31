/**
 * JT6提票 返修记录 by wujl 2016-08-08
 */
 Ext.onReady(function(){
   Ext.namespace('ZbglTpRepair');  
 
   ZbglTpRepair.jt6IDX = null;
	 /*** 提票返修 start ***/
  ZbglTpRepair.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTpRepair!pageQuery.action',                 //装载列表数据的请求URL
	viewConfig: null,
	storeAutoLoad: false,
	tbar:['refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'第几次返修', dataIndex:'repairTimes', editor:{  maxLength:50 }
	},{
		header:'故障位置', dataIndex:'faultFixFullName', editor:{  maxLength:50 }
	},{
		header:'故障原因', dataIndex:'faultReason', editor:{  maxLength:50 }
	},{
		header:'施修方法', dataIndex:'methodDesc', editor:{  maxLength:50 }
	},{
		header:'专业类型', dataIndex:'professionalTypeName', editor:{  maxLength:50 }
	},{
		header:'处理结果描述', dataIndex:'repairDesc', editor:{  maxLength:50 }
	},{
		header:'检查人', dataIndex:'checkPerson', editor:{  maxLength:50 }
	},{
		header:'检查时间', dataIndex:'checkTime', editor:{  maxLength:50 }
	},{
		header:'返修人员', dataIndex:'repairPersonName', hidden:true ,editor:{  maxLength:50 }
	},{
		header:'返修原因', dataIndex:'repairReason',width: 200, editor:{  maxLength:50 }
	},{
		header:'返修内容', dataIndex:'repairContent',width: 200, hidden:true, editor:{  maxLength:50 }
	},{
		header:'返修状态', dataIndex:'status', editor:{  maxLength:20 },
	    renderer: function(v){
	        switch(v){
	        	case 0:
	                return "正在返修";
	            case 1:
	                return "返修完成";
	            default:
	                return "";
	        }
	    }
	}],
	toEditFn: function(grid, rowIndex, e) {},
	refreshButtonFn: function(){                        //点击刷新按钮触发的函数
        ZbglTpRepair.grid.store.load();
    }
});
/*** 提票返修列表 end ***/
ZbglTpRepair.grid.store.on("beforeload", function(){	
	var whereList = [] ;
	whereList.push({propName:'jt6IDX', propValue: ZbglTpRepair.jt6IDX ,compare : Condition.EQ}) ;
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});


/** **************** 提票返修列表编辑窗口 start **************** */
ZbglTpRepair.win = new Ext.Window({
	title:"提票返修列表", maximized:true,
	layout:"fit", closeAction:"hide",
	items:[
	{
		xtype:"panel",
		layout:"fit",
		items:[
			ZbglTpRepair.grid
		]
	}
	],
	buttonAlign:'center',
	buttons:[{
		text:'关闭', iconCls:'closeIcon', handler:function(){
			this.findParentByType('window').hide();
		}
	}], 
	listeners : {
		show: function(window) {
			ZbglTpRepair.grid.store.load();
		},
		hide: function() {
		}
	}
});
/** **************** 提票返修列表编辑窗口 end **************** */

});