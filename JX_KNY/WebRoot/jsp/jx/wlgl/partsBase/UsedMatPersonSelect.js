Ext.onReady(function(){

	Ext.namespace("UsedMatPersonSelect");
	
	UsedMatPersonSelect.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	
	// 【保存】按钮触发的函数处理
	UsedMatPersonSelect.saveFn = function() {
		var records = Ext.getCmp('PersonSelect_Grid').getValues();
		if (null == records) {
			return;
		}
		var datas = [];
		for (var i = 0; i < records.length; i++) {
			var data = {};
			data.usedMatIdx = UsedMatPerson.usedMatIdx;
			data.empId = records[i].get('empid');
			data.empCode = records[i].get('empcode');
			data.empName = records[i].get('empname');
			datas.push(data);
		}
		UsedMatPersonSelect.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/usedMatPerson!save.action',
            jsonData: datas,
            success: function(response, options){
              	UsedMatPersonSelect.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    UsedMatPerson.grid.store.reload();
                    UsedMatPersonSelect.win.hide();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                UsedMatPersonSelect.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	
	// 人员列表数据容器
	UsedMatPersonSelect.userstore = new Ext.data.JsonStore({
		url : ctx + "/omEmployeeSelect!pageList.action",
		autoLoad : true,
		root : "root",
		remoteSort : true,
		totalProperty : "totalProperty",
		fields : ["empid", "empcode", "empname", "gender","orgname","orgid","payId"]
	});
	
	/** ****************************************************************************************** */
	
	//机构选择树
	UsedMatPersonSelect.OrgTree = Ext.extend(Ext.tree.TreePanel, {
		parentObj:null,
		constructor : function() {
			UsedMatPersonSelect.OrgTree.superclass.constructor.call(this, {
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
	UsedMatPersonSelect.pagingToolbar = Ext.yunda.createPagingToolbar({store: UsedMatPersonSelect.userstore});
	//多选列表
	UsedMatPersonSelect.selModel = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//人员列表
	UsedMatPersonSelect.UserList = Ext.extend(Ext.grid.GridPanel, {
		id:'PersonSelect_Grid',
		parentObj:null,
		constructor : function() {
			UsedMatPersonSelect.UserList.superclass.constructor.call(this, {
				selModel: UsedMatPersonSelect.selModel,
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
						handler : UsedMatPersonSelect.saveFn,
						scope : this
					}],
				colModel : new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(),
					UsedMatPersonSelect.selModel,{
						sortable : true,
						header : "名称",
						title : "双击该行记录选择人员",
						dataIndex : "empname"
					}, {
						sortable : true,
						header : "人员代码",
						title : "双击该行记录选择人员",
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
				store : UsedMatPersonSelect.userstore,
				bbar: UsedMatPersonSelect.pagingToolbar/*,
				listeners:{
					//双击选择人员
					"rowdblclick": {
		            	fn: function(grid, idx, e){
		            		var r = grid.store.getAt(idx);
		            		MyExt.Msg.alert(r.data.empname);
		            		this.parentObj.parentObj.setValue(r);
		            		this.parentObj.parentObj.setReturnValue(r);
		            		this.parentObj.parentObj.fireEvent('select', r);
		            		this.parentObj.close();
		            	}
					}
				}*/
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
	UsedMatPersonSelect.UserSelectWin = Ext.extend(Ext.Window, {
		tree : new UsedMatPersonSelect.OrgTree(),
		grid : new UsedMatPersonSelect.UserList(),
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
			UsedMatPersonSelect.UserSelectWin.superclass.constructor.call(this, {
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
	
	UsedMatPersonSelect.win = new UsedMatPersonSelect.UserSelectWin();
});