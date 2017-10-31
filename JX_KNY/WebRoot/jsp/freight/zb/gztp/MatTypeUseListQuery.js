/**
 * 机车检修项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('MatTypeUseList');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	MatTypeUseList.gztpIdx = "";
	MatTypeUseList.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 删除（批量删除）函数
	MatTypeUseList.deleteFn = function() {
        var data = MatTypeUseList.grid.selModel.getSelections();
        if(data.length == 0 ){
        	MyExt.Msg.alert("尚未选择一条记录！");
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
    MatTypeUseList.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
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
			header:'物料编码', dataIndex:'matCode', width: 200, editor: { readOnly: true }
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200, editor: { readOnly: true }
		},{
			header:'数量',dataIndex:'matCount', width: 100
		},{
			header:'单位', dataIndex:'unit', width: 100, editor: { readOnly: true }
		},{
			header:'单价', dataIndex:'price', width: 100,hidden:true
		},{
			header:'操作', dataIndex:'idx',hidden:true, align:'center', width: 60, renderer: function(value, metaData, record, rowIndex, colIndex, store){
				return "<img src='" + deleteIcon + "' alt='删除' style='cursor:pointer' onclick='MatTypeUseList.deleteFn();'/>";
			}
		},{
			header:'故障登记ID', dataIndex:'gztpIdx', hidden:true, editor: { readOnly: true }
		}]),
		title:'物料消耗',
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