/**
 * 菜单管理信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){

Ext.namespace('AcMenu');                       //定义命名空间
	AcMenu.selNode = null;
	AcMenu.isAdd = false; //记录操作状态（新增true，编辑false）
	
	//系统菜单树
	AcMenu.tree = new Ext.tree.TreePanel({
	    xtype:'treepanel', id:'monthTree', autoScroll:true, split:true, border:false, rootVisible:true, 
	    tbar : new Ext.Toolbar(), plugins : ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/menu!tree.action"
	    }),            
	    root: new Ext.tree.AsyncTreeNode({
	        text:'系统菜单',  id: "null", leaf: false, expanded:true, iconCls : 'chart_organisationIcon'
	    }),
	    listeners: {
	        'click': function(node, e) {
	            AcMenu.selNode = node;
	            AcMenu.grid.store.load();
	        }
	    }
	});
	//树查询参数
	AcMenu.tree.on('beforeload', function(node){
	    AcMenu.tree.loader.dataUrl = ctx + '/menu!tree.action?parentsid=' + (node.id == 'null' ? null : node.id);
	});

	AcMenu.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/menu!pageQuery.action',           //装载列表数据的请求URL
	    saveURL: ctx + '/menu!saveOrUpdate.action',        //保存数据的请求URL
	    deleteURL: ctx + '/menu!delete.action',            //删除数据的请求URL
	    searchFormColNum: 1,
	    storeAutoLoad: true,
	    singleSelect: false,
	    storeId:'menuid',
	    fieldWidth: 250,
	    fields: [{
	            header:'菜单编号', dataIndex:'menuid', hidden:false, 
	            editor: {id:'_menuid_0', maxLength:40, allowBlank:false, vtype:'alphanum2', enableKeyEvents:true,
	            	listeners : {
	            		keyup : function (){
	            			Ext.getCmp("_menucode_0").setValue(this.getValue());
	            		}
	            	}}
	        },{
	            header:'菜单名称', dataIndex:'menuname', 
	            editor:{  maxLength:20, allowBlank:false, vtype:'validChar', enableKeyEvents:true,
	            	listeners: {
	            		keyup : function (){
	            			Ext.getCmp("_menulabel_0").setValue(this.getValue());
	            		}
	            	}}
	        },{
	            header:'菜单显示名称', dataIndex:'menulabel', editor:{ id:'_menulabel_0', maxLength:20, allowBlank:false,vtype:'validChar' }
	        },{
	            header:'菜单代码', dataIndex:'menucode', editor:{ id:'_menucode_0', maxLength:40, vtype:'alphanum2' }
	        },{
	            header:'功能调用入口', dataIndex:'menuaction', width:200, editor:{xtype:'textarea', maxLength:256, id:'_menuaction_0' }
	        },{
	            header:'是否叶子菜单', dataIndex:'isleaf', hidden:true, editor:{ xtype:'hidden', value:'y' }            
	        },{
	            header:'菜单层次', dataIndex:'menulevel', hidden:true, editor:{ xtype:'hidden' }
	        },{
	            header:'父菜单编号', dataIndex:'parentsid', hidden:true, editor: { xtype:'hidden' }
	        },{
	            header:'显示顺序', dataIndex:'displayorder', editor:{ id:'_displayorder_0', xtype:'numberfield', maxLength:2 }
	        },{
	            header:'菜单路径序列', dataIndex:'menuseq', hidden:true, editor: { xtype:'hidden' }
	        },{
	            header:'页面打开方式', dataIndex:'openmode', hidden:true, editor: { xtype:'hidden', value:'comm' }
	        },{
	            header:'子节点数', dataIndex:'subcount', hidden:true, editor: { xtype:'hidden' }
	        },{
	            header:'应用编号', dataIndex:'appid', hidden:true, editor: { id:'_appid_0', xtype:'hidden' }
	        },{
	            header:'功能编号', dataIndex:'funccode', hidden:true, editor: {
					xtype: "SysFunction_SelectWin",id:'_funccode_0',
					hiddenName: "funccode", displayField:"funccode", valueField: "funccode",
					returnField :[{widgetId: '_menuaction_0',propertyName:'funcaction'},
								  {widgetId: '_appid_0',propertyName:'appid'}],
					editable: false 
				}
	        }      
	    ],
	    editOrder:['menuid','menuname','menulabel','menucode','displayorder','funccode','menuaction'],
	    searchOrder: ['menuname', 'menulabel', 'menucode', 'menuaction'],
	    afterShowSaveWin: function(){
	        var parentsid = AcMenu.selNode == null || AcMenu.selNode.id == 'null' ? '' : AcMenu.selNode.id;
	        if(typeof(this.saveForm.find('name', 'parentsid'))!='undefined' && this.saveForm.find('name', 'parentsid').length>0)
	        	this.saveForm.find('name', 'parentsid')[0].setValue(parentsid);
	        AcMenu.grid.saveForm.getForm().findField('_funccode_0').setDisplayValue('','');
	      //自动计算排序号：因为store是根据排序号进行排序的，所以获取store的最后一条数据的排序号+1设为新增数据的默认排序号
	    	var storeCount = this.store.getCount();
	    	if(storeCount>0) storeCount = --storeCount;
	    	if(typeof(this.store.getAt(storeCount))!='undefined' && this.store.getAt(storeCount).data.displayorder!=null){
	    		Ext.getCmp('_displayorder_0').setValue(this.store.getAt(storeCount).data.displayorder+1);
	    	} else {
	    		Ext.getCmp('_displayorder_0').setValue(1);
	    	}
	    },
	    //新增时， 解锁功能编号的写操作状态
	    beforeAddButtonFn: function(){
	    	Ext.getCmp('_menuid_0').setDisabled(false);
	    	//设置操作状态为新增
	    	AcMenu.isAdd = true;
	    	return true;
	    },
	    //编辑时， 锁定功能编号的写操作状态
	    afterShowEditWin: function(record, rowIndex){
	    	AcMenu.isAdd = false;
	    	Ext.getCmp('_menuid_0').setDisabled(true);
	    	AcMenu.grid.saveForm.getForm().findField('_funccode_0').setDisplayValue(record.get('funccode'),record.get('funccode'));
	    },
	    //保存前，解锁功能编号的写操作状态
	    beforeGetFormData: function(){
	    	Ext.getCmp('_menuid_0').setDisabled(false);
	    },
	    //保存后， 锁定功能编号的写操作状态
	    afterGetFormData: function(){
	    	Ext.getCmp('_menuid_0').setDisabled(true);
	    },
	    //保存前，构造默认的数据内容
	    beforeSaveFn: function(data){ 
	    	if(AcMenu.selNode == null || AcMenu.selNode.id == 'null' || AcMenu.selNode.id == '') {
	    		//当前为选择任何菜单节点或选中了虚拟根节点， 默认为添加根级菜单
	    		if(data.parentsid == null || data.parentsid == '') data.parentsid = null;
	    		if(data.menulevel == null || data.menulevel == '') data.menulevel = 1;
	    		if(data.menuseq == null || data.menuseq == '') data.menuseq = '.'+data.menuid + '.'; //菜单路径序列
	    	} else {
	    		//已选择某级菜单节点
	    		if(data.menulevel == null || data.menulevel == '') data.menulevel = parseInt(AcMenu.selNode.attributes.menulevel)+1;
	    		if(data.menuseq == null || data.menuseq == '')     data.menuseq = AcMenu.selNode.attributes.menuseq + data.menuid + '.';
	    	}
	    	if(data.isleaf == null || data.isleaf == '') data.isleaf = 'y'; //新增节点均是叶子节点
	    	if(data.subcount == null || data.subcount == '') data.subcount = 0; //新增节点没有下级节点
	    	return true;
	    },
	    saveFn: function(){
	        //表单验证是否通过
	        var form = AcMenu.grid.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        
	        //获取表单数据前触发函数
	        AcMenu.grid.beforeGetFormData();
	        var data = form.getValues();
	        //获取表单数据后触发函数
	        AcMenu.grid.afterGetFormData();
	        
	        //调用保存前触发函数，如果返回fasle将不保存记录
	        if(!AcMenu.grid.beforeSaveFn(data)) return;
	        
	        if(self.loadMask)   self.loadMask.show();
	        var cfg = {
	            scope: AcMenu.grid, url: AcMenu.grid.saveURL, jsonData: data,
	            params: {'isAdd':AcMenu.isAdd},
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    this.afterSaveSuccessFn(result, response, options);
	                } else {
	                    this.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    },
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        AcMenu.tree.root.reload();
			AcMenu.tree.getRootNode().expand();
	    },
	    afterSaveFailFn: function(result, response, options){
	    	AcMenu.grid.store.reload();
	        alertFail(result.errMsg);
	        if(AcMenu.isAdd==true)
	        Ext.getCmp('_menuid_0').setDisabled(false);
	    },
	    afterDeleteFn: function(){
    		AcMenu.tree.root.reload();
			AcMenu.tree.getRootNode().expand();
    	}
	});
	
	AcMenu.grid.store.setDefaultSort('displayorder', 'asc');
	
	//在store.load前，组装查询参数
	AcMenu.grid.store.on('beforeload', function(){
	    var whereList = [];
		//查询表单中的参数的组装
	    if(AcMenu.grid.searchForm != null){
	        var searchParam = AcMenu.grid.searchForm.getForm().getValues();
	        searchParam = MyJson.deleteBlankProp(searchParam);
	        for(prop in searchParam){
	            whereList.push({propName:prop, compare:Condition.EQ, propValue:searchParam[ prop ]});
	        }
	    }
	    if(AcMenu.selNode == null || AcMenu.selNode.id == 'null')  whereList.push({propName:'parentsid', compare:Condition.IS_NULL});
	    else    whereList.push({propName:'parentsid', compare:Condition.EQ, propValue:AcMenu.selNode.id, stringLike:false});
	    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});

	//页面主框架
	var viewport = new Ext.Viewport({ 
	    layout:"border", border: false,
	    items:[{
	        region:'west', layout: "fit", autoScroll:true, width:200, minSize:200, maxSize:500, split: true, bodyBorder:false, items: AcMenu.tree
	    },{
	        region:'center', layout:'fit', bodyBorder: false, items:AcMenu.grid
	    }]
	});

});