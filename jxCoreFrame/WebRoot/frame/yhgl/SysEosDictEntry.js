/** 业务字典项页面 */
Ext.onReady(function(){
	Ext.namespace('EosDictEntry');                       //业务字典项命名空间
	EosDictEntry.searchParams = {};
	EosDictEntry.selNode = null; //记录当前选中的节点
	
	//业务字典项树
	EosDictEntry.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/sysEosDictEntry!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : '字典类型',
			disabled : false,
			id : 'ROOT_0',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
    	listeners: {
    		click: function(node, e) {
    			var dicttypeid = EosDictType.grid.selModel.getSelections()[0].get("dicttypeid");
    			EosDictEntry.selNode = node;
   				EosDictEntry.grid.store.load();
				EosDictEntry.grid.store.load();
    		}
    	}
	});
	
	EosDictEntry.tree.on('beforeload', function(node){
		var dictid = "";
		if(typeof(node.attributes.dictid)=='undefined') dictid = '';
		else dictid = node.attributes.dictid;
		if(typeof(EosDictType.grid)!='undefined'){
			var dicttypeid = EosDictType.grid.selModel.getSelections()[0].get("dicttypeid");
			EosDictEntry.tree.loader.dataUrl = ctx + '/sysEosDictEntry!tree.action?dicttypeid='+dicttypeid+'&dictid='+dictid;
		}
	});
	
	//验证类型ID是否重复
	EosDictEntry.validateEntryID = function (dicttypeid, dictid){
		Ext.Ajax.request({
			url: ctx + '/sysEosDictEntry!validateEntryID.action', //数据源路径
			params: {'dicttypeid' : dicttypeid, 'dictid' : dictid}, //将字典ID作为参数查询
			success: function(response, options){
				var result = Ext.util.JSON.decode(response.responseText);
				if (result != null && result.errMsg != null) {
					if(result.errMsg[0]!=null&&result.errMsg[0]!=''){
						MyExt.Msg.alert(result.errMsg[0]);
						Ext.getCmp('_dictid_1').setValue('');
					}
				} 
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	//业务字典项-列表
	EosDictEntry.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/sysEosDictEntry!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/sysEosDictEntry!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/sysEosDictEntry!delete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    storeId:'tempdictid',
	    tbar:['search','add','delete'],
	    fields: [
	    	{header:'业务字典分类', dataIndex:'tempdicttypeid', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'字典项代码',  dataIndex:'tempdictid', hidden:false, 
	    		editor: {id:'_dictid_1',maxLength:25,allowBlank:false,
	    			listeners : {
	    				change : function(){
	    					var dicttypeid = EosDictType.grid.selModel.getSelections()[0].get("dicttypeid");
	    					EosDictEntry.validateEntryID(dicttypeid, this.getValue()); //验证是否存在重复的代码
	    				}
	    			}
	    		},searcher:{xtype:'textfield'}
	    	},
	    	{header:'字典项名称', dataIndex:'dictname', hidden:false, editor: {maxLength:25,allowBlank:false}},
	    	{header:'字典项状态', dataIndex:'status', hidden:false, 
	    		editor: {id:'_status_1',xtype: 'combo',dataColumn :"status", hiddenName: "status",  mode: 'local' ,valueField: "value", displayField: "text", triggerAction: "all",   		 	
            		editable: false, forceSelection: true, store:[["1","启用"],["0","禁用"]]
	    		},renderer:function(v){
	    			if(v == '1'){return '启用';}
	    			else if(v == '0'){return '禁用';}
	    			else {return '禁用';}
				}
	    	},
	    	{header:'排序号', dataIndex:'sortno', hidden:false, editor: {maxLength:3}},
	    	{header:'层次', dataIndex:'rank', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'上级字典项ID', dataIndex:'parentid', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'字典项Seq', dataIndex:'seqno', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'配置项1', dataIndex:'filter1', hidden:false, editor: {maxLength:25,allowBlank:true}},
	    	{header:'配置项2', dataIndex:'filter2', hidden:false, editor: {maxLength:25,allowBlank:true}}
	    ],
	    editOrder:['tempdictid','dictname','sortno','status'],
	    searchOrder:['dictid','dictname'],
		searchFn : function(searchParam) {
			EosDictEntry.searchParams = searchParam;
			this.store.load();
		},
			/**
	     * 新增时，解除"类型代码"的只读状态
	     * */
	    beforeAddButtonFn: function(){
	    	Ext.getCmp('_dictid_1').setDisabled(false);
	    	return true;
	    },
	    afterShowSaveWin: function(){
	    	Ext.getCmp('_status_1').setValue(1);
	    }, 
	    /**
	     * 编辑时，"类型代码"置为只读状态
	     */
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('_dictid_1').setDisabled(true);
	    },
		 /**
		  * 获取表单数据前，解除"类型代码"的只读状态
	      */
	    beforeGetFormData: function(){
	    	Ext.getCmp('_dictid_1').setDisabled(false);
	    },
	    /**
         * 获取表单数据后，"类型代码"置为只读状态
	     */    
	    afterGetFormData: function(){
	    	Ext.getCmp('_dictid_1').setDisabled(true);
	    },    
		/**
	     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
	     * @param {Object} data 要保存的数据记录，json格式
	     * @return {Boolean} 如果返回fasle将不保存记录
	     */
	    beforeSaveFn: function(data){ 
	    	if(data.tempdicttypeid == null||data.tempdicttypeid=='') data.tempdicttypeid = EosDictType.grid.selModel.getSelections()[0].get("dicttypeid"); //分类ID
	    	var selectNode = EosDictEntry.tree.getSelectionModel().getSelectedNode(); //获得用户点击的业务字典项节点对象
	    	if(selectNode == null||selectNode.id=='ROOT_0'){ //用户未选择节点或选择了根， 默认增加在根下
		    	data.rank = 1;
		    	data.seqno = '.'+data.tempdictid+'.';
		    	data.parentid = '';
	    	} else {
	    		data.parentid = selectNode.attributes.dictid; 
	    		data.rank = parseInt(selectNode.attributes.rank)+1;
	    		data.seqno = selectNode.attributes.seqno+data.tempdictid+'.';
	    	}
	    	return true; 
	    },
		afterSaveSuccessFn: function(result, response, options){
        	this.store.reload();
        	alertSuccess();
        	this.saveWin.hide();
        	EosDictEntry.tree.root.reload();
		    EosDictEntry.tree.getRootNode().expand();
		},
		deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        if(this.saveWin)    this.saveWin.hide();
	        if(this.searchWin)  this.searchWin.hide();        
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        var selectType = EosDictType.grid.selModel.getSelections()[0].get("dicttypeid"); //分类ID
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        $yd.confirmAndDelete({
	            scope: this, url: this.deleteURL, params: {ids: $yd.getSelectedIdx(this, this.storeId), 'dicttypeid':selectType}
	        });
	    },
	    afterDeleteFn: function(){
    		EosDictEntry.tree.root.reload();
		    EosDictEntry.tree.getRootNode().expand();
    	}
	});
	
	EosDictEntry.grid.store.setDefaultSort('sortno', 'asc');

	EosDictEntry.grid.store.on('beforeload', function() {
		var searchParam = EosDictEntry.searchParams;
		var whereList = [];
		whereList.push({propName : 'dicttypeid', propValue : EosDictType.grid.selModel.getSelections()[0].get("dicttypeid"), compare:Condition.EQ, stringLike:false});//传递页面查询表单的查询参数
		if(EosDictEntry.selNode == null ||(EosDictEntry.selNode.attributes.dictid == null || EosDictEntry.selNode.attributes.dictid == '')){ 
			//未点击任何节点或者点击了虚拟根
			whereList.push({propName : 'parentid', propValue : null, compare:Condition.IS_NULL});//传递页面查询表单的查询参数[点击虚拟根节点]
		} else {
			whereList.push({propName : 'parentid', propValue : EosDictEntry.selNode.attributes.dictid, compare:Condition.EQ, stringLike:false});//传递页面查询表单的查询参数[点击业务字典项节点]
		}
		
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});//传递页面查询表单的查询参数
		}
	    var params = {
	    	start : 0,
			limit : EosDictEntry.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.EosDictEntry",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	//业务字典项维护窗口
	EosDictEntry.win = new Ext.Window({
		title: "字典维护", maximizable: true, width: 700, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',
		layout : 'border',
		items : [ {
	        title : '<span style="font-weight:normal">字典维护</span>',
	        iconCls : 'chart_organisationIcon',
	        tools : [ {
	            id : 'refresh',
	            handler : function() {
	                EosDictEntry.tree.root.reload();
	                EosDictEntry.tree.getRootNode().expand();
	            }
	        } ],
	        collapsible : true,
	        width : 180,
	        split : true,
	        region : 'west',
	        bodyBorder: false,
	        autoScroll : true,
	        items : [EosDictEntry.tree]
	    }, {
	        region : 'center',
	        layout : 'fit',
	        bodyBorder: false,
	        items : [EosDictEntry.grid]
	    }]
	});
	
});