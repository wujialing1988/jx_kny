/**
 * 
 * 人员多选窗口控件
 * 控件使用方法：
 * 1.在页面jsp中引入头文件:<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
 * 2.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/employee/EmployeeMultiSelect.js"></script> 
 * 3.保存人员信息时，需要重写 EmployeeMultiSelect.saveFn = function()方法
 */


Ext.onReady(function(){

	Ext.namespace("EmployeeMultiSelect");			// 定义命名空间
	
	EmployeeMultiSelect.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	
	// 【保存】按钮触发的函数处理
	EmployeeMultiSelect.saveFn = function() {
		var records = Ext.getCmp('PersonSelect_Grid').getValues();
		if (null == records) {
			return;
		}
		var msg = "当前选中人员为：<br>";
		msg += "1.&nbsp;" + records[0].get('empname') + "[" + records[0].get('empid') + "]<br>";
		for (var i = 1; i < records.length; i++) {
			msg += (i + 1) + ".&nbsp;" + records[i].get('empname') + "[" + records[i].get('empid') + "]<br>";
		}
		msg += "请自定义保存方法！";
		MyExt.Msg.alert(msg);
	}
	
	// 人员列表数据容器
	EmployeeMultiSelect.userstore = new Ext.data.JsonStore({
		url : ctx + "/omEmployeeSelect!pageList.action",
		autoLoad : true,
		root : "root",
		remoteSort : true,
		totalProperty : "totalProperty",
		fields : ["empid", "empcode", "empname", "gender","orgname","orgid","payId"]
	});
	
	/** ****************************************************************************************** */
	
	//机构选择树
	EmployeeMultiSelect.OrgTree = Ext.extend(Ext.tree.TreePanel, {
		parentObj:null,
		constructor : function() {
			EmployeeMultiSelect.OrgTree.superclass.constructor.call(this, {
				loader : new Ext.tree.TreeLoader({
					dataUrl : ctx + '/omOrganizationSelect!tree.action'
				}),
				root : new Ext.tree.AsyncTreeNode({
					text : systemOrgname,
					id : systemOrgid,
					leaf : false,
					orgseq : ''
				}),
				listeners : {
					click : function(node, e) {
						this.parentObj.grid.store.baseParams.start = 0;
						this.parentObj.grid.store.baseParams.limit = this.parentObj.grid.bbar.pageSize;
						this.parentObj.grid.store.baseParams.orgid =  node.id;
						this.parentObj.grid.store.baseParams.orgcode = node.attributes["orgcode"];
						this.parentObj.grid.store.baseParams.orgseq = node.attributes["orgseq"];
						var empname = this.parentObj.grid.getTopToolbar().get(1).getValue();
						this.parentObj.grid.store.baseParams.emp = empname;
						this.parentObj.grid.store.load();
					},
					render : function() {		        	
		    			this.root.reload();
					    this.getRootNode().expand();
		    		},
					beforeload: function(node){
						this.loader.dataUrl = ctx + '/omOrganizationSelect!tree.action?parentIDX=' + node.id ;
					}
				},			
				rootVisible : true
			});
		}
	});	
		
	/** ****************************************************************************************** */
	
	//分页工具
	EmployeeMultiSelect.pagingToolbar = Ext.yunda.createPagingToolbar({store: EmployeeMultiSelect.userstore});
	//多选列表
	EmployeeMultiSelect.selModel = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//人员列表
	EmployeeMultiSelect.UserList = Ext.extend(Ext.grid.GridPanel, {
		id:'PersonSelect_Grid',
		parentObj:null,
		constructor : function() {
			EmployeeMultiSelect.UserList.superclass.constructor.call(this, {
				selModel: EmployeeMultiSelect.selModel,
				viewConfig : {
					forceFit : true
				},
				loadMask: {msg: "正在处理，请稍侯..."},
				// 偶数行变色
				stripeRows : true,	
				// 工具栏
				tbar : [{
						xtype:"label",
						text:"人员姓名："
					},{	            
		                xtype:"textfield",								                
		                name : "empName",
				        width: 80
					},{
						text : "搜索",
						iconCls : "searchIcon",
						handler : function(){
							var empname = this.getTopToolbar().get(1).getValue();
							this.store.baseParams.emp = empname;
							this.store.load();
						},
						scope : this
					},{
						text : "重置",
						iconCls : "resetIcon",
						handler : function(){
							this.store.baseParams.emp = "";
							this.store.load();
							this.getTopToolbar().get(1).setValue("");
						},
						scope : this
					},{
						text : "保存",
						iconCls : "saveIcon",
						handler : function(){EmployeeMultiSelect.saveFn()},
						scope : this
					}],
				colModel : new Ext.grid.ColumnModel([
					EmployeeMultiSelect.selModel,
					new Ext.grid.RowNumberer(), {
						sortable : true,
						header : "名称",
						dataIndex : "empname"
					}, {
						sortable : true,
						header : "人员代码",
						dataIndex : "empcode"
					}, {
						sortable : true,
						header : "empid",
						dataIndex : "empid",
						hidden : true
					}, {
						sortable : true,
						header : "orgid",
						dataIndex : "orgid",
						hidden : true
					}, {
						sortable : true,
						header : "orgname",
						dataIndex : "orgname",
						hidden : true
					}, {
						sortable : true,
						header : "payId",
						dataIndex : "payId",
						hidden : true
					}
				]),
				store : EmployeeMultiSelect.userstore,
				bbar: EmployeeMultiSelect.pagingToolbar
			});
		},
		getValues : function() {
			var sm = this.getSelectionModel();
			if (sm.getCount() < 1) {
				MyExt.Msg.alert("尚未选择一条记录！");
				return;
			}
			return sm.getSelections();
		}
	});
	
	/** ****************************************************************************************** */
	//弹出窗口
	EmployeeMultiSelect.UserSelectWin = Ext.extend(Ext.Window, {
		tree : new EmployeeMultiSelect.OrgTree(),
		grid : new EmployeeMultiSelect.UserList(),
		parentObj:null,
		modal:true,
		// private
	    beforeShow : function(){
	    	this.grid.parentObj = this;
	    	this.tree.parentObj = this;
	    	//Ext.Window源代码
	        delete this.el.lastXY;
	        delete this.el.lastLT;
	        if(this.x === undefined || this.y === undefined){
	            var xy = this.el.getAlignToXY(this.container, 'c-c');
	            var pos = this.el.translatePoints(xy[0], xy[1]);
	            this.x = this.x === undefined? pos.left : this.x;
	            this.y = this.y === undefined? pos.top : this.y;
	        }
	        this.el.setLeftTop(this.x, this.y);
	
	        if(this.expandOnShow){
	            this.expand(false);
	        }
	
	        if(this.modal){
	            Ext.getBody().addClass('x-body-masked');
	            this.mask.setSize(Ext.lib.Dom.getViewWidth(true), Ext.lib.Dom.getViewHeight(true));
	            this.mask.show();
	        }
	    },
		constructor : function() {		
			EmployeeMultiSelect.UserSelectWin.superclass.constructor.call(this, {
				title : "选择人员",
				width : 530,
				height : 305,			
				plain : true,
				closeAction : "hide",
				layout : "border",
				items : [new Ext.Panel({
							width : 160,
							minSize : 160,
							maxSize : 280,
							region : "west",
							items : this.tree,
							autoScroll : true
						}), new Ext.Panel({
								region : "center",
								layout : "fit",
								items : [this.grid]
							})]
	
					});
		},
		close : function() {
			this.hide();
		}
	});
	
	EmployeeMultiSelect.win = new EmployeeMultiSelect.UserSelectWin();
	
});