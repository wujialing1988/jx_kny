/**
 * 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TecCardWS');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	TecCardWS.qcItems = [];
	TecCardWS.treePath = "###";
	TecCardWS.tecCardIDX = "###";
	TecCardWS.wsParentIDX = "ROOT_0";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自动设置【工艺卡编号】字段值
	TecCardWS.setTecNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_WS_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					Ext.getCmp("wsNo_a").setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	// 手动排序 
    TecCardWS.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/tecCardWS!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义工序表格开始 ************** */
	TecCardWS.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/tecCardWS!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/tecCardWS!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/tecCardWS!logicDelete.action',            //删除数据的请求URL
	    tbar: ['add', 'delete','->', {
			text:'置顶', iconCls:'moveTopIcon', handler: function() {
				TecCardWS.moveOrder(TecCardWS.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:'上移', iconCls:'moveUpIcon', handler: function() {
				TecCardWS.moveOrder(TecCardWS.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:'下移', iconCls:'moveDownIcon', handler: function() {
				TecCardWS.moveOrder(TecCardWS.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:'置底', iconCls:'moveBottomIcon', handler: function() {
				TecCardWS.moveOrder(TecCardWS.grid, ORDER_TYPE_BOT);
			}
		}],
	    storeAutoLoad: false,
	    title: '工序列表',
		saveForm: TecCardWS.saveForm,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, width:10, editor:{ xtype:'numberfield', id:'seqNo_a', maxLength:3, disabled: true }
		},{
			header:'工序编号', dataIndex:'wsNo', width:15, editor:{  maxLength:30, id: "wsNo_a" }
		},{
			header:'工序名称', dataIndex:'wsName', width:20, editor:{  maxLength:50, allowBlank: false }
		},{
			header:'工序描述', dataIndex:'wsDesc', width:55, editor:{  maxLength:500, xtype: "textarea" }
		},{
			header:'上级工序主键', dataIndex:'wsParentIDX', hidden:true, editor:{  maxLength:50, id: "wsParentIDX_a"}
		},{
			header:'检修工艺卡主键', dataIndex:'tecCardIDX', hidden:true, editor:{  maxLength:50, id: "tecCardIDX_a" }
		}],
		beforeAddButtonFn: function(){
			var sm = TecCardWS.grid.getSelectionModel();
			// 设置一个临时变量，用于记录当前以选中的第一条记录的“排序号”，用于实现在选择记录只取插入新记录的功能
			if (sm.getCount() > 0) {
				TecCardWS.tempSeqNo = sm.getSelected().get('seqNo');
			} else {
				TecCardWS.tempSeqNo = undefined;
			}
			return true;   	
	    },
		// 打开新增窗口后的页面初始化方法
		afterAddButtonFn: function(){
			// 设置“检修工艺卡主键”
			Ext.getCmp('tecCardIDX_a').setValue(TecCardWS.tecCardIDX);
			// 设置“上级工序主键”
			Ext.getCmp('wsParentIDX_a').setValue(TecCardWS.wsParentIDX);
			// 自动生成“工序编号”
			TecCardWS.setTecNo();
			// 自动设置顺序号
			if (!TecCardWS.tempSeqNo) {
				Ext.getCmp('seqNo_a').setValue(TecCardWS.grid.store.getCount());
			} else {
				Ext.getCmp('seqNo_a').setValue(TecCardWS.tempSeqNo);
			}
		},
		// 保存成功后的页面重置方法
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 重新加载树
	        TecCardWS.tree.root.reload();
			TecCardWS.tree.expandPath(TecCardWS.treePath); // 刷新并展开树到当前编辑的节点
	    },
	    afterDeleteFn: function(){ 
	        // 重新加载树
	        TecCardWS.tree.root.reload();
			TecCardWS.tree.expandPath(TecCardWS.treePath); // 刷新并展开树到当前编辑的节点
	    }
	});
	// 设置默认排序
	TecCardWS.grid.store.setDefaultSort('seqNo', 'ASC');
	TecCardWS.grid.store.on('beforeload', function(){
		var searchParams = {
			tecCardIDX: TecCardWS.tecCardIDX,
			wsParentIDX: TecCardWS.wsParentIDX
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义工序表格结束 ************** */
	
	/** ************** 定义工序树开始 ************** */
	TecCardWS.tree = new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/tecCardWS!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : false,
	    useArrows : true,
	    border : false,
	    listeners: {
	        click: function(node, e) {
				TecCardWS.wsParentIDX = node.id;
	        	TecCardWS.treePath = node.getPath();
	        	// 重新加载【配件检修工序】列表
	        	TecCardWS.grid.store.load();
	        	// 设置工序列表表格的title
	        	TecCardWS.grid.setTitle(node.text + "&nbsp;-&nbsp;下级工序");
	        }
	    }    
	});
	TecCardWS.tree.on('beforeload', function(node){
	    TecCardWS.tree.loader.dataUrl = ctx + '/tecCardWS!tree.action?parentIDX=' + node.id + '&tecCardIDX=' + TecCardWS.tecCardIDX;
	});
	/** ************** 定义工序树结束 ************** */
	
});