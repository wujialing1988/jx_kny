/**
 * 机车检修项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('MatTypeUseList');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	MatTypeUseList.gztpIdx = "";
	MatTypeUseList.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:i18n.FaultRegQuery.Msg});
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 删除（批量删除）函数
	MatTypeUseList.deleteFn = function() {
        var data = MatTypeUseList.grid.selModel.getSelections();
        if(data.length == 0 ){
        	MyExt.Msg.alert(i18n.FaultRegQuery.Msg1);
            return;
        }		
		MatTypeUseList.grid.store.remove(data[0]);
	}
	
	/** **************** 定义全局函数结束 **************** */
	// 子节点表单数据容器
	MatTypeUseList.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:true, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/matTypeUse!pageQuery.action',
		fields:['idx','gztpIdx','matCode','matDesc','unit','price','matCount']
	});
	
	// 操作掩码
    MatTypeUseList.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:i18n.FaultRegQuery.Msg});
	// 行选择模式
    MatTypeUseList.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    //材料规格型号，分页工具
	MatTypeUseList.pagingToolbar = Ext.yunda.createPagingToolbar({store: MatTypeUseList.store});
	MatTypeUseList.grid = new Ext.grid.EditorGridPanel({  
	  	border: true, enableColumnMove: true, stripeRows: true, selModel: MatTypeUseList.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: MatTypeUseList.store,
		colModel:new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),{
			sortable:false, dataIndex:'idx', hidden:true, header:'idx'
		},{
			header:i18n.FaultRegQuery.materialCode, dataIndex:'matCode', width: 200, editor: { readOnly: true }
		},{
			header:i18n.FaultRegQuery.materialDescription, dataIndex:'matDesc', width: 200, editor: { readOnly: true }
		},{
			header:i18n.FaultRegQuery.count,dataIndex:'matCount', width: 100
		},{
			header:i18n.FaultRegQuery.unit, dataIndex:'unit', width: 100, editor: { readOnly: true }
		},{
			header:i18n.FaultRegQuery.price, dataIndex:'price', width: 100,hidden:true
		},{
			header:i18n.FaultRegQuery.operate, dataIndex:'idx',hidden:true, align:'center', width: 60, renderer: function(value, metaData, record, rowIndex, colIndex, store){
				return "<img src='" + deleteIcon + "' alt=i18n.FaultRegQuery.altDelete style='cursor:pointer' onclick='MatTypeUseList.deleteFn();'/>";
			}
		},{
			header:i18n.FaultRegQuery.faultRegisterID, dataIndex:'gztpIdx', hidden:true, editor: { readOnly: true }
		}]),
		title:i18n.FaultRegQuery.materialConsume,
		listeners: {
			beforeedit:function(e){
				return false ;
			}
		},	    
	    toobal:MatTypeUseList.pagingToolbar,
	    afterDeleteFn:function(){
	    }
	});
	
	// 加载前参数设置
	MatTypeUseList.grid.store.on('beforeload', function(){
		var whereList = [];
		whereList.push({ propName: 'gztpIdx', propValue:MatTypeUseList.gztpIdx , compare: Condition.EQ, stringLike: false });
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
});