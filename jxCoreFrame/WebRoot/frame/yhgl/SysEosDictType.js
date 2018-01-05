/** 业务字典分类页面 */
Ext.onReady(function(){
	Ext.namespace('EosDictType');                       //业务字典命名空间
	
    EosDictType.searchParams = {};
	EosDictType.selNode = null; //记录当前选中的节点
    
	//业务字典分类树
	EosDictType.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/sysEosDictType!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : i18n.SysEosDicType.dicType,
			disabled : false,
			id : 'ROOT_0',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true, //根节点是否可见
    	autoScroll : true,  //数据超过可视范围时是否自动出现滚动条
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
    	listeners: {
    		render : function() {
    			EosDictType.selNode = null;
    			EosDictType.tree.root.reload(); //加载树数据
			    EosDictType.tree.getRootNode().expand();
			    EosDictType.grid.store.load();  //加载列表数据
    		},
    		click: function(node, e) {
    			EosDictType.selNode = node;
				EosDictType.grid.store.load();
    		}
    	}
	});
	
	EosDictType.tree.on('beforeload', function(node){
    	EosDictType.tree.loader.dataUrl = ctx + '/sysEosDictType!tree.action?nodeid='+node.id;
	});
	
	//验证类型ID是否重复
	EosDictType.validateTypeID = function (arg){
		Ext.Ajax.request({
			url: ctx + '/sysEosDictType!validateTypeID.action', //数据源路径
			params: {'dicttypeid' : arg}, //将字典类型ID作为参数查询
			success: function(response, options){
				var result = Ext.util.JSON.decode(response.responseText);
				if (result != null && result.errMsg != null) {
					if(result.errMsg[0]!=null&&result.errMsg[0]!=''){
						MyExt.Msg.alert(result.errMsg[0]);
						Ext.getCmp('_dicttypeid_0').setValue('');
					}
				} 
			},
			failure: function(response, options){
				MyExt.Msg.alert(i18n.SysEosDicType.false + response.status + "\n" + response.responseText);
			}
		});
	}
	
	//下级业务字典分类-列表
	EosDictType.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/sysEosDictType!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/sysEosDictType!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/sysEosDictType!delete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    storeId:'dicttypeid',
	    tbar:['search','add','delete',{
	    	text:i18n.SysEosDicType.dicmaintain, iconCls:"leafIcon", scope:this , 
			handler: function(){
				if(!$yd.isSelectedRecord(EosDictType.grid)) return; //未选择则返回并提示
				EosDictEntry.win.show();
				EosDictEntry.tree.root.reload();
		    	EosDictEntry.tree.getRootNode().expand();
		    	EosDictEntry.tree.getRootNode().setText(EosDictType.grid.selModel.getSelections()[0].get("dict"));
		    	EosDictEntry.grid.store.load();
			}
	    }],
	    fields: [
	    	{header:i18n.SysEosDicType.TypeCode, dataIndex:'dicttypeid', 	hidden:false, 
	    		editor: {id:'_dicttypeid_0',allowBlank:false,maxLength:60,
	    			listeners : {
	    				change : function(){
	    					EosDictType.validateTypeID(this.getValue()); //验证是否存在重复的类型代码
	    				}
	    			}
	    		},searcher:{xtype:'textfield'}
	    	},
	    	{header:i18n.SysEosDicType.dict, dataIndex:'dict', hidden:false, editor: {allowBlank:false,maxLength:30}},
	    	{header:i18n.SysEosDicType.rank, dataIndex:'rank', hidden:true, editor: {xtype:'hidden'}},
	    	{header:i18n.SysEosDicType.parentid, dataIndex:'parentid', hidden:true, editor: {xtype:'hidden'}},
	    	{header:i18n.SysEosDicType.seqno, dataIndex:'seqno', hidden:true, editor: {xtype:'hidden'}}
	    ],
	    editOrder:['dicttypeid','dict'],
	    searchOrder:['dicttypeid','dict'],
		searchFn : function(searchParam) {
			EosDictType.searchParams = searchParam;
			this.store.load();
		},
		/**
	     * 新增时，解除"类型代码"的只读状态
	     * */
	    beforeAddButtonFn: function(){
	    	Ext.getCmp('_dicttypeid_0').setDisabled(false);
	    	return true;
	    },
	    /**
	     * 编辑时，"类型代码"置为只读状态
	     */
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('_dicttypeid_0').setDisabled(true);
	    },
		 /**
		  * 获取表单数据前，解除"类型代码"的只读状态
	      */
	    beforeGetFormData: function(){
	    	Ext.getCmp('_dicttypeid_0').setDisabled(false);
	    },
	    /**
         * 获取表单数据后，"类型代码"置为只读状态
	     */    
	    afterGetFormData: function(){
	    	Ext.getCmp('_dicttypeid_0').setDisabled(true);
	    },    
		/**
	     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
	     * @param {Object} data 要保存的数据记录，json格式
	     * @return {Boolean} 如果返回fasle将不保存记录
	     */
	    beforeSaveFn: function(data){ 
	    	var selectNode = EosDictType.selNode; //获取当前点击的树节点
			if(selectNode == null || (selectNode.attributes.dicttypeid == null || selectNode.attributes.dicttypeid == '')){ //如果当前没有点击任何节点,或者点击了虚拟根节点
				data.parentid = null;
				data.rank = 0;
				data.seqno = null;
			} else {
				data.parentid = selectNode.attributes.dicttypeid;
				data.rank = parseInt(selectNode.attributes.rank);
				data.seqno = selectNode.attributes.seqno;
			}
	    	return true; 
	    },
		afterSaveSuccessFn: function(result, response, options){
        	this.store.reload();
        	alertSuccess();
        	EosDictType.tree.root.reload();
		    EosDictType.tree.getRootNode().expand();
		},
    	afterDeleteFn: function(){
    		EosDictType.tree.root.reload();
		    EosDictType.tree.getRootNode().expand();
    	}
	});
	
	EosDictType.grid.store.setDefaultSort('dicttypeid', 'asc');

	EosDictType.grid.store.on('beforeload', function() {
		var whereList = []; 
		if(EosDictType.selNode == null || (EosDictType.selNode.attributes.dicttypeid == null || EosDictType.selNode.attributes.dicttypeid == '')){ //如果当前没有点击任何节点,或者点击了虚拟根节点
			var sqlStr = " (parentid = '0' or parentid is null) ";
			whereList.push({sql: sqlStr, compare: Condition.SQL}); //点击"业务字典"虚拟根节点时，执行查询条件传参
		} else {
			whereList.push({propName : 'parentid', propValue : EosDictType.selNode.attributes.dicttypeid, compare:Condition.EQ, stringLike:false}); //点击业务字典节点时，执行查询条件传参
		}
		
		var searchParam = EosDictType.searchParams; //获取查询框条件
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});//传递页面查询表单的查询参数
		}
	    var params = {
	    	start : 0,
			limit : EosDictType.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.EosDictType",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	//TabPanel
	EosDictType.TabPanel = new Ext.Panel({
		layout : 'fit', border : false, activeItem : 0, baseCls: "x-plain",
		items : [{
			layout : 'fit', border : false, height : 28, 
			items : {
				id : '_tabPanel', xtype : "tabpanel", activeTab : 0, enableTabScroll : true, border : false,
				items : [{
					title : i18n.SysEosDicType.lowerTypeInfor,
					border : false,
					layout : "fit",
					items : [EosDictType.grid]
				}]
			}
		}]
	});
	
	
	//页面主框架布局，左：业务字典树，右：多tab列表
	var viewport = new Ext.Viewport({ 
		layout : 'border', 
		border : false,
		items : [{
			region : 'west',
			xtype : 'panel',
			width : 200,
			border : true,
			layout : 'fit',
			items : [EosDictType.tree]
		},{
			id: 'censusPanel',
			region:'center',
			layout:'fit',
			xtype:'panel',
			border:true,
			bodyStyle : "background:#dae7f6;border-style:solid;border-top:1px solid #99bbe8;",
			autoScroll:true,
			items: [EosDictType.TabPanel]
		}]
	});
});